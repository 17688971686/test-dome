package cs.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程工具类
 * @author ldm
 *
 */
public class ActivitiUtil {

	/**
	 * 封装流程参数
	 * @param map
	 * @param users
	 * @param groups
	 * @param isClear
	 * @return
	 */
	public static Map<String,Object> flowArguments(Map<String,Object> map,String users,String groups,boolean isClear){
		if(map == null){
			map = new HashMap<String,Object>(2);
		}else{
			if(isClear){
				map.clear();
			}
		}
		
		if(Validate.isString(users)){
			map.put("users", users);
		}
		if(Validate.isString(groups)){
			map.put("groups", groups);
		}
		return map;
	}
	
	/**
	 * 毫秒转化时分秒毫秒 
	 * @param ms
	 * @return
	 */ 
	public static String formatTime(Long ms) {  
		if(ms == null){
			 return "";
		}
	    Integer ss = 1000;  
	    Integer mi = ss * 60;  
	    Integer hh = mi * 60;  
	    Integer dd = hh * 24;  
	  
	    Long day = ms / dd;  
	    Long hour = (ms - day * dd) / hh;  
	    Long minute = (ms - day * dd - hour * hh) / mi;  
	    Long second = (ms - day * dd - hour * hh - minute * mi) / ss;  
	    Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;  
	      
	    StringBuffer sb = new StringBuffer();  
	    if(day > 0) {  
	        sb.append(day+"天");  
	    }  
	    if(hour > 0) {  
	        sb.append(hour+"小时");  
	    }  
	    if(minute > 0) {  
	        sb.append(minute+"分");  
	    }  
	    if(second > 0) {  
	        sb.append(second+"秒");  
	    }  
	    if(milliSecond > 0) {  
	        sb.append(milliSecond+"毫秒");  
	    }  
	    return sb.toString();  
	}  
}
