package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.ahelper.RealPathResolver;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.ftp.ConfigProvider;
import cs.common.ftp.FtpClientConfig;
import cs.common.ftp.FtpUtils;
import cs.common.utils.*;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertOffer;
import cs.domain.expert.ExpertOffer_;
import cs.domain.expert.ExpertReview;
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
import cs.service.sys.LogService;
import cs.service.sys.SysFileService;
import cs.service.topic.TopicInfoService;
import org.apache.commons.codec.binary.Base64;
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
import java.math.BigDecimal;
import java.util.*;

import static cs.common.constants.Constant.*;


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
        ResultMsg resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "");
        if (multipartFileList == null || multipartFileList.length == 0) {
            resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择要上传的附件");
            return resultMsg;
        }
        //项目附件才需要类型
        if (SysFileType.SIGN.getValue().equals(mainType) && !Validate.isString(sysBusiType)) {
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "附件上传失败，请选择文件类型！");
            return resultMsg;
        }
        StringBuffer errorMsg = new StringBuffer();
        try {
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
                boolean uploadResult = ftpUtils.putFile(k, relativeFileUrl, uploadFileName, multipartFile.getInputStream());
                if (uploadResult) {
                    //保存数据库记录
                    if (null != sysFile) {
                        sysFile.setModifiedBy(SessionUtil.getDisplayName());
                        sysFile.setModifiedDate(new Date());
                        sysFileService.update(sysFile);
                        resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "文件上传成功！");
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

        }
        resultMsg.setReMsg(resultMsg.getReMsg() + errorMsg.toString());
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
                resultMsg = sysFileService.save(multipartFile, fileName, businessId, fileType, mainId, mainType, sysfileType, sysBusiType);
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件上传失败，无法获取文件信息！");
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
            SysFile sysFile = sysFileService.findFileById(sysFileId);
            String fileUrl = sysFile.getFileUrl();
            String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
            String checkFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
            //连接ftp
            Ftp f = sysFile.getFtp();
            FtpUtils ftpUtils = new FtpUtils();
            FtpClientConfig k = ConfigProvider.getDownloadConfig(f);
            boolean checkResult = ftpUtils.checkFileExist(removeRelativeUrl, checkFileName, k);
            if (checkResult) {
                resultMsg = new ResultMsg(true, MsgCode.OK.getValue(), "附件存在，可以进行下载操作！");
            } else {
                resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "没有该附件！");
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "附件确认异常，请联系管理员查看！");
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
        try {
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
            boolean downResult = ftpUtils.downLoadFile(removeRelativeUrl, fileName, k, out);
            if (downResult) {

            } else {
                String resultMsg = "连接文件服务器失败！";
                out.write(resultMsg.getBytes());
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "文件下载", path = "fileDownload", method = RequestMethod.POST)
    public void fileDownload(@RequestParam(required = true) String sysfileId, HttpServletResponse response) throws Exception {
        OutputStream out = response.getOutputStream();
        try {
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
            boolean downResult = ftpUtils.downLoadFile(removeRelativeUrl, fileName, k, out);
            if (downResult) {

            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
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
        try {
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

            //如果是pdf文件，直接输出流，否则要先转为pdf
            if (fileType.equals(".pdf") || fileType.equals(".png") || fileType.equals(".jpg") || fileType.equals(".gif")) {
                out = response.getOutputStream();
                ftpUtils.downLoadFile(removeRelativeUrl, storeFileName, k, out);
            } else {
                downFile = new File(SysFileUtil.getUploadPath() + File.separator + storeFileName);
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
                out.write(buffer); // 输出文件
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
                    String ministerhandlesug = sign.getMinisterhandlesug();
                    dataMap.put("ministerhandlesug", ministerhandlesug == null ? "" : ministerhandlesug.replaceAll("<br>", "<w:br />")
                            .replaceAll("<p style='text-align:right;'>", "").replaceAll("</p>", ""));
                    if (stageType.equals(RevireStageKey.KEY_SUG.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_STUDY.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_OTHER.getValue())) {
                        //建议书、可研、其他
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.STAGE_SUG_SIGN.getKey(), path);
                    } else if (stageType.equals(RevireStageKey.KEY_BUDGET.getValue())) {
                        //概算
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.STAGE_BUDGET_SIGN.getKey(), path);
                    } else if (stageType.equals(Constant.RevireStageKey.KEY_REPORT.getValue())) {
                        //资金
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.APPLY_REPORT_SIGN.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_DEVICE.getValue())) {
                        //进口
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.IMPORT_DEVICE_SIGN.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_HOMELAND.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_IMPORT.getValue())) {
                        //设备清单（国产、进口）
                        file = TemplateUtil.createDoc(dataMap, Constant.Template.DEVICE_BILL_SIGN.getKey(), path);

                    }
                    break;

                case "WORKPROGRAM":
                    WorkProgramDto workProgramDto = workProgramService.initWorkProgramById(businessId);
//                  SignDto signDto =  signService.findById(workProgramDto.getSignId(), true);
//                    WorkProgram workProgram = new WorkProgram();
//                    BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);
                    if (!Validate.isString(workProgramDto.getIsHaveEIA())) {
                        workProgramDto.setIsHaveEIA("0");
                    }
                    Map<String, Object> workData = TemplateUtil.entryAddMap(workProgramDto);

                    List<ExpertSelectedDto> expertSelectedDtoLists = expertSelectedRepo.findByBusinessId(workProgramDto.getId());
                    List<ExpertDto> expertDtoList = workProgramDto.getExpertDtoList();
                    ExpertDto[] expertDtos = new ExpertDto[10];

                    if (expertSelectedDtoLists != null && expertSelectedDtoLists.size() > 0) {
                        for (int i = 0; i < expertSelectedDtoLists.size() && i < 10; i++) {
                            ExpertDto expertDto = expertSelectedDtoLists.get(i).getExpertDto();
                            //目前先改代码，后期有时间转换为改模板，直接遍历select表就可以的
                            //重新设置专业和专家类别，其中专业对应select表的专家小类 ， 专家类别对应select的专家类别
                            expertDto.setMajorStudy(expertSelectedDtoLists.get(i).getMaJorSmall());
                            expertDto.setExpertSort(expertSelectedDtoLists.get(i).getExpeRttype());
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
                    if (null != stageType && (stageType.equals("STAGESUG") || stageType.equals("STAGESTUDY") || stageType.equals("STAGEBUDGET") || stageType.equals("STAGEOTHER"))) {
                        if (stageType.equals("STAGESUG")) {
                            workData.put("wpTile", "项目建议书评审工作方案");
                            workData.put("wpCode", " QR-4.3-02-A3");
                        } else if (stageType.equals("STAGEOTHER")) {
                            workData.put("wpTile", "其它评审工作方案");
                            workData.put("wpCode", " QR-4.3-02-A3");
                        } else if (stageType.equals("STAGESTUDY")) {
                            workData.put("wpTile", "可行性研究报告评审工作方案");
                            workData.put("wpCode", " QR-4.4-01-A3");
                        } else if (stageType.equals("STAGEBUDGET")) {
                            workData.put("wpTile", "项目概算评审工作方案");
                            workData.put("wpCode", " QR-4.7-01-A2");
                        }
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_SUG_WORKPROGRAM.getKey(), path);
                    } else if (null != stageType && stageType.equals("STAGEREPORT")) {
                        workData.put("wpTile", "资金申请报告工作方案");
                        workData.put("wpCode", "QR-4.9-02-A0");
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_REPORT_WORKPROGRAM.getKey(), path);
                    } else if (null != stageType && stageType.equals("STAGEDEVICE")) {
                        workData.put("wpTile", "进口设备工作方案");
                        workData.put("wpCode", "QR-4.9-02-A0");
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_DEVICE_WORKPROGRAM.getKey(), path);
                    } else if (null != stageType && stageType.equals("STAGEIMPORT")) {
                        workData.put("wpTile", "设备清单工作方案");
                        workData.put("wpCode", "QR-4.9-02-A0");
                        file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_HOMELAND_WORKPROGRAM.getKey(), path);
                    } else if (null != stageType && stageType.equals("INFORMATION")) {

                        file = TemplateUtil.createDoc(workData, Constant.Template.INFORMATION.getKey(), path);
                    }
