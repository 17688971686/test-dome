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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // begin#html
    @RequiresPermissions("smslog#html/list#get")
    @RequestMapping(name = "短信日志查询", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    //end#html

}
