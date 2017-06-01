package cs.model.expert;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

import cs.domain.DomainBase;

/**
 *专家荣誉
 * @author Administrator
 *
 */

/**
 * @author Administrator
 *
 */
public class ExpertGloryDto extends DomainBase {

	private String gID; //Id
	
    private String gQualifications;//学历
	
    private String gTitle; //职称
    
    @JSONField(format = "yyyy-MM-dd")
    private Date gGetCertifiDate; //发证日期
    
    private String gExpiryDate; //有效期
    
    private String gFileNum; //档案编号
    
    @JSONField(format = "yyyy-MM-dd")
    private Date gExpiryDateTo; //有效期至
    
    private String isGetCertificate; //是否发证
    
    private String isNoUsefull; //是否作废
    
    private String gRemark; //备注
    
    private String  expertID; //专家编号
    
    private ExpertDto expertDto;
    
    
	public String getgID() {
		return gID;
	}
	public void setgID(String gID) {
		this.gID = gID;
	}

	public String getgQualifications() {
		return gQualifications;
	}
	public void setgQualifications(String gQualifications) {
		this.gQualifications = gQualifications;
	}
	public String getgTitle() {
		return gTitle;
	}
	public void setgTitle(String gTitle) {
		this.gTitle = gTitle;
	}
	public Date getgGetCertifiDate() {
		return gGetCertifiDate;
	}
	public void setgGetCertifiDate(Date gGetCertifiDate) {
		this.gGetCertifiDate = gGetCertifiDate;
	}
	public String getgExpiryDate() {
		return gExpiryDate;
	}
	public void setgExpiryDate(String gExpiryDate) {
		this.gExpiryDate = gExpiryDate;
	}
	public String getgFileNum() {
		return gFileNum;
	}
	public void setgFileNum(String gFileNum) {
		this.gFileNum = gFileNum;
	}
	public Date getgExpiryDateTo() {
		return gExpiryDateTo;
	}
	public void setgExpiryDateTo(Date gExpiryDateTo) {
		this.gExpiryDateTo = gExpiryDateTo;
	}
	public String getgRemark() {
		return gRemark;
	}
	public void setgRemark(String gRemark) {
		this.gRemark = gRemark;
	}
	public String getIsGetCertificate() {
		return isGetCertificate;
	}
	public void setIsGetCertificate(String isGetCertificate) {
		this.isGetCertificate = isGetCertificate;
	}
	public String getIsNoUsefull() {
		return isNoUsefull;
	}
	public void setIsNoUsefull(String isNoUsefull) {
		this.isNoUsefull = isNoUsefull;
	}
	public String getGloryRemark() {
		return gRemark;
	}
	public void setGloryRemark(String gloryRemark) {
		this.gRemark = gloryRemark;
	}
	public ExpertDto getExpertDto() {
		return expertDto;
	}
	public void setExpertDto(ExpertDto expertDto) {
		this.expertDto = expertDto;
	}
	public String getExpertID() {
		return expertID;
	}
	public void setExpertID(String expertID) {
		this.expertID = expertID;
	}
	
    
	
}
