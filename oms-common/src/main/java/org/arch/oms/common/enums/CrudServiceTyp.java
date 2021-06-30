package org.arch.oms.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-29
 */
public enum CrudServiceTyp implements ValueDescription<String> {

    ORDER_ADDRESS("order_address", "订单地址"),
    ORDER_CART("order_cart", "购物车"),
    ORDER_FULFIL("order_fulfil", "订单履约"),
    ORDER_INVOICE("order_invoice", "订单发票"),
    ORDER_ITEM_RELISH("order_item_relish", "订单佐料"),
    ORDER_ITEM("order_item", "订单商品行"),
    ORDER_MASTER("order_master", "订单主表"),
    ORDER_PAYMENT("order_payment", "订单支付"),
    ;


    private final String value;

    private final String description;

    private static List<CrudServiceTyp> VALUES = EnumUtils.getEnumList(CrudServiceTyp.class);


    CrudServiceTyp(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static CrudServiceTyp getEnum(int value) {
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
