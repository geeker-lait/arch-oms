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

    /*
     每个状态中间间隔20  方便后期中间加状态
     */
    NONE(0, "无"),
    WAITING_PAYMENT(20, "等待支付"),
    PAYMENT_SUCCESS(40, "支付成功"),
    SHIPPING(60, "等待收货"),
    WAITING_COMMENT(80, "等待评价"),
    AFTER_SALE(100, "售后"),
    COMPLETE(120, "已完成"),
    CANCELLED(140, "已取消"),
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
