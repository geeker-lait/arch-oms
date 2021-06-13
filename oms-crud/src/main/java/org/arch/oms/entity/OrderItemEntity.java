package org.arch.oms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.util.Date;

/**
 * @author lait
 * @description 订单-销售订单项
 * @date 2021年6月13日 下午7:02:32
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderItemEntity extends CrudEntity<OrderItemEntity> {
    /**
     * 销售价格
     */
    private String productPrice;
    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    private String productAttr;
    /**
     * 店铺编号
     */
    private String storeNo;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 商品sku条码
     */
    private String productSkuCode;
    /**
     * 产品编号
     */
    private String productNo;
    /**
     * 购买数量
     */
    private String productQuantity;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 商品分类id
     */
    private String productCategoryId;
    /**
     * 产品品牌
     */
    private String productBrand;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商品sku编号
     */
    private String productSkuNo;
    /**
     * 产品图片
     */
    private String productPic;
    /**
     * 应用ID
     */
    private String appId;
}
