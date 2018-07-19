package com.sn.framework.core.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 模板工具类
 */

public class TemplateUtil {
    /**
     * freeemarker配置信息
     */
    private static Configuration cfg = null;
    private static Map<String, Template> allTemplates = null;   //模板缓存类
    private static String suffix = ".ftl";

    /**
     * 获取freemarker的配置 freemarker本身支持classpath,目录和从ServletContext获取.
     *
     * @return 返回Configuration对象
     */
    static Configuration getConfiguration() {
        cfg = new Configuration(Configuration.getVersion());
        cfg.setClassForTemplateLoading(TemplateUtil.class, "/templates");
        // setEncoding这个方法一定要设置国家及其编码，不然在flt中的中文在生成html后会变成乱码
        cfg.setEncoding(Locale.getDefault(), "UTF-8");

        // 设置对象的包装器
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        // 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        //初始化模板
        allTemplates = new HashMap<>();
        return cfg;
    }


    public static File createDoc(Map<?, ?> dataMap, String templateName, String outputFile) {
        if (cfg == null) {
            cfg = getConfiguration();
        }
        File f = new File(outputFile);
        Template t = allTemplates.get(templateName);
        try {
            if (t == null) {
                t = cfg.getTemplate(templateName + suffix);
                allTemplates.put(templateName, t);
            }
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }




    public static void main(String[] args) {
        Map<String, Object> dataMap = new HashMap<>();
        TemplateUtil.createDoc(dataMap, "cjb/smallProject/collectPrint/projectPrintExpBak", "G:\\6688.xls");
    }
}
