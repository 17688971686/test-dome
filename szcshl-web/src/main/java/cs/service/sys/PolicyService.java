package cs.service.sys;


import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.PolicyDto;
import cs.repository.odata.ODataObj;

/**
 * Created by zsl
 * 2018/7/27
 */
public interface PolicyService {

    PageModelDto<PolicyDto> get(ODataObj odataObj);

    ResultMsg save(PolicyDto record);
}
