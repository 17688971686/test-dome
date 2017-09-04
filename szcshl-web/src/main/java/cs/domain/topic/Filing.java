package cs.domain.topic;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 课题研究归档信息
 * Created by ldm on 2017/9/4 0004.
 */
@Entity
@Table(name="cs_topic_filing")
@DynamicUpdate(true)
public class Filing extends DomainBase {

    @Id
    @GeneratedValue(generator= "filingGenerator")
    @GenericGenerator(name= "filingGenerator",strategy = "uuid")
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
     * 序号
     */
    @Column(columnDefinition="INTEGER")
    private Integer filingSeq;

    /**
     * 课题代码
     */
    @Column(columnDefinition="VARCHAR(16)")
    private String filingCode;

    /**
     * 档案编号
     */
    @Column(columnDefinition="VARCHAR(16)")
    private String fileNo;

    //S课题前期资料
    /**
     * 课题计划书
     */
    private Integer planCount;

    private String planOrig;

    private String planCopy;

    /**
     * 课题研究合同
     */
    private Integer contract;

    private String contractOrig;

    private String contractCopy;

    /**
     * 电子文档
     */
    private Integer eleDocCount;

    private String eleDocOrig;

    private String eleDocCopy;

    /**
     * 其他
     */
    private Integer otherCount;

    private String otherOrig;

    private String otherCopy;
    //E课题前期资料


    //S课题结题资料
    /**
     * 工作方案
     */
    private Integer wplanCount;

    private String wplanOrig;

    private String wplanCopy;

    /**
     * 研究成果
     */
    private Integer studyCount;

    private String studyOrig;

    private String studyCopy;

    /**
     * 研究成果意见
     */
    private Integer studyIdeaCount;

    private String studyIdeaOrig;

    private String studyIdeaCopy;

    /**
     * 研究成果电子文档
     */
    private Integer studyEleCount;

    private String studyEleOrig;

    private String studyEleCopy;

    /**
     * 会议签到表
     */
    private Integer signinCount;

    private String signinOrig;

    private String signinCopy;

    /**
     * 课题完成报告
     */
    private Integer reportCount;

    private String reportOrig;

    private String reportCopy;

    /**
     * 其他
     */
    private Integer sOtherCount;

    private String sOtherOrig;

    private String sOtherCopy;

    /**
     * 专家发放表
     */
    private Integer epCostCount;

    private String epCostOrig;

    private String epCostCopy;

    /**
     * 项目负责人
     */
    private String principal;

    private String director;

    private String filingUser;

