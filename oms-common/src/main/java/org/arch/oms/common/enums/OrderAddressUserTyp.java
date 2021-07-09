package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-24
 */
public enum OrderAddressUserTyp implements ValueDescription<Integer> {
    SHIPPER(1, "发货人"),
    CONSIGNEE(2, "收货人")
    ;


    private final int value;

    private final String description;

    private static List<OrderAddressUserTyp> VALUES = EnumUtils.getEnumList(OrderAddressUserTyp.class);


    OrderAddressUserTyp(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderAddressUserTyp getEnum(int value) {
        return VALUES.stream().filter((n) ->
            Objects.equals(n.getValue(), value)
        ).findAny().orElse(null);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
