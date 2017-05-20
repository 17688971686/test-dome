package cs.controller.sys;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import cs.common.Constant;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.SysFileService;
import cs.service.sys.UserServiceImpl;

/**
 * @author lqs
 * 文件上传
 * */
@Controller
@RequestMapping(name = "文件管理",path = "file")
public class FileController {
	private static Logger logger = Logger.getLogger(FileController.class);
	private String ctrlName = "file";
	
	@Autowired
	private SysFileService fileService;			
	
	@RequestMapping(name="获取文件数据",path="",method=RequestMethod.GET)
    public @ResponseBody
	PageModelDto<SysFileDto> get(HttpServletRequest request) throws ParseException{
    	ODataObj odataObj = new ODataObj(request);
    	PageModelDto<SysFileDto> sysFileDtos=fileService.get(odataObj);
    	return sysFileDtos;
    }

	//@RequiresPermissions("file##post")
	@RequestMapping(name = "文件上传", path = "",method=RequestMethod.POST)	
	public @ResponseBody SysFileDto upload(HttpServletRequest request,@RequestParam("file")MultipartFile multipartFile,
			@RequestParam(required = true)String businessId,String fileType,String module,String processInstanceId) throws IOException{
		SysFileDto sysFileDto = null;
		String fileName = multipartFile.getOriginalFilename();	
		
		if(!multipartFile.isEmpty()){
			sysFileDto = fileService.save(multipartFile.getBytes(), fileName, businessId, fileType,module,processInstanceId);
		}else{
			logger.info("文件上传失败，无法获取文件信息！");
			throw new IOException(Constant.ERROR_MSG);
		}
		
		return sysFileDto;
	}
	
	
	@RequestMapping(name = "文件删除", path = "delete",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@RequestParam(required = true)String sysFileId) throws IOException{
		fileService.deleteById(sysFileId);		
	}
}