package org.arch.oms.biz;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.Constant;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.RedisDistributeLock;
import org.arch.oms.common.dto.OrderInfoSearchDto;
import org.arch.oms.common.enums.OrderInfoQueryType;
import org.arch.oms.common.request.OrderInfoManagerQueryRequest;
import org.arch.oms.common.request.OrderInfoQueryRequest;
import org.arch.oms.common.request.OrderInfoUserQueryRequest;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.PageVo;
import org.arch.oms.dto.LockExecuteResult;
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


    @Override
    public Boolean save(@RequestBody OrderSaveRequest request) {
        Long appId = userHelper.getAppId();
        Long userId = userHelper.getUserId();
        final int maxProductSize = 30;
        if (ObjectUtils.isNotEmpty(request.getProductNoList()) && request.getProductNoList().size() > maxProductSize) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("同一订单最多提交30个商品"));
        }
        // redisson 加锁 事务执行, 同一个用户同时只能提交一单
        LockExecuteResult<String> executeResult = lock.lockByDbTransactional(Constant.CREATE_ORDER_REDIS_LOCK_PREFIX + userId, () -> {
            OrderSaveDto orderSaveDTO = orderCreateManager.buildOrderInfo(request, userHelper.getUserId(), userHelper.getUserName(), appId);
            // 订单和订单附属信息 入库
            orderMasterService.saveOrderInfo(orderSaveDTO);
            return null;
        });
        if (!executeResult.isLockSuccess()) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("请不要重复提交"));
        }
        return Boolean.TRUE;
    }

    @Override
    public List<OrderInfoVo> queryListOrder(@RequestBody OrderInfoQueryRequest request) {
        ValidatorUtil.checkNull(request.getUserId(), "用户Id不能为空");
        ValidatorUtil.checkNull(request.getOrderIds(), "订单列表不能为空");
        return OrderReaderHandler.ORDER_READER_HANDLER_MAP.get(OrderInfoQueryType.DB.getValue()).queryListOrder(request.getUserId(), request.getOrderIds(), request.getOrderSectionRequest());
    }

    @Override
    public PageVo<List<OrderInfoVo>> queryOrderPageList(@RequestBody OrderInfoUserQueryRequest request) {
        OrderInfoSearchDto orderInfoSearchDto = new OrderInfoSearchDto();
        orderInfoSearchDto.setAppId(userHelper.getAppId());
        orderInfoSearchDto.setUserId(userHelper.getUserId());
        orderInfoSearchDto.setPageInfo(request.getPageInfo());
        orderInfoSearchDto.setOrderNoList(request.getOrderIdList());
        orderInfoSearchDto.setOrderSection(request.getOrderSection());
        return OrderReaderHandler.ORDER_READER_HANDLER_MAP.get(OrderInfoQueryType.DB.getValue()).queryOrderPageList(orderInfoSearchDto);
    }

    @Override
    public PageVo<List<OrderInfoVo>> queryOrderPageListByManager(@RequestBody OrderInfoManagerQueryRequest request) {
        ValidatorUtil.checkNull(request.getStoreNo(), "请填写店编");
        OrderInfoSearchDto orderInfoSearchDto = new OrderInfoSearchDto();
        orderInfoSearchDto.setUserId(request.getUserId());
        orderInfoSearchDto.setPageInfo(request.getPageInfo());
        orderInfoSearchDto.setOrderNoList(request.getOrderIdList());
        orderInfoSearchDto.setOrderSection(request.getOrderSection());
        orderInfoSearchDto.setStoreNo(request.getStoreNo());
        return OrderReaderHandler.ORDER_READER_HANDLER_MAP.get(OrderInfoQueryType.DB.getValue()).queryOrderPageList(orderInfoSearchDto);
    }

}
