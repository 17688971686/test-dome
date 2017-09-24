package cs.model.project;

import cs.domain.project.AddSuppLetter;
import cs.domain.project.Sign;
import cs.model.BaseDto;

import java.util.Date;

import javax.persistence.Column;

import com.alibaba.fastjson.annotation.JSONField;
    
    
    
    
    
    
    


/**
 * Description: 项目资料补充函 页面数据模型
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
public class AddSuppLetterDto extends BaseDto {

	private String id;
    private String orgName;
    private String userName;
    @JSONField(format = "yyyy-MM-dd")
    private Date suppLetterTime;
    private String secretLevel;
    private String mergencyLevel;
    private String filenum;
    private String title;
    private String dispaRange;
    private String suppleterSuggest;
    private Integer printnum;
    @JSONField(format = "yyyy-MM-dd")
    private Date disapDate;
    private String meetingSuggest;
    private String leaderSuggest;
    private Integer fileSeq;
    private String userId;
    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 业务类型（为了方便初始化【SIGN:表示项目，TOPIC:表示课题研究】）
     */
	private String businessType;
    
	private String businessIdType;
	
	//拟补充资料函状态：0：查询状态 9：审批状态，
    private String addSuppStatus;
    
    //审批状态：0部长审批,1分管领导审批,2主任审批
    private String addSuppAppoveStatus;
	
    //部门部长名称
	private String deptMinisterName;
    
    //部长意见
    private String deptMinisterIdeaContent;	
    
    //部长审批日期
    @JSONField(format = "yyyy-MM-dd")
    private Date deptMinisterDate;

    //分管副主任名称
	private String deptSLeaderName;
    
    //分管副主任签批
    private String deptSLeaderIdeaContent;	
    
    //分管主任审批日期
    @JSONField(format = "yyyy-MM-dd")
    private Date deptSleaderDate;
   
    //主任名称
    private String deptDirectorName;
    
    //主任意见
    private String deptDirectorIdeaContent;
    
    //主任审批日期
    @JSONField(format = "yyyy-MM-dd")
    private Date deptDirectorDate;
    
	//1公文类型
    private String missiveType;
    
    //2公文类型月报简报
    private String missiveMonthlyType;
    
    //3公文类型月报简报
     private String  missiveOtherType;
    //月报简报类型
    private String monthlyType;
    
    //存档编号
    private String fileCode;
    
    //月报简报状态：0.表示为中心文件（稿纸）
    private String monthlyStatus;
    
   
    
    public Date getDisapDate() {
		return disapDate;
	}

	public void setDisapDate(Date disapDate) {
		this.disapDate = disapDate;
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

	

	public AddSuppLetterDto() {
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

    public Integer getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(Integer fileSeq) {
		this.fileSeq = fileSeq;
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
	public String getBusinessIdType() {
		return businessIdType;
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
	

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

	public String getAddSuppStatus() {
		return addSuppStatus;
	}

	public void setAddSuppStatus(String addSuppStatus) {
		this.addSuppStatus = addSuppStatus;
	}

	public String getAddSuppAppoveStatus() {
		return addSuppAppoveStatus;
	}

	public void setAddSuppAppoveStatus(String addSuppAppoveStatus) {
		this.addSuppAppoveStatus = addSuppAppoveStatus;
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
	
    
}