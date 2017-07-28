package cs.service.sharing;

import cs.model.PageModelDto;
import cs.model.sharing.SharingPrivilegeDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 共享平台 业务操作接口
 * author: sjy
 * Date: 2017-7-20 18:23:08
 */
public interface SharingPrivilegeService {


    boolean deleteByShareId(String shareId);

    boolean bathDeleteByShareId(String shareIds);
}
