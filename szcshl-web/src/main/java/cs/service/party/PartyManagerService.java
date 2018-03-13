package cs.service.party;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.odata.ODataObj;

/**
 * Created by MCL
 * 2018/3/11
 */
public interface PartyManagerService {
    /**
     * 保存党员信息
     * @param partyManagerDto
     * @return
     */
    ResultMsg createParty(PartyManagerDto partyManagerDto);

    /**
     * 通过OData查询数据列表
     * @param oDataObj
     * @return
     */
    PageModelDto<PartyManagerDto> findByOData(ODataObj oDataObj);

    /**
     * 通过ID获取党员信息
     * @param pmId
     * @return
     */
    ResultMsg findPartyById(String pmId);
}
