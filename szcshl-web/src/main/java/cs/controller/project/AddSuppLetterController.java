package cs.controller.project;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
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
    public @ResponseBody List<AddSuppLetterDto> initSuppListDate(@RequestParam String signid){
    	List<AddSuppLetterDto> addSuppLetterDtos=addSuppLetterService.initSuppList(signid);
    	return addSuppLetterDtos;
    }
    
    @RequiresPermissions("addSuppLetter#initaddSuppLetterData#get")
    @RequestMapping(name = "初始化补充资料函", path = "initaddSuppLetterData", method = RequestMethod.GET)
    public @ResponseBody Map<String ,Object> initSuppLetterData(@RequestParam String signid, String id){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("suppletterDto", addSuppLetterService.initSuppLetter(signid,id));
    	return resultMap;
    }
    
    @RequiresPermissions("addSuppLetter#add#post")
    @RequestMapping(name = "添加拟补充资料函", path = "add", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public ResultMsg add(@RequestBody AddSuppLetterDto addSuppLetterDto,Boolean isaddSuppLettr){
		return  addSuppLetterService.addSupp(addSuppLetterDto,isaddSuppLettr);
    }
    
    @RequiresPermissions("addSuppLetter#update#get")
    @RequestMapping(name = "更新拟补充资料函", path = "update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody AddSuppLetterDto addSuppLetterDto){
    	addSuppLetterService.updateSupp(addSuppLetterDto);
    }
    
    @RequiresPermissions("addSuppLetter#findById#get")
    @RequestMapping(name = "根据id获取拟补充资料函", path = "findById", method = RequestMethod.GET)
    public @ResponseBody AddSuppLetterDto getbyId(@RequestParam String id){
    	AddSuppLetterDto addSuppLetterDto = addSuppLetterService.getbyId(id);
    	return addSuppLetterDto;
    }
    
    @RequiresPermissions("dispatch#createFileNum#post")
    @RequestMapping(name = "生成文件字号", path = "createFileNum", method = RequestMethod.POST)
    public @ResponseBody void createFileNum(@RequestParam String id) throws Exception {
        addSuppLetterService.fileNum(id);
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

}