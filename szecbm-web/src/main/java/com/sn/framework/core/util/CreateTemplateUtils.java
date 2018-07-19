package com.sn.framework.core.util;


import java.io.File;
import java.util.Map;

/**
 * Description: 生成报表模板
 * Author: zsl
 * Date: 2018/1/15 9:05
 */
public class CreateTemplateUtils {

    /**
     * 生成报表模板
     * @param dataMap
     * @return
     */
    public static File createMonthTemplate(Map<String, Object> dataMap,String templateName,String outputFile) {

      return  TemplateUtil.createDoc(dataMap, templateName,outputFile);
    }

}