//                    file = TemplateUtil.createDoc(workData, Constant.Template.STAGE_SUG_WORKPROGRAM.getKey(), path);
                    break;
                case "FILERECORD":
                    FileRecordDto fileRecordDto = fileRecordService.initBySignId(businessId);
                    Map<String, Object> fileData = TemplateUtil.entryAddMap(fileRecordDto);
                    if (stageType.equals(RevireStageKey.KEY_SUG.getValue())) {
                        //建议书
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_SUG_FILERECORD.getKey(), path);
                    } else if (stageType.equals(RevireStageKey.KEY_STUDY.getValue())) {
                        //可研
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_STUDY_FILERECORD.getKey(), path);

                    } else if (stageType.equals(RevireStageKey.KEY_BUDGET.getValue())) {
                        //概算
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_BUDGET_FILERECORD.getKey(), path);

                    } else if ("STAGEBUDGET_XS".equals(stageType)) {
                        //概算协审
                        file = TemplateUtil.createDoc(fileData, Template.STAGE_BUDGET_XS_FILERECORD.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_REPORT.getValue())) {
                        //资金
                        file = TemplateUtil.createDoc(fileData, Template.APPLY_REPORT_FILERECORD.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_DEVICE.getValue())) {
                        //进口
                        file = TemplateUtil.createDoc(fileData, Template.IMPORT_DEVICE_FILERECORD.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_HOMELAND.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_IMPORT.getValue())) {
                        //设备清单（国产、进口）
                        AddRegisterFileDto[] registerFileDto = new AddRegisterFileDto[5];
                        if (fileRecordDto != null && fileRecordDto.getRegisterFileDto() != null && fileRecordDto.getRegisterFileDto().size() > 0) {
                            for (int i = 0; i < fileRecordDto.getRegisterFileDto().size() && i < 5; i++) {
                                registerFileDto[i] = fileRecordDto.getRegisterFileDto().get(i);
                            }
                        }
                        fileData.put("registerFileList", registerFileDto);
                        file = TemplateUtil.createDoc(fileData, Template.DEVICE_BILL_FILERECORD.getKey(), path);

                    } else if (stageType.equals(RevireStageKey.KEY_OTHER.getValue())) {
                        //其他阶段
                        Sign s = signRepo.getById(businessId);
                        fileData.put("secrectlevel", s.getSecrectlevel());
                        file = TemplateUtil.createDoc(fileData, Template.OTHERS_FILERECORD.getKey(), path);
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
                    SignDto signDto = signService.findById(dispatchDoc.getSign().getSignid(), true);
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
                    dispatchData.put("ischangeEstimate", signDto.getIschangeEstimate());//是否调概

                    if (stageType.equals(RevireStageKey.KEY_SUG.getValue())) {
                        //建议书
                        dispatchData.put("wpTile", "项目建议书发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.STAGE_SUG_DISPATCHDOC.getKey(), path);
                    } else if (stageType.equals(RevireStageKey.KEY_STUDY.getValue())) {
                        //可研
                        List<SignDto> signDtoList = signDto.getAssociateSignDtoList();
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

                    } else if (stageType.equals(RevireStageKey.KEY_BUDGET.getValue())) {
                        //概算
                        List<SignDto> signDtoList = signDto.getAssociateSignDtoList();
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
                        dispatchViewList.add(getDispatchStage("可行性研究报告", dispatchList));
                        if (signDto != null && signDto.getIschangeEstimate() != null
                                && "9".equals(signDto.getIschangeEstimate())) {
                            dispatchViewList.add(getDispatchStage("项目概算", dispatchList));
                        }
                        dispatchData.put("dispatchList", dispatchViewList);
                        file = TemplateUtil.createDoc(dispatchData, Template.STAGE_BUDGET_DISPATCHDOC.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_REPORT.getValue())) {
                        //资金
                        dispatchData.put("wpTile", "资金申请报告发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.APPLY_REPORT_DISPATCHDOC.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_DEVICE.getValue())) {
                        //进口
                        dispatchData.put("wpTile", "进口设备发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.IMPORT_DEVICE_DISPATCHDOC.getKey(), path);

                    } else if (stageType.equals(Constant.RevireStageKey.KEY_HOMELAND.getValue())
                            || stageType.equals(Constant.RevireStageKey.KEY_IMPORT.getValue())) {
                        //设备清单（国产、进口）
                        dispatchData.put("wpTile", "设备清单发文审批表");
                        file = TemplateUtil.createDoc(dispatchData, Template.DEVICE_BILL_DISPATCHDOC.getKey(), path);
                    } else if (stageType.equals(RevireStageKey.KEY_OTHER.getValue())) {
                        //其他阶段
                        file = TemplateUtil.createDoc(dispatchData, Template.OTHERS_DISPATCHDOC.getKey(), path);
                    }
                    break;

                case "SIGN_EXPERT":
                    //项目环节的专家评分和专家评审费
                    Map<String, Object> expertData = new HashMap<>();
                    ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);

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

                    if (Validate.isObject(expertReview)) {
                        ExpertReviewDto expertReviewDto = expertReviewRepo.formatReview(expertReview);
                        List<ExpertSelectedDto> expertSelectedDtoList = expertReviewDto.getExpertSelectedDtoList();
                        if (expertSelectedDtoList != null && expertSelectedDtoList.size() > 0) {

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
                                            if (expertSelectedDto.getReviewTaxes() != null &&  expertSelectedDto.getReviewCost().compareTo(new BigDecimal(0)) != 0) {
                                                expertSelected1.setReviewTaxes(expertSelectedDto.getReviewTaxes().divide(expertSelectedDto.getReviewCost(), 3, BigDecimal.ROUND_HALF_UP).multiply(expertSelected1.getReviewCost()));
                                            } else {
                                                expertSelected1.setReviewTaxes(BigDecimal.ZERO);
                                            }
                                            expertSelected1.setTotalCost(Arith.safeAdd(expertSelected1.getReviewCost(),expertSelected1.getReviewTaxes()));

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
                        }

                        String[] projectNames = expertReview.getReviewTitle().split("》");
                        expertData.put("projectName", projectNames.length > 0 ? projectNames[0].substring(1, projectNames[0].length()) : expertReview.getReviewTitle());

                        if (isSplit) {
                            expertData.put("expertList", expertSelectedList1);
                            expertData.put("expertList2", expertSelectedList2);
                        } else {
                            expertData.put("expertList", expertSelectedList);
                        }
                    }
                    //专家评审费发放表
                    if ("SIGN_EXPERT_PAY".equals(stageType)) {
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
                       /* expertData.put("projectName" , expertReview.getReviewTitle().substring(1,expertReview.getReviewTitle().length()-1));*/
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
                    expertOfferDataMap.put("fafang", "深圳市政府投资项目评审中心");

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
                    Map<String , Object> addFileData = new HashMap<>();
                    addFileData.put("addFileList" , addRegisterFileList2);
                    addFileData.put("signNum" , signss.getSignNum());
                    addFileData.put("projectname" , signss.getProjectname());
                    addFileData.put("builtcompanyName" , signss.getBuiltcompanyName());
                    addFileData.put("projectcode" , signss.getProjectcode());
                    addFileData.put("strDate" , DateUtils.converToString(new Date() , "yyyy年MM月dd日"));
                    file = TemplateUtil.createDoc(addFileData, Template.ADD_REGISTER_FILE.getKey(), path);

                    break;

                default:
                    ;
            }

            if (file != null) {
                OfficeConverterUtil.convert2PDF(file.getAbsolutePath(), filePath);
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
     * @param stageName
     * @param dispatchDocDtoList
     * @return
     */
    private DispatchDocDto getDispatchStage(String stageName, List<DispatchDocDto> dispatchDocDtoList) {
        DispatchDocDto dispatchDocDto = new DispatchDocDto();
        if (dispatchDocDtoList.size() > 0) {
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
        String fileUrl = sysFileService.getLocalUrl();
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
        try {
            SysFile sysFile = sysFileRepo.findById(sysFileId);
            String fileUrl = sysFile.getFileUrl();
            String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
            String uploadFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
            //上传到ftp,
            Ftp f = sysFile.getFtp();
            FtpUtils ftpUtils = new FtpUtils();
            FtpClientConfig k = ConfigProvider.getUploadConfig(f);
            Base64 decoder = new Base64();
            boolean uploadResult = ftpUtils.putFile(k, removeRelativeUrl, uploadFileName, new ByteArrayInputStream(decoder.decode(base64String)));
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
