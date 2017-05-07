package cs.auto.core.config;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础配置抽象类
 *
 * @author tzg
 * @date 2017/5/7 15:29
 */
public abstract class AbstractGanConfig {

    protected Logger logger = Logger.getLogger(this.getClass());

    public abstract void generateParams();

    /**
     * freemarker模板参数
     */
    protected Map<String, Object> paramMap = new HashMap<String, Object>(10);
    protected List<FileConfig> fileConfs = new ArrayList<FileConfig>();

    /**
     * 模板路径
     */
    protected String templatePath;

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

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public List<FileConfig> getFileConfs() {
        return fileConfs;
    }

    public void setFileConfs(List<FileConfig> fileConfs) {
        this.fileConfs = fileConfs;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
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


    protected String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
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

}
