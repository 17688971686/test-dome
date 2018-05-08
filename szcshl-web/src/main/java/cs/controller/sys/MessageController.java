package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sys.MessageDto;
import cs.service.sys.MessageService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
@Controller
@RequestMapping(name = "短信", path = "message")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class MessageController {
    private String ctrlName = "sms";
    @Autowired
    private MessageService messageService;

    @RequiresAuthentication
    @RequestMapping(name = "新增短信", path = "saveMessage", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody MessageDto record) {
       return messageService.save(record);
    }

    // begin#html
    @RequiresPermissions("message#html/edit#get")
    @RequestMapping(name = "短信编辑", path = "html/edit", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/edit";
    }
}
