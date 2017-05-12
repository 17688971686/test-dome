package cs.service.sys;

import cs.model.PageModelDto;
import cs.model.sys.RoleDto;
import cs.repository.odata.ODataObj;

public interface RoleService {

	PageModelDto<RoleDto> get(ODataObj odataObj);
	void createRole(RoleDto roleDto);
	void updateRole(RoleDto roleDto);
	void deleteRole(String id);
	void deleteRoles(String[] ids);
	RoleDto findById(String roleId);
}