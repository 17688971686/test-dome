package cs.common.sysprop;

/**
 * 系统配置属性文件
 * 类名不要修改，避免spring 获取bean的时候获取出错
 * @author ldm on 2018/12/5 0005.
 */
public class BusinessProperties {

    private String fgwProjIfs;
    private String fgwPreProjIfs;
    private String rtxUrl;
    private String rtxIp;
    /**
     * 文件上传路径
     */
    private String fileUploadPath;
    /**
     * 系统标题名称
     */
    private String projectTitle;
    /**
     * 系统通用单位名称
     */
    private String accreditUnit;

    /**
     * 登录也logo
     */
    private String logoName;
    /**
     * 登录页标题图片
     */
    private String logoTitle;
    /**
     * 首页图片
     */
    private String homeLogo;


    public String getFgwProjIfs() {
        return fgwProjIfs;
    }

    public void setFgwProjIfs(String fgwProjIfs) {
        this.fgwProjIfs = fgwProjIfs;
    }

    public String getFgwPreProjIfs() {
        return fgwPreProjIfs;
    }

    public void setFgwPreProjIfs(String fgwPreProjIfs) {
        this.fgwPreProjIfs = fgwPreProjIfs;
    }

    public String getRtxUrl() {
        return rtxUrl;
    }

    public void setRtxUrl(String rtxUrl) {
        this.rtxUrl = rtxUrl;
    }

    public String getRtxIp() {
        return rtxIp;
    }

    public void setRtxIp(String rtxIp) {
        this.rtxIp = rtxIp;
    }

    public String getFileUploadPath() {
        return fileUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getAccreditUnit() {
        return accreditUnit;
    }

    public void setAccreditUnit(String accreditUnit) {
        this.accreditUnit = accreditUnit;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getLogoTitle() {
        return logoTitle;
    }

    public void setLogoTitle(String logoTitle) {
        this.logoTitle = logoTitle;
    }

    public String getHomeLogo() {
        return homeLogo;
    }

    public void setHomeLogo(String homeLogo) {
        this.homeLogo = homeLogo;
    }
}
