package cs.model.project;

import cs.domain.project.AddSuppLetter;
import cs.domain.project.Sign;
import cs.model.BaseDto;

import java.util.Date;

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
    private String printnum;
    private String signid;
    @JSONField(format = "yyyy-MM-dd")
    private Date disapDate;
    private String meetingSuggest;
    private String leaderSuggest;
    private Integer fileSeq;
    private String userId;
    
    
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
    public String getPrintnum() {
        return printnum;
    }

    public void setPrintnum(String printnum) {
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

	public String getSignid() {
		return signid;
	}

	public void setSignid(String signid) {
		this.signid = signid;
	}
	
	

}