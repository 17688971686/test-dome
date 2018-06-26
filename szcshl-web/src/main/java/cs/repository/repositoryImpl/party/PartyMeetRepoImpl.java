package cs.repository.repositoryImpl.party;


import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.domain.party.PartyMeet;
import cs.domain.party.PartyMeet_;
import cs.model.PageModelDto;
import cs.model.party.PartyMeetDto;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 党务会议
 * Author: mcl
 * Date: 2018/6/12 14:47
 */
@Repository
public class PartyMeetRepoImpl extends AbstractRepository<PartyMeet, String> implements  PartyMeetRepo{

    /**
     * 获取党务会议列表
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<PartyMeetDto> findListByOData(ODataObj oDataObj) {
        PageModelDto<PartyMeetDto> pageModelDto = new PageModelDto<>();
        List<PartyMeet> partyMeetList = this.findByOdata(oDataObj);
        List<PartyMeetDto> partyMeetDtoList = new ArrayList<>();
        if(partyMeetList != null && partyMeetList.size() > 0 ){
            for(PartyMeet p : partyMeetList){
                PartyMeetDto partyMeetDto = new PartyMeetDto();
                BeanCopierUtils.copyPropertiesIgnoreNull(p , partyMeetDto);
                partyMeetDtoList.add(partyMeetDto);
            }
        }
        pageModelDto.setCount(oDataObj.getCount());
        pageModelDto.setValue(partyMeetDtoList);

        return pageModelDto;
    }

    @Override
    public ResultMsg createPartyMeet(PartyMeetDto partyMeetDto) {
        PartyMeet partyMeet = new PartyMeet();
        BeanCopierUtils.copyPropertiesIgnoreNull(partyMeetDto , partyMeet);
        partyMeet.setmId(UUID.randomUUID().toString());
        Date now = new Date();
        partyMeet.setCreatedBy(SessionUtil.getDisplayName());
        partyMeet.setCreatedDate(now);
        partyMeet.setModifiedBy(SessionUtil.getDisplayName());
        partyMeet.setModifiedDate(now);
        save(partyMeet);
        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "操作成功" , partyMeet.getmId());
    }


    /**
     * 通过id获取会议信息
     * @param mId
     * @return
     */
    @Override
    public PartyMeetDto findMeetById(String mId) {

        PartyMeetDto partyMeetDto = new PartyMeetDto();

        PartyMeet partyMeet = this.findById(PartyMeet_.mId.getName() , mId);

        BeanCopierUtils.copyPropertiesIgnoreNull(partyMeet , partyMeetDto);

        return partyMeetDto;
    }

    /**
     * 更新会议信息
     * @param partyMeetDto
     * @return
     */
    @Override
    public ResultMsg updatePartyMeet(PartyMeetDto partyMeetDto) {

        PartyMeet partyMeet = this.findById(PartyMeet_.mId.getName() , partyMeetDto.getmId());
        BeanCopierUtils.copyPropertiesIgnoreNull(partyMeetDto , partyMeet);
        partyMeet.setModifiedBy(SessionUtil.getDisplayName());
        partyMeet.setModifiedDate(new Date());
        save(partyMeet);
        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "操作成功！" , null);
    }

    /**
     * 删除会议信息
     * @param id
     * @return
     */
    @Override
    public ResultMsg deletePartyMeet(String id) {
        this.deleteById(PartyMeet_.mId.getName() , id);
        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "删除成功！" , null);
    }
}