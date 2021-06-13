package org.arch.oms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.util.Date;

/**
 * @author lait
 * @description 订单-履约信息（快递or物流or其他）
 * @date 2021年6月13日 下午7:02:32
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderFulfilEntity extends CrudEntity<OrderFulfilEntity> {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 店编
     */
    private String storeNo;
    /**
     * 履约类型：快递，物流，闪送
     */
    private String fulfilType;
    /**
     * 发货时间
     */
    private Date fulfilTime;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 费用
     */
    private String fulfilFee;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 送达时间
     */
    private Date arrivalTime;
    /**
     * 履约单号
     */
    private String fulfilNo;
    /**
     * 应用ID
     */
    private String appId;
}
