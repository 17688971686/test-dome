package cs.service.party;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.party.PartyMeetDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.party.PartyMeetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 党务会议
 * Author: mcl
 * Date: 2018/6/12 14:59
 */
@Service
public class PartyMeetServiceImpl  implements  PartyMeetService{

    @Autowired
    private PartyMeetRepo partyMeetRepo;

    /**
     * 获取党务列表
     * @return
     */
    @Override
    public PageModelDto<PartyMeetDto> findListByOData(ODataObj oDataObj) {
        return partyMeetRepo.findListByOData(oDataObj);
    }

    @Override
    public ResultMsg createPartyMeet(PartyMeetDto partyMeetDto) {
        return partyMeetRepo.createPartyMeet(partyMeetDto);
    }


    /**
     * 通过ID获取会议信息
     * @param mId
     * @return
     */
    @Override
    public PartyMeetDto findMeetById(String mId) {
        return partyMeetRepo.findMeetById(mId);
    }


    /**
     * 更新会议信息
     * @param partyMeetDto
     * @return
     */
    @Override
    public ResultMsg updatePartyMeet(PartyMeetDto partyMeetDto) {
        return partyMeetRepo.updatePartyMeet(partyMeetDto);
    }


    /**
     * 删除会议信息
     * @param id
     * @return
     */
    @Override
    public ResultMsg deletePartyMeet(String id) {
        return partyMeetRepo.deletePartyMeet(id);
    }
}