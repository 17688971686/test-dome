package cs.service.party;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.party.PartyMeetDto;
import cs.repository.odata.ODataObj;

/**
 * Created by MCL
 * 2018/6/12
 */
public interface PartyMeetService {

    /**
     * 获取党务列表
     * @return
     */
    PageModelDto<PartyMeetDto> findListByOData(ODataObj oDataObj);

    /**
     * 保存会议
     * @param partyMeetDto
     * @return
     */
    ResultMsg createPartyMeet(PartyMeetDto partyMeetDto);

    /**
     * 通过ID获取会议信息
     * @param mId
     * @return
     */
    PartyMeetDto findMeetById(String mId);

    /**
     * 更新会议信息
     * @param partyMeetDto
     * @return
     */
    ResultMsg updatePartyMeet(PartyMeetDto partyMeetDto);

    /**
     * 删除会议信息
     * @param id
     * @return
     */
    ResultMsg deletePartyMeet(String id);
}
