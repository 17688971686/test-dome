package cs.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.WindowsCodec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串工具类
 *
 * @author ldm
 */
public class StringUtil extends StringUtils {

    /**
     * 截取字符串
     *
     * @param str
     * @param begin
     * @param end
     * @return
     */
    public static String getSubString(String str, int begin, int end) {
        return new String(str.substring(begin, end));
    }

    /**
     * 取默认值
     *
     * @param checkStr
     * @param otherStr
     * @return
     */
    public static String getDefaultValue(String checkStr, String otherStr) {
        if (Validate.isString(checkStr)) {
            return checkStr;
        }
        return otherStr;
    }

    /**
     * 拼接字符串
     *
     * @param str
     * @param joinSymbol
     * @param joinStr
     * @return
     */
    public static String joinString(String str, String joinSymbol, String joinStr) {
        if (Validate.isString(str)) {
            str += joinSymbol;
        }
        str = str + joinStr;
        return str;
    }

    /**
     * @param splitStr
     * @param splitChars
     * @return
     */
    public static List<String> getSplit(String splitStr, String splitChars) {
        List<String> resultArray = null;
        if (Validate.isString(splitStr)) {
            resultArray = new ArrayList<String>();
            while (true) {
                int index = splitStr.indexOf(splitChars);
                if (index < 0) {
                    if (Validate.isString(splitStr)) {
                        resultArray.add(splitStr);
                    }
                    break;
                }
                String subString = splitStr.substring(0, index);
                if (Validate.isString(subString)) {
                    resultArray.add(subString);
                }
                splitStr = splitStr.substring(index + 1);
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
     *
     * @param str
     * @return
     */
    public static String upperCaseFirst(String str) {
        if (isBlank(str)) {
            return str;
        }
        char f = str.charAt(0);
        if (Character.isUpperCase(f)) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(f)).append(str.substring(1)).toString();
        }
    }

    public static String lowerCaseFirst(String str) {
        if (isBlank(str)) {
            return str;
        }
        char f = str.charAt(0);
        if (Character.isUpperCase(f)) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(f)).append(str.substring(1)).toString();
        }
    }

    /**
     * 过滤sql注入关键字
     * 使用正则表达式过滤：过滤参数字符串
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String sqlInjectionFilter(String str) throws PatternSyntaxException {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(.*([';]+|(--)+).*)|('.+--)|(--)|(\\|)|(%7C)|(\\b(select|update|and|or|delete|from|iframe|alert|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
        Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m = sqlPattern.matcher(str);
        return m.replaceAll("").trim();
    }
    /**
     * 校验有没有
     * @param value
     * @return
     */
    public static boolean checkXSSString(String value){
        boolean isHaveXSSStr = false;
        if(Validate.isString(value)){
            value = value.toLowerCase();
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("<link>(.*?)</link>", Pattern.CASE_INSENSITIVE);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("<style>(.*?)</style>", Pattern.CASE_INSENSITIVE);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("<iframe(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("<link(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
            if(isHaveXSSStr){
                return isHaveXSSStr;
            }
            scriptPattern = Pattern.compile("<style(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            isHaveXSSStr = scriptPattern.matcher(value).find();
        }
        return isHaveXSSStr;
    }

    /**
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
     *
     * @param soap
     * @param rgex
     * @return
     */
    public static String getSubUtilSimple(String soap, String rgex) {
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(","); // 分隔符
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    private static List<String> getFilterOutput(List<String> lines, String filter) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            if (!"mkyong".equals(line)) {
                result.add(line);
            }
        }
        return result;
    }

    /**
     * 数组是否包含某个字符串
     *
     * @param strArr
     * @param str
     * @return
     */
    public static boolean isContainStr(List<String> strArr, String str) {
        return strArr.contains(str);
    }

    public static void main(String[] args) {
        String checkString = "select u.* from user u";
        //生成一个Oracle编码器实例
        Codec windowsCodec = new WindowsCodec();
        System.out.println(ESAPI.validator().isValidInput("",checkString,"SafeString",64,true));
    }


}
