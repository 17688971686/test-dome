package cs.domain.project;


import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
@Entity
@Table(name = "cs_sign")
@DynamicUpdate(true)
public class Sign extends DomainBase {

    /**
     * 收文ID
     */
    @Id
    private String signid;
    
    @Column(columnDefinition="VARCHAR(2)")
    private String isSendFileRecord;	//是否已发送存档

    //委内收文编号
    @Column(columnDefinition = "VARCHAR(30)")
    private String filecode;

    /**
     * 收文编号(年份+收文类型+序号[序号保留3位数])
     */
    @Column(columnDefinition = "VARCHAR(16)")
    private String signNum;

    /**
     * 收文序号
     */
    @Column(columnDefinition = "Integer")
    private Integer signSeq;

    //项目代码
    @Column(columnDefinition = "VARCHAR(20)")
    private String projectcode;

    //评审阶段
    @Column(columnDefinition = "VARCHAR(64)")
    private String reviewstage;

    //项目名称
    @Column(columnDefinition = "VARCHAR(200)")
    private String projectname;

    //主办处室ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String maindepetid;

    //主办事处名称
    @Column(columnDefinition = "VARCHAR(128)")
    private String maindeptName;

    //主办处室联系人ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String maindepetcontactuserid;

    //主办事处联系人
    @Column(columnDefinition = "VARCHAR(64)")
    private String mainDeptUserName;

    //主办事处联系电话
    @Column(columnDefinition = "VARCHAR(64)")
    private String mainDeptContactPhone;

    //协办处室ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String assistdeptid;

    //协办事处名称
    @Column(columnDefinition = "VARCHAR(64)")
    private String assistdeptName;

    //协办处室联系人ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String assistdeptcontactuserid;

    //协办事处联系人
    private String assistDeptUserName;

    //报审概算
    @Column(columnDefinition = "NUMBER")
    private BigDecimal declaration;
    
    //编制单位ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String designcompanyid;

    //编制单位名称
    @Column(columnDefinition = "VARCHAR(128)")
    private String designcompanyName;

    //建设单位ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String builtcompanyid;

    //建设单位名称
    @Column(columnDefinition = "VARCHAR(128)")
    private String builtcompanyName;

    //缓急程度
    @Column(columnDefinition = "VARCHAR(16)")
    private String urgencydegree;

    //年度计划类别
    @Column(columnDefinition = "VARCHAR(16)")
    private String yearplantype;

    //秘密登记
    @Column(columnDefinition = "VARCHAR(16)")
    private String secrectlevel;

    //综合部拟办意见
    @Column(columnDefinition = "VARCHAR(255)")
    private String comprehensivehandlesug;

    //综合部部长ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String comprehensiveId;

    //综合部拟办人名称
    @Column(columnDefinition = "VARCHAR(100)")
    private String comprehensiveName;

    //综合部拟办日期
    @Column(columnDefinition = "DATE")
    private Date comprehensiveDate;

    //中心领导审批意见
    @Column(columnDefinition = "VARCHAR(255)")
    private String leaderhandlesug;

    //中心领导ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String leaderId;

    //中心领导名称
    @Column(columnDefinition = "VARCHAR(32)")
    private String leaderName;

    //中心领导审批日期
    @Column(columnDefinition = "DATE")
    private Date leaderDate;

    //部长处理意见
    @Column(columnDefinition = "VARCHAR(255)")
    private String ministerhandlesug;

    //部长ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String ministerId;

    //部长名称
    @Column(columnDefinition = "VARCHAR(32)")
    private String ministerName;

    //部长处理日期
    @Column(columnDefinition = "DATE")
    private Date ministerDate;

    //送件人签名
    @Column(columnDefinition = "VARCHAR(16)")
    private String sendusersign;

    //项目签收时间
    @Column(columnDefinition = "DATE")
    private Date signdate;

    //剩余工作日
    @Column(columnDefinition = "NUMBER")
    private Float surplusdays;

    //发文时间
    @Column(columnDefinition = "DATE")
    private Date expectdispatchdate;

    //送来时间
    @Column(columnDefinition = "DATE")
    private Date receivedate;

