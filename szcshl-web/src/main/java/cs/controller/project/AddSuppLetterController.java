package cs.controller.project;

import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import cs.service.project.AddSuppLetterService;
import cs.service.rtx.RTXService;
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
@MudoleAnnotation(name = "查询统计",value = "permission#queryStatistics")
public class AddSuppLetterController {

    String ctrlName = "addSuppLetter";
    @Autowired
    private AddSuppLetterService addSuppLetterService;

    @Autowired
    private RTXService rtxService;

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#submitSupp#post")
    @RequestMapping(name = "提交拟补充资料函", path = "saveSupp", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveSupp(@RequestBody AddSuppLetterDto addSuppLetterDto){
        if(!Validate.isString(addSuppLetterDto.getFileType())){
            //1表示拟补充资料函
            addSuppLetterDto.setFileType(Constant.EnumState.PROCESS.getValue());
        }
        return  addSuppLetterService.saveSupp(addSuppLetterDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "发起项目拟补充资料函流程", path = "startSignSupperFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startSignSupperFlow(@RequestParam(required = true) String id) {
         ResultMsg resultMsg = addSuppLetterService.startSignSupperFlow(id);
        if(resultMsg.isFlag()){
            String procInstName = Validate.isObject(resultMsg.getReObj())?resultMsg.getReObj().toString():"";
            rtxService.dealPoolRTXMsg(resultMsg.getIdCode(),resultMsg,procInstName,Constant.MsgType.task_type.name());

            resultMsg.setIdCode(null);
            resultMsg.setReObj(null);
        }
         return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据ID删除", path = "deleteById", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deleteById(@RequestParam(required = true) String id) {
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"删除失败！");
        try {
            addSuppLetterService.delete(id);
            resultMsg.setFlag(true);
            resultMsg.setReCode(Constant.MsgCode.OK.getValue());
            resultMsg.setReMsg("删除成功!");
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "检查是否还有正在审批的拟补充资料函", path = "checkIsApprove", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg checkIsApprove(@RequestParam(required = true) String signId,@RequestParam(required = true) String fileType){
        return addSuppLetterService.checkIsApprove(signId,fileType);
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

    /*@RequiresAuthentication
    //@RequiresPermissions("dispatch#createFileNum#post")
    @RequestMapping(name = "生成文件字号", path = "createFileNum", method = RequestMethod.POST)
    public @ResponseBody ResultMsg createFileNum(@RequestParam String id) throws Exception {
        return addSuppLetterService.fileNum(id);
    }*/

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
    @RequestMapping(name = "拟补充资料函查询", path = "html/suppLetterList", method = RequestMethod.GET)
    public String suppLetterList() {
        return ctrlName+"/suppLetterList";
    }

    @RequiresAuthentication
    //@RequiresPermissions("addSuppLetter#html/view#get")
    @RequestMapping(name = "拟补充资料函查看", path = "view", method = RequestMethod.GET)
    public String view() {
        return ctrlName+"/view";
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