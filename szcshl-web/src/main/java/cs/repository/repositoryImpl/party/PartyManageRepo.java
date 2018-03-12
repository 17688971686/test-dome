package cs.repository.repositoryImpl.party;

import cs.common.ResultMsg;
import cs.domain.party.PartyManager;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

/**
 * Created by MCL
 * 2018/3/11
 */

public interface PartyManageRepo extends IRepository<PartyManager, String> {
    /**
     * 保存党员信息
     * @param partyManagerDto
     * @return
     */
    ResultMsg creteParty(PartyManagerDto partyManagerDto);


    /**
     * 通过OData查询数据列表
     * @param oDataObj
     * @return
     */
    PageModelDto<PartyManagerDto> findByOData(ODataObj oDataObj);

    /**
     * 通过Id查询党员信息
     * @param pmId
     * @return
     */
   ResultMsg findPartyById(String pmId);
}
