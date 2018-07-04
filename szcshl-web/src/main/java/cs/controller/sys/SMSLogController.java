package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.LogDto;
import cs.model.sys.SMSLogDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.LogService;
import cs.service.sys.SMSLogService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(name = "短信日志", path = "smslog")
@MudoleAnnotation(name = "系统管理员" , value="permission#manager")
public class SMSLogController {
    private String ctrlName = "smslog";
    @Autowired
    private SMSLogService smsLogService;

    //@RequiresPermissions("log#fingByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取短信日志数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SMSLogDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SMSLogDto> logDtos = smsLogService.get(odataObj);
        return logDtos;
    }
    @RequiresAuthentication
    @RequestMapping(name = "发送短信", path = "sendSMS", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg sendSMSContent(@RequestBody SMSLogDto smsLogDto)throws ParseException {
        return smsLogService.sendSMSContent(smsLogDto);
    }
    @RequiresAuthentication
    @RequestMapping(name = "获取短信类型", path = "querySMSLogType", method = RequestMethod.GET)
    @ResponseBody
    public List<SMSLogDto> querySMSLogType(@RequestParam(required = false) String type)throws ParseException {
        List<SMSLogDto> list = new ArrayList<>();
        SMSLogDto smsLogDto =  new SMSLogDto();
        smsLogDto.setSmsLogType("sendfgw_type");
        smsLogDto.setSmsLogTypeName("收文类型");
        list.add(smsLogDto);
        smsLogDto =  new SMSLogDto();
        smsLogDto.setSmsLogType("incoming_type");
        smsLogDto.setSmsLogTypeName("项目签收类型");
        list.add(smsLogDto);
        smsLogDto =  new SMSLogDto();
        smsLogDto.setSmsLogType("task_type");
        smsLogDto.setSmsLogTypeName("待办任务类型");
        list.add(smsLogDto);
        smsLogDto =  new SMSLogDto();
        smsLogDto.setSmsLogType("project_type");
        smsLogDto.setSmsLogTypeName("待办项目类型");
        list.add(smsLogDto);
        return list;
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除短信", path = "delete", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        smsLogService.deleteSMSLog(id);
    }
    @RequiresPermissions("smslog#html/list#get")
    @RequestMapping(name = "短信日志查询", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresPermissions("smslog#html/sendSMS#sendSMS")
    @RequestMapping(name = "发送短信", path = "html/sendSMS", method = RequestMethod.POST)
    public String sendSMS() {
        return ctrlName + "/sendSMS";
    }

    @RequiresPermissions("smslog#html/deleteSMS#deleteSMS")
    @RequestMapping(name = "删除短信", path = "html/deleteSMS", method = RequestMethod.GET)
    public String deleteSMS() {
        return ctrlName + "/deleteSMS";
    }
}
