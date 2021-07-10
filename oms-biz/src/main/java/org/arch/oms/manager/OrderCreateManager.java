package org.arch.oms.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.arch.framework.api.IdKey;
import org.arch.framework.beans.TokenInfo;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.framework.id.IdService;
import org.arch.oms.common.Constant;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.enums.OrderAddressUserTyp;
import org.arch.oms.common.enums.OrderInvoiceTyp;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.common.enums.OrderPaymentState;
import org.arch.oms.common.enums.OrderSource;
import org.arch.oms.common.enums.OrderState;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.common.vo.OrderAddressVo;
import org.arch.oms.common.vo.OrderFulfilVo;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.OrderInvoiceVo;
import org.arch.oms.common.vo.OrderItemRelishVo;
import org.arch.oms.common.vo.OrderMasterVo;
import org.arch.oms.common.vo.OrderPaymentVo;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.entity.OrderAddress;
import org.arch.oms.entity.OrderFulfil;
import org.arch.oms.entity.OrderInvoice;
import org.arch.oms.entity.OrderMaster;
import org.arch.oms.entity.OrderPayment;
import org.arch.oms.manager.pms.SkuManager;
import org.arch.oms.manager.ums.UserAddressManager;
import org.arch.oms.utils.BeanCopyUtil;
import org.arch.oms.utils.MoneyUtils;
import org.arch.oms.utils.ValidatorUtil;
import org.arch.pms.admin.api.res.ProductSkuVo;
import org.arch.ums.user.res.UserAddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private UserAddressManager userAddressManager;
    @Autowired
    private OrderCartManager orderCartManager;
    @Autowired
    private SkuManager skuManager;

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
        if (CollectionUtils.isEmpty(request.getOrderCartList()) && ObjectUtils.isEmpty(request.getProductSku())) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("提交订单没有任何商品数据"));
        }
        // 存放商品信息集合
        Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(request.getOrderCartList())) {
            Map<ProductSkuVo, BigDecimal> productSkuVos = orderCartManager.verifyOrderCartState(appId, tokenInfo.getAccountId(), request.getOrderCartList());
            // 判断购物车商品是否属于同一个商家
            if (ObjectUtils.isNotEmpty(productSkuVos) && productSkuVos.keySet().stream().map(productSkuVo -> productSkuVo.getProductSpuVo().getStoreNo()).collect(Collectors.toSet()).size() > 0) {
                throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("不同商家产品不能同时提交"));
            }
            if (ObjectUtils.isEmpty(productSkuVos)) {
                skuVoBigDecimalMap.putAll(productSkuVos);
            }

        }
        // 查询商品并加入到list中
        if (ObjectUtils.isNotEmpty(request.getProductSku())) {
            request.getProductSku().forEach(cartRequest -> {
                ValidatorUtil.checkNull(cartRequest.getQuantity(), "购买数量不能为空");
                ValidatorUtil.checkBlank(cartRequest.getSkuCode(), "sku code 不能为空");
                BigDecimal quantity = cartRequest.getQuantity();
                if (MoneyUtils.isFirstBiggerThanOrEqualToSecond(Constant.ZERO_DECIMAL, quantity)) {
                    throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("购买数量不能小于0"));
                }
            });
            List<ProductSkuVo> productSkuList = skuManager.getProductSkuList(request.getProductSku().stream().map(productSku -> productSku.getSkuCode()).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(productSkuList)) {
                Map<String, BigDecimal> collect = request.getProductSku().stream().collect(Collectors.toMap(OrderSaveRequest.ProductSku::getSkuCode, OrderSaveRequest.ProductSku::getQuantity));
                Map<ProductSkuVo, BigDecimal> productQuantityMap = productSkuList.stream().collect(Collectors.toMap(productSkuVo -> productSkuVo, productSkuVo -> collect.get(productSkuVo.getSkuNo())));
                skuVoBigDecimalMap.putAll(productQuantityMap);
            }
        }
        if (ObjectUtils.isEmpty(skuVoBigDecimalMap)) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("没有需要提交的商品"));
        }
        OrderSaveDto orderSaveDto = new OrderSaveDto();
        // 生成订单主表数据
        buildOrderMasterInfo(tokenInfo, appId, orderSaveDto, skuVoBigDecimalMap);
        // 生成订单商品行数据
        buildOrderItemInfo(orderSaveDto, skuVoBigDecimalMap);
        // 计算折扣
        buildOrderRelishInfo(skuVoBigDecimalMap, orderSaveDto);
        // 生成 发票数据
        buildOrderInvoiceInfo(orderSaveDto, request);
        // 生成 用户地址信息
        buildAddressInfo(orderSaveDto, request, tokenInfo);
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
    private void buildOrderMasterInfo(TokenInfo tokenInfo, Long appId, OrderSaveDto dto, Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap) {
        OrderMaster orderMaster = new OrderMaster();
        Set<ProductSkuVo> productSkuVos = skuVoBigDecimalMap.keySet();
        orderMaster.setId(Long.valueOf(idService.generateId(IdKey.OMS_ORDER_ID)));
        orderMaster.setAppId(appId);
        orderMaster.setBuyerAccountId(tokenInfo.getAccountId());
        orderMaster.setBuyerAccountName(tokenInfo.getAccountName());
                // 默认创建订单待支付状态
        orderMaster.setOrderState(OrderState.WAITING_PAYMENT.getValue());
        orderMaster.setOrderTime(new Date());
        orderMaster.setOrderTime(new Date());
        orderMaster.setStoreNo(Lists.newArrayList(productSkuVos).get(0).getProductSpuVo().getStoreNo());
        orderMaster.setOrderTable(OrderItemTable.PERSONAL.getValue());
        orderMaster.setOrderSource(OrderSource.AVAILABLE.getValue());

        BigDecimal orderAmount = BigDecimal.ZERO;
        productSkuVos.forEach(skuProduct -> {
            MoneyUtils.add(orderAmount, skuProduct.getPrice());
        });
        dto.setOrderDetailTable(OrderItemTable.PERSONAL.getValue());
        orderMaster.setOrderAmount(orderAmount);
        // fixme 缺少商品卖家信息
        dto.setOrderMaster(orderMaster);
    }

    /**
     * 构建 商品行信息
     * @param dto
     * @param skuVoBigDecimalMap
     */
    private void buildOrderItemInfo(OrderSaveDto dto, Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap) {
        OrderDetailHandler handler = OrderDetailHandler.getHandler(dto.getOrderDetailTable());
        handler.convertDoByProductSkuVo(skuVoBigDecimalMap, dto);
    }

    /**
     * 构建 订单，并返回订单的最终价格
     */
    private void buildOrderRelishInfo(Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap, OrderSaveDto dto) {
        OrderDetailHandler handler = OrderDetailHandler.getHandler(dto.getOrderDetailTable());
        BigDecimal bigDecimal = handler.buildOrderDetailRelish(skuVoBigDecimalMap, dto);
        OrderMaster orderMaster = dto.getOrderMaster();
        orderMaster.setPayAmount(bigDecimal);
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
        orderInvoice.setAmount(orderMaster.getPayAmount());
        orderInvoice.setAppId(orderMaster.getAppId());
        orderInvoice.setStoreNo(orderMaster.getStoreNo());
        orderInvoice.setOrderNo(orderMaster.getId());
        orderInvoice.setInvoiceTyp(invoiceInfo.getInvoiceTyp());
        orderInvoice.setInvoiceNo(invoiceInfo.getInvoiceTitle());
        orderInvoice.setInvoiceTitle(invoiceInfo.getInvoiceTitle());
        orderInvoice.setRemark(invoiceInfo.getRemark());
        orderInvoice.setReceiveEmail(invoiceInfo.getInvoiceEmail());
        orderInvoice.setInvoiceItem(invoiceInfo.getInvoiceDetail());
        dto.setOrderInvoice(orderInvoice);
    }

    /**
     * 构建用户收货信息
     * @param request
     */
    private void buildAddressInfo(OrderSaveDto dto, OrderSaveRequest request, TokenInfo tokenInfo) {
        OrderMaster orderMaster = dto.getOrderMaster();
        if (request.getAddressId() == null) {
            log.info("订单没有选择地址不生成订单配送地址数据 orderId:{}", orderMaster.getId());
            return;
        }
        UserAddressResponse userAddress = userAddressManager.getUserAddressById(request.getAddressId(), tokenInfo.getUserId());
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
        orderPayment.setOrderNo(orderMaster.getId());
        // todo 用户手机号
        orderPayment.setPayState(OrderPaymentState.WAITING_PAYMENT.getValue());
        dto.setOrderPayment(orderPayment);
    }

    /**
     * 将创建的保存订单 转换成 Vo返回给前端
     * @param dto
     * @return
     */
    public OrderInfoVo convertOrderSaveDtoToOrderInfoVo(OrderSaveDto dto) {
        if (dto == null) {
            return null;
        }
        OrderDetailHandler handler = OrderDetailHandler.getHandler(dto.getOrderDetailTable());
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setOrderDetailVo(handler.convertVo(dto.getOrderItem()));
        orderInfoVo.setOrderPaymentVo(BeanCopyUtil.convert(dto.getOrderPayment(), OrderPaymentVo.class));
        orderInfoVo.setOrderItemRelishVo(BeanCopyUtil.convert(dto.getOrderItemRelish(), OrderItemRelishVo.class));
        orderInfoVo.setOrderInvoiceVo(BeanCopyUtil.convert(dto.getOrderInvoice(), OrderInvoiceVo.class));
        orderInfoVo.setOrderMasterVo(BeanCopyUtil.convert(dto.getOrderMaster(), OrderMasterVo.class));
        orderInfoVo.setOrderFulfilVo(BeanCopyUtil.convert(dto.getOrderFulfil(), OrderFulfilVo.class));
        OrderAddressVo convert = BeanCopyUtil.convert(dto.getOrderAddress(), OrderAddressVo.class);
        orderInfoVo.setOrderAddressVoList(ObjectUtils.isEmpty(convert) ? null : Lists.newArrayList(convert));
        return orderInfoVo;
    }


}
