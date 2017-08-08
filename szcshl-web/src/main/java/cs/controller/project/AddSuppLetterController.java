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
    
    @RequiresPermissions("addSuppLetter##get")
    @RequestMapping(name = "初始化拟补充资料函", path = "", method = RequestMethod.GET)
    public @ResponseBody AddSuppLetterDto initSupp(@ RequestParam String signid,String id){
    	AddSuppLetterDto addSuppLetterDto=addSuppLetterService.initSupp(signid,id);
    	return addSuppLetterDto;
    }
    
    @RequiresPermissions("addSuppLetter#add#get")
    @RequestMapping(name = "添加拟补充资料函", path = "add", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void add(@RequestBody AddSuppLetterDto addSuppLetterDto){
    	        addSuppLetterService.addSupp(addSuppLetterDto);
    }
    
    @RequiresPermissions("addSuppLetter#update#get")
    @RequestMapping(name = "更新拟补充资料函", path = "update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody AddSuppLetterDto addSuppLetterDto){
    	addSuppLetterService.updateSupp(addSuppLetterDto);
    }
    
    @RequiresPermissions("addSuppLetter#add#get")
    @RequestMapping(name = "根据id获取拟补充资料函", path = "getbyId", method = RequestMethod.GET)
    public @ResponseBody AddSuppLetterDto getbyId(@RequestParam String id){
    	AddSuppLetterDto addSuppLetterDto = addSuppLetterService.getbyId(id);
    	return addSuppLetterDto;
    }
    
    /*@RequiresPermissions("dispatch#createFileNum#post")
    @RequestMapping(name = "生成文件字号", path = "createFileNum", method = RequestMethod.POST)
    public @ResponseBody ResultMsg createFileNum(@RequestParam String id) throws Exception {
        ResultMsg returnMsg = addSuppLetterService.fileNum(id);
        return returnMsg;
    }*/
    
    @RequiresPermissions("addSuppLetter#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "edit", method = RequestMethod.GET)
    public String edit(Model model) {
        return ctrlName+"/edit"; 
    }

}