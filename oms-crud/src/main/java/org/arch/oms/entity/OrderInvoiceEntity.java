package org.arch.oms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @description 订单-发票
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderInvoiceEntity extends CrudEntity<OrderInvoiceEntity>{
    /**
     * 发票抬头：个人/公司
     */
    private String invoiceTitle;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 店编
     */
    private String storeNo;
    /**
     * 开票明细
     */
    private String invoiceItem;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 发票类型：0->不开发票；1->电子发票；2->纸质发票
     */
    private String invoiceType;
    /**
     * 说明
     */
    private String remark;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 开票金额
     */
    private String amount;
    /**
     * 发表税号
     */
    private String invoiceNo;
    /**
     * 用户邮箱用来接受字典发票
     */
    private String userEamil;
    /**
     * 应用ID
     */
    private String appId;
}
