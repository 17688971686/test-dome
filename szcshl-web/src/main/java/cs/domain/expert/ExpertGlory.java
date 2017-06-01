package cs.domain.expert;

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
@Entity
@Table(name="cs_expert_glory")
public class ExpertGlory extends DomainBase {

	@Id
	private String gID; //Id
	
	@Column(name = "gQualifications", nullable = true)
    private String gQualifications;//学历
	
    @Column(name = "gTitle", nullable = true)
    private String gTitle; //职称
    
    @Column(name = "gGetCertifiDate", nullable = true, length = 30)
    private Date gGetCertifiDate; //发证日期
    
    @Column(name = "gExpiryDate", nullable = true, length = 30)
    private String gExpiryDate; //有效期
    
    @Column(name = "gFileNum", nullable = true, length = 30)
    private String gFileNum; //档案编号
    
    @Column(name = "gExpiryDateTo", nullable = true, length = 30)
    private Date gExpiryDateTo; //有效期至
    
    @Column(name = "isGetCertificate", nullable = true, length = 5)
    private String isGetCertificate; //是否发证
    
    @Column(name = "isNoUsefull", nullable = true, length = 5)
    private String isNoUsefull; //是否作废
    
    @Column(name = "gRemark", nullable = true)
    private String gRemark; //备注
    
    @ManyToOne
    @JoinColumn(name="expertID")
    private Expert expert;
    
    
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
	public Expert getExpert() {
		return expert;
	}
	public void setExpert(Expert expert) {
		this.expert = expert;
	}
	public String getGloryRemark() {
		return gRemark;
	}
	public void setGloryRemark(String gloryRemark) {
		this.gRemark = gloryRemark;
	}
	
    
	
}
