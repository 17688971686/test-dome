package cs.service.sys;

import cs.model.PageModelDto;
import cs.model.sys.CompanyDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

import java.util.List;

public interface OrgService {

	PageModelDto<OrgDto> get(ODataObj odataObj);
	void createOrg(OrgDto orgDto);
	void updateOrg(OrgDto orgDto);
	void deleteOrg(String id) ;
	PageModelDto<UserDto> getOrgUsers(String id) ;
	PageModelDto<UserDto> getUsersNotInOrg(String id,ODataObj oDataObj);
	void addUserToOrg(String userId,String orgId);
	void removeOrgUser(String userId, String orgId);
	void removeOrgUsers(String[] userIds, String orgId);
	List<CompanyDto> getCompany(ODataObj odataObj);
	OrgDto findById(String id);
	List<OrgDto> listAll();
}