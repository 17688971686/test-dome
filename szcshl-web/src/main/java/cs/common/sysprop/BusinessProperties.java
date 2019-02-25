package cs.common.sysprop;

/**
 * 系统配置属性文件
 * 类名不要修改，避免spring 获取bean的时候获取出错
 * Created by ldm on 2018/12/5 0005.
 */
public class BusinessProperties {

    private String fgwProjIfs;
    private String fgwPreProjIfs;
    private String rtxUrl;
    private String rtxIp;
    private String fileUploadPath;

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
}
