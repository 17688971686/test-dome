package cs.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 * @author ldm
 *
 */
public class StringUtil extends StringUtils {

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

    public static String getString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            //用这个的话，只能输出来一次结果，第二次显示为空
            // charBuffer = decoder.decode(buffer);
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String upperCaseFirst(String str) {
		if (isBlank(str)) {
			return str;
		}
		char f = str.charAt(0);
		if(Character.isUpperCase(f))
			return str;
		else
			return (new StringBuilder()).append(Character.toUpperCase(f)).append(str.substring(1)).toString();
	}

	public static String lowerCaseFirst(String str) {
		if (isBlank(str)) {
			return str;
		}
		char f = str.charAt(0);
		if(Character.isUpperCase(f))
			return str;
		else
			return (new StringBuilder()).append(Character.toLowerCase(f)).append(str.substring(1)).toString();
	}


	public static void main(String[] args){
		String s="sdgdsagds,asdgasdgsd,asdgsadg,adsgadsg,sd";		
		List<String> re = getSplit(s,",");
		for(String d : re){
			System.out.println(d);
		}
	}
}
