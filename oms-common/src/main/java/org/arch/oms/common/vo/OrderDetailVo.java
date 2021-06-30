package org.arch.oms.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品详情（订单商品行） VO
 *  暂时就一重， 后续决定要拆分还是
 * @author junboXiang
 * @version V1.0
 * 2021-06-27
 */
@Data
public class OrderDetailVo implements Serializable {
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


}
