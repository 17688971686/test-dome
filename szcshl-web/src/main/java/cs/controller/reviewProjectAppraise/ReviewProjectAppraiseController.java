package cs.controller.reviewProjectAppraise;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.project.AppraiseReportDto;
import cs.repository.odata.ODataObj;
import cs.service.reviewProjectAppraise.AppraiseService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Description:优秀评审报告
 * Author: mcl
 * Date: 2017/9/27 11:45
 */
@Controller
@RequestMapping(name="优秀评审报告" , path="reviewProjectAppraise")
@MudoleAnnotation(name = "项目管理",value = "permission#sign")
public class ReviewProjectAppraiseController {

    private String ctrlName = "reviewProjectAppraise";

    @Autowired
    private AppraiseService appraiseService;

    @RequiresAuthentication
    @RequestMapping(name="通过项目ID初始化" , path="initBySignId" , method = RequestMethod.POST)
    @ResponseBody
    public AppraiseReportDto initBySignId(@RequestParam String signId){
        return  appraiseService.initBySignId(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("reviewProjectAppraise#saveApply#post")
    @RequestMapping(name="保存申请信息" , path="saveApply" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveApply(@RequestBody  AppraiseReportDto appraiseReportDto){
        return appraiseService.saveApply(appraiseReportDto);
    }

    @RequiresAuthentication
    @RequestMapping(name="发起流程" , path="startFlow" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestParam String id){
        return appraiseService.startFlow(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("reviewProjectAppraise#findAppraisingProject#post")
    @RequestMapping(name="获取优秀评审项目" , path="findAppraisingProject" , method=RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> findAppraisingProject(HttpServletRequest request ) throws ParseException{
        ODataObj oDataObj = new ODataObj(request);
        return appraiseService.findAppraisingProject(oDataObj);
    }

    @RequiresAuthentication
    //@RequiresPermissions("reviewProjectAppraise#saveAppraisingProject#post")
    @RequestMapping(name="保存评优项目信息" , path="saveAppraisingProject" , method=RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.NO_CONTENT)
    public void saveAppraisingProject(@RequestParam String signId){

        appraiseService.updateIsAppraising(signId);
    }


    @RequiresAuthentication
    //@RequiresPermissions("reviewProjectAppraise#getAppraiseReport#post")
    @RequestMapping(name="查询优秀评审报告审批列表" , path="getAppraiseReport" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AppraiseReportDto> getAppraiseReport(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        return appraiseService.getAppraiseReport(oDataObj);
    }

    @RequiresAuthentication
    //@RequiresPermissions("reviewProjectAppraise#getAppraiseById#get")
    @RequestMapping(name="通过id查询优秀评审报告信息" , path="getAppraiseById" , method = RequestMethod.POST)
    @ResponseBody
    public AppraiseReportDto getAppraiseById(@RequestParam String id){
        return  appraiseService.getAppraiseById(id);
    }



    @RequiresPermissions("reviewProjectAppraise#html/edit#get")
    @RequestMapping(name="评审项目评优" , path="html/edit" , method = RequestMethod.GET)
    public String edit(){
        return ctrlName + "/edit";
    }

    @RequiresPermissions("reviewProjectAppraise#html/approveList#get")
    @RequestMapping(name="优秀评审报告审批" , path="html/approveList" , method = RequestMethod.GET)
    public String approveList(){
        return ctrlName + "/approveList";
    }


    @RequiresPermissions("reviewProjectAppraise#html/approveWindow#get")
    @RequestMapping(name="处理弹出框" , path = "html/approveWindow" , method = RequestMethod.GET)
    public String approveWindow(){
        return ctrlName + "/approveWindow";
    }
}