package cs.xss;

import cs.common.constants.SysConstants;
import cs.common.utils.Validate;
import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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

    //过来xss
    public String stripXss(String value) {
        if (Validate.isString(value)) {
            try {
                //按utf-8解碼:防止有害脚本
                value = URLDecoder.decode(value, SysConstants.UTF8);
                AntiSamy antiSamy = new AntiSamy();
                CleanResults cr = antiSamy.scan(value, policy);//扫描
                value = cr.getCleanHTML();//获取清洗后的结果
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
