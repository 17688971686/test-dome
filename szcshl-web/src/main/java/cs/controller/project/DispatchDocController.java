package cs.controller.project;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.service.project.DispatchDocService;

@Controller
@RequestMapping(name = "发文", path = "dispatch")
public class DispatchDocController {
	
	private  String ctrlName = "dispatch";
	
	@Autowired
	private DispatchDocService dispatchDocService;
	
	@RequiresPermissions("dispatch##post")
	@RequestMapping(name = "发文提交", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody DispatchDocDto dispatchDocDto) throws Exception  {
		dispatchDocService.save(dispatchDocDto);
	}
	
	@RequiresPermissions("dispatch##post")
	@RequestMapping(name = "生成关联信息", path = "mergeDispa",method=RequestMethod.GET)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void mergeDispa(@RequestParam String signId,String linkSignId) throws Exception  {
		dispatchDocService.mergeDispa(signId,linkSignId);
	}
	
	@RequiresPermissions("dispatch##post")
	@RequestMapping(name = "生成文件字号", path = "fileNum",method=RequestMethod.POST)	
	public @ResponseBody String  getFileNum(@RequestParam String dispaId) throws Exception  {
		String fileNum=dispatchDocService.fileNum(dispaId);
		return fileNum;
	}
	
	@RequiresPermissions("dispatch##getSign##POST")	
	@RequestMapping(name = "获取待选项目", path = "getSign", method = RequestMethod.GET)
	public @ResponseBody List<SignDto> getSign(@RequestParam String linkSignId) throws ParseException {	
		List<SignDto> sList = dispatchDocService.getSign(linkSignId);
		return sList;
	}	
	
	@RequiresPermissions("dispatch##getSelectedSign")
	@RequestMapping(name = "获取已选项目", path = "getSelectedSign",method=RequestMethod.GET)	
	public @ResponseBody List<SignDto> getSignbyIds(@RequestParam String linkSignIds) throws Exception  {
		String [] ids=linkSignIds.split(",");
		List<SignDto> signList =dispatchDocService.getSignbyIds(ids);
		return signList;
	}
	
	@RequiresPermissions("dispatch##getSeleSignByDId")
	@RequestMapping(name = "初始化页面获取已选项目", path = "getSignByDId",method=RequestMethod.GET)	
	public @ResponseBody Map<String,Object> getSeleSignBysId(@RequestParam String bussnessId) throws Exception  {
		Map<String,Object> map =dispatchDocService.getSeleSignBysId(bussnessId);
		return map;
	}
	
	@RequiresPermissions("dispatch##initDispatch")
	@RequestMapping(name = "初始化发文页面", path = "initData",method=RequestMethod.GET)	
	public @ResponseBody Map<String,Object> initDispatch(@RequestParam String signid) throws Exception  {
		Map<String,Object> map=dispatchDocService.initDispatchData(signid);
		return map;
	}
	
	@RequiresPermissions("dispatch#html/initDispatchBySignId#get")
	@RequestMapping(name = "查询流程发文信息", path = "html/initDispatchBySignId",method=RequestMethod.GET)	
	public @ResponseBody DispatchDocDto initDispatchBySignId(@RequestParam String signId) throws Exception  {
		return dispatchDocService.initDispatchBySignId(signId);
	}
	
	@RequiresPermissions("dispatch#html/edit#get")
	@RequestMapping(name = "发文编辑", path = "html/edit", method = RequestMethod.GET)
	public String edit() {		
		return ctrlName + "/edit";
	}
}
