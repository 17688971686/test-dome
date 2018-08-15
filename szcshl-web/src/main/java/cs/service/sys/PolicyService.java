package cs.service.sys;


import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.PolicyDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by zsl
 * 2018/7/27
 */
public interface PolicyService {

    PageModelDto<PolicyDto> get(ODataObj odataObj);

    ResultMsg save(PolicyDto record);

    /**
     * 初始化政策指标库的所有文件
     * @return
     */
    List<PolicyDto> initFileFolder();

    /**
     * 通过id获取文件
     * @param oDataObj
     * @param standardId
     * @return
     */
    PageModelDto<PolicyDto> findFileById(ODataObj oDataObj , String standardId);
}
