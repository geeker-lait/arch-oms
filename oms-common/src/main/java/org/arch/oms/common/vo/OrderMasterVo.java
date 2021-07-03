package org.arch.oms.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-02
 */
@Data
public class OrderMasterVo implements Serializable {
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
     * 卖家账号ID
     */
    private Long sellerAccountId;

    /**
     * 卖方账号名称
     */
    private String sellerAccountName;

    /**
     * 买方账号ID
     */
    private Long buyerAccountId;

    /**
     * 买方账号名称
     */
    private String buyerAccountName;

    /**
     * 店铺编号
     */
    private Long storeNo;

    /**
     * 单表：销售订单；秒杀订单，采购订单，用表名来表示
     */
    private String orderTable;

    /**
     * 订单来源：0->PC订单；1->app订单
     */
    private Integer orderSource;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单状态
     */
    private Integer orderState;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 确认收货时间
     */
    private Date receiveTime;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 应付金额（实际支付金额）
     */
    private BigDecimal payAmount;

    /**
     * 支付方式
     */
    private Integer payTyp;

    /**
     * 时间戳
     */
    private Date dt;
}
