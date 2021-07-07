package org.arch.oms.common.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */
@Data
public class OrderInfoQueryRequest  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private List<Long> orderIds;
    private OrderSectionRequest orderSectionRequest;

}
