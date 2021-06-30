package org.arch.oms.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.arch.oms.common.enums.OrderInfoQueryType;
import org.arch.oms.common.request.OrderInfoQueryRequest;
import org.arch.oms.common.request.OrderSectionRequest;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.manager.OrderReaderHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单查询处理类 聚合各种信息
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
@Slf4j
@Component
public class OrderReaderForDBHandler implements OrderReaderHandler, InitializingBean {
    @Override
    public OrderInfoVo queryOrder(Long userId, Long orderId, OrderSectionRequest orderSectionRequest) {
        // todo 订单查询实现
        return null;
    }

    @Override
    public List<OrderInfoVo> queryOrderList(OrderInfoQueryRequest request) {
        // todo 订单查询实现
        return null;
    }

    @Override
    public List<OrderInfoVo> queryOrderByManager(OrderInfoQueryRequest request) {
        // todo 订单查询实现
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ORDER_READER_HANDLER_MAP.put(OrderInfoQueryType.DB.getValue(), this);
    }

    private List<OrderInfoVo> queryOrder() {
    // todo
        return null;
    }
}
