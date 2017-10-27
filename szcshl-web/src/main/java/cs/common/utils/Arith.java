package cs.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * <p>
 * 确的浮点数运算，包括加减乘除和四舍五入。
 */

public class Arith {

    // 默认除法运算精度

    private static final int DEF_DIV_SCALE = 10;

    // 这个类不能实例化

    private Arith() {

    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();

    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();

    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static double mul(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.multiply(b2).doubleValue();

    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static double mul(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.multiply(b2).doubleValue();

    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * <p>
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {

        return div(v1, v2, DEF_DIV_SCALE);

    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * <p>
     * 定精度，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位 。
     * @return 两个参数的商
     */

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    // 向下取整
    public static double trunc(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        DecimalFormat myFormatter = new DecimalFormat("####.######");
        String value = myFormatter.format(v);
        if (value.indexOf(".") > 0) {
            value = value + "000000000000000000";
            value = value.substring(0, value.indexOf(".") + scale + 1);
        } else {
            String tempStr = "00000000000000000000000";
            value = value + tempStr.substring(0, scale);
        }
        return Double.parseDouble(value);
    }

    /**
     * 格式化字符为Double
     *
     * @param value
     * @return
     */
    public static Double format2Double(String value) {
        Double result = 0D;
        try {
            value = value == null ? "0" : value.trim();
            result = Double.parseDouble(value);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 格式化字符为Long
     *
     * @param value
     * @return
     */
    public static Long format2Long(String value) {
        Long result = 0L;
        try {
            value = value == null ? "0" : value.trim();
            if (value.indexOf(".") > 0) {
                value = value.substring(0, value.indexOf("."));
            }
            result = Long.parseLong(value);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 格式化金额
     *
     * @param amount 金额字符串
     * @return
     */
    public static String formatAmount(String amount) {
        int intStringLength = 0;
        String strsubstring = "";
        String strEnd = "";
        String strStart = amount;
        if (amount.indexOf(".") != -1) {
            strStart = amount.substring(0, amount.indexOf("."));
            strEnd = amount.substring(amount.indexOf("."), amount.length());
        }
        intStringLength = strStart.length();

        if (intStringLength % 3 == 0) {
            for (int i = intStringLength; i > 2; i -= 3) {
                strsubstring = "," + strStart.substring(i - 3, i) + strsubstring;
            }
            strsubstring = strsubstring.substring(1, strsubstring.length());
        } else {
            for (int i = intStringLength; i > 2; i -= 3) {
                strsubstring = "," + strStart.substring(i - 3, i) + strsubstring;
            }
            strsubstring = strStart.substring(0, intStringLength % 3) + strsubstring;
        }
        if (strEnd.indexOf(".") < 0) {
            strEnd = strEnd + ".00";
        }
        return strsubstring + strEnd;
    }

    /**
     * 格式化金额,保留小数点
     *
     * @param amount
     * @param len
     * @return
     */
    public static String formatAmountPoint(String amount, int len) {
        String formatValue = amount;
        try {
            if (formatValue.indexOf(".") != -1) {
                String intValue = formatValue.substring(0, formatValue.indexOf("."));
                String pointValue = formatValue.substring(formatValue.indexOf(".") + 1, formatValue.length());
                pointValue = pointValue + "000000000000";
                pointValue = pointValue.substring(0, len);

                formatValue = intValue + "." + pointValue;
            } else {
                formatValue = formatValue + ".00";
            }
        } catch (Exception e) {
        }
        return formatValue;
    }

    public static String formatAmountPoint(Double amount, int len) {
        String formatValue = amount.toString();
        try {
            Double newAmount = round(amount, len);
            if (newAmount.equals(0D)) {
                return "0.00";
            }
            formatValue = newAmount.toString();
            if (formatValue.indexOf(".") != -1) {
                String intValue = formatValue.substring(0, formatValue.indexOf("."));
                String pointValue = formatValue.substring(formatValue.indexOf(".") + 1, formatValue.length());
                pointValue = pointValue + "000000000000";
                pointValue = pointValue.substring(0, len);

                formatValue = intValue + "." + pointValue;
            } else {
                formatValue = formatValue + ".00";
            }
        } catch (Exception e) {
        }
        return formatValue;
    }

}
