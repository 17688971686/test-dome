package cs.controller.manager;

import cs.ahelper.MudoleAnnotation;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.service.project.SignDispaWorkService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 评审费发放
 * Author: mcl
 * Date: 2017/10/11 16:26
 */
@Controller
@RequestMapping( name = "评审费发放" , path = "reviewFee")
@MudoleAnnotation(name = "系统管理员" , value="permission#manager")
public class ReviewFeeController {

    private String ctrlName = "reviewFee";

    @Autowired
    private SignDispaWorkService signDispaWorkService;


    @RequiresAuthentication
    @RequestMapping(name="查询发文申请阶段，发放评审费超期的项目信息列表" , path = "findSignDispaWork" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> findSignDispaWork(){
        return signDispaWorkService.findOverSignDispaWork();
    }


    @RequiresPermissions("reviewFee#html/list#get")
    @RequestMapping(name="项目信息列表" , path="html/list" , method = RequestMethod.GET)
    public String list(){
        return ctrlName + "/list";
    }
}