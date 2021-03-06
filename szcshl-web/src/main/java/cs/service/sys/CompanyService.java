package cs.service.sys;

import java.util.List;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.project.UnitScoreDto;
import cs.model.sys.CompanyDto;
import cs.repository.odata.ODataObj;

public interface CompanyService {

	PageModelDto<CompanyDto> get(ODataObj odataObj);

	void createCompany(CompanyDto companyDto);

	void deleteCompany(String id);

	void deleteCompanys(String[] ids);
	
	void updateCompany(CompanyDto companyDto);

	ResultMsg updateUnitScore(UnitScoreDto unitScoreDto);

	CompanyDto findByIdCompany(String id);

	List<CompanyDto> findCompanys();

	/**
	 * 项目添加建设单位和编制单位
	 * @param name
	 * @param comType
	 */
	void createSignCompany(String name,String comType,boolean isSignUser);

}
