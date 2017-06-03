package cs.service.sys;

import java.util.List;

import cs.model.PageModelDto;
import cs.model.sys.CompanyDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

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
	public List<UserDto> getUser(ODataObj odataObj);
	List<CompanyDto> getCompany(ODataObj odataObj);
	OrgDto findById(String id);
	List<OrgDto> findUserChargeOrg();
	List<OrgDto> listAll();
}