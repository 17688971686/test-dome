package cs.controller.sys;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.common.sysResource.SysResourceDto;
import cs.service.sys.SysService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(name = "系统资源", path = "sys")
@IgnoreAnnotation
public class SysController {
    @Autowired
    private SysService sysService;

    //@RequiresPermissions("sys#resource#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取系统资源数据", path = "resource", method = RequestMethod.GET)
    public @ResponseBody
    List<SysResourceDto> get(HttpServletRequest request) {
        List<SysResourceDto> ZTreeList = sysService.get();
        return ZTreeList;
    }

    @RequestMapping(name = "系统初始化", path = "init", method = RequestMethod.GET)
    @ResponseBody
    public String init() {
        ResultMsg resultMsg = sysService.SysInit();
        if (resultMsg.isFlag()) {
            return "Init system success";
        } else {
            return "Init system fail";
        }
    }

}
