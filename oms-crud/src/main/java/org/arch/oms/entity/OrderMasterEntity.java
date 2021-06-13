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
* @description 订单-订单主体
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderMasterEntity extends CrudEntity<OrderMasterEntity>{
    /**
     * 订单金额
     */
    private String orderAmount;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 确认收货时间
     */
    private Date receiveTime;
    /**
     * 订单表：销售订单；秒杀订单，采购订单，用表名来表示......
     */
    private String orderTable;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 订单来源：0->PC订单；1->app订单
     */
    private String orderSource;
    /**
     * 发货时间
     */
    private Date deliveryTime;
    /**
     * 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
     */
    private String orderState;
    /**
     * 买方账号名称
     */
    private String buyerAccountName;
    /**
     * 买方账号ID
     */
    private String buyerAccountId;
    /**
     * 卖方账号名称
     */
    private String sellerAccountName;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 店编
     */
    private String storeNo;
    /**
     * 应付金额（实际支付金额）
     */
    private String payAmount;
    /**
     * 卖方账号ID
     */
    private String sellerAccountId;
    /**
     * 订单号(分布式id生成)
     */
    private String orderNo;
    /**
     * 支付方式：1->支付宝；2->微信，3：三方
     */
    private String payTyp;
    /**
     * 应用ID
     */
    private String appId;
}
