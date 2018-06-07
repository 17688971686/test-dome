package cs.model.sys;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import java.util.Date;
import java.util.List;

public class AnnountmentDto extends BaseDto {

    private String anId;
    private String anTitle;    //公告标题
    private String anOrg;    //发布部门
    @JSONField(format = "yyyy-MM-dd")
    private Date anDate;    //发布时间
    private String anUser;    //发布人
    /**
     * 是否置顶 （0:不置顶[默认]  9:置顶）
     */
    private Integer isStick;
    /**
     * 是否正式发布（0：否，9:是）
     */
    private String issue;
    private String issueUser;
    private Date issueDate;
    private Integer anSort;        //	排序
    private String anContent;    //内容

    //部长ID
    private String deptMinisterId;

    //部长签名
    private String deptMinisterName;

    //部长意见/核稿意见
    private String deptMinisterIdeaContent;

    //部长审批日期
    private Date deptMinisterDate;

    //分管副主任名称
    private String deptSLeaderId;

    //分管副主任名称
    private String deptSLeaderName;

    //分管副主任签批/会签意见
    private String deptSLeaderIdeaContent;

    //分管主任审批日期
    private Date deptSleaderDate;

    private String deptDirectorId;

    //主任名称
    private String deptDirectorName;

    //主任意见/领导意见
    private String deptDirectorIdeaContent;

    //主任审批日期
    private Date deptDirectorDate;
    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 审批状态：0部长审批,1副主任审批,2主任审批,9审批完成
     */
    private String appoveStatus;
    /**
     * 是否需要审批
     */
    private String needFlow;
    /**
     * 附件列表
     */
    private List<SysFileDto> sysFileList;

    public List<SysFileDto> getSysFileList() {
        return sysFileList;
    }

    public void setSysFileList(List<SysFileDto> sysFileList) {
        this.sysFileList = sysFileList;
    }

    public String getAnId() {
        return anId;
    }

    public void setAnId(String anId) {
        this.anId = anId;
    }

    public String getAnTitle() {
        return anTitle;
    }

    public void setAnTitle(String anTitle) {
        this.anTitle = anTitle;
    }

    public String getAnOrg() {
        return anOrg;
    }

    public void setAnOrg(String anOrg) {
        this.anOrg = anOrg;
    }

    public Date getAnDate() {
        return anDate;
    }

    public void setAnDate(Date anDate) {
        this.anDate = anDate;
    }

    public Integer getAnSort() {
        return anSort;
    }

    public void setAnSort(Integer anSort) {
        this.anSort = anSort;
    }

    public String getAnUser() {
        return anUser;
    }

    public void setAnUser(String anUser) {
        this.anUser = anUser;
    }

    public String getAnContent() {
        return anContent;
    }

    public void setAnContent(String anContent) {
        this.anContent = anContent;
    }

    public Integer getIsStick() {
        return isStick;
    }

    public void setIsStick(Integer isStick) {
        this.isStick = isStick;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueUser() {
        return issueUser;
    }

    public void setIssueUser(String issueUser) {
        this.issueUser = issueUser;
    }

    public String getDeptMinisterId() {
        return deptMinisterId;
    }

    public void setDeptMinisterId(String deptMinisterId) {
        this.deptMinisterId = deptMinisterId;
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

    public String getDeptSLeaderId() {
        return deptSLeaderId;
    }

    public void setDeptSLeaderId(String deptSLeaderId) {
        this.deptSLeaderId = deptSLeaderId;
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

    public String getDeptDirectorId() {
        return deptDirectorId;
    }

    public void setDeptDirectorId(String deptDirectorId) {
        this.deptDirectorId = deptDirectorId;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getAppoveStatus() {
        return appoveStatus;
    }

    public void setAppoveStatus(String appoveStatus) {
        this.appoveStatus = appoveStatus;
    }

    public String getNeedFlow() {
        return needFlow;
    }

    public void setNeedFlow(String needFlow) {
        this.needFlow = needFlow;
    }
}
