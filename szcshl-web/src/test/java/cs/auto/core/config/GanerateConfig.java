package cs.auto.core.config;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器配置类
 *
 * @author tzg
 * @date 2017/5/7 14:10
 */
public class GanerateConfig {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected Class cls;

    /**
     * 实例化代码生成器的配置类
     * @param cls       数据映射实体类
     */
    public GanerateConfig(Class cls) {
        this.cls = cls;
    }

    /**
     * 实例化代码生成器的配置类
     * @param cls       数据映射实体类
     * @param comment   注解
     */
    public GanerateConfig(Class cls, String comment) {
        this.cls = cls;
        this.comment = comment;
    }

    /**
     * 实例化代码生成器的配置类
     * @param cls       数据映射实体类
     * @param comment   注解
     * @param ouputPath 输出路径
     */
    public GanerateConfig(Class cls, String comment, String ouputPath) {
        this.cls = cls;
        this.comment = comment;
        this.ouputPath = ouputPath;
    }

    /**
     * freemarker模板参数
     */
    protected Map<String, Object> paramMap = new HashMap<String, Object>(10);

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 生成代码生成器的配置信息
     */
    public void generateParams() {
        logger.info("<<=====================开始生成代码生成器的配置信息==============================");
        String beanPackage = cls.getPackage().getName();
        beanName = cls.getSimpleName();
        paramMap.put("beanPackage", beanPackage);
        paramMap.put("beanName", beanName);

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

        paramMap.put("comment", comment);
        paramMap.put("author", author);
//        logger.info(JSON.toJSONString(paramMap));
        logger.info("<<=====================结束生成代码生成器的配置信息==============================");
    }

    //==========================类名格式 begin======================================================
    protected String dtoClsName = "%sDto.java";
    protected String repoClsName = "%sRepo.java";
    protected String repoImplClsName = "%sRepoImpl.java";
    protected String serviceClsName = "%sService.java";
    protected String serviceImplClsName = "%sServiceImpl.java";
    protected String controllerClsName = "%sController.java";

    protected String listPageName = "%sList.html";
    protected String listCtrName = "%s.ctr.js";
    protected String listSvcName = "%s.svc.js";
    protected String editPageName = "%sEdit.html";
    protected String editCtrName = "%s.edit.ctr.js";

    public String getDtoClsName() {
        return dtoClsName;
    }

    public void setDtoClsName(String dtoClsName) {
        this.dtoClsName = dtoClsName;
    }

    public String getRepoClsName() {
        return repoClsName;
    }

    public void setRepoClsName(String repoClsName) {
        this.repoClsName = repoClsName;
    }

    public String getRepoImplClsName() {
        return repoImplClsName;
    }

    public void setRepoImplClsName(String repoImplClsName) {
        this.repoImplClsName = repoImplClsName;
    }

    public String getServiceClsName() {
        return serviceClsName;
    }

    public void setServiceClsName(String serviceClsName) {
        this.serviceClsName = serviceClsName;
    }

    public String getServiceImplClsName() {
        return serviceImplClsName;
    }

    public void setServiceImplClsName(String serviceImplClsName) {
        this.serviceImplClsName = serviceImplClsName;
    }

    public String getControllerClsName() {
        return controllerClsName;
    }

    public void setControllerClsName(String controllerClsName) {
        this.controllerClsName = controllerClsName;
    }

    public String getListPageName() {
        return listPageName;
    }

    public void setListPageName(String listPageName) {
        this.listPageName = listPageName;
    }

    public String getListCtrName() {
        return listCtrName;
    }

    public void setListCtrName(String listCtrName) {
        this.listCtrName = listCtrName;
    }

    public String getListSvcName() {
        return listSvcName;
    }

    public void setListSvcName(String listSvcName) {
        this.listSvcName = listSvcName;
    }

    public String getEditPageName() {
        return editPageName;
    }

    public void setEditPageName(String editPageName) {
        this.editPageName = editPageName;
    }

    public String getEditCtrName() {
        return editCtrName;
    }

    public void setEditCtrName(String editCtrName) {
        this.editCtrName = editCtrName;
    }
    //==========================类名格式 end======================================================

    //==========================生成类的包路径 begin======================================================
    protected String dtoPackage = "cs.model";
    protected String repoPackage = "cs.repository.repositoryImpl";
    protected String repoImplPackage = "cs.repository.repositoryImpl";
    protected String servicePackage = "cs.service";
    protected String serviceImplPackage = "cs.service";
    protected String controllerPackage = "cs.controller";

    public String getDtoPackage() {
        return dtoPackage;
    }

    public void setDtoPackage(String dtoPackage) {
        this.dtoPackage = dtoPackage;
    }

    public String getRepoPackage() {
        return repoPackage;
    }

    public void setRepoPackage(String repoPackage) {
        this.repoPackage = repoPackage;
    }

    public String getRepoImplPackage() {
        return repoImplPackage;
    }

    public void setRepoImplPackage(String repoImplPackage) {
        this.repoImplPackage = repoImplPackage;
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
    //============================生成类的包路径 end====================================================


    protected String beanName;
    protected String comment = "";
    /**
     * 作者名
     */
    protected String author = System.getProperty("user.name");
    /**
     * 输出目录路径
     */
    protected String ouputPath = "D:\\tmp"; // 默认在D盘创建一个零售目录（只支持Windows）
    /**
     * 是否覆盖
     */
    protected boolean fileOverride = false;
    /**
     * 是否打开输出目录
     */
    protected boolean open = true;
    /**
     * 模板路径
     */
    protected String templatePath;

    private PackageConfig packageConfig;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

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

    public String getOuputPath() {
        return ouputPath;
    }

    public void setOuputPath(String ouputPath) {
        this.ouputPath = ouputPath;
    }

    public boolean isFileOverride() {
        return fileOverride;
    }

    public void setFileOverride(boolean fileOverride) {
        this.fileOverride = fileOverride;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
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
