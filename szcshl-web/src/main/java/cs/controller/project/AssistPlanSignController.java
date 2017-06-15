package cs.controller.project;

import java.util.List;

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
@RequestMapping(name="协审项目信息",path="assistPlanSign")
public class AssistPlanSignController {
	
	@Autowired
	private AssistPlanSignService assistPlanSignService;
	
	@RequiresPermissions("assistPlanSign#getPlanSignByPlanId#get")
	@RequestMapping(name="通过协审计划id获取协审项目信息",path="getPlanSignByPlanId",method=RequestMethod.GET)
	public @ResponseBody List<AssistPlanSignDto> getPlanSignByPlanId(@RequestParam(required=true) String planId){
		
		return assistPlanSignService.getPlanSignByPlanId(planId);
	}
	
	@RequiresPermissions("assistPlanSign#savePlanSign#put")
	@RequestMapping(name="保存协审项目",path="savePlanSign",method=RequestMethod.PUT)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void savePlanSign(@RequestBody AssistPlanSignDto[] planSigns){
		
		assistPlanSignService.savePlanSign(planSigns);
		
	}

}
