package cs.xss;

import cs.common.constants.SysConstants;
import cs.common.utils.Validate;
import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * @author ldm on 2018/12/6 0006.
 * @desc 基于AntiSamy的XSS防御
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    //AntiSamy使用的策略文件
    private static Policy policy = null;
    private Map<String, String[]> parameterMap;  //所有参数的Map集合
    static {
        String antiSamyPath = XssHttpServletRequestWrapper.class.getClassLoader().getResource("antisamy-ebay.xml").getFile();
        System.out.println("policy_filepath:" + antiSamyPath);
        if (antiSamyPath.startsWith("file")) {
            antiSamyPath = antiSamyPath.substring(6);
        }
        try {
            policy = Policy.getInstance(antiSamyPath);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }


    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        parameterMap = request.getParameterMap();
    }
    /** * 获取所有参数名 * @return 返回所有参数名 * */
    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<>(parameterMap.keySet());
        return vector.elements();
    }

    /**
     * @desc Header为空直接返回，不然进行XSS清洗
     * @author howinfun
     * @date 2018/10/24
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value ==  null) {
            return null;
        }
        return cleanXSS(value);

    }

    /**
     * @desc Parameter为空直接返回，不然进行XSS清洗
     */
    @Override
    public String getParameter(String name) {
        String[] results = parameterMap.get(name);
        if(Validate.isEmpty(results)) {
            return null;
        } else{
            return cleanXSS(results[0]);
        }
    }

    /**
     * 获取指定参数名的所有值的数组，如：checkbox的所有数据
     * 接收数组变量 ，如checkobx类型
     * */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = parameterMap.get(name);
        if (!Validate.isEmpty(values)) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = cleanXSS(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    /**
     * @desc AntiSamy清洗数据
     */
    private String cleanXSS(String taintedHTML) {
        return XssShieldUtil.getInstance().stripXss(taintedHTML);
    }
}