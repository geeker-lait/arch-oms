package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * 订单支付状态
 * @author junboXiang
 * @version V1.0
 * 2021-07-10
 */
public enum OrderPaymentState implements ValueDescription<Integer> {

    WAITING_PAYMENT(20, "等待支付"),
    PAYMENT_SUCCESS(40, "支付成功"),
    CANCELLED(60, "已取消"),
            ;

    private final int value;

    private final String description;

    private static List<OrderPaymentState> VALUES = EnumUtils.getEnumList(OrderPaymentState.class);

    OrderPaymentState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderPaymentState getEnum(int value) {
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
