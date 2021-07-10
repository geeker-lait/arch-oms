package org.arch.oms.common.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-10
 */
@Data
public class PayOrderByCardRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long orderId;
}
