package org.arch.oms.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.request.OrderCartSaveRequest;
import org.arch.oms.common.vo.OrderCartVo;
import org.arch.oms.entity.OrderCart;
import org.arch.oms.manager.OrderCartManager;
import org.arch.oms.manager.UserHelper;
import org.arch.oms.rest.OrderCartRest;
import org.arch.oms.service.OrderCartService;
import org.arch.oms.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-24
 */
@Slf4j
@RestController
@RequestMapping("/oms/cart")
public class OrderCartRestBiz implements OrderCartRest {

    @Autowired
    private OrderCartService orderCartService;
    @Autowired
    private UserHelper userHelper;
    @Autowired
    private OrderCartManager orderCartManager;

    @Override
    public Boolean save(@RequestBody List<OrderCartSaveRequest> requests) {
        Long userId = userHelper.getUserId();
        Long appId = userHelper.getAppId();
        if (CollectionUtils.isEmpty(requests)) {
            return true;
        }
        // fixme 此出数量 可以按照应用 维度从配置中心读取
        final int cartNumMax = 10;
        if (requests.size() > cartNumMax) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode(MessageFormat.format("购物车最大数量是{0}", cartNumMax)));
        }
        List<OrderCart> orderCarts = orderCartManager.buildAndCheckOrderCart(requests, userHelper.getTokenInfo(), appId);
        return orderCartService.saveCarts(orderCarts, userId);
    }

    @Override
    public Boolean delete(@RequestBody List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return Boolean.TRUE;
        }
        return orderCartService.deleteByIds(ids, userHelper.getUserId());
    }

    @Override
    public List<OrderCartVo> getCartList() {
        Long userId = userHelper.getUserId();
        Long appId = userHelper.getAppId();
        LambdaQueryWrapper<OrderCart> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderCart::getAppId, appId).eq(OrderCart::getBuyerAccountId, userId);
        List<OrderCart> orderCarts = orderCartService.findAllBySpec(queryWrapper);
        // 调用获取商品规格信息是否变更，变更了批量update 购物车状态 数据
        orderCartManager.verifyOrderCartState(orderCarts);
        // fixme 是否分组排序可以根据appId从配置获取
        boolean enableSort = true;
        if (CollectionUtils.isNotEmpty(orderCarts) && enableSort) {
            // 根据 店铺分组,然后按照加入时间排序   作用：相同门店的商品在一起 所以按商家编号 分组
            LinkedHashMap<String,List<OrderCart>> collect = orderCarts.stream().sorted(Comparator.comparing(OrderCart::getDt).reversed())
                    .collect(Collectors.groupingBy(OrderCart::getStoreNo, LinkedHashMap::new,Collectors.toList()));
            List<OrderCart> finalOrderCarts = Lists.newArrayList();
            collect.entrySet().forEach(keyMap -> {
                finalOrderCarts.addAll(keyMap.getValue());
            });
            orderCarts= finalOrderCarts;

        }
        return BeanCopyUtil.convert(orderCarts, OrderCartVo.class);
    }

}
