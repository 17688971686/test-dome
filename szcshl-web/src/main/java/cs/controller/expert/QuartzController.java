package cs.controller.expert;

import cs.model.PageModelDto;
import cs.model.sys.QuartzDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.QuartzService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Description: 定时器配置 控制层
 * author: ldm
 * Date: 2017-6-20 10:47:42
 */
@Controller
@RequestMapping(name = "定时器配置", path = "quartz")
public class QuartzController {

	String ctrlName = "quartz";
    @Autowired
    private QuartzService quartzService;

    @RequiresPermissions("quartz#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<QuartzDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<QuartzDto> quartzDtos = quartzService.get(odataObj);	
        return quartzDtos;
    }

    @RequiresPermissions("quartz##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody QuartzDto record) {
        quartzService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody QuartzDto findById(@RequestParam(required = true)String id){		
		return quartzService.findById(id);
	}
	
    @RequiresPermissions("quartz##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	quartzService.delete(id);      
    }

    @RequiresPermissions("quartz##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody QuartzDto record) {
        quartzService.update(record);
    }

    // begin#html
    @RequiresPermissions("quartz#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("quartz#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}