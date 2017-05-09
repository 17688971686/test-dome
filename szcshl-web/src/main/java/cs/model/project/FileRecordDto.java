package cs.model.project;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import cs.model.BaseDto;

public class FileRecordDto extends BaseDto{

	private String fileRecordId;

	private String signId;
	
	private String projectName;
	
	private String fileNo;
	
	private String projectCompany;
	
	private String projectCode;
	
	private String fileTitle;
	
	private String fileNumber;
	
	private String recordFormOriginal;
	
	private String recordFormCopy;
	
	private String recordFormCopyScan;
	
	private String sugproHandleFormOriginal;
	
	private String sugproHandleFormCopy;
	
	private String sugproHandleFormScan;
	
	private String sugfileHandleFormOriginal;
	
	private String sugfileHandleFormCopy;
	
	private String sugfileHandleFormScan;
	
	private String sugproCompanyFormOriginal;
	
	private String sugproCompanyFormCopy;
	
	private String sugproCompanyFormScan;
	
	private String sugproComletterOriginal;
	
	private String sugproComletterCopy;
		 
	private String sugproComletterScan;
		 
	private String pauseFormOriginal;
		 
	private String pauseFormCopy;	
	 
	private String pauseFormScan;
		 
	private Integer pauseFormCount;	
	 
	private String supplyFileLetterOriginal;	
	 
	private String supplyFileLetterCopy;	
	 
	private String supplyFileLetterScan;	
	 
	private Integer supplyFileLetterCount;	
	 
	private String slowReviewOriginal;	
	 
	private String slowReviewCopy;
		 
	private String slowReviewScan;
		 
	private Integer slowReviewCount;
		 
	private String supplyFileListOriginal;
		 
	private String supplyFileListScan;	
	 
	private String supplyFileListCopy;	
	 
	private Integer supplyFileListCount;	
	 
	private Integer electronicDiskCount;
	 
	private String meetingSummaryOriginal;
		 
	private String meetingSummaryScan;
		 
	private String otherImportFileOriginal;
		 
	private String otherImportFileCopy;
	 
	private String procsugFileOriginal;
		 
	private Integer procsugFileCount;
		 
	private String procsugFileEl;
		 
	private String supplyFileOriginal;
	 
	private Integer supplyFileCount;	
	 
	private String supplyFileEl;
		 
	private String planOriginal;
		 
	private String planCopy;
		 
	private String planScan;
		 
	private Integer planCount;
		 
	private String expertOriginal;
		 
	private String expertCopy;	
	 
	private String expertScan;	
	 
	private Integer expertCount;	
	 
	private String meetingSignOriginal;
	 
	private String meetingSignCopy;	
	 
	private String meetingScan;	
	 
	private Integer meetingSignCount;
	
	private String projectChargeUserid;
	
	private Date printDate;
	
	private String signUserid;
	
	private Date fileDate;

	public String getFileRecordId() {
		return fileRecordId;
	}

	public void setFileRecordId(String fileRecordId) {
		this.fileRecordId = fileRecordId;
	}

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
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

	public String getProjectChargeUserid() {
		return projectChargeUserid;
	}

	public void setProjectChargeUserid(String projectChargeUserid) {
		this.projectChargeUserid = projectChargeUserid;
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
	
}