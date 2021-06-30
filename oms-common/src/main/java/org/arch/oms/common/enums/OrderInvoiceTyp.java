package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-25
 */
public enum OrderInvoiceTyp implements ValueDescription<Integer> {
    NO_INVOICE(0, "不开发票"),
    ELECTRONIC_INVOICE(1, "电子发票"),
    PAPER_INVOICE(2, "纸质发票"),
    ;

    private final int value;

    private final String description;

    private static List<OrderInvoiceTyp> VALUES = EnumUtils.getEnumList(OrderInvoiceTyp.class);

    OrderInvoiceTyp(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderInvoiceTyp getEnum(int value) {
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
