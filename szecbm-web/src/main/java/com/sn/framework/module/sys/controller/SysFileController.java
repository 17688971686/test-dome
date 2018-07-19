package com.sn.framework.module.sys.controller;

import com.sn.framework.common.SnRuntimeException;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.Constants;
import com.sn.framework.core.common.ResultMsg;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.common.Validate;
import com.sn.framework.core.ftp.ConfigProvider;
import com.sn.framework.core.ftp.FtpClientConfig;
import com.sn.framework.core.ftp.FtpUtils;
import com.sn.framework.core.util.ResponseUtils;
import com.sn.framework.core.util.SysFileUtil;
import com.sn.framework.module.sys.domain.Ftp;
import com.sn.framework.module.sys.domain.SysFile;
import com.sn.framework.module.sys.model.SysFileDto;
import com.sn.framework.module.sys.repo.IFtpRepo;
import com.sn.framework.module.sys.repo.ISysFileRepo;
import com.sn.framework.module.sys.service.ISysFileService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ldm on 2018/7/11 0011.
 */
@Controller
@RequestMapping(name = "附件管理", path = "sys/sysfile")
public class SysFileController {

    @Autowired
    private ISysFileService sysFileService;

    @Autowired
    private IFtpRepo ftpRepo;

    @Value("${system.sysfile.enableip}")
    private String enabledIP;


   @Autowired
    private ISysFileRepo sysFileRepo;


    @RequiresAuthentication
    @RequestMapping(name = "根据业务ID获取附件", path = "findByBusinessId", method = RequestMethod.GET)
    @ResponseBody
    public List<SysFileDto> findByBusinessId(@RequestParam(required = true) String businessId) {
        List<SysFileDto> sysfileDto = sysFileService.findByBusinessId(businessId);
        return sysfileDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据主模块ID获取附件", path = "findByMainId", method = RequestMethod.GET)
    @ResponseBody
    public List<SysFileDto> findByMainId(@RequestParam(required = true) String mainId) {
        List<SysFileDto> sysfileDto = sysFileService.findByMainId(mainId);
        return sysfileDto;
    }

    /**
     * @param request
     * @param
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
    public List<SysFile> upload(HttpServletRequest request,  List<MultipartFile> files,String sysFileId,
                             String businessId, String mainId, String mainType,
                            String sysfileType, String sysBusiType) {
        ResultMsg resultMsg = new ResultMsg(false, Constants.ReCode.ERROR.name(), "");
        List<SysFile> attachmentList = new ArrayList<>();
        Assert.notEmpty(files, "请选择要上传的附件");
        try {
            String relativeFileUrl = SysFileUtil.generatRelativeUrl(mainType, mainId, sysBusiType, null);
            Ftp f = ftpRepo.getByIP(enabledIP);
            Assert.notNull(f,"请配置ftp信息");
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

            for (MultipartFile multipartFile : files) {
                String fileName = multipartFile.getOriginalFilename();
                if (fileName.indexOf(".") == -1) {
                    throw new SnRuntimeException("附件【" + fileName + "】没有后缀名，无法上传到文件服务器！");
                }
                String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                //统一转成小写
                fileType = fileType.toLowerCase();
                String uploadFileName = "";
                //SysFile sysFile = sysFileService.isExistFile(relativeFileUrl, fileName);
                SysFile sysFile = null;
                //如果附件已存在
                if (StringUtil.isNotBlank(sysFileId)) {
                    sysFile = sysFileRepo.getById(sysFileId);
                    String fileUrl = sysFile.getFileUrl();
                    String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
                    if (relativeFileUrl.equals(removeRelativeUrl)) {
                        uploadFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
                    }
                } else {
                    uploadFileName = SysFileUtil.generateRandomFilename().concat(fileType);
                }
                boolean uploadResult = ftpUtils.putFile(k, relativeFileUrl, uploadFileName, multipartFile.getInputStream());
                if (uploadResult) {
                    //保存数据库记录
                    if (null != sysFile) {
                        sysFile.setModifiedBy(SessionUtil.getDisplayName());
                        sysFile.setModifiedDate(new Date());
                        sysFileService.save(sysFile);
                    } else {
                        sysFile = sysFileService.saveToFtp(multipartFile.getSize(), fileName, businessId, fileType,
                                relativeFileUrl + File.separator + uploadFileName, mainId, mainType, sysfileType, sysBusiType, f);
                    }
                    attachmentList.add(sysFile);
                } else {
                    throw new SnRuntimeException("附件【" + fileName + "】上传失败，无法上传到文件服务器！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SnRuntimeException("附件上传失败，连接ftp服务失败，请核查！");
        } finally {

        }
        return attachmentList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "ftp文件校验", path = "fileSysCheck", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg checkFtpFile(@RequestParam(required = true) String sysFileId) {
        ResultMsg resultMsg;
        try {
            SysFile sysFile = sysFileService.getById(sysFileId);
            String fileUrl = sysFile.getFileUrl();
            String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
            String checkFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
            //连接ftp
            Ftp f = sysFile.getFtp();
            FtpUtils ftpUtils = new FtpUtils();
            FtpClientConfig k = ConfigProvider.getDownloadConfig(f);
            boolean checkResult = ftpUtils.checkFileExist(removeRelativeUrl, checkFileName, k);
            if (checkResult) {
                resultMsg = new ResultMsg(true, Constants.ReCode.OK.name(), "附件存在，可以进行下载操作！");
            } else {
                resultMsg = new ResultMsg(false, Constants.ReCode.ERROR.name(), "没有该附件！");
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, Constants.ReCode.ERROR.name(), "附件确认异常，请联系管理员查看！");
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
            SysFile sysFile = sysFileService.getById(sysfileId);
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


    @RequestMapping(name = "文件下载", path = "fileDownload", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void fileDownload(@RequestParam(required = true)String sysfileId, HttpServletResponse response) throws Exception {
        OutputStream out = response.getOutputStream();
        try {
            SysFile sysFile = sysFileService.getById(sysfileId);
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

    @RequestMapping(name = "下载附件", path = "download/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void download1(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();
       // attachmentService.download(id, request, response);
    }

    @RequestMapping(name = "附件删除", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(required = true) String sysFileId) {
        sysFileService.deleteByFileId(sysFileId);
    }
}
