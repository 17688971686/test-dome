package cs.controller.manager;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.project.SignDto;
import cs.service.project.SignService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Description: 项目维护
 * Author: mcl
 * Date: 2018/1/25 17:29
 */
@Controller
@RequestMapping(name = "项目维护", path = "maintainProject")
@MudoleAnnotation(name = "系统管理员" , value="permission#manager")
public class MaintainProjectController {

    private String ctrName = "maintainProject";

    @Autowired
    private SignService signService ;

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

        return ctrName + "/reviewWorkday";
    }

}