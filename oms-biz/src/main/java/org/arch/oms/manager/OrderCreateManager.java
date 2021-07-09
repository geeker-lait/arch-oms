package org.arch.oms.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.arch.framework.api.IdKey;
import org.arch.framework.beans.TokenInfo;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.framework.id.IdService;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.enums.OrderAddressUserTyp;
import org.arch.oms.common.enums.OrderInvoiceTyp;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.common.enums.OrderState;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.entity.OrderAddress;
import org.arch.oms.entity.OrderCart;
import org.arch.oms.entity.OrderFulfil;
import org.arch.oms.entity.OrderInvoice;
import org.arch.oms.entity.OrderMaster;
import org.arch.oms.entity.OrderPayment;
import org.arch.oms.manager.ums.UserAddressManager;
import org.arch.oms.service.OrderCartService;
import org.arch.ums.user.res.UserAddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
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
    @Autowired
    private UserAddressManager userAddressManager;

    /**
     * 通过创建用户请求转换成 oder
     *  fixme 暂时没有库存、折扣逻辑
     * @param request
     */
    public OrderSaveDto buildOrderInfo(OrderSaveRequest request, TokenInfo tokenInfo, Long appId) {
        /**
         * 1.根据商品或者购物车查询出商品
         *
         * 2.生成订单相关数据
         */
        if (CollectionUtils.isEmpty(request.getOrderCartList()) && CollectionUtils.isEmpty(request.getProductSkuNoList())) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("提交订单没有任何商品数据"));
        }
        // 存放商品信息集合
        List<Object> productList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(request.getOrderCartList())) {
            LambdaQueryWrapper<OrderCart> cartWrapper = Wrappers.lambdaQuery();
            cartWrapper.in(OrderCart::getId, request.getOrderCartList()).eq(OrderCart::getBuyerAccountId, tokenInfo.getAccountId());
            List<OrderCart> carts = orderCartService.findAllBySpec(cartWrapper);
            // 判断购物车商品是否属于同一个商家
            if (CollectionUtils.isNotEmpty(carts) && carts.stream().map(OrderCart::getStoreNo).collect(Collectors.toSet()).size() > 0) {
                throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("不同商家产品不能同时提交"));
            }
            // todo 根据购物车查询商品信息 并判断是否过期， 没有过期加入到list中 并将购物车id添加到 返回值中
        }
        // 查询商品并加入到list中
        if (CollectionUtils.isEmpty(request.getProductSkuNoList())) {

            // todo 查询 传入的商品编号列表
        }
        if (CollectionUtils.isEmpty(productList)) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("没有需要提交的商品"));
        }
        OrderSaveDto orderSaveDto = new OrderSaveDto();
        // 生成订单主表数据
        buildOrderMasterInfo(tokenInfo, appId, orderSaveDto);
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
        buildPaymentInfo(orderSaveDto, tokenInfo);
        return orderSaveDto;
    }

    /**
     * 构建商品主表信息
     * @param tokenInfo
     * @param dto
     * @param appId
     */
    private void buildOrderMasterInfo(TokenInfo tokenInfo, Long appId, OrderSaveDto dto) {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setId(Long.valueOf(idService.generateId(IdKey.OMS_ORDER_ID)))
                .setAppId(appId).setBuyerAccountId(tokenInfo.getAccountId()).setBuyerAccountName(tokenInfo.getAccountName())
                // 默认创建订单待支付状态
                .setOrderState(OrderState.WAITING_PAYMENT.getValue()).setOrderTime(new Date())
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
        OrderMaster orderMaster = dto.getOrderMaster();
        if (request.getInvoiceInfo() == null || OrderInvoiceTyp.getEnum(request.getInvoiceInfo().getInvoiceTyp()) == null) {
            log.info("订单没有填写发票类型不生成发票记录 orderId:{}", orderMaster.getId());
            return;
        }
        OrderSaveRequest.InvoiceInfo invoiceInfo = request.getInvoiceInfo();

        OrderInvoice orderInvoice = new OrderInvoice();
        orderInvoice.setAmount(orderMaster.getOrderAmount())
                .setAppId(orderMaster.getAppId())
                .setStoreNo(orderMaster.getStoreNo())
                .setOrderNo(orderMaster.getId())
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
        OrderMaster orderMaster = dto.getOrderMaster();
        if (request.getAddressId() == null) {
            log.info("订单没有选择地址不生成订单配送地址数据 orderId:{}", orderMaster.getId());
            return;
        }
        UserAddressResponse userAddress = userAddressManager.getUserAddressById(request.getAddressId(), orderMaster.getBuyerAccountId());
        if (userAddress == null) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("用户地址不存在"));
        }
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setAppId(orderMaster.getAppId());
        orderAddress.setStoreNo(orderMaster.getStoreNo());
        orderAddress.setOrderNo(orderMaster.getId());
        orderAddress.setUserPhone(userAddress.getMobile());
        orderAddress.setUserName(userAddress.getName());
        orderAddress.setCountry(userAddress.getCountry());
        orderAddress.setZipcode(userAddress.getZipCode());
        orderAddress.setUserTyp(OrderAddressUserTyp.CONSIGNEE.getValue());
        orderAddress.setProvinceName(userAddress.getProvinceName());
        orderAddress.setProvinceNo(userAddress.getProvinceNo());
        orderAddress.setProvinceShortName(userAddress.getProvinceShortName());
        orderAddress.setCityName(userAddress.getCityName());
        orderAddress.setCityNo(userAddress.getCityNo());
        orderAddress.setCityShortName(userAddress.getCityShortName());
        orderAddress.setDistrictName(userAddress.getDistrictName());
        orderAddress.setDistrictNo(userAddress.getDistrictNo());
        orderAddress.setStreetName(userAddress.getStreetName());
        orderAddress.setStreetNo(userAddress.getStreetNo());
        orderAddress.setAddress(userAddress.getAddress());
        dto.setOrderAddress(orderAddress);
    }

    /**
     * 构建履约信息
     */
    private void buildFulFilInfo(OrderSaveDto dto) {
        OrderMaster orderMaster = dto.getOrderMaster();
        // todo 生成履约单 id 是在哪个地方
        OrderFulfil orderFulfil = new OrderFulfil();
        orderFulfil.setAppId(orderMaster.getAppId());
        orderFulfil.setOrderNo(orderMaster.getId());
        orderFulfil.setStoreNo(orderMaster.getStoreNo());
//        orderFulfil.setFulfilNo(idService.generateId())
        dto.setOrderFulfil(orderFulfil);

    }

    /**
     * 构建支付信息
     */
    private void buildPaymentInfo(OrderSaveDto dto, TokenInfo tokenInfo) {
        OrderMaster orderMaster = dto.getOrderMaster();
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setAccountId(orderMaster.getBuyerAccountId());
        orderPayment.setAccountName(orderMaster.getBuyerAccountName());
        orderPayment.setAmount(orderMaster.getPayAmount());
        // todo 用户手机号
//        orderPayment.setPhoneNo(tokenInfo.get)
    }


}
