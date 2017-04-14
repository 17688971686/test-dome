package cs.service;

import cs.model.CompanyDto;
import cs.model.MeetingRoomDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

public interface CompanyService {

	PageModelDto<CompanyDto> get(ODataObj odataObj);

	void createCompany(CompanyDto companyDto);

	void deleteCompany(String id);

	void deleteCompany(String[] ids);
	
	void updateCompany(CompanyDto companyDto);
}