    /**
     * 归档日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date filingDate;


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

    public Integer getFilingSeq() {
        return filingSeq;
    }

    public void setFilingSeq(Integer filingSeq) {
        this.filingSeq = filingSeq;
    }

    public String getFilingCode() {
        return filingCode;
    }

    public void setFilingCode(String filingCode) {
        this.filingCode = filingCode;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Integer getPlanCount() {
        return planCount;
    }

    public void setPlanCount(Integer planCount) {
        this.planCount = planCount;
    }

    public String getPlanOrig() {
        return planOrig;
    }

    public void setPlanOrig(String planOrig) {
        this.planOrig = planOrig;
    }

    public String getPlanCopy() {
        return planCopy;
    }

    public void setPlanCopy(String planCopy) {
        this.planCopy = planCopy;
    }

    public Integer getContract() {
        return contract;
    }

    public void setContract(Integer contract) {
        this.contract = contract;
    }

    public String getContractOrig() {
        return contractOrig;
    }

    public void setContractOrig(String contractOrig) {
        this.contractOrig = contractOrig;
    }

    public String getContractCopy() {
        return contractCopy;
    }

    public void setContractCopy(String contractCopy) {
        this.contractCopy = contractCopy;
    }

    public Integer getEleDocCount() {
        return eleDocCount;
    }

    public void setEleDocCount(Integer eleDocCount) {
        this.eleDocCount = eleDocCount;
    }

    public String getEleDocOrig() {
        return eleDocOrig;
    }

    public void setEleDocOrig(String eleDocOrig) {
        this.eleDocOrig = eleDocOrig;
    }

    public String getEleDocCopy() {
        return eleDocCopy;
    }

    public void setEleDocCopy(String eleDocCopy) {
        this.eleDocCopy = eleDocCopy;
    }

    public Integer getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(Integer otherCount) {
        this.otherCount = otherCount;
    }

    public String getOtherOrig() {
        return otherOrig;
    }

    public void setOtherOrig(String otherOrig) {
        this.otherOrig = otherOrig;
    }

    public String getOtherCopy() {
        return otherCopy;
    }

    public void setOtherCopy(String otherCopy) {
        this.otherCopy = otherCopy;
    }

    public Integer getWplanCount() {
        return wplanCount;
    }

    public void setWplanCount(Integer wplanCount) {
        this.wplanCount = wplanCount;
    }

    public String getWplanOrig() {
        return wplanOrig;
    }

    public void setWplanOrig(String wplanOrig) {
        this.wplanOrig = wplanOrig;
    }

    public String getWplanCopy() {
        return wplanCopy;
    }

    public void setWplanCopy(String wplanCopy) {
        this.wplanCopy = wplanCopy;
    }

    public Integer getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(Integer studyCount) {
        this.studyCount = studyCount;
    }

    public String getStudyOrig() {
        return studyOrig;
    }

    public void setStudyOrig(String studyOrig) {
        this.studyOrig = studyOrig;
    }

    public String getStudyCopy() {
        return studyCopy;
    }

    public void setStudyCopy(String studyCopy) {
        this.studyCopy = studyCopy;
    }

    public Integer getStudyIdeaCount() {
        return studyIdeaCount;
    }

    public void setStudyIdeaCount(Integer studyIdeaCount) {
        this.studyIdeaCount = studyIdeaCount;
    }

    public String getStudyIdeaOrig() {
        return studyIdeaOrig;
    }

    public void setStudyIdeaOrig(String studyIdeaOrig) {
        this.studyIdeaOrig = studyIdeaOrig;
    }

    public String getStudyIdeaCopy() {
        return studyIdeaCopy;
    }

    public void setStudyIdeaCopy(String studyIdeaCopy) {
        this.studyIdeaCopy = studyIdeaCopy;
    }

    public Integer getStudyEleCount() {
        return studyEleCount;
    }

    public void setStudyEleCount(Integer studyEleCount) {
        this.studyEleCount = studyEleCount;
    }

    public String getStudyEleOrig() {
        return studyEleOrig;
    }

    public void setStudyEleOrig(String studyEleOrig) {
        this.studyEleOrig = studyEleOrig;
    }

    public String getStudyEleCopy() {
        return studyEleCopy;
    }

    public void setStudyEleCopy(String studyEleCopy) {
        this.studyEleCopy = studyEleCopy;
    }

    public Integer getSigninCount() {
        return signinCount;
    }

    public void setSigninCount(Integer signinCount) {
        this.signinCount = signinCount;
    }

    public String getSigninOrig() {
        return signinOrig;
    }

    public void setSigninOrig(String signinOrig) {
        this.signinOrig = signinOrig;
    }

    public String getSigninCopy() {
        return signinCopy;
    }

    public void setSigninCopy(String signinCopy) {
        this.signinCopy = signinCopy;
    }

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

    public String getReportOrig() {
        return reportOrig;
    }

    public void setReportOrig(String reportOrig) {
        this.reportOrig = reportOrig;
    }

    public String getReportCopy() {
        return reportCopy;
    }

    public void setReportCopy(String reportCopy) {
        this.reportCopy = reportCopy;
    }

    public Integer getsOtherCount() {
        return sOtherCount;
    }

    public void setsOtherCount(Integer sOtherCount) {
        this.sOtherCount = sOtherCount;
    }

    public String getsOtherOrig() {
        return sOtherOrig;
    }

    public void setsOtherOrig(String sOtherOrig) {
        this.sOtherOrig = sOtherOrig;
    }

    public String getsOtherCopy() {
        return sOtherCopy;
    }

    public void setsOtherCopy(String sOtherCopy) {
        this.sOtherCopy = sOtherCopy;
    }

    public Integer getEpCostCount() {
        return epCostCount;
    }

    public void setEpCostCount(Integer epCostCount) {
        this.epCostCount = epCostCount;
    }

    public String getEpCostOrig() {
        return epCostOrig;
    }

    public void setEpCostOrig(String epCostOrig) {
        this.epCostOrig = epCostOrig;
    }

    public String getEpCostCopy() {
        return epCostCopy;
    }

    public void setEpCostCopy(String epCostCopy) {
        this.epCostCopy = epCostCopy;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getFilingUser() {
        return filingUser;
    }

    public void setFilingUser(String filingUser) {
        this.filingUser = filingUser;
    }

    public Date getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(Date filingDate) {
        this.filingDate = filingDate;
    }

    public TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        this.topicInfo = topicInfo;
    }
}