    //发文后工作日
    @Column(columnDefinition = "NUMBER")
    private Float daysafterdispatch;

    //评审天数
    @Column(columnDefinition = "NUMBER")
    private Float reviewdays;

    //归档编号
    @Column(columnDefinition = "VARCHAR(100)")
    private String filenum;

    //文号
    @Column(columnDefinition = "VARCHAR(100)")
    private String docnum;

    //建议书项目处理表份数
    @Column(columnDefinition = "INTEGER")
    private Integer sugProDealCount;

    //建议书项目处理表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugProDealOriginal;

    //建议书项目处理表是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugProDealCopy;

    //建议书文件处理表份数
    @Column(columnDefinition = "INTEGER")
    private Integer sugFileDealCount;

    //建议书文件处理表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugFileDealOriginal;

    //建议书文件处理表是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugFileDealCopy;

    //建议书项目单位申报表份数
    @Column(columnDefinition = "INTEGER")
    private Integer sugOrgApplyCount;

    //建议书项目单位申报表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugOrgApplyOriginal;

    //建议书项目单位申报表是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugOrgApplyCopy;

    //建议书项目单位请示报告份数
    @Column(columnDefinition = "INTEGER")
    private Integer sugOrgReqCount;

    //建议书项目单位请示报告是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugOrgReqOriginal;

    //建议书项目单位请示报告是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugOrgReqCopy;

    //资金申请报告份数
    @Column(columnDefinition = "INTEGER")
    private Integer capitalAppReportCount;
    
    //资金申请报告是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String capitalAppReportOriginal;
    
    //项目建议书份数
    @Column(columnDefinition = "INTEGER")
    private Integer sugProAdviseCount;

    //项目建议书是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugProAdviseOriginal;

    //项目建议书是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugProAdviseCopy;

    //项目建议书电子文档份数
    @Column(columnDefinition = "INTEGER")
    private Integer proSugEledocCount;

    //项目建议书电子文档是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String proSugEledocOriginal;

    //项目建议书电子文档是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String proSugEledocCopy;

    //建议书相关会议纪要份数
    @Column(columnDefinition = "INTEGER")
    private Integer sugMeetCount;

    //建议书相关会议纪要是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugMeetOriginal;

    //建议书相关会议纪要是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sugMeetCopy;

    //可研项目处理表份数
    @Column(columnDefinition = "INTEGER")
    private Integer studyProDealCount;

    //可研项目处理表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyPealOriginal;

    //可研项目处理表是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyProDealCopy;

    //可研文件处理表份数
    @Column(columnDefinition = "INTEGER")
    private Integer studyFileDealCount;

    //可研文件处理表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyFileDealOriginal;

    //可研文件处理表是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyFileDealCopy;

    //可研项目单位申报表份数
    @Column(columnDefinition = "INTEGER")
    private Integer studyOrgApplyCount;

    //可研项目单位申报表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyOrgApplyOriginal;

    //可研项目单位申报表是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyOrgApplyCopy;

    //可研项目单位请示报告份数
    @Column(columnDefinition = "INTEGER")
    private Integer studyOrgReqCount;

    //可研项目单位请示报告是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyOrgReqOriginal;

    //可研项目单位请示报告是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyOrgReqCopy;

    //可研项目建议书批复
    @Column(columnDefinition = "Integer")
    private Integer studyProSugCount;

    //可研项目建议书批复是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyProSugOriginal;

    //可研项目建议书批复是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyProSugCopy;

    //可研相关会议纪要份数
    @Column(columnDefinition = "INTEGER")
    private Integer studyMeetCount;

    //可研相关会议是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyMeetOriginal;

    //可研相关会议是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String studyMeetCopy;

    //可研环保批复文件份数
    @Column(columnDefinition = "INTEGER")
    private Integer envproReplyCount;

    //可研环保批复文件是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String envproReplyOriginal;

    //可研环保批复文件是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String envproReplyCopy;

    //可研规划选址批文份数
    @Column(columnDefinition = "INTEGER")
    private Integer planAddrCount;

