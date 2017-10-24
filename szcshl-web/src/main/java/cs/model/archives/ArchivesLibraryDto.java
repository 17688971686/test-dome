package cs.model.archives;

import cs.model.BaseDto;
    
    
    
    
    
    
    
    import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;


/**
 * Description: 档案借阅管理 页面数据模型
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
public class ArchivesLibraryDto extends BaseDto {


    private String id;

    /**
     * 查阅单位
     */
    private String readCompany;

    /**
     * 查阅人
     */
    private String readUsername;

    /**
     * 查阅项目名称
     */
    private String readProjectName;

    /**
     * 档案编号
     */
    private String readArchivesCode;

    /**
     * 部长ID
     */
    private String deptMinisterId;

    /**
     * 部长名称
     */
    private String deptMinister;

    /**
     * 部长意见
     */
    private String deptMinisterIdeaContent;

    /**
     * 部长审批日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date deptMinisterDate;

    /**
     * 分管副主任ID
     */
    private String deptSLeaderId;

    /**
     * 分管副主任
     */
    private String deptSLeader;

    /**
     * 分管副主任签批
     */
    private String deptSLeaderIdeaContent;

    /**
     * 分管主任审批日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date deptSleaderDate;

    /**
     * 主任ID
     */
    private String deptDirectorId;

    /**
     * 主任
     */
    private String deptDirector;

    /**
     * 主任意见
     */
    private String deptDirectorIdeaContent;

    /**
     * 主任审批日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date deptDirectorDate;

    /**
     * 查阅时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date readDate;

    /**
     * 归还时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date resotoreDate;

    /**
     * 存档管理员签名
     */
    private String archivesUserName;

    /**
     * 审批状态：0部长审批,1分管领导审批,2主任审批
     */
    private String archivesStatus;

    /**
     * 档案类型：0,中心档案借阅类型, 9市档案借阅类型
     */
    private String archivesType;

    /**
     * 是否本单位人员借阅（9：是，0：否）
     */
    private String isSZECUser;

    /**
     * 是否外借（9：是，0：否）
     */
    private String isLendOut;

    /**
     * 是否同意借阅（9：同意，0：不同意）
     */
    private String isAgree;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

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

    public String getDeptMinisterId() {
        return deptMinisterId;
    }

    public void setDeptMinisterId(String deptMinisterId) {
        this.deptMinisterId = deptMinisterId;
    }

    public String getDeptMinister() {
        return deptMinister;
    }

    public void setDeptMinister(String deptMinister) {
        this.deptMinister = deptMinister;
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

    public String getDeptSLeaderId() {
        return deptSLeaderId;
    }

    public void setDeptSLeaderId(String deptSLeaderId) {
        this.deptSLeaderId = deptSLeaderId;
    }

    public String getDeptSLeader() {
        return deptSLeader;
    }

    public void setDeptSLeader(String deptSLeader) {
        this.deptSLeader = deptSLeader;
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

    public String getDeptDirectorId() {
        return deptDirectorId;
    }

    public void setDeptDirectorId(String deptDirectorId) {
        this.deptDirectorId = deptDirectorId;
    }

    public String getDeptDirector() {
        return deptDirector;
    }

    public void setDeptDirector(String deptDirector) {
        this.deptDirector = deptDirector;
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
        this.archivesUserName = archivesUserName;
    }

    public String getArchivesStatus() {
        return archivesStatus;
    }

    public void setArchivesStatus(String archivesStatus) {
        this.archivesStatus = archivesStatus;
    }

    public String getArchivesType() {
        return archivesType;
    }

    public void setArchivesType(String archivesType) {
        this.archivesType = archivesType;
    }

    public String getIsSZECUser() {
        return isSZECUser;
    }

    public void setIsSZECUser(String isSZECUser) {
        this.isSZECUser = isSZECUser;
    }

    public String getIsLendOut() {
        return isLendOut;
    }

    public void setIsLendOut(String isLendOut) {
        this.isLendOut = isLendOut;
    }

    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}