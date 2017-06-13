package cs.controller.project;

import cs.model.PageModelDto;
import cs.model.project.AssistPlanDto;
import cs.repository.odata.ODataObj;
import cs.service.project.AssistPlanService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Description: 协审方案 控制层
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Controller
@RequestMapping(name = "协审方案", path = "assistPlan")
public class AssistPlanController {

	String ctrlName = "assist";

    @Autowired
    private AssistPlanService assistPlanService;

    @RequiresPermissions("assistPlan#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AssistPlanDto> findByOData(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AssistPlanDto> assistPlanDtos = assistPlanService.get(odataObj);	
        return assistPlanDtos;
    }

    @RequiresPermissions("assistPlan##post")
    @RequestMapping(name = "新增计划信息", path = "", method = RequestMethod.POST)
    public @ResponseBody AssistPlanDto post(@RequestBody AssistPlanDto record) {
        assistPlanService.save(record);
        return record;
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody AssistPlanDto findById(@RequestParam(required = true)String id){		
		return assistPlanService.findById(id);
	}

    /**
     * 初始化协审计划信息
     * @return
     */
    @RequestMapping(name = "初始化管理页面", path = "initPlanManager",method=RequestMethod.GET)
    public @ResponseBody Map<String,Object> initPlanManager(){
        return assistPlanService.initPlanManager();
    }

    @RequestMapping(name = "保存次项目信息", path = "saveLowPlanSign",method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveLowPlanSign(@RequestBody AssistPlanDto assistPlanDto){
        assistPlanService.saveLowPlanSign(assistPlanDto);
    }

    @RequiresPermissions("assistPlan##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        assistPlanService.delete(id);
    }

    @RequestMapping(name = "删除主项目", path = "cancelPlanSign", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelPlanSign(@RequestParam(required = false) String planId,@RequestParam(required = true)String signIds) {
        assistPlanService.cancelPlanSign(planId,signIds,true);
    }

    @RequestMapping(name = "删除次项目", path = "cancelLowPlanSign", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelLowPlanSign(@RequestParam(required = false) String planId,@RequestParam(required = true)String signIds) {
        assistPlanService.cancelPlanSign(planId,signIds,false);
    }

    @RequestMapping(name = "根据项目ID查询", path = "getAssistPlanBySignId", method = RequestMethod.GET)
    @ResponseBody
    public AssistPlanDto getAssistPlanBySignId(@RequestParam(required = true)String signId) {
       return assistPlanService.getAssistPlanBySignId(signId);
    }


    @RequiresPermissions("assistPlan##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody AssistPlanDto record) {
        assistPlanService.update(record);
    }

    // begin#html
    @RequiresPermissions("assistPlan#html/manager#get")
    @RequestMapping(name = "协审计划管理", path = "html/manager", method = RequestMethod.GET)
    public String manager() {
        return ctrlName+"/manager";
    }

    @RequiresPermissions("assistPlan#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }



    // end#html

}