package cs.controller.project;

import cs.model.PageModelDto;
import cs.model.project.AssistUnitUserDto;
import cs.repository.odata.ODataObj;
import cs.service.project.AssistUnitUserService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

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
    @RequiresPermissions("assistUnitUser#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/userList";
    }

    @RequiresPermissions("assistUnitUser#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/userEdit";
    }
    // end#html

}