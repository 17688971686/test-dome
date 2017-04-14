package cs.service;

import cs.model.CompanyDto;
import cs.model.MeetingRoomDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

public interface CompanyService {

	PageModelDto<CompanyDto> get(ODataObj odataObj);

	void createMeeting(CompanyDto companyDto);

	void deleteMeeting(String id);

	void deleteMeeting(String[] ids);
	
	void updateMeeting(CompanyDto companyDto);
}
