package org.arch.oms.common.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-26
 */
@Data
public class OrderCartSaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productId;


    private BigDecimal quantity;


}