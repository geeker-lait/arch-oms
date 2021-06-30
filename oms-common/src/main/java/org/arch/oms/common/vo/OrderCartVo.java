package org.arch.oms.common.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-26
 */
public class OrderCartVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 卖家账号ID
     */
    private Long sellerAccountId;

    /**
     * 卖方账号名称
     */
    private String sellerAccountName;

    /**
     * 买方账号ID
     */
    private Long buyerAccountId;

    /**
     * 买方账号名称
     */
    private String buyerAccountName;

    /**
     * 店铺编号
     */
    private Long storeNo;

    /**
     * 商品分类
     */
    private Long productCategoryId;

    /**
     * 商品品牌
     */
    private String productBrand;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品副标题（卖点）
     */
    private String productSubTitle;

    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    private String productAttrs;

    /**
     * 商品主图url
     */
    private String productPic;

    /**
     * 产品sku编号
     */
    private String productSkuNo;

    /**
     * 商品sku码(条码or二维码)用于生成分享
     */
    private String productSkuCode;

    /**
     * 购买数量
     */
    private BigDecimal quantity;

    /**
     * 添加到购物车的价格
     */
    private BigDecimal price;

    /**
     * 状态（下单之后对应商品就不应该显示在购物车了）
     */
    private Integer state;

    /**
     * 时间戳
     */
    private Date dt;

}