package cs.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.tools.internal.ws.wsdl.framework.ParseException;

public class DateUtils {

	/** 
    * 将Date类型时间转换为字符串 
    * @param date 
    * @return 
    */
	public static String toString(Date date) {  
        String time;  
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");  
       // formater.applyPattern("yyyy-MM-dd HH:mm:ss");  
        time = formater.format(date);  
        return time;  
       
    }
	/** 
	 * 将Date类型时间转换为字符串     一整天
	 * @param date 
	 * @return 
	 */
	public static String toStringDay(Date date){
		String time ;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		time = formater.format(date);
		return time;
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
     * 按照参数提供的格式将Date类型时间转换为字符串 
     * @param date 
     * @param formaterString 
     * @return   
     */  
    public static String toDateString(Date date, String formaterString) {  
        String time;  
        SimpleDateFormat formater = new SimpleDateFormat();  
        formater.applyPattern(formaterString);  
        time = formater.format(date);  
        return time;  
    }
	/** 
     * 将时间字符串转换为Date类型 
     * @param dateStr 
     * @return Date 
     */  
    public static Date toDate(String dateStr) {  
    	Date date = null;
    	SimpleDateFormat formater = new SimpleDateFormat();
    	formater.applyPattern("yyyy-MM-dd HH:mm:ss");
    	//formater.format("yyyy-MM-dd HH:mm:ss");
    	try {
			date =formater.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return date;
    } 
    /** 
     * 将时间字符串转换为Date类型  天
     * @param dateStr 
     * @return Date 
     */  
    public static Date toDateDay(String dateStr){
    	
    	Date date = null;
    	SimpleDateFormat formater = new SimpleDateFormat();
    	formater.applyPattern("yyyy-MM-dd HH:mm:ss");
    	//formater.format("yyyy-MM-dd HH:mm:ss");
    	try {
			date =formater.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return date;
    }
    
    
    /** 
     * 把日期转为字符串  
     * @param dateStr 
     * @return date
     */  
    public static String ConverToString(Date date)  
    {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
          
        return df.format(date);  
    }  
    /** 
     * 把字符串转为日期    
     * @param dateStr 
     * @return date
     */  
    public static Date ConverToDate(String strDate) throws Exception  
    {  
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return df.parse(strDate);  
    }
	
}
