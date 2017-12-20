package cs.model.sys;

import cs.model.BaseDto;
            
/**
 * Description: 附件 页面数据模型
 * author: ldm
 * Date: 2017-5-12 13:14:01
 */
public class SysFileDto extends BaseDto {

    //主键
    private String sysFileId;

    private String businessId;

    private String fileUrl;

    private String showName;

    private Long fileSize;

    private String fileType;

    /**
     * 主键ID，例如，收文为收文ID，课题研究为课题研究ID
     */
    private String mainId;

    /**
     * 主要业务名称，所有统一类型的附件，放在同一个文件夹（例如：收文为sign,课题为 topic,....）
     */
    private String mainType;

    /**
     * 文件模块类型（例如，收文环节，为收文，工作方案环节，为工作方案，跟businessId对应）
     */
    private String sysfileType;

    /**
     * 文件业务类型（例如，评审方案，会签准备材料等）
     */
    private String sysBusiType;

    /**
     * 文件大小，主要用于显示
     */
    private String fileSizeStr;

    public SysFileDto() {
    }

    public String getSysFileId() {
        return sysFileId;
    }

    public void setSysFileId(String sysFileId) {
        this.sysFileId = sysFileId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getSysfileType() {
        return sysfileType;
    }

    public void setSysfileType(String sysfileType) {
        this.sysfileType = sysfileType;
    }

    public String getSysBusiType() {
        return sysBusiType;
    }

    public void setSysBusiType(String sysBusiType) {
        this.sysBusiType = sysBusiType;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
    }
}