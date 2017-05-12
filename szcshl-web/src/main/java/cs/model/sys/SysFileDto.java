package cs.model.sys;

import cs.model.BaseDto;
            
/**
 * Description: 附件 页面数据模型
 * author: ldm
 * Date: 2017-5-12 13:14:01
 */
public class SysFileDto extends BaseDto {

    private String sysFileId;
    private String businessId;
    private String fileUrl;
    private String showName;
    private Integer fileSize;
    private String fileType;
    private String proccessInstanceId;

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
    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getProccessInstanceId() {
		return proccessInstanceId;
	}
	public void setProccessInstanceId(String proccessInstanceId) {
		this.proccessInstanceId = proccessInstanceId;
	}	
}