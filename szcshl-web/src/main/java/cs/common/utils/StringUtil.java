package cs.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 * @author ldm
 *
 */
public class StringUtil {

	public static String getSubString(String str,int begin,int end){
		return new String(str.substring(begin, end));
	}
	
	
	/**
	 * @param splitStr
	 * @param splitChars
	 * @return
	 */
	public static List<String> getSplit(String splitStr,String splitChars){
		List<String> resultArray = null;
		if(Validate.isString(splitStr)){
			resultArray = new ArrayList<String>();
			while(true){
				int index = splitStr.indexOf(splitChars);
				if(index < 0){
					resultArray.add(splitStr);
					break;
				}
				resultArray.add(splitStr.substring(0, index));
				splitStr = splitStr.substring(index+1);
			}
		}
		return resultArray;
	}
	
	public static void main(String[] args){
		String s="sdgdsagds,asdgasdgsd,asdgsadg,adsgadsg,sd";		
		List<String> re = getSplit(s,",");
		for(String d : re){
			System.out.println(d);
		}
	}
}
