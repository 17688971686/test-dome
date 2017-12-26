package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.ahelper.RealPathResolver;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.domain.project.*;
import cs.domain.sys.*;
import cs.model.PageModelDto;
import cs.model.archives.ArchivesLibraryDto;
import cs.model.expert.*;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.*;
import cs.model.sys.PluginFileDto;
import cs.model.sys.SysFileDto;
import cs.model.topic.FilingDto;
import cs.model.topic.TopicInfoDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.service.archives.ArchivesLibraryService;
import cs.service.expert.ExpertService;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.project.*;
import cs.service.sys.SysFileService;
import cs.service.topic.TopicInfoService;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
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
import javax.xml.ws.Dispatch;
import java.io.*;
import java.util.*;
import cs.domain.sys.Ftp;
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
    private SignService signService;

    @Autowired
    private WorkProgramService workProgramService;

    @Autowired
    private SignBranchRepo signBranchRepo;

    @Autowired
    private FileRecordService fileRecordService;

    @Autowired
    private DispatchDocRepo dispatchDocRepo;

    @Autowired
    private ExpertReviewRepo expertReviewRepo;

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;

    @Autowired
    private FtpRepo ftpRepo;

    @Autowired
    private ExpertService expertService;

    @Autowired
    private TopicInfoService topicInfoService;

    @Autowired
    private AddSuppLetterService addSuppLetterService;

    @Autowired
    private ArchivesLibraryService archivesLibraryService ;

    @Autowired
    private AddRegisterFileService addRegisterFileService;

    @Autowired
    private FileRecordRepo fileRecordRepo;


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
     * @param multipartFileList
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
    public ResultMsg upload(HttpServletRequest request,@RequestParam(name="file") MultipartFile[] multipartFileList,
                            @RequestParam(required = true) String businessId, String mainId, String mainType,
                            String sysfileType, String sysBusiType) {
        ResultMsg resultMsg = null;
        if(multipartFileList == null || multipartFileList.length == 0){
            resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"请选择要上传的附件");
            return resultMsg;
        }
        if (!Validate.isString(sysBusiType)) {
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "附件上传失败，请选择文件类型！");
            return resultMsg;
        }

        StringBuffer errorMsg = new StringBuffer();
        try {
            String fileUploadPath = SysFileUtil.getUploadPath();
            String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath, mainType, mainId, sysBusiType, null);
            //连接ftp
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            Ftp f = ftpRepo.findById(Ftp_.ipAddr.getName(),propertyUtil.readProperty(FTP_IP1));
            boolean linkSucess = FtpUtil.connectFtp(f);
            if (linkSucess) {
                for(MultipartFile multipartFile : multipartFileList){
                    String fileName = multipartFile.getOriginalFilename();
                    String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    //统一转成小写
                    fileType = fileType.toLowerCase();
                    //上传到ftp,
                    String uploadFileName = Tools.generateRandomFilename().concat(fileType);
                    linkSucess = FtpUtil.uploadFile(relativeFileUrl, uploadFileName, multipartFile.getInputStream());
                    if (linkSucess) {
                        //保存数据库记录
                        resultMsg = fileService.saveToFtp(multipartFile.getSize(), fileName, businessId, fileType,
                                relativeFileUrl + "/" + uploadFileName, mainId, mainType, sysfileType, sysBusiType, f);
                    } else {
                        errorMsg.append(fileName+"附件上传失败，无法上传到文件服务器！");
                    }
                }
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "附件上传失败，连接ftp服务失败，请核查！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "附件上传失败，连接ftp服务失败，请核查！");
        }finally {
            FtpUtil.closeFtp();
        }
        resultMsg.setReMsg(resultMsg.getReMsg()+errorMsg.toString());
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
                resultMsg = fileService.save(multipartFile, fileName, businessId, fileType, mainId, mainType, sysfileType, sysBusiType);
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
            //连接ftp
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            Ftp f = ftpRepo.findById(Ftp_.ipAddr.getName(),propertyUtil.readProperty(FTP_IP1));
            boolean linkSucess = FtpUtil.connectFtp(f);
            if (linkSucess) {
                SysFile sysFile = fileService.findFileById(sysFileId);
                //获取相对路径
                String fileUrl = sysFile.getFileUrl();
                fileUrl = FtpUtil.processDir(fileUrl);
                String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
                String storeFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
                //文件路径
                String filePath = SysFileUtil.getUploadPath() + "/" + storeFileName;
                File file = new File(filePath);
                FileInputStream fileInputStream = new FileInputStream(file);
                boolean result = FtpUtil.uploadFile(removeRelativeUrl, storeFileName, fileInputStream);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "ftp文件校验", path = "fileSysCheck", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg checkFtpFile(@RequestParam(required = true) String sysFileId) {
        ResultMsg resultMsg;
        try {
            SysFile sysFile = fileService.findFileById(sysFileId);
            //连接ftp
            Ftp f = sysFile.getFtp();
            boolean linkSucess = FtpUtil.connectFtp(f);
            if (linkSucess) {
                //获取相对路径
                String fileUrl = sysFile.getFileUrl();
                fileUrl = FtpUtil.processDir(fileUrl);
                String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
                String storeFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
                if (FtpUtil.checkFileExist(removeRelativeUrl, storeFileName)) {
                    resultMsg = new ResultMsg(true, MsgCode.OK.getValue(), "附件存在，可以进行下载操作！");
                } else {
                    resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "没有该附件！");
                }
            } else {
                resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "文件下载失败，无法连接文件服务器！");
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "文件下载失败，请联系管理员查看！");
        }

        return resultMsg;
    }

    /**
     * 用于其他项目下载附件
     * @param sysfileId
     * @param model
     * @param response
     * @throws Exception
     */
    @RequestMapping(name = "外链文件下载", path = "remoteDownload/{sysfileId}", method = RequestMethod.GET)
    public void remoteDownload(@PathVariable("sysfileId") String sysfileId, Model model, HttpServletResponse response) throws Exception {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //连接ftp
            SysFile sysFile = fileService.findFileById(sysfileId);
            //连接ftp
            Ftp f = sysFile.getFtp();
            boolean linkSucess = FtpUtil.connectFtp(f);
            if(linkSucess){
                //获取相对路径
                String fileUrl = sysFile.getFileUrl();
                fileUrl = FtpUtil.processDir(fileUrl);
                String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
                String storeFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
                //涉及到中文问题 根据系统实际编码改变
                removeRelativeUrl = new String(removeRelativeUrl.getBytes("GBK"), "iso-8859-1");
                storeFileName = new String(storeFileName.getBytes("GBK"), "iso-8859-1");
                //切换工作路径
                boolean changedir = FtpUtil.getFtp().changeWorkingDirectory(removeRelativeUrl);
                if (changedir) {
                    switch (sysFile.getFileType()) {
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
                            + new String(sysFile.getShowName().getBytes("GB2312"), "ISO8859-1"));

                    FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
                    //中文
                    conf.setServerLanguageCode("zh");
                    FTPFile[] files = FtpUtil.getFtp().listFiles();
                    for (int i = 0; i < files.length; i++) {
                        FTPFile ff = files[i];
                        if (storeFileName.equals(ff.getName())) {
                            FtpUtil.getFtp().retrieveFile(ff.getName(), out);
                            break;
                        }
                    }
                }
            }else{
                String resultMsg = "连接文件服务器失败！";
                out.write(resultMsg.getBytes());
            }

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                FtpUtil.closeFtp();
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "文件下载", path = "fileDownload", method = RequestMethod.GET)
    public void fileDownload(@RequestParam(required = true) String sysfileId, HttpServletResponse response) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            SysFile sysFile = fileService.findFileById(sysfileId);
            //获取相对路径
            String fileUrl = sysFile.getFileUrl();
            fileUrl = FtpUtil.processDir(fileUrl);
            String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
            String storeFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
            //涉及到中文问题 根据系统实际编码改变
            removeRelativeUrl = new String(removeRelativeUrl.getBytes("GBK"), "iso-8859-1");
            storeFileName = new String(storeFileName.getBytes("GBK"), "iso-8859-1");
            //切换工作路径
            boolean changedir = FtpUtil.getFtp().changeWorkingDirectory(removeRelativeUrl);
            if (changedir) {
                switch (sysFile.getFileType()) {
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
                        + new String(sysFile.getShowName().getBytes("GB2312"), "ISO8859-1"));

                FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
                //中文
                conf.setServerLanguageCode("zh");
                FTPFile[] files = FtpUtil.getFtp().listFiles();
                for (int i = 0; i < files.length; i++) {
                    FTPFile f = files[i];
                    if (storeFileName.equals(f.getName())) {
                        FtpUtil.getFtp().retrieveFile(f.getName(), out);
                        break;
                    }
                }
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                FtpUtil.closeFtp();
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //@RequiresPermissions("file#deleteSysFile#delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除系统文件", path = "deleteSysFile", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteSysFile(@RequestParam String id) {
       return fileService.deleteById(id);
    }

   /* @RequiresAuthentication
    @RequestMapping(name = "文件删除", path = "delete", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(required = true) String sysFileId) {
        fileService.deleteById(sysFileId);
    }*/

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
        //文件路径
        File downFile = null;
        File file = null;
        InputStream inputStream = null;
        OutputStream out = null;
        try {
            SysFile sysFile = fileService.findFileById(sysFileId);
            //获取相对路径
            String fileUrl = sysFile.getFileUrl();
            String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
            String storeFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
            //涉及到中文问题 根据系统实际编码改变
            removeRelativeUrl = new String(removeRelativeUrl.getBytes("GBK"), "iso-8859-1");
            storeFileName = new String(storeFileName.getBytes("GBK"), "iso-8859-1");
            //切换工作路径
            boolean changedir = FtpUtil.getFtp().changeWorkingDirectory(removeRelativeUrl);
            if (changedir) {
                //下载ftp服务器附件到本地
                FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
                //中文
                conf.setServerLanguageCode("zh");
                FTPFile[] files = FtpUtil.getFtp().listFiles();
                boolean isFtpFile = Template.PDF_SUFFIX.getKey().equals(sysFile.getFileType());
                for (int i = 0; i < files.length; i++) {
                    FTPFile f = files[i];
                    if (storeFileName.equals(f.getName())) {
                        //如果是pdf文件，直接输出流，否则要先转为pdf
                        if(isFtpFile || sysFile.getFileType().equals(".png") || sysFile.getFileType().equals(".jpg")|| sysFile.getFileType().equals(".gif")){
                            out = response.getOutputStream();
                            FtpUtil.getFtp().retrieveFile(f.getName(), out);
                        }else{
                            downFile = new File(SysFileUtil.getUploadPath() + "/" + storeFileName);
                            out = new FileOutputStream(downFile);
                            FtpUtil.getFtp().retrieveFile(f.getName(), out);
                            String filePath = downFile.getAbsolutePath();
                            filePath = filePath.substring(0, filePath.lastIndexOf(".")) + Template.PDF_SUFFIX.getKey();
                            if (downFile != null) {
                                OfficeConverterUtil.convert2PDF(downFile.getAbsolutePath(), filePath);
                            }
                            file = new File(filePath);
                            inputStream = new BufferedInputStream(new FileInputStream(file));
                            byte[] buffer = new byte[inputStream.available()];
                            inputStream.read(buffer);  //读取文件流
                            inputStream.close();
                            out = new BufferedOutputStream(response.getOutputStream());
                            out.write(buffer); // 输出文件
                        }

                        break;
                    }
                }
            }else{
                file = new File(realPathResolver.get(Constant.plugin_file_path) + "/" + "nofile.png");
                sysFile.setFileType(".png");
            }

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
            //response.addHeader("Content-Length", "" + file.length());  //返回头 文件大小
            response.setHeader("Content-Disposition", "inline;filename=" + new String(sysFile.getShowName().getBytes(), "ISO-8859-1"));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
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
    public void printPreview(@PathVariable String businessId, @PathVariable String businessType , @PathVariable String stageType, HttpServletResponse response) {
        InputStream inputStream = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + "/" + Tools.generateRandomFilename() + Template.WORD_SUFFIX.getKey();
            String filePath = path.substring(0, path.lastIndexOf(".")) + Template.PDF_SUFFIX.getKey();
            String fileName = "";
            switch (businessType) {
                case "SIGN":
                    Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
                    Map<String, Object> dataMap = TemplateUtil.entryAddMap(sign);
                    if(stageType.equals(RevireStageKey.KEY_SUG.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_STUDY.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_OTHER.getValue())){
                        //建议书、可研、其他
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.STAGE_SUG_SIGN.getKey(), path);
                    }else if(stageType.equals(RevireStageKey.KEY_BUDGET.getValue())){
                        //概算
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.STAGE_BUDGET_SIGN.getKey(), path);
                    }else if(stageType.equals(Constant.RevireStageKey.KEY_REPORT.getValue())){
                        //资金
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.APPLY_REPORT_SIGN.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_DEVICE.getValue())){
                        //进口
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.IMPORT_DEVICE_SIGN.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_HOMELAND.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_IMPORT.getValue())){
                        //设备清单（国产、进口）
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.DEVICE_BILL_SIGN.getKey(), path);

                    }
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
                        for (int i = 0; i < expertDtoList.size() && i<10; i++) {
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

//                    file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_SUG_WORKPROGRAM.getKey(), path);
                    break;
                case "FILERECORD" :
                    FileRecordDto fileRecordDto = fileRecordService.initBySignId( businessId);
                    Map<String , Object> fileData = TemplateUtil.entryAddMap(fileRecordDto);
                    if(stageType.equals(RevireStageKey.KEY_SUG.getValue())){
                        //建议书
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_SUG_FILERECORD.getKey(), path);
                    }else if(stageType.equals(RevireStageKey.KEY_STUDY.getValue())){
                        //可研
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_STUDY_FILERECORD.getKey(), path);

                    }else if(stageType.equals(RevireStageKey.KEY_BUDGET.getValue())){
                        //概算
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_BUDGET_FILERECORD.getKey(), path);

                    }else if("STAGEBUDGET_XS".equals(stageType)){
                        //概算协审
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_BUDGET_XS_FILERECORD.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_REPORT.getValue())){
                        //资金
                        file = TemplateUtil.createDoc(fileData, Template.APPLY_REPORT_FILERECORD.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_DEVICE.getValue())){
                        //进口
                        file = TemplateUtil.createDoc(fileData, Template.IMPORT_DEVICE_FILERECORD.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_HOMELAND.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_IMPORT.getValue())){
                        //设备清单（国产、进口）
                        AddRegisterFileDto[] registerFileDto = new AddRegisterFileDto[5];
                        if(fileRecordDto!=null && fileRecordDto.getRegisterFileDto() !=null && fileRecordDto.getRegisterFileDto().size()>0) {
                            for (int i = 0; i < fileRecordDto.getRegisterFileDto().size() && i < 5; i++) {
                                registerFileDto[i] = fileRecordDto.getRegisterFileDto().get(i);
                            }
                        }
                        fileData.put("registerFileList", registerFileDto);
                        file = TemplateUtil.createDoc(fileData, Template.DEVICE_BILL_FILERECORD.getKey(), path);

                    }
                    break;

                case "FILERECOED_OTHERFILE" :
                    //项目归档的其它资料
