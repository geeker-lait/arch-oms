package org.arch.oms.manager;

import org.arch.oms.common.dto.OrderInfoSearchDto;
import org.arch.oms.common.request.OrderSectionRequest;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.OrderMasterVo;
import org.arch.oms.common.vo.PageVo;
import org.arch.oms.entity.OrderMaster;
import org.arch.oms.utils.BeanCopyUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
public abstract class OrderReaderHandler {

    public static final Map<String, OrderReaderHandler> ORDER_READER_HANDLER_MAP = new ConcurrentHashMap<>();

    /**
     * 根据指定订单号列表查询
     * @param userId
     * @param orderIds
     * @param orderSectionRequest
     * @return
     */
    public abstract List<OrderInfoVo> queryListOrder(Long userId, List<Long> orderIds, OrderSectionRequest orderSectionRequest);

    /**
     * 用户查询订单 by page
     * @param request
     * @return
     */
    public abstract PageVo<List<OrderInfoVo>> queryOrderPageList(OrderInfoSearchDto request);

    /**
     * 管理端查询订单 by page
     * @param request
     * @return
     */
    public abstract PageVo<List<OrderInfoVo>> queryOrderPageListByManager(OrderInfoSearchDto request);

    /**
     * 是否查询 进行订单其他信息的处理
     * @param orderSectionRequest
     * @return
     */
    protected static boolean needBuildInfoDetail(OrderSectionRequest orderSectionRequest) {
        if (orderSectionRequest == null) {
            return false;
        }
        return orderSectionRequest.getOrderAddress() ||
                orderSectionRequest.getOrderFulfil() ||
                orderSectionRequest.getOrderInvoice() ||
                orderSectionRequest.getOrderItemRelish() ||
                orderSectionRequest.getOrderItem() ||
                orderSectionRequest.getOrderPayment();
    }

    /**
     * 将 OrderMaster 集合转成vo 集合
     * @param orderMasters
     * @return
     */
    protected static List<OrderInfoVo> orderMasterToOrderInfoVo(List<OrderMaster> orderMasters) {
        return orderMasters.stream().map(orderMaster -> {
            OrderInfoVo orderInfoVo = new OrderInfoVo();
            orderInfoVo.setOrderMasterVo(BeanCopyUtil.convert(orderMaster, OrderMasterVo.class));
            return orderInfoVo;
        }).collect(Collectors.toList());
    }


}
