package cs.controller.meeting;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.ExcelUtils;
import cs.common.utils.FileUtils;
import cs.common.utils.SessionUtil;
import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.meeting.RoomBookingSerivce;
import cs.service.sys.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.List;


@Controller
@RequestMapping(name = "会议室预定", path = "room")
@MudoleAnnotation(name = "会议室管理",value = "permission#meeting")
public class RoomBookingController {

    private String ctrlName = "room";
    @Autowired
    private RoomBookingSerivce roomBookingSerivce;
    @Autowired
    private UserService userService;

    @RequiresAuthentication
    //@RequiresPermissions("room##get")
    @RequestMapping(name = "获取会议预定数据", path = "", method = RequestMethod.GET)
    public @ResponseBody
    PageModelDto<RoomBookingDto> get(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        PageModelDto<RoomBookingDto> roomDtos = roomBookingSerivce.get(oDataObj);
        return roomDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("room#fingByOData#post")
    @RequestMapping(name = "获取会议预定数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<RoomBookingDto> getCountList(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        PageModelDto<RoomBookingDto> roomDtos = roomBookingSerivce.get(oDataObj);
        return roomDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("room#meeting#get")
    @RequestMapping(name = "会议室查询", path = "meeting", method = RequestMethod.GET)
    @ResponseBody
    public List<MeetingRoomDto> meetingList(String mrID, HttpServletRequest request, ModelMap model) throws ParseException {
        List<MeetingRoomDto> meeting = roomBookingSerivce.findMeetingAll();
        return meeting;
    }

    @RequiresAuthentication
    //@RequiresPermissions("room#roomNamelist#get")
    @RequestMapping(name = "会议室预定查询", path = "roomNamelist", method = RequestMethod.GET)
    @ResponseBody
    public List<RoomBookingDto> roomList(HttpServletRequest request) throws ParseException {
        List<RoomBookingDto> roomlist = roomBookingSerivce.getRoomList();
        return roomlist;
    }

    @RequiresAuthentication
    @RequestMapping(name = "会议室预定初始化值", path = "initDefaultValue", method = RequestMethod.POST)
    @ResponseBody
    public RoomBookingDto initDefaultValue(String businessId,String businessType) {
        return roomBookingSerivce.initDefaultValue(businessId,businessType);
    }

    @RequiresAuthentication
    //@RequiresPermissions("room#findUser#get")
    @RequestMapping(name = "获取会议室预定人", path = "findUser", method = RequestMethod.GET)
    @ResponseBody
    public UserDto userObj() {
        UserDto user = userService.findUserByName(SessionUtil.getLoginName());
        return user;
    }

    @SuppressWarnings("unused")
    @RequiresPermissions("room#exportThisWeekStage#get")
    @RequestMapping(name = "导出本周评审会议安排", path = "exportThisWeekStage", method = RequestMethod.GET)
    public void exportThisWeekStage(HttpServletRequest request , HttpServletRequest req, HttpServletResponse resp, @RequestParam String currentDate, @RequestParam String rbType, @RequestParam String mrId , @RequestParam String fileName) {
//		roomBookingSerivce.exportThisWeekStage();
        String date = currentDate.replaceAll("/", "-");
        InputStream is = null;
        ServletOutputStream out = null;

        try {
//            String title = new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
            String title = java.net.URLDecoder.decode(fileName,"UTF-8");//解码，需要抛异常
            File file =  roomBookingSerivce.exportRoom(currentDate, rbType, mrId);
            is = new FileInputStream(file);
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件默认
            String fileName2 = new String((fileName + ".doc").getBytes("GB2312"), "ISO-8859-1");
            resp.addHeader("Content-Disposition", "attachment;filename=" + fileName2);
            out = resp.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while((bytesToRead = is.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @RequiresAuthentication
    //@RequiresPermissions("room#exportNextWeekStage#get")
    @RequestMapping(name = "导出下周评审会议安排", path = "exportNextWeekStage", method = RequestMethod.GET)
    public void exportsNext(HttpServletRequest req, HttpServletResponse resp) {
        roomBookingSerivce.exportNextWeekStage();
    }

    //导出本周全部会议安排
    @RequiresAuthentication
    //@RequiresPermissions("room#exportWeek#get")
    @RequestMapping(name = "导出本周全部会议安排", path = "exportWeek", method = RequestMethod.GET)
    public void exportWeek(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Workbook wb = new HSSFWorkbook();
            String headers[] = {"会议名称", "会议日期", "会议开始时间", "会议结束时间", "会议预定人", "会议主持人", "会议地点"};
            List<RoomBooking> room = roomBookingSerivce.findWeek();
            ExcelUtils.findWeek(room, wb, headers);
            ExcelUtils.exports(resp, wb, "导出本周全部会议安排.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //导出下周全部会议安排
    @RequiresAuthentication
    //@RequiresPermissions("room#exportNextWeek#get")
    @RequestMapping(name = "导出下周全部会议安排", path = "exportNextWeek", method = RequestMethod.GET)
    public void exportNextWeek(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Workbook wb = new HSSFWorkbook();
            String headers[] = {"会议名称", "会议日期", "会议开始时间", "会议结束时间", "会议预定人", "会议主持人", "会议地点"};
            List<RoomBookingDto> rb = roomBookingSerivce.findNextWeek();

            ExcelUtils.excelNextWeek(rb, wb, headers);
            ExcelUtils.exports(resp, wb, "下周全部会议安排.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresAuthentication
    //@RequiresPermissions("room#addRoom#post")
    @RequestMapping(name = "创建会议室预定", path = "addRoom", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg save(@RequestBody RoomBookingDto roomDto) {
        return roomBookingSerivce.saveRoom(roomDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("room#saveRoom#post")
    @RequestMapping(name = "保存会议室预定", path = "saveRoom", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveRoom(@RequestBody RoomBookingDto roomDto) {
        return roomBookingSerivce.saveRoom(roomDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("room##delete")
    @RequestMapping(name = "删除会议室预定", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam String id) {
        roomBookingSerivce.deleteRoom(id);
    }

    @RequiresPermissions("room#html/roomlist#get")
    @RequestMapping(name = "会议预定管理", path = "html/roomlist", method = RequestMethod.GET)
    public String roomlist(HttpServletRequest request, ModelMap model) {
        UserDto user = userService.findUserByName(SessionUtil.getLoginName());
        if (user != null) {
            model.addAttribute("user", user.getLoginName());
        }
        return ctrlName + "/roomlist";
    }

    @RequiresAuthentication
    //@RequiresPermissions("room#html/edit#get")
    @RequestMapping(name = "会议室预定编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
    //end#html

    @RequiresPermissions("room#html/countlist#get")
    @RequestMapping(name = "预定会议统计情况", path = "html/countlist", method = RequestMethod.GET)
    public String countList() {
        return ctrlName + "/countlist";
    }


}
