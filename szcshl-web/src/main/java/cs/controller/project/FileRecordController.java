package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.constants.Constant.EnumFlowNodeGroupName;
import cs.common.ResultMsg;
import cs.model.project.FileRecordDto;
import cs.service.project.FileRecordService;
import cs.service.sys.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(name = "归档", path = "fileRecord")
@IgnoreAnnotation
public class FileRecordController {

private  String ctrlName = "fileRecord";
	
	@Autowired
	private FileRecordService fileRecordService;
	@Autowired
	private UserService userService;

    @RequiresAuthentication
    //@RequiresPermissions("fileRecord##post")
	@RequestMapping(name = "发文提交", path = "",method=RequestMethod.POST)
	@ResponseBody
	public ResultMsg post(@RequestBody FileRecordDto fileRecordDto) throws Exception  {
		return fileRecordService.save(fileRecordDto);
	}

    @RequiresAuthentication
    //@RequiresPermissions("fileRecord#html/edit#get")
	@RequestMapping(name = "归档编辑", path = "html/edit", method = RequestMethod.GET)
	public String edit() {		
		return ctrlName + "/edit";
	}

    @RequiresAuthentication
    //@RequiresPermissions("fileRecord#initFillPage#get")
	@RequestMapping(name = "初始归档编辑页面", path = "initFillPage",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> initFillPage(@RequestParam(required = true)String signId){		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("file_record", fileRecordService.initBySignId(signId));
		resultMap.put("sign_user_List", userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue()));
		return resultMap;	
	}

    @RequiresAuthentication
	//@RequiresPermissions("fileRecord#html/initBySignId#get")
	@RequestMapping(name = "归档详情信息", path = "html/initBySignId",method=RequestMethod.GET)	
	public @ResponseBody FileRecordDto initBySignId(@RequestParam(required = true)String signId){		
		return fileRecordService.initBySignId(signId);	
	}
	
	
}
