package cs.domain.project;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;

@Entity
@Table(name = "cs_file_record")
@DynamicUpdate(true)
public class FileRecord extends DomainBase{

	//存档资料Id
	@Id
	private String fileRecordId;
		
	//项目名称
	@Column(columnDefinition="VARCHAR(200)")
	private String projectName;
	
	//评审阶段
	@Column(columnDefinition="VARCHAR(200)")
	private String fileReviewstage;
	
	/**
     * 存档类型（评估类、资金申请报告、其他类：PD，概算类：GD，设备类：SD）
     */
    @Column(columnDefinition = "VARCHAR(8)")
    private String fileType;

    /**
     * 存档顺序号（每年从1开始递增）
     */
    @Column(columnDefinition = "INTEGER")
    private Integer fileSeq;
	
	//档案编号
	@Column(columnDefinition="VARCHAR(16)")
	private String fileNo;
	
	//项目单位
	@Column(columnDefinition="VARCHAR(100)")
	private String projectCompany;
	
	//项目代码
	@Column(columnDefinition="VARCHAR(30)")
	private String projectCode;
	
	//文件标题
	@Column(columnDefinition="VARCHAR(100)")
	private String fileTitle;
	
	//文号
	@Column(columnDefinition="VARCHAR(100)")
	private String fileNumber;
	
	//是否曾经暂停项目
	@Column(columnDefinition="VARCHAR(4)")
	private String isStachProject;
	
	//是否有补充资料
	@Column(columnDefinition="VARCHAR(4)")
	private String isSupplementary;
	
	//报审登记表份数
	@Column(columnDefinition="INTEGER")
	private Integer recordFormCount;
	
	//报审登记表是否有原件0表示没有，9表示有
	@Column(columnDefinition="VARCHAR(2)")
	private String recordFormOriginal;
	
	//报审登记表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String recordFormCopy;
	
	//报审登记表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String recordFormCopyScan;
	
	//项目处理表分数
	@Column(columnDefinition="INTEGER")
	private Integer sugproHandleFormCount;
	
	//项目处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproHandleFormOriginal;
	
	//项目处理表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproHandleFormCopy;
	
	//项目处理表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproHandleFormScan;
	
	//委文件处理表份数
	@Column(columnDefinition="INTEGER")
	private Integer sugfileHandleFormCount;
	
	//委文件处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugfileHandleFormOriginal;
	
	//委文件处理表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugfileHandleFormCopy;
	
	//委文件处理表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugfileHandleFormScan;
	
	//项目单位申报表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproCompanyFormOriginal;
	
	//项目单位申报表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproCompanyFormCopy;
	
	//项目单位申报表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproCompanyFormScan;
	
	//项目单位申报函是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproComletterOriginal;
	
	//项目单位申报函是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproComletterCopy;
	
	//项目单位申报函是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproComletterScan;
	
	//暂停申请表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String pauseFormOriginal;
	
	//暂停申请表是否复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String pauseFormCopy;
	
	//暂停申请表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String pauseFormScan;
	
	//暂停申请表数量
	@Column(columnDefinition="INTEGER")
	private Integer pauseFormCount;
	
	//补充资料函是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileLetterOriginal;
	
	//补充资料函是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileLetterCopy;
	
	//补充资料函是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileLetterScan;
	
	//补充资料函份数
	@Column(columnDefinition="INTEGER")
	private Integer supplyFileLetterCount;
	
	//暂缓审核函是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String slowReviewOriginal;
	
	//暂缓审核函是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String slowReviewCopy;
	
	//暂缓审核函是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String slowReviewScan;
	
	//暂缓审核函份数
	@Column(columnDefinition="INTEGER")
	private Integer slowReviewCount;
	
	//补充资料清单是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileListOriginal;
	
	//补充资料清单是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileListScan;
	
	//补充资料清单是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileListCopy;
	
	//补充资料清单份数
	@Column(columnDefinition="INTEGER")
	private Integer supplyFileListCount;
	
	//电子光盘份数
	@Column(columnDefinition="INTEGER")
	private Integer electronicDiskCount;
	
	//相关会议纪要是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String meetingSummaryOriginal;
	
	//相关会议纪要是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String meetingSummaryScan;
	
	//其他重要资料分数
	@Column(columnDefinition="VARCHAR(2)")
	private Integer otherImportFileCount;
	
	//其他重要资料是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String otherImportFileOriginal;
	
	//其他重要资料是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String otherImportFileCopy;
	
	//项目建议书是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String procsugFileOriginal;
	
	//项目建议书数量
	@Column(columnDefinition="INTEGER")
	private Integer procsugFileCount;
	
	//项目建议书是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String procsugFileEl;
	
	//补充资料是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileOriginal;
	
	//补充资料数量
	@Column(columnDefinition="INTEGER")
	private Integer supplyFileCount;
	
	//补充资料是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String supplyFileEl;
	
	//评审工作方案是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String planOriginal;
	
	//评审工作方案是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String planCopy;
	
	//评审工作方案是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String planScan;
	
	//评审工作方案份数
	@Column(columnDefinition="INTEGER")
	private Integer planCount;
	
	//评审会专家意见是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertOriginal;
	
	//评审会专家意见是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertCopy;
	
	//评审会专家意见是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertScan;
	
	//评审会专家意见份数
	@Column(columnDefinition="INTEGER")
	private Integer expertCount;
	
