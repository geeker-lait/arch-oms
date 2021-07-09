package org.arch.oms.utils;

import org.apache.commons.lang3.StringUtils;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.Constant;
import org.arch.oms.common.ExceptionStatusCode;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * 金额 操作工具类
 */
public final class MoneyUtils {

    private MoneyUtils() {
    }

    public static BigDecimal newMoneyFromLong(long number) {
        BigDecimal bd = new BigDecimal(number);
        return setScale(bd);
    }

    public static BigDecimal newMoney(String number) {
        number = StringUtils.replace(number, ",", "");
        BigDecimal bd = new BigDecimal(number);
        return setScale(bd);
    }


    public static BigDecimal newMoneyFromFloat(float number) {
        return newMoneyFromDouble((double) number);
    }

    public static BigDecimal newMoneyFromDouble(double number) {
        BigDecimal bd = new BigDecimal(number);
        return setScale(bd);
    }

    /**
     * 从int初始化金额BigDecimal
     *
     * @param number 数字
     * @return BigDecimal
     */
    public static BigDecimal newMoneyFromInt(int number) {
        BigDecimal bd = new BigDecimal(number);
        return setScale(bd);
    }

    public static BigDecimal setScale(BigDecimal bd) {
        return bd.setScale(Constant.DEFAULT_SCALE, Constant.DEFAULT_ROUND);
    }

    public static int displayInt(BigDecimal bd) {
        return MoneyUtils.multiply(bd, Constant.HUNDRED_DECIMAL).intValue();
    }

    public static BigDecimal fromDisplayInt(int bd) {
        return divide(newMoneyFromDouble(bd), Constant.HUNDRED_DECIMAL);
    }

    public static String displayMoney(BigDecimal bd) {
        String template = "\u00A5%s";
        return String.format(template, bd.toString());
    }

    public static BigDecimal add(final BigDecimal first, final BigDecimal second) {
        if (first == null || notNull(first).equals(BigDecimal.ZERO)) {
            return notNull(second);
        } else if (second == null || notNull(second).equals(BigDecimal.ZERO)) {
            return notNull(first);
        } else {
            return first.add(second).setScale(Constant.DEFAULT_SCALE, Constant.DEFAULT_ROUND);
        }
    }

    public static BigDecimal sum(BigDecimal... bds) {
        BigDecimal sum = Constant.ZERO_DECIMAL;
        if (bds == null) {
            return sum;
        }
        for (BigDecimal bd : bds) {
            sum = MoneyUtils.add(bd, sum);
        }
        return sum;
    }

    public static BigDecimal substract(BigDecimal first, BigDecimal second) {
        if (first == null) {
            first = BigDecimal.ZERO;
        }
        if (second == null) {
            second = BigDecimal.ZERO;
        }
        return first.subtract(second).setScale(Constant.DEFAULT_SCALE, Constant.DEFAULT_ROUND);
    }

