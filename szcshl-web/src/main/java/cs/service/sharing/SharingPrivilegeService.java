package cs.service.sharing;

import cs.domain.sharing.SharingPrivilege;
import cs.model.PageModelDto;
import cs.model.sharing.SharingPrivilegeDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 共享平台 业务操作接口
 * author: sjy
 * Date: 2017-7-20 18:23:08
 */
public interface SharingPrivilegeService {
    
    PageModelDto<SharingPrivilegeDto> get(ODataObj odataObj);

	void save(SharingPrivilegeDto record);

	void update(SharingPrivilegeDto record);

	SharingPrivilegeDto findById(String deptId);

	void delete(String id);

}
