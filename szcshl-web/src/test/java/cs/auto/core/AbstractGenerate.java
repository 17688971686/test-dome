package cs.auto.core;

import cs.auto.core.config.GanerateConfig;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Locale;

/**
 * 代码生成器抽象类
 *
 * @author tzg
 * @date 2017/5/7 14:49
 */
public abstract class AbstractGenerate {

    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * freeemarker配置信息
     */
    protected static Configuration cfg = null;
    /**
     * 代码生成的配置类
     */
    protected GanerateConfig gconf = null;

    public AbstractGenerate(GanerateConfig gconf) {
        this.gconf = gconf;
        execute();
    }

    /**
     * 执行生成
     */
    public abstract void execute();

    /**
     * 获取freemarker的配置 freemarker本身支持classpath,目录和从ServletContext获取.
     *
     * @return 返回Configuration对象
     */
    protected Configuration getConfiguration() {
        if (null == cfg) {
            cfg = new Configuration();
            if (StringUtils.isBlank(gconf.getTemplatePath())) {
                // 读取classpath下的一个目录（读取jar文件）
                cfg.setClassForTemplateLoading(Generate.class, "/templates");
            } else {
                // 读取指定目录
                try {
                    cfg.setDirectoryForTemplateLoading(new File(gconf.getTemplatePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // setEncoding这个方法一定要设置国家及其编码，不然在flt中的中文在生成html后会变成乱码
            cfg.setEncoding(Locale.getDefault(), "UTF-8");

            // 设置对象的包装器
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            // 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        }

        return cfg;
    }

    /**
     * @param templatePath        模板文件名,相对上面的模版根目录templates路径,例如/module/view.templatePath templates/module/view.templatePath
     * @param outputFilePath 要生成的静态文件的路径,相对设置中的根路径,例如 "jsp/user/1.html"
     * @return
     */
    protected void createFile(String templatePath, String outputFilePath) {
        if (StringUtils.isAnyBlank(templatePath, outputFilePath)) {
            return;
        }
        // 创建Template对象
        try {

            File outFile = new File(outputFilePath);
            if (outFile.exists() && !gconf.isFileOverride()) { // 检测已存在的文件是否要重写
                return;
            }

            if (!outFile.getParentFile().exists()) {    // 检测目录是否存在，不存在则创建
                // 如果文件所在的目录不存在，则创建目录
                if (!outFile.getParentFile().mkdirs()) {
                    logger.debug("创建文件所在的目录失败!");
                    return;
                }
            }

            Configuration cfg = getConfiguration();
            Template template = cfg.getTemplate(templatePath);
            template.setEncoding("UTF-8");

            // 生成静态页面
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            template.process(gconf.getParamMap(), out);
            out.flush();
            out.close();
            logger.info("===>> 模板: " + templatePath + ";  文件: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
