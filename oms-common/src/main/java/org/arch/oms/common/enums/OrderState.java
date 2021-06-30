package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-25
 */
public enum OrderState implements ValueDescription<Integer> {

    NONE(0, "无"),
    WAITING_PAYMENT(10, "等待支付"),
    PAYMENT_SUCCESS(20, "支付成功"),
    SHIPPING(30, "等待收货"),
    WAITING_COMMENT(40, "售后"),
    AFTER_SALE(50, "售后"),
    COMPLETE(60, "已完成"),
    CANCELLED(70, "已取消"),
    ;

    private final int value;

    private final String description;

    private static List<OrderState> VALUES = EnumUtils.getEnumList(OrderState.class);

    OrderState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderState getEnum(int value) {
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
