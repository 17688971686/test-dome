package cs.service.external;

import java.util.List;

import cs.domain.external.OfficeUser;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.repository.odata.ODataObj;

public interface DeptService {

	PageModelDto<DeptDto> get(ODataObj odataObj);

	void save(DeptDto record);

	void update(DeptDto record);

	DeptDto findById(String deptId);

	void delete(String id);

//	PageModelDto<OfficeUserDto> getOfficeUsers(String officeID, ODataObj odataObj);

	PageModelDto<OfficeUserDto> getDeptOfficeUsers(String officeId);

	void addOfficeUserToDept(String deptId, String officeId);

	void removeOfficeUserDepts(String[] ids, String deptId);

	void removeOfficeUserDept(String officeId, String deptId);

	PageModelDto<OfficeUserDto> getOfficeUsersNotInDept(String deptId, ODataObj odataObj);
	
	
}
