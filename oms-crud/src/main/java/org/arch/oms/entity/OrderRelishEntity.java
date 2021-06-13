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
* @description 订单-佐料
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderRelishEntity extends CrudEntity<OrderRelishEntity>{
    /**
     * 促销优化金额（促销价、满减、阶梯价）
     */
    private String promotionAmount;
    /**
     * 该商品经过优惠后的分解金额
     */
    private String realAmount;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 可以活动的成长值
     */
    private String growth;
    /**
     * 商品促销名称
     */
    private String promotionName;
    /**
     * 订单项ID
     */
    private String orderItemId;
    /**
     * 管理员后台调整订单使用的折扣金额
     */
    private String discountAmount;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 店编
     */
    private String storeNo;
    /**
     * 成长值
     */
    private String giftGrowth;
    /**
     * 优惠券抵扣金额
     */
    private String couponAmount;
    /**
     * 积分抵扣金额
     */
    private String integrationAmount;
    /**
     * 积分
     */
    private String giftIntegration;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商品sku编号
     */
    private String productSkuNo;
    /**
     * 可以获得的积分
     */
    private String integration;
    /**
     * 下单时使用的积分
     */
    private String useIntegration;
    /**
     * 应用ID
     */
    private String appId;
}
