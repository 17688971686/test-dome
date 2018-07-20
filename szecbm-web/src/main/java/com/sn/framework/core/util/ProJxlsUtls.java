package com.sn.framework.core.util;



import java.util.Map;

/**
 * Description: ProJxlsUtls
 *
 * @author: zsl
 * @date: 2018/7/20 15:38
 */
public class ProJxlsUtls {

   private static  Map<String, Object> resultMap ;

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        ProJxlsUtls.resultMap = resultMap;
    }

    public ProJxlsUtls(Map<String, Object> resultMap) {
        ProJxlsUtls.resultMap = resultMap;
    }

    /**
     * 获取各个行业部门数
     * @param key
     * @return
     */
    public static String getProDeptByIndustry(String key) {
        String temp = "";
        if (null != ProJxlsUtls.resultMap) {


        }
        return "其中"+temp;
    }




}
