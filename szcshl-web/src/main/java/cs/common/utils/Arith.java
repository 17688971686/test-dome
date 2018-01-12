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
     * @param v1    被除数
     * @param v2    除数
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
     * @param v     需要四舍五入的数字
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

    /**
     * BigDecimal的加法运算封装
     *
     * @param b1
     * @param bn
     * @return
     * @author : shijing
     * 2017年3月23日下午4:53:21
     */
    public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
        if (null == b1) {
            b1 = BigDecimal.ZERO;
        }
        if (null != bn) {
            for (BigDecimal b : bn) {
                b1 = b1.add(null == b ? BigDecimal.ZERO : b);
            }
        }
        return b1;
    }


    /**
     * 计算金额方法
     *
     * @param b1
     * @param bn
     * @return
     */
    public static BigDecimal safeSubtract(BigDecimal b1, BigDecimal... bn) {
        return safeSubtract(true, b1, bn);
    }

    /**
     * BigDecimal的安全减法运算
     *
     * @param isZero 减法结果为负数时是否返回0，true是返回0（金额计算时使用），false是返回负数结果
     * @param b1     被减数
     * @param bn     需要减的减数数组
     * @return
     */
    public static BigDecimal safeSubtract(Boolean isZero, BigDecimal b1, BigDecimal... bn) {
        if (null == b1) {
            b1 = BigDecimal.ZERO;
        }
        BigDecimal r = b1;
        if (null != bn) {
            for (BigDecimal b : bn) {
                r = r.subtract((null == b ? BigDecimal.ZERO : b));
            }
        }
        return isZero ? (r.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : r) : r;
    }

    /**
     * 金额除法计算，返回2位小数（具体的返回多少位大家自己看着改吧）
     *
     * @param b1
     * @param b2
     * @return
     */
    public static <T extends Number> BigDecimal safeDivide(T b1, T b2) {
        return safeDivide(b1, b2, BigDecimal.ZERO);
    }

    /**
     * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值
     * 默认返回小数位后2位，用于金额计算
     *
     * @param b1
     * @param b2
     * @param defaultValue
     * @return
     */
    public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
        if (null == b1 || null == b2) {
            return defaultValue;
        }
        try {
            return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * BigDecimal的乘法运算封装
     *
     * @param b1
     * @param b2
     * @return
     */
    public static <T extends Number> BigDecimal safeMultiply(T b1, T b2) {
        if (null == b1 || null == b2) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    //保留2位小数
    public static double get2Double(BigDecimal a) {
        DecimalFormat df = new DecimalFormat("#.00");
        return new Double(df.format(a.setScale(2)).toString());
    }

    /**
     * 计算专家税费
     * 	800<X≤4000时：（所得额-800）*20%——如果是1000元，就是缴税40
     * 	4000<X≤20000时:所得额*(1-20%)*20%
     * 	20000＜X≤50000时：所得额*（1-20%）*30%-2000
     * 	超过50000忘记是多少了，我再问财务找一个那个文件
     *
     * @param expense
     * @return
     */
    public static BigDecimal countCost(BigDecimal expense) {
        BigDecimal returnCost = BigDecimal.ZERO;
        if (expense == null || expense.compareTo(BigDecimal.ZERO) == -1) {
            return returnCost;
        }
        double totalValue = expense.doubleValue();
        if (totalValue > 800 && totalValue <= 4000) {
            returnCost = safeDivide((totalValue - 800) * 20, 100);
        } else if (totalValue > 4000 && totalValue <= 20000) {
            returnCost = safeDivide(totalValue * 80 * 20, 10000);
        } else if (totalValue > 20000 && totalValue <= 50000) {
            returnCost = safeDivide(totalValue * 80 * 30, 10000);
            returnCost = safeSubtract(returnCost, new BigDecimal(2000));
        } else if (totalValue > 50000) {
            returnCost = safeDivide(totalValue * 80 * 30, 10000);
            returnCost = safeSubtract(returnCost, new BigDecimal(2755));
        }

        return returnCost;
    }

    public static void main(String[] args) {
        /*System.out.println(countCost(new BigDecimal(500)));
        System.out.println(countCost(new BigDecimal(800)));
        System.out.println(countCost(new BigDecimal(900)));
        System.out.println(countCost(new BigDecimal(2000)));
        System.out.println(countCost(new BigDecimal(4000)));
        System.out.println(countCost(new BigDecimal(10000)));
        System.out.println(countCost(new BigDecimal(20000)));
        System.out.println(countCost(new BigDecimal(40000)));
        System.out.println(countCost(new BigDecimal(50000)));
        System.out.println(countCost(new BigDecimal(60000)));*/

        System.out.println(get2Double(new BigDecimal(60000)));
        System.out.println((new BigDecimal(60000)).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}
