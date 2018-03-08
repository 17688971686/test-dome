package cs.service.project;

import cs.common.ResultMsg;
import cs.model.project.DispatchDocDto;

import java.util.Map;

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

    /**
     * 生成发文模板
     * @param signId
     * @return
     */
    ResultMsg createDisPatchTemplate (String signId);

}
