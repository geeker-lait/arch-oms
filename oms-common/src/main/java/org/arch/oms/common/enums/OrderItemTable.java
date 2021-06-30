package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-25
 */
public enum OrderItemTable implements ValueDescription<String> {

    PERSONAL("order_item", "通用订单子表"),
    ;


    private final String value;

    private final String description;

    private static List<OrderItemTable> VALUES = EnumUtils.getEnumList(OrderItemTable.class);


    OrderItemTable(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderItemTable getEnum(int value) {
        return VALUES.stream().filter((n) ->
                Objects.equals(n.getValue(), value)
        ).findAny().orElse(null);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
