package cs.controller.manager;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.project.SignDto;
import cs.service.project.SignService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Description: 维护项目
 * Author: mcl
 * Date: 2017/10/11 16:26
 */
@Controller
@RequestMapping( name = "维护项目" , path = "maintainProject")
@MudoleAnnotation(name = "系统管理员" , value="permission#manager")
public class MaintainProjectController {

    private String ctrlName = "maintainProject";
    @Autowired
    private SignService signService ;

    @RequiresAuthentication
    @RequestMapping(name="修改评审意见" , path="html/reviewOpinion" , method = RequestMethod.GET)
    public String reviewOpinion(){
        return ctrlName + "/reviewOpinion";
    }

    @RequiresAuthentication
    @RequestMapping(name="初始化评审工作日维护编辑页" , path = "initReviewDays" , method = RequestMethod.POST)
    @ResponseBody
    public SignDto findReviewDayBySignId(@RequestParam String signId){
        return signService.findReviewDayBySignId(signId);
    }

    @RequiresAuthentication
    @RequestMapping(name="初始化评审工作日维护编辑页" , path = "saveReview" , method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg saveReview(@RequestBody SignDto signDto){
        return signService.saveReview(signDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("maintainProject#html/reviewWorkday#get")
    @RequestMapping(name = "评审工作日维护编辑页", path = "html/reviewWorkday", method = RequestMethod.GET)
    public String reviewWorkday() {

        return ctrlName + "/reviewWorkday";
    }

    @RequiresAuthentication
    //@RequiresPermissions("maintainProject#html/maintainExpertScore#get")
    @RequestMapping(name = "专家评分", path = "html/maintainExpertScore", method = RequestMethod.GET)
    public String maintainExpertScore() {

        return  ctrlName+"/maintainExpertScore";
    }

    @RequiresAuthentication
    //@RequiresPermissions("maintainProject#html/maintainExpertPayment#get")
    @RequestMapping(name = "维护项目的评审费发放", path = "html/maintainExpertPayment", method = RequestMethod.GET)
    public String maintainExpertPayment() {

        return  ctrlName + "/maintainExpertPayment";
    }

    @RequiresAuthentication
    //@RequiresPermissions("maintainProject#html/maintainExpertConfirm#get")
    @RequestMapping(name = "修改确定专家", path = "html/maintainExpertConfirm", method = RequestMethod.GET)
    public String maintainExpertConfirm() {

        return  ctrlName + "/maintainExpertConfirm";
    }

    @RequiresAuthentication
    @RequestMapping(name = "维护工作方案", path = "html/workprogramEdit", method = RequestMethod.GET)
    public String workprogramEdit() {
        return  ctrlName + "/workprogramEdit";
    }

    //@RequiresAuthentication
    @RequiresPermissions("maintainProject#html/MaintainProjectList#get")
    @RequestMapping(name = "维护项目", path = "html/MaintainProjectList", method = RequestMethod.GET)
    public String MaintainProjectList() {

        return "sign/MaintainProjectList";
    }

    //@RequiresAuthentication
    @RequiresPermissions("maintainProject#html/deletList#get")
    @RequestMapping(name = "作废项目", path = "html/deletList", method = RequestMethod.GET)
    public String deletList() {

        return "sign/deletList";
    }
}