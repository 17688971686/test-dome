package cs.controller.manager;

import cs.ahelper.MudoleAnnotation;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertReviewService;
import cs.service.manager.ReviewFeeService;
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
 * Description: 维护项目
 * Author: mcl
 * Date: 2017/10/11 16:26
 */
@Controller
@RequestMapping( name = "维护项目" , path = "maintainProjec")
@MudoleAnnotation(name = "系统管理员" , value="permission#manager")
public class MaintainProjectController {

    private String ctrlName = "maintainProjec";


    @RequestMapping(name="修改评审意见" , path="html/reviewOpinion" , method = RequestMethod.GET)
    public String reviewOpinion(){
        return ctrlName + "/reviewOpinion";
    }
}