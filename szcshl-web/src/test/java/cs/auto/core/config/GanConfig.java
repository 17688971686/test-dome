package cs.auto.core.config;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器配置类
 *
 * @author tzg
 * @date 2017/5/7 14:10
 */
public class GanConfig extends AbstractGanConfig {

    protected Class cls;

    /**
     * 实例化代码生成器的配置类
     * @param cls       数据映射实体类
     */
    public GanConfig(Class cls) {
        this.cls = cls;
    }

    /**
     * 实例化代码生成器的配置类
     * @param cls       数据映射实体类
     * @param comment   注解
     */
    public GanConfig(Class cls, String comment) {
        this.cls = cls;
        this.comment = comment;
    }

    /**
     * 实例化代码生成器的配置类
     * @param cls       数据映射实体类
     * @param comment   注解
     * @param ouputPath 输出路径
     */
    public GanConfig(Class cls, String comment, String ouputPath) {
        this.cls = cls;
        this.comment = comment;
        this.ouputPath = ouputPath;
    }


    /**
     * 生成代码生成器的配置信息
     */
    @Override
    public void generateParams() {
        logger.info("<<=====================开始生成代码生成器的配置信息==============================");
        String beanPackage = cls.getPackage().getName();
        beanName = cls.getSimpleName();
        paramMap.put("beanPackage", beanPackage);
        paramMap.put("beanName", beanName);

        dtoClsName = String.format(dtoClsName, beanName);
        repoClsName = String.format(repoClsName, beanName);
        repoImplClsName = String.format(repoImplClsName, beanName);
        serviceClsName = String.format(serviceClsName, beanName);
        serviceImplClsName = String.format(serviceImplClsName, beanName);
        controllerClsName = String.format(controllerClsName, beanName);

        packageConfig = new PackageConfig();
        paramMap.put("packageConfig", packageConfig);

        if(beanPackage.indexOf(packageConfig.getDomain()) > -1) {
            String pf = beanPackage.replace(packageConfig.getDomain(), "%s");
            dtoPackage = String.format(pf, packageConfig.getDto());
            repoPackage = String.format(pf, packageConfig.getRepo());
            repoImplPackage = String.format(pf, packageConfig.getRepoImpl());
            servicePackage = String.format(pf, packageConfig.getService());
            serviceImplPackage = String.format(pf, packageConfig.getServiceImpl());
            controllerPackage = String.format(pf, packageConfig.getController());
        }

        paramMap.put("dtoPackage", dtoPackage);
        paramMap.put("repoPackage", repoPackage);
        paramMap.put("repoImplPackage", repoImplPackage);
        paramMap.put("servicePackage", servicePackage);
        paramMap.put("serviceImplPackage", serviceImplPackage);
        paramMap.put("controllerPackage", controllerPackage);

        List<FieldConfig> clsFields = new ArrayList<FieldConfig>();
        FieldConfig cf;
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            cf = new FieldConfig();
            cf.setName(f.getName());
            cf.setType(f.getType().getSimpleName());
            clsFields.add(cf);
        }
        paramMap.put("fields", clsFields);

        paramMap.put("comment", comment);
        paramMap.put("author", author);
//        logger.info(JSON.toJSONString(paramMap));

        generateFileConf();
        logger.info("<<=====================结束生成代码生成器的配置信息==============================");
    }

    public void generateFileConf() {
        fileConfs.add(new FileConfig(dtoClsName, TemplateConfig.JAVA_DTO, getOutputPath(dtoPackage, dtoClsName)));
        fileConfs.add(new FileConfig(repoClsName, TemplateConfig.JAVA_REPO, getOutputPath(repoPackage, repoClsName)));
        fileConfs.add(new FileConfig(repoImplClsName, TemplateConfig.JAVA_REPO_IMPL, getOutputPath(repoImplPackage, repoImplClsName)));
        fileConfs.add(new FileConfig(serviceClsName, TemplateConfig.JAVA_SERVICE, getOutputPath(servicePackage, serviceClsName)));
        fileConfs.add(new FileConfig(serviceImplClsName, TemplateConfig.JAVA_SERVICE_IMPL, getOutputPath(serviceImplPackage, serviceImplClsName)));
        fileConfs.add(new FileConfig(controllerClsName, TemplateConfig.JAVA_CONTROLLER, getOutputPath(controllerPackage, controllerClsName)));
    }

    /**
     * 获取输出文件路径
     * @param moduleName
     * @param fileName
     * @return
     */
    protected String getOutputPath(String moduleName, String fileName) {
        String path = ouputPath.concat(File.separator);
        if (StringUtils.isNoneBlank(moduleName)) {
            path.concat(moduleName.replace(".", File.separator)).concat(File.separator);
        }
        return path.concat(fileName);
    }

    protected String comment = "";
    /**
     * 作者名
     */
    protected String author = System.getProperty("user.name");


    private PackageConfig packageConfig;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public PackageConfig getPackageConfig() {
        return packageConfig;
    }

    public void setPackageConfig(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
    }

}