    //可研规划选址批文是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String planAddrOriginal;

    //可研规划选址批文是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String planAddrCopy;

    //可研报告份数
    @Column(columnDefinition = "INTEGER")
    private Integer reportCount;

    //可研报告是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String reportOrigin;

    //可研报告是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String reportCopy;

    //可研报告电子文档份数
    @Column(columnDefinition = "INTEGER")
    private Integer eledocCount;

    //可研报告电子文档是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String eledocOriginal;

    //可研报告电子文档是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String eledocCopy;

    //可研节能报告份数
    @Column(columnDefinition = "INTEGER")
    private Integer energyCount;

    //可研节能报告是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String energyOriginal;

    //可研节能报告是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String energyCopy;

    //流程实例ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String processInstanceId;

    //申报投资
    @Column(columnDefinition = "NUMBER")
    private BigDecimal appalyInvestment;
    
   //S (进口设备)政府采购进口产品申报份数
    @Column(columnDefinition = "INTEGER")
    private Integer governmentPurchasCount;

    //政府采购进口产品申报是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String governmentPurchasOriginal;

   // 政府采购进口产品申报是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String governmentPurchasCopy;


   // 专家论证意见份数
    @Column(columnDefinition = "INTEGER")
    private Integer expertArgumentCount;

    //专家论证意见是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String expertArgumentOriginal;

    //专家论证意见是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String expertArgumentCopy;

    //发改委意见份数
    @Column(columnDefinition = "INTEGER")
    private Integer developmentCount;

    //发改委意见是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String developementOriginal;

    //发改委意见是否复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String developementCopy;

    //文件处理表份数
    @Column(columnDefinition = "INTEGER")
    private Integer officialDisposeCount;

    //文件处理表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String officialDisposeOriginal;

    //文件处理表是否复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String officialDisposeCopy;

    //主管部门意见分数
    @Column(columnDefinition = "INTEGER")
    private Integer managerDeptCount;

    //主管部门意见是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String managerDeptOriginal;

    //主管部门意见是否复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String managerDeptCopy;


    //进口产品目录分数
    @Column(columnDefinition = "INTEGER")
    private Integer importProductCount;

    //进口产品目录是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String importProductOriginal;

    //进口产品目录是否复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String importProductCopy;
    
    //其他项目资料分数
    @Column(columnDefinition = "INTEGER")
    private Integer ortherProjectCount;
    
    //其他项目资料是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String ortherProjectOriginal;
    
    //其他项目资料是否复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String ortherProjectCopy;
    
    //设备说明书分数
    @Column(columnDefinition = "INTEGER")
    private Integer sprcialDevicesCount;

    //设备说明书是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sprcialDevicesOriginal;


    //E (进口设备) 设备说明书是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String sprcialDevicesCopy;

    //S (设备清单(国产))

    //项目申报表是否有分数
    @Column(columnDefinition = "INTEGER")
    private Integer projectDeclareCount;

    //项目申报表是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  projectDeclareOriginal;

    //项目申报表是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  projectDeclareCopy;

    //项目申请报告分数
    @Column(columnDefinition = "INTEGER")
    private Integer projectApplyReportCount;

    //项目申请报告是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  projectApplyReportOriginal;

    //项目申请报告是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  projectApplyReportCopy;

    //采购设备清单分数
    @Column(columnDefinition = "INTEGER")
    private Integer purchasDeviceCount;

    //采购设备清单是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  purchasDeviceOriginal;

    //采购设备清单是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  purchasDeviceCopy;

    //电子文档分数
    @Column(columnDefinition = "INTEGER")
    private Integer electronicDocumentCount;

    //电子文档是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  electronicDocumentOriginal;

    //电子文档是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String electronicDocumentCopy;

    //采购合同书分数
    @Column(columnDefinition = "INTEGER")
    private Integer purchasDevicePactCount;

    //采购合同书是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  purchasDevicePactOriginal;

    //采购合同书是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String purchasDevicePactCopy;

    //项目核准文件份数
    @Column(columnDefinition = "INTEGER")
    private Integer projectApproveFileCount;

