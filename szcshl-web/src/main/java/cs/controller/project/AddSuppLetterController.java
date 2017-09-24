package cs.controller.project;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.Archives.ArchivesLibraryDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import cs.service.project.AddSuppLetterService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 项目资料补充函 控制层
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Controller
@RequestMapping(name = "拟补充资料函", path = "addSuppLetter")
public class AddSuppLetterController {

	String ctrlName = "addSuppLetter";
    @Autowired
    private AddSuppLetterService addSuppLetterService;
    
    @RequiresPermissions("addSuppLetter#initSuppListDate#post")
    @RequestMapping(name = "初始化拟补充资料函列表", path = "initSuppListDate", method = RequestMethod.POST)
    public @ResponseBody List<AddSuppLetterDto> initSuppListDate(@RequestParam String businessId){
    	List<AddSuppLetterDto> addSuppLetterDtos=addSuppLetterService.initSuppList(businessId);
    	return addSuppLetterDtos;
    }
    
    @RequiresPermissions("addSuppLetter#addsuppListData#post")
    @RequestMapping(name = "获取拟补充资料函查询列表", path = "addsuppListData", method = RequestMethod.POST)
    public  @ResponseBody PageModelDto<AddSuppLetterDto> getProject(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddSuppLetterDto> addSuppLetterDtos = addSuppLetterService.addsuppListData(odataObj);	
        return addSuppLetterDtos;
    }
    
    @RequiresPermissions("addSuppLetter#addSuppApproveList#post")
    @RequestMapping(name = "获取拟补充资料函审批处理列表", path = "addSuppApproveList", method = RequestMethod.POST)
    @ResponseBody
    public  PageModelDto<AddSuppLetterDto> getAddSuppApprove(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddSuppLetterDto> addSuppLetterDtos = addSuppLetterService.addSuppApproveList(odataObj);	
        return addSuppLetterDtos;
    }
    
    @RequiresPermissions("addSuppLetter#updateApprove#post")
    @RequestMapping(name = "领导审批处理", path = "updateApprove", method = RequestMethod.POST)
    @ResponseBody
    public void updateApprove(@RequestBody AddSuppLetterDto addSuppLetterDto){
		addSuppLetterService.updateApprove(addSuppLetterDto);
    } 
    
    @RequiresPermissions("addSuppLetter#initSuppLetter#post")
    @RequestMapping(name = "初始化补充资料函", path = "initSuppLetter", method = RequestMethod.POST)
    public @ResponseBody AddSuppLetterDto initSuppLetter(@RequestParam String businessId, String businessType){
        return addSuppLetterService.initSuppLetter(businessId,businessType);
    }
    
    @RequiresPermissions("addSuppLetter#save#post")
    @RequestMapping(name = "保存拟补充资料函", path = "save", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg add(@RequestBody AddSuppLetterDto addSuppLetterDto){
		return  addSuppLetterService.addSupp(addSuppLetterDto);
    }
    
    @RequiresPermissions("addSuppLetter#findById#get")
    @RequestMapping(name = "根据id获取拟补充资料函", path = "findById", method = RequestMethod.GET)
    public @ResponseBody AddSuppLetterDto getbyId(@RequestParam String id){
    	AddSuppLetterDto addSuppLetterDto = addSuppLetterService.getbyId(id);
    	return addSuppLetterDto;
    }
    
    @RequiresPermissions("dispatch#createFileNum#post")
    @RequestMapping(name = "生成文件字号", path = "createFileNum", method = RequestMethod.POST)
    public @ResponseBody ResultMsg createFileNum(@RequestParam String id) throws Exception {
        return addSuppLetterService.fileNum(id);
    }
    
    @RequiresPermissions("addSuppLetter#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "edit", method = RequestMethod.GET)
    public String edit(Model model) {
        return ctrlName+"/edit"; 
    }
    
    @RequiresPermissions("addSuppLetter#html/list#get")
    @RequestMapping(name = "拟补充资料函列表页面", path = "list", method = RequestMethod.GET)
    public String addSuppLetterList() {
        return ctrlName+"/list"; 
    }
    
    @RequiresPermissions("addSuppLetter#html/detail#get")
    @RequestMapping(name = "拟补充资料函详细信息页面", path = "detail", method = RequestMethod.GET)
    public String addSuppLetterDetail() {
        return ctrlName+"/detail"; 
    }
    
    @RequiresPermissions("addSuppLetter#html/suppLetterList#get")
    @RequestMapping(name = "拟补充资料函查询列表页面", path = "suppLetterList", method = RequestMethod.GET)
    public String suppLetterList() {
        return ctrlName+"/suppLetterList"; 
    }
    
    @RequiresPermissions("addSuppLetter#html/suppLetterApproveList#get")
    @RequestMapping(name = "拟补充资料函审批列表页面", path = "suppLetterApproveList", method = RequestMethod.GET)
    public String suppLetterApproveList() {
        return ctrlName+"/suppLetterApproveList"; 
    }
    
    @RequiresPermissions("addSuppLetter#html/suppLetterApproveEdit#get")
    @RequestMapping(name = "拟补充资料函审批处理页面", path = "suppLetterApproveEdit", method = RequestMethod.GET)
    public String suppLetterApproveEdit(){
    	return ctrlName+"/suppLetterApproveEdit"; 
    }

}