package cs.auto.core.config;

/**
 * 文件配置常量
 *
 * @author tzg
 * @date 2017/5/7 19:08
 */
public enum FileConst {
    domainCls("Domain", ".java", PackageConst.PACKAGE_DOMAIN, null),
    dtoCls("Dto", ".java", PackageConst.PACKAGE_DTO, TemplateConst.JAVA_DTO),
    repoCls("Repo", ".java", PackageConst.PACKAGE_REPO, TemplateConst.JAVA_REPO),
    repoImplCls("RepoImpl", ".java", PackageConst.PACKAGE_REPOIMPL, TemplateConst.JAVA_REPO_IMPL),
    serviceCls("Service", ".java", PackageConst.PACKAGE_SERVICE, TemplateConst.JAVA_SERVICE),
    serviceImplCls("ServiceImpl", ".java", PackageConst.PACKAGE_SERVICEIMPL, TemplateConst.JAVA_SERVICE_IMPL),
    controllerCls("Controller", ".java", PackageConst.PACKAGE_CONTROLLER, TemplateConst.JAVA_CONTROLLER),

    listHtml("List", ".html", null, TemplateConst.FRONT_LIST),
    listCtrlJs(".ctrl", ".js", null, TemplateConst.FRONT_LIST_CTRL),
    listSvcJs(".svc", ".js", null, TemplateConst.FRONT_LIST_SVC),
    editHtml("Edit", ".html", null, TemplateConst.FRONT_EDIT),
    editCtrJs(".edit.ctrl", ".js", null, TemplateConst.FRONT_EDIT_CTRL);


    private String suffix;          // 文件名后缀
    private String fileType;        // 文件类型（文件后缀，如.java）
    private String layer;           // 文件的层
    private String templateName;    // 模板名

    FileConst(String suffix, String fileType, String layer, String templateName) {
        this.suffix = suffix;
        this.fileType = fileType;
        this.layer = layer;
        this.templateName = templateName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
