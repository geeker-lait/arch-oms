package org.arch.oms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.util.Date;

/**
 * @author lait
 * @description 订单-支付记录表
 * @date 2021年6月13日 下午7:02:32
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderPaymentEntity extends CrudEntity<OrderPaymentEntity> {
    /**
     * 支付状态
     */
    private String payState;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 支付流水号
     */
    private String paySeqno;
    /**
     * 用户
     */
    private String userName;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 支付类型
     */
    private String payTyp;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 支付金额
     */
    private String amount;
    /**
     * 手机号
     */
    private String phoneNo;
    /**
     * 订单单号
     */
    private String orderNo;
}
