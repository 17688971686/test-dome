package cs.controller.expert;

import cs.model.PageModelDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertSelConditionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Description: 专家抽取条件 控制层
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
@Controller
@RequestMapping(name = "专家抽取条件", path = "expertSelCondition")
public class ExpertSelConditionController {

    @Autowired
    private ExpertSelConditionService expertSelConditionService;

    @RequiresPermissions("expertSelCondition##get")
    @RequestMapping(name = "获取数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<ExpertSelConditionDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertSelConditionDto> expertSelConditionDtos = expertSelConditionService.get(odataObj);	
        return expertSelConditionDtos;
    }

    @RequiresPermissions("expertSelCondition##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ExpertSelConditionDto record) {
        expertSelConditionService.save(record);
    }


    @RequiresPermissions("expertSelCondition#saveConditionList#post")
    @RequestMapping(name = "创建记录", path = "saveConditionList", method = RequestMethod.POST)
    public @ResponseBody List<ExpertSelConditionDto> saveConditionList(@RequestBody ExpertSelConditionDto[] paramArrary) throws Exception{
        return  (paramArrary==null||paramArrary.length==0)?null:expertSelConditionService.saveConditionList(paramArrary);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody ExpertSelConditionDto findById(@RequestParam(required = true)String id){		
		return expertSelConditionService.findById(id);
	}

    @RequiresPermissions("expertSelCondition##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(required = true)String ids) {
        expertSelConditionService.delete(ids);
    }

    @RequiresPermissions("expertSelCondition##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ExpertSelConditionDto record) {
	    expertSelConditionService.update(record);
    }



}