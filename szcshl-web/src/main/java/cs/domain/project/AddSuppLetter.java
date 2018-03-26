package cs.domain.project;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
/*
 * 添加项目资料补充函
 */
@Entity
@Table(name = "cs_add_suppLetter")
@DynamicUpdate(true)
public class AddSuppLetter extends DomainBase {
	//拟稿编号
	@Id
	@GeneratedValue(generator = "suppLetterGenerator")
    @GenericGenerator(name = "suppLetterGenerator", strategy = "uuid2")
	private String id;

	/**
	 * 文件年度（月报简报用）
	 */
	@Column(columnDefinition="VARCHAR(4)")
	private String fileYear;

	/**
	 * 拟稿部门
	 */
	@Column(columnDefinition="VARCHAR(64)")
	private String orgName;

	/**
	 * 拟稿人
	 */
	@Column(columnDefinition="VARCHAR(64)")
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
	@Column(columnDefinition = "VARCHAR(16)")
	private String mergencyLevel;
	
	//文件字号
	@Column(columnDefinition ="VARCHAR(32)")
	private String filenum;
	
	//文件标题
	@Column(columnDefinition = "VARCHAR(256)")
	private String title;
	
	//发行范围
	@Column(columnDefinition = "VARCHAR(512)")
	private String dispaRange;

	//打印份数
	@Column(columnDefinition = "INTEGER")
	private Integer printnum;

	//文字序号
    @Column(columnDefinition = "INTEGER")
	private Integer fileSeq;

	//部长ID
	@Column(columnDefinition = "varchar(64)")
	private String deptMinisterId;

   //部长签名
    @Column(columnDefinition = "varchar(32)")
	private String deptMinisterName;
    
    //部长意见/核稿意见
    @Column(columnDefinition = "varchar(256)")
    private String deptMinisterIdeaContent;

    /**
     * 会签意见
     */
    @Column(columnDefinition = "varchar(1024)")
    private String leaderSignIdeaContent;

    //部长审批日期
    @Column(columnDefinition="date")
    private Date deptMinisterDate;

    //分管副主任名称
    @Column(columnDefinition = "varchar(64)")
    private String deptSLeaderId;

    //分管副主任名称
    @Column(columnDefinition = "varchar(32)")
	private String deptSLeaderName;
    
    //分管副主任签批/会签意见
    @Column(columnDefinition = "varchar(256)")
    private String deptSLeaderIdeaContent;	
    
    //分管主任审批日期
    @Column(columnDefinition="date")
    private Date deptSleaderDate;

    @Column(columnDefinition = "varchar(64)")
    private String deptDirectorId;

    //主任名称
    @Column(columnDefinition = "varchar(32)")
    private String deptDirectorName;
    
    //主任意见/领导意见
    @Column(columnDefinition = "varchar(256)")
    private String deptDirectorIdeaContent;
    
    //主任审批日期
    @Column(columnDefinition="date")
    private Date deptDirectorDate;
	
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

	//月报的文字序号
	@Column(columnDefinition = "INTEGER")
	private Integer monthlySeq;

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

    /**
     * 审批状态：0部长审批,1分管领导审批,2主任审批,9审批完成
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String appoveStatus;

    /**
     * 文件类型，1：表示你补充资料函，2：表示月报简报
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String fileType;

    /**
     * 流程实例ID
     */
    @Column(columnDefinition = "VARCHAR(63)")
    private String processInstanceId;

	public Integer getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(Integer fileSeq) {
		this.fileSeq = fileSeq;
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

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

    public String getBusinessType() {
        return businessType;
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

	public String getDeptMinisterName() {
		return deptMinisterName;
	}

	public void setDeptMinisterName(String deptMinisterName) {
		this.deptMinisterName = deptMinisterName;
	}

	public String getDeptMinisterIdeaContent() {
		return deptMinisterIdeaContent;
	}

	public void setDeptMinisterIdeaContent(String deptMinisterIdeaContent) {
		this.deptMinisterIdeaContent = deptMinisterIdeaContent;
	}

	public Date getDeptMinisterDate() {
		return deptMinisterDate;
	}

	public void setDeptMinisterDate(Date deptMinisterDate) {
		this.deptMinisterDate = deptMinisterDate;
	}

	public String getDeptSLeaderName() {
		return deptSLeaderName;
	}

	public void setDeptSLeaderName(String deptSLeaderName) {
		this.deptSLeaderName = deptSLeaderName;
	}

	public String getDeptSLeaderIdeaContent() {
		return deptSLeaderIdeaContent;
	}

	public void setDeptSLeaderIdeaContent(String deptSLeaderIdeaContent) {
		this.deptSLeaderIdeaContent = deptSLeaderIdeaContent;
	}

	public Date getDeptSleaderDate() {
		return deptSleaderDate;
	}

	public void setDeptSleaderDate(Date deptSleaderDate) {
		this.deptSleaderDate = deptSleaderDate;
	}

	public String getDeptDirectorName() {
		return deptDirectorName;
	}

	public void setDeptDirectorName(String deptDirectorName) {
		this.deptDirectorName = deptDirectorName;
	}

	public String getDeptDirectorIdeaContent() {
		return deptDirectorIdeaContent;
	}

	public void setDeptDirectorIdeaContent(String deptDirectorIdeaContent) {
		this.deptDirectorIdeaContent = deptDirectorIdeaContent;
	}

	public Date getDeptDirectorDate() {
		return deptDirectorDate;
	}

	public void setDeptDirectorDate(Date deptDirectorDate) {
		this.deptDirectorDate = deptDirectorDate;
	}

    public String getAppoveStatus() {
        return appoveStatus;
    }

    public void setAppoveStatus(String appoveStatus) {
        this.appoveStatus = appoveStatus;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDeptMinisterId() {
        return deptMinisterId;
    }

    public void setDeptMinisterId(String deptMinisterId) {
        this.deptMinisterId = deptMinisterId;
    }

    public String getLeaderSignIdeaContent() {
        return leaderSignIdeaContent;
    }

    public void setLeaderSignIdeaContent(String leaderSignIdeaContent) {
        this.leaderSignIdeaContent = leaderSignIdeaContent;
    }

    public String getDeptSLeaderId() {
        return deptSLeaderId;
    }

    public void setDeptSLeaderId(String deptSLeaderId) {
        this.deptSLeaderId = deptSLeaderId;
    }

    public String getDeptDirectorId() {
        return deptDirectorId;
    }

    public void setDeptDirectorId(String deptDirectorId) {
        this.deptDirectorId = deptDirectorId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public Integer getMonthlySeq() {
		return monthlySeq;
	}

	public void setMonthlySeq(Integer monthlySeq) {
		this.monthlySeq = monthlySeq;
	}

    public String getFileYear() {
        return fileYear;
    }

    public void setFileYear(String fileYear) {
        this.fileYear = fileYear;
    }
}
