package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-25
 */
public enum OrderCartState implements ValueDescription<Integer> {
    DISABLED(0, "不可用"),
    AVAILABLE(1, "可用")
    ;

    private final int value;

    private final String description;

    private static List<OrderCartState> VALUES = EnumUtils.getEnumList(OrderCartState.class);

    OrderCartState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderCartState getEnum(int value) {
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
