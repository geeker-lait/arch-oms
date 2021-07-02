package org.arch.oms.common.vo;

import lombok.Data;

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
public class OrderInvoiceVo {

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

}
