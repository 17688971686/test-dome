package cs.controller.project;

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

import cs.model.project.FileRecordDto;
import cs.service.project.FileRecordService;

@Controller
@RequestMapping(name = "归档", path = "fileRecord")
public class FileRecordController {

private  String ctrlName = "fileRecord";
	
	@Autowired
	private FileRecordService fileRecordService;
	
	@RequiresPermissions("fileRecord##post")
	@RequestMapping(name = "发文提交", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody FileRecordDto fileRecordDto) throws Exception  {
		fileRecordService.save(fileRecordDto);
	}
	
	@RequiresPermissions("fileRecord#html/edit#get")
	@RequestMapping(name = "归档编辑", path = "html/edit", method = RequestMethod.GET)
	public String edit() {		
		return ctrlName + "/edit";
	}
	
	@RequestMapping(name = "初始归档编辑页面", path = "html/initBySignId",method=RequestMethod.GET)	
	@Transactional
	public @ResponseBody FileRecordDto initBySignId(@RequestParam(required = true)String signId){		
		return fileRecordService.initBySignId(signId);	
	}
	
}
