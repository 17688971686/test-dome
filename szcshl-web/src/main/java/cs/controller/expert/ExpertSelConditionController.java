package cs.controller.expert;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertSelConditionService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

import static cs.common.constants.Constant.ERROR_MSG;

/**
 * Description: 专家抽取条件 控制层
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
@Controller
@IgnoreAnnotation
@RequestMapping(name = "专家抽取条件", path = "expertSelCondition")
public class ExpertSelConditionController {

    @Autowired
    private ExpertSelConditionService expertSelConditionService;

    @RequiresAuthentication
    //@RequiresPermissions("expertSelCondition##get")
    @RequestMapping(name = "获取数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<ExpertSelConditionDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertSelConditionDto> expertSelConditionDtos = expertSelConditionService.get(odataObj);	
        return expertSelConditionDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelCondition##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ExpertSelConditionDto record) {
        expertSelConditionService.save(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelCondition#saveConditionList#post")
    @RequestMapping(name = "保存专家抽取条件", path = "saveConditionList", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg saveConditionList(@RequestBody ExpertSelConditionDto[] paramArrary,@RequestParam String businessId,
        @RequestParam String minBusinessId, @RequestParam String businessType, String reviewId){
        ResultMsg resultMsg = expertSelConditionService.saveConditionList(businessId,minBusinessId,businessType,reviewId,paramArrary);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
    @ResponseBody
	public  ExpertSelConditionDto findById(@RequestParam String id){
        ExpertSelConditionDto expertSelConditionDto = expertSelConditionService.findById(id);
        if(!Validate.isObject(expertSelConditionDto)){
            expertSelConditionDto = new ExpertSelConditionDto();
        }
		return expertSelConditionDto;
	}

    @RequiresAuthentication
    //@RequiresPermissions("expertSelCondition##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam(required = true)String ids,@RequestParam String reviewId) {
	    return expertSelConditionService.delete(reviewId,ids);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelCondition##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ExpertSelConditionDto record) {
	    expertSelConditionService.update(record);
    }



}