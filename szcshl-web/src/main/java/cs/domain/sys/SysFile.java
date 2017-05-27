package cs.domain.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cs.domain.DomainBase;

/**
 * @author ldm
 * 系统文件
 * 
 * */
@Entity
@Table(name = "cs_sysfile")
public class SysFile extends DomainBase{
	//主键
	@Id
	private String sysFileId;
	
	@Column(columnDefinition = "varchar(64) NOT NULL")
	private String businessId;
	
	@Column(columnDefinition = "varchar(200) NOT NULL")
	private String fileUrl;
	
	@Column(columnDefinition = "varchar(100) NOT NULL")
	private String showName;
	
	@Column(columnDefinition = "INTEGER")
	private Integer fileSize;
	
	@Column(columnDefinition = "varchar(10)")
	private String fileType;	
	
	@Column(columnDefinition = "varchar(64)")
	private String processInstanceId;
	
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

	
}
