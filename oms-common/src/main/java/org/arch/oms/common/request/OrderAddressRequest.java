package org.arch.oms.common.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderAddressRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderNo;

    private Long accountId;

}

