package cs.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



import cs.domain.RoomBooking;
import cs.model.MeetingRoomDto;
import cs.model.PageModelDto;
import cs.model.RoomBookingDto;
import cs.repository.odata.ODataObj;
import cs.service.RoomBookingSerivce;



@Controller
@RequestMapping(name = "会议预定", path = "room")
public class RoomBookingController {

	private String ctrlName = "room";
	@Autowired
	private RoomBookingSerivce roomBookingSerivce;
	
	@RequiresPermissions("room##get")
	@RequestMapping(name="获取会议预定数据",path = "",method =RequestMethod.GET)
	public @ResponseBody PageModelDto<RoomBookingDto> get(HttpServletRequest request)throws  java.text.ParseException {
		
		ODataObj oDataObj = new ODataObj(request);
		PageModelDto<RoomBookingDto> roomDtos=roomBookingSerivce.get(oDataObj);
		return roomDtos;
	}
	
	/*@RequiresPermissions("room##get")
	@RequestMapping( name="预定数据", path="", method=RequestMethod.GET)
	@ResponseBody
	public List<RoomBooking> roomList(String mrID ,HttpServletRequest request ,ModelMap model) throws ParseException, java.text.ParseException{
		ODataObj oDataObj = new ODataObj(request);
		List<RoomBooking> rb=roomBookingSerivce.getList(oDataObj);
		System.out.println(rb);
		return rb;
	}*/
	
	/*@RequiresPermissions("room##post")
	@RequestMapping( name="导出本周评审会议安排", path="", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody  RoomBookingDto roomDto){
		
		//meetingRoomService.createMeeting(meetingDto);
	}*/
	
	@RequiresPermissions("room##post")
	@RequestMapping(name = "创建会议室预定", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void save(@RequestBody  RoomBookingDto roomDto ){
		
		roomBookingSerivce.createRoom(roomDto);
	}
	//begin#html
	@RequiresPermissions("room#html/list#get")
	@RequestMapping(name = "预定会议室列表", path = "html/list" ,method = RequestMethod.GET)
	public String list(){
		
		return ctrlName +"/list";
	}
	@RequiresPermissions("room#html/roomlist#get")
	@RequestMapping(name = "预定会议室列表2", path = "html/roomlist" ,method = RequestMethod.GET)
	public String roomlist(){
		
		return ctrlName +"/roomlist";
	}
	@RequiresPermissions("room#html/edit#get")
	@RequestMapping(name = "会议室预定编辑页面", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
		
		return ctrlName + "/edit";
	}
	//end#html
	
	
}
