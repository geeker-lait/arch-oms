package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
public enum  OrderInfoQueryType implements ValueDescription<String> {
    DB("db", "数据库"),
    ;


    private final String value;

    private final String description;

    private static List<OrderInfoQueryType> VALUES = EnumUtils.getEnumList(OrderInfoQueryType.class);


    OrderInfoQueryType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderInfoQueryType getEnum(int value) {
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
