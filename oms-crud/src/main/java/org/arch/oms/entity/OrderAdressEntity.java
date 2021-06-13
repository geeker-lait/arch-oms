package org.arch.oms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.arch.framework.crud.CrudEntity;

import java.util.Date;

/**
 * @author lait
 * @description 订单-收发货方信息
 * @date 2021年6月13日 下午7:02:32
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName
public class OrderAdressEntity extends CrudEntity<OrderAdressEntity> {
    /**
     * 区县名称
     */
    private String districtName;
    /**
     * 收or发货人手机
     */
    private String userPhone;
    /**
     * 街道名称
     */
    private String streetName;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 国家
     */
    private String country;
    /**
     * 店铺编号
     */
    private String storeNo;
    /**
     * 市编码
     */
    private String cityNo;
    /**
     * 街道编码
     */
    private String streetNo;
    /**
     * 省简称
     */
    private String provinceShortName;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 时间戳
     */
    private Date dt;
    /**
     * 收or发货人姓名
     */
    private String userName;
    /**
     * 邮编
     */
    private String zipcode;
    /**
     * 省编码
     */
    private String provinceNo;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 类型：1:发货人，2:收货人
     */
    private String userTyp;
    /**
     * 市简称
     */
    private String cityShortName;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 区县编码
     */
    private String districtNo;
    /**
     * 订单号
     */
    private String orderNo;
}
