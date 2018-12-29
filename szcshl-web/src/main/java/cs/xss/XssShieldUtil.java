package cs.xss;

import cs.common.ResultMsg;
import cs.common.constants.SysConstants;
import cs.common.utils.ReflectionUtils;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.owasp.validator.html.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ldm on 2018/12/4 0004.
 */
public class XssShieldUtil {
    private static volatile XssShieldUtil instance = null;
    private static Policy policy = null;

    public static XssShieldUtil getInstance() {
        synchronized (XssShieldUtil.class) {
            if (instance == null) {
                instance = new XssShieldUtil();
            }
        }
        return instance;
    }

    private XssShieldUtil() {
        String antiSamyPath = XssHttpServletRequestWrapper.class.getClassLoader().getResource("antisamy-anythinggoes.xml").getFile();
        if (antiSamyPath.startsWith("file")) {
            antiSamyPath = antiSamyPath.substring(6);
        }
        try {
            policy = Policy.getInstance(antiSamyPath);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    public void cleanXssObjectArr(List<Object[]> sourceObjArr) {
        if (Validate.isList(sourceObjArr)) {
            for (int i = 0, l = sourceObjArr.size(); i < l; i++) {
                Object[] objectArr = sourceObjArr.get(i);
                if (!Validate.isEmpty(objectArr)) {
                    for (Object value : objectArr) {
                        if (value != null) {
                            if (value instanceof String) {
                                if (Validate.isString(value)) {
                                    objectArr[i] = responseEncodeForHTML(value.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void cleanMapXss(Map<String, Object> resultMap) {
        if (Validate.isMap(resultMap)) {
            Iterator entries = resultMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    if (value instanceof String) {
                        if (Validate.isString(value)) {
                            resultMap.put(key, responseEncodeForHTML(value.toString()));
                        }
                    }
                }
            }
        }
    }

    public void cleanMapListXss(List<Map<String, Object>> mapList) {
        if (Validate.isList(mapList)) {
            for (Map<String, Object> map : mapList) {
                cleanMapXss(map);
            }
        }
    }

    /**
     * 过滤返回page
     *
     * @param pageModelDto
     * @param <T>
     * @return
     */
    public <T> void cleanPageXss(PageModelDto<T> pageModelDto) {
        if (Validate.isObject(pageModelDto)) {
            List<T> pageValueList = pageModelDto.getValue();
            cleanListXss(pageValueList);
            pageModelDto.setValue(pageValueList);
        }
    }

    /**
     * 过滤返回resultMsg
     *
     * @param resultMsg
     * @return
     */
    public void cleanResultMsgXss(ResultMsg resultMsg) {
        if (Validate.isObject(resultMsg)) {
            if (Validate.isString(resultMsg.getIdCode())) {
                resultMsg.setIdCode(responseEncodeForHTML(resultMsg.getIdCode()));
            }
            if (Validate.isString(resultMsg.getReCode())) {
                resultMsg.setReCode(responseEncodeForHTML(resultMsg.getReCode()));
            }
            if (Validate.isString(resultMsg.getReMsg())) {
                resultMsg.setReMsg(responseEncodeForHTML(resultMsg.getReMsg()));
            }
            if (Validate.isObject(resultMsg.getReObj())) {
                Object value = resultMsg.getReObj();
                if (value instanceof String) {
                    if (Validate.isString(value)) {
                        resultMsg.setReObj(responseEncodeForHTML(value.toString()));
                    }
                }
            }
        }
    }

    public <T> void cleanObjXss(T t) {
        Class<?> clazz1 = t.getClass();
        List<Field> field1 = ReflectionUtils.getClassAllField(clazz1);
        //遍历属性列表field1
        for (int i = 0, l = field1.size(); i < l; i++) {
            String fieldName1 = field1.get(i).getName();
            Object value = ReflectionUtils.getFieldValueByName(fieldName1, clazz1);
            if (value != null) {
                if (value instanceof String) {
                    if (Validate.isString(value)) {
                        value = responseEncodeForHTML(value.toString());
                        ReflectionUtils.setFieldValue(t, fieldName1, value);
                    }
                }
            }
        }
    }

    public <T> void cleanListXss(List<T> list) {
        if (Validate.isList(list)) {
            for (T t : list) {
                cleanObjXss(t);
            }
        }
    }

    /**
     * 返回页面的字符串进行加密处理
     * 术语解释：canonicalize是指将数据转换或解码成一个常见字符集的过程。这里的数据可以是经过多重混合编码的。
     * 比如：%3cscript%3ealert(%22xss%22)%3c%2fscript%3e还原成<script>alert("xss")</script>。在对输入数据进行过滤之前应进行规范化！
     * @param value
     * @return
     */
    public String responseEncodeForHTML(String value){
        return ESAPI.encoder().encodeForHTML(ESAPI.encoder().canonicalize(value));
    }

    /**
     * 输入字符串过滤
     * @param value
     * @return
     */
    public String stripXss(String value) {
        if (Validate.isString(value)) {
            try {
                //按utf-8解碼:防止有害脚本
                value = URLDecoder.decode(value, SysConstants.UTF8);
                AntiSamy antiSamy = new AntiSamy();
                //扫描
                CleanResults cr = antiSamy.scan(xssEncode(value), policy);
                //获取清洗后的结果
                value = cr.getCleanHTML();
            } catch (ScanException e) {
                e.printStackTrace();
            } catch (PolicyException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    //还原原先字符串（有些查询条件含有特殊字符）
    public String unStripXss(String value) {
        if (Validate.isString(value)) {
            return StringEscapeUtils.unescapeHtml(value);
        }
        return value;
    }

    //将容易引起xss漏洞的半角字符直接替换成全角字符
    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     * @param s
     * @return
     */
    public String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    //全角大于号
                    sb.append('＞');
                    break;
                case '<':
                    //全角小于号
                    sb.append('＜');
                    break;
                case '\"':
                    //全角双引号
                    sb.append('＂');
                    break;
                case '+':
                    //全角双引号
                    sb.append('＋');
                    break;
                case '%':
                    //全角百分号
                    sb.append('％');
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args){
        /*String outputString = "<script ff>sss</script>12142141";
        //规范化字符串
        outputString = ESAPI.encoder().canonicalize(outputString);
        System.out.println(outputString);
        System.out.println(ESAPI.encoder().encodeForHTML(outputString));
        System.out.println(Encode.forHtml(outputString));*/
        Codec oracleCodec = new OracleCodec();
        System.out.println(ESAPI.encoder().encodeForSQL(oracleCodec, "delete from cs_user where id = '124' "));
    }
}
