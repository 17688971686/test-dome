package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.project.AssistPlanSignDto;
import cs.service.project.AssistPlanSignService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static cs.common.constants.Constant.ERROR_MSG;
import static cs.common.constants.Constant.ERROR_XSS_MSG;

@Controller
@RequestMapping(name = "协审项目信息", path = "assistPlanSign")
@IgnoreAnnotation
public class AssistPlanSignController {

    @Autowired
    private AssistPlanSignService assistPlanSignService;

    @RequiresAuthentication
    //@RequiresPermissions("assistPlanSign#getPlanSignByPlanId#get")
    @RequestMapping(name = "通过协审计划id获取协审项目信息", path = "getPlanSignByPlanId", method = RequestMethod.POST)
    @ResponseBody
    public List<AssistPlanSignDto> getPlanSignByPlanId(@RequestParam String planId) {
        List<AssistPlanSignDto> assistPlanSignDtoList = assistPlanSignService.getPlanSignByPlanId(planId);
        if(!Validate.isList(assistPlanSignDtoList)){
            assistPlanSignDtoList = new ArrayList<>();
        }
        return assistPlanSignDtoList;
    }

    /**
     * 停用（2017-11-16 ldm修改）
     * @param planSigns
     * @return
     */
    @Deprecated
    @RequiresAuthentication
    //@RequiresPermissions("assistPlanSign#savePlanSign#put")
    @RequestMapping(name = "保存协审项目", path = "savePlanSign", method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg savePlanSign(@RequestBody AssistPlanSignDto[] planSigns) {
        ResultMsg resultMsg = ResultMsg.error("协审信息没填写正确，操作失败！");
        if(planSigns.length > 0){
            resultMsg = assistPlanSignService.savePlanSign(planSigns);
            if(!Validate.isObject(resultMsg)){
                resultMsg = ResultMsg.error(ERROR_MSG);
            }
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name="通过收文ID获取协审单位和协审费用" , path = "findAssistPlanSignById" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findAssistPlanSignById(String signId){
        if(!Validate.isString(signId)){
            return ResultMsg.error(ERROR_XSS_MSG);
        }
        return assistPlanSignService.findAssistPlanSignBySignId(signId);
    }

}
