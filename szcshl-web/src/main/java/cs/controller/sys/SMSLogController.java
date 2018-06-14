package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
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
    @RequestMapping(name = "发送短信", path = "html/sendSMS", method = RequestMethod.POST)
    @ResponseBody
    public String sendSMSContent(HttpServletRequest request)throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SMSLogDto> logDtos = smsLogService.get(odataObj);
        return null;
    }
    @RequiresAuthentication
    @RequestMapping(name = "删除短信", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
//        orgService.deleteOrg(id);
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
