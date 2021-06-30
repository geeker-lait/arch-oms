package org.arch.oms.common.request;

import java.io.Serializable;

/**
 *
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
public class OrderSectionRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean orderItem = Boolean.FALSE;

    private boolean orderAddress = Boolean.FALSE;

    private boolean orderInvoice = Boolean.FALSE;

    private boolean orderFulfil = Boolean.FALSE;

    private boolean orderItemRelish = Boolean.FALSE;

    private boolean orderPayment = Boolean.FALSE;


    public OrderSectionRequest includeOrderItem() {
        this.orderItem = Boolean.TRUE;
        return this;
    }

    public OrderSectionRequest includeOrderAddress() {
        this.orderAddress = Boolean.TRUE;
        return this;
    }

    public OrderSectionRequest includeOrderInvoice() {
        this.orderInvoice = Boolean.TRUE;
        return this;
    }

    public OrderSectionRequest includeOrderFulfil() {
        this.orderFulfil = Boolean.TRUE;
        return this;
    }

    public OrderSectionRequest includeOrderItemRelish() {
        this.orderItemRelish = Boolean.TRUE;
        return this;
    }

    public OrderSectionRequest includeOrderPayment() {
        this.orderPayment = Boolean.TRUE;
        return this;
    }

}
