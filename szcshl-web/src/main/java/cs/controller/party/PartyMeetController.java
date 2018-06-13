package cs.controller.party;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.party.PartyMeetDto;
import cs.repository.odata.ODataObj;
import cs.service.party.PartyMeetService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 党务会议
 * Author: mcl
 * Date: 2018/6/12 14:20
 */
@Controller
@RequestMapping(name = "党务会议" , path = "partyMeet")
@MudoleAnnotation(name = "党务管理",value = "permission#partyManager")
public class PartyMeetController {

    @Autowired
    private PartyMeetService partyMeetService;

    private String ctrlName = "party";

    @RequiresAuthentication
    @RequestMapping(name = "查询党务会议列表" , path = "findByDataList" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PartyMeetDto> findByDataList(HttpServletRequest request){
        ODataObj oDataObj = null;
        try {
            oDataObj = new ODataObj(request);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return partyMeetService.findListByOData(oDataObj);
    }

    @RequiresAuthentication
    @RequestMapping(name="保存党务会议" , path ="createPartyMeet" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg createPartyMeet(@RequestBody PartyMeetDto partyMeetDto){
        return partyMeetService.createPartyMeet(partyMeetDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过id查询会议信息" , path = "findMeetById" , method = RequestMethod.POST)
    @ResponseBody
    public PartyMeetDto findMeetById(@RequestParam String mId){
        return partyMeetService.findMeetById(mId);
    }

    @RequiresAuthentication
    @RequestMapping(name="更新党务会议" , path ="updatePartyMeet" , method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg updatePartyMeet(@RequestBody PartyMeetDto partyMeetDto){
        return partyMeetService.updatePartyMeet(partyMeetDto);
    }


    @RequiresAuthentication
    @RequestMapping(name="删除党务会议" , path ="deletePartyMeet" , method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deletePartyMeet(@RequestParam String mId){
        return partyMeetService.deletePartyMeet(mId);
    }


    @RequiresPermissions("partyMeet#html/partyMeetList#get")
    @RequestMapping(name = "党务会议列表" , path = "html/partyMeetList" , method = RequestMethod.GET)
    public String partyMeetList(){
        return ctrlName + "/partyMeetList";
    }

    @RequiresPermissions("partyMeet#html/addPartyMeeting#get")
    @RequestMapping(name = "党员会议添加" , path = "html/addPartyMeeting" , method = RequestMethod.GET )
    public String partyMeeting(){
        return ctrlName + "/addPartyMeeting";
    }

    @RequiresPermissions("partyMeet#html/partyMeetDetail#get")
    @RequestMapping(name = "党员会议详情页" , path = "html/partyMeetDetail" , method = RequestMethod.GET )
    public String partyMeetDetail(){
        return ctrlName + "/partyMeetDetail";
    }
}