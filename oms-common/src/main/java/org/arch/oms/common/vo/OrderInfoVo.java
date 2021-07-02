package org.arch.oms.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
@Data
public class OrderInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private OrderMasterVo orderMasterVo;

    private List<OrderDetailVo> orderDetailVo;

    private OrderFulfilVo orderFulfilVo;

    private OrderInvoiceVo orderInvoiceVo;

    private OrderItemRelishVo orderItemRelishVo;

    private OrderPaymentVo orderPaymentVo;

    /**
     * 双向的包括用户收货地址 和商家的发货地址
     */
    private List<OrderAddressVo> orderAddressVoList;

}
