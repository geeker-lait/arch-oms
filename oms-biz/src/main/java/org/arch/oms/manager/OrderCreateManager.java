package org.arch.oms.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.arch.framework.api.IdKey;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.framework.id.IdService;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.enums.OrderInvoiceTyp;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.entity.OrderCart;
import org.arch.oms.entity.OrderInvoice;
import org.arch.oms.entity.OrderMaster;
import org.arch.oms.service.OrderCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-28
 */
@Slf4j
@Component
public class OrderCreateManager {

    @Autowired
    private IdService idService;
    @Autowired
    private OrderCartService orderCartService;

    /**
     * 通过创建用户请求转换成 oder
     *  fixme 暂时没有库存、折扣逻辑
     * @param request
     */
    public OrderSaveDto buildOrderInfo(OrderSaveRequest request, Long userId, String userName, Long appId) {
        /**
         * 1.根据商品或者购物车查询出商品
         *
         * 2.生成订单相关数据
         */
        if (CollectionUtils.isEmpty(request.getOrderCartList()) && CollectionUtils.isEmpty(request.getProductNoList())) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("没有商品或购物车数据"));
        }
        // 存放商品信息集合
        ArrayList<Object> productList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(request.getOrderCartList())) {
            LambdaQueryWrapper<OrderCart> cartWrapper = Wrappers.lambdaQuery();
            cartWrapper.in(OrderCart::getId, request.getOrderCartList()).eq(OrderCart::getBuyerAccountId, userId);
            List<OrderCart> carts = orderCartService.findAllBySpec(cartWrapper);
            // 判断购物车商品是否属于同一个商家
            if (CollectionUtils.isNotEmpty(carts) && carts.stream().map(OrderCart::getStoreNo).collect(Collectors.toSet()).size() > 0) {
                throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("不通商家产品不能同事提交"));
            }
            // todo 根据购物车查询商品信息 并判断是否过期， 没有过期加入到list中 并将购物车id添加到 返回值中
        }
        // 查询商品并加入到list中
        if (CollectionUtils.isEmpty(request.getProductNoList())) {

        }
        if (CollectionUtils.isEmpty(productList)) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("没有需要提交的商品"));
        }
        OrderSaveDto orderSaveDto = new OrderSaveDto();
        // 生成订单主表数据
        buildOrderMasterInfo(userId, userName, appId, orderSaveDto);
        // 生成订单商品行数据
        buildOrderItemInfo(orderSaveDto, productList);
        // 计算折扣
        buildOrderRelishInfo(productList, orderSaveDto);
        // 生成 发票数据
        buildOrderInvoiceInfo(orderSaveDto, request);
        // 生成 用户地址信息
        buildAddressInfo(orderSaveDto, request);
        // 生成 履约信息
        buildFulFilInfo(orderSaveDto);
        // 支付表信息
        buildPayInfo(orderSaveDto);
        return orderSaveDto;
    }

    /**
     * 构建商品主表信息
     * @param userId
     * @param userName
     * @param dto
     * @param appId
     */
    private void buildOrderMasterInfo(Long userId, String userName, Long appId, OrderSaveDto dto) {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setId(Long.valueOf(idService.generateId(IdKey.OMS_ORDER_ID)))
                .setAppId(appId).setBuyerAccountId(userId).setBuyerAccountName(userName)
        ;
        // todo 缺少商品卖家信息
        dto.setOrderMaster(orderMaster);
        // todo 填充默认的 商品item 表 类型
        dto.setOrderDetailTable(OrderItemTable.PERSONAL.getValue());
    }

    /**
     * 构建 商品行信息
     * @param dto
     * @param products
     */
    private void buildOrderItemInfo(OrderSaveDto dto, List<Object> products) {
        // todo

    }

    /**
     * 构建 订单，并返回订单的最终价格
     */
    private BigDecimal buildOrderRelishInfo(List<Object> products, OrderSaveDto dto) {
        // todo 暂时循环所有商品行的销售价格相加
        OrderDetailHandler handler = OrderDetailHandler.getHandler(dto.getOrderDetailTable());
        return handler.buildOrderDetailRelish(products, dto);
    }

    /**
     * 构建发票信息
     * @param dto
     * @param request
     */
    private void buildOrderInvoiceInfo(OrderSaveDto dto, OrderSaveRequest request) {
        if (request.getInvoiceInfo() == null || OrderInvoiceTyp.getEnum(request.getInvoiceInfo().getInvoiceTyp()) == null) {
            return;
        }
        OrderSaveRequest.InvoiceInfo invoiceInfo = request.getInvoiceInfo();
        OrderMaster orderMaster = dto.getOrderMaster();
        OrderInvoice orderInvoice = new OrderInvoice();
        orderInvoice.setAmount(orderMaster.getOrderAmount())
                .setAppId(orderMaster.getAppId())
                .setStoreNo(orderMaster.getStoreNo())
                .setOrderNo(orderMaster.getOrderNo())
                .setInvoiceTyp(invoiceInfo.getInvoiceTyp())
                .setInvoiceNo(invoiceInfo.getInvoiceTitle())
                .setInvoiceTitle(invoiceInfo.getInvoiceTitle())
                .setRemark(invoiceInfo.getRemark())
                .setReceiveEmail(invoiceInfo.getInvoiceEmail())
                .setInvoiceItem(invoiceInfo.getInvoiceDetail());
        dto.setOrderInvoice(orderInvoice);
    }

    /**
     * 构建用户收货信息
     * @param request
     */
    private void buildAddressInfo(OrderSaveDto dto, OrderSaveRequest request) {
        if (request.getAddressId() == null) {
            return;
        }
        // todo 根据查询的用户地址id

    }

    /**
     * 构建履约信息
     */
    private void buildFulFilInfo(OrderSaveDto dto) {

    }

    /**
     * 构建支付信息
     */
    private void buildPayInfo(OrderSaveDto dto) {

    }


}
