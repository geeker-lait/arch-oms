package org.arch.oms.manager;

import org.arch.oms.common.request.OrderInfoQueryRequest;
import org.arch.oms.common.request.OrderSectionRequest;
import org.arch.oms.common.vo.OrderInfoVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
public interface OrderReaderHandler {

    Map<String, OrderReaderHandler> ORDER_READER_HANDLER_MAP = new ConcurrentHashMap<>();

    /**
     * 查询单个订单详情
     * @param userId
     * @param orderId
     * @param orderSectionRequest
     * @return
     */
    OrderInfoVo queryOrder(Long userId, Long orderId, OrderSectionRequest orderSectionRequest);

    /**
     * 用户查询订单
     * @param request
     * @return
     */
    List<OrderInfoVo> queryOrderList(OrderInfoQueryRequest request);

    /**
     * 管理端查询订单
     * @param request
     * @return
     */
    List<OrderInfoVo> queryOrderByManager(OrderInfoQueryRequest request);



}
