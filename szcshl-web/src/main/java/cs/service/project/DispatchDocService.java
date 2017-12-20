package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.Sign;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;

public interface DispatchDocService {

    ResultMsg save(DispatchDocDto dispatchDocDto);

    Map<String, Object> initDispatchData(String signId);

    /**
     * 生成发文编号
     * @param signId
     * @param dispatchId
     * @return
     */
    ResultMsg fileNum(String signId,String dispatchId);

    DispatchDocDto initDispatchBySignId(String signId);

    ResultMsg createDisPatchTemplate (String signId);

}
