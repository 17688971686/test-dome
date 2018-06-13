package cs.repository.repositoryImpl.party;

import cs.common.ResultMsg;
import cs.domain.party.PartyMeet;
import cs.model.PageModelDto;
import cs.model.party.PartyMeetDto;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

/**
 * Description: 党务会议
 * Author: mcl
 * Date: 2018/6/12 14:44
 */

public interface PartyMeetRepo extends IRepository<PartyMeet, String> {

    /**
     * 获取党务会议列表
     * @param oDataObj
     * @return
     */
    PageModelDto<PartyMeetDto> findListByOData(ODataObj oDataObj);

    /**
     * 新增会议
     * @param partyMeetDto
     * @return
     */
    ResultMsg createPartyMeet(PartyMeetDto partyMeetDto);

    /**
     * 通过id获取会议信息
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