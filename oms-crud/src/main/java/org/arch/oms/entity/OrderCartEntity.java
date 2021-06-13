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
* @description 订单-购物车
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderCartEntity extends CrudEntity<OrderCartEntity>{
    /**
     * 购买数量
     */
    private String quantity;
    /**
     * 商品主图
     */
    private String productPic;
    /**
     * 店铺编号
     */
    private String storeNo;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    private String productAttrs;
    /**
     * 商品分类
     */
    private String productCategoryId;
    /**
     * 买方账号名称
     */
    private String buyerAccountName;
    /**
     * 状态（下单之后对应商品就不应该显示在购物车了）
     */
    private String state;
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
     * 卖方账号ID
     */
    private String sellerAccountId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品副标题（卖点）
     */
    private String productSubTitle;
    /**
     * 产品sku编号
     */
    private String productSkuNo;
    /**
     * 产品品牌
     */
    private String productBrand;
    /**
     * 添加到购物车的价格
     */
    private String price;
    /**
     * 商品sku码(条码or二维码)用于生成分享
     */
    private String productSkuCode;
    /**
     * 应用ID
     */
    private String appId;
}
