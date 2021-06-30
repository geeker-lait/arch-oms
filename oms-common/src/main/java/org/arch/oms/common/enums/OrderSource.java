package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-25
 */
public enum OrderSource implements ValueDescription<Integer> {

    DISABLED(0, "pc订单"),
    AVAILABLE(1, "app订单")
    ;

    private final int value;

    private final String description;

    private static List<OrderSource> VALUES = EnumUtils.getEnumList(OrderSource.class);

    OrderSource(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderSource getEnum(int value) {
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