//                    FileRecordDto fileRecordDto2 = fileRecordService.initBySignId( businessId);
                    FileRecord fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName() , businessId);
                    Map<String , Object> otherFileData = new HashMap<>();
                    otherFileData.put("fileNo" , fileRecord.getFileNo());
                    otherFileData.put("projectName" , fileRecord.getProjectName());
                    otherFileData.put("projectCompany" , fileRecord.getProjectCompany());
                    otherFileData.put("projectCode" , fileRecord.getProjectCode());
                    otherFileData.put("OtherTitle" , "归档表");
                    List<AddRegisterFile> addRegisterFileList = new ArrayList<>();
                    //其它资料
                    if("OTHER_FILE".equals(stageType)){
                        otherFileData.put("otherFileType" , "其它资料");
                        addRegisterFileList = addRegisterFileService.findByBusIdAndBusType(businessId , 4);
                    }
                    //图纸资料
                    if("DRAWING_FILE".equals(stageType)){
                        otherFileData.put("otherFileType" , "图纸资料");
                        addRegisterFileList = addRegisterFileService.findByBusIdAndBusType(businessId , 2);

                    }
                    otherFileData.put("otherFileList" , addRegisterFileList);
                    file = TemplateUtil.createDoc(otherFileData, Template.OTHER_FILE.getKey(), path);
                    break;
                case "SIGN_OTHERFILE" :
                    //项目申报的其它资料
                    Sign signs = signRepo.findById(Sign_.signid.getName(), businessId);
                    Map<String , Object> otherFileDatas = new HashMap<>();
                    otherFileDatas.put("fileNo" , signs.getSignNum());
                    otherFileDatas.put("projectName" , signs.getProjectname());
                    otherFileDatas.put("projectCompany" , signs.getBuiltcompanyName());
                    otherFileDatas.put("projectCode" , signs.getProjectcode());
                    otherFileDatas.put("OtherTitle" , "报审登记");
                    List<AddRegisterFile> addRegisterFileLists = new ArrayList<>();
                    //其它资料
                    if("OTHER_FILE".equals(stageType)){
                        otherFileDatas.put("otherFileType" , "其它资料");
                        addRegisterFileLists = addRegisterFileService.findByBusIdAndBusType(businessId , 4);
                    }
                    //图纸资料
                    if("DRAWING_FILE".equals(stageType)){
                        otherFileDatas.put("otherFileType" , "图纸资料");
                        addRegisterFileLists = addRegisterFileService.findByBusIdAndBusType(businessId , 2);

                    }
                    otherFileDatas.put("otherFileList" , addRegisterFileLists);
                    file = TemplateUtil.createDoc(otherFileDatas, Template.OTHER_FILE.getKey(), path);
                    break;

                case "DISPATCHDOC":
                    DispatchDoc dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName() , businessId);
                    SignDto signDto = signService.findById(dispatchDoc.getSign().getSignid(),true);
                    Map<String , Object> dispatchData = TemplateUtil.entryAddMap(dispatchDoc);
                    String mianChargeSuggest = dispatchDoc.getMianChargeSuggest();
                    String main = null;
                    String second = null;
                    if(Validate.isString(mianChargeSuggest)){
                         main = mianChargeSuggest.replaceAll("<br>" , "<w:br />").replaceAll("&nbsp;" , "").replaceAll(" " , "");
                    }
                    String secondChargeSuggest = dispatchDoc.getSecondChargeSuggest();
                    if(Validate.isString(secondChargeSuggest)){
                         second = secondChargeSuggest.replaceAll("<br>" , "<w:br />").replaceAll("&nbsp;" , "").replaceAll(" " , "");
                    }
                    dispatchData.put("mianChargeSuggest" , main) ;
                    dispatchData.put("secondChargeSuggest" , second);

                    if(stageType.equals(RevireStageKey.KEY_SUG.getValue())){
                        //建议书
                        dispatchData.put("wpTile","项目建议书发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.STAGE_SUG_DISPATCHDOC.getKey(), path);
                    }else if(stageType.equals(RevireStageKey.KEY_STUDY.getValue())){
                        //可研
                        List<SignDto> signDtoList = signDto.getAssociateSignDtoList();
                        List<DispatchDocDto> dispatchList = new ArrayList<DispatchDocDto>();
                        List<DispatchDocDto> dispatchViewList = new ArrayList<DispatchDocDto>();
                        if(null != signDtoList){
                            for(int i=0;i<signDtoList.size();i++){
                                if(null != signDtoList.get(i).getDispatchDocDto()){
                                    dispatchList.add(signDtoList.get(i).getDispatchDocDto());
                                }
                            }
                        }
                        dispatchViewList.add(getDispatchStage("项目建议书",dispatchList));
                        dispatchData.put("dispatchList",dispatchViewList);
                        file = TemplateUtil.createDoc(dispatchData, Template.STAGE_STUDY_DISPATCHDOC.getKey(), path);

                    }else if(stageType.equals(RevireStageKey.KEY_BUDGET.getValue())){
                        //概算
                        List<SignDto> signDtoList = signDto.getAssociateSignDtoList();
                        List<DispatchDocDto> dispatchList = new ArrayList<DispatchDocDto>();
                        List<DispatchDocDto> dispatchViewList = new ArrayList<DispatchDocDto>();
                        if(null != signDtoList){
                            for(int i=0;i<signDtoList.size();i++){
                                if(null != signDtoList.get(i).getDispatchDocDto()){
                                    dispatchList.add(signDtoList.get(i).getDispatchDocDto());
                                }
                            }
                        }
                            dispatchViewList.add(getDispatchStage("项目建议书",dispatchList));
                            dispatchViewList.add(getDispatchStage("可行性研究报告",dispatchList));
                            dispatchData.put("dispatchList",dispatchViewList);
                        file = TemplateUtil.createDoc(dispatchData, Template.STAGE_BUDGET_DISPATCHDOC.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_REPORT.getValue())){
                        //资金
                        dispatchData.put("wpTile","资金申请报告发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.APPLY_REPORT_DISPATCHDOC.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_DEVICE.getValue())){
                        //进口
                        dispatchData.put("wpTile","进口设备发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.IMPORT_DEVICE_DISPATCHDOC.getKey(), path);

                    }else if(stageType.equals(Constant.RevireStageKey.KEY_HOMELAND.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_IMPORT.getValue())){
                        //设备清单（国产、进口）
                        dispatchData.put("wpTile","设备清单发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.DEVICE_BILL_DISPATCHDOC.getKey(), path);
                    }
                    break;

                case "SIGN_EXPERT":
                    //项目环节的专家评分和专家评审费
                    Map<String , Object> expertData = new HashMap<>();
                    ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);
                    List<ExpertSelectedDto> expertSelectedList = new ArrayList<>();
                    if (Validate.isObject(expertReview)) {
                        ExpertReviewDto expertReviewDto = expertReviewRepo.formatReview(expertReview);
                        List<ExpertSelectedDto> expertSelectedDtoList = expertReviewDto.getExpertSelectedDtoList();
                        if(expertSelectedDtoList != null && expertSelectedDtoList.size() >0){
                            for(ExpertSelectedDto expertSelectedDto : expertSelectedDtoList){
                                ExpertSelectedDto expertSelected = new ExpertSelectedDto();
                                if(EnumState.YES.getValue().equals(expertSelectedDto.getIsJoin())
                                        && EnumState.YES.getValue().equals(expertSelectedDto.getIsConfrim())){
                                    BeanCopierUtils.copyPropertiesIgnoreNull(expertSelectedDto, expertSelected);
                                    expertSelectedList.add(expertSelected);
                                }
                            }
                        }
                        expertData.put("projectName" , expertReview.getReviewTitle().substring(1,expertReview.getReviewTitle().length()-1));
                        expertData.put("expertList" , expertSelectedList);
                    }
                    //专家评审费发放表
                    if("SIGN_EXPERT_PAY".equals(stageType)){

                        file = TemplateUtil.createDoc(expertData, Template.EXPERT_PAYMENT.getKey(), path);
                    }
                    //专家评分
                    if("SIGN_EXPERT_SCORE".equals(stageType)){
                        file = TemplateUtil.createDoc(expertData, Template.EXPERT_SCORD.getKey(), path);
                    }
                    break;
                case  "ADDSUPPLETER":
                    //拟补充资料函
                    AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName() , businessId);
                    Map<String , Object> addsuppletterData = TemplateUtil.entryAddMap(addSuppLetter);
                    String leaderSignIdea = addSuppLetter.getLeaderSignIdeaContent().replaceAll("<br>" , "").replaceAll("&nbsp;" , "");
                    addsuppletterData.put("leaderSignIdea" , leaderSignIdea);
                    file = TemplateUtil.createDoc(addsuppletterData, Template.ADDSUPPLETER.getKey(), path);
                    break;

                case "EXPERT" :
                    //专家申请表
                    ExpertDto expertDto = expertService.findById( businessId);
                    Expert expert = new Expert();
                    BeanCopierUtils.copyPropertiesIgnoreNull(expertDto , expert);
                    Map<String , Object> expertDataMap = TemplateUtil.entryAddMap(expert);
                    WorkExpeDto[] workExpes = new WorkExpeDto[4];
                    ProjectExpeDto[] projectExpes = new ProjectExpeDto[4];

                    if(expertDto.getWorkDtoList()!=null && expertDto.getWorkDtoList().size()>0){
                        for(int i=0 ; i<expertDto.getWorkDtoList().size() && i<4 ; i++){
                            WorkExpeDto workExpeDto = expertDto.getWorkDtoList().get(i);
                            workExpeDto.setBeginTimeStr(DateUtils.converToString(workExpeDto.getBeginTime() , "yyyyMMdd"));
                           workExpeDto.setEndTimeStr(DateUtils.converToString(workExpeDto.getEndTime() , "yyyyMmdd"));
                            workExpes[i] = workExpeDto;
                        }
                    }
                    if(expertDto.getProjectDtoList()!=null && expertDto.getProjectDtoList().size()>0){
                        for(int i=0 ; i<expertDto.getProjectDtoList().size() && i<4 ; i++){
                            projectExpes[i] = expertDto.getProjectDtoList().get(i);
                        }
                    }
                    expertDataMap.put("workList" , workExpes);
                    expertDataMap.put("projectList" , projectExpes);

                    file = TemplateUtil.createDoc(expertDataMap, Template.EXPERT.getKey(), path);

                    break;

                case "TOPICINFO":
                    //课题研究
                    TopicInfoDto topicInfoDto = topicInfoService.findById(businessId);
                    //归档
                    if("TOPICINFO_FILERECORD".equals(stageType)){
                        if(topicInfoDto!=null){
                            FilingDto filingDto= topicInfoDto.getFilingDto();
                            Map<String , Object> filingData = TemplateUtil.entryAddMap(filingDto);
                            AddRegisterFileDto[] registerFileDto = new AddRegisterFileDto[5];
                            if(filingDto!=null && filingDto.getRegisterFileDto() !=null && filingDto.getRegisterFileDto().size()>0) {
                                for (int i = 0; i < filingDto.getRegisterFileDto().size() && i < 5; i++) {
                                    registerFileDto[i] = filingDto.getRegisterFileDto().get(i);
                                }
                            }
                            filingData.put("registerFileList", registerFileDto);
                            file = TemplateUtil.createDoc(filingData, Template.TOPICINFO_FILERECORD.getKey(), path);
                        }
                    }

                    //工作方案
                    if("TOPICINFO_WORKPROGRAM".equals(stageType)){
                        if(topicInfoDto!=null){
                            WorkPlanDto workPlanDto = topicInfoDto.getWorkPlanDto();
                            Map<String , Object> workPlanData = TemplateUtil.entryAddMap(workPlanDto);
                            workPlanData.put("createdDate" , DateUtils.converToString(workPlanDto.getCreatedDate() , "yyyy年MM月dd日"));
                            String rbDateStr = "";
                            String address = "";
                            if(workPlanDto!=null && workPlanDto.getRoomDtoList()!=null && workPlanDto.getRoomDtoList().size()>0){
                                rbDateStr = workPlanDto.getRoomDtoList().get(0).getRbDate();
                                address = workPlanDto.getRoomDtoList().get(0).getAddressName();
                            }
                            workPlanData.put("rbDateStr", rbDateStr);
                            workPlanData.put("addRess", address);
                            ExpertDto[] expeDtos = new ExpertDto[10];
                            if(workPlanDto!=null && workPlanDto.getExpertDtoList()!=null && workPlanDto.getExpertDtoList().size()>0){
                                for(int i=0; i<workPlanDto.getExpertDtoList().size() && i<10 ; i++){
                                    expeDtos[i] = workPlanDto.getExpertDtoList().get(i);
                                }
                            }
                            workPlanData.put("expertList", expeDtos);
                            file = TemplateUtil.createDoc(workPlanData, Template.TOPICINFO_WORKPROGRAM.getKey(), path);
                        }
                    }

                    break;

                case "MONTHLY" :
                    //月报简报
                    AddSuppLetterDto addSuppLetterDto = addSuppLetterService.findById(businessId);
                    Map<String , Object> addSuppleterData = TemplateUtil.entryAddMap(addSuppLetterDto);
                    file = TemplateUtil.createDoc(addSuppleterData, Template.MONTHLY.getKey(), path);
                    break;

                case "ARCHIVES":
                    //借阅档案
                    ArchivesLibraryDto archivesLibraryDto = archivesLibraryService.findById(businessId);
                    Map<String , Object> archivesData = TemplateUtil.entryAddMap(archivesLibraryDto);
                    file = TemplateUtil.createDoc(archivesData, Template.ARCHIVES_DETAIL.getKey(), path);
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

    /**
     *
     * @param stageName
     * @param dispatchDocDtoList
     * @return
     */
    private DispatchDocDto getDispatchStage(String stageName,List<DispatchDocDto> dispatchDocDtoList){
        DispatchDocDto dispatchDocDto = new DispatchDocDto();
           if(dispatchDocDtoList.size()>0){
               for(int i = 0;i < dispatchDocDtoList.size();i++){
                   if(stageName.equals(dispatchDocDtoList.get(i).getDispatchStage())){
                       return dispatchDocDtoList.get(i);
                   }
                   if(i == (dispatchDocDtoList.size()-1)){
                       dispatchDocDto.setDispatchStage(stageName);
                       break;
                   }
               }
           }else{
               dispatchDocDto.setDispatchStage(stageName);
           }

        return dispatchDocDto;
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
        //获取相对路径
        String fileUrl = sysFile.getFileUrl();
        fileUrl = FtpUtil.processDir(fileUrl);
        String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
        String storeFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
        //涉及到中文问题 根据系统实际编码改变
        try{
            removeRelativeUrl = new String(removeRelativeUrl.getBytes("GBK"), "iso-8859-1");
            storeFileName = new String(storeFileName.getBytes("GBK"), "iso-8859-1");
        }catch (Exception e){
            e.printStackTrace();
        }

        //下载ftp服务器附件到本地服務
        Boolean flag = FtpUtil.downloadFile(sysFile.getFtp().getIpAddr(), sysFile.getFtp().getPort() != null ? sysFile.getFtp().getPort() : 0, sysFile.getFtp().getUserName(), sysFile.getFtp().getPwd(), removeRelativeUrl,
                storeFileName, SysFileUtil.getUploadPath());
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
