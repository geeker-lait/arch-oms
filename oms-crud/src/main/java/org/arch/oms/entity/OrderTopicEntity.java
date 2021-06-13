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
* @description 订单-评价
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderTopicEntity extends CrudEntity<OrderTopicEntity>{
    /**
     * 内容
     */
    private String content;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 评论分数
     */
    private String score;
    /**
     * 评价时间
     */
    private Date commentTime;
    /**
     * 买方账号名称
     */
    private String buyerAccountName;
    /**
     * 买方账号ID
     */
    private String buyerAccountId;
    /**
     * 图片列表
     */
    private String pics;
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
     * 单号
     */
    private String orderNo;
    /**
     * 卖方账号ID
     */
    private String sellerAccountId;
    /**
     * 点赞数
     */
    private String point;
    /**
     * 应用ID
     */
    private String appId;
}
