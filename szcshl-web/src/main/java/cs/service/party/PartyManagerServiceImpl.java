package cs.service.party;

import cs.common.ResultMsg;
import cs.domain.party.PartyManager;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.party.PartyManageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Description:
 * Author: mcl
 * Date: 2018/3/11 21:00
 */
@Service
public class PartyManagerServiceImpl implements  PartyManagerService {

    @Autowired
    private PartyManageRepo partyManagerRepo;
    /**
     * 保存党员信息
     * @param partyManagerDto
     * @return
     */
    @Override
    public ResultMsg createParty(PartyManagerDto partyManagerDto) {
        return partyManagerRepo.creteParty(partyManagerDto);
    }

    /**
     * 通过OData查询数据列表
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<PartyManagerDto> findByOData(ODataObj oDataObj) {
        return partyManagerRepo.findByOData(oDataObj);
    }

    /**
     * 通过ID获取党员信息
     * @param pmId
     * @return
     */
    @Override
    public ResultMsg findPartyById(String pmId) {
        return partyManagerRepo.findPartyById(pmId);
    }

    /**
     * 更新党员信息
     * @param partyManagerDto
     * @return
     */
    @Override
    public ResultMsg updateParty(PartyManagerDto partyManagerDto) {
        return partyManagerRepo.updateParty(partyManagerDto);
    }

    /**
     * 删除党员信息
     * @param pmId
     */
    @Override
    public void deleteParty(String pmId) {
        partyManagerRepo.deleteParty(pmId);
    }

    /**
     * 批量保存
     * @param partyManagerList
     */
    @Override
    public void batchSave(List<PartyManager> partyManagerList) {
        partyManagerRepo.bathUpdate(partyManagerList);
    }


    /**
     * 通过身份证号判断该党员是否已经存在
     * @param pmIDCard
     * @return
     */
    @Override
    public Boolean existByIdCar(String pmIDCard) {
        return partyManagerRepo.existByIdCar(pmIDCard);
    }

    @Override
    @Transactional
    public void saveParty(PartyManager partyManager) {
        partyManagerRepo.save(partyManager);
    }
}