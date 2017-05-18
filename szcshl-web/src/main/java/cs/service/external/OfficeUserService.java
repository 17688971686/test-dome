package cs.service.external;

import java.util.List;

import cs.domain.external.OfficeUser;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

public interface OfficeUserService {

	PageModelDto<OfficeUserDto> get(ODataObj odataObj);

	void save(OfficeUserDto record);

	void update(OfficeUserDto record);

	OfficeUserDto findById(String officeId);

	void delete(String id);
	void deletes(String [] ids);
	
	List<OfficeUserDto> officeUserList();

	List<DeptDto> getDepts();
	List<OfficeUserDto> findOfficeUserByDeptId(String deptId);
}
