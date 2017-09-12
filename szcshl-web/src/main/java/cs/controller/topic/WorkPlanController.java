package cs.controller.topic;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;
import cs.service.topic.WorkPlanService;

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
@RequestMapping(name = "", path = "workPlan")
public class WorkPlanController {

    @Autowired
    private WorkPlanService workPlanService;

    @RequiresPermissions("workPlan#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<WorkPlanDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<WorkPlanDto> workPlanDtos = workPlanService.get(odataObj);	
        return workPlanDtos;
    }

    @RequiresPermissions("workPlan##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody WorkPlanDto record) {
        return workPlanService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody WorkPlanDto findById(@RequestParam(required = true)String id){		
		return workPlanService.findById(id);
	}

    @RequestMapping(name = "根据课题ID查询", path = "initByTopicId",method=RequestMethod.POST)
    public @ResponseBody WorkPlanDto initByTopicId(@RequestParam(required = true)String topicId){
        return workPlanService.initByTopicId(topicId);
    }
	
    @RequiresPermissions("workPlan##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	workPlanService.delete(id);      
    }

    @RequiresPermissions("workPlan##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody WorkPlanDto record) {
        workPlanService.update(record);
    }

    // begin#html

    @RequiresPermissions("workPlan#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "topicInfo/planEdit";
    }
    // end#html

}