package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.ahelper.RealPathResolver;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.PluginFileDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.project.WorkProgramService;
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
import java.util.*;

import static cs.common.Constant.*;


/**
 * @author lqs
 *         文件上传
 */
@Controller
@RequestMapping(name = "文件管理", path = "file")
@MudoleAnnotation(name = "系统管理", value = "permission#system")
public class FileController implements ServletConfigAware, ServletContextAware {
    private static Logger logger = Logger.getLogger(FileController.class);

    private String ctrlName = "file";

    @Autowired
    private SysFileService fileService;
    @Autowired
    private RealPathResolver realPathResolver;
    @Autowired
    private SignRepo signRepo;

    @Autowired
    private WorkProgramService workProgramService ;

    @Autowired
    private SignBranchRepo signBranchRepo;

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
    @RequestMapping(name = "获取文件数据", path = "findByOdata", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SysFileDto> findByOdata(HttpServletRequest request) {
        PageModelDto<SysFileDto> sysFileDtos = null;
        try {
            ODataObj odataObj = new ODataObj(request);
            sysFileDtos = fileService.get(odataObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysFileDtos;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据业务ID获取附件", path = "findByBusinessId", method = RequestMethod.POST)
    @ResponseBody
    public List<SysFileDto> findByBusinessId(@RequestParam(required = true) String businessId) {
        List<SysFileDto> sysfileDto = fileService.findByBusinessId(businessId);
        return sysfileDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据主模块ID获取附件", path = "findByMainId", method = RequestMethod.POST)
    @ResponseBody
    public List<SysFileDto> findByMainId(@RequestParam(required = true) String mainId) {
        List<SysFileDto> sysfileDto = fileService.findByMainId(mainId);
        return sysfileDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "点树形图文件夹获取附件", path = "queryFile", method = RequestMethod.POST)
    @ResponseBody
    public List<SysFileDto> queryFile(@RequestParam String mainId, @RequestParam String sysBusiType) {
        List<SysFileDto> sysfileDto = null;
        try {
            sysfileDto = fileService.queryFile(mainId, sysBusiType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysfileDto;
    }

    /**
     * @param request
     * @param multipartFile
     * @param businessId    业务ID
     * @param mainId        主ID
     * @param sysfileType   附件模块类型
     * @param sysBusiType   附件业务类型
     * @return
     * @throws IOException
     */
    @RequiresAuthentication
    @RequestMapping(name = "文件上传", path = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                            @RequestParam(required = true) String businessId, String mainId, String mainType,
                            String sysfileType, String sysBusiType) {
        ResultMsg resultMsg = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            fileType = fileType.toLowerCase();      //统一转成小写
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            if (Validate.isString(sysBusiType)) {
                boolean result = FtpUtil.uploadFile(propertyUtil.readProperty(FTP_IP1), Integer.parseInt(propertyUtil.readProperty(FTP_PORT1)),
                        propertyUtil.readProperty(FTP_USER), propertyUtil.readProperty(FTP_PWD), propertyUtil.readProperty(FTP_BASE_PATH), "",
                        new String(fileName.getBytes("GBK"), "ISO-8859-1"), multipartFile.getInputStream());

                if (result) {
                    resultMsg = fileService.saveToFtp(multipartFile.getBytes(), fileName, businessId, fileType, mainId, mainType, sysfileType, sysBusiType, propertyUtil.readProperty(FTP_IP1), propertyUtil.readProperty(FTP_PORT1), propertyUtil.readProperty(FTP_USER), propertyUtil.readProperty(FTP_PWD), propertyUtil.readProperty(FTP_BASE_PATH), "");
                } else {
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "附件上传失败，连接ftp服务失败，请核查！");
                }
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "附件上传失败，请选择文件类型！");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMsg;
    }


    /**
     * 文件上传保留本地（预留）
     *
     * @param request
     * @param multipartFile
     * @param businessId    业务ID
     * @param mainId        主ID
     * @param sysfileType   附件模块类型
     * @param sysBusiType   附件业务类型
     * @return
     * @throws IOException
     */
    @RequiresAuthentication
    @RequestMapping(name = "文件上传", path = "fileUploadLocal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg uploadLocal(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                                 @RequestParam(required = true) String businessId, String mainId, String mainType,
                                 String sysfileType, String sysBusiType) {
        ResultMsg resultMsg = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            fileType = fileType.toLowerCase();      //统一转成小写

            if (!multipartFile.isEmpty()) {
                resultMsg = fileService.save(multipartFile.getBytes(), fileName, businessId, fileType, mainId, mainType, sysfileType, sysBusiType);
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件上传失败，无法获取文件信息！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMsg;
    }


    /**
     * @return
     * @throws IOException
     */
    @RequiresAuthentication
    @RequestMapping(name = "ftp文件同步", path = "fileSysUpload", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg fileSysUpload(String sysFileId) {
        logger.debug("==================ftp文件同步==================");
        ResultMsg resultMsg = null;
        try {
            SysFile sysFile = fileService.findFileById(sysFileId);
            //文件路径
            String filePath = SysFileUtil.getUploadPath() + File.separator + sysFile.getShowName();
            filePath = filePath.replaceAll("\\\\", "/");
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);

            boolean result = FtpUtil.uploadFile(sysFile.getFtpIp(), Integer.parseInt(sysFile.getPort()),
                    sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(), "",
                    new String(sysFile.getShowName().getBytes("GBK"), "ISO-8859-1"), fileInputStream);
            if (result) {
                SysFileDto sysFileDto = new SysFileDto();
                sysFile.setModifiedBy(SessionUtil.getDisplayName());
                sysFile.setModifiedDate(new Date());
                fileService.update(sysFile);
                BeanCopierUtils.copyProperties(sysFile, sysFileDto);
                resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "文件同步成功！", sysFileDto);
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件同步失败！");
            }
            //删除本地附件
            //SysFileUtil.delete(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "ftp文件校验", path = "fileSysCheck", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg checkFtpFile(@RequestParam(required = true) String sysFileId) {
        logger.debug("==================ftp文件校验==================");
        ResultMsg resultMsg = null;
        try {
            SysFile sysFile = fileService.findFileById(sysFileId);
            //下载ftp服务器附件到本地
            Boolean flag = FtpUtil.downloadFile(sysFile.getFtpIp(), sysFile.getPort() != null ? Integer.parseInt(sysFile.getPort()) : 0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                    sysFile.getShowName(), SysFileUtil.getUploadPath());
            if (flag) {
                resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "", "");
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "ftp服务器上不存在该文件，请核查！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMsg;
    }

    /*@RequiresAuthentication
    @RequestMapping(name = "文件下载", path = "fileDownload/{sysfileId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> download(@PathVariable("sysfileId") String sysfileId,
                                           Model model,HttpServletRequest request) throws Exception {
        SysFile sysFile = fileService.findFileById(sysfileId);
        //文件路径
        String filePath = SysFileUtil.getUploadPath() + File.separator + sysFile.getShowName();
        //下载ftp服务器附件到本地
        *//*Boolean flag = FtpUtil.downloadFile(sysFile.getFtpIp(), sysFile.getPort() != null ? Integer.parseInt(sysFile.getPort()) : 0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                sysFile.getShowName(), SysFileUtil.getUploadPath());
*//*
        File file = new File("D:\\评审中心文档\\ISO9001.rar");
        //下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = new String(sysFile.getShowName().getBytes("UTF-8"), "iso-8859-1");
        //通知浏览器以attachment（下载方式）打开图片
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "ISO9001.rar");
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
    }*/

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
            String filePath = SysFileUtil.getUploadPath() + File.separator + sysFile.getShowName();
            // filePath = filePath.replaceAll("\\\\", "/");
            //下载ftp服务器附件到本地
            Boolean flag = FtpUtil.downloadFile(sysFile.getFtpIp(), sysFile.getPort() != null ? Integer.parseInt(sysFile.getPort()) : 0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
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
    public void delete(@RequestParam(required = true) String sysFileId) {
        fileService.deleteById(sysFileId);
    }

    /**
     * 针对pdf和图片文件
     *
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
        File downFile = null;
        File file = null;
        InputStream inputStream = null;
        try {
            String filePath = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + sysFile.getShowName();
            filePath = filePath.replaceAll("\\\\", "/");

            //下载ftp服务器附件到本地
            Boolean flag = FtpUtil.downloadFile(sysFile.getFtpIp(), sysFile.getPort() != null ? Integer.parseInt(sysFile.getPort()) : 0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                    sysFile.getShowName(), SysFileUtil.getUploadPath());
            downFile = new File(filePath);
            if (!downFile.exists()) {
                file = new File(realPathResolver.get(Constant.plugin_file_path) + File.separator + "nofile.png");
                sysFile.setFileType(".png");
            } else {
                if (!Template.PDF_SUFFIX.getKey().equals(sysFile.getFileType())) {
                    filePath = filePath.substring(0, filePath.lastIndexOf(".")) + Template.PDF_SUFFIX.getKey();
                    if (file != null) {
                        OfficeConverterUtil.convert2PDF(downFile.getAbsolutePath(), filePath);
                    }
                    file = new File(filePath);
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                } else {
                    file = downFile;
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                }
            }

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);  //读取文件流
            inputStream.close();

            response.reset();  //重置结果集
            switch (sysFile.getFileType()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (file != null) {
                    Tools.deleteFile(file);
                }
                if (downFile != null) {
                    Tools.deleteFile(downFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印预览
     *
     * @param businessId
     * @param businessType
     * @param response
     */
    @RequiresAuthentication
    @RequestMapping(name = "打印预览", path = "printPreview/{businessId}/{businessType}/{stageType}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void printPreview(@PathVariable String businessId, @PathVariable String businessType,@PathVariable String stageType,HttpServletResponse response) {
        InputStream inputStream = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Template.WORD_SUFFIX.getKey();
            String filePath = path.substring(0, path.lastIndexOf(".")) + Template.PDF_SUFFIX.getKey();
            String fileName = "";
            switch (businessType) {
                case "SIGN":
                    Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
                    Map<String, Object> dataMap = TemplateUtil.entryAddMap(sign);
                    file = TemplateUtil.createDoc(dataMap, Constant.Template.STAGE_SUG_SIGN.getKey(), path);
                    break;

                case "WORKPROGRAM":
                    WorkProgramDto workProgramDto = workProgramService.initWorkProgramById(businessId);
                    WorkProgram workProgram  = new WorkProgram();
                    BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto , workProgram);
                    if(!Validate.isString(workProgram.getIsHaveEIA())){
                        workProgram.setIsHaveEIA("0");
                    }
                    Map<String , Object> workData = TemplateUtil.entryAddMap(workProgram);
                    List<ExpertDto> expertDtoList = workProgramDto.getExpertDtoList();
                    ExpertDto[] expertDtos = new ExpertDto[10];
                    if(expertDtoList!=null && expertDtoList.size()>0) {
                        for (int i = 0; i < expertDtoList.size(); i++) {
                            expertDtos[i] = expertDtoList.get(i);
                        }
                    }
                    String addressName = "";
                    String rbDate = "";
                    List<RoomBookingDto> roomBookingDtoList =  workProgramDto.getRoomBookingDtos();
                    if(roomBookingDtoList!= null && roomBookingDtoList.size()>0){
                        addressName = workProgramDto.getRoomBookingDtos().get(0).getAddressName();
                        rbDate = workProgramDto.getRoomBookingDtos().get(0).getRbDate();
                    }

                    int count = signBranchRepo.countBranch(workProgramDto.getSignId());
                    workData.put("expertList" , expertDtos);//聘请专家
                    workData.put("works" , count);//控制是否多个分支
                    workData.put("addressName" ,addressName );//会议室名称
                    workData.put("rbDate" , rbDate);//评审会时间
                    workData.put("studyBeginTimeStr" , DateUtils.getTimeNow(workProgram.getStudyBeginTime()));//调研开始时间
                    workData.put("studyEndTimeStr" , DateUtils.getTimeNow(workProgram.getStudyEndTime()));//调研结束时间
                    if(null!=stageType && (stageType.equals("STAGESUG") || stageType.equals("STAGESTUDY")|| stageType.equals("STAGEBUDGET")|| stageType.equals("STAGEOTHER") )){
                        if(stageType.equals("STAGESUG") ){
                            workData.put("wpTile","项目建议书评审工作方案");
                            workData.put("wpCode"," QR-4.3-02-A3");
                        }else if(stageType.equals("STAGESTUDY") ){
                            workData.put("wpTile","可行性研究报告评审工作方案");
                            workData.put("wpCode"," QR-4.4-01-A3");
                        }else if(stageType.equals("STAGEBUDGET") ){
                            workData.put("wpTile","项目概算评审工作方案");
                            workData.put("wpCode"," QR-4.7-01-A2");
                        }
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_SUG_WORKPROGRAM.getKey(), path);
                    }else if(null!=stageType && stageType.equals("STAGEREPORT")){
                        workData.put("wpTile","资金申请报告工作方案");
                        workData.put("wpCode","QR-4.9-02-A0");
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_REPORT_WORKPROGRAM.getKey(), path);
                    }else if(null!=stageType && stageType.equals("STAGEDEVICE")){
                        workData.put("wpTile","进口设备工作方案");
                        workData.put("wpCode","QR-4.9-02-A0");
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_DEVICE_WORKPROGRAM.getKey(), path);
                    }else if(null!=stageType && stageType.equals("STAGEIMPORT")){
                        workData.put("wpTile","设备清单工作方案");
                        workData.put("wpCode","QR-4.9-02-A0");
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_HOMELAND_WORKPROGRAM.getKey(), path);
                    }
                    break;
                default:
                    ;
            }

            if (file != null) {
                OfficeConverterUtil.convert2PDF(path, filePath);
            }

            printFile = new File(filePath);
            inputStream = new BufferedInputStream(new FileInputStream(printFile));

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);  //读取文件流
            inputStream.close();

            response.reset();  //重置结果集
            response.setContentType("application/pdf");
            response.addHeader("Content-Length", "" + printFile.length());  //返回头 文件大小
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));

            //获取返回体输出权
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            os.write(buffer); // 输出文件
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (file != null) {
                    Tools.deleteFile(file);
                }
                if (printFile != null) {
                    Tools.deleteFile(printFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresAuthentication
    @RequestMapping(name = "下载服务器Word", path = "html/download", method = RequestMethod.GET)
    public String downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        if (fileName != null) {
            try {
                fileName = java.net.URLDecoder.decode(java.net.URLDecoder.decode(fileName, "UTF-8"), "UTF-8");
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
     *
     * @param model
     * @param sysFileId
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "附件编辑", path = "editFile", method = RequestMethod.GET)
    public String editFile(Model model, @RequestParam(required = true) String sysFileId, HttpServletRequest request) {
        SysFile sysFile = fileService.findFileById(sysFileId);
        //文件路径
        String filePath = SysFileUtil.getUploadPath();
        filePath = filePath.replaceAll("\\\\", "/");
        //下载ftp服务器附件到本地服務
        Boolean flag = FtpUtil.downloadFile(sysFile.getFtpIp(), sysFile.getPort() != null ? Integer.parseInt(sysFile.getPort()) : 0, sysFile.getFtpUser(), sysFile.getFtpPwd(), sysFile.getFtpBasePath(),
                sysFile.getShowName(), SysFileUtil.getUploadPath());
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        model.addAttribute("filePath", basePath + "/file/html/download");
        model.addAttribute("uploadFile", basePath + "/contents/uploadFile.jsp");
        model.addAttribute("fileName", sysFile.getShowName());
        model.addAttribute("sysFileId", sysFileId);
        //文件类型
        String fileTyp;
        switch (sysFile.getFileType().toLowerCase()) {
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
        model.addAttribute("fileType", fileTyp);
        //附件信息
        model.addAttribute("sysFile", sysFile);
        if ("pdf".equals(fileTyp)) {
            return "weboffice/reader_pdf";
        } else {
            model.addAttribute("pluginFilePath", Constant.plugin_file_path + "/weboffice.rar");
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
        File parent = new File(realPathResolver.get(Constant.plugin_file_path));
        if (parent.exists()) {
            File flist[] = parent.listFiles();
            for (File f : flist) {
                if (!f.isDirectory() && !f.getName().toLowerCase().endsWith("png")) {
                    list.add(new PluginFileDto(f, Constant.plugin_file_path));
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
        File parent = new File(realPathResolver.get(Constant.plugin_file_path));
        if (parent.exists()) {
            File flist[] = parent.listFiles();
            for (File f : flist) {
                if (!f.isDirectory() && !f.getName().toLowerCase().endsWith("png")) {
                    list.add(new PluginFileDto(f, Constant.plugin_file_path));
                }
            }
        }
        return list;
    }
}
