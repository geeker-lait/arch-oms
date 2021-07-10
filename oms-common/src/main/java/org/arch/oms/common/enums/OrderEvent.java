package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * 订单流转
 * @author junboXiang
 * @version V1.0
 * 2021-07-10
 */
public enum OrderEvent implements ValueDescription<String> {
    CREATE("CREATE", "创建"),
    PAY_SUCCESS("PAY_SUCCESS", "支付成功"),
    SHIPPING("SHIPPING", "发货"),
    COMMENT("COMMENT", "评论"),
    AFTER_SALE("AFTER_SALE", "售后"),
    CANCELLED("CANCELLED", "取消"),
    ;


    private final String value;

    private final String description;

    private static List<OrderEvent> VALUES = EnumUtils.getEnumList(OrderEvent.class);


    OrderEvent(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderEvent getEnum(int value) {
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
