package org.arch.oms.common.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 订单查询request
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
@Data
public class OrderInfoQueryRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * appId
     */
    private Long appId;

    /**
     * 店铺编号
     */
    private Long StoreNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单状态
     */
    private List<Long> status;

    /**
     * 订单集合
     */
    private List<Long> orderIdList;

    /**
     * 分页参数
     */
    private PageInfo pageInfo;

    /**
     * 查询订单内容
     */
    private OrderSectionRequest orderSection;


}
