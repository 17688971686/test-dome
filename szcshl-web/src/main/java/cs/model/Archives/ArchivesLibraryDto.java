package cs.model.Archives;

import cs.domain.Archives.ArchivesLibrary;
import cs.model.BaseDto;
    
    
    
    
    
    
    
    import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
    


/**
 * Description: 档案借阅管理 页面数据模型
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
public class ArchivesLibraryDto extends BaseDto {

    private String id;
    private String readCompany;
    
    private String readUsername;
    
    private String readProjectName;
    
    private String readArchivesCode;
    
    private String deptMinister;
    
    private String deptMinisterIdeaContent;	
    
    @JSONField(format = "yyyy-MM-dd")
    private Date deptMinisterDate;
    
    private String deptSLeader;
    
    private String deptSLeaderIdeaContent;	
    
    @JSONField(format = "yyyy-MM-dd")
    private Date deptSleaderDate;
    
    private String deptDirector;
    
    private String deptDirectorIdeaContent;
    
    @JSONField(format = "yyyy-MM-dd")
    private Date deptDirectorDate;
    
    @JSONField(format = "yyyy-MM-dd")
    private Date readDate;
    
    @JSONField(format = "yyyy-MM-dd")
    private Date resotoreDate;
    
    private String archivesUserName;
    
    private String archivesStatus;
    
    private String archivesType;
    
    private String hideStatus;

    public ArchivesLibraryDto() {
    }
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getReadCompany() {
        return readCompany;
    }

    public void setReadCompany(String readCompany) {
        this.readCompany = readCompany;
    }
    public String getReadUsername() {
        return readUsername;
    }

    public void setReadUsername(String readUsername) {
        this.readUsername = readUsername;
    }
    public String getReadProjectName() {
        return readProjectName;
    }

    public void setReadProjectName(String readProjectName) {
        this.readProjectName = readProjectName;
    }
    public String getReadArchivesCode() {
        return readArchivesCode;
    }

    public void setReadArchivesCode(String readArchivesCode) {
        this.readArchivesCode = readArchivesCode;
    }
    public String getDeptMinister() {
        return deptMinister;
    }

    public void setDeptMinister(String deptMinister) {
        this.deptMinister = deptMinister;
    }
    public String getDeptSLeader() {
        return deptSLeader;
    }

    public void setDeptSLeader(String deptSLeader) {
        this.deptSLeader = deptSLeader;
    }
    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }
    public Date getResotoreDate() {
        return resotoreDate;
    }

    public void setResotoreDate(Date resotoreDate) {
        this.resotoreDate = resotoreDate;
    }
    public String getArchivesUserName() {
        return archivesUserName;
    }

    public void setArchivesUserName(String ArchivesUserName) {
        this.archivesUserName = ArchivesUserName;
    }

	public String getDeptDirector() {
		return deptDirector;
	}

	public void setDeptDirector(String deptDirector) {
		this.deptDirector = deptDirector;
	}

	public String getArchivesStatus() {
		return archivesStatus;
	}

	public void setArchivesStatus(String archivesStatus) {
		this.archivesStatus = archivesStatus;
	}

	public String getDeptMinisterIdeaContent() {
		return deptMinisterIdeaContent;
	}

	public void setDeptMinisterIdeaContent(String deptMinisterIdeaContent) {
		this.deptMinisterIdeaContent = deptMinisterIdeaContent;
	}

	public String getDeptSLeaderIdeaContent() {
		return deptSLeaderIdeaContent;
	}

	public void setDeptSLeaderIdeaContent(String deptSLeaderIdeaContent) {
		this.deptSLeaderIdeaContent = deptSLeaderIdeaContent;
	}

	public String getDeptDirectorIdeaContent() {
		return deptDirectorIdeaContent;
	}

	public void setDeptDirectorIdeaContent(String deptDirectorIdeaContent) {
		this.deptDirectorIdeaContent = deptDirectorIdeaContent;
	}

	public String getArchivesType() {
		return archivesType;
	}

	public void setArchivesType(String archivesType) {
		this.archivesType = archivesType;
	}

	public String getHideStatus() {
		return hideStatus;
	}

	public void setHideStatus(String hideStatus) {
		this.hideStatus = hideStatus;
	}

	public Date getDeptMinisterDate() {
		return deptMinisterDate;
	}

	public void setDeptMinisterDate(Date deptMinisterDate) {
		this.deptMinisterDate = deptMinisterDate;
	}

	public Date getDeptSleaderDate() {
		return deptSleaderDate;
	}

	public void setDeptSleaderDate(Date deptSleaderDate) {
		this.deptSleaderDate = deptSleaderDate;
	}

	public Date getDeptDirectorDate() {
		return deptDirectorDate;
	}

	public void setDeptDirectorDate(Date deptDirectorDate) {
		this.deptDirectorDate = deptDirectorDate;
	}

	
	
	
    
}