    //项目核准文件是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String projectApproveFileOriginal;

    //项目核准文件是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String  projectApproveFileCopy;

    //营业执照分数
    @Column(columnDefinition = "INTEGER")
    private Integer businessLicenseCount;

    //营业执照是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String businessLicenseOriginal;

    //营业执照是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String businessLicenseCopy;

    //法人身份证分数
    @Column(columnDefinition = "INTEGER")
    private Integer legalPersoncCardCount;

    //法人身份证是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String legalPersoncCardOriginal;

    //法人身份证复印件
    @Column(columnDefinition = "VARCHAR(2)")
    private String legalPersoncCardCopy;
    
    //E (设备清单(国产))
    
    //S (项目概算)
	//环境影响评价报告分数
    @Column(columnDefinition = "INTEGER")
    private Integer environmentalEffectCount;
	
	//环境影响评价报告是否有原件
    @Column(columnDefinition = "VARCHAR(2)")
    private String environmentalEffectOriginal;
	
	//环境影响评价报告是否有复印件
    @Column(columnDefinition = "VARCHAR(2)")
	private String environmentalEffectCopy;
	
	//用地规划许可证分数
	@Column(columnDefinition = "INTEGER")
	private Integer landPlanningLicenseCount;
	
	//用地规划许可证是否有原件
	@Column(columnDefinition = "VARCHAR(2)")
	private String landPlanningLicenseOriginal;
	
	//用地规划许可证是否有复印件
	@Column(columnDefinition = "VARCHAR(2)")
	private String  landPlanningLicenseCopy;
	
	//地质勘察报告分数
	@Column(columnDefinition = "INTEGER")
	private Integer geologicalSurveyCount;
	
	//地质勘察报告是否有原件
	@Column(columnDefinition = "VARCHAR(2)")
	private String geologicalSurveyOriginal;
	
	//地质勘察报告是否有复印件
	@Column(columnDefinition = "VARCHAR(2)")
	private String geologicalSurveyCopy;
	
	//设计说明分数
	@Column(columnDefinition = "INTEGER")
	private Integer descriptionDesignCount;
	
	//设计说明是否有原件
	@Column(columnDefinition = "VARCHAR(2)")
	private String descriptionDesignOriginal;
	
	//设计说明是否有复印件
	@Column(columnDefinition = "VARCHAR(2)")
	private String descriptionDesignCopy;
	
	//工程概算书分数
	@Column(columnDefinition = "INTEGER")
	private Integer projectBudgetCount;
	
	//工程概算书是否有原件
	@Column(columnDefinition = "VARCHAR(2)")
	private String projectBudgetOriginal;
	
	
	//工程概算书是否有复印件
	@Column(columnDefinition = "VARCHAR(2)")
	private String projectBudgetCopy;
	
	//初步设计图纸分数
	@Column(columnDefinition = "INTEGER")
	private Integer preliminaryDrawingCount;
	
	//初步设计图纸是否有原件
	@Column(columnDefinition = "VARCHAR(2)")
	private String preliminaryDrawingOriginal;
	
	//初步设计图纸是否有复印件
	@Column(columnDefinition = "VARCHAR(2)")
	private String preliminaryDrawingCopy;
	
	//施工设计图纸分数
	@Column(columnDefinition = "INTEGER")
	private Integer constructionDrawingsCount;
	
	//施工设计图纸是否有原件
	@Column(columnDefinition = "VARCHAR(2)")
	private String constructionDrawingsOriginal;
	
	//施工设计图纸是否有复印件
	@Column(columnDefinition = "VARCHAR(2)")
	private String constructionDrawingsCopy;
    //E (项目概算)

    //第二负责人
    @Column(columnDefinition = "VARCHAR(64)")
    private String secondPriUser;
    
    //拟补充资料ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String suppletterid;
    
    /**
     * 默认办理类型[收文类型（评估类：PX，概算类:GX）]
     */
    @Column(columnDefinition = "VARCHAR(5)")
    private String dealOrgType;
    /**
     * 预签收日期
     */
    @Column(columnDefinition = "DATE")
    private Date presignDate;

