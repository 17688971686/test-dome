package cs.model.project;


import com.alibaba.fastjson.annotation.JSONField;
import cs.common.utils.Validate;
import cs.model.BaseDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.sys.SysFileDto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SignDto extends BaseDto {


    private String signid;

    /**
     * 委里收文编号
     */
    private String filecode;

    /**
     * 旧项目ID
     */
    private Integer oldProjectId;

    private String comprehensiveName;//综合部拟办人名称

    /**
     * 警示灯状态
     */
    private String isLightUp;
    private String isSendFileRecord;

    @JSONField(format = "yyyy-MM-dd")
    private Date comprehensiveDate;//综合部拟办日期

    private String leaderName;//中心领导名称

    @JSONField(format = "yyyy-MM-dd")
    private Date leaderDate;//中心领导审批日期

    private String ministerName;//部长名称

    @JSONField(format = "yyyy-MM-dd")
    private Date ministerDate;//部长处理日期

    @JSONField(format = "yyyy-MM-dd")
    private Date presignDate;  //预签收时间

    /**
     * 收文编号(年份+收文类型+序号[序号保留3位数])
     */
    private String signNum;

    /**
     * 收文序号
     */
    private Integer signSeq;

    private String projectcode;

    /**
     * 报审概算，总投资金额
     */
    private BigDecimal declaration;

    /**
     * 评审阶段
     */
    private String reviewstage;

    private String ispresign;

    //是否有登记补充资料:9表示是,0表示否
    private String isSupplementary;

    //是否有拟补充资料函:9表示是,0表示否
    private String isHaveSuppLetter;

    //拟补充资料函发文日期
    @JSONField(format = "yyyy-MM-dd")
    private Date suppLetterDate;
    //审定投资
    private BigDecimal authorizeValue;

    private String projectname;

    private String ischangeEstimate;

    private String maindepetid;

    private String maindeptName;

    private String mainDeptUserName;

    private String maindepetcontactuserid;

    private String mainDeptContactPhone;

    private String assistdeptid;

    private String assistdeptName;

    private String assistDeptUserName;

    private String assistdeptcontactuserid;

    private String designcompanyid;

    private String designcompanyName;

    private String builtcompanyid;

    private String builtcompanyName;

    private String urgencydegree;

    private String yearplantype;

    private String secrectlevel;

    private String sendusersign;

    private String issign;

    private String isassistflow;

    private String ishasreviewcost;

    @JSONField(format = "yyyy-MM-dd")
    private Date signdate;

    private Float surplusdays;

    @JSONField(format = "yyyy-MM-dd")
    private Date expectdispatchdate;

    @JSONField(format = "yyyy-MM-dd")
    private Date receivedate;

    private Float daysafterdispatch;

    private Float reviewdays;

    //总评审天数
    private Float totalReviewdays;

    private String isassistproc;

    private String filenum;

    private String docnum;

    //建议书项目处理表份数
    private Integer sugProDealCount;
    //建议书项目处理表是否有原件
    private String sugProDealOriginal;
    //建议书项目处理表是否有复印件
    private String sugProDealCopy;

    //建议书文件处理表份数
    private Integer sugFileDealCount;

    //建议书文件处理表是否有原件
    private String sugFileDealOriginal;

    //建议书文件处理表是否有复印件
    private String sugFileDealCopy;


    //建议书项目单位申报表份数
    private Integer sugOrgApplyCount;

    //建议书项目单位申报表是否有原件
    private String sugOrgApplyOriginal;

    //建议书项目单位申报表是否有复印件
    private String sugOrgApplyCopy;


    //建议书项目单位请示报告份数
    private Integer sugOrgReqCount;
    //建议书项目单位请示报告是否有原件
    private String sugOrgReqOriginal;

    //建议书项目单位请示报告是否有复印件
    private String sugOrgReqCopy;

    //资金申请报告份数
    private Integer capitalAppReportCount;

    //资金申请报告是否有原件
    private String capitalAppReportOriginal;

    //项目建议书份数
    private Integer sugProAdviseCount;

    //项目建议书是否有原件
    private String sugProAdviseOriginal;

    //项目建议书是否有复印件
    private String sugProAdviseCopy;

    //项目建议书电子文档份数
    private Integer proSugEledocCount;

    //项目建议书电子文档是否有原件
    private String proSugEledocOriginal;

    //项目建议书电子文档是否有复印件
    private String proSugEledocCopy;

    //申报投资
    private BigDecimal appalyInvestment;

    //建议书相关会议纪要份数
    private Integer sugMeetCount;

    //建议书相关会议纪要是否有原件
    private String sugMeetOriginal;

    //建议书相关会议纪要是否有复印件
    private String sugMeetCopy;

    //第二阶段：项目可研究性阶段

    //1.可研项目处理表份数
    private Integer studyProDealCount;

    //可研项目处理表是否有原件
    private String studyPealOriginal;

    //可研项目处理表是否有复印件
    private String studyProDealCopy;


    //2.可研文件处理表份数
    private Integer studyFileDealCount;

    //可研文件处理表是否有原件
    private String studyFileDealOriginal;

    //可研文件处理表是否有复印件
    private String studyFileDealCopy;

    //3.可研项目单位申报表份数
    private Integer studyOrgApplyCount;

    //可研项目单位申报表是否有原件
    private String studyOrgApplyOriginal;

    //可研项目单位申报表是否有复印件
    private String studyOrgApplyCopy;

    //4.可研项目单位请示报告份数
    private Integer studyOrgReqCount;

    //可研项目单位请示报告是否有原件
    private String studyOrgReqOriginal;

    //可研项目单位请示报告是否有复印件
    private String studyOrgReqCopy;

    //5.可研项目建议书批复
    private Integer studyProSugCount;

    //可研项目建议书批复是否有原件
    private String studyProSugOriginal;

    //可研项目建议书批复是否有复印件
    private String studyProSugCopy;

    //6.可研相关会议纪要份数
    private Integer studyMeetCount;

    //可研相关会议是否有原件
    private String studyMeetOriginal;

    //可研相关会议是否有复印件
    private String studyMeetCopy;

    //第二阶段 、右边
    //1.可研环保批复文件份数
    private Integer envproReplyCount;

    //可研环保批复文件是否有原件
    private String envproReplyOriginal;

    //可研环保批复文件是否有复印件
    private String envproReplyCopy;

    //2.可研规划选址批文份数
    private Integer planAddrCount;

    //可研规划选址批文是否有原件
    private String planAddrOriginal;

    //可研规划选址批文是否有复印件
    private String planAddrCopy;

    //3.可研报告份数
    private Integer reportCount;

    //可研报告是否有原件
    private String reportOrigin;

    //可研报告是否有复印件
    private String reportCopy;

    //4可研报告电子文档份数
    private Integer eledocCount;

    //可研报告电子文档是否有原件
    private String eledocOriginal;

    //可研报告电子文档是否有复印件
    private String eledocCopy;

    //5.可研节能报告份数
    private Integer energyCount;

    //可研节能报告是否有原件
    private String energyOriginal;

    //可研节能报告是否有复印件
    private String energyCopy;

    //S (进口设备) 政府采购进口产品申报份数
    private Integer governmentPurchasCount;

    //政府采购进口产品申报是否有原件
    private String governmentPurchasOriginal;

    // 政府采购进口产品申报是否有复印件
    private String governmentPurchasCopy;

    // 专家论证意见份数
    private Integer expertArgumentCount;

    //专家论证意见是否有原件
    private String expertArgumentOriginal;

    //专家论证意见是否有复印件
    private String expertArgumentCopy;

    //发改委意见份数
    private Integer developmentCount;

    //发改委意见是否有原件
    private String developementOriginal;

    //发改委意见是否复印件
    private String developementCopy;

    //文件处理表份数
    private Integer officialDisposeCount;

    //文件处理表是否有原件
    private String officialDisposeOriginal;

    //文件处理表是否复印件
    private String officialDisposeCopy;

    //主管部门意见分数
    private Integer managerDeptCount;

    //主管部门意见是否有原件
    private String managerDeptOriginal;

    //主管部门意见是否复印件
    private String managerDeptCopy;

    //进口产品目录分数
    private Integer importProductCount;

    //进口产品目录是否有原件
    private String importProductOriginal;

    //进口产品目录是否复印件
    private String importProductCopy;

    //其他项目资料分数
    private Integer ortherProjectCount;

    //其他项目资料是否有原件
    private String ortherProjectOriginal;

    //其他项目资料是否复印件
    private String ortherProjectCopy;

    //设备说明书分数
    private Integer sprcialDevicesCount;

    //设备说明书是否有原件
    private String sprcialDevicesOriginal;

    //设备说明书是否有复印件
    private String sprcialDevicesCopy;

    //E (进口设备)

    //S (设备清单(国产))

    //项目申报表是否有分数
    private Integer projectDeclareCount;

    //项目申报表是否有原件
    private String projectDeclareOriginal;

    //项目申报表是否有复印件
    private String projectDeclareCopy;

    //项目申请报告分数
    private Integer projectApplyReportCount;

    //项目申请报告是否有原件
    private String projectApplyReportOriginal;

    //项目申请报告是否有复印件
    private String projectApplyReportCopy;

    //采购设备清单分数
    private Integer purchasDeviceCount;

    //采购设备清单是否有原件
    private String purchasDeviceOriginal;

    //采购设备清单是否有复印件
    private String purchasDeviceCopy;

    //电子文档分数
    private Integer electronicDocumentCount;

    //电子文档是否有原件
    private String electronicDocumentOriginal;

    //电子文档是否有复印件
    private String electronicDocumentCopy;

    //采购合同书分数
    private Integer purchasDevicePactCount;

    //采购合同书是否有原件
    private String purchasDevicePactOriginal;

    //采购合同书是否有复印件
    private String purchasDevicePactCopy;

    //项目核准文件份数
    private Integer projectApproveFileCount;

    //项目核准文件是否有原件
    private String projectApproveFileOriginal;

    //项目核准文件是否有复印件
    private String projectApproveFileCopy;

    //营业执照分数
    private Integer businessLicenseCount;

    //营业执照是否有原件
    private String businessLicenseOriginal;

    //营业执照是否有复印件
    private String businessLicenseCopy;

    //法人身份证分数
    private Integer legalPersoncCardCount;

    //法人身份证是否有原件
    private String legalPersoncCardOriginal;

    //法人身份证复印件
    private String legalPersoncCardCopy;

    //E (设备清单(国产))

    //S (项目概算)

    //环境影响评价报告分数
    private Integer environmentalEffectCount;

    //环境影响评价报告是否有原件
    private String environmentalEffectOriginal;

    //环境影响评价报告是否有复印件
    private String environmentalEffectCopy;

    //用地规划许可证分数
    private Integer landPlanningLicenseCount;

    //用地规划许可证是否有原件
    private String landPlanningLicenseOriginal;

    //用地规划许可证是否有复印件
    private String landPlanningLicenseCopy;

    //地质勘察报告分数
    private Integer geologicalSurveyCount;

    //地质勘察报告是否有原件
    private String geologicalSurveyOriginal;

    //地质勘察报告是否有复印件
    private String geologicalSurveyCopy;

    //设计说明分数
    private Integer descriptionDesignCount;

    //设计说明是否有原件
    private String descriptionDesignOriginal;

    //设计说明是否有复印件
    private String descriptionDesignCopy;

    //工程概算书分数
    private Integer projectBudgetCount;

    //工程概算书是否有原件
    private String projectBudgetOriginal;

    //工程概算书是否有复印件
    private String projectBudgetCopy;

    //初步设计图纸分数
    private Integer preliminaryDrawingCount;

    //初步设计图纸是否有原件
    private String preliminaryDrawingOriginal;

    //初步设计图纸是否有复印件
    private String preliminaryDrawingCopy;

    //施工设计图纸分数
    private Integer constructionDrawingsCount;

    //施工设计图纸是否有原件
    private String constructionDrawingsOriginal;

    //施工设计图纸是否有复印件
    private String constructionDrawingsCopy;
    //E (项目概算)

    //综合部拟办意见
    private String comprehensivehandlesug;

    //综合部部长ID
    private String comprehensiveId;

    //中心领导审批意见
    private String leaderhandlesug;

    //中心领导ID
    private String leaderId;

    //部长处理意见
    private String ministerhandlesug;

    //部长ID
    private String ministerId;

    //收文状态
    private String signState;

    //项目流程状态信息
    private Integer processState;

    //流程实例ID
    private String processInstanceId;

    //第二负责人
    private String secondPriUser;

    //开始时间
    @JSONField(format = "yyyy-MM-dd")
    private Date startTime;

    //结束时间
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime;

    //项目是否已关联,0未关联，1已关联，默认未关联
    private Integer isAssociate = 0;

    //是否提前介入
    private String isAdvanced;

    //关联阶段的项目
    private SignDto associateSign;

    /**
     * 项目是否曾经暂停（9：是暂停过，0：否）
     */
    private String isProjectState;
    /**
     * 默认办理类型,项目类型（评估类：PX，概算类:GX）
     */
    private String dealOrgType;

    //工作方案
    private List<WorkProgramDto> workProgramDtoList;

    //发文
    private DispatchDocDto dispatchDocDto;

    //归档
    private FileRecordDto fileRecordDto;

    //协审计划信息
    private AssistPlanDto assistPlanDto;

    //协审项目信息
    private List<AssistPlanSignDto> planSignDtoList;

    /**
     * 是否已经回传给发改委（9：是，其它：否）
     */
    private String isSendFGW;

    /**
     * 拟补充资料函列表
     */
    private List<AddSuppLetterDto> suppLetterDtoList;
    /**
     * 拟补充资料列表
     */
    private List<AddRegisterFileDto> registerFileDtoDtoList;

    /**
     * 关联项目列表
     */
    private List<SignDto> associateSignDtoList;
    /**
     * 专家评审方案信息
     */
    private ExpertReviewDto expertReviewDto;

    /**
     * 资料存放位置
     */
    private String palceStorage;

    /**
     * 送来存放时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date palceTime;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactsPhone;

    /**
     * 附件列表（目前用于接收附件）
     */
    private List<SysFileDto> sysFileDtoList;


    /**
     * 已逝工作日
     */
    private Float goneDays;

    /**
     * 延长工作日（是指超过评审天数的天数）
     */
    private Float overDays;

    /**
     * 延长天数（系统管理员添加）
     */
    private Float lengthenDays;

    /**
     * 延长评审说明
     */
    private String lengthenExp;

    /**
     * 主办部门ID
     */
    private String mOrgId;

    /**
     * 主办部门名称
     */
    private String mOrgName;

    /**
     * 协办部门ID
     */
    private String aOrgId;

    /**
     * 协办部门名称
     */
    private String aOrgName;

    /**
     * 主负责人ID
     */
    private String mUserId;

    /**
     * 主负责人名称
     */
    private String mUserName;

    /**
     * 负责人ID
     */
    private String aUserID;
    /**
     * 负责人名称
     */
    private String aUserName;
    /**
     * 单位评分
     */
    private UnitScoreDto unitScoreDto;

    /**
     * 当前日期
     */
    private String curDate;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 总分支数
     */
    private Integer branchCount;

    //以下几个字段主要是生成委内处理表
    /**
     * 建设单位负责人
     */
    private String builtCompUserName;

    /**
     * 窗口受理时间
     */
    private Date acceptDate;

    /**
     * 主办室处意见
     */
    private String maindeptOpinion;

    /**
     * 发文日期
     */
    private Date dispatchdate;

    /**
     * 是否能自选多个专家
     */
    private String isMoreExpert;

    /**
     * 项目基本信息
     */
    private ProjBaseInfoDto projBaseInfoDto;

    /**
     * 项目暂停信息
     */
    private List<ProjectStopDto> projectStopDtos;

    public String getIsMoreExpert() {
        return isMoreExpert;
    }

    public void setIsMoreExpert(String isMoreExpert) {
        this.isMoreExpert = isMoreExpert;
    }

    public String getBuiltCompUserName() {
        return builtCompUserName;
    }

    public void setBuiltCompUserName(String builtCompUserName) {
        this.builtCompUserName = builtCompUserName;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getMaindeptOpinion() {
        return maindeptOpinion;
    }

    public void setMaindeptOpinion(String maindeptOpinion) {
        this.maindeptOpinion = maindeptOpinion;
    }

    public SignDto() {
    }

    public Float getGoneDays() {
        return goneDays;
    }

    public void setGoneDays(Float goneDays) {
        this.goneDays = goneDays;
    }

    public Float getOverDays() {
        return overDays;
    }

    public void setOverDays(Float overDays) {
        this.overDays = overDays;
    }

    public Float getLengthenDays() {
        return lengthenDays;
    }

    public void setLengthenDays(Float lengthenDays) {
        this.lengthenDays = lengthenDays;
    }

    public String getLengthenExp() {
        return lengthenExp;
    }

    public void setLengthenExp(String lengthenExp) {
        if(Validate.isString(lengthenExp)){
            this.lengthenExp = lengthenExp;
        }
    }

    /*********************************** 以下是set get 方法 *****************************************/



    public String getComprehensiveName() {
        return comprehensiveName;
    }

    public void setComprehensiveName(String comprehensiveName) {
        this.comprehensiveName = comprehensiveName;
    }

    public String getIsLightUp() {
        return isLightUp;
    }

    public void setIsLightUp(String isLightUp) {
        this.isLightUp = isLightUp;
    }

    public String getIsSendFileRecord() {
        return isSendFileRecord;
    }

    public void setIsSendFileRecord(String isSendFileRecord) {
        this.isSendFileRecord = isSendFileRecord;
    }

    public Date getComprehensiveDate() {
        return comprehensiveDate;
    }

    public void setComprehensiveDate(Date comprehensiveDate) {
        this.comprehensiveDate = comprehensiveDate;
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

    public String getMinisterName() {
        return ministerName;
    }

    public void setMinisterName(String ministerName) {
        this.ministerName = ministerName;
    }

    public Date getMinisterDate() {
        return ministerDate;
    }

    public void setMinisterDate(Date ministerDate) {
        this.ministerDate = ministerDate;
    }

    public Date getPresignDate() {
        return presignDate;
    }

    public void setPresignDate(Date presignDate) {
        this.presignDate = presignDate;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        if(Validate.isString(signid)){
            this.signid = signid;
        }
    }

    public String getFilecode() {
        return filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }

    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode;
    }

    public BigDecimal getDeclaration() {
        return declaration;
    }

    public void setDeclaration(BigDecimal declaration) {
        this.declaration = declaration;
    }

    public String getReviewstage() {
        return reviewstage;
    }

    public void setReviewstage(String reviewstage) {
        this.reviewstage = reviewstage;
    }

    public String getIspresign() {
        return ispresign;
    }

    public void setIspresign(String ispresign) {
        this.ispresign = ispresign;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getIschangeEstimate() {
        return ischangeEstimate;
    }

    public void setIschangeEstimate(String ischangeEstimate) {
        this.ischangeEstimate = ischangeEstimate;
    }

    public String getMaindepetid() {
        return maindepetid;
    }

    public void setMaindepetid(String maindepetid) {
        this.maindepetid = maindepetid;
    }

    public String getMaindeptName() {
        return maindeptName;
    }

    public void setMaindeptName(String maindeptName) {
        this.maindeptName = maindeptName;
    }

    public String getMainDeptUserName() {
        return mainDeptUserName;
    }

    public void setMainDeptUserName(String mainDeptUserName) {
        this.mainDeptUserName = mainDeptUserName;
    }

    public String getMaindepetcontactuserid() {
        return maindepetcontactuserid;
    }

    public void setMaindepetcontactuserid(String maindepetcontactuserid) {
        this.maindepetcontactuserid = maindepetcontactuserid;
    }

    public String getAssistdeptid() {
        return assistdeptid;
    }

    public void setAssistdeptid(String assistdeptid) {
        this.assistdeptid = assistdeptid;
    }

    public String getAssistdeptName() {
        return assistdeptName;
    }

    public void setAssistdeptName(String assistdeptName) {
        this.assistdeptName = assistdeptName;
    }

    public String getAssistDeptUserName() {
        return assistDeptUserName;
    }

    public void setAssistDeptUserName(String assistDeptUserName) {
        this.assistDeptUserName = assistDeptUserName;
    }

    public String getAssistdeptcontactuserid() {
        return assistdeptcontactuserid;
    }

    public void setAssistdeptcontactuserid(String assistdeptcontactuserid) {
        this.assistdeptcontactuserid = assistdeptcontactuserid;
    }

    public String getDesigncompanyid() {
        return designcompanyid;
    }

    public void setDesigncompanyid(String designcompanyid) {
        this.designcompanyid = designcompanyid;
    }

    public String getDesigncompanyName() {
        return designcompanyName;
    }

    public void setDesigncompanyName(String designcompanyName) {
        this.designcompanyName = designcompanyName;
    }

    public String getBuiltcompanyid() {
        return builtcompanyid;
    }

    public void setBuiltcompanyid(String builtcompanyid) {
        this.builtcompanyid = builtcompanyid;
    }

    public String getBuiltcompanyName() {
        return builtcompanyName;
    }

    public void setBuiltcompanyName(String builtcompanyName) {
        this.builtcompanyName = builtcompanyName;
    }

    public String getUrgencydegree() {
        return urgencydegree;
    }

    public void setUrgencydegree(String urgencydegree) {
        this.urgencydegree = urgencydegree;
    }

    public String getYearplantype() {
        return yearplantype;
    }

    public void setYearplantype(String yearplantype) {
        this.yearplantype = yearplantype;
    }

    public String getSecrectlevel() {
        return secrectlevel;
    }

    public void setSecrectlevel(String secrectlevel) {
        this.secrectlevel = secrectlevel;
    }

    public String getSendusersign() {
        return sendusersign;
    }

    public void setSendusersign(String sendusersign) {
        this.sendusersign = sendusersign;
    }

    public String getIssign() {
        return issign;
    }

    public void setIssign(String issign) {
        this.issign = issign;
    }

    public String getIsassistflow() {
        return isassistflow;
    }

    public void setIsassistflow(String isassistflow) {
        this.isassistflow = isassistflow;
    }

    public String getIshasreviewcost() {
        return ishasreviewcost;
    }

    public void setIshasreviewcost(String ishasreviewcost) {
        this.ishasreviewcost = ishasreviewcost;
    }

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
    }

    public Float getSurplusdays() {
        return surplusdays;
    }

    public void setSurplusdays(Float surplusdays) {
        this.surplusdays = surplusdays;
    }

    public Date getExpectdispatchdate() {
        return expectdispatchdate;
    }

    public void setExpectdispatchdate(Date expectdispatchdate) {
        this.expectdispatchdate = expectdispatchdate;
    }

    public Date getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(Date receivedate) {
        this.receivedate = receivedate;
    }

    public Float getDaysafterdispatch() {
        return daysafterdispatch;
    }

    public void setDaysafterdispatch(Float daysafterdispatch) {
        this.daysafterdispatch = daysafterdispatch;
    }

    public Float getReviewdays() {
        return reviewdays;
    }

    public void setReviewdays(Float reviewdays) {
        this.reviewdays = reviewdays;
    }

    public String getIsassistproc() {
        return isassistproc;
    }

    public void setIsassistproc(String isassistproc) {
        this.isassistproc = isassistproc;
    }

    public String getFilenum() {
        return filenum;
    }

    public void setFilenum(String filenum) {
        this.filenum = filenum;
    }

    public String getDocnum() {
        return docnum;
    }

    public void setDocnum(String docnum) {
        this.docnum = docnum;
    }


    public Integer getSugProDealCount() {
        return sugProDealCount;
    }

    public void setSugProDealCount(Integer sugProDealCount) {
        this.sugProDealCount = sugProDealCount;
    }

    public String getSugProDealOriginal() {
        return sugProDealOriginal;
    }

    public void setSugProDealOriginal(String sugProDealOriginal) {
        this.sugProDealOriginal = sugProDealOriginal;
    }

    public String getSugProDealCopy() {
        return sugProDealCopy;
    }

    public void setSugProDealCopy(String sugProDealCopy) {
        this.sugProDealCopy = sugProDealCopy;
    }

    public Integer getSugFileDealCount() {
        return sugFileDealCount;
    }

    public void setSugFileDealCount(Integer sugFileDealCount) {
        this.sugFileDealCount = sugFileDealCount;
    }

    public String getSugFileDealOriginal() {
        return sugFileDealOriginal;
    }

    public void setSugFileDealOriginal(String sugFileDealOriginal) {
        this.sugFileDealOriginal = sugFileDealOriginal;
    }

    public String getSugFileDealCopy() {
        return sugFileDealCopy;
    }

    public void setSugFileDealCopy(String sugFileDealCopy) {
        this.sugFileDealCopy = sugFileDealCopy;
    }

    public Integer getSugOrgApplyCount() {
        return sugOrgApplyCount;
    }

    public void setSugOrgApplyCount(Integer sugOrgApplyCount) {
        this.sugOrgApplyCount = sugOrgApplyCount;
    }

    public String getSugOrgApplyOriginal() {
        return sugOrgApplyOriginal;
    }

    public void setSugOrgApplyOriginal(String sugOrgApplyOriginal) {
        this.sugOrgApplyOriginal = sugOrgApplyOriginal;
    }

    public String getSugOrgApplyCopy() {
        return sugOrgApplyCopy;
    }

    public void setSugOrgApplyCopy(String sugOrgApplyCopy) {
        this.sugOrgApplyCopy = sugOrgApplyCopy;
    }

    public Integer getSugOrgReqCount() {
        return sugOrgReqCount;
    }

    public void setSugOrgReqCount(Integer sugOrgReqCount) {
        this.sugOrgReqCount = sugOrgReqCount;
    }

    public String getSugOrgReqOriginal() {
        return sugOrgReqOriginal;
    }

    public void setSugOrgReqOriginal(String sugOrgReqOriginal) {
        this.sugOrgReqOriginal = sugOrgReqOriginal;
    }

    public String getSugOrgReqCopy() {
        return sugOrgReqCopy;
    }

    public void setSugOrgReqCopy(String sugOrgReqCopy) {
        this.sugOrgReqCopy = sugOrgReqCopy;
    }

    public Integer getCapitalAppReportCount() {
        return capitalAppReportCount;
    }

    public void setCapitalAppReportCount(Integer capitalAppReportCount) {
        this.capitalAppReportCount = capitalAppReportCount;
    }

    public String getCapitalAppReportOriginal() {
        return capitalAppReportOriginal;
    }

    public void setCapitalAppReportOriginal(String capitalAppReportOriginal) {
        this.capitalAppReportOriginal = capitalAppReportOriginal;
    }

    public Integer getSugProAdviseCount() {
        return sugProAdviseCount;
    }

    public void setSugProAdviseCount(Integer sugProAdviseCount) {
        this.sugProAdviseCount = sugProAdviseCount;
    }

    public String getSugProAdviseOriginal() {
        return sugProAdviseOriginal;
    }

    public void setSugProAdviseOriginal(String sugProAdviseOriginal) {
        this.sugProAdviseOriginal = sugProAdviseOriginal;
    }

    public String getSugProAdviseCopy() {
        return sugProAdviseCopy;
    }

    public void setSugProAdviseCopy(String sugProAdviseCopy) {
        this.sugProAdviseCopy = sugProAdviseCopy;
    }

    public Integer getProSugEledocCount() {
        return proSugEledocCount;
    }

    public void setProSugEledocCount(Integer proSugEledocCount) {
        this.proSugEledocCount = proSugEledocCount;
    }

    public String getProSugEledocOriginal() {
        return proSugEledocOriginal;
    }

    public void setProSugEledocOriginal(String proSugEledocOriginal) {
        this.proSugEledocOriginal = proSugEledocOriginal;
    }

    public String getProSugEledocCopy() {
        return proSugEledocCopy;
    }

    public void setProSugEledocCopy(String proSugEledocCopy) {
        this.proSugEledocCopy = proSugEledocCopy;
    }

    public BigDecimal getAppalyInvestment() {
        return appalyInvestment;
    }

    public void setAppalyInvestment(BigDecimal appalyInvestment) {
        this.appalyInvestment = appalyInvestment;
    }

    public Integer getSugMeetCount() {
        return sugMeetCount;
    }

    public void setSugMeetCount(Integer sugMeetCount) {
        this.sugMeetCount = sugMeetCount;
    }

    public String getSugMeetOriginal() {
        return sugMeetOriginal;
    }

    public void setSugMeetOriginal(String sugMeetOriginal) {
        this.sugMeetOriginal = sugMeetOriginal;
    }

    public String getSugMeetCopy() {
        return sugMeetCopy;
    }

    public void setSugMeetCopy(String sugMeetCopy) {
        this.sugMeetCopy = sugMeetCopy;
    }

    public Integer getStudyProDealCount() {
        return studyProDealCount;
    }

    public void setStudyProDealCount(Integer studyProDealCount) {
        this.studyProDealCount = studyProDealCount;
    }

    public String getStudyPealOriginal() {
        return studyPealOriginal;
    }

    public void setStudyPealOriginal(String studyPealOriginal) {
        this.studyPealOriginal = studyPealOriginal;
    }

    public String getStudyProDealCopy() {
        return studyProDealCopy;
    }

    public void setStudyProDealCopy(String studyProDealCopy) {
        this.studyProDealCopy = studyProDealCopy;
    }

    public Integer getStudyFileDealCount() {
        return studyFileDealCount;
    }

    public void setStudyFileDealCount(Integer studyFileDealCount) {
        this.studyFileDealCount = studyFileDealCount;
    }

    public String getStudyFileDealOriginal() {
        return studyFileDealOriginal;
    }

    public void setStudyFileDealOriginal(String studyFileDealOriginal) {
        this.studyFileDealOriginal = studyFileDealOriginal;
    }

    public String getStudyFileDealCopy() {
        return studyFileDealCopy;
    }

    public void setStudyFileDealCopy(String studyFileDealCopy) {
        this.studyFileDealCopy = studyFileDealCopy;
    }

    public Integer getStudyOrgApplyCount() {
        return studyOrgApplyCount;
    }

    public void setStudyOrgApplyCount(Integer studyOrgApplyCount) {
        this.studyOrgApplyCount = studyOrgApplyCount;
    }

    public String getStudyOrgApplyOriginal() {
        return studyOrgApplyOriginal;
    }

    public void setStudyOrgApplyOriginal(String studyOrgApplyOriginal) {
        this.studyOrgApplyOriginal = studyOrgApplyOriginal;
    }

    public String getStudyOrgApplyCopy() {
        return studyOrgApplyCopy;
    }

    public void setStudyOrgApplyCopy(String studyOrgApplyCopy) {
        this.studyOrgApplyCopy = studyOrgApplyCopy;
    }

    public Integer getStudyOrgReqCount() {
        return studyOrgReqCount;
    }

    public void setStudyOrgReqCount(Integer studyOrgReqCount) {
        this.studyOrgReqCount = studyOrgReqCount;
    }

    public String getStudyOrgReqOriginal() {
        return studyOrgReqOriginal;
    }

    public void setStudyOrgReqOriginal(String studyOrgReqOriginal) {
        this.studyOrgReqOriginal = studyOrgReqOriginal;
    }

    public String getStudyOrgReqCopy() {
        return studyOrgReqCopy;
    }

    public void setStudyOrgReqCopy(String studyOrgReqCopy) {
        this.studyOrgReqCopy = studyOrgReqCopy;
    }

    public Integer getStudyProSugCount() {
        return studyProSugCount;
    }

    public void setStudyProSugCount(Integer studyProSugCount) {
        this.studyProSugCount = studyProSugCount;
    }

    public String getStudyProSugOriginal() {
        return studyProSugOriginal;
    }

    public void setStudyProSugOriginal(String studyProSugOriginal) {
        this.studyProSugOriginal = studyProSugOriginal;
    }

    public String getStudyProSugCopy() {
        return studyProSugCopy;
    }

    public void setStudyProSugCopy(String studyProSugCopy) {
        this.studyProSugCopy = studyProSugCopy;
    }

    public Integer getStudyMeetCount() {
        return studyMeetCount;
    }

    public void setStudyMeetCount(Integer studyMeetCount) {
        this.studyMeetCount = studyMeetCount;
    }

    public String getStudyMeetOriginal() {
        return studyMeetOriginal;
    }

    public void setStudyMeetOriginal(String studyMeetOriginal) {
        this.studyMeetOriginal = studyMeetOriginal;
    }

    public String getStudyMeetCopy() {
        return studyMeetCopy;
    }

    public void setStudyMeetCopy(String studyMeetCopy) {
        this.studyMeetCopy = studyMeetCopy;
    }

    public Integer getEnvproReplyCount() {
        return envproReplyCount;
    }

    public void setEnvproReplyCount(Integer envproReplyCount) {
        this.envproReplyCount = envproReplyCount;
    }

    public String getEnvproReplyOriginal() {
        return envproReplyOriginal;
    }

    public void setEnvproReplyOriginal(String envproReplyOriginal) {
        this.envproReplyOriginal = envproReplyOriginal;
    }

    public String getEnvproReplyCopy() {
        return envproReplyCopy;
    }

    public void setEnvproReplyCopy(String envproReplyCopy) {
        this.envproReplyCopy = envproReplyCopy;
    }

    public Integer getPlanAddrCount() {
        return planAddrCount;
    }

    public void setPlanAddrCount(Integer planAddrCount) {
        this.planAddrCount = planAddrCount;
    }

    public String getPlanAddrOriginal() {
        return planAddrOriginal;
    }

    public void setPlanAddrOriginal(String planAddrOriginal) {
        this.planAddrOriginal = planAddrOriginal;
    }

    public String getPlanAddrCopy() {
        return planAddrCopy;
    }

    public void setPlanAddrCopy(String planAddrCopy) {
        this.planAddrCopy = planAddrCopy;
    }

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

    public String getReportOrigin() {
        return reportOrigin;
    }

    public void setReportOrigin(String reportOrigin) {
        this.reportOrigin = reportOrigin;
    }

    public String getReportCopy() {
        return reportCopy;
    }

    public void setReportCopy(String reportCopy) {
        this.reportCopy = reportCopy;
    }

    public Integer getEledocCount() {
        return eledocCount;
    }

    public void setEledocCount(Integer eledocCount) {
        this.eledocCount = eledocCount;
    }

    public String getEledocOriginal() {
        return eledocOriginal;
    }

    public void setEledocOriginal(String eledocOriginal) {
        this.eledocOriginal = eledocOriginal;
    }

    public String getEledocCopy() {
        return eledocCopy;
    }

    public void setEledocCopy(String eledocCopy) {
        this.eledocCopy = eledocCopy;
    }

    public Integer getEnergyCount() {
        return energyCount;
    }

    public void setEnergyCount(Integer energyCount) {
        this.energyCount = energyCount;
    }

    public String getEnergyOriginal() {
        return energyOriginal;
    }

    public void setEnergyOriginal(String energyOriginal) {
        this.energyOriginal = energyOriginal;
    }

    public String getEnergyCopy() {
        return energyCopy;
    }

    public void setEnergyCopy(String energyCopy) {
        this.energyCopy = energyCopy;
    }

    public Integer getGovernmentPurchasCount() {
        return governmentPurchasCount;
    }

    public void setGovernmentPurchasCount(Integer governmentPurchasCount) {
        this.governmentPurchasCount = governmentPurchasCount;
    }

    public String getGovernmentPurchasOriginal() {
        return governmentPurchasOriginal;
    }

    public void setGovernmentPurchasOriginal(String governmentPurchasOriginal) {
        this.governmentPurchasOriginal = governmentPurchasOriginal;
    }

    public String getGovernmentPurchasCopy() {
        return governmentPurchasCopy;
    }

    public void setGovernmentPurchasCopy(String governmentPurchasCopy) {
        this.governmentPurchasCopy = governmentPurchasCopy;
    }

    public Integer getExpertArgumentCount() {
        return expertArgumentCount;
    }

    public void setExpertArgumentCount(Integer expertArgumentCount) {
        this.expertArgumentCount = expertArgumentCount;
    }

    public String getExpertArgumentOriginal() {
        return expertArgumentOriginal;
    }

    public void setExpertArgumentOriginal(String expertArgumentOriginal) {
        this.expertArgumentOriginal = expertArgumentOriginal;
    }

    public String getExpertArgumentCopy() {
        return expertArgumentCopy;
    }

    public void setExpertArgumentCopy(String expertArgumentCopy) {
        this.expertArgumentCopy = expertArgumentCopy;
    }

    public Integer getDevelopmentCount() {
        return developmentCount;
    }

    public void setDevelopmentCount(Integer developmentCount) {
        this.developmentCount = developmentCount;
    }

    public String getDevelopementOriginal() {
        return developementOriginal;
    }

    public void setDevelopementOriginal(String developementOriginal) {
        this.developementOriginal = developementOriginal;
    }

    public String getDevelopementCopy() {
        return developementCopy;
    }

    public void setDevelopementCopy(String developementCopy) {
        this.developementCopy = developementCopy;
    }

    public Integer getOfficialDisposeCount() {
        return officialDisposeCount;
    }

    public void setOfficialDisposeCount(Integer officialDisposeCount) {
        this.officialDisposeCount = officialDisposeCount;
    }

    public String getOfficialDisposeOriginal() {
        return officialDisposeOriginal;
    }

    public void setOfficialDisposeOriginal(String officialDisposeOriginal) {
        this.officialDisposeOriginal = officialDisposeOriginal;
    }

    public String getOfficialDisposeCopy() {
        return officialDisposeCopy;
    }

    public void setOfficialDisposeCopy(String officialDisposeCopy) {
        this.officialDisposeCopy = officialDisposeCopy;
    }

    public Integer getManagerDeptCount() {
        return managerDeptCount;
    }

    public void setManagerDeptCount(Integer managerDeptCount) {
        this.managerDeptCount = managerDeptCount;
    }

    public String getManagerDeptOriginal() {
        return managerDeptOriginal;
    }

    public void setManagerDeptOriginal(String managerDeptOriginal) {
        this.managerDeptOriginal = managerDeptOriginal;
    }

    public String getManagerDeptCopy() {
        return managerDeptCopy;
    }

    public void setManagerDeptCopy(String managerDeptCopy) {
        this.managerDeptCopy = managerDeptCopy;
    }

    public Integer getImportProductCount() {
        return importProductCount;
    }

    public void setImportProductCount(Integer importProductCount) {
        this.importProductCount = importProductCount;
    }

    public String getImportProductOriginal() {
        return importProductOriginal;
    }

    public void setImportProductOriginal(String importProductOriginal) {
        this.importProductOriginal = importProductOriginal;
    }

    public String getImportProductCopy() {
        return importProductCopy;
    }

    public void setImportProductCopy(String importProductCopy) {
        this.importProductCopy = importProductCopy;
    }

    public Integer getOrtherProjectCount() {
        return ortherProjectCount;
    }

    public void setOrtherProjectCount(Integer ortherProjectCount) {
        this.ortherProjectCount = ortherProjectCount;
    }

    public String getOrtherProjectOriginal() {
        return ortherProjectOriginal;
    }

    public void setOrtherProjectOriginal(String ortherProjectOriginal) {
        this.ortherProjectOriginal = ortherProjectOriginal;
    }

    public String getOrtherProjectCopy() {
        return ortherProjectCopy;
    }

    public void setOrtherProjectCopy(String ortherProjectCopy) {
        this.ortherProjectCopy = ortherProjectCopy;
    }

    public Integer getSprcialDevicesCount() {
        return sprcialDevicesCount;
    }

    public void setSprcialDevicesCount(Integer sprcialDevicesCount) {
        this.sprcialDevicesCount = sprcialDevicesCount;
    }

    public String getSprcialDevicesOriginal() {
        return sprcialDevicesOriginal;
    }

    public void setSprcialDevicesOriginal(String sprcialDevicesOriginal) {
        this.sprcialDevicesOriginal = sprcialDevicesOriginal;
    }

    public String getSprcialDevicesCopy() {
        return sprcialDevicesCopy;
    }

    public void setSprcialDevicesCopy(String sprcialDevicesCopy) {
        this.sprcialDevicesCopy = sprcialDevicesCopy;
    }

    public Integer getProjectDeclareCount() {
        return projectDeclareCount;
    }

    public void setProjectDeclareCount(Integer projectDeclareCount) {
        this.projectDeclareCount = projectDeclareCount;
    }

    public String getProjectDeclareOriginal() {
        return projectDeclareOriginal;
    }

    public void setProjectDeclareOriginal(String projectDeclareOriginal) {
        this.projectDeclareOriginal = projectDeclareOriginal;
    }

    public String getProjectDeclareCopy() {
        return projectDeclareCopy;
    }

    public void setProjectDeclareCopy(String projectDeclareCopy) {
        this.projectDeclareCopy = projectDeclareCopy;
    }

    public Integer getProjectApplyReportCount() {
        return projectApplyReportCount;
    }

    public void setProjectApplyReportCount(Integer projectApplyReportCount) {
        this.projectApplyReportCount = projectApplyReportCount;
    }

    public String getProjectApplyReportOriginal() {
        return projectApplyReportOriginal;
    }

    public void setProjectApplyReportOriginal(String projectApplyReportOriginal) {
        this.projectApplyReportOriginal = projectApplyReportOriginal;
    }

    public String getProjectApplyReportCopy() {
        return projectApplyReportCopy;
    }

    public void setProjectApplyReportCopy(String projectApplyReportCopy) {
        this.projectApplyReportCopy = projectApplyReportCopy;
    }

    public Integer getPurchasDeviceCount() {
        return purchasDeviceCount;
    }

    public void setPurchasDeviceCount(Integer purchasDeviceCount) {
        this.purchasDeviceCount = purchasDeviceCount;
    }

    public String getPurchasDeviceOriginal() {
        return purchasDeviceOriginal;
    }

    public void setPurchasDeviceOriginal(String purchasDeviceOriginal) {
        this.purchasDeviceOriginal = purchasDeviceOriginal;
    }

    public String getPurchasDeviceCopy() {
        return purchasDeviceCopy;
    }

    public void setPurchasDeviceCopy(String purchasDeviceCopy) {
        this.purchasDeviceCopy = purchasDeviceCopy;
    }

    public Integer getElectronicDocumentCount() {
        return electronicDocumentCount;
    }

    public void setElectronicDocumentCount(Integer electronicDocumentCount) {
        this.electronicDocumentCount = electronicDocumentCount;
    }

    public String getElectronicDocumentOriginal() {
        return electronicDocumentOriginal;
    }

    public void setElectronicDocumentOriginal(String electronicDocumentOriginal) {
        this.electronicDocumentOriginal = electronicDocumentOriginal;
    }

    public String getElectronicDocumentCopy() {
        return electronicDocumentCopy;
    }

    public void setElectronicDocumentCopy(String electronicDocumentCopy) {
        this.electronicDocumentCopy = electronicDocumentCopy;
    }

    public Integer getPurchasDevicePactCount() {
        return purchasDevicePactCount;
    }

    public void setPurchasDevicePactCount(Integer purchasDevicePactCount) {
        this.purchasDevicePactCount = purchasDevicePactCount;
    }

    public String getPurchasDevicePactOriginal() {
        return purchasDevicePactOriginal;
    }

    public void setPurchasDevicePactOriginal(String purchasDevicePactOriginal) {
        this.purchasDevicePactOriginal = purchasDevicePactOriginal;
    }

    public String getPurchasDevicePactCopy() {
        return purchasDevicePactCopy;
    }

    public void setPurchasDevicePactCopy(String purchasDevicePactCopy) {
        this.purchasDevicePactCopy = purchasDevicePactCopy;
    }

    public Integer getProjectApproveFileCount() {
        return projectApproveFileCount;
    }

    public void setProjectApproveFileCount(Integer projectApproveFileCount) {
        this.projectApproveFileCount = projectApproveFileCount;
    }

    public String getProjectApproveFileOriginal() {
        return projectApproveFileOriginal;
    }

    public void setProjectApproveFileOriginal(String projectApproveFileOriginal) {
        this.projectApproveFileOriginal = projectApproveFileOriginal;
    }

    public String getProjectApproveFileCopy() {
        return projectApproveFileCopy;
    }

    public void setProjectApproveFileCopy(String projectApproveFileCopy) {
        this.projectApproveFileCopy = projectApproveFileCopy;
    }

    public Integer getBusinessLicenseCount() {
        return businessLicenseCount;
    }

    public void setBusinessLicenseCount(Integer businessLicenseCount) {
        this.businessLicenseCount = businessLicenseCount;
    }

    public String getBusinessLicenseOriginal() {
        return businessLicenseOriginal;
    }

    public void setBusinessLicenseOriginal(String businessLicenseOriginal) {
        this.businessLicenseOriginal = businessLicenseOriginal;
    }

    public String getBusinessLicenseCopy() {
        return businessLicenseCopy;
    }

    public void setBusinessLicenseCopy(String businessLicenseCopy) {
        this.businessLicenseCopy = businessLicenseCopy;
    }

    public Integer getLegalPersoncCardCount() {
        return legalPersoncCardCount;
    }

    public void setLegalPersoncCardCount(Integer legalPersoncCardCount) {
        this.legalPersoncCardCount = legalPersoncCardCount;
    }

    public String getLegalPersoncCardOriginal() {
        return legalPersoncCardOriginal;
    }

    public void setLegalPersoncCardOriginal(String legalPersoncCardOriginal) {
        this.legalPersoncCardOriginal = legalPersoncCardOriginal;
    }

    public String getLegalPersoncCardCopy() {
        return legalPersoncCardCopy;
    }

    public void setLegalPersoncCardCopy(String legalPersoncCardCopy) {
        this.legalPersoncCardCopy = legalPersoncCardCopy;
    }

    public Integer getEnvironmentalEffectCount() {
        return environmentalEffectCount;
    }

    public void setEnvironmentalEffectCount(Integer environmentalEffectCount) {
        this.environmentalEffectCount = environmentalEffectCount;
    }

    public String getEnvironmentalEffectOriginal() {
        return environmentalEffectOriginal;
    }

    public void setEnvironmentalEffectOriginal(String environmentalEffectOriginal) {
        this.environmentalEffectOriginal = environmentalEffectOriginal;
    }

    public String getEnvironmentalEffectCopy() {
        return environmentalEffectCopy;
    }

    public void setEnvironmentalEffectCopy(String environmentalEffectCopy) {
        this.environmentalEffectCopy = environmentalEffectCopy;
    }

    public Integer getLandPlanningLicenseCount() {
        return landPlanningLicenseCount;
    }

    public void setLandPlanningLicenseCount(Integer landPlanningLicenseCount) {
        this.landPlanningLicenseCount = landPlanningLicenseCount;
    }

    public String getLandPlanningLicenseOriginal() {
        return landPlanningLicenseOriginal;
    }

    public void setLandPlanningLicenseOriginal(String landPlanningLicenseOriginal) {
        this.landPlanningLicenseOriginal = landPlanningLicenseOriginal;
    }

    public String getLandPlanningLicenseCopy() {
        return landPlanningLicenseCopy;
    }

    public void setLandPlanningLicenseCopy(String landPlanningLicenseCopy) {
        this.landPlanningLicenseCopy = landPlanningLicenseCopy;
    }

    public Integer getGeologicalSurveyCount() {
        return geologicalSurveyCount;
    }

    public void setGeologicalSurveyCount(Integer geologicalSurveyCount) {
        this.geologicalSurveyCount = geologicalSurveyCount;
    }

    public String getGeologicalSurveyOriginal() {
        return geologicalSurveyOriginal;
    }

    public void setGeologicalSurveyOriginal(String geologicalSurveyOriginal) {
        this.geologicalSurveyOriginal = geologicalSurveyOriginal;
    }

    public String getGeologicalSurveyCopy() {
        return geologicalSurveyCopy;
    }

    public void setGeologicalSurveyCopy(String geologicalSurveyCopy) {
        this.geologicalSurveyCopy = geologicalSurveyCopy;
    }

    public Integer getDescriptionDesignCount() {
        return descriptionDesignCount;
    }

    public void setDescriptionDesignCount(Integer descriptionDesignCount) {
        this.descriptionDesignCount = descriptionDesignCount;
    }

    public String getDescriptionDesignOriginal() {
        return descriptionDesignOriginal;
    }

    public void setDescriptionDesignOriginal(String descriptionDesignOriginal) {
        this.descriptionDesignOriginal = descriptionDesignOriginal;
    }

    public String getDescriptionDesignCopy() {
        return descriptionDesignCopy;
    }

    public void setDescriptionDesignCopy(String descriptionDesignCopy) {
        this.descriptionDesignCopy = descriptionDesignCopy;
    }

    public Integer getProjectBudgetCount() {
        return projectBudgetCount;
    }

    public void setProjectBudgetCount(Integer projectBudgetCount) {
        this.projectBudgetCount = projectBudgetCount;
    }

    public String getProjectBudgetOriginal() {
        return projectBudgetOriginal;
    }

    public void setProjectBudgetOriginal(String projectBudgetOriginal) {
        this.projectBudgetOriginal = projectBudgetOriginal;
    }

    public String getProjectBudgetCopy() {
        return projectBudgetCopy;
    }

    public void setProjectBudgetCopy(String projectBudgetCopy) {
        this.projectBudgetCopy = projectBudgetCopy;
    }

    public Integer getPreliminaryDrawingCount() {
        return preliminaryDrawingCount;
    }

    public void setPreliminaryDrawingCount(Integer preliminaryDrawingCount) {
        this.preliminaryDrawingCount = preliminaryDrawingCount;
    }

    public String getPreliminaryDrawingOriginal() {
        return preliminaryDrawingOriginal;
    }

    public void setPreliminaryDrawingOriginal(String preliminaryDrawingOriginal) {
        this.preliminaryDrawingOriginal = preliminaryDrawingOriginal;
    }

    public String getPreliminaryDrawingCopy() {
        return preliminaryDrawingCopy;
    }

    public void setPreliminaryDrawingCopy(String preliminaryDrawingCopy) {
        this.preliminaryDrawingCopy = preliminaryDrawingCopy;
    }

    public Integer getConstructionDrawingsCount() {
        return constructionDrawingsCount;
    }

    public void setConstructionDrawingsCount(Integer constructionDrawingsCount) {
        this.constructionDrawingsCount = constructionDrawingsCount;
    }

    public String getConstructionDrawingsOriginal() {
        return constructionDrawingsOriginal;
    }

    public void setConstructionDrawingsOriginal(String constructionDrawingsOriginal) {
        this.constructionDrawingsOriginal = constructionDrawingsOriginal;
    }

    public String getConstructionDrawingsCopy() {
        return constructionDrawingsCopy;
    }

    public void setConstructionDrawingsCopy(String constructionDrawingsCopy) {
        this.constructionDrawingsCopy = constructionDrawingsCopy;
    }

    public String getComprehensivehandlesug() {
        return comprehensivehandlesug;
    }

    public void setComprehensivehandlesug(String comprehensivehandlesug) {
        this.comprehensivehandlesug = comprehensivehandlesug;
    }

    public String getComprehensiveId() {
        return comprehensiveId;
    }

    public void setComprehensiveId(String comprehensiveId) {
        this.comprehensiveId = comprehensiveId;
    }


   /* public String getFinanciaStatus() {
        return financiaStatus;
	}

	public void setFinanciaStatus(String financiaStatus) {
		this.financiaStatus = financiaStatus;
	}*/

    public String getLeaderhandlesug() {
        return leaderhandlesug;
    }

    public void setLeaderhandlesug(String leaderhandlesug) {
        this.leaderhandlesug = leaderhandlesug;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getMinisterhandlesug() {
        return ministerhandlesug;
    }

    public void setMinisterhandlesug(String ministerhandlesug) {
        this.ministerhandlesug = ministerhandlesug;
    }

    public String getMinisterId() {
        return ministerId;
    }

    public void setMinisterId(String ministerId) {
        this.ministerId = ministerId;
    }

    public String getSignState() {
        return signState;
    }

    public void setSignState(String signState) {
        this.signState = signState;
    }

    public Integer getProcessState() {
        return processState;
    }

    public void setProcessState(Integer processState) {
        this.processState = processState;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getSecondPriUser() {
        return secondPriUser;
    }

    public void setSecondPriUser(String secondPriUser) {
        this.secondPriUser = secondPriUser;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsAssociate() {
        return isAssociate;
    }

    public void setIsAssociate(Integer isAssociate) {
        this.isAssociate = isAssociate;
    }

    public String getIsAdvanced() {
        return isAdvanced;
    }

    public void setIsAdvanced(String isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    public SignDto getAssociateSign() {
        return associateSign;
    }

    public void setAssociateSign(SignDto associateSign) {
        this.associateSign = associateSign;
    }

    public String getDealOrgType() {
        return dealOrgType;
    }

    public void setDealOrgType(String dealOrgType) {
        this.dealOrgType = dealOrgType;
    }

    public List<WorkProgramDto> getWorkProgramDtoList() {
        return workProgramDtoList;
    }

    public void setWorkProgramDtoList(List<WorkProgramDto> workProgramDtoList) {
        this.workProgramDtoList = workProgramDtoList;
    }

    public DispatchDocDto getDispatchDocDto() {
        return dispatchDocDto;
    }

    public void setDispatchDocDto(DispatchDocDto dispatchDocDto) {
        this.dispatchDocDto = dispatchDocDto;
    }

    public FileRecordDto getFileRecordDto() {
        return fileRecordDto;
    }

    public void setFileRecordDto(FileRecordDto fileRecordDto) {
        this.fileRecordDto = fileRecordDto;
    }

    public AssistPlanDto getAssistPlanDto() {
        return assistPlanDto;
    }

    public void setAssistPlanDto(AssistPlanDto assistPlanDto) {
        this.assistPlanDto = assistPlanDto;
    }

    public List<AssistPlanSignDto> getPlanSignDtoList() {
        return planSignDtoList;
    }

    public void setPlanSignDtoList(List<AssistPlanSignDto> planSignDtoList) {
        this.planSignDtoList = planSignDtoList;
    }

    public String getIsProjectState() {
        return isProjectState;
    }

    public void setIsProjectState(String isProjectState) {
        this.isProjectState = isProjectState;
    }

    public String getSignNum() {
        return signNum;
    }

    public void setSignNum(String signNum) {
        this.signNum = signNum;
    }

    public Integer getSignSeq() {
        return signSeq;
    }

    public void setSignSeq(Integer signSeq) {
        this.signSeq = signSeq;
    }

    public String getIsSupplementary() {
        return isSupplementary;
    }

    public void setIsSupplementary(String isSupplementary) {
        this.isSupplementary = isSupplementary;
    }

    /*public String getAssistStatus() {
		return assistStatus;
	}

	public void setAssistStatus(String assistStatus) {
		this.assistStatus = assistStatus;
	}*/

    public BigDecimal getAuthorizeValue() {
        return authorizeValue;
    }

    public void setAuthorizeValue(BigDecimal authorizeValue) {
        this.authorizeValue = authorizeValue;
    }

    public String getIsHaveSuppLetter() {
        return isHaveSuppLetter;
    }

    public void setIsHaveSuppLetter(String isHaveSuppLetter) {
        this.isHaveSuppLetter = isHaveSuppLetter;
    }

    public Date getSuppLetterDate() {
        return suppLetterDate;
    }

    public void setSuppLetterDate(Date suppLetterDate) {
        this.suppLetterDate = suppLetterDate;
    }

    public ExpertReviewDto getExpertReviewDto() {
        return expertReviewDto;
    }

    public void setExpertReviewDto(ExpertReviewDto expertReviewDto) {
        this.expertReviewDto = expertReviewDto;
    }

    public List<AddSuppLetterDto> getSuppLetterDtoList() {
        return suppLetterDtoList;
    }

    public void setSuppLetterDtoList(List<AddSuppLetterDto> suppLetterDtoList) {
        this.suppLetterDtoList = suppLetterDtoList;
    }

    public List<AddRegisterFileDto> getRegisterFileDtoDtoList() {
        return registerFileDtoDtoList;
    }

    public void setRegisterFileDtoDtoList(List<AddRegisterFileDto> registerFileDtoDtoList) {
        this.registerFileDtoDtoList = registerFileDtoDtoList;
    }

    public List<SignDto> getAssociateSignDtoList() {
        return associateSignDtoList;
    }

    public void setAssociateSignDtoList(List<SignDto> associateSignDtoList) {
        this.associateSignDtoList = associateSignDtoList;
    }

    public Integer getOldProjectId() {
        return oldProjectId;
    }

    public void setOldProjectId(Integer oldProjectId) {
        this.oldProjectId = oldProjectId;
    }

    public String getPalceStorage() {
        return palceStorage;
    }

    public void setPalceStorage(String palceStorage) {
        this.palceStorage = palceStorage;
    }

    public Date getPalceTime() {
        return palceTime;
    }

    public void setPalceTime(Date palceTime) {
        this.palceTime = palceTime;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getIsSendFGW() {
        return isSendFGW;
    }

    public void setIsSendFGW(String isSendFGW) {
        this.isSendFGW = isSendFGW;
    }

    public List<SysFileDto> getSysFileDtoList() {
        return sysFileDtoList;
    }

    public void setSysFileDtoList(List<SysFileDto> sysFileDtoList) {
        this.sysFileDtoList = sysFileDtoList;
    }

    public Float getTotalReviewdays() {
        return totalReviewdays;
    }

    public void setTotalReviewdays(Float totalReviewdays) {
        this.totalReviewdays = totalReviewdays;
    }

    public UnitScoreDto getUnitScoreDto() {
        return unitScoreDto;
    }

    public void setUnitScoreDto(UnitScoreDto unitScoreDto) {
        this.unitScoreDto = unitScoreDto;
    }

    public String getmOrgId() {
        return mOrgId;
    }

    public void setmOrgId(String mOrgId) {
        this.mOrgId = mOrgId;
    }

    public String getmOrgName() {
        return mOrgName;
    }

    public void setmOrgName(String mOrgName) {
        this.mOrgName = mOrgName;
    }

    public String getaOrgId() {
        return aOrgId;
    }

    public void setaOrgId(String aOrgId) {
        this.aOrgId = aOrgId;
    }

    public String getaOrgName() {
        return aOrgName;
    }

    public void setaOrgName(String aOrgName) {
        this.aOrgName = aOrgName;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getaUserID() {
        return aUserID;
    }

    public void setaUserID(String aUserID) {
        this.aUserID = aUserID;
    }

    public String getaUserName() {
        return aUserName;
    }

    public void setaUserName(String aUserName) {
        this.aUserName = aUserName;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getBranchCount() {
        return branchCount;
    }

    public void setBranchCount(Integer branchCount) {
        this.branchCount = branchCount;
    }

    public Date getDispatchdate() {
        return dispatchdate;
    }

    public void setDispatchdate(Date dispatchdate) {
        this.dispatchdate = dispatchdate;
    }

    public String getMainDeptContactPhone() {
        return mainDeptContactPhone;
    }

    public void setMainDeptContactPhone(String mainDeptContactPhone) {
        this.mainDeptContactPhone = mainDeptContactPhone;
    }

    public ProjBaseInfoDto getProjBaseInfoDto() {
        return projBaseInfoDto;
    }

    public void setProjBaseInfoDto(ProjBaseInfoDto projBaseInfoDto) {
        this.projBaseInfoDto = projBaseInfoDto;
    }

    public List<ProjectStopDto> getProjectStopDtos() {
        return projectStopDtos;
    }

    public void setProjectStopDtos(List<ProjectStopDto> projectStopDtos) {
        this.projectStopDtos = projectStopDtos;
    }
}