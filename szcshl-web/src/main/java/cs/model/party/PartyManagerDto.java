package cs.model.party;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Description: 党员管理
 * Author: mcl
 * Date: 2018/3/8 10:28
 */
public class PartyManagerDto extends DomainBase{
    /**
     * 党员ID
     */
    private String pmId;

    /**
     * 党员名称
     */
    private String pmName;

    /**
     * 党员性别
     */
    private String pmSex;

    /**
     * 民族
     */
    private String pmNation;

    /**
     * 身份证号
     */
    private String pmIDCard;

    /**
     * 出生日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date pmBirthday;

    /**
     * 学历
     */
    private String pmEducation;

    /**
     * 人员类别   1 = 正式党员 ， 2 = 预备党员
     */
    private String pmCategory;

    /**
     * 所在党支部
     */
    private String pmPartyBranch;

    /**
     * 加入党组织日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date pmJoinPartyDate;

    /**
     * 转为正式党员日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date pmTurnToPatryDate;

    /**
     * 工作岗位
     */
    private String pmWorkPost;

    /**
     * 手机号
     */
    private String pmPhone;

    /**
     * 固定电话
     */
    private String pmTel;

    /**
     * 家庭住址
     */
    private String pmAddress;

    /**
     * 党籍状态  1 = 正常 ， 2 = 停止
     */
    private String pmPartyState;

    /**
     * 是否为失联党员  0 = 否 ， 9 = 是
     */
    private String isLossParty;

    /**
     * 失联日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date lossDate;

    /**
     * 是否为流动党员
     */
    private String isMobileParty;

    /**
     * 外出流向
     */
    private String goOutTo;

    /**
     * 信息采集员名称
     */
    private String infoCollectorName;

    /**
     * 信息采集日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date infoCollectorDate;

    /**
     * 党支部书记名称
     */
    private String secretaryName;

    /**
     * 党支部书记签名日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date secretaryDate;

    /**
     * 基层党委名称
     */
    private String partyCommitteeName;

    /**
     * 基层党委签名日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date partyCommitteeDate;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 是否农民工  0 = 否 ， 9 = 是
     */
    private String isPeasantWorker;

    /**
     * 专业技术职务
     */
    private String technicalPosition;

    /**
     * 行政职务
     */
    private String administrativeDuty;

    /**
     * 行政级别
     */
    private String administrativeRank;

    /**
     * 党代表类型  1 = 省（区、市）代表 ， 2 = 市（州）代表 ， 3 = 县（市、区）代表 ， 4 = 乡（镇）代表
     */
    private String partyDelegateType;

    /**
     * 出国情况
     */
    private String goAbroadCondition;

    /**
     * 出国原因
     */
    private String goAbroadCause;

    /**
     * 出国日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date goAbroadDate;

    /**
     * 失联情形
     */
    private String lossCondition;

    /**
     * 流出党员流向单位类别
     */
    private String outflowToUnitType;

    /**
     * 入党介绍人
     */
    private String introducer;

    /**
     * 组织关系是否在我委
     */
    private String isInOrg;

    /**
     * 转入党组织日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date joinOrgDate;

    /**
     * 参加工作时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date joinWorkDate;

    /**
     * 是否在编
     */
    private String isEnrolled;

    public String getPmId() {
        return pmId;
    }

