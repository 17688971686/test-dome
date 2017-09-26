package cs.controller.reviewProjectAppraising;

import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.service.reviewProjectAppraising.AppraisingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 优秀评审项目管理
 * Author: mcl
 * Date: 2017/9/23 15:21
 */
@Controller
@RequestMapping(name="优秀评审项目" , path="reviewProjectAppraising")
public class ReviewProjectAppraisingController {

    private String ctrlName = "reviewProjectAppraising";

    @Autowired
    private AppraisingService appraisingService;

    @RequiresPermissions("reviewProjectAppraising#findEndProject#post")
    @RequestMapping(name="获取已办结项目" , path="findEndProject" , method=RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> findEndProject(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        return appraisingService.findEndProject(oDataObj);
    }

    @RequiresPermissions("reviewProjectAppraising#findAppraisingProject#post")
    @RequestMapping(name="获取优秀评审项目" , path="findAppraisingProject" , method=RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> findAppraisingProject(HttpServletRequest request ) throws ParseException{
        ODataObj oDataObj = new ODataObj(request);
        return appraisingService.findAppraisingProject(oDataObj);
    }


    @RequiresPermissions("reviewProjectAppraising#saveAppraisingProject#post")
    @RequestMapping(name="保存评优项目信息" , path="saveAppraisingProject" , method=RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.NO_CONTENT)
    public void saveAppraisingProject(@RequestParam String signId){

        appraisingService.updateIsAppraising(signId);

    }


    @RequiresPermissions("reviewProjectAppraising#html/list#get")
    @RequestMapping(name="优秀评审项目查询" , path="html/list" , method = RequestMethod.GET)
    public String list(){
        return ctrlName + "/list";
    }


    @RequiresPermissions("reviewProjectAppraising#html/edit#get")
    @RequestMapping(name="评审项目评优列表" , path="html/edit" , method = RequestMethod.GET)
    public String edit(){
        return ctrlName + "/edit";
    }
}