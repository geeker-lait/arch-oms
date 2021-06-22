package org.arch.oms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单-收发货方信息
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderAddress extends CrudEntity<OrderAddress> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
     * 收or发货人姓名
     */
    private String userName;

    /**
     * 收or发货人手机
     */
    private String userPhone;

    /**
     * 类型：1:发货人，2:收货人
     */
    private Integer userTyp;

    /**
     * 国家
     */
    private String country;

    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 省编码
     */
    private String provinceNo;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 省简称
     */
    private String provinceShortName;

    /**
     * 市编码
     */
    private String cityNo;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 市简称
     */
    private String cityShortName;

    /**
     * 区县编码
     */
    private String districtNo;

    /**
     * 区县名称
     */
    private String districtName;

    /**
     * 街道编码
     */
    private String streetNo;

    /**
     * 街道名称
     */
    private String streetName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 时间戳
     */
    private Date dt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
