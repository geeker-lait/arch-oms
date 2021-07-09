package org.arch.oms.entity;

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
 * 订单-发票
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderInvoice extends CrudEntity<OrderInvoice> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 店编
     */
    private String storeNo;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 发票类型：0->不开发票；1->电子发票；2->纸质发票
     */
    private Integer invoiceTyp;

    /**
     * 发票抬头：个人/公司
     */
    private String invoiceTitle;

    /**
     * 发票税号
     */
    private String invoiceNo;

    /**
     * 开票金额
     */
    private BigDecimal amount;

    /**
     * 说明
     */
    private String remark;

    /**
     * 开票明细
     */
    private String invoiceItem;

    /**
     * 用户邮箱用来接受字典发票
     */
    private String receiveEmail;

    /**
     * 时间戳
     */
    private Date dt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
