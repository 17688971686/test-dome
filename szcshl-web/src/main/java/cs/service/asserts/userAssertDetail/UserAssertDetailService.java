package cs.service.asserts.userAssertDetail;

import cs.model.PageModelDto;
import cs.model.asserts.userAssertDetail.UserAssertDetailDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 用户资产明细 业务操作接口
 * author: zsl
 * Date: 2017-9-20 15:35:02
 */
public interface UserAssertDetailService {
    
    PageModelDto<UserAssertDetailDto> get(ODataObj odataObj);

	void save(UserAssertDetailDto record);

	void update(UserAssertDetailDto record);

	UserAssertDetailDto findById(String deptId);

	void delete(String id);

}
