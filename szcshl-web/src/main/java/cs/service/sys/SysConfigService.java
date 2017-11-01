package cs.service.sys;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.SysConfigDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Description: 系统参数 业务操作接口
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
public interface SysConfigService {

    PageModelDto<SysConfigDto> get(ODataObj odataObj);

    ResultMsg save(SysConfigDto record);

    SysConfigDto findById(String id);

    void delete(String id);

    List<SysConfigDto> queryAll();

    SysConfigDto findByKey(String key);
}
