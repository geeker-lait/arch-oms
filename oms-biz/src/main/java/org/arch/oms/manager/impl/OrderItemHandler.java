package org.arch.oms.manager.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.common.vo.OrderDetailVo;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.entity.OrderItem;
import org.arch.oms.entity.OrderMaster;
import org.arch.oms.manager.OrderDetailHandler;
import org.arch.oms.utils.BeanCopyUtil;
import org.arch.oms.utils.MoneyUtils;
import org.arch.pms.admin.api.res.ProductSkuVo;
import org.arch.pms.admin.api.res.ProductSpuVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-29
 */
@Slf4j
@Component
public class OrderItemHandler extends OrderDetailHandler implements InitializingBean {

    @Override
    public List<OrderDetailVo> convertVo(List<Object> detail) {
        if (CollectionUtils.isEmpty(detail)) {
            return Lists.newArrayList();
        }
        List<OrderItem> collect = detail.stream().map(obj -> (OrderItem) obj).collect(Collectors.toList());
        return BeanCopyUtil.convert(collect, OrderDetailVo.class);
    }

    @Override
    public void convertDoByProductSkuVo(Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap, OrderSaveDto orderSaveDto) {
        int oderItemLength = getOderItemLength(skuVoBigDecimalMap.size());
        OrderMaster orderMaster = orderSaveDto.getOrderMaster();
        final int[] index = {0};
        List<OrderItem> collect = skuVoBigDecimalMap.entrySet().stream().map(entry -> {
            ProductSkuVo productSkuVo = entry.getKey();
            ProductSpuVo productSpuVo = productSkuVo.getProductSpuVo();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemNo(orderMaster.getId() + String.format("%0"+ oderItemLength +"d", ++index[0]));
            orderItem.setOrderNo(orderMaster.getId());
            orderItem.setAppId(orderMaster.getAppId());
            orderItem.setStoreNo(orderMaster.getStoreNo());
            orderItem.setProductNo(productSkuVo.getSpuNo());
            orderItem.setProductPic(productSkuVo.getPic());
            orderItem.setProductName(productSkuVo.getSkuName());
            orderItem.setProductBrand(productSpuVo.getBrandName());
            orderItem.setProductPrice(productSkuVo.getPrice());
            // 价格是 数量乘以单价
            orderItem.setProductQuantity(MoneyUtils.multiply(productSkuVo.getPrice(), entry.getValue()));
            orderItem.setProductSkuNo(productSkuVo.getSkuNo());
            orderItem.setProductCategoryId(productSpuVo.getCategoryId());
            orderItem.setProductAttr(productSkuVo.getSpecJson());
            return orderItem;
        }).collect(Collectors.toList());
        BigDecimal orderAmount = BigDecimal.ZERO;
        orderSaveDto.setOrderItem((List) collect);
        Set<Map.Entry<ProductSkuVo, BigDecimal>> entries = skuVoBigDecimalMap.entrySet();
        for (Map.Entry<ProductSkuVo, BigDecimal> entry : entries) {
            ProductSkuVo productSkuVo = entry.getKey();
            orderAmount = MoneyUtils.add(orderAmount, MoneyUtils.multiply(productSkuVo.getPrice(), entry.getValue()));
        }
        orderMaster.setOrderAmount(orderAmount);

    }

    @Override
    public BigDecimal buildOrderDetailRelish(Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap, OrderSaveDto orderSaveDto) {
        // 暂时没有 促销，直接使用 sku价格
        BigDecimal orderAmount = BigDecimal.ZERO;
        Set<Map.Entry<ProductSkuVo, BigDecimal>> entries = skuVoBigDecimalMap.entrySet();
        for (Map.Entry<ProductSkuVo, BigDecimal> entry : entries) {
            ProductSkuVo productSkuVo = entry.getKey();
            orderAmount = MoneyUtils.add(orderAmount, MoneyUtils.multiply(productSkuVo.getPrice(), entry.getValue()));
        }
        return orderAmount;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CONVERT_ORDER_DETAIL_HANDLERS.put(OrderItemTable.PERSONAL.getValue(), this);
    }
}
