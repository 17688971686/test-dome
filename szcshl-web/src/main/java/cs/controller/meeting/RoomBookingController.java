package cs.controller.meeting;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
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

import cs.common.ICurrentUser;
import cs.common.utils.ExcelUtils;
import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.meeting.MeetingRoomService;
import cs.service.meeting.RoomBookingSerivce;
import cs.service.sys.UserService;



@Controller
@RequestMapping(name = "会议室预定", path = "room")
public class RoomBookingController {

	private String ctrlName = "room";
	@Autowired
	private RoomBookingSerivce roomBookingSerivce;
	@Autowired
	private MeetingRoomService meetingRoomService;
	@Autowired
	private UserService userService;
	@Autowired
	private ICurrentUser currentUser;
	
	@RequiresPermissions("room##get")
	@RequestMapping(name="获取会议预定数据",path = "",method =RequestMethod.GET)
	public @ResponseBody PageModelDto<RoomBookingDto> get(HttpServletRequest request)throws  ParseException {
		ODataObj oDataObj = new ODataObj(request);
		PageModelDto<RoomBookingDto> roomDtos = roomBookingSerivce.get(oDataObj);
		return roomDtos;
	}
	
	@RequiresPermissions("room#fingByOData#post")
	@RequestMapping(name="获取会议预定数据",path = "fingByOData",method =RequestMethod.POST)
	@ResponseBody
	public  PageModelDto<RoomBookingDto> getCountList(HttpServletRequest request) throws ParseException{
		ODataObj oDataObj = new ODataObj(request);
		PageModelDto<RoomBookingDto> roomDtos=roomBookingSerivce.get(oDataObj);
		return roomDtos;
	}
	
	@RequiresPermissions("room#meeting#get")
	@RequestMapping( name="会议室查询", path="meeting", method=RequestMethod.GET)
	@ResponseBody
	public List<MeetingRoomDto> meetingList(String mrID ,HttpServletRequest request ,ModelMap model) throws  ParseException{
		List<MeetingRoomDto> meeting=roomBookingSerivce.findMeetingAll();
		return meeting;
	}
	
	@RequiresPermissions("room#roomNamelist#get")
	@RequestMapping( name="会议室预定查询", path="roomNamelist", method=RequestMethod.GET)
	@ResponseBody
	public List<RoomBookingDto> roomList(HttpServletRequest request) throws ParseException{
		List<RoomBookingDto> roomlist=	roomBookingSerivce.getRoomList();
		return roomlist;
	}
	
	@RequiresPermissions("room#findUser#get")
	@RequestMapping( name="获取会议室预定人", path="findUser", method=RequestMethod.GET)
	@ResponseBody		
	public UserDto userObj(){
		UserDto user = userService.findUserByName(currentUser.getLoginName());
		return user;
	}
	
	@RequiresPermissions("room#exports#get")
	@RequestMapping( name="导出本周评审会议安排", path="exports", method=RequestMethod.GET)
	public void exports(HttpServletRequest req,HttpServletResponse resp){
		try {
			Workbook wb=new HSSFWorkbook();
			String headers[]={"会议名称","评审项目","会议开始时间","会议结束时间","会议预定人","会议主持人","会议地点"};
			List<RoomBooking> foom=roomBookingSerivce.findAll();
			ExcelUtils.fillExcelData(foom,wb, headers);
			ExcelUtils.exports(resp, wb, "本周评审会会议安排.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequiresPermissions("room#stageNextWeek#get")
	@RequestMapping(name="导出下周评审会议安排" ,path = "stageNextWeek", method=RequestMethod.GET)
	public void exportsNext(HttpServletRequest req,HttpServletResponse resp){
		try {
			Workbook wb = new HSSFWorkbook();
			String headers [] ={"会议名称","会议日期","会议开始时间","会议结束时间","会议预定人","会议主持人","会议地点"};
			List<RoomBooking> room =roomBookingSerivce.findNextWeek();
			ExcelUtils.exportStageNextWeek(room, wb, headers);
			ExcelUtils.exports(resp, wb, "导出下周评审会议安排.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//导出本周全部会议安排
	@RequiresPermissions("room#exportWeek#get")
	@RequestMapping( name="导出本周全部会议安排", path="exportWeek", method=RequestMethod.GET)
	public void exportWeek(HttpServletRequest req,HttpServletResponse resp){
		try {
			Workbook wb=new HSSFWorkbook();
			String headers[]={"会议名称","会议日期","会议开始时间","会议结束时间","会议预定人","会议主持人","会议地点"};
			List<RoomBooking> room=roomBookingSerivce.findWeek();
			ExcelUtils.findWeek(room, wb, headers);
			ExcelUtils.exports(resp, wb, "导出本周全部会议安排.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//导出下周全部会议安排
	@RequiresPermissions("room#exportNextWeek#get")
	@RequestMapping( name="导出下周全部会议安排", path="exportNextWeek", method=RequestMethod.GET)
	public void exportNextWeek(HttpServletRequest req,HttpServletResponse resp){
		try {
			Workbook wb =new HSSFWorkbook();
			String headers [] ={"会议名称","会议日期","会议开始时间","会议结束时间","会议预定人","会议主持人","会议地点"};
			List<RoomBooking> rb =roomBookingSerivce.findNextWeek();
			ExcelUtils.excelNextWeek(rb, wb, headers);
			ExcelUtils.exports(resp, wb, "下周全部会议安排.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequiresPermissions("room#addRoom#post")
	@RequestMapping(name = "创建会议室预定", path = "addRoom",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void save(@RequestBody RoomBookingDto roomDto ){
		roomBookingSerivce.createRoom(roomDto);
	}
	
	@RequiresPermissions("room#saveRoom#post")
	@RequestMapping(name = "评审会时间", path = "saveRoom",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void saveRoom(@RequestBody RoomBookingDto roomDto,WorkProgramDto workProgramDto){
		roomBookingSerivce.saveRoom(roomDto,workProgramDto);
	}
	
	@RequiresPermissions("room#updateRoom#put")
	@RequestMapping(name = "编辑会议室预定", path = "updateRoom",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void update(@RequestBody  RoomBookingDto roomDto){
		roomBookingSerivce.updateRoom(roomDto);
	}
	
	@RequiresPermissions("room##delete")
	@RequestMapping(name = "删除会议室预定" ,path = "" ,method =RequestMethod.DELETE)
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
	public void delete(@RequestBody String id){
		if(id!=null){
			roomBookingSerivce.deleteRoom(id);
		}
	}
	
	@RequiresPermissions("room#html/roomlist#get")
	@RequestMapping(name = "预定会议室列表", path = "html/roomlist" ,method = RequestMethod.GET)
	public String roomlist(HttpServletRequest request ,ModelMap model){		
		UserDto user = userService.findUserByName(currentUser.getLoginName());
		if(user!=null){
			model.addAttribute("user", user.getLoginName());
		}
		return ctrlName +"/roomlist";
	}
	
	@RequiresPermissions("room#html/edit#get")
	@RequestMapping(name = "会议室预定编辑页面", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
		return ctrlName + "/edit";
	}
	//end#html
	
	@RequiresPermissions("room#html/countlist#get")
	@RequestMapping( name="预定会议统计情况", path="html/countlist", method=RequestMethod.GET)
	public String countList(){
		return ctrlName + "/countlist";
	}
	

}
