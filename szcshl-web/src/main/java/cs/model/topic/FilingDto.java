package cs.model.topic;

import cs.model.BaseDto;

import java.util.Date;
    


/**
 * Description: 课题归档 页面数据模型
 * author: ldm
 * Date: 2017-9-4 15:40:48
 */
public class FilingDto extends BaseDto {

    private String id;
    private String topicName;
    private String cooperator;
    private Integer filingSeq;
    private String filingCode;
    private String fileNo;
    private Integer planCount;
    private String planOrig;
    private String planCopy;
    private Integer contract;
    private String contractOrig;
    private String contractCopy;
    private Integer eleDocCount;
    private String eleDocOrig;
    private String eleDocCopy;
    private Integer otherCount;
    private String otherOrig;
    private String otherCopy;
    private Integer wplanCount;
    private String wplanOrig;
    private String wplanCopy;
    private Integer studyCount;
    private String studyOrig;
    private String studyCopy;
    private Integer studyIdeaCount;
    private String studyIdeaOrig;
    private String studyIdeaCopy;
    private Integer studyEleCount;
    private String studyEleOrig;
    private String studyEleCopy;
    private Integer signinCount;
    private String signinOrig;
    private String signinCopy;
    private Integer reportCount;
    private String reportOrig;
    private String reportCopy;
    private Integer sOtherCount;
    private String sOtherOrig;
    private String sOtherCopy;
    private Integer epCostCount;
    private String epCostOrig;
    private String epCostCopy;
    private String principal;
    private String director;
    private String filingUser;
    private Date filingDate;
    private TopicInfoDto topicInfoDto;

    public FilingDto() {
    }
   
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
    public Integer getSOtherCount() {
        return sOtherCount;
    }

    public void setSOtherCount(Integer sOtherCount) {
        this.sOtherCount = sOtherCount;
    }
    public String getSOtherOrig() {
        return sOtherOrig;
    }

    public void setSOtherOrig(String sOtherOrig) {
        this.sOtherOrig = sOtherOrig;
    }
    public String getSOtherCopy() {
        return sOtherCopy;
    }

    public void setSOtherCopy(String sOtherCopy) {
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

    public void setFilingDate(Date filingDate) {
        this.filingDate = filingDate;
    }

    public TopicInfoDto getTopicInfoDto() {
        return topicInfoDto;
    }

    public void setTopicInfoDto(TopicInfoDto topicInfoDto) {
        this.topicInfoDto = topicInfoDto;
    }
}