	//会议签到表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String meetingSignOriginal;
	
	//会议签到表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String meetingSignCopy;
	
	//会议签到表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String meetingScan;
	
	//会议签到表份数
	@Column(columnDefinition="INTEGER")
	private Integer meetingSignCount;
	
	//专家个人意见分数
	@Column(columnDefinition="INTEGER")
	private Integer expertAmanCount;
	
	//专家个人意见原件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertAmanOriginal;
	
	//专家个人意见附件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertAmanCopy;
	
	//专家个人意见扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertAmanScan;
	
	//评审费发放表分数
	@Column(columnDefinition="INTEGER")
	private Integer stageCostCount;
	
	//评审费发放原件
	@Column(columnDefinition="VARCHAR(2)")
	private String stageCostOriginal;
	
	//评审费发放附件
	@Column(columnDefinition="VARCHAR(2)")
	private String stageCostCopy;
	
	//评审费发放扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String stageCostScan;
	
	//项目第一负责人
	@Column(columnDefinition="VARCHAR(64)")
	private String projectChargeUser;
	
	//项目第二负责人
	@Column(columnDefinition="VARCHAR(64)")
	private String projectTwoUser;
	
	//表格打印日期
	@Column(columnDefinition="DATE")
	private Date printDate;
	
	//档案签收员Id
	@Column(columnDefinition="VARCHAR(64)")
	private String signUserid;
	
	//档案签收员名称
	@Column(columnDefinition="VARCHAR(64)")
	private String signUserName;
		
	//存档日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date fileDate;

