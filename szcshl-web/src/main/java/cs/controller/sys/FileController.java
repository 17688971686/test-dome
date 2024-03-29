package cs.controller.sys;

import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import cs.ahelper.MudoleAnnotation;
import cs.ahelper.RealPathResolver;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.ProjectConstant;
import cs.common.ftp.ConfigProvider;
import cs.common.ftp.FtpClientConfig;
import cs.common.ftp.FtpUtils;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.domain.project.*;
import cs.domain.sys.Ftp;
import cs.domain.sys.Ftp_;
import cs.domain.sys.Log;
import cs.domain.sys.SysFile;
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
import cs.repository.repositoryImpl.expert.ExpertOfferRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.service.archives.ArchivesLibraryService;
import cs.service.expert.ExpertService;
import cs.service.project.*;
import cs.service.restService.SignRestService;
import cs.service.sys.LogService;
import cs.service.sys.SysFileService;
import cs.service.topic.TopicInfoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.net.ftp.FTPClient;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static cs.common.constants.Constant.*;
import static cs.common.constants.ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET;


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
    private SysFileService sysFileService;
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
    private AddRegisterFileRepo addRegisterFileRepo;

    @Autowired
    private SysFileRepo sysFileRepo;

    @Autowired
    private FtpRepo ftpRepo;

    @Autowired
    private ExpertService expertService;

    @Autowired
    private TopicInfoService topicInfoService;

    @Autowired
    private AddSuppLetterService addSuppLetterService;

    @Autowired
    private ArchivesLibraryService archivesLibraryService;

    @Autowired
    private AddRegisterFileService addRegisterFileService;

    @Autowired
    private LogService logService;

    private ServletContext servletContext;

    @Autowired
    private ExpertOfferRepo expertOfferRepo;
    @Autowired
    private UnitScoreRepo unitScoreRepo;

    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;

    @Autowired
    private SignRestService signRestService;

    @Autowired
    private ProjectStopRepo projectStopRepo;

    @Autowired
    private FileRecordRepo fileRecordRepo;

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
            sysFileDtos = sysFileService.get(odataObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysFileDtos;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据业务ID获取附件", path = "findByBusinessId", method = RequestMethod.POST)
    @ResponseBody
    public List<SysFileDto> findByBusinessId(@RequestParam(required = true) String businessId) {
        List<SysFileDto> sysfileDto = sysFileService.findByBusinessId(businessId);
        return sysfileDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据主模块ID获取附件", path = "findByMainId", method = RequestMethod.POST)
    @ResponseBody
    public List<SysFileDto> findByMainId(@RequestParam(required = true) String mainId) {
        List<SysFileDto> sysfileDto = sysFileService.findByMainId(mainId);
        return sysfileDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "点树形图文件夹获取附件", path = "queryFile", method = RequestMethod.POST)
    @ResponseBody
    public List<SysFileDto> queryFile(@RequestParam String mainId, @RequestParam String sysBusiType) {
        List<SysFileDto> sysfileDto = null;
        try {
            sysfileDto = sysFileService.queryFile(mainId, sysBusiType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysfileDto;
    }

    /**
     * @param request
     * @param multipartFileList
     * @param businessId        业务ID
     * @param mainId            主ID
     * @param sysfileType       附件模块类型
     * @param sysBusiType       附件业务类型
     * @return
     * @throws IOException
     */
    @RequiresAuthentication
    @RequestMapping(name = "文件上传", path = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg upload(HttpServletRequest request, @RequestParam(name = "file") MultipartFile[] multipartFileList,
                            @RequestParam(required = true) String businessId, String mainId, String mainType,
                            String sysfileType, String sysBusiType) {
        if (multipartFileList == null || multipartFileList.length == 0) {
            return ResultMsg.error( "请选择要上传的附件");
        }
        //项目附件才需要类型
        if (SysFileType.SIGN.getValue().equals(mainType) && !Validate.isString(sysBusiType)) {
            return ResultMsg.error("附件上传失败，请选择文件类型！");
        }
        ResultMsg resultMsg = ResultMsg.error("");
        StringBuffer errorMsg = new StringBuffer();
        String fileUploadPath = SysFileUtil.getUploadPath();
        String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath, mainType, mainId, sysBusiType, null);

        Ftp f = ftpRepo.findById(Ftp_.ipAddr.getName(), sysFileService.findFtpId());
        FtpUtils ftpUtils = new FtpUtils();
        FtpClientConfig k = ConfigProvider.getUploadConfig(f);
        //上传到ftp,如果有根目录，则加入根目录
        if (Validate.isString(k.getFtpRoot())) {
            if (relativeFileUrl.startsWith(File.separator)) {
                relativeFileUrl = File.separator + k.getFtpRoot() + relativeFileUrl;
            } else {
                relativeFileUrl = File.separator + k.getFtpRoot() + relativeFileUrl + File.separator;
            }
        }
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtils.getFtpClient(ftpUtils.getFtpClientPool(),k);
            for (MultipartFile multipartFile : multipartFileList) {
                String fileName = multipartFile.getOriginalFilename();
                if (fileName.indexOf(".") == -1) {
                    errorMsg.append("附件【" + fileName + "】没有后缀名，无法上传到文件服务器！");
                    continue;
                }
                String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                //统一转成小写
                fileType = fileType.toLowerCase();
                String uploadFileName = "";
                SysFile sysFile = sysFileRepo.isExistFile(relativeFileUrl, fileName);
                //如果附件已存在
                if (null != sysFile) {
                    String fileUrl = sysFile.getFileUrl();
                    String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
                    if (relativeFileUrl.equals(removeRelativeUrl)) {
                        uploadFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
                    }
                } else {
                    uploadFileName = Tools.generateRandomFilename().concat(fileType);
                }
                boolean uploadResult = ftpUtils.putFile(ftpClient,k, relativeFileUrl, uploadFileName, multipartFile.getInputStream());
                if (uploadResult) {
                    //保存数据库记录
                    if (null != sysFile) {
                        sysFile.setModifiedBy(SessionUtil.getDisplayName());
                        sysFile.setModifiedDate(new Date());
                        sysFileService.update(sysFile);
                        resultMsg = ResultMsg.ok( "文件上传成功！");
                    } else {
                        resultMsg = sysFileService.saveToFtp(multipartFile.getSize(), fileName, businessId, fileType,
                                relativeFileUrl + File.separator + uploadFileName, mainId, mainType, sysfileType, sysBusiType, f);
                    }
                } else {
                    errorMsg.append("附件【" + fileName + "】上传失败，无法上传到文件服务器！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "附件上传失败，连接ftp服务失败，请核查！");
        } finally {
            /*try {
                if(ftpClient != null){
                    ftpUtils.getFtpClientPool().returnObject(k,ftpClient);
                }
            }catch (Exception ex){

            }*/
        }
        resultMsg.setReMsg(resultMsg.getReMsg() + errorMsg.toString());
        return resultMsg;
    }

    /**
     * 文件上传保留本地（预留）
     *
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
    public ResultMsg uploadLocal(@RequestParam("file") MultipartFile multipartFile, @RequestParam String businessId,
                                 String mainId, String mainType, String sysfileType, String sysBusiType) {
        ResultMsg resultMsg = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //统一转成小写
            fileType = fileType.toLowerCase();
            if (!multipartFile.isEmpty()) {
                resultMsg = sysFileService.save(multipartFile, fileName, businessId, fileType, mainId, mainType, sysfileType, sysBusiType);
            } else {
                resultMsg = ResultMsg.error("文件上传失败，无法获取文件信息！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "ftp文件校验", path = "fileSysCheck", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg checkFtpFile(@RequestParam String sysFileId) {
        ResultMsg resultMsg=null;
        SysFile sysFile = sysFileService.findFileById(sysFileId);
        String fileUrl = sysFile.getFileUrl();
        String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
        String checkFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
        //连接ftp
        Ftp f = sysFile.getFtp();
        FtpUtils ftpUtils = new FtpUtils();
        FtpClientConfig k = ConfigProvider.getDownloadConfig(f);
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtils.getFtpClient(ftpUtils.getFtpClientPool(),k);
            boolean checkResult = ftpUtils.checkFileExist(ftpClient,removeRelativeUrl, checkFileName, k);
            if (checkResult) {
                resultMsg = ResultMsg.ok("附件存在，可以进行下载操作！");
            } else {
                resultMsg = ResultMsg.error( "没有该附件！");
            }
        } catch (Exception e) {
            resultMsg = ResultMsg.error("附件确认异常，请联系管理员查看！");
        }finally {
            /*try {
                if(ftpClient != null){
                    ftpUtils.getFtpClientPool().returnObject(k,ftpClient);
                }
            }catch (Exception ex){

            }*/
        }
        return resultMsg;
    }

    /**
     * 用于其他项目下载附件
     *
     * @param sysfileId
     * @param response
     * @throws Exception
     */
    @RequestMapping(name = "外链文件下载", path = "remoteDownload/{sysfileId}", method = RequestMethod.GET)
    public void remoteDownload(@PathVariable("sysfileId") String sysfileId, HttpServletResponse response) throws Exception {
        OutputStream out = response.getOutputStream();
        SysFile sysFile = sysFileService.findFileById(sysfileId);
        ResponseUtils.setResponeseHead(sysFile.getFileType(), response);
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(sysFile.getShowName().getBytes("GB2312"), "ISO8859-1"));
        //获取相对路径
        String fileUrl = sysFile.getFileUrl();
        String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
        //连接ftp
        Ftp f = sysFile.getFtp();
        FtpUtils ftpUtils = new FtpUtils();
        FtpClientConfig k = ConfigProvider.getDownloadConfig(f);
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtils.getFtpClient(ftpUtils.getFtpClientPool(),k);
            boolean downResult = ftpUtils.downLoadFile(ftpClient,removeRelativeUrl, fileName, k, out);
            if (downResult) {

            } else {
                String resultMsg = "连接文件服务器失败！";
                out.write(resultMsg.getBytes());
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                /*if(ftpClient != null){
                    ftpUtils.getFtpClientPool().returnObject(k,ftpClient);
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "文件下载", path = "fileDownload", method = RequestMethod.POST)
    public void fileDownload(@RequestParam String sysfileId, HttpServletResponse response) throws Exception {
        OutputStream out = response.getOutputStream();
        SysFile sysFile = sysFileService.findFileById(sysfileId);
        ResponseUtils.setResponeseHead(sysFile.getFileType(), response);
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(sysFile.getShowName().getBytes("GB2312"), "ISO8859-1"));
        //获取相对路径
        String fileUrl = sysFile.getFileUrl();
        int seParatorIndex = fileUrl.lastIndexOf(File.separator);
        String removeRelativeUrl = fileUrl.substring(0, seParatorIndex);
        String fileName = fileUrl.substring(seParatorIndex + 1, fileUrl.length());
        //连接ftp
        Ftp f = sysFile.getFtp();
        FtpUtils ftpUtils = new FtpUtils();
        FtpClientConfig k = ConfigProvider.getDownloadConfig(f);
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtils.getFtpClient(ftpUtils.getFtpClientPool(),k);
            ftpUtils.downLoadFile(ftpClient,removeRelativeUrl, fileName, k, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                /*if(ftpClient != null){
                    ftpUtils.getFtpClientPool().returnObject(k,ftpClient);
                }*/
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
        return sysFileService.deleteById(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "文件删除", path = "delete", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(required = true) String sysFileId) {
        sysFileService.deleteById(sysFileId);
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
        //文件路径
        File downFile = null;
        File file = null;
        InputStream inputStream = null;
        OutputStream out = null;
        SysFile sysFile = sysFileService.findFileById(sysFileId);
        //获取相对路径
        String fileUrl = sysFile.getFileUrl();
        String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
        String storeFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
        String fileType = sysFile.getFileType().toLowerCase();
        //连接ftp
        Ftp ftp = sysFile.getFtp();
        FtpUtils ftpUtils = new FtpUtils();
        FtpClientConfig k = ConfigProvider.getDownloadConfig(ftp);
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtils.getFtpClient(ftpUtils.getFtpClientPool(),k);
            //需要转换的类型，自己在方法实现
            if (AsposeUtil.neddConvertToPdf(fileType)) {
                out = response.getOutputStream();
                if (AsposeUtil.getLicense()) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ftpUtils.downLoadFile(ftpClient,removeRelativeUrl, storeFileName, k, byteArrayOutputStream);
                    inputStream = IOStreamUtil.parse(byteArrayOutputStream);
                    switch (fileType) {
                        case ".doc":
                        case ".docx":
                            Document doc = new Document(inputStream);
                            doc.save(out, com.aspose.words.SaveFormat.PDF);
                            break;
                        case ".xls":
                        case ".xlsx":
                            Workbook wb = new Workbook(inputStream);
                            wb.save(out, com.aspose.cells.SaveFormat.PDF);
                            break;
                        case ".ppt":
                        case ".pptx":
                            Presentation pres = new Presentation(inputStream);//输入pdf路径
                            pres.save(out, com.aspose.slides.SaveFormat.Pdf);
                            break;
                        default:
                            ;
                    }
                }
            }
            //如果是pdf文件，直接输出流，否则要先转为pdf
            else if (".pdf".equals(fileType) || ".png".equals(fileType) || ".jpg".equals(fileType) || ".gif".equals(fileType) || ".txt".equals(fileType) || ".text".equals(fileType)) {
                out = response.getOutputStream();
                ftpUtils.downLoadFile(ftpClient,removeRelativeUrl, storeFileName, k, out);
            } else {
                /*downFile = new File(SysFileUtil.getUploadPath() + File.separator + storeFileName);
                out = new FileOutputStream(downFile);
                ftpUtils.downLoadFile(removeRelativeUrl, storeFileName, k, out);
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
                out.write(buffer); // 输出文件*/
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
                default:
                    ;
            }
            //response.addHeader("Content-Length", "" + file.length());  //返回头 文件大小
            response.setHeader("Content-Disposition", "inline;filename=" + new String(sysFile.getShowName().getBytes(), "ISO-8859-1"));

            out.flush();
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
                /*if(ftpClient != null){
                    ftpUtils.getFtpClientPool().returnObject(k,ftpClient);
                }*/
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
    public void printPreview(@PathVariable String businessId, @PathVariable String businessType, @PathVariable String stageType, HttpServletResponse response) {
        InputStream inputStream = null;
        OutputStream out = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Template.WORD_SUFFIX.getKey();
            //String filePath = path.substring(0, path.lastIndexOf(".")) + Template.PDF_SUFFIX.getKey();
            String fileName = "";
            file = createFile(businessId, businessType, stageType, path);
            /*if (file != null) {
                OfficeConverterUtil.convert2PDF(file.getAbsolutePath(), filePath);
            }

            printFile = new File(filePath);
            inputStream = new BufferedInputStream(new FileInputStream(printFile));

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);  //读取文件流
            inputStream.close();*/
            //重置结果集
            response.reset();
            //使用新的转换控件
            out = response.getOutputStream();
            if (file != null) {
                inputStream = new FileInputStream(file);
                Document doc = new Document(inputStream);
                doc.save(out, com.aspose.words.SaveFormat.PDF);
            }
            response.setContentType("application/pdf");
            //返回头 文件大小
            //response.addHeader("Content-Length", "" + file.length());
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if(out != null){
                    out.close();
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
     * 生成打印文件
     *
     * @return
     */
    private File createFile(String businessId, String businessType, String stageType, String path) {
        File file = null;
        switch (businessType) {
            case "SIGN":
                Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
                Map<String, Object> dataMap = TemplateUtil.entryAddMap(sign);
                String ministerhandlesug = sign.getMinisterhandlesug();
                dataMap.put("ministerhandlesug", ministerhandlesug == null ? "" : ministerhandlesug.replaceAll("<br>", "<w:br />")
                        .replaceAll("<p style='text-align:right;'>", "").replaceAll("</p>", ""));
                ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageType);
                if(Validate.isObject(reviewStateEnum)){
                    switch (reviewStateEnum){
                        /**
                         * 建议书、可研、其他 、登记赋码
                         */
                        case STAGESUG:
                        case STAGESTUDY:
                        case STAGEOTHER:
                        case REGISTERCODE:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.STAGE_SUG_SIGN.getKey(), path);
                            break;
                        /**
                         * 项目概算
                         */
                        case STAGEBUDGET:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.STAGE_BUDGET_SIGN.getKey(), path);
                            break;
                        /**
                         * 资金申请报告
                         */
                        case STAGEREPORT:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.APPLY_REPORT_SIGN.getKey(), path);
                            break;
                        /**
                         * 进口设备
                         */
                        case STAGEDEVICE:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.IMPORT_DEVICE_SIGN.getKey(), path);
                            break;
                        /**
                         * 设备清单（国产和进口）
                         */
                        case STAGEHOMELAND:
                        case STAGEIMPORT:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.DEVICE_BILL_SIGN.getKey(), path);
                            break;
                        default:
                                ;

                    }
                }
                break;

            case "WORKPROGRAM":
                WorkProgramDto workProgramDto = workProgramService.initWorkProgramById(businessId);
//                  SignDto signDto123 =  signService.findById(workProgramDto.getSignId(), true);
//                    WorkProgram workProgram = new WorkProgram();
//                    BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);
                if (!Validate.isString(workProgramDto.getIsHaveEIA())) {
                    workProgramDto.setIsHaveEIA("0");
                }
                Map<String, Object> workData = TemplateUtil.entryAddMap(workProgramDto);

                List<ExpertSelectedDto> expertSelectedDtoLists = expertSelectedRepo.findByBusinessId(workProgramDto.getId());
//                List<ExpertSelectedDto> expertSelectedDtoLists = workProgramDto.getExpertSelectedDtoList();
//                List<ExpertDto> expertDtoList = workProgramDto.getExpertDtoList();
                ExpertDto[] expertDtos = null;

                if (expertSelectedDtoLists != null && expertSelectedDtoLists.size() > 0) {
                    //控制专家信息列表
                    int length = expertSelectedDtoLists.size();
                    int size = length;
                    if(length <=10){
                        size = 10;
                    }else if(length > 10 && length <=30){
                        size = 30;
                    }else{
                        int s = (length - 30 )/20;
                        if(length - (s*20 + 30) > 13){
                            size = (s+1)*20 + 30;
                        }else{
                            size = s*20 + 30 + 20;
                        }
                    }

                    expertDtos = new ExpertDto[size];
                    for (int i = 0; i < expertSelectedDtoLists.size(); i++) {
                        ExpertDto expertDto = expertSelectedDtoLists.get(i).getExpertDto();
                        //目前先改代码，后期有时间转换为改模板，直接遍历select表就可以的
                        //重新设置专业和专家类别，其中专业对应select表的专家小类 ， 专家类别对应select的专家类别
                        expertDto.setMajorStudy(expertSelectedDtoLists.get(i).getMaJorSmall());
                        expertDto.setExpertSort(expertSelectedDtoLists.get(i).getExpeRttype());
                        expertDto.setRemark(expertSelectedDtoLists.get(i).getRemark());
                        expertDtos[i] = expertDto;
                    }
                }
                String addressName = "";
                String rbDate = "";
                List<RoomBookingDto> roomBookingDtoList = workProgramDto.getRoomBookingDtos();
                if (roomBookingDtoList != null && roomBookingDtoList.size() > 0) {
                    addressName = workProgramDto.getRoomBookingDtos().get(0).getAddressName();
                    rbDate = workProgramDto.getRoomBookingDtos().get(0).getRbDate();
                }

                int count = signBranchRepo.countBranch(workProgramDto.getSignId());
                workData.put("expertList", expertDtos);//聘请专家
                workData.put("works", count);//控制是否多个分支
                workData.put("addressName", addressName);//会议室名称
                workData.put("rbDate", rbDate);//评审会时间
                workData.put("studyBeginTimeStr", DateUtils.getTimeNow(workProgramDto.getStudyBeginTime()));//调研开始时间
                workData.put("studyEndTimeStr", DateUtils.getTimeNow(workProgramDto.getStudyEndTime()));//调研结束时间
                if (null != stageType && ("STAGESUG".equals(stageType) || "STAGESTUDY".equals(stageType)
                        || "STAGEBUDGET".equals(stageType) || "STAGEOTHER".equals(stageType)
                        || "REGISTERCODE".equals(stageType))) {
                    if ("STAGESUG".equals(stageType)) {
                        workData.put("wpTile", "项目建议书评审工作方案");
                        workData.put("wpCode", " QR-4.3-02-A3");
                    } else if ("STAGEOTHER".equals(stageType)) {
                        workData.put("wpTile", "其它评审工作方案");
                        workData.put("wpCode", " QR-4.3-02-A3");
                    } else if ("STAGESTUDY".equals(stageType)) {
                        workData.put("wpTile", "可行性研究报告评审工作方案");
                        workData.put("wpCode", " QR-4.4-01-A3");
                    } else if ("STAGEBUDGET".equals(stageType)) {
                        workData.put("wpTile", "项目概算评审工作方案");
                        workData.put("wpCode", " QR-4.7-01-A2");
                    } else if ("REGISTERCODE".equals(stageType)) {
                        workData.put("wpTile", "登记赋码评审工作方案");
                        workData.put("wpCode", "QR-4.3-02-A3");
                    }
                    file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_SUG_WORKPROGRAM.getKey(), path);
                } else if (null != stageType && "STAGEREPORT".equals(stageType)) {
                    workData.put("wpTile", "资金申请报告工作方案");
                    workData.put("wpCode", "QR-4.9-02-A0");
                    file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_REPORT_WORKPROGRAM.getKey(), path);
                } else if (null != stageType && "STAGEDEVICE".equals(stageType)) {
                    workData.put("wpTile", "进口设备工作方案");
                    workData.put("wpCode", "QR-4.9-02-A0");
                    file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_DEVICE_WORKPROGRAM.getKey(), path);
                } else if (null != stageType && "STAGEIMPORT".equals(stageType)) {
                    workData.put("wpTile", "设备清单工作方案");
                    workData.put("wpCode", "QR-4.9-02-A0");
                    file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_HOMELAND_WORKPROGRAM.getKey(), path);
                } else if (null != stageType && "INFORMATION".equals(stageType)) {

                    file = TemplateUtil.createDoc(workData, Constant.Template.DOWNLOAD_INFORMATION.getKey(), path);
                }
                break;
            case "FILERECORD":
                FileRecordDto fileRecordDto = fileRecordService.initBySignId(businessId);
                Map<String, Object> fileData = TemplateUtil.entryAddMap(fileRecordDto);
                ProjectConstant.REVIEW_STATE_ENUM recordReviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageType);
                if(Validate.isObject(recordReviewStateEnum)){
                    switch (recordReviewStateEnum){
                        /**
                         * 建议书
                         */
                        case STAGESUG:
                            file = TemplateUtil.createDoc(fileData, Template.STAGE_SUG_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 可研
                         */
                        case STAGESTUDY:
                            file = TemplateUtil.createDoc(fileData, Template.STAGE_STUDY_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 其他
                         */
                        case STAGEOTHER:
                            Sign s = signRepo.getById(businessId);
                            fileData.put("secrectlevel", s.getSecrectlevel());
                            file = TemplateUtil.createDoc(fileData, Template.OTHERS_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 登记赋码
                         */
                        case REGISTERCODE:
                            file = TemplateUtil.createDoc(fileData, Template.DJFM_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 项目概算
                         */
                        case STAGEBUDGET:
                            file = TemplateUtil.createDoc(fileData, Template.STAGE_BUDGET_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 概算协审
                         */
                        case STAGEBUDGET_XS:
                            file = TemplateUtil.createDoc(fileData, Template.STAGE_BUDGET_XS_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 资金申请报告
                         */
                        case STAGEREPORT:
                            file = TemplateUtil.createDoc(fileData, Template.APPLY_REPORT_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 进口设备
                         */
                        case STAGEDEVICE:
                            file = TemplateUtil.createDoc(fileData, Template.IMPORT_DEVICE_FILERECORD.getKey(), path);
                            break;
                        /**
                         * 设备清单（国产和进口）
                         */
                        case STAGEHOMELAND:
                        case STAGEIMPORT:
                            //设备清单（国产、进口）
                            AddRegisterFileDto[] registerFileDto = new AddRegisterFileDto[5];
                            if (fileRecordDto != null && fileRecordDto.getRegisterFileDto() != null && fileRecordDto.getRegisterFileDto().size() > 0) {
                                for (int i = 0; i < fileRecordDto.getRegisterFileDto().size() && i < 5; i++) {
                                    registerFileDto[i] = fileRecordDto.getRegisterFileDto().get(i);
                                }
                            }
                            fileData.put("registerFileList", registerFileDto);
                            file = TemplateUtil.createDoc(fileData, Template.DEVICE_BILL_FILERECORD.getKey(), path);
                            break;
                        default:
                            ;

                    }
                }
                break;

            case "FILERECOED_OTHERFILE":
                //项目归档的其它资料
                FileRecordDto FileRecordDto = fileRecordService.initBySignId(businessId);
                Map<String, Object> otherFileData = new HashMap<>();
                otherFileData.put("fileNo", FileRecordDto.getFileNo());
                otherFileData.put("projectName", FileRecordDto.getProjectName());
                otherFileData.put("projectCompany", FileRecordDto.getProjectCompany());
                otherFileData.put("projectCode", FileRecordDto.getProjectCode());
                otherFileData.put("OtherTitle", "归档表");
                List<AddRegisterFile> addRegisterFileList = new ArrayList<>();
                //其它资料
                if ("OTHER_FILE".equals(stageType)) {
                    otherFileData.put("otherFileType", "其它资料");
                    addRegisterFileList = addRegisterFileRepo.findByBusIdNoAndBusType(businessId, "FILERECORD");
                }
                //图纸资料
                if ("DRAWING_FILE".equals(stageType)) {
                    otherFileData.put("otherFileType", "图纸资料");
                    addRegisterFileList = addRegisterFileService.findByBusIdAndBusType(businessId, 2);

                }
                otherFileData.put("otherFileList", addRegisterFileList);
                file = TemplateUtil.createDoc(otherFileData, Template.OTHER_FILE.getKey(), path);
                break;
            case "SIGN_OTHERFILE":
                //项目申报的其它资料
                Sign signs = signRepo.findById(Sign_.signid.getName(), businessId);
                    /*List<AddRegisterFile> addRegisterFile=addRegisterFileRepo*/

                Map<String, Object> otherFileDatas = new HashMap<>();
                otherFileDatas.put("fileNo", signs.getSignNum());
                otherFileDatas.put("projectName", signs.getProjectname());
                otherFileDatas.put("projectCompany", signs.getBuiltcompanyName());
                otherFileDatas.put("projectCode", signs.getProjectcode());
                otherFileDatas.put("OtherTitle", "报审登记");
                List<AddRegisterFile> addRegisterFileLists = new ArrayList<>();
                //其它资料
                if ("OTHER_FILE".equals(stageType)) {
                    otherFileDatas.put("otherFileType", "其它资料");
                    //查询不等于拟补充材料和图纸的其他材料
                    addRegisterFileLists = addRegisterFileRepo.findByBusIdNoAndBusType(businessId, "SIGN");
                }
                otherFileDatas.put("otherFileList", addRegisterFileLists);
                file = TemplateUtil.createDoc(otherFileDatas, Template.OTHER_FILE.getKey(), path);
                break;

            case "DISPATCHDOC":
                DispatchDoc dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                SignDto signDto = null;
                if (Validate.isObject(dispatchDoc.getSign())) {
                    signDto = signService.findById(dispatchDoc.getSign().getSignid(), true);
                }
                Map<String, Object> dispatchData = TemplateUtil.entryAddMap(dispatchDoc);
                String mianChargeSuggest = dispatchDoc.getMianChargeSuggest();
                String main = null;
                String second = null;
                String sugge = null;
                String vice = null;
                //项目第一负责人
                if (Validate.isString(mianChargeSuggest)) {
                    main = mianChargeSuggest.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ");
                }
                //第二负责人
                String secondChargeSuggest = dispatchDoc.getSecondChargeSuggest();
                if (Validate.isString(secondChargeSuggest)) {
                    second = secondChargeSuggest.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ");
                }
                String ministerSuggesttion = dispatchDoc.getMinisterSuggesttion();
                if (Validate.isString(ministerSuggesttion)) {
                    sugge = ministerSuggesttion.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ")
                            .replaceAll("<p style='text-align:right;'>", "").replaceAll("</p>", "");
                }
                String viceDirectorSuggesttion = dispatchDoc.getViceDirectorSuggesttion();
                if (Validate.isString(viceDirectorSuggesttion)) {
                    vice = viceDirectorSuggesttion.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ")
                            .replaceAll("<p style='text-align:right;'>", "").replaceAll("</p>", "");
                }
                dispatchData.put("mianChargeSuggest", main);
                dispatchData.put("secondChargeSuggest", second);
                dispatchData.put("ministerSuggesttion", sugge);
                dispatchData.put("viceDirectorSuggesttion", vice);
                //是否调概
                dispatchData.put("ischangeEstimate", signDto == null ? "" : signDto.getIschangeEstimate());

                ProjectConstant.REVIEW_STATE_ENUM disReviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageType);
                if(Validate.isObject(disReviewStateEnum)){
                    switch (disReviewStateEnum){
                        /**
                         * 建议书
                         */
                        case STAGESUG:
                            dispatchData.put("wpTile", "项目建议书发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.STAGE_SUG_DISPATCHDOC.getKey(), path);
                            break;
                        /**
                         * 可研
                         */
                        case STAGESTUDY:
                            List<SignDto> signDtoList = null;
                            if (signDto != null) {
                                signDtoList = signDto.getAssociateSignDtoList();
                            }
                            List<DispatchDocDto> dispatchList = new ArrayList<DispatchDocDto>();
                            List<DispatchDocDto> dispatchViewList = new ArrayList<DispatchDocDto>();
                            if (null != signDtoList) {
                                for (int i = 0; i < signDtoList.size(); i++) {
                                    if (null != signDtoList.get(i).getDispatchDocDto()) {
                                        dispatchList.add(signDtoList.get(i).getDispatchDocDto());
                                    }
                                }
                            }
                            dispatchViewList.add(getDispatchStage("项目建议书", dispatchList));
                            dispatchData.put("dispatchList", dispatchViewList);
                            file = TemplateUtil.createDoc(dispatchData, Template.STAGE_STUDY_DISPATCHDOC.getKey(), path);
                            break;
                        /**
                         * 其他
                         */
                        case STAGEOTHER:
                            file = TemplateUtil.createDoc(dispatchData, Template.OTHERS_DISPATCHDOC.getKey(), path);
                            break;
                        /**
                         * 登记赋码
                         */
                        case REGISTERCODE:
                            dispatchData.put("wpTile", "登记赋码发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.DJFM_DISPATCHDOC.getKey(), path);

                            break;
                        /**
                         * 项目概算
                         */
                        case STAGEBUDGET:
                            List<SignDto> signBugDtoList = null;
                            if (signDto != null) {
                                signBugDtoList = signDto.getAssociateSignDtoList();
                            }
                            List<DispatchDocDto> dispatchBugList = new ArrayList<DispatchDocDto>();
                            Map<String , DispatchDocDto> dispatchBugViewList = new HashMap<>();
                            if (null != signBugDtoList) {
                                for (int i = 0,l=signBugDtoList.size(); i < l; i++) {
                                    if (null != signBugDtoList.get(i).getDispatchDocDto()) {
                                        dispatchBugList.add(signBugDtoList.get(i).getDispatchDocDto());
                                    }
                                }
                            }
                            dispatchBugViewList.put("项目建议书" , getDispatchStage("项目建议书", dispatchBugList));
                            dispatchBugViewList.put("可行性研究报告" , getDispatchStage("可行性研究报告", dispatchBugList));
                            if (signDto != null && signDto.getIschangeEstimate() != null
                                    && "9".equals(signDto.getIschangeEstimate())) {
                                dispatchBugViewList.put("项目概算" , getDispatchStage("项目概算", dispatchBugList));
                            }
                            dispatchData.put("dispatchList", dispatchBugViewList);
                            file = TemplateUtil.createDoc(dispatchData, Template.STAGE_BUDGET_DISPATCHDOC.getKey(), path);
                            break;
                        /**
                         * 资金申请报告
                         */
                        case STAGEREPORT:
                            List<SignDto> signPortDtoList = null;
                            if (signDto != null) {
                                signPortDtoList = signDto.getAssociateSignDtoList();
                            }
                            List<DispatchDocDto> dispatchPortList = new ArrayList<DispatchDocDto>();
                            List<DispatchDocDto> dispatchPortViewList = new ArrayList<DispatchDocDto>();
                            if (null != signPortDtoList) {
                                for (int i = 0,l=signPortDtoList.size(); i < l; i++) {
                                    if (null != signPortDtoList.get(i).getDispatchDocDto()) {
                                        dispatchPortList.add(signPortDtoList.get(i).getDispatchDocDto());
                                    }
                                }
                            }
                            dispatchData.put("wpTile", "资金申请报告发文审批表");
                            dispatchPortViewList.add(getDispatchStage("资金申请报告", dispatchPortList));
                            dispatchData.put("dispatchList", dispatchPortViewList);

                            file = TemplateUtil.createDoc(dispatchData, Template.APPLY_REPORT_DISPATCHDOC.getKey(), path);
                            break;
                        /**
                         * 进口设备
                         */
                        case STAGEDEVICE:
                            dispatchData.put("wpTile", "进口设备发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.IMPORT_DEVICE_DISPATCHDOC.getKey(), path);
                            break;
                        /**
                         * 设备清单（国产和进口）
                         */
                        case STAGEHOMELAND:
                        case STAGEIMPORT:
                            dispatchData.put("wpTile", "设备清单发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.DEVICE_BILL_DISPATCHDOC.getKey(), path);
                            break;
                        default:
                            ;

                    }
                }
                break;

            case "SIGN_EXPERT":
                //专家评审费发放表
                if ("SIGN_EXPERT_PAY".equals(stageType)) {
                    //项目环节的专家评分和专家评审费
                    Map<String, Object> expertData = new HashMap<>();
//                    ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);

                    List<ExpertSelectedDto> expertSelectedList = new ArrayList<>();
                    List<ExpertSelectedDto> expertSelectedList1 = new ArrayList<>();
                    List<ExpertSelectedDto> expertSelectedList2 = new ArrayList<>();

                    Boolean isSplit = false;

                    //合计 ： 评审费 税额  总计
                    BigDecimal reviewCostSum1 = new BigDecimal(0);
                    BigDecimal reviewTaxesSum1 = new BigDecimal(0);
                    BigDecimal totalCostSum1 = new BigDecimal(0);

                    BigDecimal reviewCostSum2 = new BigDecimal(0);
                    BigDecimal reviewTaxesSum2 = new BigDecimal(0);
                    BigDecimal totalCostSum2 = new BigDecimal(0);
                    List<ExpertSelectedDto> expertSelectedDtoList = expertSelectedRepo.expertSelectedByIds(businessId);
                    ExpertReviewDto  expertReview = new ExpertReviewDto();
                        if (expertSelectedDtoList != null && expertSelectedDtoList.size() > 0) {
                            expertReview   =  expertSelectedDtoList.get(0).getExpertReviewDto();

                            for (ExpertSelectedDto expertSelectedDto : expertSelectedDtoList) {
                                ExpertSelectedDto expertSelected = new ExpertSelectedDto(); //不拆分时
                                ExpertSelectedDto expertSelected1 = new ExpertSelectedDto(); //第一张表
                                ExpertSelectedDto expertSelected2 = new ExpertSelectedDto(); //第二张表

                                if (EnumState.YES.getValue().equals(expertSelectedDto.getIsJoin())
                                        && EnumState.YES.getValue().equals(expertSelectedDto.getIsConfrim())) {
                                    BeanCopierUtils.copyPropertiesIgnoreNull(expertSelectedDto, expertSelected);

                                    //是专家评审费才需要计算费用
                                    if ("SIGN_EXPERT_PAY".equals(stageType)) {
                                        BeanCopierUtils.copyPropertiesIgnoreNull(expertSelectedDto, expertSelected1);
                                        if (EnumState.YES.getValue().equals(expertSelectedDto.getIsSplit())) {
                                            isSplit = true;

                                            BeanCopierUtils.copyPropertiesIgnoreNull(expertSelectedDto, expertSelected2);
                                            //判断第一张表费用是否为空，为空默认为评审费总金额
                                            expertSelected1.setReviewCost(expertSelectedDto.getOneCost() == null ? expertSelectedDto.getReviewCost() : expertSelectedDto.getOneCost());

                                            //判断缴税金额不为空，并且评审费不为0，则计算缴税金额，否则缴税金额为0
                                            if (expertSelectedDto.getReviewTaxes() != null && expertSelectedDto.getReviewCost().compareTo(new BigDecimal(0)) != 0) {
                                                expertSelected1.setReviewTaxes(expertSelectedDto.getReviewTaxes().divide(expertSelectedDto.getReviewCost(), 3, BigDecimal.ROUND_HALF_UP).multiply(expertSelected1.getReviewCost()));
                                            } else {
                                                expertSelected1.setReviewTaxes(BigDecimal.ZERO);
                                            }
                                            expertSelected1.setTotalCost(Arith.safeAdd(expertSelected1.getReviewCost(), expertSelected1.getReviewTaxes()));

                                            expertSelected2.setReviewCost(expertSelectedDto.getReviewCost().subtract(expertSelected1.getReviewCost()));
                                            if (expertSelectedDto.getReviewTaxes() != null && expertSelectedDto.getReviewCost().compareTo(BigDecimal.ZERO) != 0) {
                                                expertSelected2.setReviewTaxes(expertSelectedDto.getReviewTaxes().subtract(expertSelected1.getReviewTaxes()));
                                            } else {
                                                expertSelected2.setReviewTaxes(BigDecimal.ZERO);
                                            }
                                            expertSelected2.setTotalCost(expertSelected2.getReviewCost().add(expertSelected2.getReviewTaxes()));

                                            reviewCostSum2 = reviewCostSum2.add(expertSelected2.getReviewCost());
                                            reviewTaxesSum2 = reviewTaxesSum2.add(expertSelected2.getReviewTaxes());
                                            totalCostSum2 = totalCostSum2.add(Arith.safeAdd(expertSelected2.getReviewCost(), expertSelected2.getReviewTaxes()));
                                            expertSelectedList2.add(expertSelected2);
                                        }
                                        BigDecimal reviewCostCount = expertSelected1.getReviewCost() == null ? BigDecimal.ZERO : expertSelected1.getReviewCost();
                                        BigDecimal totalTaxesCount = expertSelected1.getReviewTaxes() == null ? BigDecimal.ZERO : expertSelected1.getReviewTaxes();
                                        reviewCostSum1 = reviewCostSum1.add(reviewCostCount);
                                        reviewTaxesSum1 = reviewTaxesSum1.add(totalTaxesCount);
                                        totalCostSum1 = totalCostSum1.add(Arith.safeAdd(reviewCostCount, totalTaxesCount));

                                        expertSelectedList1.add(expertSelected1);
                                    }
                                    expertSelectedList.add(expertSelected);
                                }
                            }

                        String reviewTitle = expertReview.getReviewTitle();
                        expertData.put("projectName", reviewTitle);
                        expertData.put("projectName2", reviewTitle.replaceAll("专家", "外地专家"));
                        if (isSplit) {
                            expertData.put("expertList", expertSelectedList1);
                            expertData.put("expertList2", expertSelectedList2);
                        } else {
                            expertData.put("expertList", expertSelectedList);
                        }
                    }
                    expertData.put("reviewCostSum", isSplit ? reviewCostSum1 : expertReview.getReviewCost());
                    expertData.put("reviewTaxesSum", isSplit ? reviewTaxesSum1 : expertReview.getReviewTaxes());
                    expertData.put("totalCostSum", isSplit ? totalCostSum1 : Arith.safeAdd(expertReview.getReviewCost(), expertReview.getReviewTaxes()));

                    expertData.put("reviewCostSum2", reviewCostSum2);
                    expertData.put("reviewTaxesSum2", reviewTaxesSum2);
                    expertData.put("totalCostSum2", totalCostSum2);

                    expertData.put("payDate", expertReview.getReviewDate() == null ? "" : DateUtils.converToString(expertReview.getReviewDate(), "yyyy年MM月dd日"));

                    if (isSplit) {
                        file = TemplateUtil.createDoc(expertData, Template.EXPERT_PAYMENT.getKey(), path);
                    } else {
                        file = TemplateUtil.createDoc(expertData, Template.EXPERT_PAYMENT_one.getKey(), path);
                    }


                }
                //专家评分
                if ("SIGN_EXPERT_SCORE".equals(stageType)) {
                    Map<String, Object> expertData = new HashMap<>();
                    ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);
                    List<ExpertSelected> expertSelectedList = expertReview.getExpertSelectedList();
                    List<ExpertSelected> expertSelectedList2 = new ArrayList<>();

                    if(expertSelectedList != null && expertSelectedList.size() > 0 ){
                        for(ExpertSelected expertSelected : expertSelectedList){
                            if(EnumState.YES.getValue().equals( expertSelected.getIsJoin()) && EnumState.YES.getValue().equals(expertSelected.getIsConfrim()) ){
                                expertSelectedList2.add(expertSelected);
                            }
                        }
                    }
                    expertData.put("expertList", expertSelectedList2 );
                    file = TemplateUtil.createDoc(expertData, Template.EXPERT_SCORD.getKey(), path);
                }
                break;
            case "ADDSUPPLETER":
                //拟补充资料函
                AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), businessId);
                Map<String, Object> addsuppletterData = TemplateUtil.entryAddMap(addSuppLetter);
                if (Validate.isString(addSuppLetter.getLeaderSignIdeaContent())) {
                    String leaderSignIdea = addSuppLetter.getLeaderSignIdeaContent().replaceAll("<br>", "").replaceAll("&nbsp;", "");
                    addsuppletterData.put("leaderSignIdea", leaderSignIdea);

                }
                file = TemplateUtil.createDoc(addsuppletterData, Template.ADDSUPPLETER.getKey(), path);
                break;

            case "SIGN_UNIT":
                //单位评分
                if ("SIGN_UNIT_SCORE".equals(stageType)) {
                    UnitScore unitScore = unitScoreRepo.findById(UnitScore_.id.getName(), businessId);
                    Map<String, Object> unitScoerMap = new HashMap<>();
                    if (Validate.isObject(unitScore)) {
                        unitScoerMap.put("coName", unitScore.getCompany().getCoName());
                        unitScoerMap.put("coPhone", unitScore.getCompany().getCoPhone());
                        unitScoerMap.put("coPC", unitScore.getCompany().getCoPC());
                        unitScoerMap.put("coAddress", unitScore.getCompany().getCoAddress());
                        unitScoerMap.put("score", unitScore.getScore());
                        unitScoerMap.put("describes", unitScore.getDescribes());
                    }
                    file = TemplateUtil.createDoc(unitScoerMap, Template.UNIT_SCORE.getKey(), path);
                }
                break;
            case "EXPERT":
                //专家申请表
                ExpertDto expertDto = expertService.findById(businessId);
                Expert expert = new Expert();
                BeanCopierUtils.copyPropertiesIgnoreNull(expertDto, expert);
                Map<String, Object> expertDataMap = TemplateUtil.entryAddMap(expert);
                WorkExpeDto[] workExpes = new WorkExpeDto[4];
                ProjectExpeDto[] projectExpes = new ProjectExpeDto[4];

                if (expertDto.getWorkDtoList() != null && expertDto.getWorkDtoList().size() > 0) {
                    for (int i = 0; i < expertDto.getWorkDtoList().size() && i < 4; i++) {
                        WorkExpeDto workExpeDto = expertDto.getWorkDtoList().get(i);
                        workExpeDto.setBeginTimeStr(DateUtils.converToString(workExpeDto.getBeginTime(), "yyyyMMdd"));
                        workExpeDto.setEndTimeStr(DateUtils.converToString(workExpeDto.getEndTime(), "yyyyMmdd"));
                        workExpes[i] = workExpeDto;
                    }
                }
                if (expertDto.getProjectDtoList() != null && expertDto.getProjectDtoList().size() > 0) {
                    for (int i = 0; i < expertDto.getProjectDtoList().size() && i < 4; i++) {
                        projectExpes[i] = expertDto.getProjectDtoList().get(i);
                    }
                }
                expertDataMap.put("workList", workExpes);
                expertDataMap.put("projectList", projectExpes);

                file = TemplateUtil.createDoc(expertDataMap, Template.EXPERT.getKey(), path);

                break;

            case "EXPERTOFFER":
                //专家聘书
                ExpertOffer expertOffer = expertOfferRepo.findById(ExpertOffer_.id.getName(), businessId);
                Map<String, Object> expertOfferDataMap = new HashMap<>();
                expertOfferDataMap.put("name", expertOffer.getExpert().getName());
                expertOfferDataMap.put("sex", expertOffer.getExpert().getSex());
                expertOfferDataMap.put("birthDay", expertOffer.getExpert().getBirthDay());
                expertOfferDataMap.put("idCard", expertOffer.getExpert().getIdCard());
                expertOfferDataMap.put("qualifiCations", expertOffer.getExpert().getQualifiCations());
                expertOfferDataMap.put("post", expertOffer.getExpert().getPost());
                expertOfferDataMap.put("sendCcieDate", expertOffer.getSendCcieDate());
                expertOfferDataMap.put("period", expertOffer.getPeriod());
                expertOfferDataMap.put("expertNo", expertOffer.getExpert().getExpertNo());
                expertOfferDataMap.put("fafang", COMPANY_NAME);

                file = TemplateUtil.createDoc(expertOfferDataMap, Template.EXPERTOFFER.getKey(), path);
                break;

            case "TOPICINFO":
                //课题研究
                TopicInfoDto topicInfoDto = topicInfoService.findById(businessId);
                //归档
                if ("TOPICINFO_FILERECORD".equals(stageType)) {
                    if (topicInfoDto != null) {
                        FilingDto filingDto = topicInfoDto.getFilingDto();
                        Map<String, Object> filingData = TemplateUtil.entryAddMap(filingDto);
                        AddRegisterFileDto[] registerFileDto = new AddRegisterFileDto[5];
                        if (filingDto != null && filingDto.getRegisterFileDto() != null && filingDto.getRegisterFileDto().size() > 0) {
                            for (int i = 0; i < filingDto.getRegisterFileDto().size() && i < 5; i++) {
                                registerFileDto[i] = filingDto.getRegisterFileDto().get(i);
                            }
                        }
                        filingData.put("registerFileList", registerFileDto);
                        file = TemplateUtil.createDoc(filingData, Template.TOPICINFO_FILERECORD.getKey(), path);
                    }
                }

                //工作方案
                if ("TOPICINFO_WORKPROGRAM".equals(stageType)) {
                    if (topicInfoDto != null) {
                        WorkPlanDto workPlanDto = topicInfoDto.getWorkPlanDto();
                        Map<String, Object> workPlanData = TemplateUtil.entryAddMap(workPlanDto);
                        workPlanData.put("createdDate", DateUtils.converToString(workPlanDto.getCreatedDate(), "yyyy年MM月dd日"));
                        String rbDateStr = "";
                        String address = "";
                        if (workPlanDto != null && workPlanDto.getRoomDtoList() != null && workPlanDto.getRoomDtoList().size() > 0) {
                            rbDateStr = workPlanDto.getRoomDtoList().get(0).getRbDate();
                            address = workPlanDto.getRoomDtoList().get(0).getAddressName();
                        }
                        workPlanData.put("rbDateStr", rbDateStr);
                        workPlanData.put("addRess", address);
                        ExpertDto[] expeDtos = new ExpertDto[10];
                        if (workPlanDto != null && workPlanDto.getExpertDtoList() != null && workPlanDto.getExpertDtoList().size() > 0) {
                            for (int i = 0; i < workPlanDto.getExpertDtoList().size() && i < 10; i++) {
                                expeDtos[i] = workPlanDto.getExpertDtoList().get(i);
                            }
                        }
                        workPlanData.put("expertList", expeDtos);
                        file = TemplateUtil.createDoc(workPlanData, Template.TOPICINFO_WORKPROGRAM.getKey(), path);
                    }
                }

                break;

            case "MONTHLY":
                //月报简报
                AddSuppLetterDto addSuppLetterDto = addSuppLetterService.findById(businessId);
                Map<String, Object> addSuppleterData = TemplateUtil.entryAddMap(addSuppLetterDto);
                file = TemplateUtil.createDoc(addSuppleterData, Template.MONTHLY.getKey(), path);
                break;

            case "ARCHIVES":
                //借阅档案
                ArchivesLibraryDto archivesLibraryDto = archivesLibraryService.findById(businessId);
                Map<String, Object> archivesData = TemplateUtil.entryAddMap(archivesLibraryDto);
                file = TemplateUtil.createDoc(archivesData, Template.ARCHIVES_DETAIL.getKey(), path);
                break;

            //补充资料清单
            case "ADDSUPPLEFILE":
                Sign signss = signRepo.findById(Sign_.signid.getName(), businessId);
                List<AddRegisterFile> addRegisterFileList2 = addRegisterFileService.findByBusIdAndBusType(businessId, 3);

                String stage = "评估论证";
                if((STAGEBUDGET.getZhCode()).equals(signss.getReviewstage())){
                    stage = "项目概算";
                }
                Map<String, Object> addFileData = new HashMap<>();
                addFileData.put("addFileList", addRegisterFileList2);
                addFileData.put("signNum", signss.getSignNum());
                addFileData.put("projectname", signss.getProjectname());
                addFileData.put("builtcompanyName", signss.getBuiltcompanyName());
                addFileData.put("projectcode", signss.getProjectcode());
                addFileData.put("strDate", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                addFileData.put("stage" , stage);
                file = TemplateUtil.createDoc(addFileData, Template.ADD_REGISTER_FILE.getKey(), path);
                break;
            case "PROJECTSTOP":
                ProjectStop projectStop = projectStopRepo.findById(ProjectStop_.stopid.getName(), businessId);
                Sign sign1 = signRepo.findById(projectStop.getSign().getSignid());
                Map<String, Object> psData = TemplateUtil.entryAddMap(projectStop);
                psData.put("projectname", sign1.getProjectname());
                psData.put("builtcompanyname", sign1.getBuiltcompanyName());
                psData.put("mOrgName", sign1.getmOrgName());
                psData.put("mUserName", sign1.getmUserName());
                psData.put("receivedate", DateUtils.converToString(sign1.getReceivedate(), "yyyy-MM-dd"));
                file = TemplateUtil.createDoc(psData, Template.PROJECT_STOP.getKey(), path);
                break;
            case "VPROJECT":
                //委项目处理表
                Sign vSign = signRepo.findById(Sign_.signid.getName() , businessId);
                Map<String, Object> vData = TemplateUtil.entryAddMap(vSign);
                file = TemplateUtil.createDoc(vData, Template.PROJECT_VPROJECT.getKey(), path);
                break;

            default:
                break;
        }
        return file;
    }

    /**
     * 打印归档资料
     */
    @RequestMapping(name = "打印归档资料", path = "otherFilePrint/{businessId}/{fileId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public  void otherFilePrint(@PathVariable String businessId , @PathVariable String fileId , HttpServletResponse response){
        InputStream inputStream = null;
        OutputStream out = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Template.WORD_SUFFIX.getKey();
            String fileName = "";
            FileRecord FileRecord = fileRecordRepo.findById("signid",businessId);
            Map<String, Object> otherFileData = new HashMap<>();
            otherFileData.put("fileNo", FileRecord.getFileNo());
            otherFileData.put("projectName", FileRecord.getProjectName());
            otherFileData.put("projectCompany", FileRecord.getProjectCompany());
            otherFileData.put("projectCode", FileRecord.getProjectCode());
            otherFileData.put("OtherTitle", "归档表");
            List<AddRegisterFile> addRegisterFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.id.getName() , fileId , null);
            otherFileData.put("otherFileList", addRegisterFileList);
            file = TemplateUtil.createDoc(otherFileData, Template.OTHER_FILE.getKey(), path);
            //重置结果集
            response.reset();
            //使用新的转换控件
            out = response.getOutputStream();
            if (file != null) {
                inputStream = new FileInputStream(file);
                Document doc = new Document(inputStream);
                doc.save(out, com.aspose.words.SaveFormat.PDF);
            }
            response.setContentType("application/pdf");
            //返回头 文件大小
            //response.addHeader("Content-Length", "" + file.length());
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if(out != null){
                    out.close();
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
     * 导出功能
     */
    @RequiresAuthentication
    @RequestMapping(name = "导出", path = "exportInfo", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportInfo(@RequestParam String businessId, @RequestParam String businessType, @RequestParam String stageType, @RequestParam String fileName, HttpServletResponse response) {
        ServletOutputStream ouputStream = null;
        InputStream inStream = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Template.WORD_SUFFIX.getKey();
            File file = createFile(businessId, businessType, stageType, path);
            ResponseUtils.setResponeseHead(Template.WORD_SUFFIX.getKey(), response);
            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            //获取相对路径
            int bytesum = 0;
            int byteread = 0;
            inStream = new FileInputStream(file); //读入原文件
            ouputStream = response.getOutputStream();
            byte[] buffer = new byte[1444];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                ouputStream.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ouputStream != null) {
                    ouputStream.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param stageName
     * @param dispatchDocDtoList
     * @return
     */
    private DispatchDocDto getDispatchStage(String stageName, List<DispatchDocDto> dispatchDocDtoList) {
        DispatchDocDto dispatchDocDto = new DispatchDocDto();
        if (dispatchDocDtoList != null && dispatchDocDtoList.size() > 0) {
            for (int i = 0; i < dispatchDocDtoList.size(); i++) {
                if (stageName.equals(dispatchDocDtoList.get(i).getDispatchStage())) {
                    return dispatchDocDtoList.get(i);
                }
                if (i == (dispatchDocDtoList.size() - 1)) {
                    dispatchDocDto.setDispatchStage(stageName);
                    break;
                }
            }
        } else {
            dispatchDocDto.setDispatchStage(stageName);
        }

        return dispatchDocDto;
    }


    /**
     * 只针对office文件
     *
     * @param model
     * @param sysFileId
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "在线编辑", path = "editFile", method = RequestMethod.GET)
    public String editFile(Model model, @RequestParam(required = true) String sysFileId, @RequestParam(required = true) String fileType) {
        model.addAttribute("sysFileId", sysFileId);
        String openType = "";
        //文件类型
        switch (fileType.toLowerCase()) {
            case ".doc":
            case ".docx":
                openType = "doc";
                break;
            case ".xls":
            case ".xlsx":
                openType = "xls";
                break;
            case ".ppt":
            case ".pptx":
                openType = "ppt";
                break;
            default:
                openType = "doc";
        }
        model.addAttribute("fileType", openType);
        String fileUrl = signRestService.getLocalUrl();
        if (fileUrl.endsWith("/")) {
            fileUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
        }
        model.addAttribute("fileUrl", fileUrl);
        model.addAttribute("pluginFilePath", Constant.plugin_file_path + "/weboffice.rar");
        return "weboffice/edit_dj";
    }


    /**
     * 根据base64，保存文件
     *
     * @param sysFileId
     * @param base64String
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "保存文件", path = "saveByFileBase64", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveByFileBase64(@RequestParam(required = true) String sysFileId, @RequestParam(required = true) String base64String) {
        ResultMsg resultMsg;
        String errorMsg = "";
        SysFile sysFile = sysFileRepo.findById(sysFileId);
        String fileUrl = sysFile.getFileUrl();
        String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
        String uploadFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
        //上传到ftp,
        Ftp f = sysFile.getFtp();
        FtpUtils ftpUtils = new FtpUtils();
        FtpClientConfig k = ConfigProvider.getUploadConfig(f);
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpUtils.getFtpClient(ftpUtils.getFtpClientPool(),k);
            Base64 decoder = new Base64();
            boolean uploadResult = ftpUtils.putFile(ftpClient,k, removeRelativeUrl, uploadFileName, new ByteArrayInputStream(decoder.decode(base64String)));
            if (uploadResult) {
                //保存数据库记录
                sysFile.setModifiedBy(SessionUtil.getDisplayName());
                sysFile.setModifiedDate(new Date());
                sysFileService.update(sysFile);
                resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "文件保存成功！修改的文件名是【" + sysFile.getShowName() + "】");
            } else {
                resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "文件保存失败！修改的文件名是【" + sysFile.getShowName() + "】");
            }
        } catch (Exception e) {
            errorMsg += e.getMessage();
            resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "文件保存异常！");
        }finally {
            /*try {
                if(ftpClient != null){
                    ftpUtils.getFtpClientPool().returnObject(k,ftpClient);
                }
            }catch (Exception ex){

            }*/
        }
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(SessionUtil.getDisplayName());
        log.setLogCode(resultMsg.getReCode());
        log.setBuninessId(sysFileId);
        log.setMessage(resultMsg.getReMsg() + errorMsg);
        log.setModule(Constant.LOG_MODULE.FILEUPDATE.getValue());
        log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".saveByFileBase64");
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);
        return resultMsg;
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
            if (!Validate.isEmpty(flist)) {
                for (File f : flist) {
                    if (!f.isDirectory() && !f.getName().toLowerCase().endsWith("png")) {
                        list.add(new PluginFileDto(f, Constant.plugin_file_path));
                    }
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
            if (!Validate.isEmpty(flist)) {
                for (File f : flist) {
                    if (!f.isDirectory() && !f.getName().toLowerCase().endsWith("png")) {
                        list.add(new PluginFileDto(f, Constant.plugin_file_path));
                    }
                }
            }
        }
        return list;
    }


    /**
     * 打印预览-下载
     *
     * @param businessId
     * @param businessType
     * @param response
     */
    @RequiresAuthentication
    @RequestMapping(name = "打印预览-下载", path = "downloadPreview/{businessId}/{businessType}/{stageType}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadPreview(@PathVariable String businessId, @PathVariable String businessType, @PathVariable String stageType, HttpServletResponse response) {
        InputStream inputStream = null;
        OutputStream out = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Template.WORD_SUFFIX.getKey();
            file = createFileDownload(businessId, businessType, stageType, path);
            String fileName = "";
            ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageType);
            //重置结果集
            response.reset();
            //使用新的转换控件
            out = response.getOutputStream();
            if (file != null) {
                inputStream = new FileInputStream(file);
//                Document doc = new Document(inputStream);
//                doc.save(out, SaveFormat.DOC);
            }
            response.setContentType("application/msword");
            //返回头 文件大小
            //response.addHeader("Content-Length", "" + file.length());
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));

            byte[] arr = new byte[1024];
            int len;
            while( ( len=inputStream.read(arr) ) != -1 ) {
                out.write(arr, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if(out != null){
                    out.close();
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
     * 生成打印-下载文件
     *
     * @return
     */
    private File createFileDownload(String businessId, String businessType, String stageType, String path) {
        File file = null;
        switch (businessType) {
            case "SIGN":
                Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
                Map<String, Object> dataMap = TemplateUtil.entryAddMap(sign);
                String ministerhandlesug = sign.getMinisterhandlesug();
                dataMap.put("ministerhandlesug", ministerhandlesug == null ? "" : ministerhandlesug.replaceAll("<br>", "<w:br />")
                        .replaceAll("<p style='text-align:right;'>", "").replaceAll("</p>", ""));
                ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageType);
                if(Validate.isObject(reviewStateEnum)){
                    switch (reviewStateEnum){
                        /**
                         * 建议书、可研、其他 、登记赋码
                         */
                        case STAGESUG:
                        case STAGESTUDY:
                        case STAGEOTHER:
                        case REGISTERCODE:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.DOWNLOAD_SIGN_XMJYS.getKey(), path);
                            break;
                        /**
                         * 项目概算
                         */
                        case STAGEBUDGET:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.DOWNLOAD_SIGN_XMGS.getKey(), path);
                            break;
                        /**
                         * 资金申请报告
                         */
                        case STAGEREPORT:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.DOWNLOAD_SIGN_ZJSB.getKey(), path);
                            break;
                        /**
                         * 进口设备
                         */
                        case STAGEDEVICE:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.DOWNLOAD_SIGN_JKSB.getKey(), path);
                            break;
                        /**
                         * 设备清单（国产和进口）
                         */
                        case STAGEHOMELAND:
                        case STAGEIMPORT:
                            file = TemplateUtil.createDoc(dataMap, Constant.Template.DOWNLOAD_SIGN_SBQD.getKey(), path);
                            break;
                        default:
                            ;

                    }
                }
                break;

            case "WORKPROGRAM":
                WorkProgramDto workProgramDto = workProgramService.initWorkProgramById(businessId);
                if (!Validate.isString(workProgramDto.getIsHaveEIA())) {
                    workProgramDto.setIsHaveEIA("0");
                }
                Map<String, Object> workData = TemplateUtil.entryAddMap(workProgramDto);

                List<ExpertSelectedDto> expertSelectedDtoLists = expertSelectedRepo.findByBusinessId(workProgramDto.getId());
                List<ExpertDto> expertDtos =  new ArrayList<>();;

                if (expertSelectedDtoLists != null && expertSelectedDtoLists.size() > 0) {
                    //控制专家信息列表
                    int length = expertSelectedDtoLists.size();
                    int size = length;
                    if(length <=10){
                        size = 10;
                    }else if(length > 10 && length <=30){
                        size = 30;
                    }else{
                        int s = (length - 30 )/20;
                        if(length - (s*20 + 30) > 13){
                            size = (s+1)*20 + 30;
                        }else{
                            size = s*20 + 30 + 20;
                        }
                    }
                    for (int i = 0; i < size; i++) {
                        ExpertDto expertDto = null ;
                        if(i < expertSelectedDtoLists.size()){
                            expertDto = expertSelectedDtoLists.get(i).getExpertDto();
                            //目前先改代码，后期有时间转换为改模板，直接遍历select表就可以的
                            //重新设置专业和专家类别，其中专业对应select表的专家小类 ， 专家类别对应select的专家类别
                            expertDto.setMajorStudy(expertSelectedDtoLists.get(i).getMaJorSmall());
                            expertDto.setExpertSort(expertSelectedDtoLists.get(i).getExpeRttype());
                            expertDto.setRemark(expertSelectedDtoLists.get(i).getRemark());
                        }else{
                            expertDto = new ExpertDto();
                        }
                        expertDtos.add(expertDto);
                    }
                }else{
                    for (int i = 0; i < 10; i++) {
                        expertDtos.add(new ExpertDto());
                    }
                }
                String addressName = "";
                String rbDate = "";
                List<RoomBookingDto> roomBookingDtoList = workProgramDto.getRoomBookingDtos();
                if (roomBookingDtoList != null && roomBookingDtoList.size() > 0) {
                    addressName = workProgramDto.getRoomBookingDtos().get(0).getAddressName();
                    rbDate = workProgramDto.getRoomBookingDtos().get(0).getRbDate();
                }

                int count = signBranchRepo.countBranch(workProgramDto.getSignId());
                workData.put("expertList", expertDtos);//聘请专家
                workData.put("works", count);//控制是否多个分支
                workData.put("addressName", addressName);//会议室名称
                workData.put("rbDate", rbDate);//评审会时间
                workData.put("studyBeginTimeStr", DateUtils.getTimeNow(workProgramDto.getStudyBeginTime()));//调研开始时间
                workData.put("studyEndTimeStr", DateUtils.getTimeNow(workProgramDto.getStudyEndTime()));//调研结束时间
                if (null != stageType && ("STAGESUG".equals(stageType) || "STAGESTUDY".equals(stageType)
                        || "STAGEBUDGET".equals(stageType) || "STAGEOTHER".equals(stageType)
                        || "REGISTERCODE".equals(stageType))) {
                    if ("STAGESUG".equals(stageType)) {
                        workData.put("wpTile", "项目建议书评审工作方案");
                        workData.put("wpCode", " QR-4.3-02-A3");
                    } else if ("STAGEOTHER".equals(stageType)) {
                        workData.put("wpTile", "其它评审工作方案");
                        workData.put("wpCode", " QR-4.3-02-A3");
                    } else if ("STAGESTUDY".equals(stageType)) {
                        workData.put("wpTile", "可行性研究报告评审工作方案");
                        workData.put("wpCode", " QR-4.4-01-A3");
                    } else if ("STAGEBUDGET".equals(stageType)) {
                        workData.put("wpTile", "项目概算评审工作方案");
                        workData.put("wpCode", " QR-4.7-01-A2");
                    } else if ("REGISTERCODE".equals(stageType)) {
                        workData.put("wpTile", "登记赋码评审工作方案");
                        workData.put("wpCode", "QR-4.3-02-A3");
                    }
                    file = TemplateUtil.createDoc(workData, Constant.Template.DOWNLOAD_WORKPROGRAM_XMJYS.getKey(), path);
                } else if (null != stageType && "STAGEREPORT".equals(stageType)) {
                    workData.put("wpTile", "资金申请报告工作方案");
                    workData.put("wpCode", "QR-4.9-02-A0");
                    file = TemplateUtil.createDoc(workData, Constant.Template.DOWNLOAD_WORKPROGRAM_ZJSB.getKey(), path);
                } else if (null != stageType && "STAGEDEVICE".equals(stageType)) {
                    workData.put("wpTile", "进口设备工作方案");
                    workData.put("wpCode", "QR-4.9-02-A0");
                    file = TemplateUtil.createDoc(workData, Constant.Template.DOWNLOAD_WORKPROGRAM_JKSB.getKey(), path);
                } else if (null != stageType && "STAGEIMPORT".equals(stageType)) {
                    workData.put("wpTile", "设备清单工作方案");
                    workData.put("wpCode", "QR-4.9-02-A0");
                    file = TemplateUtil.createDoc(workData, Constant.Template.DOWNLOAD_WORKPROGRAM_SBQD.getKey(), path);
                } else if (null != stageType && "INFORMATION".equals(stageType)) {

                    file = TemplateUtil.createDoc(workData, Constant.Template.DOWNLOAD_INFORMATION.getKey(), path);
                }
                break;
            case "FILERECORD":
                FileRecordDto fileRecordDto = fileRecordService.initBySignId(businessId);
                Map<String, Object> fileData = TemplateUtil.entryAddMap(fileRecordDto);
                ProjectConstant.REVIEW_STATE_ENUM recordReviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageType);
                if(Validate.isObject(recordReviewStateEnum)){
                    switch (recordReviewStateEnum){
                        /**
                         * 建议书
                         */
                        case STAGESUG:
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_XMJYS.getKey(), path);
                            break;
                        /**
                         * 可研
                         */
                        case STAGESTUDY:
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_KXXYJ.getKey(), path);
                            break;
                        /**
                         * 其他
                         */
                        case STAGEOTHER:
                            Sign s = signRepo.getById(businessId);
                            fileData.put("secrectlevel", s.getSecrectlevel());
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_OTHER.getKey(), path);
                            break;
                        /**
                         * 登记赋码
                         */
                        case REGISTERCODE:
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_DJFM.getKey(), path);
                            break;
                        /**
                         * 项目概算
                         */
                        case STAGEBUDGET:
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_XMGS.getKey(), path);
                            break;
                        /**
                         * 概算协审
                         */
                        case STAGEBUDGET_XS:
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_XMGSXS.getKey(), path);
                            break;
                        /**
                         * 资金申请报告
                         */
                        case STAGEREPORT:
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_ZJSB.getKey(), path);
                            break;
                        /**
                         * 进口设备
                         */
                        case STAGEDEVICE:
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_JKSB.getKey(), path);
                            break;
                        /**
                         * 设备清单（国产和进口）
                         */
                        case STAGEHOMELAND:
                        case STAGEIMPORT:
                            //设备清单（国产、进口）
                            AddRegisterFileDto[] registerFileDto = new AddRegisterFileDto[5];
                            if (fileRecordDto != null && fileRecordDto.getRegisterFileDto() != null && fileRecordDto.getRegisterFileDto().size() > 0) {
                                for (int i = 0; i < fileRecordDto.getRegisterFileDto().size() && i < 5; i++) {
                                    registerFileDto[i] = fileRecordDto.getRegisterFileDto().get(i);
                                }
                            }
                            fileData.put("registerFileList", registerFileDto);
                            file = TemplateUtil.createDoc(fileData, Template.DOWNLOAD_FILERECORD_SBQD.getKey(), path);
                            break;
                        default:
                            ;

                    }
                }
                break;

            case "FILERECOED_OTHERFILE":
                //项目归档的其它资料
                FileRecordDto FileRecordDto = fileRecordService.initBySignId(businessId);
                Map<String, Object> otherFileData = new HashMap<>();
                otherFileData.put("fileNo", FileRecordDto.getFileNo());
                otherFileData.put("projectName", FileRecordDto.getProjectName());
                otherFileData.put("projectCompany", FileRecordDto.getProjectCompany());
                otherFileData.put("projectCode", FileRecordDto.getProjectCode());
                otherFileData.put("OtherTitle", "归档表");
                List<AddRegisterFile> addRegisterFileList = new ArrayList<>();
                //其它资料
                if ("OTHER_FILE".equals(stageType)) {
                    otherFileData.put("otherFileType", "其它资料");
                    addRegisterFileList = addRegisterFileRepo.findByBusIdNoAndBusType(businessId, "FILERECORD");
                }
                //图纸资料
                if ("DRAWING_FILE".equals(stageType)) {
                    otherFileData.put("otherFileType", "图纸资料");
                    addRegisterFileList = addRegisterFileService.findByBusIdAndBusType(businessId, 2);

                }
                otherFileData.put("otherFileList", addRegisterFileList);
                file = TemplateUtil.createDoc(otherFileData, Template.DOWNLOAD_OTHER_FILE.getKey(), path);
                break;
            case "SIGN_OTHERFILE":
                //项目申报的其它资料
                Sign signs = signRepo.findById(Sign_.signid.getName(), businessId);
                    /*List<AddRegisterFile> addRegisterFile=addRegisterFileRepo*/

                Map<String, Object> otherFileDatas = new HashMap<>();
                otherFileDatas.put("fileNo", signs.getSignNum());
                otherFileDatas.put("projectName", signs.getProjectname());
                otherFileDatas.put("projectCompany", signs.getBuiltcompanyName());
                otherFileDatas.put("projectCode", signs.getProjectcode());
                otherFileDatas.put("OtherTitle", "报审登记");
                List<AddRegisterFile> addRegisterFileLists = new ArrayList<>();
                //其它资料
                if ("OTHER_FILE".equals(stageType)) {
                    otherFileDatas.put("otherFileType", "其它资料");
                    //查询不等于拟补充材料和图纸的其他材料
                    addRegisterFileLists = addRegisterFileRepo.findByBusIdNoAndBusType(businessId, "SIGN");
                }
                otherFileDatas.put("otherFileList", addRegisterFileLists);
                file = TemplateUtil.createDoc(otherFileDatas, Template.DOWNLOAD_OTHER_FILE.getKey(), path);
                break;

            case "DISPATCHDOC":
                DispatchDoc dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                SignDto signDto = null;
                if (Validate.isObject(dispatchDoc.getSign())) {
                    signDto = signService.findById(dispatchDoc.getSign().getSignid(), true);
                }
                Map<String, Object> dispatchData = TemplateUtil.entryAddMap(dispatchDoc);
                String mianChargeSuggest = dispatchDoc.getMianChargeSuggest();
                String main = null;
                String second = null;
                String sugge = null;
                String vice = null;
                //项目第一负责人
                if (Validate.isString(mianChargeSuggest)) {
                    main = mianChargeSuggest.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ");
                }
                //第二负责人
                String secondChargeSuggest = dispatchDoc.getSecondChargeSuggest();
                if (Validate.isString(secondChargeSuggest)) {
                    second = secondChargeSuggest.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ");
                }
                String ministerSuggesttion = dispatchDoc.getMinisterSuggesttion();
                if (Validate.isString(ministerSuggesttion)) {
                    sugge = ministerSuggesttion.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ")
                            .replaceAll("<p style='text-align:right;'>", "").replaceAll("</p>", "");
                }
                String viceDirectorSuggesttion = dispatchDoc.getViceDirectorSuggesttion();
                if (Validate.isString(viceDirectorSuggesttion)) {
                    vice = viceDirectorSuggesttion.replaceAll("<br>", "<w:br />").replaceAll("&nbsp;", " ")
                            .replaceAll("<p style='text-align:right;'>", "").replaceAll("</p>", "");
                }
                dispatchData.put("mianChargeSuggest", main);
                dispatchData.put("secondChargeSuggest", second);
                dispatchData.put("ministerSuggesttion", sugge);
                dispatchData.put("viceDirectorSuggesttion", vice);
                //是否调概
                dispatchData.put("ischangeEstimate", (signDto == null || signDto.getIschangeEstimate() == null) ? 0 : signDto.getIschangeEstimate());

                ProjectConstant.REVIEW_STATE_ENUM disReviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageType);
                if(Validate.isObject(disReviewStateEnum)){
                    switch (disReviewStateEnum){
                        /**
                         * 建议书
                         */
                        case STAGESUG:
                            dispatchData.put("wpTile", "项目建议书发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_XMJYS.getKey(), path);
                            break;
                        /**
                         * 可研
                         */
                        case STAGESTUDY:
                            List<SignDto> signDtoList = null;
                            if (signDto != null) {
                                signDtoList = signDto.getAssociateSignDtoList();
                            }
                            List<DispatchDocDto> dispatchList = new ArrayList<DispatchDocDto>();
                            List<DispatchDocDto> dispatchViewList = new ArrayList<DispatchDocDto>();
                            if (null != signDtoList) {
                                for (int i = 0; i < signDtoList.size(); i++) {
                                    if (null != signDtoList.get(i).getDispatchDocDto()) {
                                        dispatchList.add(signDtoList.get(i).getDispatchDocDto());
                                    }
                                }
                            }
                            dispatchViewList.add(getDispatchStage("项目建议书", dispatchList));
                            dispatchData.put("dispatchList", dispatchViewList);
                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_KXXYJ.getKey(), path);
                            break;
                        /**
                         * 其他
                         */
                        case STAGEOTHER:
                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_OTHER.getKey(), path);
                            break;
                        /**
                         * 登记赋码
                         */
                        case REGISTERCODE:
                            dispatchData.put("wpTile", "登记赋码发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_DJFM.getKey(), path);

                            break;
                        /**
                         * 项目概算
                         */
                        case STAGEBUDGET:
                            List<SignDto> signBugDtoList = null;
                            if (signDto != null) {
                                signBugDtoList = signDto.getAssociateSignDtoList();
                            }
                            List<DispatchDocDto> dispatchBugList = new ArrayList<DispatchDocDto>();
                            Map<String , DispatchDocDto> dispatchBugViewList = new LinkedHashMap<>();
                            if (null != signBugDtoList) {
                                for (int i = 0,l=signBugDtoList.size(); i < l; i++) {
                                    if (null != signBugDtoList.get(i).getDispatchDocDto()) {
                                        dispatchBugList.add(signBugDtoList.get(i).getDispatchDocDto());
                                    }
                                }
                            }
                            dispatchBugViewList.put("项目建议书" , getDispatchStage("项目建议书", dispatchBugList));
                            dispatchBugViewList.put("可行性研究" , getDispatchStage("可行性研究报告", dispatchBugList));
                            if (signDto != null && signDto.getIschangeEstimate() != null
                                    && "9".equals(signDto.getIschangeEstimate())) {
                                dispatchBugViewList.put("项目概算" , getDispatchStage("项目概算", dispatchBugList));
                            }
                            dispatchData.put("dispatchList", dispatchBugViewList);
                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_XMGS.getKey(), path);
                            break;
                        /**
                         * 资金申请报告
                         */
                        case STAGEREPORT:
                            List<SignDto> signPortDtoList = null;
                            if (signDto != null) {
                                signPortDtoList = signDto.getAssociateSignDtoList();
                            }
                            List<DispatchDocDto> dispatchPortList = new ArrayList<DispatchDocDto>();
                            List<DispatchDocDto> dispatchPortViewList = new ArrayList<DispatchDocDto>();
                            if (null != signPortDtoList) {
                                for (int i = 0,l=signPortDtoList.size(); i < l; i++) {
                                    if (null != signPortDtoList.get(i).getDispatchDocDto()) {
                                        dispatchPortList.add(signPortDtoList.get(i).getDispatchDocDto());
                                    }
                                }
                            }
                            dispatchData.put("wpTile", "资金申请报告发文审批表");
                            dispatchPortViewList.add(getDispatchStage("资金申请报告", dispatchPortList));
                            dispatchData.put("dispatchList", dispatchPortViewList);

                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_ZJSB.getKey(), path);
                            break;
                        /**
                         * 进口设备
                         */
                        case STAGEDEVICE:
                            dispatchData.put("wpTile", "进口设备发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_JKSB.getKey(), path);
                            break;
                        /**
                         * 设备清单（国产和进口）
                         */
                        case STAGEHOMELAND:
                        case STAGEIMPORT:
                            dispatchData.put("wpTile", "设备清单发文审批表");
                            file = TemplateUtil.createDoc(dispatchData, Template.DOWNLOAD_DISPATHDOC_SBQD.getKey(), path);
                            break;
                        default:
                            ;

                    }
                }
                break;

            case "SIGN_EXPERT":
                //专家评审费发放表
                if ("SIGN_EXPERT_PAY".equals(stageType)) {
                    //项目环节的专家评分和专家评审费
                    Map<String, Object> expertData = new HashMap<>();
//                    ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);

                    List<ExpertSelectedDto> expertSelectedList = new ArrayList<>();
                    List<ExpertSelectedDto> expertSelectedList1 = new ArrayList<>();
                    List<ExpertSelectedDto> expertSelectedList2 = new ArrayList<>();

                    Boolean isSplit = false;

                    //合计 ： 评审费 税额  总计
                    BigDecimal reviewCostSum1 = new BigDecimal(0);
                    BigDecimal reviewTaxesSum1 = new BigDecimal(0);
                    BigDecimal totalCostSum1 = new BigDecimal(0);

                    BigDecimal reviewCostSum2 = new BigDecimal(0);
                    BigDecimal reviewTaxesSum2 = new BigDecimal(0);
                    BigDecimal totalCostSum2 = new BigDecimal(0);
                    List<ExpertSelectedDto> expertSelectedDtoList = expertSelectedRepo.expertSelectedByIds(businessId);
                    ExpertReviewDto  expertReview = new ExpertReviewDto();
                    if (expertSelectedDtoList != null && expertSelectedDtoList.size() > 0) {
                        expertReview   =  expertSelectedDtoList.get(0).getExpertReviewDto();

                        for (ExpertSelectedDto expertSelectedDto : expertSelectedDtoList) {
                            ExpertSelectedDto expertSelected = new ExpertSelectedDto(); //不拆分时
                            ExpertSelectedDto expertSelected1 = new ExpertSelectedDto(); //第一张表
                            ExpertSelectedDto expertSelected2 = new ExpertSelectedDto(); //第二张表

                            if (EnumState.YES.getValue().equals(expertSelectedDto.getIsJoin())
                                    && EnumState.YES.getValue().equals(expertSelectedDto.getIsConfrim())) {
                                BeanCopierUtils.copyPropertiesIgnoreNull(expertSelectedDto, expertSelected);

                                //是专家评审费才需要计算费用
                                if ("SIGN_EXPERT_PAY".equals(stageType)) {
                                    BeanCopierUtils.copyPropertiesIgnoreNull(expertSelectedDto, expertSelected1);
                                    if (EnumState.YES.getValue().equals(expertSelectedDto.getIsSplit())) {
                                        isSplit = true;

                                        BeanCopierUtils.copyPropertiesIgnoreNull(expertSelectedDto, expertSelected2);
                                        //判断第一张表费用是否为空，为空默认为评审费总金额
                                        expertSelected1.setReviewCost(expertSelectedDto.getOneCost() == null ? expertSelectedDto.getReviewCost() : expertSelectedDto.getOneCost());

                                        //判断缴税金额不为空，并且评审费不为0，则计算缴税金额，否则缴税金额为0
                                        if (expertSelectedDto.getReviewTaxes() != null && expertSelectedDto.getReviewCost().compareTo(new BigDecimal(0)) != 0) {
                                            expertSelected1.setReviewTaxes(expertSelectedDto.getReviewTaxes().divide(expertSelectedDto.getReviewCost(), 3, BigDecimal.ROUND_HALF_UP).multiply(expertSelected1.getReviewCost()));
                                        } else {
                                            expertSelected1.setReviewTaxes(BigDecimal.ZERO);
                                        }
                                        expertSelected1.setTotalCost(Arith.safeAdd(expertSelected1.getReviewCost(), expertSelected1.getReviewTaxes()));

                                        expertSelected2.setReviewCost(expertSelectedDto.getReviewCost().subtract(expertSelected1.getReviewCost()));
                                        if (expertSelectedDto.getReviewTaxes() != null && expertSelectedDto.getReviewCost().compareTo(BigDecimal.ZERO) != 0) {
                                            expertSelected2.setReviewTaxes(expertSelectedDto.getReviewTaxes().subtract(expertSelected1.getReviewTaxes()));
                                        } else {
                                            expertSelected2.setReviewTaxes(BigDecimal.ZERO);
                                        }
                                        expertSelected2.setTotalCost(expertSelected2.getReviewCost().add(expertSelected2.getReviewTaxes()));

                                        reviewCostSum2 = reviewCostSum2.add(expertSelected2.getReviewCost());
                                        reviewTaxesSum2 = reviewTaxesSum2.add(expertSelected2.getReviewTaxes());
                                        totalCostSum2 = totalCostSum2.add(Arith.safeAdd(expertSelected2.getReviewCost(), expertSelected2.getReviewTaxes()));
                                        expertSelectedList2.add(expertSelected2);
                                    }
                                    BigDecimal reviewCostCount = expertSelected1.getReviewCost() == null ? BigDecimal.ZERO : expertSelected1.getReviewCost();
                                    BigDecimal totalTaxesCount = expertSelected1.getReviewTaxes() == null ? BigDecimal.ZERO : expertSelected1.getReviewTaxes();
                                    reviewCostSum1 = reviewCostSum1.add(reviewCostCount);
                                    reviewTaxesSum1 = reviewTaxesSum1.add(totalTaxesCount);
                                    totalCostSum1 = totalCostSum1.add(Arith.safeAdd(reviewCostCount, totalTaxesCount));

                                    expertSelectedList1.add(expertSelected1);
                                }
                                expertSelectedList.add(expertSelected);
                            }
                        }

                        String reviewTitle = expertReview.getReviewTitle();
                        expertData.put("projectName", reviewTitle);
                        expertData.put("projectName2", reviewTitle.replaceAll("专家", "外地专家"));
                        if (isSplit) {
                            expertData.put("expertList", expertSelectedList1);
                            expertData.put("expertList2", expertSelectedList2);
                        } else {
                            expertData.put("expertList", expertSelectedList);
                        }
                    }
                    expertData.put("reviewCostSum", isSplit ? reviewCostSum1 : expertReview.getReviewCost());
                    expertData.put("reviewTaxesSum", isSplit ? reviewTaxesSum1 : expertReview.getReviewTaxes());
                    expertData.put("totalCostSum", isSplit ? totalCostSum1 : Arith.safeAdd(expertReview.getReviewCost(), expertReview.getReviewTaxes()));

                    expertData.put("reviewCostSum2", reviewCostSum2);
                    expertData.put("reviewTaxesSum2", reviewTaxesSum2);
                    expertData.put("totalCostSum2", totalCostSum2);

                    expertData.put("payDate", expertReview.getReviewDate() == null ? "" : DateUtils.converToString(expertReview.getReviewDate(), "yyyy年MM月dd日"));

                    if (isSplit) {
                        file = TemplateUtil.createDoc(expertData, Template.DOWNLOAD_EXPERT_PAYMENT.getKey(), path);
                    } else {
                        file = TemplateUtil.createDoc(expertData, Template.DOWNLOAD_EXPERT_PAYMENT_one.getKey(), path);
                    }


                }
                //专家评分
                if ("SIGN_EXPERT_SCORE".equals(stageType)) {
                    Map<String, Object> expertData = new HashMap<>();
                    ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);
                    List<ExpertSelected> expertSelectedList = expertReview.getExpertSelectedList();
                    List<ExpertSelected> expertSelectedList2 = new ArrayList<>();

                    if(expertSelectedList != null && expertSelectedList.size() > 0 ){
                        for(ExpertSelected expertSelected : expertSelectedList){
                            if(EnumState.YES.getValue().equals( expertSelected.getIsJoin()) && EnumState.YES.getValue().equals(expertSelected.getIsConfrim()) ){
                                expertSelectedList2.add(expertSelected);
                            }
                        }
                    }
                    expertData.put("expertList", expertSelectedList2 );
                    file = TemplateUtil.createDoc(expertData, Template.DOWNLOAD_EXPERT_SCORD.getKey(), path);
                }
                break;
            case "ADDSUPPLETER":
                //拟补充资料函
                AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), businessId);
                Map<String, Object> addsuppletterData = TemplateUtil.entryAddMap(addSuppLetter);
                if (Validate.isString(addSuppLetter.getLeaderSignIdeaContent())) {
                    String leaderSignIdea = addSuppLetter.getLeaderSignIdeaContent().replaceAll("<br>", "").replaceAll("&nbsp;", "");
                    addsuppletterData.put("leaderSignIdea", leaderSignIdea);

                }
                file = TemplateUtil.createDoc(addsuppletterData, Template.DOWNLOAD_ADDSUPPLETER.getKey(), path);
                break;

            case "SIGN_UNIT":
                //单位评分
                if ("SIGN_UNIT_SCORE".equals(stageType)) {
                    UnitScore unitScore = unitScoreRepo.findById(UnitScore_.id.getName(), businessId);
                    Map<String, Object> unitScoerMap = new HashMap<>();
                    if (Validate.isObject(unitScore)) {
                        unitScoerMap.put("coName", unitScore.getCompany().getCoName());
                        unitScoerMap.put("coPhone", unitScore.getCompany().getCoPhone());
                        unitScoerMap.put("coPC", unitScore.getCompany().getCoPC());
                        unitScoerMap.put("coAddress", unitScore.getCompany().getCoAddress());
                        unitScoerMap.put("score", unitScore.getScore());
                        unitScoerMap.put("describes", unitScore.getDescribes());
                    }
                    file = TemplateUtil.createDoc(unitScoerMap, Template.DOWNLOAD_UNIT_SCORE.getKey(), path);
                }
                break;
            case "EXPERT":
                //专家申请表
                ExpertDto expertDto = expertService.findById(businessId);
                Expert expert = new Expert();
                BeanCopierUtils.copyPropertiesIgnoreNull(expertDto, expert);
                Map<String, Object> expertDataMap = TemplateUtil.entryAddMap(expert);
                WorkExpeDto[] workExpes = new WorkExpeDto[4];
                ProjectExpeDto[] projectExpes = new ProjectExpeDto[4];

                if (expertDto.getWorkDtoList() != null && expertDto.getWorkDtoList().size() > 0) {
                    for (int i = 0; i < expertDto.getWorkDtoList().size() && i < 4; i++) {
                        WorkExpeDto workExpeDto = expertDto.getWorkDtoList().get(i);
                        workExpeDto.setBeginTimeStr(DateUtils.converToString(workExpeDto.getBeginTime(), "yyyyMMdd"));
                        workExpeDto.setEndTimeStr(DateUtils.converToString(workExpeDto.getEndTime(), "yyyyMmdd"));
                        workExpes[i] = workExpeDto;
                    }
                }
                if (expertDto.getProjectDtoList() != null && expertDto.getProjectDtoList().size() > 0) {
                    for (int i = 0; i < expertDto.getProjectDtoList().size() && i < 4; i++) {
                        projectExpes[i] = expertDto.getProjectDtoList().get(i);
                    }
                }
                expertDataMap.put("workList", workExpes);
                expertDataMap.put("projectList", projectExpes);

                file = TemplateUtil.createDoc(expertDataMap, Template.DOWNLOAD_EXPERT.getKey(), path);

                break;

            case "EXPERTOFFER":
                //专家聘书
                ExpertOffer expertOffer = expertOfferRepo.findById(ExpertOffer_.id.getName(), businessId);
                Map<String, Object> expertOfferDataMap = new HashMap<>();
                expertOfferDataMap.put("name", expertOffer.getExpert().getName());
                expertOfferDataMap.put("sex", expertOffer.getExpert().getSex());
                expertOfferDataMap.put("birthDay", expertOffer.getExpert().getBirthDay());
                expertOfferDataMap.put("idCard", expertOffer.getExpert().getIdCard());
                expertOfferDataMap.put("qualifiCations", expertOffer.getExpert().getQualifiCations());
                expertOfferDataMap.put("post", expertOffer.getExpert().getPost());
                expertOfferDataMap.put("sendCcieDate", expertOffer.getSendCcieDate());
                expertOfferDataMap.put("period", expertOffer.getPeriod());
                expertOfferDataMap.put("expertNo", expertOffer.getExpert().getExpertNo());
                expertOfferDataMap.put("fafang", COMPANY_NAME);

                file = TemplateUtil.createDoc(expertOfferDataMap, Template.DOWNLOAD_EXPERTOFFER.getKey(), path);
                break;

            case "TOPICINFO":
                //课题研究
                TopicInfoDto topicInfoDto = topicInfoService.findById(businessId);
                //归档
                if ("TOPICINFO_FILERECORD".equals(stageType)) {
                    if (topicInfoDto != null) {
                        FilingDto filingDto = topicInfoDto.getFilingDto();
                        Map<String, Object> filingData = TemplateUtil.entryAddMap(filingDto);
                        AddRegisterFileDto[] registerFileDto = new AddRegisterFileDto[5];
                        if (filingDto != null && filingDto.getRegisterFileDto() != null && filingDto.getRegisterFileDto().size() > 0) {
                            for (int i = 0; i < filingDto.getRegisterFileDto().size() && i < 5; i++) {
                                registerFileDto[i] = filingDto.getRegisterFileDto().get(i);
                            }
                        }
                        filingData.put("registerFileList", registerFileDto);
                        file = TemplateUtil.createDoc(filingData, Template.DOWNLOAD_TOPICINFO_FILERECORD.getKey(), path);
                    }
                }

                //工作方案
                if ("TOPICINFO_WORKPROGRAM".equals(stageType)) {
                    if (topicInfoDto != null) {
                        WorkPlanDto workPlanDto = topicInfoDto.getWorkPlanDto();
                        Map<String, Object> workPlanData = TemplateUtil.entryAddMap(workPlanDto);
                        workPlanData.put("createdDate", DateUtils.converToString(workPlanDto.getCreatedDate(), "yyyy年MM月dd日"));
                        String rbDateStr = "";
                        String address = "";
                        if (workPlanDto != null && workPlanDto.getRoomDtoList() != null && workPlanDto.getRoomDtoList().size() > 0) {
                            rbDateStr = workPlanDto.getRoomDtoList().get(0).getRbDate();
                            address = workPlanDto.getRoomDtoList().get(0).getAddressName();
                        }
                        workPlanData.put("rbDateStr", rbDateStr);
                        workPlanData.put("addRess", address);
                        ExpertDto[] expeDtos = new ExpertDto[10];
                        if (workPlanDto != null && workPlanDto.getExpertDtoList() != null && workPlanDto.getExpertDtoList().size() > 0) {
                            for (int i = 0; i < workPlanDto.getExpertDtoList().size() && i < 10; i++) {
                                expeDtos[i] = workPlanDto.getExpertDtoList().get(i);
                            }
                        }
                        workPlanData.put("expertList", expeDtos);
                        file = TemplateUtil.createDoc(workPlanData, Template.DOWNLOAD_TOPICINFO_WORKPROGRAM.getKey(), path);
                    }
                }

                break;

            case "MONTHLY":
                //月报简报
                AddSuppLetterDto addSuppLetterDto = addSuppLetterService.findById(businessId);
                Map<String, Object> addSuppleterData = TemplateUtil.entryAddMap(addSuppLetterDto);
                file = TemplateUtil.createDoc(addSuppleterData, Template.DOWNLOAD_MONTHLY.getKey(), path);
                break;

            case "ARCHIVES":
                //借阅档案
                ArchivesLibraryDto archivesLibraryDto = archivesLibraryService.findById(businessId);
                Map<String, Object> archivesData = TemplateUtil.entryAddMap(archivesLibraryDto);
                file = TemplateUtil.createDoc(archivesData, Template.DOWNLOAD_ARCHIVES_DETAIL.getKey(), path);
                break;

            //补充资料清单
            case "ADDSUPPLEFILE":
                Sign signss = signRepo.findById(Sign_.signid.getName(), businessId);
                List<AddRegisterFile> addRegisterFileList2 = addRegisterFileService.findByBusIdAndBusType(businessId, 3);

                String stage = "评估论证";
                if((STAGEBUDGET.getZhCode()).equals(signss.getReviewstage())){
                    stage = "项目概算";
                }
                Map<String, Object> addFileData = new HashMap<>();
                addFileData.put("fileObjList", addRegisterFileList2);
                addFileData.put("signNum", signss.getSignNum());
                addFileData.put("projectname", signss.getProjectname());
                addFileData.put("builtcompanyName", signss.getBuiltcompanyName());
                addFileData.put("projectcode", signss.getProjectcode());
                addFileData.put("strDate", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                addFileData.put("stage" , stage);
                file = TemplateUtil.createDoc(addFileData, Template.DOWNLOAD_DD_REGISTER_FILE.getKey(), path);
                break;
            case "PROJECTSTOP":
                ProjectStop projectStop = projectStopRepo.findById(ProjectStop_.stopid.getName(), businessId);
                Sign sign1 = signRepo.findById(projectStop.getSign().getSignid());
                Map<String, Object> psData = TemplateUtil.entryAddMap(projectStop);
                psData.put("projectname", sign1.getProjectname());
                psData.put("builtcompanyname", sign1.getBuiltcompanyName());
                psData.put("mOrgName", sign1.getmOrgName());
                psData.put("mUserName", sign1.getmUserName());
                psData.put("receivedate", DateUtils.converToString(sign1.getReceivedate(), "yyyy-MM-dd"));
                file = TemplateUtil.createDoc(psData, Template.DOWNLOAD_PROJECT_STOP.getKey(), path);
                break;
            case "VPROJECT":
                //委项目处理表
                Sign vSign = signRepo.findById(Sign_.signid.getName() , businessId);
                Map<String, Object> vData = TemplateUtil.entryAddMap(vSign);
                file = TemplateUtil.createDoc(vData, Template.DOWNLOAD_PROJECT_VPROJECT.getKey(), path);
                break;

            default:
                break;
        }
        return file;
    }

    /**
     * 打印归档资料
     */
    @RequestMapping(name = "打印归档资料", path = "otherFileDownload/{businessId}/{fileId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public  void otherFileDownload(@PathVariable String businessId , @PathVariable String fileId , HttpServletResponse response){
        InputStream inputStream = null;
        OutputStream out = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Template.WORD_SUFFIX.getKey();
            String fileName = "";
            FileRecord FileRecord = fileRecordRepo.findById("signid",businessId);
            Map<String, Object> otherFileData = new HashMap<>();
            otherFileData.put("fileNo", FileRecord.getFileNo());
            otherFileData.put("projectName", FileRecord.getProjectName());
            otherFileData.put("projectCompany", FileRecord.getProjectCompany());
            otherFileData.put("projectCode", FileRecord.getProjectCode());
            otherFileData.put("OtherTitle", "归档表");
            List<AddRegisterFile> addRegisterFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.id.getName() , fileId , null);
            otherFileData.put("otherFileList", addRegisterFileList);
            file = TemplateUtil.createDoc(otherFileData, Template.DOWNLOAD_OTHER_FILE.getKey(), path);
            //重置结果集
            response.reset();
            //使用新的转换控件
            out = response.getOutputStream();
            if (file != null) {
                inputStream = new FileInputStream(file);
            }
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
            byte[] arr = new byte[1024];
            int len;
            while( ( len=inputStream.read(arr) ) != -1 ) {
                out.write(arr, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if(out != null){
                    out.close();
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

}
