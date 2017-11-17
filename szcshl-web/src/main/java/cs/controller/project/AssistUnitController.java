package cs.controller.project;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
import cs.repository.odata.ODataObj;
import cs.service.project.AssistPlanService;
import cs.service.project.AssistUnitService;

/**
 * Description: 协审单位 控制层
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
@Controller
@RequestMapping(name = "协审单位", path = "assistUnit")
@MudoleAnnotation(name = "项目管理", value = "permission#sign")
public class AssistUnitController {

    String ctrlName = "assist";
    @Autowired
    private AssistUnitService assistUnitService;

    @Autowired
    private AssistPlanService assistPlanService;

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit#fingByOData#post")
    @RequestMapping(name = "获取数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AssistUnitDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AssistUnitDto> assistUnitDtos = assistUnitService.get(odataObj);
        return assistUnitDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit#getAssistUnitById#get")
    @RequestMapping(name = "根据ID获取协审单位数据", path = "getAssistUnitById", method = RequestMethod.GET)
    public @ResponseBody
    AssistUnitDto getAssistUnitById(@RequestParam String id) {
        AssistUnitDto assistUnitDto = assistUnitService.findById(id);
        return assistUnitDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody AssistUnitDto record) {
        assistUnitService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    public @ResponseBody
    AssistUnitDto findById(@RequestParam(required = true) String id) {
        return assistUnitService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        assistUnitService.delete(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody AssistUnitDto record) {
        assistUnitService.update(record);
    }

    /**
     * 获取协审单位
     *
     * @param planId
     * @return
     */
   /* @RequiresAuthentication
    //@RequiresPermissions("assistUnit#findPlanAssistUnit#get")
    @RequestMapping(name = "获取协审计划的协审单位", path = "findPlanAssistUnit", method = RequestMethod.GET)
    @ResponseBody
    public List<AssistUnitDto> findPlanAssistUnit(@RequestParam String planId, @RequestParam Integer number) {
        return assistUnitService.findDrawUnit(planId, number);
    }*/

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit#chooseAssistUnit#post")
    @RequestMapping(name = "抽签协审单位", path = "chooseAssistUnit", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg chooseAssistUnit(@RequestParam String planId, @RequestParam Integer number, @RequestParam String drawType) {
        List<AssistUnitDto> assistUnitDtoList = assistUnitService.findDrawUnit(planId, number,drawType);
        if(Validate.isList(assistUnitDtoList)){
           /* 不明白为什么要这么做，修改ldm 2017-11-16
            for (AssistUnitDto assistUnitDto : assistUnitDtoList) {
                assistPlanService.addAssistUnit(planId, assistUnitDto.getId());
                assistUnitService.update(assistUnitDto);
            }*/
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"抽取协审成功！",assistUnitDtoList);
        }else{
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"抽取协审单位失败,没有协审单位！");
        }
        //assistPlanService.updateDrawType(planId, drawType);
    }

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit#getAssistUnitByPlanId#get")
    @RequestMapping(name = "通过planId获取协审单位", path = "getAssistUnitByPlanId", method = RequestMethod.GET)
    @ResponseBody
    public List<AssistUnitDto> getAssistUnitByPlanId(@RequestParam String planId) {

        return assistUnitService.getAssistUnitByPlanId(planId);
    }

    // begin#html
    @RequiresPermissions("assistUnit#html/assistUnitList#get")
    @RequestMapping(name = "协审单位管理", path = "html/assistUnitList", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/assistUnitList";
    }

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit#html/assistUnitEdit#get")
    @RequestMapping(name = "编辑页面", path = "html/assistUnitEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/assistUnitEdit";
    }

    @RequiresAuthentication
    //@RequiresPermissions("assistUnit#html/unitUser#get")
    @RequestMapping(name = "人员列表页面", path = "html/unitUser", method = RequestMethod.GET)
    public String unitUserlist() {
        return ctrlName + "/unit_user_List";
    }
    // end#html

}