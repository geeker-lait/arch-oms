package org.arch.oms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单商品行-佐料
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderItemRelish extends CrudEntity<OrderItemRelish> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 店编
     */
    private Long storeNo;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 订单项ID
     */
    private Long orderItemId;

    /**
     * 可以获得的积分
     */
    private Integer integration;

    /**
     * 可以活动的成长值
     */
    private Integer growth;

    /**
     * 下单时使用的积分
     */
    private Integer useIntegration;

    /**
     * 促销优化金额（促销价、满减、阶梯价）
     */
    private BigDecimal promotionAmount;

    /**
     * 积分抵扣金额
     */
    private BigDecimal integrationAmount;

    /**
     * 优惠券抵扣金额
     */
    private BigDecimal couponAmount;

    /**
     * 管理员后台调整订单使用的折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 商品sku编号
     */
    private String productSkuNo;

    /**
     * 商品促销名称
     */
    private String promotionName;

    /**
     * 该商品经过优惠后金额具体金额
     */
    private BigDecimal realAmount;

    /**
     * 时间戳
     */
    private Date dt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
