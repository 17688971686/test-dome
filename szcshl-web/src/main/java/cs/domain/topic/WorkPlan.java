package cs.domain.topic;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 课题研究工作方案
 * Created by ldm on 2017/9/4 0004.
 */
@Entity
@Table(name="cs_topic_workplan")
@DynamicUpdate(true)
public class WorkPlan extends DomainBase {

    @Id
    @GeneratedValue(generator= "workPlanGenerator")
    @GenericGenerator(name= "workPlanGenerator",strategy = "uuid2")
    private String id;

    /**
     * 课题名称
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String topicName;

    /**
     * 课题合作单位
     */
    @Column(columnDefinition="VARCHAR(256)")
    private String cooperator;

    /**
     * 联系人
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String contactName;

    /**
     * 联系电话
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String tellPhone;

    /**
     * 传真
     */
    @Column(columnDefinition="VARCHAR(16)")
    private String fax;

    /**
     * 课题研究内容和研究成果
     */
    @Column(columnDefinition="CLOB")
    private String topicContent;

    /**
     * 经费估算
     */
    @Column(columnDefinition="NUMBER")
    private BigDecimal cost;

    /**
     * 调研时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date researchDate;

    /**
     * 调研时间段
     */
    @Column(columnDefinition="VARCHAR(5)")
    private String studyQuantum;
    /**
     * 拟要求单位及领导
     */
    @Column(columnDefinition="VARCHAR(512)")
    private String unitAndLeader;

    /**
     * 部门主管意见
     */
    @Column(columnDefinition="VARCHAR(512)")
    private String directorOption;

    /**
     * 部门主管名称
     */
    @Column(columnDefinition="VARCHAR(32)")
    private String directorName;

    /**
     * 部门主管审批日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date directorDate;

    /**
     * 领导批示
     */
    @Column(columnDefinition="VARCHAR(512)")
    private String leaderOption;

    /**
     * 领导名称
     */
    @Column(columnDefinition="VARCHAR(32)")
    private String leaderName;

    /**
     * 领导批示日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date leaderDate;

    /**
     * 主任批示
     */
    @Column(columnDefinition="VARCHAR(512)")
    private String mleaderOption;

    /**
     * 主任名称
     */
    @Column(columnDefinition="VARCHAR(32)")
    private String mleaderName;

    /**
     * 主任批示日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date mleaderDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="topId",unique = true)
    private TopicInfo topicInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getCooperator() {
        return cooperator;
    }

    public void setCooperator(String cooperator) {
        this.cooperator = cooperator;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTellPhone() {
        return tellPhone;
    }

    public void setTellPhone(String tellPhone) {
        this.tellPhone = tellPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getResearchDate() {
        return researchDate;
    }

    public void setResearchDate(Date researchDate) {
        this.researchDate = researchDate;
    }

    public String getUnitAndLeader() {
        return unitAndLeader;
    }

    public void setUnitAndLeader(String unitAndLeader) {
        this.unitAndLeader = unitAndLeader;
    }

    public String getDirectorOption() {
        return directorOption;
    }

    public void setDirectorOption(String directorOption) {
        this.directorOption = directorOption;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public Date getDirectorDate() {
        return directorDate;
    }

    public void setDirectorDate(Date directorDate) {
        this.directorDate = directorDate;
    }

    public String getLeaderOption() {
        return leaderOption;
    }

    public void setLeaderOption(String leaderOption) {
        this.leaderOption = leaderOption;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public Date getLeaderDate() {
        return leaderDate;
    }

    public void setLeaderDate(Date leaderDate) {
        this.leaderDate = leaderDate;
    }

    public TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        this.topicInfo = topicInfo;
    }

    public String getStudyQuantum() {
        return studyQuantum;
    }

    public void setStudyQuantum(String studyQuantum) {
        this.studyQuantum = studyQuantum;
    }

    public String getMleaderOption() {
        return mleaderOption;
    }

    public void setMleaderOption(String mleaderOption) {
        this.mleaderOption = mleaderOption;
    }

    public String getMleaderName() {
        return mleaderName;
    }

    public void setMleaderName(String mleaderName) {
        this.mleaderName = mleaderName;
    }

    public Date getMleaderDate() {
        return mleaderDate;
    }

    public void setMleaderDate(Date mleaderDate) {
        this.mleaderDate = mleaderDate;
    }
}
