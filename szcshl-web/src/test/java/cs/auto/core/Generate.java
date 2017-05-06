package cs.auto.core;

import com.alibaba.fastjson.JSON;
import cs.auto.core.config.ClsField;
import cs.auto.core.config.PackageConfig;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Description: 代码生成器基础配置
 * User: tzg
 * Date: 2017/5/6 16:43
 */
public class Generate {

    /**
     * freeemarker配置信息
     */
    private static Configuration cfg = null;

    private Map<String, Object> paramMap = new HashMap<String, Object>(10);

    public Generate(Class cls) {
        generateParams(cls);
    }

    public Generate(Class cls, String ouputPath) {
        this.ouputPath = ouputPath;
        generateParams(cls);
        execute();
    }


    public void execute() {
        // 创建DTO类
        createFile("Dto.ftl", paramMap, getOuputPath() + "//" + dtoPackage.replace(".", "\\") + "\\" + beanName + "Dto.java");



    }

    private String beanName;

    public void generateParams(Class cls) {
        String beanPackage = cls.getPackage().getName();
        beanName = cls.getSimpleName();
        paramMap.put("beanPackage", beanPackage);
        paramMap.put("beanName", beanName);

        packageConfig = new PackageConfig();
        paramMap.put("packageConfig", packageConfig);

        if(beanPackage.indexOf(packageConfig.getDomain()) > -1) {
            String pf = beanPackage.replace(packageConfig.getDomain(), "%s");
            dtoPackage = String.format(pf, packageConfig.getDto());
            repositoryPackage = String.format(pf, packageConfig.getRepository());
            repositoryImplPackage = String.format(pf, packageConfig.getRepositoryImpl());
            servicePackage = String.format(pf, packageConfig.getService());
            serviceImplPackage = String.format(pf, packageConfig.getServiceImpl());
            controllerPackage = String.format(pf, packageConfig.getController());
        }

        paramMap.put("dtoPackage", dtoPackage);
        paramMap.put("repositoryPackage", repositoryPackage);
        paramMap.put("repositoryImplPackage", repositoryImplPackage);
        paramMap.put("servicePackage", servicePackage);
        paramMap.put("serviceImplPackage", serviceImplPackage);
        paramMap.put("controllerPackage", controllerPackage);

        List<ClsField> clsFields = new ArrayList<ClsField>();
        ClsField cf;
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            cf = new ClsField();
            cf.setName(f.getName());
            cf.setType(f.getType().getSimpleName());
            clsFields.add(cf);
        }
        paramMap.put("fields", clsFields);
        System.out.println(JSON.toJSONString(paramMap));
    }

    /**
     * 获取freemarker的配置 freemarker本身支持classpath,目录和从ServletContext获取.
     *
     * @return 返回Configuration对象
     */
    private Configuration getConfiguration() {
        if (null == cfg) {
            cfg = new Configuration();
            if (StringUtils.isBlank(templatePath)) {
                // 读取classpath下的一个目录（读取jar文件）
                cfg.setClassForTemplateLoading(Generate.class, "/templates");
            } else {
                // 读取指定目录
                try {
                    cfg.setDirectoryForTemplateLoading(new File(templatePath));
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
     * @param ftl        模板文件名,相对上面的模版根目录templates路径,例如/module/view.ftl templates/module/view.ftl
     * @param data       填充数据
     * @param targetFile 要生成的静态文件的路径,相对设置中的根路径,例如 "jsp/user/1.html"
     * @return
     */
    public boolean createFile(String ftl, Map<String, Object> data, String targetFile) {

        try {
            // 创建Template对象
            Configuration cfg = getConfiguration();
            Template template = cfg.getTemplate(ftl);
            template.setEncoding("UTF-8");

            if (targetFile.indexOf("\\") > 0) {
                String dir = targetFile.substring(0, targetFile.lastIndexOf("\\") - 1);

                targetFile = targetFile.substring(targetFile.lastIndexOf("\\"));
            }

            // 生成静态页面
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
            template.process(data, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (TemplateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //==========================生成类的包路径======================================================
    private String dtoPackage = "cs.model";
    private String repositoryPackage = "cs.repository.repositoryImpl";
    private String repositoryImplPackage = "cs.repository.repositoryImpl";
    private String servicePackage = "cs.service";
    private String serviceImplPackage = "cs.service";
    private String controllerPackage = "cs.controller";

    public String getDtoPackage() {
        return dtoPackage;
    }

    public void setDtoPackage(String dtoPackage) {
        this.dtoPackage = dtoPackage;
    }

    public String getRepositoryPackage() {
        return repositoryPackage;
    }

    public void setRepositoryPackage(String repositoryPackage) {
        this.repositoryPackage = repositoryPackage;
    }

    public String getRepositoryImplPackage() {
        return repositoryImplPackage;
    }

    public void setRepositoryImplPackage(String repositoryImplPackage) {
        this.repositoryImplPackage = repositoryImplPackage;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getServiceImplPackage() {
        return serviceImplPackage;
    }

    public void setServiceImplPackage(String serviceImplPackage) {
        this.serviceImplPackage = serviceImplPackage;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }
    //================================================================================

    /**
     * 输出目录路径
     */
    private String ouputPath;
    /**
     * 是否覆盖
     */
    private boolean isOverride = false;
    /**
     * 模板路径
     */
    private String templatePath;

    private PackageConfig packageConfig;

    public String getOuputPath() {
        return ouputPath;
    }

    public void setOuputPath(String ouputPath) {
        this.ouputPath = ouputPath;
    }

    public boolean isOverride() {
        return isOverride;
    }

    public void setOverride(boolean override) {
        isOverride = override;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public PackageConfig getPackageConfig() {
        return packageConfig;
    }

    public void setPackageConfig(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
    }
}