package cs.model.sys;

import javax.persistence.Column;

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
    private String processInstanceId;
  	private String sysSingId;  //系统收文ID
  	private String sysfileType;//系统文件类型
  	private String sysMinType;	//小类

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

    public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getSysSingId() {
		return sysSingId;
	}

	public void setSysSingId(String sysSingId) {
		this.sysSingId = sysSingId;
	}

	public String getSysfileType() {
		return sysfileType;
	}

	public void setSysfileType(String sysfileType) {
		this.sysfileType = sysfileType;
	}

	public String getSysMinType() {
		return sysMinType;
	}

	public void setSysMinType(String sysMinType) {
		this.sysMinType = sysMinType;
	}	
	
	
}