package cs.service.party;

import cs.common.ResultMsg;
import cs.domain.party.PartyManager;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.odata.ODataObj;

import java.util.List;

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

    /**
     * 更新党员信息
     * @param partyManagerDto
     * @return
     */
    ResultMsg updateParty(PartyManagerDto partyManagerDto);

    /**
     * 删除党员信息
     * @param pmId
     */
    void deleteParty(String pmId);

    /**
     * 批量保存
     * @param partyManagerList
     */
    void batchSave(List<PartyManager> partyManagerList);

    /**
     * 通过身份证号判断该党员是否已经存在
     * @param pmIDCard
     * @return
     */
    Boolean existByIdCar(String pmIDCard);

    /**
     * 保存党务信息
     * @param partyManager
     */
    void saveParty(PartyManager partyManager);
}
