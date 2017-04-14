package cs.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public @ResponseBody PageModelDto<RoomBookingDto> get(HttpServletRequest request)throws ParseException{
		
		ODataObj oDataObj = new ODataObj(request);
		PageModelDto<RoomBookingDto> roomDtos=roomBookingSerivce.get(oDataObj);
		return roomDtos;
	}
	
	//begin#html
	@RequiresPermissions("room#html/list#get")
	@RequestMapping(name = "预定会议室列表", path = "html/list" ,method = RequestMethod.GET)
	public String list(){
		
		return ctrlName +"/list";
	}
	@RequiresPermissions("room#html/edit#get")
	@RequestMapping(name = "会议室预定编辑页面", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
		
		return ctrlName + "/edit";
	}
	//end#html
}
