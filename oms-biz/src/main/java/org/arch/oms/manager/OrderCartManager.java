package org.arch.oms.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.arch.framework.beans.TokenInfo;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.Constant;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.enums.OrderCartState;
import org.arch.oms.common.request.OrderCartSaveRequest;
import org.arch.oms.entity.OrderCart;
import org.arch.oms.manager.pms.SkuManager;
import org.arch.oms.service.OrderCartService;
import org.arch.oms.utils.MoneyUtils;
import org.arch.oms.utils.ValidatorUtil;
import org.arch.pms.admin.api.res.ProductSkuVo;
import org.arch.pms.admin.api.res.ProductSpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-09
 */
@Component
public class OrderCartManager {

    @Autowired
    private SkuManager skuManager;
    @Autowired
    private OrderCartService orderCartService;

    /**
     * 根据创建购物车请求 构建 保存 购物车 DO
     * @return
     */
    public List<OrderCart> buildAndCheckOrderCart(List<OrderCartSaveRequest> requests, TokenInfo tokenInfo, Long appId) {
        requests.forEach(cartRequest -> {
            ValidatorUtil.checkNull(cartRequest.getQuantity(), "购买数量不能为空");
            ValidatorUtil.checkBlank(cartRequest.getSkuCode(), "sku code 不能为空");
            BigDecimal quantity = cartRequest.getQuantity();
            if (MoneyUtils.isFirstBiggerThanOrEqualToSecond(Constant.ZERO_DECIMAL, quantity)) {
                throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("购买数量不能小于0"));
            }
        });
        Map<String, BigDecimal> collect = requests.stream().collect(Collectors.toMap(request -> request.getSkuCode(), request -> request.getQuantity()));
        List<ProductSkuVo> productSkuList = skuManager.getProductSkuList(collect.keySet());
        ValidatorUtil.checkNull(productSkuList, "没有查到商品sku");
        return productSkuList.stream().map(productSkuVo -> {
            ProductSpuVo productSpuVo = productSkuVo.getProductSpuVo();
            OrderCart orderCart = new OrderCart();
            orderCart.setAppId(appId);
            orderCart.setBuyerAccountId(tokenInfo.getAccountId());
            orderCart.setBuyerAccountName(tokenInfo.getAccountName());
            orderCart.setStoreNo(productSpuVo.getStoreNo());
            orderCart.setProductCategoryId(productSpuVo.getCategoryId());
            orderCart.setProductBrand(productSpuVo.getBrandName());
            orderCart.setProductNo(productSkuVo.getSpuNo());
            orderCart.setProductName(productSpuVo.getSpuName());
            orderCart.setProductSubTitle(productSpuVo.getSubTitle());
            orderCart.setProductAttrs(productSkuVo.getSpecJson());
            orderCart.setProductPic(productSkuVo.getPic());
            orderCart.setProductSkuNo(productSkuVo.getSkuNo());
            orderCart.setQuantity(collect.get(productSkuVo.getSkuNo()));
            orderCart.setPrice(productSkuVo.getPrice());
            orderCart.setState(OrderCartState.AVAILABLE.getValue());
            return orderCart;
        }).collect(Collectors.toList());
    }


    public Map<ProductSkuVo, BigDecimal> verifyOrderCartState(Long appId, Long userId, Collection<Long> orderCartIds) {
        LambdaQueryWrapper<OrderCart> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderCart::getAppId, appId).eq(OrderCart::getBuyerAccountId, userId);
        if (ObjectUtils.isNotEmpty(orderCartIds)) {
            queryWrapper.in(OrderCart::getId, orderCartIds);
        }
        List<OrderCart> list = orderCartService.findAllBySpec(queryWrapper);
        if (ObjectUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        Map<String, ProductSkuVo> collect = Optional.ofNullable(skuManager.getProductSkuList(list.stream().map(OrderCart::getProductSkuNo).collect(Collectors.toList())))
                .orElse(Lists.newArrayList()).stream().collect(Collectors.toMap(ProductSkuVo::getSkuNo, productSkuVo -> productSkuVo));
        List<OrderCart> updateStateList = Lists.newArrayList();
        list.forEach(orderCart -> {
            if (collect.containsKey(orderCart.getProductSkuNo())) {
                if (!Objects.equals(orderCart.getProductAttrs(), collect.get(orderCart.getProductSkuNo()))) {
                    OrderCart updateOrderCart = new OrderCart();
                    updateOrderCart.setId(orderCart.getId());
                    updateOrderCart.setState(OrderCartState.DISABLED.getValue());
                    updateStateList.add(updateOrderCart);
                    orderCart.setState(OrderCartState.DISABLED.getValue());
                }
            } else {
                OrderCart updateOrderCart = new OrderCart();
                updateOrderCart.setId(orderCart.getId());
                updateOrderCart.setState(OrderCartState.DISABLED.getValue());
                orderCart.setState(OrderCartState.DISABLED.getValue());
                updateStateList.add(updateOrderCart);
            }
        });
        if (ObjectUtils.isNotEmpty(updateStateList)) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("提交的购物车商品已过期请重新选择"));
        }
        return list.stream().collect(Collectors.toMap(orderCart -> collect.get(orderCart.getProductSkuNo()), orderCart -> orderCart.getQuantity()));
    }

    /**
     * 验证购物车集合是否失效并更新
     * @param appId
     * @param accountId
     * @return
     */
    public List<OrderCart> verifyOrderCartState(Long appId, Long accountId) {
        LambdaQueryWrapper<OrderCart> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderCart::getAppId, appId).eq(OrderCart::getBuyerAccountId, accountId);
        List<OrderCart> list = orderCartService.findAllBySpec(queryWrapper);
        if (ObjectUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        Map<String, String> collect = Optional.ofNullable(skuManager.getProductSkuList(list.stream().map(OrderCart::getProductSkuNo).collect(Collectors.toList())))
                .orElse(Lists.newArrayList()).stream().collect(Collectors.toMap(ProductSkuVo::getSkuNo, ProductSkuVo::getSpecJson));
        List<OrderCart> updateStateList = Lists.newArrayList();
        list.forEach(orderCart -> {
            if (collect.containsKey(orderCart.getProductSkuNo())) {
                if (!Objects.equals(orderCart.getProductAttrs(), collect.get(orderCart.getProductSkuNo()))) {
                    OrderCart updateOrderCart = new OrderCart();
                    updateOrderCart.setId(orderCart.getId());
                    updateOrderCart.setState(OrderCartState.DISABLED.getValue());
                    updateStateList.add(updateOrderCart);
                    orderCart.setState(OrderCartState.DISABLED.getValue());
                }
            } else {
                OrderCart updateOrderCart = new OrderCart();
                updateOrderCart.setId(orderCart.getId());
                updateOrderCart.setState(OrderCartState.DISABLED.getValue());
                updateStateList.add(updateOrderCart);
                orderCart.setState(OrderCartState.DISABLED.getValue());
            }
        });
        orderCartService.updateBatchById(updateStateList);
        return list;
    }

}
