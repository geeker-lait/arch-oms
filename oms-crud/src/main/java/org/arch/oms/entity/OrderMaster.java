package org.arch.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单-订单主体
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderMaster extends CrudEntity<OrderMaster> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.INPUT)
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
    private String storeNo;


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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
