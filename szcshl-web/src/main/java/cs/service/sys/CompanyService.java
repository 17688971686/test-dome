package cs.service.sys;

import java.util.List;

import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.sys.CompanyDto;
import cs.repository.odata.ODataObj;

public interface CompanyService {

	PageModelDto<CompanyDto> get(ODataObj odataObj);

	void createCompany(CompanyDto companyDto);

	void deleteCompany(String id);

	void deleteCompanys(String[] ids);
	
	void updateCompany(CompanyDto companyDto);

	CompanyDto findByIdCompany(String id);

	List<CompanyDto> findCompanys();
}
