package com.sn.framework.core.util;

import com.sn.framework.core.common.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * 日期工具类
 *
 * @author HZH
 */
public class DateUtil {
    private static Log log = LogFactory.getLog(DateUtil.class);
    /**
     * 缺省长日期格式,精确到秒
     */
    public static final String DEFAULT_DATETIME_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";
    /**
     * 缺省日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String TIME_LINK_SYMBOL = "-";
    public static final long DAY_MILLI = 24 * 60 * 60 * 1000;
    public static final int DAY_HOUR_COUNT = 24;
    public static final int DAY_MONTH_COUNT = 12;

    private static final int DAY_OF_MONTH[] = {
            31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
            30, 31
    };

    /**
     * 获取指定日期内的随机日期，注意格式应与format保存一致
     *
     * @param beginDate 起始日期（含该日期）例：1949-01-01
     * @param endDate   终止日期（含该日期）例：2009-01-01
     * @param format    返回日期字符串格式 例："yyyy-MM-dd"
     * @return 指定格式日期字符串
     * @author HZH
     */
    public static String randomDate(String beginDate, String endDate, String format) {
        try {
            if (format == null || "".equals(format)) {
                return "";
            }

            //格式化日期
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date start = sdf.parse(beginDate);
            Date end = sdf.parse(endDate);
            end.setHours(23);
            end.setMinutes(59);
            end.setSeconds(59);
            //进行首末日期判断，如果不合法就返回空；
            if (start.getTime() >= end.getTime()) {
                return "";
            }

            long date = random(start.getTime(), end.getTime());

            return sdf.format(new Date(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取随机日期核心方法
     *
     * @param begin
     * @param end
     * @return
     * @author HZH
     */
    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));

        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    /**
     * 将时间格式化为yyyy-MM-dd
     *
     * @param date
     * @return
     * @author HZH
     */
    public static String getDate(Date date) {
        return getSimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }

    /**
     * @param format
     * @param date
     * @return
     * @throws ParseException
     * @author HZH
     */
    public static Date convertStringToDate(String format, String date) throws ParseException {
        return getSimpleDateFormat(format).parse(date);
    }

    /**
     * @param strDate
     * @return
     * @throws ParseException
     * @author HZH
     */
    public static Date convertStringToDate(String strDate) throws ParseException {
        return convertStringToDate(DEFAULT_DATE_FORMAT,strDate);
    }
    /**
     * 获取格式化对象
     *
     * @param strFormat 格式化的格式 如"yyyy-MM-dd"
     * @return 格式化对象
     */
    public static SimpleDateFormat getSimpleDateFormat(String strFormat) {
        if (strFormat != null && !"".equals(strFormat.trim())) {
            return new SimpleDateFormat(strFormat);
        } else {
            return new SimpleDateFormat();
        }
    }
    /**
     * 将时间格式化为HH:mm
     *
     * @param theTime
     * @return
     * @author HZH
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * 获取今天的日期
     *
     * @return
     * @author HZH
     */
    public static Calendar getToday() throws ParseException {
        String todayAsString = getDate(new Date());
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));
        return cal;
    }

    /**
     * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss.S
     *
     * @param mask
     * @return
     * @author HZH
     */
    public static String getNowTime(String mask) {
        Date today = new Date();
        if (mask == null || "".endsWith(mask)) {
            mask = "yyyy-MM-dd HH:mm:ss.S";
        }
        return getDateTime(mask, today);
    }

    /**
     * 将时间格式化
     *
     * @param aMask
     * @param date
     * @return
     * @author HZH
     */
    public static String getDateTime(String aMask, Date date) {
        return getSimpleDateFormat(aMask).format(date);
    }

    /**
     * 将long形时间转成给定的字符串格式
     *
     * @param mi
     * @param mask
     * @return
     * @author HZH
     */
    public static String getDateTime(long mi, String mask) {
        Date date = new Date(mi);
        return getDateTime(mask, date);
    }


    /**
     * 取得两个日期之间的日数
     *
     * @param da 日期
     * @param db 日期
     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
     * @author HZH
     */
    public static long daysBetween(Date da, Date db) {
        if (da == null || db == null) {
            return 0;
        }
        return (db.getTime() - da.getTime()) / DAY_MILLI;
    }