    public static BigDecimal multiply(BigDecimal first, BigDecimal second) {
        if (first == null || second == null) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("qty can't be null value"));
        }
        return first.multiply(second).setScale(Constant.DEFAULT_SCALE, Constant.DEFAULT_ROUND);
    }

    public static BigDecimal divide(BigDecimal first, BigDecimal second) {
        if (first == null || second == null) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("qty can't be null value"));
        }
        return first.divide(second, Constant.DEFAULT_SCALE, Constant.DEFAULT_ROUND);
    }

    /**
     * 根据促销价和原价，返回文本类型的折扣比例，精确到一位小数
     *
     * @param firstAmount  折扣金额，单位分
     * @param secondAmount 原始金额，单位分
     * @return 折扣比例 x.x折 文本
     */
    public static String makeDiscountPercentText(Integer firstAmount, Integer secondAmount) {
        if (secondAmount == 0) {
            return null;
        }

        BigDecimal first = newMoneyFromInt(firstAmount);
        BigDecimal second = newMoneyFromInt(secondAmount);
        return makeDiscountPercentText(first, second);
    }

    /**
     * 根据促销价和原价，返回文本类型的折扣比例，精确到一位小数
     *
     * @param firstAmount  折扣金额，单位分
     * @param secondAmount 原始金额，单位分
     * @return 折扣比例 x.x折 文本
     */
    public static String makeDiscountPercentText(BigDecimal firstAmount, BigDecimal secondAmount) {
        BigDecimal divide = divide(firstAmount, secondAmount);
        BigDecimal multiply = multiply(divide, newMoneyFromInt(10));
        return MessageFormat.format("{0}折", multiply.setScale(1, Constant.DEFAULT_ROUND));
    }


    /**
     * Get max value from given values.
     *
     * @param first  first given
     * @param second second value
     * @return max value.
     */
    public static BigDecimal max(final BigDecimal first, final BigDecimal second) {
        if (isFirstBiggerThanSecond(
                notNull(first),
                notNull(second))) {
            return notNull(first);
        }
        return notNull(second);
    }

    /**
     * Get minimal, but greater than 0 value from given values.
     *
     * @param first  first given
     * @param second second value
     * @return max value.
     */
    public static BigDecimal minPositive(final BigDecimal first, final BigDecimal second) {
        if (first == null || notNull(first).equals(BigDecimal.ZERO)) {
            return notNull(second);
        } else if (second == null || notNull(second).equals(BigDecimal.ZERO)) {
            return notNull(first);
        } else if (isFirstBiggerThanSecond(
                notNull(first),
                notNull(second))) {
            return notNull(second);
        }
        return notNull(first);
    }

    /**
     * @param value value to check
     * @return value if it not null, otherwise BigDecimal.ZERO
     */
    public static BigDecimal notNull(final BigDecimal value) {
        return notNull(value, null);
    }

    /**
     * @param value  value to check
     * @param ifNull value to return if value to check is null
     * @return value or ifNull if value is null. if ifNull is null returns BigDecimal.ZERO.
     */
    public static BigDecimal notNull(final BigDecimal value, final BigDecimal ifNull) {
        if (value == null) {
            if (ifNull == null) {
                return BigDecimal.ZERO;
            }
            return ifNull;
        }
        return value;
    }


    /**
     * @param first  value
     * @param second value
     * @return true if first is greater than second (null safe)
     */
    public static boolean isFirstBiggerThanSecond(final BigDecimal first, final BigDecimal second) {

        if (first == null && second == null) {
            return false;
        } else if (second == null) {
            return true;
        } else if (first == null) {
            return false;
        }
        return first.compareTo(second) > 0;

    }

    public static boolean isZero(final BigDecimal first) {
        if (first == null) {
            return false;
        }

        return isFirstEqualToSecond(first, Constant.ZERO_DECIMAL);

    }

    public static boolean isBiggerThanZero(final BigDecimal first) {
        return first != null && isFirstBiggerThanSecond(first, Constant.ZERO_DECIMAL);

    }

    public static boolean isFirstBiggerThanOrEqualToZero(final BigDecimal first) {
        return first != null && isFirstBiggerThanOrEqualToSecond(first, Constant.ZERO_DECIMAL);

    }

    /**
     * @param first  value
     * @param second value
     * @return true if first is greater than or equal to second (null safe)
     */
    public static boolean isFirstBiggerThanOrEqualToSecond(final BigDecimal first, final BigDecimal second) {
        if (first == null && second == null) {
            return false;
        } else if (second == null) {
            return true;
        } else if (first == null) {
            return false;
        }

        return first.compareTo(second) >= 0;
    }

    /**
     * @param first  value
     * @param second value
     * @return true if first is equal to second (null safe)
     */
    public static boolean isFirstEqualToSecond(final BigDecimal first, final BigDecimal second) {
        if (first == null && second == null) {
            return false;
        } else if (second == null) {
            return false;
        } else if (first == null) {
            return false;
        }

        return first.compareTo(second) == 0;
    }

    /**
     * @param first  value
     * @param second value
     * @param scale  scale
     * @return true if first is equal to second (null safe)
     */
    public static boolean isFirstEqualToSecond(final BigDecimal first, final BigDecimal second, final int scale) {
        if (first == null && second == null) {
            return false;
        } else if (second == null) {
            return false;
        } else if (first == null) {
            return false;
        }
        return first.setScale(scale).compareTo(second.setScale(scale)) == 0;

    }

    /**
     * Get discount as percentage.
     * E.g. original 100.00, discounted 80.0 - the result will be 80 (%)
     * E.g. original 100.00, discounted 80.99 - the result will be 80 (%)
     *
     * @param original   original price
     * @param discounted discounted price
     * @return discount in percent
     */
    public static BigDecimal getDiscountDisplayValue(final BigDecimal original, final BigDecimal discounted) {
        if (original == null || discounted == null) {
            return BigDecimal.ZERO;
        }
        return discounted.multiply(Constant.HUNDRED_DECIMAL)
                .divide(original, Constant.DEFAULT_ROUND).setScale(0, Constant.DEFAULT_ROUND);
    }

    public static BigDecimal displayQty(BigDecimal qty) {
        if (qty == null) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("qty can't be null value"));
        }
        return qty.multiply(Constant.HUNDRED_DECIMAL).setScale(Constant.DEFAULT_SCALE, Constant.DEFAULT_ROUND);
    }

    /**
     * 将数字转换为取出所有末尾0的自然数表达的文字，比如 2.00 -> 2 2.10 -> 2.1
     * 如果数字为null，返回空字符串
     *
     * @param amount 数字
     * @return 字符串表达
     */
    public static String toNoTrailingZerosString(BigDecimal amount) {
        if (amount == null) {
            return StringUtils.EMPTY;
        }

        return amount.stripTrailingZeros().toPlainString();
    }

    /**
     * 是否是小数  null: false   小数: true  整数: false
     * @param number
     * @return
     */
    public static boolean hasDecimals(BigDecimal number) {
        if ("".equals(number) && number == null) {
            return false;
        }
        return new BigDecimal(number.intValue()).compareTo(number) == 0 ? false : true;
    }
}
