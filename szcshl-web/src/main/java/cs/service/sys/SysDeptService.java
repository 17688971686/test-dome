package cs.service.sys;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.SysDeptDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

public interface SysDeptService {

    PageModelDto<SysDeptDto> get(ODataObj odataObj);

	ResultMsg save(SysDeptDto dept);

    SysDeptDto findById(String id);

    ResultMsg deleteSysDept(String id);

    PageModelDto<UserDto> getSysDeptUsers(String id);

    PageModelDto<UserDto> getUserNotInSysDept(String id);

    void removeSysDeptUser(String id, String userId);

    void addUserToSysDept(String id, String userId);
}