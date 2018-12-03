package com.sn.framework.core.common;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 */
public class StrUtils {
    /**
     * 禁止实例化
     */
    private StrUtils() {
    }

    /**
     * 对象转换成字符串，
     * @param v1  要转换的对象
     * @param v2  v1为空时，返回的值
     * @return
     */
    public static String initValue(Object v1,String v2){
        if(Validate.isObject(v1)){
            return v1.toString();
        }
        return v2;
    }

    /**
     * 对象转换成字符串，
     * @param v1  要转换的对象
     * @param v2  v1为空时，返回的值
     * @return
     */
    public static Integer initIntegerValue(Object v1,Integer v2){
        if(Validate.isObject(v1) && Validate.isString(v1.toString())){
            return Integer.parseInt(v1.toString());
        }
        return v2;
    }
    /**
     * 处理url
     * <p>
     * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
     *
     * @param url
     * @return
     */
    public static String handelUrl(String url) {
        if (url == null) {
            return null;
        }
        url = url.trim();
        if (url.equals("") || url.startsWith("http://")
                || url.startsWith("https://")) {
            return url;
        } else {
            return "http://" + url.trim();
        }
    }

    /**
     * 分割并且去除空格
     *
     * @param str  待分割字符串
     * @param sep  分割符
     * @param sep2 第二个分隔符
     * @return 如果str为空，则返回null。
     */
    public static String[] splitAndTrim(String str, String sep, String sep2) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (!StringUtils.isBlank(sep2)) {
            str = StringUtils.replace(str, sep2, sep);
        }
        String[] arr = StringUtils.split(str, sep);
        // trim
        for (int i = 0, len = arr.length; i < len; i++) {
            arr[i] = arr[i].trim();
        }
        return arr;
    }

    /**
     * 文本转html
     *
     * @param txt
     * @return
     */
    public static String txt2htm(String txt) {
        if (StringUtils.isBlank(txt)) {
            return txt;
        }
        StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
        char c;
        boolean doub = false;
        for (int i = 0; i < txt.length(); i++) {
            c = txt.charAt(i);
            if (c == ' ') {
                if (doub) {
                    sb.append(' ');
                    doub = false;
                } else {
                    sb.append("&nbsp;");
                    doub = true;
                }
            } else {
                doub = false;
                switch (c) {
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    case '"':
                        sb.append("&quot;");
                        break;
                    case '\n':
                        sb.append("<br/>");
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 剪切文本。如果进行了剪切，则在文本后加上"..."
     *
     * @param s   剪切对象。
     * @param len 编码小于256的作为一个字符，大于256的作为两个字符。
     * @return
     */
    public static String textCut(String s, int len, String append) {
        if (s == null) {
            return null;
        }
        int slen = s.length();
        if (slen <= len) {
            return s;
        }
        // 最大计数（如果全是英文）
        int maxCount = len * 2;
        int count = 0;
        int i = 0;
        for (; count < maxCount && i < slen; i++) {
            if (s.codePointAt(i) < 256) {
                count++;
            } else {
                count += 2;
            }
        }
        if (i < slen) {
            if (count > maxCount) {
                i--;
            }
            if (!StringUtils.isBlank(append)) {
                if (s.codePointAt(i - 1) < 256) {
                    i -= 2;
                } else {
                    i--;
                }
                return s.substring(0, i) + append;
            } else {
                return s.substring(0, i);
            }
        } else {
            return s;
        }
    }

    /**
     * p换行
     *
     * @param inputString
     * @return
     */
    public static String removeHtmlTagP(String inputString) {
        if (inputString == null){
            return null;
        }
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            htmlStr.replace("</p>", "\n");
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    public static String removeHtmlTag(String inputString) {
        if (inputString == null){
            return null;
        }

        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    /**
     * 检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
     *
     * @param str
     * @param search
     * @return
     */
    public static boolean contains(String str, String search) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
            return false;
        }
        String reg = StringUtils.replace(search, "*", ".*");
        Pattern p = Pattern.compile(reg);
        return p.matcher(str).matches();
    }

    public static boolean containsKeyString(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (str.contains("'") || str.contains("\"") || str.contains("\r")
                || str.contains("\n") || str.contains("\t")
                || str.contains("\b") || str.contains("\f")) {
            return true;
        }
        return false;
    }


    public static String addCharForString(String str, int strLength, char c, int position) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                if (position == 1) {
                    //右補充字符c
                    sb.append(c).append(str);
                } else {
                    //左補充字符c
                    sb.append(str).append(c);
                }
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    // 将""和'转义
    public static String replaceKeyString(String str) {
        if (containsKeyString(str)) {
            return str.replace("'", "\\'").replace("\"", "\\\"").replace("\r",
                    "\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
                    "\b", "\\b").replace("\f", "\\f");
        } else {
            return str;
        }
    }

    //单引号转化成双引号
    public static String replaceString(String str) {
        if (containsKeyString(str)) {
            return str.replace("'", "\"").replace("\"", "\\\"").replace("\r",
                    "\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
                    "\b", "\\b").replace("\f", "\\f");
        } else {
            return str;
        }
    }

    public static String getSuffix(String str) {
        int splitIndex = str.lastIndexOf(".");
        return str.substring(splitIndex + 1);
    }

    // 过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
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
        String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|from|iframe|alert|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
        Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m = sqlPattern.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 处理跨脚本攻击方法
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        if (value != null) {
            value = value.replaceAll("", "");
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("<link>(.*?)</link>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("<style>(.*?)</style>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("</iframe>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("</link>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("</style>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("<iframe(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("<link(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("<style(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return filter(value);
    }

    /**
     * 过滤特殊字符
     */
    public static String filter(String value) {
        if (value == null) {
            return null;
        }
        StringBuffer result = new StringBuffer(value.length());
        for (int i = 0; i < value.length(); ++i) {
            switch (value.charAt(i)) {
                case '<':
                    result.append("<");
                    break;
                case '>':
                    result.append(">");
                    break;
                case '"':
                    result.append("\"");
                    break;
                case '\'':
                    result.append("'");
                    break;
                case '%':
                    result.append("%");
                    break;
                case ';':
                    result.append(";");
                    break;
                case '(':
                    result.append("(");
                    break;
                case ')':
                    result.append(")");
                    break;
                case '&':
                    result.append("&");
                    break;
                case '+':
                    result.append("+");
                    break;
                default:
                    result.append(value.charAt(i));
                    break;
            }
        }
        return result.toString();
    }

    /**
     * 判断字符串是否是小数
     *
     * @param str
     * @return
     */
    public static boolean isStringDouble(String str) {
        return str.matches("-?[0-9]+.*[0-9]*");
    }

    /**
     * 匹配字符串
     * @param source
     * @param regexList
     * @return
     */
    public static boolean isLike(String source, List<String> regexList) {
        for(String regex:regexList){
            if(isLike(source, regex, false)){
                return  true;
            }
        }
        return false;
    }

    /**
     * 匹配字符串
     * @param source
     * @param regex
     * @return
     */
    public static boolean isLike(String source, String regex) {
       return  isLike(source, regex, false);
    }
    /**
     * 判断source字符串是否能够被regex匹配，能满足普通情况，没有考虑特别复杂的情况
     * @param source 任意字符串
     * @param regex 包含*或？的匹配表达式(如果要匹配*、?、\,需要加上反斜杠，如\*,\?,\\)
     * @param ignoreCase 大小写敏感
     * @return
     */
    public static boolean isLike(String source, String regex, boolean ignoreCase) {
        if (source == null || regex == null) return false;
        if (ignoreCase) {
            source = source.toLowerCase();
            regex = regex.toLowerCase();
        }
        return matches(source, regex.replaceAll("(^|([^\\\\]))[\\*]{2,}", "$2*"));//去除多余*号
    }

    private static boolean matches(String source, String regex) {
        //如果source与regex完全相等，且source不包含反斜杠，则返回true。(当source包含*号或者？号，此时亦满足，不做多余判断)
        if (source.equals(regex) && source.indexOf('\\') < 0) return true;
        int rIdx = 0, sIdx = 0;//同时遍历源字符串与匹配表达式
        while (rIdx < regex.length() && sIdx < source.length()) {
            char c = regex.charAt(rIdx);//以匹配表达式为主导
            switch (c) {
                case '*'://匹配到*号进入下一层递归
                    String tempSource = source.substring(sIdx);//去除前面已经完全匹配的前缀
                    String tempRegex = regex.substring(rIdx + 1);//从星号后一位开始认为是新的匹配表达式
                    for (int j = 0; j <= tempSource.length(); j++) {//此处等号不能缺，如（ABCD，*），等号能达成("", *)条件
                        if (matches(tempSource.substring(j), tempRegex)) {//很普通的递归思路
                            return true;
                        }
                    }
                    return false;//排除所有潜在可能性，则返回false
                case '?':
                    break;
                case '\\'://匹配到反斜杠跳过一位，匹配下一个字符串
                    c = regex.charAt(++rIdx);
                default:
                    if (source.charAt(sIdx) != c) return false;//普通字符的匹配
            }
            rIdx++;
            sIdx++;
        }
        //最终source被匹配完全，而regex也被匹配完整或只剩一个*号
        return source.length() == sIdx &&
                (regex.length() == rIdx ||
                        regex.length() == rIdx + 1 && regex.charAt(rIdx) == '*');
    }

    public static void main(String args[]) {
       /* System.out.println("str=ABCD regex=ABC? :"+isLike("ABCD", "ABC?"));
        System.out.println("str=ABCD regex=A??? :"+isLike("ABCD", "A???"));
        System.out.println("str=ABCD regex=A?? :"+isLike("ABCD", "A??"));
        System.out.println("str=ABCD regex=?BC? :"+isLike("ABCD", "?BC?"));
        System.out.println("str=ABCD regex=*B*D :"+isLike("ABCD", "*B*D"));
        System.out.println("str=ABCD regex=*BCD :"+isLike("ABCD", "*BCD"));
        System.out.println("str=ABCD regex=*A*B*D :"+isLike("ABCD", "*A*B*D"));*/
    }

}
