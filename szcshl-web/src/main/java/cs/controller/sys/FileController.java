package cs.controller.sys;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.ahelper.RealPathResolver;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.SysFileUtil;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.PluginFileDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.SysFileService;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lqs
 *         文件上传
 */
@Controller
@RequestMapping(name = "文件管理", path = "file")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class FileController {
    private static Logger logger = Logger.getLogger(FileController.class);
    
    private static final String plugin_file_path = "contents/plugins";
    
    private String ctrlName = "file";

    @Autowired
    private SysFileService fileService;
    
    @Autowired
    private RealPathResolver realPathResolver;

    @RequiresAuthentication
    @RequestMapping(name = "获取文件数据", path = "", method = RequestMethod.GET)
    public @ResponseBody
    PageModelDto<SysFileDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SysFileDto> sysFileDtos = fileService.get(odataObj);
        return sysFileDtos;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据业务ID获取附件", path = "findByBusinessId", method = RequestMethod.POST)
    public @ResponseBody
    List<SysFileDto> findByBusinessId(@RequestParam(required = true) String businessId) {
        List<SysFileDto> sysfileDto = fileService.findByBusinessId(businessId);
        return sysfileDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据收文ID获取附件", path = "findByMainId", method = RequestMethod.POST)
    public @ResponseBody
    List<SysFileDto> findByMainId(@RequestParam(required = true) String mainId) {
        List<SysFileDto> sysfileDto = fileService.findByMainId(mainId);
        return sysfileDto;
    }
    @RequiresAuthentication
    @RequestMapping(name = "点树形图文件夹获取附件", path = "queryFile", method = RequestMethod.POST)
    public @ResponseBody
    List<SysFileDto> queryFile(@RequestParam String mainId,@RequestParam String sysBusiType)throws ParseException{
        List<SysFileDto> sysfileDto = fileService.queryFile(mainId,sysBusiType);
        return sysfileDto;
    }

    /**
     *
     * @param request
     * @param multipartFile
     * @param businessId 业务ID
     * @param mainId     主ID
     * @param sysfileType 附件模块类型
     * @param sysBusiType 附件业务类型
     * @return
     * @throws IOException
     */
    @RequiresAuthentication
    @RequestMapping(name = "文件上传", path = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public  ResultMsg upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                     @RequestParam(required = true) String businessId, String mainId,String mainType,
                     String sysfileType, String sysBusiType) throws IOException {

        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        fileType = fileType.toLowerCase();      //统一转成小写

        if (!multipartFile.isEmpty()) {
           return  fileService.save(multipartFile.getBytes(), fileName, businessId, fileType, mainId, mainType,sysfileType,sysBusiType);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"文件上传失败，无法获取文件信息！");
        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "文件下载", path = "fileDownload", method = RequestMethod.GET)
    public @ResponseBody
    void fileDownload(@RequestParam(required = true) String sysfileId, HttpServletResponse response) throws IOException {
        logger.debug("==================附件下载==================");
        SysFile file = fileService.findFileById(sysfileId);
        String path = SysFileUtil.getUploadPath();
        String url = file.getFileUrl();
        String fileType = file.getFileType().toLowerCase(); //最小化
        String filename = file.getShowName();
        filename = URLDecoder.decode(filename, "UTF-8");
        //获取文件保存路径
        String pathUrl = path + url;
        String reg = ".*\\\\(.*)";
        //文件名
        String fileNames = pathUrl.replaceAll(reg, "$1");
        if (fileNames == null || fileNames.equals(" ")) {
            return;
        }
        //调用输出流
        File f = new File(pathUrl);
        if (!f.exists()) {
            return;
        }
        FileInputStream in = new FileInputStream(f);
        OutputStream out = response.getOutputStream();
        try {
            switch (fileType) {
                case ".jpg":
                    response.setHeader("Content-type", "application/.jpg");
                    break;
                case ".png":
                    response.setHeader("Content-type", "application/.png");
                    break;
                case ".gif":
                    response.setHeader("Content-type", "application/.gif");
                    break;
                case ".docx":
                    response.setHeader("Content-type", "application/.docx");
                    break;
                case ".doc":
                    response.setHeader("Content-type", "application/.doc");
                    break;
                case ".xlsx":
                    response.setHeader("Content-type", "application/.xlsx");
                    break;
                case ".xls":
                    response.setHeader("Content-type", "application/.xls");
                    break;
                case ".pdf":
                    response.setHeader("Content-type", "application/.pdf");
                    break;
                default:
                    response.setContentType("application/octet-stream");

            }

            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(filename.getBytes("GB2312"), "ISO8859-1"));

            byte b[] = new byte[1024];
            int len = -1;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
    }

    //@RequiresPermissions("file#deleteSysFile#delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除系统文件", path = "deleteSysFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSysFile(@RequestParam String id) {
        fileService.deleteById(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "文件删除", path = "delete", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(required = true) String sysFileId) throws IOException {
        fileService.deleteById(sysFileId);
    }

    /**
     * 针对pdf和图片文件
     * @param sysFileId
     * @param response
     * @throws IOException
     */
    @RequiresAuthentication
    @RequestMapping(name = "预览附件", path = "preview/{sysFileId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void preview(@PathVariable String sysFileId, HttpServletResponse response) throws IOException {
        SysFile sysFile = fileService.findFileById(sysFileId);
        String filePath = SysFileUtil.getUploadPath()+sysFile.getFileUrl();
        File file = new File(filePath);
        if(!file.exists()){
            file = new File(realPathResolver.get(plugin_file_path)+File.separator+"nofile.png");
            sysFile.setFileType(".png");
        }

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);  //读取文件流
        inputStream.close();

        response.reset();  //重置结果集
        switch (sysFile.getFileType()){
            case ".pdf":
                response.setContentType("application/pdf");
                break;
            case ".png":
            case ".jpg":
            case ".gif":
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/jpeg");
                break;
        }
        response.addHeader("Content-Length", "" + file.length());  //返回头 文件大小
        response.setHeader("Content-Disposition", "inline;filename=" + new String(sysFile.getShowName().getBytes(), "ISO-8859-1"));

        //获取返回体输出权
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        os.write(buffer); // 输出文件
        os.flush();
        os.close();
    }

    /**
     * 只针对office文件
     * @param model
     * @param sysFileId
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "附件编辑", path = "editFile", method = RequestMethod.GET)
    public String editFile(Model model,@RequestParam(required = true) String sysFileId){
        SysFile sysFile = fileService.findFileById(sysFileId);
        //文件路径
        String filePath = SysFileUtil.getUploadPath()+sysFile.getFileUrl();
        filePath = filePath.replaceAll("\\\\", "/");
        model.addAttribute("filePath",filePath);
        //文件类型
        String fileTyp ;
        switch (sysFile.getFileType().toLowerCase()){
            case ".doc":
            case ".docx":
                fileTyp = "doc";
                break;
            case ".xls":
            case ".xlsx":
                fileTyp = "xls";
                break;
            case ".ppt":
            case ".pptx":
                fileTyp = "ppt";
                break;
            case ".pdf":
                fileTyp = "pdf";
                break;
            default:
                fileTyp = "doc";
        }
        model.addAttribute("fileType",fileTyp);
        //附件信息
        model.addAttribute("sysFile",sysFile);
        if("pdf".equals(fileTyp)){
            return "weboffice/reader_pdf";
        }else{
            model.addAttribute("pluginFilePath",plugin_file_path+"/weboffice.rar");
            return "weboffice/edit_dj";
        }
    }

    @RequiresPermissions("file#html/pluginfile#get")
    //@RequiresAuthentication
    @RequestMapping(name = "插件管理", path = "html/pluginfile")
    public String pluginfile(Model model) {
        return ctrlName + "/pluginfile";
    }

    @RequestMapping(name = "附件管理", path = "html/rightList")
    public String rightList(Model model) {
        return ctrlName + "/rightList";
    }




    @RequiresAuthentication
    @RequestMapping(name = "获取本地插件", path = "getPluginFile", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto listFile() {
        PageModelDto<PluginFileDto> pageModelDto = new PageModelDto();
        List<PluginFileDto> list = new ArrayList<PluginFileDto>();
		File parent = new File(realPathResolver.get(plugin_file_path));
		if (parent.exists()) {			
			 File flist[] = parent.listFiles();
			 for (File f : flist) {
	                if (!f.isDirectory() && !f.getName().toLowerCase().endsWith("png")) {
	                	list.add(new PluginFileDto(f,plugin_file_path)) ;
	                } 
	         }
		}
        pageModelDto.setValue(list);
        pageModelDto.setCount(list.size());

        return pageModelDto;
	}

    @RequiresAuthentication
    @RequestMapping(name = "获取首页本地插件", path = "listHomeFile", method = RequestMethod.POST)
    @ResponseBody
    public List<PluginFileDto> listHomeFile() {
        List<PluginFileDto> list = new ArrayList<PluginFileDto>();
        File parent = new File(realPathResolver.get(plugin_file_path));
        if (parent.exists()) {
            File flist[] = parent.listFiles();
            for (File f : flist) {
                if (!f.isDirectory() && !f.getName().toLowerCase().endsWith("png")) {
                    list.add(new PluginFileDto(f,plugin_file_path)) ;
                }
            }
        }
        return list;
    }
}
