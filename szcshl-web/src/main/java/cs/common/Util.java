package cs.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static String formatDate(Date date) {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
	}
	
	/**
	 * 把id数组封装成查询 的in 条件
	 * @param idArr
	 * @param type(0:表示整数型，1表示字符串)
	 * @return
	 */
	public static String idArrToQueryInString(String[] idArr,int type){			
		if(type == 0){
			return intArrToInString(idArr);
		}else{
			return strArrToInString(idArr);
		}
		
	}
	
	public static String intArrToInString(String[] idArr){
		String resultStr = "";
		int totalL = idArr.length;
    	for(int i=0;i<totalL;i++){
    		if(i == totalL-1){
    			resultStr += idArr[i];
    		}else{
    			resultStr += idArr[i]+",";
    		}
    	}
    	return resultStr;
	}
	
	public static String strArrToInString(String[] idArr){
		String resultStr = "";
		int totalL = idArr.length;
    	for(int i=0;i<totalL;i++){
    		if(i == totalL-1){
    			resultStr += "'"+idArr[i]+"' ";
    		}else{
    			resultStr += "'"+idArr[i]+"',";
    		}
    	}
    	return resultStr;
	}
}
