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

    List<SignDto> getSignForMerge(SignDto signDto, String dispatchId);

    void mergeDispa(String signId, String mainBusinessId, String linkSignId) throws Exception;

    List<SignDto> getSeleSignByMainBusiId(String mainBussnessId);

    ResultMsg fileNum(String dispatchId);

    DispatchDocDto initDispatchBySignId(String signId);

    void deleteMergeDispa(String mainBusinessId, String removeSignIds);

}
