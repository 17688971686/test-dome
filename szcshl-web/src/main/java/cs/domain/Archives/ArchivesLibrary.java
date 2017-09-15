package cs.domain.Archives;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cs.domain.DomainBase;
@Entity
@Table(name = "cs_archives_Library")
public class ArchivesLibrary extends DomainBase{

	@Id
	private String id;
	
	//查阅单位
	@Column(columnDefinition = "varchar(255)")
	private String readCompany;
	
	//查阅人
	@Column(columnDefinition = "varchar(20)")
	private String readUsername;
	
	//查阅项目名称
	@Column(columnDefinition = "varchar(255)")
	private String readProjectName;
	
	//档案编号
    @Column(columnDefinition = "varchar(128)")
	private String readArchivesCode;
    
	//部门部长
    @Column(columnDefinition = "varchar(30)")
	private String deptMinister;
    
    //部长意见
    @Column(columnDefinition = "varchar(215)")
    private String deptMinisterIdeaContent;	
    
    //部长审批日期
    @Column(columnDefinition="date")
    private Date deptMinisterDate;

    //分管副主任
    @Column(columnDefinition = "varchar(30)")
	private String deptSLeader;
    
    //分管副主任签批
    @Column(columnDefinition = "varchar(215)")
    private String deptSLeaderIdeaContent;	
    
    //分管主任审批日期
    @Column(columnDefinition="date")
    private Date deptSleaderDate;
   
    //主任
    @Column(columnDefinition = "varchar(30)")
    private String deptDirector;
    
    //主任意见
    @Column(columnDefinition = "varchar(215)")
    private String deptDirectorIdeaContent;
    
    //主任审批日期
    @Column(columnDefinition="date")
    private Date deptDirectorDate;
    
	//查阅时间
    @Column(columnDefinition="date")
	private Date readDate;
	
	//归还世间
    @Column(columnDefinition="date")
	private Date resotoreDate;
	
	//存档管理员签名
    @Column(columnDefinition = "varchar(30)")
	private String archivesUserName;
    
   /**
    * 审批状态：0部长审批,1分管领导审批,2主任审批
    */
    @Column(columnDefinition = "varchar(4)")
    private String archivesStatus;
    
    /**
     * 借阅状态：0,中心档案借阅类型, 9市档案借阅类型
     */
    @Column(columnDefinition = "varchar(4)")
    private String archivesType;
    /**
     * 隐藏部分表单：0隐藏员工填写表单
     */
    @Column(columnDefinition = "varchar(4)")
    private String hideStatus;

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

	public void setArchivesUserName(String archivesUserName) {
		archivesUserName = archivesUserName;
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
