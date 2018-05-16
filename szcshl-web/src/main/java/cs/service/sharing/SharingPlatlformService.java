package cs.service.sharing;

import cs.model.PageModelDto;
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

import java.util.Map;

/**
 * Description: 共享平台 业务操作接口
 * author: sjy
 * Date: 2017-7-11 10:40:17
 */
public interface SharingPlatlformService {

    PageModelDto<SharingPlatlformDto> get(ODataObj odataObj);

    PageModelDto<SharingPlatlformDto> findByCurUser(ODataObj odataObj);

    void save(SharingPlatlformDto record);

    void update(SharingPlatlformDto record);

    SharingPlatlformDto findById(String deptId);

    void delete(String id);

    void updatePublishStatus(String ids,String status);

    UserDto findUser(String loginName);

    /**
     * 查询用户部门信息
     * @param isInCludeGroup  是否包含组
     * @return
     */
    Map<String, Object> initOrgAndUser(boolean isInCludeGroup);

    PageModelDto<SharingPlatlformDto> findByReception(ODataObj odataObj);
}
