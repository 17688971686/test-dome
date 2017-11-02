package cs.controller.project;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import cs.service.project.AddSuppLetterService;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Description: 项目资料补充函 控制层
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Controller
@RequestMapping(name = "拟补充资料函", path = "addSuppLetter")
@MudoleAnnotation(name = "项目管理",value = "permission#sign")
public class AddSuppLetterController {

    String ctrlName = "addSuppLetter";
    @Autowired
    private AddSuppLetterService addSuppLetterService;

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#submitSupp#post")
    @RequestMapping(name = "提交拟补充资料函", path = "saveSupp", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveSupp(@RequestBody AddSuppLetterDto addSuppLetterDto){
        return  addSuppLetterService.saveSupp(addSuppLetterDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "发起项目拟补充资料函流程", path = "startSignSupperFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startSignSupperFlow(@RequestParam(required = true) String id) {
        return addSuppLetterService.startSignSupperFlow(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#initSuppListDate#post")
    @RequestMapping(name = "初始化拟补充资料函列表", path = "initSuppListDate", method = RequestMethod.POST)
    public @ResponseBody List<AddSuppLetterDto> initSuppListDate(@RequestParam String businessId){
        List<AddSuppLetterDto> addSuppLetterDtos=addSuppLetterService.initSuppList(businessId);
        return addSuppLetterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#addsuppListData#post")
    @RequestMapping(name = "获取拟补充资料函查询列表", path = "addsuppListData", method = RequestMethod.POST)
    public  @ResponseBody PageModelDto<AddSuppLetterDto> getProject(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddSuppLetterDto> addSuppLetterDtos = addSuppLetterService.addsuppListData(odataObj);
        return addSuppLetterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#addSuppApproveList#post")
    @RequestMapping(name = "获取拟补充资料函审批处理列表", path = "addSuppApproveList", method = RequestMethod.POST)
    @ResponseBody
    public  PageModelDto<AddSuppLetterDto> getAddSuppApprove(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddSuppLetterDto> addSuppLetterDtos = addSuppLetterService.addSuppApproveList(odataObj);
        return addSuppLetterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#updateApprove#post")
    @RequestMapping(name = "领导审批处理", path = "updateApprove", method = RequestMethod.POST)
    @ResponseBody
    public void updateApprove(@RequestBody AddSuppLetterDto addSuppLetterDto){
        addSuppLetterService.updateApprove(addSuppLetterDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#initSuppLetter#post")
    @RequestMapping(name = "初始化补充资料函", path = "initSuppLetter", method = RequestMethod.POST)
    public @ResponseBody AddSuppLetterDto initSuppLetter(@RequestParam String businessId, String businessType){
        return addSuppLetterService.initSuppLetter(businessId,businessType);
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#findById#get")
    @RequestMapping(name = "根据id获取拟补充资料函", path = "findById", method = RequestMethod.POST)
    public @ResponseBody AddSuppLetterDto findById(@RequestParam String id){
        return addSuppLetterService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("dispatch#createFileNum#post")
    @RequestMapping(name = "生成文件字号", path = "createFileNum", method = RequestMethod.POST)
    public @ResponseBody ResultMsg createFileNum(@RequestParam String id) throws Exception {
        return addSuppLetterService.fileNum(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "edit", method = RequestMethod.GET)
    public String edit(Model model) {
        return ctrlName+"/edit";
    }


    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#html/edit#get")
    @RequestMapping(name = "编辑上传后页面", path = "editUpload", method = RequestMethod.GET)
    public String editUpload(Model model) {
        return ctrlName+"/editUpload";
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#html/list#get")
    @RequestMapping(name = "拟补充资料函列表页面", path = "list", method = RequestMethod.GET)
    public String addSuppLetterList() {
        return ctrlName+"/list";
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#html/detail#get")
    @RequestMapping(name = "拟补充资料函详细信息页面", path = "detail", method = RequestMethod.GET)
    public String addSuppLetterDetail() {
        return ctrlName+"/detail";
    }

    @RequiresPermissions("addSuppLetter#html/suppLetterList#get")
    @RequestMapping(name = "拟补充资料函查询", path = "suppLetterList", method = RequestMethod.GET)
    public String suppLetterList() {
        return ctrlName+"/suppLetterList";
    }


    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#html/suppLetterApproveEdit#get")
    @RequestMapping(name = "拟补充资料函审批处理页面", path = "suppLetterApproveEdit", method = RequestMethod.GET)
    public String suppLetterApproveEdit(){
        return ctrlName+"/suppLetterApproveEdit";
    }

    @RequiresAuthentication
    @RequestMapping(name = "年度月报简报详情", path = "monthlyMultiyearList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AddSuppLetterDto> monthlyMultiyearList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddSuppLetterDto> addSuppLetterDtos = addSuppLetterService.monthlyMultiyearListData(odataObj);
        return addSuppLetterDtos;
    }
}