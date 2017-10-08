package cs.controller.topic;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;
import cs.service.topic.WorkPlanService;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description:  控制层
 * author: ldm
 * Date: 2017-9-4 15:33:03
 */
@Controller
@RequestMapping(name = "课题工作方案", path = "workPlan")
@IgnoreAnnotation
public class WorkPlanController {

    @Autowired
    private WorkPlanService workPlanService;

    @RequiresAuthentication
    //@RequiresPermissions("workPlan#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<WorkPlanDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<WorkPlanDto> workPlanDtos = workPlanService.get(odataObj);	
        return workPlanDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("workPlan##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody WorkPlanDto record) {
        return workPlanService.save(record);
    }

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody WorkPlanDto findById(@RequestParam(required = true)String id){		
		return workPlanService.findById(id);
	}

    @RequiresAuthentication
    @RequestMapping(name = "根据课题ID查询", path = "initByTopicId",method=RequestMethod.POST)
    public @ResponseBody WorkPlanDto initByTopicId(@RequestParam(required = true)String topicId){
        return workPlanService.initByTopicId(topicId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workPlan##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	workPlanService.delete(id);      
    }

    @RequiresAuthentication
    //@RequiresPermissions("workPlan##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody WorkPlanDto record) {
        workPlanService.update(record);
    }

    // begin#html
    @RequiresAuthentication
    //@RequiresPermissions("workPlan#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "topicInfo/planEdit";
    }
    // end#html

}