    public static boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        return ((GregorianCalendar) calendar).isLeapYear(year);
    }

    public static Date addDay(Date date, int dayAmount) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, dayAmount);
            return calendar.getTime();
        }
    }

    public static Date addHour(Date date, int hourAmount) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(10, hourAmount);
            return calendar.getTime();
        }
    }

    public static Date addMinute(Date date, int minuteAmount) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(12, minuteAmount);
            return calendar.getTime();
        }
    }

    public static int compareHourAndMinute(Date date, Date anotherDate) {
        if (date == null) {
            date = new Date();
        }
        if (anotherDate == null) {
            anotherDate = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hourOfDay1 = cal.get(11);
        int minute1 = cal.get(12);
        cal.setTime(anotherDate);
        int hourOfDay2 = cal.get(11);
        int minute2 = cal.get(12);
        if (hourOfDay1 > hourOfDay2) {
            return 1;
        }
        if (hourOfDay1 == hourOfDay2) {
            return minute1 <= minute2 ? minute1 != minute2 ? -1 : 0 : 1;
        } else {
            return -1;
        }
    }

    public static int compareIgnoreSecond(Date date, Date anotherDate) {
        if (date == null) {
            date = new Date();
        }
        if (anotherDate == null) {
            anotherDate = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(13, 0);
        cal.set(14, 0);
        date = cal.getTime();
        cal.setTime(anotherDate);
        cal.set(13, 0);
        cal.set(14, 0);
        anotherDate = cal.getTime();
        return date.compareTo(anotherDate);
    }

    public static String currentDate2String() {
        return date2String(new Date());
    }

    public static String currentDate2StringByDay() {
        return date2StringByDay(new Date());
    }

    public static Date currentEndDate() {
        return getEndDate(new Date());
    }

    public static Date currentStartDate() {
        return getStartDate(new Date());
    }

    public static String date2String(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String date2String(Date date, String pattern) {
        if (date == null) {
            return null;
        } else {
            return (new SimpleDateFormat(pattern)).format(date);
        }
    }

    public static String date2StringByDay(Date date) {
        return date2String(date, "yyyy-MM-dd");
    }

    public static String date2StringByMinute(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm");
    }

    public static String date2StringBySecond(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getChineseWeekNumber(String englishWeekName) {
        if ("monday".equalsIgnoreCase(englishWeekName)) {
            return "一";
        }
        if ("tuesday".equalsIgnoreCase(englishWeekName)) {
            return "二";
        }
        if ("wednesday".equalsIgnoreCase(englishWeekName)) {
            return "三";
        }
        if ("thursday".equalsIgnoreCase(englishWeekName)) {
            return "四";
        }
        if ("friday".equalsIgnoreCase(englishWeekName)) {
            return "五";
        }
        if ("saturday".equalsIgnoreCase(englishWeekName)) {
            return "六";
        }
        if ("sunday".equalsIgnoreCase(englishWeekName)) {
            return "日";
        } else {
            return null;
        }
    }

    public static Date getDate(int year, int month, int date) {
        return getDate(year, month, date, 0, 0);
    }

    public static Date getDate(int year, int month, int date, int hourOfDay, int minute) {
        return getDate(year, month, date, hourOfDay, minute, 0);
    }

    public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hourOfDay, minute, second);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }

    public static Date getEndDate(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 23);
            cal.set(12, 59);
            cal.set(13, 59);
            cal.set(14, 999);
            return cal.getTime();
        }
    }

    /**
     * 取一个月中最大的一天
     *
     * @param year
     * @param month
     * @return
     * @author HZH
     */
    public static int getMaxDayOfMonth(int year, int month) {
        if (2 == month && isLeapYear(year)) {
            return 29;
        } else {
            return DAY_OF_MONTH[month - 1];
        }
    }

    public static Date getNextDay(Date date) {
        return addDay(date, 1);
    }

    public static Date getStartDate(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return cal.getTime();
        }
    }

    public static String getTime(Date date) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            return format.format(date);
        }
    }

    public static String getTimeIgnoreSecond(Date date) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(date);
        }
    }

    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(3);
    }

    /**
     * 日期格式化处理方法
     *
     * @param source Date、Calendar、String...
     * @param patt   格式化样式
     * @return
     * @throws Exception 不能正确解析source
     * @author HZH
     */
    public static String dateFormat(Object source, String patt) {
        SimpleDateFormat sdf = new SimpleDateFormat(patt, Locale.CHINESE);
        String ret = "";
        if (source instanceof Date) {
            ret = sdf.format(source);
        } else if (source instanceof Calendar) {
            Calendar c = (Calendar) source;
            ret = sdf.format(c.getTime());
        }
        return ret;
    }

    /**
     * 计算年龄方法
     *
     * @param birthDay
     * @return
     * @throws Exception
     * @author HZH
     */
    public static int computeAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    //do nothing
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        } else {
            //monthNow<monthBirth
            //donothing
        }

        return age;
    }

    /**
     * 根据传入年龄，返回出生日期串，用于进行年龄段范围的日期比较搜索
     * 典型案例：根据用户输入年龄段返回数据库出生日期范围中的结果
     *
     * @param age
     * @param patt
     * @return
     * @author HZH
     */
    public static String ageConvertDate(int age, String patt) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -age);
        SimpleDateFormat sdf = new SimpleDateFormat(patt, Locale.CHINESE);
        return sdf.format(c.getTime());
    }

    /**
     * 获取当前日期时间(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     * @author HZH
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    /**
     * 根据格式获取当前日期
     *
     * @param reg "yyyy-MM-dd"、"yyyy/MM/dd"...
     * @return
     * @author HZH
     */
    public static String getCurrentDate(String reg) {
        SimpleDateFormat sdf = new SimpleDateFormat(reg);
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    /**
     * 取当前年
     *
     * @return
     * @author HZH
     */
    public static String getNowYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    /**
     * 取当前月
     *
     * @return
     * @author HZH
     */
    public static String getNowMonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        return String.valueOf(month + 1);
    }

    /**
     * 取当前日
     *
     * @return
     * @author HZH
     */
    public static String getNowDay() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        return String.valueOf(day);
    }

    /**
     * 取当前星期几
     *
     * @return
     * @author HZH
     */
    public static String getNowDayOfWeek() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return String.valueOf(dayOfWeek - 1);
    }

    /**
     * 获取签名方式日期
     *
     * @return
     */
    public static String getSignatureDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(date);
    }

    /**
     * @param date      字符串日期值
     * @param oldformat 描述字符串日期值格式
     * @param newformat 需要转换的日期格式
     * @return
     * @author lmp
     * @date 2018/9/1 15:57
     * @Description: 字符串日期转指定格式
     */
    public static String formatStringDate(String date, String oldformat, String newformat) {
        SimpleDateFormat oldsdf = new SimpleDateFormat(oldformat);
        SimpleDateFormat newsdf = new SimpleDateFormat(newformat);
        String strDate = "";
        try {
            strDate = newsdf.format(oldsdf.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * @param days 前后几天
     * @return
     * @author lmp
     * @date 2018/9/1 15:57
     * @Description: 获取今天的前几天或后几天日期，days>0 为后几天，<0是前几天
     */
    public static Date previousDay(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 判断日期是否包含在某个日期区间
     * @param targetDate
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean isInDateRange(String targetDate,String beginDate,String endDate){
        if(!Validate.isString(targetDate) || !Validate.isString(beginDate) || !Validate.isString(endDate)){
            return  false;
        }
        try {
            Date beginDateTime = convertStringToDate(DEFAULT_DATETIME_FORMAT_SEC,beginDate+" 00:00:00");
            Date endDateTime = convertStringToDate(DEFAULT_DATETIME_FORMAT_SEC,endDate+" 23:59:59");
            Date targetDateTime = convertStringToDate(DEFAULT_DATE_FORMAT,targetDate);
            long targetTime = targetDateTime.getTime();
            return (targetTime >= beginDateTime.getTime() && targetTime <= endDateTime.getTime());
        }catch (Exception e){

        }
        return false;
    }

    public static void  main(String[] arg){
        try {
            System.out.println(daysBetween(DateUtil.convertStringToDate("2019-03-29"),DateUtil.convertStringToDate("2019-03-30")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
