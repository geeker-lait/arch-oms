package org.arch.oms.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单-支付记录表
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Data
public class OrderPaymentVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 订单单号
     */
    private Long orderNo;

    /**
     * 用户ID
     */
    private Long accountId;

    /**
     * 用户名
     */
    private String accountName;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付流水号
     */
    private String paySeqNo;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付类型
     */
    private Integer payTyp;

    /**
     * 支付状态
     */
    private Integer payState;

    /**
     * 时间戳
     */
    private Date dt;


}
