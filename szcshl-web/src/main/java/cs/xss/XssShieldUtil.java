package cs.xss;

import cs.common.ResultMsg;
import cs.common.constants.SysConstants;
import cs.common.utils.ReflectionUtils;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.encoder.Encode;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.EncodingException;
import org.owasp.validator.html.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;

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
        String antiSamyPath = XssHttpServletRequestWrapper.class.getClassLoader().getResource("antisamy-ebay.xml").getFile();
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
                                    objectArr[i] = responseCleanXss(value.toString(),1);
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
                            resultMap.put(key, responseCleanXss(value.toString(),1));
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
                resultMsg.setIdCode(responseCleanXss2(resultMsg.getIdCode()));
            }
            if (Validate.isString(resultMsg.getReCode())) {
                resultMsg.setReCode(responseCleanXss2(resultMsg.getReCode()));
            }
            if (Validate.isString(resultMsg.getReMsg())) {
                resultMsg.setReMsg(responseCleanXss2(resultMsg.getReMsg()));
            }
            if (Validate.isObject(resultMsg.getReObj())) {
                Object value = resultMsg.getReObj();
                if (value instanceof String) {
                    if (Validate.isString(value)) {
                        resultMsg.setReObj(responseCleanXss2(value.toString()));
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
                        value = responseCleanXss(value.toString(),1);
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

    public String responseCleanXss2(String value){
        if(Validate.isString(value)){
            //规范化字符串
            value = ESAPI.encoder().canonicalize(value);
            return Encode.forHtml(value);
        }
        return value;
    }

    /**
     * 对输出字符串进行encode
     * @param value
     * @param encodeType
     * 1.HTML编码
     * 2.HTML属性编码
     * 3.JavaScript编码
     * 4.URL编码
     * 5.DN编码
     * @return
     */
    public String responseCleanXss(String value, int encodeType) {
        if (Validate.isString(value)) {
            //规范化字符串
            value = ESAPI.encoder().canonicalize(value);
            //输出转码
            switch (encodeType) {
                case 1:
                    value = ESAPI.encoder().encodeForHTML(value);
                    break;
                case 2:
                    value = ESAPI.encoder().encodeForHTMLAttribute(value);
                    break;
                case 3:
                    value = ESAPI.encoder().encodeForJavaScript(value);
                    break;
                case 4:
                    try {
                        value = ESAPI.encoder().encodeForURL(value);
                    } catch (EncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    value = ESAPI.encoder().encodeForDN(value);
                    break;
                default:
                    value = ESAPI.encoder().encodeForHTML(value);
            }
        }
        return value;
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
                CleanResults cr = antiSamy.scan(value, policy);
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
    public String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    sb.append('＞');          //全角大于号
                    break;

                case '<':
                    sb.append('＜');          //全角小于号
                    break;

                case '\'':
                    sb.append('‘');           //全角单引号
                    break;

                case '\"':
                    sb.append('“');           //全角双引号
                    break;

                case '&':
                    sb.append('＆');          //全角
                    break;

                case '\\':
                    sb.append('＼');          //全角斜线
                    break;

                case '#':
                    sb.append('＃');          //全角井号
                    break;

                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
