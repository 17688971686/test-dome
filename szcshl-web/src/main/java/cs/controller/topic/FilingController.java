package cs.controller.topic;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.topic.FilingDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;
import cs.service.topic.FilingService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description: 课题归档 控制层
 * author: ldm
 * Date: 2017-9-4 15:40:48
 */
@Controller
@RequestMapping(name = "课题归档", path = "filing")
public class FilingController {

	String ctrlName = "filing";
    @Autowired
    private FilingService filingService;

    @RequiresPermissions("filing#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<FilingDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<FilingDto> filingDtos = filingService.get(odataObj);	
        return filingDtos;
    }

    @RequiresPermissions("filing##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody FilingDto record) {

        return filingService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody FilingDto findById(@RequestParam(required = true)String id){		
		return filingService.findById(id);
	}

    @RequestMapping(name = "根据课题ID查询", path = "initByTopicId",method=RequestMethod.POST)
    public @ResponseBody
    FilingDto initByTopicId(@RequestParam(required = true)String topicId){
        return filingService.initByTopicId(topicId);
    }
    @RequiresPermissions("filing##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	filingService.delete(id);      
    }

    @RequiresPermissions("filing##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody FilingDto record) {
        filingService.update(record);
    }

    // begin#html
    @RequiresPermissions("filing#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("filing#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "topicInfo/filingEdit";
    }
    // end#html

}