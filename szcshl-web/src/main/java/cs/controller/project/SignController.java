package cs.controller.project;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import cs.common.Constant;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignService;

@Controller
@RequestMapping(name = "收文", path = "sign")
public class SignController {
	
	String ctrlName = "sign";
	@Autowired
	private SignService signService;
	
	@RequiresPermissions("sign#fingByOData#post")	
	@RequestMapping(name = "获取收文数据", path = "fingByOData", method = RequestMethod.POST)
	public @ResponseBody PageModelDto<SignDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);	
		PageModelDto<SignDto> signDtos = signService.get(odataObj);		
		return signDtos;
	}
	
	//编辑收文
	@RequiresPermissions("sign##put")
	@RequestMapping(name = "更新收文", path = "",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  update(@RequestBody SignDto signDto){	
		signService.updateSign(signDto);		
	}
	
	@RequiresPermissions("sign#html/list#get")
	@RequestMapping(name = "收文列表", path = "html/list", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/list";
	}
	
	@RequiresPermissions("sign#html/add#get")
	@RequestMapping(name = "新增收文", path = "html/add", method = RequestMethod.GET)
	public String add() {		
		return ctrlName + "/add";
	}
	
	@RequiresPermissions("sign##post")
	@RequestMapping(name = "创建收文", path = "",method=RequestMethod.POST)	
	public @ResponseBody SignDto post(@RequestBody SignDto signDto)  {
		if(!Validate.isString(signDto.getSignid())){
			signDto.setSignid(UUID.randomUUID().toString());
        }
		// not complete
		signDto.setIsregisteredcompleted(Constant.EnumState.NO.getValue());
		signService.createSign(signDto);	
		
		return signDto;
	}

    @RequestMapping(name = "根据协审计划查询收文信息", path = "findByPlanId",method=RequestMethod.GET)
    public @ResponseBody List<SignDto> findByPlanId(@RequestParam(required = true)String planId)  {
        return signService.findByPlanId(planId);
    }
	
	@RequiresPermissions("sign#html/fillin#get")
	@RequestMapping(name = "填写表格页面", path = "html/fillin", method = RequestMethod.GET)
	public String fillin(){
		
		return ctrlName + "/fillin";
	}
	
	//收文详细信息
	@RequiresPermissions("sign#html/signDetails#get")
	@RequestMapping(name = "收文详细信息", path = "html/signDetails", method = RequestMethod.GET)
	public String signDetails(){
		
		return ctrlName +"/signDetails";
	}	
	
	@RequiresPermissions("sign##delete")
	@RequestMapping(name = "删除收文" ,path = "" ,method =RequestMethod.DELETE)
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
	public void deleteSign(@RequestParam String signid){
		String [] ids=signid.split(",");
		if(ids.length>1){
			signService.deleteSigns(ids);
		}else{			
			signService.deleteSign(signid);
		}
	}
		
	@RequestMapping(name = "初始收文编辑页面", path = "html/initFillPageData",method=RequestMethod.GET)	
	@Transactional
	public @ResponseBody Map<String,Object> initFillPageData(@RequestParam(required = true)String signid,String taskId){		
		return signService.initFillPageData(signid);	
	}
	
	@RequestMapping(name = "初始化详情页面", path = "html/initDetailPageData",method=RequestMethod.GET)	
	@Transactional
	public @ResponseBody SignDto initDetailPageData(@RequestParam(required = true)String signid,@RequestParam(defaultValue = "false",required = false)boolean queryAll){
		return signService.findById(signid,queryAll);
	}
	
	@RequiresPermissions("sign#selectSign#get")
	@RequestMapping(name = "获取办事处信息", path = "selectSign", method = RequestMethod.GET)
	public 	@ResponseBody List<OrgDto> selectSign(HttpServletRequest request) throws ParseException{
		ODataObj odataObj = new ODataObj(request);
		List<OrgDto> orgDto =	signService.selectSign(odataObj);
		return orgDto;
	}
	
	/***************************************  S 新流程处理的方法     *******************************************/

	@RequestMapping(name = "初始化流程处理页面", path = "html/initFlowPageData",method=RequestMethod.GET)
	@Transactional
	public @ResponseBody SignDto initFlowPageData(@RequestParam(required = true)String signid){
		return signService.findById(signid,true);
	}

	@RequiresPermissions("sign#html/startNewFlow#post")
	@RequestMapping(name = "发起流程", path = "html/startNewFlow", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void startNewFlow(@RequestParam(required = true)String signid){
		signService.startNewFlow(signid);
	}
	

	@RequiresPermissions("sign#html/flowDeal#get")
	@RequestMapping(name = "项目流程处理", path = "html/flowDeal", method = RequestMethod.GET)
	public String flowDeal(){
		
		return ctrlName + "/flowDeal";
	}

	@RequiresPermissions("sign#html/signFlowDetail#get")
	@RequestMapping(name = "项目流程详情", path = "html/signFlowDetail", method = RequestMethod.GET)
	public String signFlowDetail(){

		return ctrlName + "/flowDetail";
	}

    @RequiresPermissions("sign#html/signEndDetails#get")
    @RequestMapping(name = "项目流程处理", path = "html/signEndDetails", method = RequestMethod.GET)
    public String signEndDetails(){

        return ctrlName + "/signEndDetails";
    }
	/***************************************  E 新流程处理的方法     *******************************************/


    @RequestMapping(name = "查找项目概算", path = "findAssistSign",method=RequestMethod.GET)
    public @ResponseBody List<SignDto> findAssistSign()  {

        return signService.findAssistSign();
    }
    
}
