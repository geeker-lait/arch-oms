package org.arch.oms.common;

import java.math.BigDecimal;

/**
 * 常量信息存放
 * @author junboXiang
 * @version V1.0
 * 2021-07-03
 */
public class Constant {

    /**
     *  BigDecimal 四舍五入
     */
    public static final int DEFAULT_ROUND = BigDecimal.ROUND_HALF_UP;

    /**
     * 保留2位小数
     */
    public static final int DEFAULT_SCALE = 2;

    /**
     * BigDecimal 0
     */
    public static final BigDecimal ZERO_DECIMAL = new BigDecimal("0.00").setScale(Constant.DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);

    /**
     * BigDecimal 100
     */
    public static final BigDecimal HUNDRED_DECIMAL = new BigDecimal("100.00").setScale(Constant.DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);
}
