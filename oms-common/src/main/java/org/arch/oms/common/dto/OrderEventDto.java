package org.arch.oms.common.dto;

import lombok.Data;
import org.arch.oms.common.enums.OrderEvent;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-10
 */
@Data
public class OrderEventDto<T> {
    private OrderEvent orderEvent;
    private T params;
    private String operatorId;
    private String operatorName;
    private String lockKey;
}
