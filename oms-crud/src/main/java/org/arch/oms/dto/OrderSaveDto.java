package org.arch.oms.dto;

import lombok.Data;
import org.arch.oms.annotation.CrudServiceTypAnnotation;
import org.arch.oms.common.enums.CrudServiceTyp;
import org.arch.oms.entity.OrderAddress;
import org.arch.oms.entity.OrderFulfil;
import org.arch.oms.entity.OrderInvoice;
import org.arch.oms.entity.OrderItemRelish;
import org.arch.oms.entity.OrderMaster;
import org.arch.oms.entity.OrderPayment;

import java.io.Serializable;
import java.util.List;

/**
 * 订单保存Dto
 * @author junboXiang
 * @version V1.0
 * 2021-06-28
 */
@Data
public class OrderSaveDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单详情表名称
     */
    private String orderDetailTable;

    /**
     * 订单主表
     */
    @CrudServiceTypAnnotation(type = CrudServiceTyp.ORDER_MASTER)
    private OrderMaster orderMaster;

    /**
     * 订单子项集合
     */
    @CrudServiceTypAnnotation(type = CrudServiceTyp.ORDER_ITEM)
    private List<Object> orderItem;

    /**
     * 订单子项佐料集合
     */
    @CrudServiceTypAnnotation(type = CrudServiceTyp.ORDER_ITEM_RELISH)
    private List<OrderItemRelish> orderItemRelish;

    /**
     * 配送单履约信息
     */
    @CrudServiceTypAnnotation(type = CrudServiceTyp.ORDER_FULFIL)
    private OrderFulfil orderFulfil;

    /**
     * 用户地址信息
     */
    @CrudServiceTypAnnotation(type = CrudServiceTyp.ORDER_ADDRESS)
    private OrderAddress orderAddress;

    /**
     * 订单发票信息
     */
    @CrudServiceTypAnnotation(type = CrudServiceTyp.ORDER_INVOICE)
    private OrderInvoice orderInvoice;

    /**
     * 订单支付单信息
     */
    @CrudServiceTypAnnotation(type = CrudServiceTyp.ORDER_PAYMENT)
    private OrderPayment orderPayment;

    /**
     * 需要移除的购物车列表
     */
    private List<Long> removeCart;

}
