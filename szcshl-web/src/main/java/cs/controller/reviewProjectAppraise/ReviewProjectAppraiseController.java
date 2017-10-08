package cs.controller.reviewProjectAppraise;

import cs.ahelper.MudoleAnnotation;
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
    //@RequiresPermissions("reviewProjectAppraise#findEndProject#post")
    @RequestMapping(name="获取已办结项目" , path="findEndProject" , method= RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> findEndProject(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        return appraiseService.findEndProject(oDataObj);
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
    //@RequiresPermissions("reviewProjectAppraise#initProposer#get")
    @RequestMapping(name="初始化评审报告申请人" , path="initProposer" , method = RequestMethod.GET)
    @ResponseBody
    public AppraiseReportDto initProposer(){
        return appraiseService.initProposer();
    }

    @RequiresAuthentication
    //@RequiresPermissions("reviewProjectAppraise#saveApply#post")
    @RequestMapping(name="保存申请信息" , path="saveApply" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveApply(@RequestBody  AppraiseReportDto appraiseReportDto){
        appraiseService.saveApply(appraiseReportDto);
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
    @RequestMapping(name="通过id查询优秀评审报告信息" , path="getAppraiseById" , method = RequestMethod.GET)
    @ResponseBody
    public AppraiseReportDto getAppraiseById(@RequestParam String id){
        return  appraiseService.getAppraiseById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("reviewProjectAppraise#saveApprove#put")
    @RequestMapping(name="保存审批内容" , path="saveApprove" , method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveApprove(@RequestBody AppraiseReportDto appraiseReportDto){
        appraiseService.saveApprove(appraiseReportDto);
    }

    @RequiresPermissions("reviewProjectAppraise#html/list#get")
    @RequestMapping(name="优秀评审报告查询" , path="html/list" , method = RequestMethod.GET)
    public String list(){
        return ctrlName + "/list";
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
}