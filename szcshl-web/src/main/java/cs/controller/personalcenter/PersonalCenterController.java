package cs.controller.personalcenter;

import cs.ahelper.MudoleAnnotation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    //@RequiresAuthentication
    @RequiresPermissions("personalCenter#html/takeUser#get")
    @RequestMapping(name="代办人编辑页",path="html/takeUser",method= RequestMethod.GET)
    public String takeUser(){

        return ctrlName+"/takeUser";

    }
}