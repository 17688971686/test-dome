package cs.controller.project;

import java.util.List;

import cs.ahelper.IgnoreAnnotation;
import cs.common.Constant;
import cs.common.ResultMsg;
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

import cs.model.project.AssistPlanSignDto;
import cs.service.project.AssistPlanSignService;

@Controller
@RequestMapping(name = "协审项目信息", path = "assistPlanSign")
@IgnoreAnnotation
public class AssistPlanSignController {

    @Autowired
    private AssistPlanSignService assistPlanSignService;

    @RequiresAuthentication
    //@RequiresPermissions("assistPlanSign#getPlanSignByPlanId#get")
    @RequestMapping(name = "通过协审计划id获取协审项目信息", path = "getPlanSignByPlanId", method = RequestMethod.POST)
    public @ResponseBody
    List<AssistPlanSignDto> getPlanSignByPlanId(@RequestParam(required = true) String planId) {
        return assistPlanSignService.getPlanSignByPlanId(planId);
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
        if(planSigns.length > 0){
            return assistPlanSignService.savePlanSign(planSigns);
        }else{
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"协审信息没填写正确，操作失败！");
        }

    }

}
