package org.arch.oms.biz;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.arch.framework.beans.Response;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.Constant;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.RedisDistributeLock;
import org.arch.oms.common.dto.LockExecuteResult;
import org.arch.oms.common.dto.OrderInfoSearchDto;
import org.arch.oms.common.enums.OrderInfoQueryType;
import org.arch.oms.common.enums.OrderInvoiceTyp;
import org.arch.oms.common.request.OrderInfoManagerQueryRequest;
import org.arch.oms.common.request.OrderInfoQueryRequest;
import org.arch.oms.common.request.OrderInfoUserQueryRequest;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.common.request.PayOrderByCardRequest;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.PageVo;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.manager.OrderCreateManager;
import org.arch.oms.manager.OrderReaderHandler;
import org.arch.oms.manager.UserHelper;
import org.arch.oms.rest.OrderMasterRest;
import org.arch.oms.service.OrderMasterService;
import org.arch.oms.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

/**
 *
 * @author junboXiang
 * @version V1.0
 * 2021-06-24
 */
@Slf4j
@RestController
@RequestMapping("/oms/order")
public class OrderMasterRestBiz implements OrderMasterRest {

    @Autowired
    private UserHelper userHelper;
    @Autowired
    private OrderCreateManager orderCreateManager;
    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private RedisDistributeLock lock;

    public static void main(String[] args) {
        OrderSaveRequest orderSaveRequest = new OrderSaveRequest();
        orderSaveRequest.setAddressId(1L);
        OrderSaveRequest.InvoiceInfo invoiceInfo = new OrderSaveRequest.InvoiceInfo();
        invoiceInfo.setInvoiceDetail("test");
        invoiceInfo.setInvoiceEmail("test");
        invoiceInfo.setInvoiceNo("test");
        invoiceInfo.setInvoiceTyp(OrderInvoiceTyp.ELECTRONIC_INVOICE.getValue());
        invoiceInfo.setRemark("test");
        orderSaveRequest.setInvoiceInfo(invoiceInfo);
        OrderSaveRequest.ProductSku productSku = new OrderSaveRequest.ProductSku();
        productSku.setQuantity(BigDecimal.ONE);
        productSku.setSkuCode("CTNO_TEST2118973563000000121189735700000004");
        OrderSaveRequest.ProductSku productSku1 = new OrderSaveRequest.ProductSku();
        productSku1.setQuantity(BigDecimal.TEN);
        productSku1.setSkuCode("CTNO_TEST2118973563000000121189735700000003");
        orderSaveRequest.setProductSku(Lists.newArrayList(productSku, productSku1));
        System.out.println(JSONObject.toJSONString(orderSaveRequest));
    }

    @Override
    public Response<OrderInfoVo> save(@RequestBody OrderSaveRequest request) {
        Long appId = userHelper.getAppId();
        Long accountId = userHelper.getAccountId();
        final int maxProductSize = 10;
        if (ObjectUtils.isNotEmpty(request.getProductSku()) && request.getProductSku().size() > maxProductSize) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode(MessageFormat.format("同一订单最多提交{0}个商品", maxProductSize)));
        }
        // redisson 加锁 事务执行, 同一个用户同时只能提交一单
        LockExecuteResult<OrderSaveDto> executeResult = lock.lock(Constant.CREATE_ORDER_REDIS_LOCK_PREFIX + accountId, () -> {
            OrderSaveDto orderSaveDTO = orderCreateManager.buildOrderInfo(request, userHelper.getTokenInfo(), appId);
            // 订单和订单附属信息 入库
            orderMasterService.saveOrderInfo(orderSaveDTO);
            return orderSaveDTO;
        });
        if (!executeResult.isLockSuccess()) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("请不要重复提交"));
        }
        return Response.success(orderCreateManager.convertOrderSaveDtoToOrderInfoVo(executeResult.getResult()));
    }

    @Override
    public Response<OrderInfoVo> queryOrder(@RequestBody OrderInfoQueryRequest request) {
        ValidatorUtil.checkNull(userHelper.getAccountId(), "用户Id不能为空");
        ValidatorUtil.checkNull(request.getOrderId(), "订单id不能为空");
        List<OrderInfoVo> orderInfoVos = OrderReaderHandler.ORDER_READER_HANDLER_MAP.get(OrderInfoQueryType.DB.getValue()).queryListOrder(userHelper.getAccountId(), Lists.newArrayList(request.getOrderId()), request.getOrderSectionRequest());
        return Response.success(ObjectUtils.isEmpty(orderInfoVos) ? null : orderInfoVos.get(0));
    }

    @Override
    public Response<Boolean> payOrderByCard(@RequestBody PayOrderByCardRequest request) {
        ValidatorUtil.checkNull(request, "非法参数");
        ValidatorUtil.checkNull(request.getOrderId(), "订单id不能为空");
        ValidatorUtil.checkNull(request.getId(), "卡号信息不能为空");
        // todo 获取订单、卡号并调用支付
        return null;
    }

    @Override
    public Response<PageVo<List<OrderInfoVo>>> queryOrderPageList(@RequestBody OrderInfoUserQueryRequest request) {
        OrderInfoSearchDto orderInfoSearchDto = new OrderInfoSearchDto();
        orderInfoSearchDto.setAppId(userHelper.getAppId());
        orderInfoSearchDto.setAccountId(userHelper.getAccountId());
        orderInfoSearchDto.setPageInfo(request.getPageInfo());
        orderInfoSearchDto.setOrderNoList(request.getOrderIdList());
        orderInfoSearchDto.setOrderSection(request.getOrderSection());
        return Response.success(OrderReaderHandler.ORDER_READER_HANDLER_MAP.get(OrderInfoQueryType.DB.getValue()).queryOrderPageList(orderInfoSearchDto));
    }

    @Override
    public Response<List<OrderInfoVo>> queryListOrderByManager(OrderInfoManagerQueryRequest request) {
        ValidatorUtil.checkNull(request.getOrderIdList(), "订单id不能为空");
        return Response.success(OrderReaderHandler.ORDER_READER_HANDLER_MAP.get(OrderInfoQueryType.DB.getValue()).queryListOrder(request.getAccountId(), request.getOrderIdList(), request.getOrderSection()));
    }

    @Override
    public Response<PageVo<List<OrderInfoVo>>> queryOrderPageListByManager(@RequestBody OrderInfoManagerQueryRequest request) {
        ValidatorUtil.checkNull(request.getStoreNo(), "请填写店编");
        OrderInfoSearchDto orderInfoSearchDto = new OrderInfoSearchDto();
        orderInfoSearchDto.setAccountId(request.getAccountId());
        orderInfoSearchDto.setPageInfo(request.getPageInfo());
        orderInfoSearchDto.setOrderNoList(request.getOrderIdList());
        orderInfoSearchDto.setOrderSection(request.getOrderSection());
        orderInfoSearchDto.setStoreNo(request.getStoreNo());
        return Response.success(OrderReaderHandler.ORDER_READER_HANDLER_MAP.get(OrderInfoQueryType.DB.getValue()).queryOrderPageList(orderInfoSearchDto));
    }

}
