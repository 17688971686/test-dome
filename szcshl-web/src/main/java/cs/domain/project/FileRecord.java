package cs.domain.project;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;

@Entity
@Table(name = "FILERECORD")
@DynamicUpdate(true)
public class FileRecord extends DomainBase{

	//存档资料Id
	@Id
	private String fileRecordId;
		
	//项目名称
	@Column(columnDefinition="VARCHAR(200)")
	private String projectName;
	
	//档案编号
	@Column(columnDefinition="VARCHAR(50)")
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
	
	//报审登记表是否有原件0表示没有，9表示有
	@Column(columnDefinition="VARCHAR(2)")
	private String recordFormOriginal;
	
	//报审登记表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String recordFormCopy;
	
	//报审登记表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String recordFormCopyScan;
	
	//项目处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproHandleFormOriginal;
	
	//项目处理表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproHandleFormCopy;
	
	//项目处理表是否有扫描件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugproHandleFormScan;
	
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
	
	//项目负责人
	@Column(columnDefinition="VARCHAR(64)")
	private String projectChargeUser;
	
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
	@Column(columnDefinition="DATE")
	private Date fileDate;

	//收文，一对一
	@OneToOne
	@JoinColumn(name="signId")
	private Sign sign;
		
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
	
	
}