    public void setPmId(String pmId) {
        this.pmId = pmId;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getPmSex() {
        return pmSex;
    }

    public void setPmSex(String pmSex) {
        this.pmSex = pmSex;
    }

    public String getPmNation() {
        return pmNation;
    }

    public void setPmNation(String pmNation) {
        this.pmNation = pmNation;
    }

    public String getPmIDCard() {
        return pmIDCard;
    }

    public void setPmIDCard(String pmIDCard) {
        this.pmIDCard = pmIDCard;
    }

    public Date getPmBirthday() {
        return pmBirthday;
    }

    public void setPmBirthday(Date pmBirthday) {
        this.pmBirthday = pmBirthday;
    }

    public String getPmEducation() {
        return pmEducation;
    }

    public void setPmEducation(String pmEducation) {
        this.pmEducation = pmEducation;
    }

    public String getPmCategory() {
        return pmCategory;
    }

    public void setPmCategory(String pmCategory) {
        this.pmCategory = pmCategory;
    }

    public String getPmPartyBranch() {
        return pmPartyBranch;
    }

    public void setPmPartyBranch(String pmPartyBranch) {
        this.pmPartyBranch = pmPartyBranch;
    }

    public Date getPmJoinPartyDate() {
        return pmJoinPartyDate;
    }

    public void setPmJoinPartyDate(Date pmJoinPartyDate) {
        this.pmJoinPartyDate = pmJoinPartyDate;
    }

    public Date getPmTurnToPatryDate() {
        return pmTurnToPatryDate;
    }

    public void setPmTurnToPatryDate(Date pmTurnToPatryDate) {
        this.pmTurnToPatryDate = pmTurnToPatryDate;
    }

    public String getPmWorkPost() {
        return pmWorkPost;
    }

    public void setPmWorkPost(String pmWorkPost) {
        this.pmWorkPost = pmWorkPost;
    }

    public String getPmPhone() {
        return pmPhone;
    }

    public void setPmPhone(String pmPhone) {
        this.pmPhone = pmPhone;
    }

    public String getPmTel() {
        return pmTel;
    }

    public void setPmTel(String pmTel) {
        this.pmTel = pmTel;
    }

    public String getPmAddress() {
        return pmAddress;
    }

    public void setPmAddress(String pmAddress) {
        this.pmAddress = pmAddress;
    }

    public String getPmPartyState() {
        return pmPartyState;
    }

    public void setPmPartyState(String pmPartyState) {
        this.pmPartyState = pmPartyState;
    }

    public String getIsLossParty() {
        return isLossParty;
    }

    public void setIsLossParty(String isLossParty) {
        this.isLossParty = isLossParty;
    }

    public Date getLossDate() {
        return lossDate;
    }

    public void setLossDate(Date lossDate) {
        this.lossDate = lossDate;
    }

    public String getIsMobileParty() {
        return isMobileParty;
    }

    public void setIsMobileParty(String isMobileParty) {
        this.isMobileParty = isMobileParty;
    }

    public String getGoOutTo() {
        return goOutTo;
    }

    public void setGoOutTo(String goOutTo) {
        this.goOutTo = goOutTo;
    }

    public String getInfoCollectorName() {
        return infoCollectorName;
    }

    public void setInfoCollectorName(String infoCollectorName) {
        this.infoCollectorName = infoCollectorName;
    }

    public Date getInfoCollectorDate() {
        return infoCollectorDate;
    }

    public void setInfoCollectorDate(Date infoCollectorDate) {
        this.infoCollectorDate = infoCollectorDate;
    }

    public String getSecretaryName() {
        return secretaryName;
    }

    public void setSecretaryName(String secretaryName) {
        this.secretaryName = secretaryName;
    }

    public Date getSecretaryDate() {
        return secretaryDate;
    }

    public void setSecretaryDate(Date secretaryDate) {
        this.secretaryDate = secretaryDate;
    }

    public String getPartyCommitteeName() {
        return partyCommitteeName;
    }

    public void setPartyCommitteeName(String partyCommitteeName) {
        this.partyCommitteeName = partyCommitteeName;
    }

    public Date getPartyCommitteeDate() {
        return partyCommitteeDate;
    }

    public void setPartyCommitteeDate(Date partyCommitteeDate) {
        this.partyCommitteeDate = partyCommitteeDate;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getIsPeasantWorker() {
        return isPeasantWorker;
    }

    public void setIsPeasantWorker(String isPeasantWorker) {
        this.isPeasantWorker = isPeasantWorker;
    }

    public String getTechnicalPosition() {
        return technicalPosition;
    }

    public void setTechnicalPosition(String technicalPosition) {
        this.technicalPosition = technicalPosition;
    }

    public String getAdministrativeRank() {
        return administrativeRank;
    }

    public void setAdministrativeRank(String administrativeDuty) {
        this.administrativeDuty = administrativeDuty;
    }

    public String getAdministrativeDuty() {
        return administrativeDuty;
    }

    public void setAdministrativeDuty(String administrativeDuty) {
        this.administrativeDuty = administrativeDuty;
    }
    public String getPartyDelegateType() {
        return partyDelegateType;
    }

    public void setPartyDelegateType(String partyDelegateType) {
        this.partyDelegateType = partyDelegateType;
    }

    public String getGoAbroadCondition() {
        return goAbroadCondition;
    }

    public void setGoAbroadCondition(String goAbroadCondition) {
        this.goAbroadCondition = goAbroadCondition;
    }

    public String getGoAbroadCause() {
        return goAbroadCause;
    }

    public void setGoAbroadCause(String goAbroadCause) {
        this.goAbroadCause = goAbroadCause;
    }

    public Date getGoAbroadDate() {
        return goAbroadDate;
    }

    public void setGoAbroadDate(Date goAbroadDate) {
        this.goAbroadDate = goAbroadDate;
    }

    public String getLossCondition() {
        return lossCondition;
    }

    public void setLossCondition(String lossCondition) {
        this.lossCondition = lossCondition;
    }

    public String getOutflowToUnitType() {
        return outflowToUnitType;
    }

    public void setOutflowToUnitType(String outflowToUnitType) {
        this.outflowToUnitType = outflowToUnitType;
    }

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }

    public String getIsInOrg() {
        return isInOrg;
    }

    public void setIsInOrg(String isInOrg) {
        this.isInOrg = isInOrg;
    }

    public Date getJoinOrgDate() {
        return joinOrgDate;
    }

    public void setJoinOrgDate(Date joinOrgDate) {
        this.joinOrgDate = joinOrgDate;
    }

    public Date getJoinWorkDate() {
        return joinWorkDate;
    }

    public void setJoinWorkDate(Date joinWorkDate) {
        this.joinWorkDate = joinWorkDate;
    }

    public String getIsEnrolled() {
        return isEnrolled;
    }

    public void setIsEnrolled(String isEnrolled) {
        this.isEnrolled = isEnrolled;
    }
}