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
 * 订单-履约信息（快递or物流or其他）
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderFulfil extends CrudEntity<OrderCart> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 店铺编号
     */
    private Long storeNo;

    /**
     * 订单编号
     */
    private Long orderNo;

    /**
     * 履约类型
     */
    private Integer fulfilTyp;

    /**
     * 履约单号
     */
    private String fulfilNo;

    /**
     * 费用
     */
    private BigDecimal fulfilFee;

    /**
     * 发货时间
     */
    private Date fulfilTime;

    /**
     * 送达时间
     */
    private Date arrivalTime;

    /**
     * 时间戳
     */
    private Date dt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
