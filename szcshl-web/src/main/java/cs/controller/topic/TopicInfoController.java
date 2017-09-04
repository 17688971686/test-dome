package cs.controller.topic;

import cs.model.PageModelDto;
import cs.model.topic.TopicInfoDto;
import cs.repository.odata.ODataObj;
import cs.service.topic.TopicInfoService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description: 课题研究 控制层
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
@Controller
@RequestMapping(name = "课题研究", path = "topicInfo")
public class TopicInfoController {

	String ctrlName = "topicInfo";
    @Autowired
    private TopicInfoService topicInfoService;

    @RequiresPermissions("topicInfo#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<TopicInfoDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<TopicInfoDto> topicInfoDtos = topicInfoService.get(odataObj);	
        return topicInfoDtos;
    }

    @RequiresPermissions("topicInfo##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody TopicInfoDto record) {
        topicInfoService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody TopicInfoDto findById(@RequestParam(required = true)String id){		
		return topicInfoService.findById(id);
	}
	
    @RequiresPermissions("topicInfo##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	topicInfoService.delete(id);      
    }

    @RequiresPermissions("topicInfo##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody TopicInfoDto record) {
        topicInfoService.update(record);
    }

    // begin#html
    @RequiresPermissions("topicInfo#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("topicInfo#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}