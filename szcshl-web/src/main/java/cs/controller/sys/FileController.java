package cs.controller.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import org.springframework.web.multipart.MultipartFile;

import cs.common.Constant;
import cs.common.utils.SysFileUtil;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.SysFileService;

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
	
	@RequestMapping(name="根据业务ID获取上传数据",path="findBySysFileSignId",method=RequestMethod.GET)
	public @ResponseBody List<SysFileDto> findByFileSignId(@RequestParam(required = true)String signid){
		List<SysFileDto>  sysfileDto = fileService.findBySysFileSignId(signid);
		return sysfileDto;
	}
	
	@RequestMapping(name="获取一个项目下的所有附件列表",path="initFileUploadlist",method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> initFileUploadlist(@RequestParam(required = true)String signid){
//		
		return fileService.initFileUploadlist(signid);
	}
	
	
	@RequestMapping(name = "文件上传", path = "fileUpload",method=RequestMethod.POST)	
	public @ResponseBody SysFileDto upload(HttpServletRequest request,@RequestParam("file")MultipartFile multipartFile,
			@RequestParam(required = true)String businessId,String sysSignId,String sysfileType ) throws IOException{
		SysFileDto sysFileDto = null;
		String fileName = multipartFile.getOriginalFilename();	
		String fileType=fileName.substring(fileName.lastIndexOf("."),fileName.length());
		
		if(!multipartFile.isEmpty()){
			sysFileDto = fileService.save(multipartFile.getBytes(), fileName, businessId, fileType,sysSignId,sysfileType);
		}else{
			logger.info("文件上传失败，无法获取文件信息！");
			throw new IOException(Constant.ERROR_MSG);
		}
		
		return sysFileDto;
	}
	@RequestMapping(name = "文件下载", path = "fileDownload",method=RequestMethod.GET)
	public @ResponseBody  void fileDownload(@RequestParam(required = true) String sysfileId,HttpServletResponse response) throws IOException{
		
		try {
			logger.debug("==================下载模板文件==================");
			SysFile  file = fileService.findFileById(sysfileId);
			String path = SysFileUtil.getUploadPath();
			String url = file.getFileUrl();
			String fileType = file.getFileType();
			String filename = file.getShowName();
			filename = URLDecoder.decode(filename, "UTF-8");
			//获取文件保存路径
			String pathUrl = path+url;
			String reg = ".*\\\\(.*)";
			//文件名
			String fileNames = pathUrl.replaceAll(reg, "$1");
			if(fileNames == null||fileNames.equals(" ")){
				return;
			}
			//调用输出流
			File f = new File(pathUrl);
			if(!f.exists()){
				logger.info("附件信息不存在!!!");
			}
			if (fileType.equals(".jpg")) {
				response.setHeader("Content-type", "application/.jpg");
			}
			else if (fileType.equals(".png")) {
				response.setHeader("Content-type", "application/.png");
			} 
			else if(fileType.equals(".gif")){
				response.setHeader("Content-type", "application/.gif");
			}
			else if(fileType.equals(".docx")){
				response.setHeader("Content-type", "application/.docx");
			}
			else if(fileType.equals(".doc")){
				response.setHeader("Content-type", "application/.doc");
			}
			else if(fileType.equals(".xlsx")){
				response.setHeader("Content-type", "application/.xlsx");
			}
			else if(fileType.equals(".xls")){
				response.setHeader("Content-type", "application/.xls");
			}
			else if(fileType.equals(".pdf")){
				response.setHeader("Content-type", "application/.pdf");
			}
			else if (fileType.equals("xls")) {
				response.setContentType("applicationnd.ms-excel;charset=GBK");
				response.setHeader("Content-type", "application/x-msexcel");
			} else {
				response.setContentType("application/octet-stream");
			}
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(filename.getBytes("GB2312"), "ISO8859-1"));
				
			FileInputStream in = new FileInputStream(f);
			OutputStream out = response.getOutputStream();
			byte b[] = new byte[1024];
			int len = -1;
			while((len=in.read(b))!=-1){
				out.write(b, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		
		}
	}
	
	@RequiresPermissions("file#deleteSysFile#delete")
	@RequestMapping(name = "删除系统文件" ,path = "deleteSysFile" ,method =RequestMethod.DELETE)
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
	public void deleteSysFile (@RequestBody String id){
		fileService.deleteById(id);	
	}
	
	@RequestMapping(name = "文件删除", path = "delete",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@RequestParam(required = true)String sysFileId) throws IOException{
		fileService.deleteById(sysFileId);		
	}
}
