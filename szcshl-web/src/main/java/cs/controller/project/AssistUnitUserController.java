package cs.controller.project;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.project.AssistUnitDto;
import cs.model.project.AssistUnitUserDto;
import cs.repository.odata.ODataObj;
import cs.service.project.AssistUnitUserService;

/**
 * Description: 协审单位用户 控制层
 * author: ldm
 * Date: 2017-6-9 9:37:54
 */
@Controller
@RequestMapping(name = "协审单位用户", path = "assistUnitUser")
public class AssistUnitUserController {

	String ctrlName = "assist";
    @Autowired
    private AssistUnitUserService assistUnitUserService;

    @RequiresPermissions("assistUnitUser#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AssistUnitUserDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AssistUnitUserDto> assistUnitUserDtos = assistUnitUserService.get(odataObj);	
        return assistUnitUserDtos;
    }
    
    @RequiresPermissions("assistUnitUser#getAssistUnit#post")
    @RequestMapping(name="获取协审单位",path="getAssistUnit",method=RequestMethod.POST)
    @ResponseBody
    public List<AssistUnitDto> getAssistUnit(HttpServletRequest request) throws ParseException{
    	ODataObj odataObj=new ODataObj(request);
    	List<AssistUnitDto> assistUnitDtoList=assistUnitUserService.getAssistUnit(odataObj);
    	return assistUnitDtoList;
    	
    }

    @RequiresPermissions("assistUnitUser##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody AssistUnitUserDto record) {
        assistUnitUserService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody AssistUnitUserDto findById(@RequestParam(required = true)String id){		
		return assistUnitUserService.findById(id);
	}
	
    @RequiresPermissions("assistUnitUser##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	assistUnitUserService.delete(id);      
    }

    @RequiresPermissions("assistUnitUser##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody AssistUnitUserDto record) {
        assistUnitUserService.update(record);
    }

    // begin#html
    @RequiresPermissions("assistUnitUser#html/assistUnitUserList#get")
    @RequestMapping(name = "列表页面", path = "html/assistUnitUserList", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/assistUnitUserList";
    }

    @RequiresPermissions("assistUnitUser#html/assistUnitUserEdit#get")
    @RequestMapping(name = "编辑页面", path = "html/assistUnitUserEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/assistUnitUserEdit";
    }
    // end#html

}