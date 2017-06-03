package cs.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtils {
	private static Logger log = Logger.getLogger(DateUtils.class);
	
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String TIME_PATTERN = "HH:mm";
	public static final long DAY_MILLI = 24 * 60 * 60 * 1000;

	public static final int DAY_OF_MONTH[] = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };
		  
	/**
	 * 取随机日期核心方法
	 * @param begin
	 * @param end
	 * @return
	  
	 */
	public static long random(long begin,long end){
	   long rtn = begin + (long)(Math.random() * (end - begin));
	   
	   if(rtn == begin || rtn == end){
		   return random(begin,end);
	   }
	   return rtn;
	}

    public static String getYear(Date date) {
    	if(date == null){
    		return null;
    	}
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(date);
    }   

    /**
     * 将时间格式化为HH:mm
     * @param theTime
     * @return
	  
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss.S
     * @param mask
     * @return
	  
     */
    public static String getNowTime(String mask){
        Date today = new Date();
        if(mask == null || "".endsWith(mask)){
        	mask = "yyyy-MM-dd HH:mm:ss.S";
        }
        return getDateTime(mask, today);
    }

    /**
     * 将时间格式化为yyyy-MM-dd HH:mm:ss.S
     * @param aMask
     * @param aDate
     * @return
	  
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            try{
                df = new SimpleDateFormat(aMask);
                returnValue = df.format(aDate);
            }catch(Exception e){
                log.error("时间格式转换出错 " + e.getMessage());
            }
        }

        return (returnValue);
    }

    /**
     * 将long形时间转成给定的字符串格式
     * @param mi
     * @param mask
     * @return
	  
     */
    public static String getDateTime(long mi, String mask){
        Date date = new Date(mi);

        return getDateTime(mask, date);
    }

    /**
     * 取得两个日期之间的日数
     * @param da 日期
     * @param db 日期
     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
	  
     */
    public static long daysBetween(Date da, Date db){
    	if(da == null || db == null)
    		return 0;
    	return (db.getTime() - da.getTime()) / DAY_MILLI;
    }

   
    
	public static Date addDay(Date date, int dayAmount){
        if(date == null){
            return null;
        } else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, dayAmount);
            return calendar.getTime();
        }
    }

    public static Date addHour(Date date, int hourAmount){
        if(date == null){
            return null;
        } else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(10, hourAmount);
            return calendar.getTime();
        }
    }

    public static Date addMinute(Date date, int minuteAmount){
        if(date == null){
            return null;
        } else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(12, minuteAmount);
            return calendar.getTime();
        }
    }

    public static int compareHourAndMinute(Date date, Date anotherDate){
        if(date == null)
            date = new Date();
        if(anotherDate == null)
            anotherDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hourOfDay1 = cal.get(11);
        int minute1 = cal.get(12);
        cal.setTime(anotherDate);
        int hourOfDay2 = cal.get(11);
        int minute2 = cal.get(12);
        if(hourOfDay1 > hourOfDay2)
            return 1;
        if(hourOfDay1 == hourOfDay2)
            return minute1 <= minute2 ? minute1 != minute2 ? -1 : 0 : 1;
        else
            return -1;
    }

    public static int compareIgnoreSecond(Date date, Date anotherDate){
        if(date == null)
            date = new Date();
        if(anotherDate == null)
            anotherDate = new Date();
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

    public static String currentDate2String(){
        return date2String(new Date());
    }

    public static String currentDate2StringByDay(){
        return date2StringByDay(new Date());
    }

    public static Date currentEndDate(){
        return getEndDate(new Date());
    }

    public static Date currentStartDate(){
        return getStartDate(new Date());
    }

    public static String date2String(Date date){
        return date2String(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String date2String(Date date, String pattern){
        if(date == null)
            return null;
        else
            return (new SimpleDateFormat(pattern)).format(date);
    }

    public static String date2StringByDay(Date date){
        return date2String(date, "yyyy-MM-dd");
    }

    public static String date2StringByMinute(Date date){
        return date2String(date, "yyyy-MM-dd HH:mm");
    }

    public static String date2StringBySecond(Date date){
        return date2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getChineseWeekNumber(String englishWeekName){
        if("monday".equalsIgnoreCase(englishWeekName))
            return "一";
        if("tuesday".equalsIgnoreCase(englishWeekName))
            return "二";
        if("wednesday".equalsIgnoreCase(englishWeekName))
            return "三";
        if("thursday".equalsIgnoreCase(englishWeekName))
            return "四";
        if("friday".equalsIgnoreCase(englishWeekName))
            return "五";
        if("saturday".equalsIgnoreCase(englishWeekName))
            return "六";
        if("sunday".equalsIgnoreCase(englishWeekName))
            return "日";
        else
            return null;
    }

    public static Date getDate(int year, int month, int date){
        return getDate(year, month, date, 0, 0);
    }

    public static Date getDate(int year, int month, int date, int hourOfDay, int minute){
        return getDate(year, month, date, hourOfDay, minute, 0);
    }

    public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hourOfDay, minute, second);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static int getDayOfWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }

    public static Date getEndDate(Date date){
        if(date == null){
            return null;
        } else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 23);
            cal.set(12, 59);
            cal.set(13, 59);
            cal.set(14, 999);
            return cal.getTime();
        }
    }

    public static boolean isLeapYear(int year){
        Calendar calendar = Calendar.getInstance();
        return ((GregorianCalendar)calendar).isLeapYear(year);
    }
    /**
     * 取一个月中最大的一天
     * @param year
     * @param month
     * @return
	  
     */
    public static int getMaxDayOfMonth(int year, int month)
    {
        if(month == 1 && isLeapYear(year))
            return 29;
        else
            return DAY_OF_MONTH[month];
    }

    public static Date getNextDay(Date date){
        return addDay(date, 1);
    }

    public static Date getStartDate(Date date){
        if(date == null){
            return null;
        } else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return cal.getTime();
        }
    }

    public static String getTime(Date date){
        if(date == null){
            return null;
        } else{
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            return format.format(date);
        }
    }

    public static String getTimeIgnoreSecond(Date date){
        if(date == null){
            return null;
        } else{
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(date);
        }
    }

    public static int getWeekOfYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(3);
    }

   
    /**
     * 计算年龄方法
     * @param birthDay 
     * @return
     * @throws Exception 
	  
     */
    public static int computeAge(Date birthDay) throws Exception{
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
     * @param age
     * @param patt
     * @return
	  
     */
    public static String ageConvertDate(int age, String patt){
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.YEAR, -age);
    	SimpleDateFormat sdf = new SimpleDateFormat(patt, Locale.CHINESE);
    	return sdf.format(c.getTime());
    }
    
    /**
     * 获取当前日期时间(yyyy-MM-dd HH:mm:ss)
     * @return
	  
     */
    public static String getCurrentDateTime(){
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
    	Calendar c = Calendar.getInstance();
    	return sdf.format(c.getTime());
    }
    
    /**
     * 根据格式获取当前日期
     * @param reg "yyyy-MM-dd"、"yyyy/MM/dd"...
     * @return
	  
     */
    public static String getCurrentDate(String reg){
    	SimpleDateFormat sdf = new SimpleDateFormat(reg);
    	Calendar c = Calendar.getInstance();
    	return sdf.format(c.getTime());
    }
    
    /**
     * 取当前年
     * @return
	  
     */
    public static String getNowYear(){
    	Calendar c = Calendar.getInstance();
    	int year = c.get(Calendar.YEAR);
    	return String.valueOf(year);
    }

    /**
     * 取当前月
     * @return
	  
     */
    public static String getNowMonth(){
    	Calendar c = Calendar.getInstance();
    	int month = c.get(Calendar.MONTH);
    	return String.valueOf(month + 1);
    }
    
    /**
     * 取当前日
     * @return
	  
     */
    public static String getNowDay(){
    	Calendar c = Calendar.getInstance();
    	int day = c.get(Calendar.DATE);
    	return String.valueOf(day);
    }
    
    /**
     * 取当前星期几
     * @return	  
     */
    public static String getNowDayOfWeek(){
    	Calendar c = Calendar.getInstance();
    	int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    	return String.valueOf(dayOfWeek - 1);
    }
    
    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
     
    /**
     * 获取当年的最后一天
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }
     
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
     
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
         
        return currYearLast;
    }
    
   
	/**
	 * 获取当前年
	 * @return
	 */
	public static int getCurYear(){
		 Calendar now = Calendar.getInstance(); 
		 
		 return now.get(Calendar.YEAR);
	}
	
	/** 
    * 将Date类型时间转换为字符串 (yyyy-MM-dd HH:mm:ss)
    * @param date 
    * @return 
    */
	public static String toStringDateTime(Date date) {    
		if(date == null){
			return null;
		}
        SimpleDateFormat formater = new SimpleDateFormat(DATE_TIME_PATTERN);  
        return formater.format(date);   
       
    }
	/** 
	 * 将Date类型时间转换为字符串 (yyyy-MM-dd)
	 * @param date 
	 * @return 
	 */
	public static String toStringDay(Date date){
		if(date == null){
			return null;
		}
		SimpleDateFormat formater = new SimpleDateFormat(DATE_PATTERN);
		return formater.format(date);
	}
	/** 
	 * 将Date类型时间转换为字符串    小时 
	 * @param date 
	 * @return 
	 */
	public static String toStringHours(Date date){
		String time ;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		time = formater.format(date);
		return time;
	}
	
    
    
    /** 
     * 把日期转为字符串  
     * @param date
     * @param dateFormt
     * @return String
     */  
    public static String converToString(Date date,String dateFormt) { 
    	 String returnValue = "";
    	 if(date == null){
    		  return returnValue;
    	 }
    	 if(!Validate.isString(dateFormt)){
    		dateFormt = DATE_PATTERN;
    	 }
    	 SimpleDateFormat df = new SimpleDateFormat(dateFormt);

    	 return df.format(date);             
    }  
    /** 
     * 把字符串转为日期    
     * @param strDate
     * @param dateFormt
     * @return date
     */  
    public static Date converToDate(String strDate,String dateFormt)  {  
    	if(!Validate.isString(strDate)){
    		return null;
    	}
    	if(!Validate.isString(dateFormt)){
    		dateFormt = DATE_PATTERN;
    	 }
    	Date date = null;
    	SimpleDateFormat df = new SimpleDateFormat(dateFormt);  
    	try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            log.error("ParseException: " + pe);             
        }
        return date;  
    }
	
    
    public static void main(String[] args) throws ParseException {
		String dateString = "2016年6月30日";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	    Date myDate = sdf.parse(dateString);
		System.out.println("<<---" + myDate);
        System.out.println("KKKKKKKK"+converToString(new Date(),"yyyy年MM月dd日"));

	}
}
