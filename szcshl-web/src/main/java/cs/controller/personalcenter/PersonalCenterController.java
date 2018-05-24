package cs.controller.personalcenter;

import cs.ahelper.MudoleAnnotation;
import cs.domain.project.AgentTask;
import cs.model.PageModelDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.service.project.AgentTaskService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 个人中心
 * Author: mcl
 * Date: 2017/8/14 11:28
 */
@Controller
@RequestMapping(name="个人中心",path="personalCenter")
@MudoleAnnotation(name = "其它",value = "permission#other")
public class PersonalCenterController {

    private String ctrlName="personalcenter";

    @Autowired
    private AgentTaskService agentTaskService;

    @RequiresAuthentication
    @RequestMapping(name = "获取代办记录", path = "findByOData", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<AgentTask> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AgentTask> agentTaskList = agentTaskService.get(odataObj);
        return agentTaskList;
    }

    //@RequiresAuthentication
    @RequiresPermissions("personalCenter#html/takeUser#get")
    @RequestMapping(name="个人代办设置",path="html/takeUser",method= RequestMethod.GET)
    public String takeUser(){

        return ctrlName+"/takeUser";

    }
}