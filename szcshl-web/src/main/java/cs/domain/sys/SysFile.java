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
	
	@Column(columnDefinition = "varchar(5)")
	private String fileType;	
	
	@Column(columnDefinition = "varchar(64)")
	private String processInstanceId;
	
	//系统收文ID
	@Column(columnDefinition = "varchar(64)")
	private String sysSingId;
	
	//系统文件类型
	@Column(columnDefinition = "varchar(64)")
	private String sysfileType;
	
	@Column(columnDefinition = "varchar(64)")
	private String sysMinType;
	
	
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

	/**
	 * 全参数构造函数
	 * @param sysFileId
	 * @param businessId
	 * @param fileUrl
	 * @param showName
	 * @param fileSize
	 * @param fileType
	 * @param processInstanceId
	 * @param sysSingId
	 * @param sysfileType
	 * @param sysMinType
	 */
	public SysFile(String sysFileId, String businessId, String fileUrl, String showName, Integer fileSize, String fileType, String processInstanceId, String sysSingId, String sysfileType, String sysMinType) {
		this.sysFileId = sysFileId;
		this.businessId = businessId;
		this.fileUrl = fileUrl;
		this.showName = showName;
		this.fileSize = fileSize;
		this.fileType = fileType;
		this.processInstanceId = processInstanceId;
		this.sysSingId = sysSingId;
		this.sysfileType = sysfileType;
		this.sysMinType = sysMinType;
	}

    public SysFile() {
        super();
    }
}
