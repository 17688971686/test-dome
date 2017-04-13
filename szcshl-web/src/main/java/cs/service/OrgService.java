package cs.service;

import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.UserDto;
import cs.repository.odata.ODataObj;

public interface OrgService {

	PageModelDto<OrgDto> get(ODataObj odataObj);
	void createOrg(OrgDto orgDto);
	void updateOrg(OrgDto orgDto);
	void deleteOrg(String id) ;
	void deleteOrgs(String[] ids);
	PageModelDto<UserDto> getOrgUsers(String id) ;
	PageModelDto<UserDto> getUsersNotInOrg(String id,ODataObj oDataObj);
	void addUserToOrg(String userId,String orgId);
	void removeOrgUser(String userId, String orgId);
	void removeOrgUsers(String[] userIds, String orgId);
}