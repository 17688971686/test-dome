package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.ahelper.RealPathResolver;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
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
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author lqs
 *         文件上传
 */
@Controller
@RequestMapping(name = "文件管理", path = "file")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class FileController implements ServletConfigAware,ServletContextAware {
    private static Logger logger = Logger.getLogger(FileController.class);
    
    private static final String plugin_file_path = "contents/plugins";

    private static String FTP_IP1 = "FTP_IP1";
    private static String FTP_PORT1 = "FTP_PORT1";
    private static String FTP_USER = "FTP_USER";
    private static String FTP_PWD = "FTP_PWD";
    private static String FTP_BASE_PATH = "FTP_BASE_PATH";
    private static String FTP_FILE_PATH = "FTP_FILE_PATH";
    
    private String ctrlName = "file";

    @Autowired
    private SysFileService fileService;
    
    @Autowired
    private RealPathResolver realPathResolver;

    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    private ServletConfig servletConfig;
    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取文件数据", path = "", method = RequestMethod.GET)
    public @ResponseBody
    PageModelDto<SysFileDto> get(HttpServletRequest request){
        PageModelDto<SysFileDto> sysFileDtos = null;
        try{
            ODataObj odataObj = new ODataObj(request);
            sysFileDtos = fileService.get(odataObj);

        }catch(Exception e){
            e.printStackTrace();
        }
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
    List<SysFileDto> queryFile(@RequestParam String mainId,@RequestParam String sysBusiType){
        List<SysFileDto> sysfileDto = null;
        try{
             sysfileDto = fileService.queryFile(mainId,sysBusiType);
        }catch (Exception e){
            e.printStackTrace();;
        }
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
                     String sysfileType, String sysBusiType) {
        ResultMsg resultMsg = null;
        try{
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            fileType = fileType.toLowerCase();      //统一转成小写
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            boolean result = FtpUtil.uploadFile(propertyUtil.readProperty(FTP_IP1),Integer.parseInt(propertyUtil.readProperty(FTP_PORT1)),
                    propertyUtil.readProperty(FTP_USER), propertyUtil.readProperty(FTP_PWD), propertyUtil.readProperty(FTP_BASE_PATH), "",
                    new String(fileName.getBytes("GBK"), "ISO-8859-1"), multipartFile.getInputStream());
            if (result) {
                 resultMsg = fileService.saveToFtp(multipartFile.getBytes(), fileName, businessId, fileType, mainId, mainType,sysfileType,sysBusiType,propertyUtil.readProperty(FTP_IP1),propertyUtil.readProperty(FTP_PORT1), propertyUtil.readProperty(FTP_USER),propertyUtil.readProperty(FTP_PWD),propertyUtil.readProperty(FTP_BASE_PATH),"");
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"附件上传失败，连接ftp服务失败，请核查！");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  resultMsg;
    }


    /**
     *文件上传保留本地（预留）
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
    @RequestMapping(name = "文件上传", path = "fileUploadLocal", method = RequestMethod.POST)
    @ResponseBody
    public  ResultMsg uploadLocal(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                             @RequestParam(required = true) String businessId, String mainId,String mainType,
                             String sysfileType, String sysBusiType) {
        ResultMsg resultMsg = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            fileType = fileType.toLowerCase();      //统一转成小写

            if (!multipartFile.isEmpty()) {
                resultMsg =  fileService.save(multipartFile.getBytes(), fileName, businessId, fileType, mainId, mainType,sysfileType,sysBusiType);
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"文件上传失败，无法获取文件信息！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  resultMsg;
    }


    /**
     *
     * @return
     * @throws IOException
     */
    @RequiresAuthentication
    @RequestMapping(name = "ftp文件同步", path = "fileSysUpload", method = RequestMethod.GET)
    @ResponseBody
    public  ResultMsg fileSysUpload( String sysFileId) {
        logger.debug("==================ftp文件同步==================");
        ResultMsg resultMsg = null;
        try{
            SysFile sysFile = fileService.findFileById(sysFileId);
            //文件路径
            String filePath = SysFileUtil.getUploadPath()+File.separator+sysFile.getShowName();
            filePath = filePath.replaceAll("\\\\", "/");
            File file = new File (filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            boolean result = FtpUtil.uploadFile(sysFile.getFtpIp(),Integer.parseInt(sysFile.getPort()),
                    sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(), "",
                    new String(sysFile.getShowName().getBytes("GBK"), "ISO-8859-1"), fileInputStream);
            if (result) {
                //file.delete();
                SysFileDto sysFileDto = new SysFileDto();
                sysFile.setModifiedBy(SessionUtil.getDisplayName());
                sysFile.setModifiedDate(new Date());
                fileService.update(sysFile);
                BeanCopierUtils.copyProperties(sysFile,sysFileDto);
                resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(),"文件同步成功！",sysFileDto);
            } else {
                resultMsg =  new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"文件同步失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "ftp文件校验", path = "fileSysCheck", method = RequestMethod.POST)
    @ResponseBody
    public  ResultMsg checkFtpFile(@RequestParam(required = true)  String sysFileId) {
        logger.debug("==================ftp文件校验==================");
        ResultMsg resultMsg = null;
        try{
            SysFile sysFile = fileService.findFileById(sysFileId);
            //下载ftp服务器附件到本地
            Boolean flag = FtpUtil.downloadFile( sysFile.getFtpIp(), sysFile.getPort()!=null?Integer.parseInt(sysFile.getPort()):0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                    sysFile.getShowName(), SysFileUtil.getUploadPath());
            if(flag){
                resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(),"","");
            }else{
                resultMsg =  new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"ftp服务器上不存在改文件请核查！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMsg;
    }


    @RequiresAuthentication
    @RequestMapping(name = "文件下载", path = "fileDownload", method = RequestMethod.GET)
    public @ResponseBody
    void fileDownload(@RequestParam(required = true) String sysfileId, HttpServletResponse response) {
        logger.debug("==================附件下载==================");
        FileInputStream in = null;
        OutputStream out = null;
        try {
        SysFile sysFile = fileService.findFileById(sysfileId);
        //文件路径
        String filePath = SysFileUtil.getUploadPath()+File.separator+sysFile.getShowName();
       // filePath = filePath.replaceAll("\\\\", "/");
        //下载ftp服务器附件到本地
        Boolean flag = FtpUtil.downloadFile( sysFile.getFtpIp(), sysFile.getPort()!=null?Integer.parseInt(sysFile.getPort()):0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                sysFile.getShowName(), SysFileUtil.getUploadPath());
        String fileType = sysFile.getFileType().toLowerCase(); //最小化
        String filename = sysFile.getShowName();
        filename = URLDecoder.decode(filename, "UTF-8");
        String reg = ".*\\\\(.*)";
        //文件名
        String fileNames = filePath.replaceAll(reg, "$1");
        if (fileNames == null || fileNames.equals(" ")) {
            return;
        }
        //调用输出流
        File f = new File(filePath);
        if (!f.exists()) {
            return;
        }
         in = new FileInputStream(f);
            out = response.getOutputStream();

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
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void delete(@RequestParam(required = true) String sysFileId){
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
    public void preview(@PathVariable String sysFileId, HttpServletResponse response) {
        SysFile sysFile = fileService.findFileById(sysFileId);
        //文件路径
        String filePath = SysFileUtil.getUploadPath()+File.separator+sysFile.getShowName();
        try{
            filePath = filePath.replaceAll("\\\\", "/");
            //下载ftp服务器附件到本地
            Boolean flag = FtpUtil.downloadFile( sysFile.getFtpIp(), sysFile.getPort()!=null?Integer.parseInt(sysFile.getPort()):0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                    sysFile.getShowName(), SysFileUtil.getUploadPath());
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @RequiresAuthentication
    @RequestMapping(name = "下载服务器Word", path = "html/download", method = RequestMethod.GET)
    public String downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        if (fileName != null) {
            try {
                fileName = java.net.URLDecoder.decode(java.net.URLDecoder.decode(fileName,"UTF-8"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String realPath = SysFileUtil.getUploadPath();
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * 只针对office文件
     * @param model
     * @param sysFileId
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "附件编辑", path = "editFile", method = RequestMethod.GET)
    public String editFile(Model model,@RequestParam(required = true) String sysFileId,HttpServletRequest request){
        SysFile sysFile = fileService.findFileById(sysFileId);
        //文件路径
        String filePath = SysFileUtil.getUploadPath();
        filePath = filePath.replaceAll("\\\\", "/");
        //下载ftp服务器附件到本地服務
        Boolean flag = FtpUtil.downloadFile( sysFile.getFtpIp(), sysFile.getPort()!=null?Integer.parseInt(sysFile.getPort()):0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                sysFile.getShowName(), SysFileUtil.getUploadPath());
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        model.addAttribute("filePath",basePath+"/file/html/download");
        model.addAttribute("uploadFile",basePath+"/contents/uploadFile.jsp");
        model.addAttribute("fileName",sysFile.getShowName());
        model.addAttribute("sysFileId",sysFileId);
        //文件类型
        String fileTyp ;
        switch (sysFile.getFileType().toLowerCase()){
            case ".doc":
            case ".docx":
                fileTyp = "doc";
                break;
            case ".xls":
                fileTyp = "xls";
                break;
            case ".xlsx":
                fileTyp = "xlsx";
                break;
            case ".ppt":
                fileTyp = "ppt";
                break;
            case ".pptx":
                fileTyp = "pptx";
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
    @RequestMapping(name = "系统安装文件", path = "html/pluginfile")
    public String pluginfile(Model model) {
        return ctrlName + "/pluginfile";
    }

    @RequiresAuthentication
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
