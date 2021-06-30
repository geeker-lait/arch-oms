package org.arch.oms.common.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-28
 */
@Data
public class OrderSaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品编号集合
     */
    private List<Long> productNoList;

    /**
     * 购物车id集合
     */
    private List<Long> orderCartList;

    /**
     * 收货地址 id
     */
    private Long addressId;

    /**
     * 发票信息
     */
    private InvoiceInfo invoiceInfo;





    @Data
    public static class InvoiceInfo implements Serializable {

        private static final long serialVersionUID = 1L;
        /**
         * 发票邮箱
         */
        private String invoiceEmail;

        /**
         * 发票类型：0->不开发票；1->电子发票；2->纸质发票
         */
        private Integer invoiceTyp;

        /**
         * 发票title
         */
        private String invoiceTitle;

        /**
         * 发票税号
         */
        private String invoiceNo;

        private String remark;

        private String invoiceDetail;
    }


}