	/**
	 * 发送存档日期为第二负责人审批意见后的日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	@JSONField(format = "yyyy-MM-dd")
	private Date sendStoreDate;

	/**
	 * 纸质文件接受日期 ：为归档员陈春燕确认的归档日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	@JSONField(format = "yyyy-MM-dd")
	private Date pageDate;

	//S （进口设备）
	//政府采购进口产品申报份数
	@Column(columnDefinition="INTEGER")
	private Integer governmentPurchasCount;

	//政府采购进口产品申报是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String governmentPurchasOriginal;

	//政府采购进口产品申报是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String governmentPurchasCopy;

	//专家论证意见份数
	@Column(columnDefinition="INTEGER")
	private Integer expertArgumentCount;

	//专家论证意见是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertArgumentOriginal;

	//专家论证意见是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertArgumentCopy;

	//发改委意见份数
	@Column(columnDefinition="INTEGER")
	private Integer developmentCount;

	//发改委意见是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String developementOriginal;

	//发改委意见是否复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String developementCopy;

	//文件处理表份数
	@Column(columnDefinition="INTEGER")
	private Integer officialDisposeCount;

	//文件处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String officialDisposeOriginal;

	//文件处理表是否复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String officialDisposeCopy;

	//主管部门意见分数
	@Column(columnDefinition="INTEGER")
	private Integer managerDeptCount;

	//主管部门意见是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String managerDeptOriginal;

	//主管部门意见是否复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String managerDeptCopy;

	//进口产品目录分数
	@Column(columnDefinition="INTEGER")
	private Integer importProductCount;

	//进口产品目录原件
	@Column(columnDefinition="VARCHAR(2)")
	private String importProductOriginal;

	//进口产品目录是否复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String importProductCopy;

	//设备说明书分数
	@Column(columnDefinition="INTEGER")
	private Integer sprcialDevicesCount;

	//设备说明书是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sprcialDevicesOriginal;

	//E（进口设备）
	//设备说明书是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sprcialDevicesCopy;
	
	//S 设备清单（国产）
	//项目申报表是否有分数
	@Column(columnDefinition="INTEGER")
	private Integer projectDeclareCount;

	//项目申报表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectDeclareOriginal;

	//项目申报表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectDeclareCopy;

	//可研报告分数
	@Column(columnDefinition="INTEGER")
	private Integer feasibilityReportCount;
	
	//可研报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String feasibilityReportOriginal;
	
	//可研报告是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String feasibilityReportCopy;

	//电子文档分数
	@Column(columnDefinition="INTEGER")
	private Integer electronicDocumentCount;

	//电子文档是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String electronicDocumentOriginal;

	//电子文档是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String electronicDocumentCopy;

	//项目申请报告分数
	@Column(columnDefinition="INTEGER")
	private Integer projectApplyReportCount;

	//项目申请报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectApplyReportOriginal;

	//项目申请报告是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectApplyReportCopy;

	//申报登记表分数
	@Column(columnDefinition="INTEGER")
	private Integer declareRegisterCount;
	
	//申报登记表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String declareRegisterOriginal;
	
	//申报登记表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String declareRegisterCopy;

	//采购设备清单分数
	@Column(columnDefinition="INTEGER")
	private Integer purchasDeviceCount;

	//采购设备清单是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String purchasDeviceOriginal;

	//采购设备清单是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String purchasDeviceCopy;

	//采购合同书分数
	@Column(columnDefinition="INTEGER")
	private Integer purchasDevicePactCount;

	//采购合同书是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String purchasDevicePactOriginal;

	//采购合同书是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String purchasDevicePactCopy;

	//项目核准文件份数
	@Column(columnDefinition="INTEGER")
	private Integer projectApproveFileCount;

	//项目核准文件是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectApproveFileOriginal;

	//项目核准文件是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectApproveFileCopy;

	//营业执照分数
	@Column(columnDefinition="INTEGER")
	private Integer businessLicenseCount;

	//营业执照是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String businessLicenseOriginal;

	//营业执照是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String businessLicenseCopy;

	//法人身份证分数
	@Column(columnDefinition="INTEGER")
	private Integer legalPersoncCardCount;

	//法人身份证是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String legalPersoncCardOriginal;

	//法人身份证复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String legalPersoncCardCopy;
	
	//E 设备清单（国产）
	
	//环境影响评价报告是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String environmentalEffectCopy;

	//用地规划许可证是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String landPlanningLicenseCopy;
	
	//项目审核明细表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectDetailOriginal;
	
	//项目审核明细表是否有复印件/扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectDetailCopy;
	
	//审定工程概算书是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String judgemenProjectOriginal;
	
	//审定工程概算书是否有复印件/扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String judgemenProjectCopy;
	
	//环评批复文件是否复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String environmentalCopy;
	
	//环评批复文件是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String environmentalRmb;
	
	//规划选址批文是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String planningCopy;
	
	//归还选址批文是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String PlanningRmb;
	
	//节能报告分数
	@Column(columnDefinition="INTEGER")
	private Integer energyCount;
	
	//节能报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String energyOriginal;
	
	//节能报告是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String energyCopy;
	
	//S 项目概算
	//项目申报基本情况表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectBaseOriginal;
	
	//项目申报基本情况表是否有电子版/扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectBaseCopy;
	
	//项目指标分析表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectTargetOriginal;
	
	//项目指标分析表是否有电子版/扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectTargetCopy;
	
	//工程量计算书是否原件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectComputeOriginal;
	
	//工程量计算书是否电子版/扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String projectComputeCopy;
		
	//沟通会签到表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String communicateMeetingOriginal;
	
	//沟通会签到表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String communicateMeetingCopy;
	
	//调概项目转评审请示是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String probableTurnStageOriginal;
	
	//调概项目转评审请示是否复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String probableTurnStageCopy;
	
	//调概项目转评审请示是否扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String probableTurnStageRmb;
	
	//专家审核概算书
	@Column(columnDefinition="INTEGER")
	private Integer expertBookCount;
	
	//专家审核概算书是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertBookCountOriginal;
	
	//专家审核概算书是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertBookCountCopy;
	
	//专家审核概算书是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String expertBookCountScan;
	
	//1.地址勘察报告分数
	@Column(columnDefinition="INTEGER")
	private Integer addressInvestigateCount;
	
	//地址勘察报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String addressInvestigateOriginal;
	
	//地址勘察报告是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String addressInvestigateEpub;

	//2.设计说明分数
	@Column(columnDefinition="INTEGER")
	private Integer designDescriptionCount;
	
	//设计说明是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String designDescriptionOriginal;
	
	//设计说明是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String designDescriptionEpub;

	//申报工程概算书份数
	@Column(columnDefinition="INTEGER")
	private Integer projectBudgetCount;
	
	//申报工程概算书是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	 private String projectBudgetOriginal;
	 
	//申报工程概算书是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String projectBudgetEpub;

	//初步设计图纸分数
	@Column(columnDefinition="INTEGER")
	private Integer preliminaryDesignCount;
	
	//初步设计图纸是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String preliminaryDesignOriginal;
	
	//初步设计图纸是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String preliminaryDesignEpub;

	//施工设计图纸份数
	@Column(columnDefinition="INTEGER")
	private Integer constructionDesignCount;
	
	//施工设计图纸是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String constructionDesignOriginal;
	
	//施工设计图纸是否有电子版
	@Column(columnDefinition="VARCHAR(2)")
	private String constructionDesignEpub;

	//6.项目电子光盘
	@Column(columnDefinition="INTEGER")
	private Integer electronicCount;

	//7.建筑
	@Column(columnDefinition="VARCHAR(2)")
	private String architecture;
	
	//8.结构
	@Column(columnDefinition="VARCHAR(2)")
	private String construction;
	
	//9.给排水
	@Column(columnDefinition="VARCHAR(2)")
	private String drainAwayWater;

	//10.空调
	@Column(columnDefinition="VARCHAR(2)")
	private String conditioner;
	
	//11.强电
	@Column(columnDefinition="VARCHAR(2)")
	private String highVoltage;
	
	//12.弱电
	@Column(columnDefinition="VARCHAR(2)")
	private String lightCurrent;
	
	//13.道路
	@Column(columnDefinition="VARCHAR(2)")
	private String roadway;
	
	//14.桥梁
	@Column(columnDefinition="VARCHAR(2)")
	private String bridge;
	
	//15.园林
	@Column(columnDefinition="VARCHAR(2)")
	private String gardens;
	
	//16.消防
	@Column(columnDefinition="VARCHAR(2)")
	private String fireFighting;
	
	//17.燃气
	@Column(columnDefinition="VARCHAR(2)")
	private String combustiblegas;

	//18.其他
	@Column(columnDefinition="VARCHAR(2)")
	private String fieOther;
	//E 项目概算

	
	//收文，一对一
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="signId",unique = true)
	private Sign sign;
		
	public Integer getAddressInvestigateCount() {
		return addressInvestigateCount;
	}

	public void setAddressInvestigateCount(Integer addressInvestigateCount) {
		this.addressInvestigateCount = addressInvestigateCount;
	}

	public String getAddressInvestigateOriginal() {
		return addressInvestigateOriginal;
	}

	public void setAddressInvestigateOriginal(String addressInvestigateOriginal) {
		this.addressInvestigateOriginal = addressInvestigateOriginal;
	}

	public String getAddressInvestigateEpub() {
		return addressInvestigateEpub;
	}

	public void setAddressInvestigateEpub(String addressInvestigateEpub) {
		this.addressInvestigateEpub = addressInvestigateEpub;
	}

	public Integer getDesignDescriptionCount() {
		return designDescriptionCount;
	}

	public void setDesignDescriptionCount(Integer designDescriptionCount) {
		this.designDescriptionCount = designDescriptionCount;
	}

	public String getDesignDescriptionOriginal() {
		return designDescriptionOriginal;
	}

	public void setDesignDescriptionOriginal(String designDescriptionOriginal) {
		this.designDescriptionOriginal = designDescriptionOriginal;
	}

	public String getDesignDescriptionEpub() {
		return designDescriptionEpub;
	}

	public void setDesignDescriptionEpub(String designDescriptionEpub) {
		this.designDescriptionEpub = designDescriptionEpub;
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

	public String getProjectBudgetEpub() {
		return projectBudgetEpub;
	}

	public void setProjectBudgetEpub(String projectBudgetEpub) {
		this.projectBudgetEpub = projectBudgetEpub;
	}

	public Integer getPreliminaryDesignCount() {
		return preliminaryDesignCount;
	}

	public void setPreliminaryDesignCount(Integer preliminaryDesignCount) {
		this.preliminaryDesignCount = preliminaryDesignCount;
	}

	public String getPreliminaryDesignOriginal() {
		return preliminaryDesignOriginal;
	}

	public void setPreliminaryDesignOriginal(String preliminaryDesignOriginal) {
		this.preliminaryDesignOriginal = preliminaryDesignOriginal;
	}

	public String getPreliminaryDesignEpub() {
		return preliminaryDesignEpub;
	}

	public void setPreliminaryDesignEpub(String preliminaryDesignEpub) {
		this.preliminaryDesignEpub = preliminaryDesignEpub;
	}

	public Integer getConstructionDesignCount() {
		return constructionDesignCount;
	}

	public void setConstructionDesignCount(Integer constructionDesignCount) {
		this.constructionDesignCount = constructionDesignCount;
	}

	public String getConstructionDesignOriginal() {
		return constructionDesignOriginal;
	}

	public void setConstructionDesignOriginal(String constructionDesignOriginal) {
		this.constructionDesignOriginal = constructionDesignOriginal;
	}

	public String getConstructionDesignEpub() {
		return constructionDesignEpub;
	}

	public void setConstructionDesignEpub(String constructionDesignEpub) {
		this.constructionDesignEpub = constructionDesignEpub;
	}

	public Integer getElectronicCount() {
		return electronicCount;
	}

	public void setElectronicCount(Integer electronicCount) {
		this.electronicCount = electronicCount;
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public String getDrainAwayWater() {
		return drainAwayWater;
	}

	public void setDrainAwayWater(String drainAwayWater) {
		this.drainAwayWater = drainAwayWater;
	}

	public String getConditioner() {
		return conditioner;
	}

	public void setConditioner(String conditioner) {
		this.conditioner = conditioner;
	}

	public String getHighVoltage() {
		return highVoltage;
	}

	public void setHighVoltage(String highVoltage) {
		this.highVoltage = highVoltage;
	}

	public String getLightCurrent() {
		return lightCurrent;
	}

	public void setLightCurrent(String lightCurrent) {
		this.lightCurrent = lightCurrent;
	}

	public String getRoadway() {
		return roadway;
	}

	public void setRoadway(String roadway) {
		this.roadway = roadway;
	}

	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public String getGardens() {
		return gardens;
	}

	public void setGardens(String gardens) {
		this.gardens = gardens;
	}

	public String getFireFighting() {
		return fireFighting;
	}

	public void setFireFighting(String fireFighting) {
		this.fireFighting = fireFighting;
	}

	public String getCombustiblegas() {
		return combustiblegas;
	}

	public void setCombustiblegas(String combustiblegas) {
		this.combustiblegas = combustiblegas;
	}

	public String getFieOther() {
		return fieOther;
	}

	public void setFieOther(String fieOther) {
		this.fieOther = fieOther;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public String getFileRecordId() {
		return fileRecordId;
	}

	public void setFileRecordId(String fileRecordId) {
		this.fileRecordId = fileRecordId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getProjectCompany() {
		return projectCompany;
	}

	public void setProjectCompany(String projectCompany) {
		this.projectCompany = projectCompany;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getRecordFormOriginal() {
		return recordFormOriginal;
	}

	public void setRecordFormOriginal(String recordFormOriginal) {
		this.recordFormOriginal = recordFormOriginal;
	}

	public String getRecordFormCopy() {
		return recordFormCopy;
	}

	public void setRecordFormCopy(String recordFormCopy) {
		this.recordFormCopy = recordFormCopy;
	}

	public String getRecordFormCopyScan() {
		return recordFormCopyScan;
	}

	public void setRecordFormCopyScan(String recordFormCopyScan) {
		this.recordFormCopyScan = recordFormCopyScan;
	}

	public String getSugproHandleFormOriginal() {
		return sugproHandleFormOriginal;
	}

	public void setSugproHandleFormOriginal(String sugproHandleFormOriginal) {
		this.sugproHandleFormOriginal = sugproHandleFormOriginal;
	}

	public String getSugproHandleFormCopy() {
		return sugproHandleFormCopy;
	}

	public void setSugproHandleFormCopy(String sugproHandleFormCopy) {
		this.sugproHandleFormCopy = sugproHandleFormCopy;
	}

	public String getSugproHandleFormScan() {
		return sugproHandleFormScan;
	}

	public void setSugproHandleFormScan(String sugproHandleFormScan) {
		this.sugproHandleFormScan = sugproHandleFormScan;
	}

	public String getSugfileHandleFormOriginal() {
		return sugfileHandleFormOriginal;
	}

	public void setSugfileHandleFormOriginal(String sugfileHandleFormOriginal) {
		this.sugfileHandleFormOriginal = sugfileHandleFormOriginal;
	}

	public String getSugfileHandleFormCopy() {
		return sugfileHandleFormCopy;
	}

	public void setSugfileHandleFormCopy(String sugfileHandleFormCopy) {
		this.sugfileHandleFormCopy = sugfileHandleFormCopy;
	}

	public String getSugfileHandleFormScan() {
		return sugfileHandleFormScan;
	}

	public void setSugfileHandleFormScan(String sugfileHandleFormScan) {
		this.sugfileHandleFormScan = sugfileHandleFormScan;
	}

	public String getSugproCompanyFormOriginal() {
		return sugproCompanyFormOriginal;
	}

	public void setSugproCompanyFormOriginal(String sugproCompanyFormOriginal) {
		this.sugproCompanyFormOriginal = sugproCompanyFormOriginal;
	}

	public String getSugproCompanyFormCopy() {
		return sugproCompanyFormCopy;
	}

	public void setSugproCompanyFormCopy(String sugproCompanyFormCopy) {
		this.sugproCompanyFormCopy = sugproCompanyFormCopy;
	}

	public String getSugproCompanyFormScan() {
		return sugproCompanyFormScan;
	}

	public void setSugproCompanyFormScan(String sugproCompanyFormScan) {
		this.sugproCompanyFormScan = sugproCompanyFormScan;
	}

	public String getSugproComletterOriginal() {
		return sugproComletterOriginal;
	}

	public void setSugproComletterOriginal(String sugproComletterOriginal) {
		this.sugproComletterOriginal = sugproComletterOriginal;
	}

	public String getSugproComletterCopy() {
		return sugproComletterCopy;
	}

	public void setSugproComletterCopy(String sugproComletterCopy) {
		this.sugproComletterCopy = sugproComletterCopy;
	}

	public String getSugproComletterScan() {
		return sugproComletterScan;
	}

	public void setSugproComletterScan(String sugproComletterScan) {
		this.sugproComletterScan = sugproComletterScan;
	}

	public String getPauseFormOriginal() {
		return pauseFormOriginal;
	}

	public void setPauseFormOriginal(String pauseFormOriginal) {
		this.pauseFormOriginal = pauseFormOriginal;
	}

	public String getPauseFormCopy() {
		return pauseFormCopy;
	}

	public void setPauseFormCopy(String pauseFormCopy) {
		this.pauseFormCopy = pauseFormCopy;
	}

	public String getPauseFormScan() {
		return pauseFormScan;
	}

	public void setPauseFormScan(String pauseFormScan) {
		this.pauseFormScan = pauseFormScan;
	}

	public Integer getPauseFormCount() {
		return pauseFormCount;
	}

	public void setPauseFormCount(Integer pauseFormCount) {
		this.pauseFormCount = pauseFormCount;
	}

	public String getSupplyFileLetterOriginal() {
		return supplyFileLetterOriginal;
	}

	public void setSupplyFileLetterOriginal(String supplyFileLetterOriginal) {
		this.supplyFileLetterOriginal = supplyFileLetterOriginal;
	}

	public String getSupplyFileLetterCopy() {
		return supplyFileLetterCopy;
	}

	public void setSupplyFileLetterCopy(String supplyFileLetterCopy) {
		this.supplyFileLetterCopy = supplyFileLetterCopy;
	}

	public String getSupplyFileLetterScan() {
		return supplyFileLetterScan;
	}

	public void setSupplyFileLetterScan(String supplyFileLetterScan) {
		this.supplyFileLetterScan = supplyFileLetterScan;
	}

	public Integer getSupplyFileLetterCount() {
		return supplyFileLetterCount;
	}

	public void setSupplyFileLetterCount(Integer supplyFileLetterCount) {
		this.supplyFileLetterCount = supplyFileLetterCount;
	}

	public String getSlowReviewOriginal() {
		return slowReviewOriginal;
	}

	public void setSlowReviewOriginal(String slowReviewOriginal) {
		this.slowReviewOriginal = slowReviewOriginal;
	}

	public String getSlowReviewCopy() {
		return slowReviewCopy;
	}

	public void setSlowReviewCopy(String slowReviewCopy) {
		this.slowReviewCopy = slowReviewCopy;
	}

	public String getSlowReviewScan() {
		return slowReviewScan;
	}

	public void setSlowReviewScan(String slowReviewScan) {
		this.slowReviewScan = slowReviewScan;
	}

	public Integer getSlowReviewCount() {
		return slowReviewCount;
	}

	public void setSlowReviewCount(Integer slowReviewCount) {
		this.slowReviewCount = slowReviewCount;
	}

	public String getSupplyFileListOriginal() {
		return supplyFileListOriginal;
	}

	public void setSupplyFileListOriginal(String supplyFileListOriginal) {
		this.supplyFileListOriginal = supplyFileListOriginal;
	}

	public String getSupplyFileListScan() {
		return supplyFileListScan;
	}

	public void setSupplyFileListScan(String supplyFileListScan) {
		this.supplyFileListScan = supplyFileListScan;
	}

	public String getSupplyFileListCopy() {
		return supplyFileListCopy;
	}

	public void setSupplyFileListCopy(String supplyFileListCopy) {
		this.supplyFileListCopy = supplyFileListCopy;
	}

	public Integer getSupplyFileListCount() {
		return supplyFileListCount;
	}

	public void setSupplyFileListCount(Integer supplyFileListCount) {
		this.supplyFileListCount = supplyFileListCount;
	}

	public Integer getElectronicDiskCount() {
		return electronicDiskCount;
	}

	public void setElectronicDiskCount(Integer electronicDiskCount) {
		this.electronicDiskCount = electronicDiskCount;
	}

	public String getMeetingSummaryOriginal() {
		return meetingSummaryOriginal;
	}

	public void setMeetingSummaryOriginal(String meetingSummaryOriginal) {
		this.meetingSummaryOriginal = meetingSummaryOriginal;
	}

	public String getMeetingSummaryScan() {
		return meetingSummaryScan;
	}

	public void setMeetingSummaryScan(String meetingSummaryScan) {
		this.meetingSummaryScan = meetingSummaryScan;
	}

	public String getOtherImportFileOriginal() {
		return otherImportFileOriginal;
	}

	public void setOtherImportFileOriginal(String otherImportFileOriginal) {
		this.otherImportFileOriginal = otherImportFileOriginal;
	}

	public String getOtherImportFileCopy() {
		return otherImportFileCopy;
	}

	public void setOtherImportFileCopy(String otherImportFileCopy) {
		this.otherImportFileCopy = otherImportFileCopy;
	}

	public String getProcsugFileOriginal() {
		return procsugFileOriginal;
	}

	public void setProcsugFileOriginal(String procsugFileOriginal) {
		this.procsugFileOriginal = procsugFileOriginal;
	}

	public Integer getProcsugFileCount() {
		return procsugFileCount;
	}

	public void setProcsugFileCount(Integer procsugFileCount) {
		this.procsugFileCount = procsugFileCount;
	}

	public String getProcsugFileEl() {
		return procsugFileEl;
	}

	public void setProcsugFileEl(String procsugFileEl) {
		this.procsugFileEl = procsugFileEl;
	}

	public String getSupplyFileOriginal() {
		return supplyFileOriginal;
	}

	public void setSupplyFileOriginal(String supplyFileOriginal) {
		this.supplyFileOriginal = supplyFileOriginal;
	}

	public Integer getSupplyFileCount() {
		return supplyFileCount;
	}

	public void setSupplyFileCount(Integer supplyFileCount) {
		this.supplyFileCount = supplyFileCount;
	}

	public String getSupplyFileEl() {
		return supplyFileEl;
	}

	public void setSupplyFileEl(String supplyFileEl) {
		this.supplyFileEl = supplyFileEl;
	}

	public String getPlanOriginal() {
		return planOriginal;
	}

	public void setPlanOriginal(String planOriginal) {
		this.planOriginal = planOriginal;
	}

	public String getPlanCopy() {
		return planCopy;
	}

	public void setPlanCopy(String planCopy) {
		this.planCopy = planCopy;
	}

	public String getPlanScan() {
		return planScan;
	}

	public void setPlanScan(String planScan) {
		this.planScan = planScan;
	}

	public Integer getPlanCount() {
		return planCount;
	}

	public void setPlanCount(Integer planCount) {
		this.planCount = planCount;
	}

	public String getExpertOriginal() {
		return expertOriginal;
	}

	public void setExpertOriginal(String expertOriginal) {
		this.expertOriginal = expertOriginal;
	}

	public String getExpertCopy() {
		return expertCopy;
	}

	public void setExpertCopy(String expertCopy) {
		this.expertCopy = expertCopy;
	}

	public String getExpertScan() {
		return expertScan;
	}

	public void setExpertScan(String expertScan) {
		this.expertScan = expertScan;
	}

	public Integer getExpertCount() {
		return expertCount;
	}

	public void setExpertCount(Integer expertCount) {
		this.expertCount = expertCount;
	}

	public String getMeetingSignOriginal() {
		return meetingSignOriginal;
	}

	public void setMeetingSignOriginal(String meetingSignOriginal) {
		this.meetingSignOriginal = meetingSignOriginal;
	}

	public String getMeetingSignCopy() {
		return meetingSignCopy;
	}

	public void setMeetingSignCopy(String meetingSignCopy) {
		this.meetingSignCopy = meetingSignCopy;
	}

	public String getMeetingScan() {
		return meetingScan;
	}

	public void setMeetingScan(String meetingScan) {
		this.meetingScan = meetingScan;
	}

	public Integer getMeetingSignCount() {
		return meetingSignCount;
	}

	public void setMeetingSignCount(Integer meetingSignCount) {
		this.meetingSignCount = meetingSignCount;
	}	

	public String getProjectChargeUser() {
		return projectChargeUser;
	}

	public void setProjectChargeUser(String projectChargeUser) {
		this.projectChargeUser = projectChargeUser;
	}

	public String getSignUserName() {
		return signUserName;
	}

	public void setSignUserName(String signUserName) {
		this.signUserName = signUserName;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String getSignUserid() {
		return signUserid;
	}

	public void setSignUserid(String signUserid) {
		this.signUserid = signUserid;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public Integer getExpertAmanCount() {
		return expertAmanCount;
	}

	public void setExpertAmanCount(Integer expertAmanCount) {
		this.expertAmanCount = expertAmanCount;
	}

	public String getExpertAmanOriginal() {
		return expertAmanOriginal;
	}

	public void setExpertAmanOriginal(String expertAmanOriginal) {
		this.expertAmanOriginal = expertAmanOriginal;
	}

	public String getExpertAmanCopy() {
		return expertAmanCopy;
	}

	public void setExpertAmanCopy(String expertAmanCopy) {
		this.expertAmanCopy = expertAmanCopy;
	}

	public String getExpertAmanScan() {
		return expertAmanScan;
	}

	public void setExpertAmanScan(String expertAmanScan) {
		this.expertAmanScan = expertAmanScan;
	}

	public Integer getStageCostCount() {
		return stageCostCount;
	}

	public void setStageCostCount(Integer stageCostCount) {
		this.stageCostCount = stageCostCount;
	}

	public String getStageCostOriginal() {
		return stageCostOriginal;
	}

	public void setStageCostOriginal(String stageCostOriginal) {
		this.stageCostOriginal = stageCostOriginal;
	}

	public String getStageCostCopy() {
		return stageCostCopy;
	}

	public void setStageCostCopy(String stageCostCopy) {
		this.stageCostCopy = stageCostCopy;
	}

	public String getStageCostScan() {
		return stageCostScan;
	}

	public void setStageCostScan(String stageCostScan) {
		this.stageCostScan = stageCostScan;
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

	public Integer getSugproHandleFormCount() {
		return sugproHandleFormCount;
	}

	public void setSugproHandleFormCount(Integer sugproHandleFormCount) {
		this.sugproHandleFormCount = sugproHandleFormCount;
	}

    public Integer getOtherImportFileCount() {
        return otherImportFileCount;
    }

    public void setOtherImportFileCount(Integer otherImportFileCount) {
        this.otherImportFileCount = otherImportFileCount;
    }

    public String getFileReviewstage() {
		return fileReviewstage;
	}

	public void setFileReviewstage(String fileReviewstage) {
		this.fileReviewstage = fileReviewstage;
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

	public Integer getFeasibilityReportCount() {
		return feasibilityReportCount;
	}

	public void setFeasibilityReportCount(Integer feasibilityReportCount) {
		this.feasibilityReportCount = feasibilityReportCount;
	}

	public String getFeasibilityReportOriginal() {
		return feasibilityReportOriginal;
	}

	public void setFeasibilityReportOriginal(String feasibilityReportOriginal) {
		this.feasibilityReportOriginal = feasibilityReportOriginal;
	}

	public String getFeasibilityReportCopy() {
		return feasibilityReportCopy;
	}

	public void setFeasibilityReportCopy(String feasibilityReportCopy) {
		this.feasibilityReportCopy = feasibilityReportCopy;
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

	public Integer getDeclareRegisterCount() {
		return declareRegisterCount;
	}

	public void setDeclareRegisterCount(Integer declareRegisterCount) {
		this.declareRegisterCount = declareRegisterCount;
	}

	public String getDeclareRegisterOriginal() {
		return declareRegisterOriginal;
	}

	public void setDeclareRegisterOriginal(String declareRegisterOriginal) {
		this.declareRegisterOriginal = declareRegisterOriginal;
	}

	public String getDeclareRegisterCopy() {
		return declareRegisterCopy;
	}

	public void setDeclareRegisterCopy(String declareRegisterCopy) {
		this.declareRegisterCopy = declareRegisterCopy;
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

	public Integer getSugfileHandleFormCount() {
		return sugfileHandleFormCount;
	}

	public void setSugfileHandleFormCount(Integer sugfileHandleFormCount) {
		this.sugfileHandleFormCount = sugfileHandleFormCount;
	}

	public String getEnvironmentalEffectCopy() {
		return environmentalEffectCopy;
	}

	public void setEnvironmentalEffectCopy(String environmentalEffectCopy) {
		this.environmentalEffectCopy = environmentalEffectCopy;
	}

	public String getLandPlanningLicenseCopy() {
		return landPlanningLicenseCopy;
	}

	public void setLandPlanningLicenseCopy(String landPlanningLicenseCopy) {
		this.landPlanningLicenseCopy = landPlanningLicenseCopy;
	}

	public String getProjectDetailOriginal() {
		return projectDetailOriginal;
	}

	public void setProjectDetailOriginal(String projectDetailOriginal) {
		this.projectDetailOriginal = projectDetailOriginal;
	}

	public String getProjectDetailCopy() {
		return projectDetailCopy;
	}

	public void setProjectDetailCopy(String projectDetailCopy) {
		this.projectDetailCopy = projectDetailCopy;
	}

	public String getJudgemenProjectOriginal() {
		return judgemenProjectOriginal;
	}

	public void setJudgemenProjectOriginal(String judgemenProjectOriginal) {
		this.judgemenProjectOriginal = judgemenProjectOriginal;
	}

	public String getJudgemenProjectCopy() {
		return judgemenProjectCopy;
	}

	public void setJudgemenProjectCopy(String judgemenProjectCopy) {
		this.judgemenProjectCopy = judgemenProjectCopy;
	}

	public String getEnvironmentalCopy() {
		return environmentalCopy;
	}

	public void setEnvironmentalCopy(String environmentalCopy) {
		this.environmentalCopy = environmentalCopy;
	}

	public String getEnvironmentalRmb() {
		return environmentalRmb;
	}

	public void setEnvironmentalRmb(String environmentalRmb) {
		this.environmentalRmb = environmentalRmb;
	}

	public String getPlanningCopy() {
		return planningCopy;
	}

	public void setPlanningCopy(String planningCopy) {
		this.planningCopy = planningCopy;
	}

	public String getPlanningRmb() {
		return PlanningRmb;
	}

	public void setPlanningRmb(String planningRmb) {
		PlanningRmb = planningRmb;
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

	public Integer getExpertBookCount() {
		return expertBookCount;
	}

	public void setExpertBookCount(Integer expertBookCount) {
		this.expertBookCount = expertBookCount;
	}

	public String getExpertBookCountOriginal() {
		return expertBookCountOriginal;
	}

	public void setExpertBookCountOriginal(String expertBookCountOriginal) {
		this.expertBookCountOriginal = expertBookCountOriginal;
	}

	public String getExpertBookCountCopy() {
		return expertBookCountCopy;
	}

	public void setExpertBookCountCopy(String expertBookCountCopy) {
		this.expertBookCountCopy = expertBookCountCopy;
	}

	public String getExpertBookCountScan() {
		return expertBookCountScan;
	}

	public void setExpertBookCountScan(String expertBookCountScan) {
		this.expertBookCountScan = expertBookCountScan;
	}

	public String getProbableTurnStageOriginal() {
		return probableTurnStageOriginal;
	}

	public void setProbableTurnStageOriginal(String probableTurnStageOriginal) {
		this.probableTurnStageOriginal = probableTurnStageOriginal;
	}

	public String getProbableTurnStageCopy() {
		return probableTurnStageCopy;
	}

	public void setProbableTurnStageCopy(String probableTurnStageCopy) {
		this.probableTurnStageCopy = probableTurnStageCopy;
	}

	public String getProbableTurnStageRmb() {
		return probableTurnStageRmb;
	}

	public void setProbableTurnStageRmb(String probableTurnStageRmb) {
		this.probableTurnStageRmb = probableTurnStageRmb;
	}

	public String getCommunicateMeetingOriginal() {
		return communicateMeetingOriginal;
	}

	public void setCommunicateMeetingOriginal(String communicateMeetingOriginal) {
		this.communicateMeetingOriginal = communicateMeetingOriginal;
	}

	public String getCommunicateMeetingCopy() {
		return communicateMeetingCopy;
	}

	public void setCommunicateMeetingCopy(String communicateMeetingCopy) {
		this.communicateMeetingCopy = communicateMeetingCopy;
	}

	public String getProjectBaseOriginal() {
		return projectBaseOriginal;
	}

	public void setProjectBaseOriginal(String projectBaseOriginal) {
		this.projectBaseOriginal = projectBaseOriginal;
	}

	public String getProjectBaseCopy() {
		return projectBaseCopy;
	}

	public void setProjectBaseCopy(String projectBaseCopy) {
		this.projectBaseCopy = projectBaseCopy;
	}

	public String getProjectTargetOriginal() {
		return projectTargetOriginal;
	}

	public void setProjectTargetOriginal(String projectTargetOriginal) {
		this.projectTargetOriginal = projectTargetOriginal;
	}

	public String getProjectTargetCopy() {
		return projectTargetCopy;
	}

	public void setProjectTargetCopy(String projectTargetCopy) {
		this.projectTargetCopy = projectTargetCopy;
	}

	public String getProjectComputeOriginal() {
		return projectComputeOriginal;
	}

	public void setProjectComputeOriginal(String projectComputeOriginal) {
		this.projectComputeOriginal = projectComputeOriginal;
	}

	public String getProjectComputeCopy() {
		return projectComputeCopy;
	}

	public void setProjectComputeCopy(String projectComputeCopy) {
		this.projectComputeCopy = projectComputeCopy;
	}

	public Integer getRecordFormCount() {
		return recordFormCount;
	}

	public void setRecordFormCount(Integer recordFormCount) {
		this.recordFormCount = recordFormCount;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(Integer fileSeq) {
		this.fileSeq = fileSeq;
	}

	public String getProjectTwoUser() {
		return projectTwoUser;
	}

	public void setProjectTwoUser(String projectTwoUser) {
		this.projectTwoUser = projectTwoUser;
	}

	public String getIsStachProject() {
		return isStachProject;
	}

	public void setIsStachProject(String isStachProject) {
		this.isStachProject = isStachProject;
	}

	public String getIsSupplementary() {
		return isSupplementary;
	}

	public void setIsSupplementary(String isSupplementary) {
		this.isSupplementary = isSupplementary;
	}

	public Date getSendStoreDate() {
		return sendStoreDate;
	}

	public void setSendStoreDate(Date sendStoreDate) {
		this.sendStoreDate = sendStoreDate;
	}

	public Date getPageDate() {
		return pageDate;
	}

	public void setPageDate(Date pageDate) {
		this.pageDate = pageDate;
	}
}