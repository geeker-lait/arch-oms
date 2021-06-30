package org.arch.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 订单-销售订单项
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderItem extends CrudEntity<OrderItem> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 应用ID
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
     * 产品编号
     */
    private Long productNo;

    /**
     * 产品图片
     */
    private String productPic;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品品牌
     */
    private String productBrand;

    /**
     * 销售价格
     */
    private BigDecimal productPrice;

    /**
     * 购买数量
     */
    private BigDecimal productQuantity;

    /**
     * 商品sku编号
     */
    private String productSkuNo;

    /**
     * 商品sku条码
     */
    private String productSkuCode;

    /**
     * 商品分类id
     */
    private Long productCategoryId;

    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    private String productAttr;

    /**
     * 时间戳
     */
    private Date dt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
