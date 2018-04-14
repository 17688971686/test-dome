package cs.controller.party;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.odata.ODataObj;
import cs.service.party.PartyManagerService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * Description: 党务管理控制层
 * Author: mcl
 * Date: 2018/3/8 11:12
 */
@Controller
@RequestMapping(name = "党务管理" , path = "partyManager")
@MudoleAnnotation(name = "党务管理",value = "permission#partyManager")
public class PartyManagerController {

    private String ctrlName = "party";

    @Autowired
    private PartyManagerService partyManagerService;

    @RequiresAuthentication
    @RequestMapping(name="查询党员信息列表" , path = "findByOData" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PartyManagerDto> findByOData(HttpServletRequest request){
        ODataObj oDataObj = null;
        try {
            oDataObj = new ODataObj(request);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PageModelDto<PartyManagerDto> pageModelDto = partyManagerService.findByOData(oDataObj);
        return pageModelDto;

    }

    @RequiresAuthentication
    @RequestMapping(name="通过ID查询党员信息" , path = "findById" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findById(@RequestParam String pmId){
        return partyManagerService.findPartyById(pmId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存党员信息" , path = "createParty" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg createParty(@RequestBody PartyManagerDto partyManagerDto){
        return partyManagerService.createParty(partyManagerDto);
    }

    @RequiresPermissions("partyManager#html/partyList#get")
    @RequestMapping(name = "党员信息录入页" , path = "html/partyEdit" , method = RequestMethod.GET)
    public String edit(){
        return ctrlName + "/partyEdit";
    }

    @RequiresPermissions("partyManager#html/partyList#get")
    @RequestMapping(name = "党员信息列表" , path = "html/partyList" , method = RequestMethod.GET)
    public String partyList(){
        return ctrlName + "/partyList";
    }

    @RequiresPermissions("partyManager#html/addPartyMeeting#get")
    @RequestMapping(name = "党员会议添加" , path = "html/addPartyMeeting" , method = RequestMethod.GET )
    public String partyMeeting(){
        return ctrlName + "/addPartyMeeting";
    }
}