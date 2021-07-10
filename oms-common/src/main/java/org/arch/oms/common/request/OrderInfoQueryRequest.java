package org.arch.oms.common.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */
@Data
public class OrderInfoQueryRequest  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private OrderSectionRequest orderSectionRequest;

}
