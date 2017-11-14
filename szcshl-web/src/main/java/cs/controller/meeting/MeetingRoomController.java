package cs.controller.meeting;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.sys.CompanyDto;
import cs.repository.odata.ODataObj;
import cs.service.meeting.MeetingRoomService;

import java.util.List;

@Controller
@RequestMapping(name = "会议室", path = "meeting")
@MudoleAnnotation(name = "会议室管理",value = "permission#meeting")
public class MeetingRoomController {

    private String ctrlName = "meeting";

    @Autowired
    private MeetingRoomService meetingRoomService;

    @RequiresAuthentication
    //@RequiresPermissions("meeting#fingByOData#post")
    @RequestMapping(name = "获取会议室数据", path = "fingByOData", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<MeetingRoomDto> get(HttpServletRequest request) throws java.text.ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MeetingRoomDto> meetingDtos = meetingRoomService.get(odataObj);
        return meetingDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("meeting#html/findByIdMeeting#get")
    @RequestMapping(name = "根据ID获取会议室", path = "html/findByIdMeeting", method = RequestMethod.GET)
    public @ResponseBody
    MeetingRoomDto findByIdMeeting(@RequestParam(required = true) String id) {
        MeetingRoomDto meetingDto = meetingRoomService.findByIdMeeting(id);
        return meetingDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据ID获取会议室", path = "findAll", method = RequestMethod.POST)
    @ResponseBody
    public List<MeetingRoomDto> findAll() {
        return  meetingRoomService.findAll();
    }

    //begin#html
    @RequiresPermissions("meeting#html/list#get")
    @RequestMapping(name = "会议室管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresAuthentication
    //@RequiresPermissions("meeting#html/edit#get")
    @RequestMapping(name = "会议室编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
    // end#html

    @RequiresAuthentication
    //@RequiresPermissions("meeting##post")
    @RequestMapping(name = "创建会议室", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody MeetingRoomDto meetingDto) {
        return meetingRoomService.createMeeting(meetingDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("meeting##post")
    @RequestMapping(name = "更新会议室", path = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg put(@RequestBody MeetingRoomDto meetingDto) {
       return meetingRoomService.updateMeeting(meetingDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("meeting##post")
    @RequestMapping(name = "更新会议室使用状态", path = "roomUseState", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void roomUseState(@RequestBody MeetingRoomDto meetingDto) {
        meetingRoomService.roomUseState(meetingDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("meeting##delete")
    @RequestMapping(name = "删除会议室", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam String id) {

        return meetingRoomService.deleteMeeting(id);
    }

}
