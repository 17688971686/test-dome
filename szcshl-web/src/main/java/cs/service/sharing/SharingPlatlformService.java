package cs.service.sharing;

import cs.domain.sharing.SharingPlatlform;
import cs.model.PageModelDto;
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 共享平台 业务操作接口
 * author: sjy
 * Date: 2017-7-11 10:40:17
 */
public interface SharingPlatlformService {
    
    PageModelDto<SharingPlatlformDto> get(ODataObj odataObj);

	void save(SharingPlatlformDto record);

	void update(SharingPlatlformDto record);

	SharingPlatlformDto findById(String deptId);

	void delete(String id);

	void deletes(String[] ids);

	SharingPlatlformDto postSharingAritle(String id);

	SharingPlatlformDto nextSharingArticle(String id);

	void updatePublishStatus(SharingPlatlformDto record);

	UserDto findUser(String loginName);

}