    //工作方案
    @OneToMany(mappedBy = "sign", fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    private List<WorkProgram> workProgramList;
    
    
    //暂停项目
    @OneToMany(mappedBy="sign", fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    private List<ProjectStop> projectStopList;
    
    //发文
    @OneToOne(mappedBy = "sign", fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    private DispatchDoc dispatchDoc;

    //归档
    @OneToOne(mappedBy = "sign",fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    private FileRecord fileRecord;

    //关联下一阶段的项目
    @OneToOne(fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    @JoinTable(
            name = "cs_associate_sign",
            joinColumns = @JoinColumn(name = "signid"),
            inverseJoinColumns = @JoinColumn(name = "associate_signid"))
    private Sign associateSign;

    /**************************  是否或者 状态字段放这里  ****************************/
    //是否项目预签收
    @Column(columnDefinition = "VARCHAR(1)")
    private String ispresign;

    //警示灯状态
    @Column(columnDefinition="VARCHAR(2)")
    private String isLightUp;

    //项目状态 
    @Column(columnDefinition = "VARCHAR(2)")
    private String signState;
    
    //项目是否曾经暂停 9:表示项目曾经暂停,0:表示未暂停
    @Column(columnDefinition = "VARCHAR(2)")
    private String isProjectState;

    //进程状态(1:已发起，2:正在做工作方案，3:已完成工作方案，4:正在做发文 5:已完成发文 6:已完成发文编号 7:正在归档，8:已完成归档，9:已确认归档)
    @Column(columnDefinition = "Integer")
    private Integer processState;

	//是否有评审费用
    @Column(columnDefinition = "VARCHAR(2)")
    private String ishasreviewcost;

    //是否协审
    @Column(columnDefinition = "VARCHAR(2)")
    private String isassistproc;

    //是否协审流程
    @Column(columnDefinition = "VARCHAR(2)")
    private String isassistflow;

    //是否确认签收
    @Column(columnDefinition = "VARCHAR(2)")
    private String issign;

    //项目是否已关联,0未关联，1已关联，默认未关联
    @Column(columnDefinition = "INTEGER")
    private Integer isAssociate;

    //是否调概
    @Column(columnDefinition = "VARCHAR(5)")
    private String ischangeEstimate;

	//是否提前介入
    @Column(columnDefinition = "VARCHAR(2)")
    private String isAdvanced;

    //是否有专家评审方案
    @Column(columnDefinition = "VARCHAR(2)")
    private String hasExpertReview;
    /**************************  状态字段放这里  ****************************/
	
    public List<ProjectStop> getProjectStopList() {
		return projectStopList;
	}

	public void setProjectStopList(List<ProjectStop> projectStopList) {
		this.projectStopList = projectStopList;
	}

	public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
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

    public String getMaindepetcontactuserid() {
        return maindepetcontactuserid;
    }

    public void setMaindepetcontactuserid(String maindepetcontactuserid) {
        this.maindepetcontactuserid = maindepetcontactuserid;
    }

    public String getMainDeptUserName() {
        return mainDeptUserName;
    }

    public void setMainDeptUserName(String mainDeptUserName) {
        this.mainDeptUserName = mainDeptUserName;
    }

    public String getMainDeptContactPhone() {
        return mainDeptContactPhone;
    }

    public void setMainDeptContactPhone(String mainDeptContactPhone) {
        this.mainDeptContactPhone = mainDeptContactPhone;
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

    public String getAssistdeptcontactuserid() {
        return assistdeptcontactuserid;
    }

    public void setAssistdeptcontactuserid(String assistdeptcontactuserid) {
        this.assistdeptcontactuserid = assistdeptcontactuserid;
    }

    public String getAssistDeptUserName() {
        return assistDeptUserName;
    }

    public void setAssistDeptUserName(String assistDeptUserName) {
        this.assistDeptUserName = assistDeptUserName;
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

    public String getComprehensivehandlesug() {
        return comprehensivehandlesug;
    }

    public void setComprehensivehandlesug(String comprehensivehandlesug) {
        this.comprehensivehandlesug = comprehensivehandlesug;
    }

    public String getComprehensiveName() {
        return comprehensiveName;
    }

    public void setComprehensiveName(String comprehensiveName) {
        this.comprehensiveName = comprehensiveName;
    }

    public Date getComprehensiveDate() {
        return comprehensiveDate;
    }

    public void setComprehensiveDate(Date comprehensiveDate) {
        this.comprehensiveDate = comprehensiveDate;
    }

    public String getLeaderhandlesug() {
        return leaderhandlesug;
    }

    public void setLeaderhandlesug(String leaderhandlesug) {
        this.leaderhandlesug = leaderhandlesug;
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

    public String getMinisterhandlesug() {
        return ministerhandlesug;
    }

    public void setMinisterhandlesug(String ministerhandlesug) {
        this.ministerhandlesug = ministerhandlesug;
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

    public String getIsassistflow() {
        return isassistflow;
    }

    public void setIsassistflow(String isassistflow) {
        this.isassistflow = isassistflow;
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

    public String getIschangeEstimate() {
        return ischangeEstimate;
    }

    public void setIschangeEstimate(String ischangeEstimate) {
        this.ischangeEstimate = ischangeEstimate;
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

    public String getSignState() {
        return signState;
    }

    public void setSignState(String signState) {
        this.signState = signState;
    }

    public List<WorkProgram> getWorkProgramList() {
        return workProgramList;
    }

    public void setWorkProgramList(List<WorkProgram> workProgramList) {
        this.workProgramList = workProgramList;
    }

    public DispatchDoc getDispatchDoc() {
        return dispatchDoc;
    }

    public void setDispatchDoc(DispatchDoc dispatchDoc) {
        this.dispatchDoc = dispatchDoc;
    }

    public FileRecord getFileRecord() {
        return fileRecord;
    }

    public void setFileRecord(FileRecord fileRecord) {
        this.fileRecord = fileRecord;
    }

    public String getSuppletterid() {
		return suppletterid;
	}

	public void setSuppletterid(String suppletterid) {
		this.suppletterid = suppletterid;
	}

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Integer getIsAssociate() {
        return isAssociate;
    }

    public void setIsAssociate(Integer isAssociate) {
        this.isAssociate = isAssociate;
    }

    public Sign getAssociateSign() {
        return associateSign;
    }

    public void setAssociateSign(Sign associateSign) {
        this.associateSign = associateSign;
    }

    public String getIsAdvanced() {
        return isAdvanced;
    }

    public void setIsAdvanced(String isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    public BigDecimal getAppalyInvestment() {
        return appalyInvestment;
    }

    public void setAppalyInvestment(BigDecimal appalyInvestment) {
        this.appalyInvestment = appalyInvestment;
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

    public String getSecondPriUser() {
        return secondPriUser;
    }

    public void setSecondPriUser(String secondPriUser) {
        this.secondPriUser = secondPriUser;
    }

    public Date getPresignDate() {
        return presignDate;
    }

    public void setPresignDate(Date presignDate) {
        this.presignDate = presignDate;
    }

	public String getDealOrgType() {
		return dealOrgType;
	}

	public void setDealOrgType(String dealOrgType) {
		this.dealOrgType = dealOrgType;
	}
	
	public BigDecimal getDeclaration() {
		return declaration;
	}

	public void setDeclaration(BigDecimal declaration) {
		this.declaration = declaration;
	}

	public Integer getProcessState() {
		return processState;
	}

	public void setProcessState(Integer processState) {
		this.processState = processState;
	}

    public String getHasExpertReview() {
        return hasExpertReview;
    }

    public void setHasExpertReview(String hasExpertReview) {
        this.hasExpertReview = hasExpertReview;
    }

    public String getComprehensiveId() {
        return comprehensiveId;
    }

    public void setComprehensiveId(String comprehensiveId) {
        this.comprehensiveId = comprehensiveId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getMinisterId() {
        return ministerId;
    }

    public void setMinisterId(String ministerId) {
        this.ministerId = ministerId;
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
}