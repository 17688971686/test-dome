package cs.domain.sys;

import cs.domain.DomainBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 通知公告
 *
 * @author MCL
 * @date 2017年7月5日 下午3:14:13
 */
@Entity
@Table(name = "cs_annountment")
public class Annountment extends DomainBase {

    @Id
    @GeneratedValue(generator = "noticeGenerator")
    @GenericGenerator(name = "noticeGenerator", strategy = "uuid2")
    private String anId;

    /**
     * 公告标题
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String anTitle;

    /**
     * 发布部门
     */
    @Column(columnDefinition = "VARCHAR(126)")
    private String anOrg;

    /**
     * 是否置顶 （0:不置顶[默认]  9:置顶）
     */
    @Column(columnDefinition = "INTEGER")
    private Integer isStick;

    /**
     * 是否正式发布（0：否，9:是）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String issue;

    /**
     * 发布人
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String issueUser;

    /**
     * 发布日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date issueDate;
    /**
     * 排序
     */
    @Column(columnDefinition = "INTEGER")
    private Integer anSort;

    /**
     * 内容
     */
    @Column(columnDefinition = "CLOB")
    private String anContent;

    //部长ID
    @Column(columnDefinition = "varchar(64)")
    private String deptMinisterId;

    //部长签名
    @Column(columnDefinition = "varchar(32)")
    private String deptMinisterName;

    //部长意见/核稿意见
    @Column(columnDefinition = "varchar(256)")
    private String deptMinisterIdeaContent;

    //部长审批日期
    @Column(columnDefinition="date")
    private Date deptMinisterDate;

    /**
     * 分管副主任Id
     */

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

    /**
     * 是否需要流程
     */
    @Column(columnDefinition = "varchar(2)")
    private String needFlow;

    /**
     * 主任ID
     */
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

    /**
     * 流程实例ID
     */
    @Column(columnDefinition = "VARCHAR(63)")
    private String processInstanceId;

    /**
     * 审批状态：0部长审批,1副主任审批,2主任审批,9审批完成,8:强制结束
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String appoveStatus;


    public String getNeedFlow() {
        return needFlow;
    }

    public void setNeedFlow(String needFlow) {
        this.needFlow = needFlow;
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


    public Integer getAnSort() {
        return anSort;
    }


    public void setAnSort(Integer anSort) {
        this.anSort = anSort;
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
}
