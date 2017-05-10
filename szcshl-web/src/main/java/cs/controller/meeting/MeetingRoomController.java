package cs.controller.meeting;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.repository.odata.ODataObj;
import cs.service.meeting.MeetingRoomService;

@Controller
@RequestMapping(name ="会议室",path = "meeting")
public class MeetingRoomController {

	private String ctrlName = "meeting";
	
	@Autowired
	private MeetingRoomService meetingRoomService;
	
	@RequiresPermissions("meeting#fingByOData#post")
	@RequestMapping(name="获取会议室数据",path = "fingByOData",method =RequestMethod.POST)
	public @ResponseBody PageModelDto<MeetingRoomDto> get( HttpServletRequest request ) throws java.text.ParseException {
	
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<MeetingRoomDto> meetingDtos= meetingRoomService.get(odataObj);
		return  meetingDtos;
	}
	//begin#html
	@RequiresPermissions("meeting#html/list#get")
	@RequestMapping(name = "会议室列表", path = "html/list" ,method = RequestMethod.GET)
	public String list(){
		
		return ctrlName +"/list";
	}
	@RequiresPermissions("meeting#html/edit#get")
	@RequestMapping(name = "会议室编辑页面", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
		
		return ctrlName + "/edit";
	}
	// end#html
	
	@RequiresPermissions("meeting##post")
	@RequestMapping(name = "创建会议室", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody  MeetingRoomDto meetingDto){
		
		meetingRoomService.createMeeting(meetingDto);
	}
	@RequiresPermissions("meeting##post")
	@RequestMapping(name = "更新会议室" ,path = "" ,method =RequestMethod.PUT)
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
	public void put(@RequestBody MeetingRoomDto meetingDto){
		
		meetingRoomService.updateMeeting(meetingDto);
	}
	@RequiresPermissions("meeting##delete")
	@RequestMapping(name = "删除会议室" ,path = "" ,method =RequestMethod.DELETE)
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
	public void delete (@RequestBody String id){
		
		String [] ids=id.split(",");
		if(ids.length>1){
			
			meetingRoomService.deleteMeeting(ids);
		}else{
			meetingRoomService.deleteMeeting(id);
		}
	}
	
}
