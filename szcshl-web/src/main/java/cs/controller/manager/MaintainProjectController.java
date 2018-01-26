package cs.controller.manager;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.project.SignDto;
import cs.service.project.SignService;
import cs.service.expert.ExpertReviewService;
import cs.service.manager.ReviewFeeService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

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

    @RequestMapping(name="修改评审意见" , path="html/reviewOpinion" , method = RequestMethod.GET)
    public String reviewOpinion(){
        return ctrlName + "/reviewOpinion";
    }

    @RequestMapping(name="初始化评审工作日维护编辑页" , path = "initReviewDays" , method = RequestMethod.POST)
    @ResponseBody
    public SignDto findReviewDayBySignId(@RequestParam String signId){
        return signService.findReviewDayBySignId(signId);
    }

    @RequestMapping(name="初始化评审工作日维护编辑页" , path = "saveReview" , method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg saveReview(@RequestBody SignDto signDto){
        return signService.saveReview(signDto);
    }


    @RequiresPermissions("maintainProject#html/reviewWorkday#get")
    @RequestMapping(name = "评审工作日维护编辑页", path = "html/reviewWorkday", method = RequestMethod.GET)
    public String reviewWorkday() {

        return ctrlName + "/reviewWorkday";
    }

}