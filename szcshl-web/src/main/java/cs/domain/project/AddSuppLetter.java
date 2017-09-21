package cs.domain.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.alibaba.fastjson.annotation.JSONField;

import cs.domain.DomainBase;
/*
 * 添加项目资料补充函
 */
@Entity
@Table(name = "cs_add_suppLetter")
@DynamicUpdate(true)
public class AddSuppLetter extends DomainBase {
	//拟稿编号
	@Id
	private String id;
	
   //拟稿部门
	@Column(columnDefinition="VARCHAR(64)")
	private String orgName;
	
	//拟稿人
	@Column(columnDefinition = "VARCHAR(64)")	
	private String userName;
	
	//拟稿时间
	@Column(columnDefinition = "Date")
	private Date suppLetterTime;
	
	//发文日期
	@Column(columnDefinition = "Date")
	private Date disapDate;
	
	//秘密等级
	@Column(columnDefinition = "VARCHAR(32)")
	private String secretLevel;
	
	//缓急程度
	@Column(columnDefinition = "VARCHAR(10)")
	private String mergencyLevel;
	
	//文件字号
	@Column(columnDefinition ="VARCHAR(32)")
	private String filenum;
	
	//用户登录id
	@Column(columnDefinition ="VARCHAR(100)")
	private String userId;
	
	//文件标题
	@Column(columnDefinition = "VARCHAR(200)")
	private String title;
	
	//发行范围
	@Column(columnDefinition = "VARCHAR(225)")
	private String dispaRange;
	
	//核稿意见
	@Column(columnDefinition = "VARCHAR(225)")
	private String suppleterSuggest;
	
	//会签意见
	@Column(columnDefinition = "VARCHAR(225)")
	private String meetingSuggest;
	
	//领导意见
	@Column(columnDefinition = "VARCHAR(225)")
	private String leaderSuggest;
	
	//打印份数
	@Column(columnDefinition = "INTEGER")
	private Integer printnum;

	/**
	 * 业务ID
	 */
	@Column(columnDefinition = "VARCHAR(64)")
	private String businessId;

	/**
	 * 业务类型（为了方便初始化【SIGN:表示项目，TOPIC:表示课题研究】）
	 */
	@Column(columnDefinition = "VARCHAR(64)")
	private String businessType;
	
	@Column(columnDefinition = "VARCHAR(64)")
	private String businessIdType;
	//文字序号
    @Column(columnDefinition = "INTEGER")
	private Integer fileSeq;
	
    //1公文类型
    @Column(columnDefinition = "VARCHAR(64)")
    private String missiveType;
    
    //2公文类型月报简报
    @Column(columnDefinition = "VARCHAR(64)")
    private String missiveMonthlyType;
    
   //3公文类型月报简报
    @Column(columnDefinition = "VARCHAR(64)")
    private String  missiveOtherType;
    
    //月报简报类型
    @Column(columnDefinition = "VARCHAR(64)")
    private String monthlyType;
    
    //存档编号
    @Column(columnDefinition = "VARCHAR(64)")
    private String fileCode;
    
    //月报简报状态：0.表示为中心文件（稿纸）
    @Column(columnDefinition = "VARCHAR(4)")
    private String monthlyStatus;
    
	public Integer getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(Integer fileSeq) {
		this.fileSeq = fileSeq;
	}

	public String getMeetingSuggest() {
		return meetingSuggest;
	}

	public void setMeetingSuggest(String meetingSuggest) {
		this.meetingSuggest = meetingSuggest;
	}

	public String getLeaderSuggest() {
		return leaderSuggest;
	}

	public void setLeaderSuggest(String leaderSuggest) {
		this.leaderSuggest = leaderSuggest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getSuppLetterTime() {
		return suppLetterTime;
	}

	public void setSuppLetterTime(Date suppLetterTime) {
		this.suppLetterTime = suppLetterTime;
	}

	public String getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}

	public String getMergencyLevel() {
		return mergencyLevel;
	}

	public void setMergencyLevel(String mergencyLevel) {
		this.mergencyLevel = mergencyLevel;
	}

	public String getFilenum() {
		return filenum;
	}

	public void setFilenum(String filenum) {
		this.filenum = filenum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDispaRange() {
		return dispaRange;
	}

	public void setDispaRange(String dispaRange) {
		this.dispaRange = dispaRange;
	}

	public String getSuppleterSuggest() {
		return suppleterSuggest;
	}

	public void setSuppleterSuggest(String suppleterSuggest) {
		this.suppleterSuggest = suppleterSuggest;
	}

    public Integer getPrintnum() {
        return printnum;
    }

    public void setPrintnum(Integer printnum) {
        this.printnum = printnum;
    }

    public Date getDisapDate() {
		return disapDate;
	}

	public void setDisapDate(Date disapDate) {
		this.disapDate = disapDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
	public void setBusinessIdType(String businessIdType) {
		this.businessIdType = businessIdType;
	}

	public String getMissiveType() {
		return missiveType;
	}

	public void setMissiveType(String missiveType) {
		this.missiveType = missiveType;
	}

	public String getMonthlyType() {
		return monthlyType;
	}

	public void setMonthlyType(String monthlyType) {
		this.monthlyType = monthlyType;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public String getMonthlyStatus() {
		return monthlyStatus;
	}

	public void setMonthlyStatus(String monthlyStatus) {
		this.monthlyStatus = monthlyStatus;
	}

	public String getBusinessIdType() {
		return businessIdType;
	}

	public String getMissiveMonthlyType() {
		return missiveMonthlyType;
	}

	public void setMissiveMonthlyType(String missiveMonthlyType) {
		this.missiveMonthlyType = missiveMonthlyType;
	}

	public String getMissiveOtherType() {
		return missiveOtherType;
	}

	public void setMissiveOtherType(String missiveOtherType) {
		this.missiveOtherType = missiveOtherType;
	}
	
	
	
}
