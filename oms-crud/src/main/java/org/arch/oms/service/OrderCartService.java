package org.arch.oms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudService;
import org.arch.oms.common.ContainerConstants;
import org.arch.oms.common.enums.CrudServiceTyp;
import org.arch.oms.dao.OrderCartDao;
import org.arch.oms.entity.OrderCart;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lait
 * @description 项目业务(OrderCart) 表服务层
 * @date 2021年6月13日 下午7:02:32
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderCartService extends CrudService<OrderCart, Long> implements InitializingBean {
    private final OrderCartDao orderCartDao;

    /**
     * 根据id集合删除购物车 - 用户端
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByIds(List<Long> ids, Long accountId) {
        LambdaQueryWrapper<OrderCart> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderCart::getBuyerAccountId, accountId).in(OrderCart::getId, ids);
        orderCartDao.remove(queryWrapper);
        return orderCartDao.removeByIds(ids);
    }

    /**
     * 根据商品判断购物车是否存在 - 用户端
     * @param orderCarts
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveCarts(List<OrderCart> orderCarts, Long accountId) {
        LambdaQueryWrapper<OrderCart> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderCart::getBuyerAccountId, accountId).in(OrderCart::getProductSkuNo, orderCarts.stream().map(OrderCart::getProductSkuNo).collect(Collectors.toList()));
        Map<String, Long> existProductList = orderCartDao.list(queryWrapper).stream().collect(Collectors.toMap(orderCart -> orderCart.getProductSkuNo(), orderCart -> orderCart.getId()));
        List<OrderCart> insertList = Lists.newArrayList();
        List<OrderCart> updateList = Lists.newArrayList();
        insertList.addAll(orderCarts.stream().filter(orderCart -> !existProductList.containsKey(orderCart.getProductSkuNo())).collect(Collectors.toList()));
        // 根据商品id 查询 全部是空 表示全部是新增
        List<OrderCart> updateOrderCart = orderCarts.stream().filter(orderCart -> existProductList.containsKey(orderCart.getProductSkuNo()))
                .map(orderCart -> {
                    orderCart.setId(existProductList.get(orderCart.getProductSkuNo()));
                    return orderCart;
                }).collect(Collectors.toList());
        updateList.addAll(updateOrderCart);
        if (CollectionUtils.isNotEmpty(updateList)) {
            orderCartDao.updateBatchById(updateList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            orderCartDao.saveBatch(insertList);
        }
        return Boolean.TRUE;
    }

    /**
     * 更新指定字段数据 - 用户端
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateCartsState(List<Long> ids, Long accountId) {
        LambdaQueryWrapper<OrderCart> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(OrderCart::getId, ids);
        orderCartDao.update(queryWrapper);
        return null;
    }


    /**
     * 商品变更 更新购物车状态 - 消息通知调用 预留
     * @param productSkuNo
     * @param productAttrs
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStateByProductChange(String productSkuNo, String productAttrs) {
        LambdaQueryWrapper<OrderCart> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderCart::getProductSkuNo, productSkuNo).ne(OrderCart::getProductAttrs, productAttrs);
        orderCartDao.update(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 批量根据Id更新
     * @param list
     * @return
     */
    public Boolean updateBatchById(List<OrderCart> list) {
        return orderCartDao.updateBatchById(list, 100);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ContainerConstants.CRUD_SERVICE_MAP.put(CrudServiceTyp.ORDER_CART.getValue(), this);
    }
}
