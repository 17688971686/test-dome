package cs.service.sys;

import cs.common.HqlBuilder;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.SysConstants;
import cs.common.ftp.ConfigProvider;
import cs.common.ftp.FtpClientConfig;
import cs.common.ftp.FtpUtils;
import cs.common.utils.*;
import cs.domain.sys.Ftp;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.model.PageModelDto;
import cs.model.sys.SysConfigDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author ldm
 */
@Service
public class SysFileServiceImpl implements SysFileService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private SysFileRepo sysFileRepo;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private FtpRepo ftpRepo;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg save(MultipartFile multipartFile, String fileName, String businessId, String fileType,
                          String mainId, String mainType, String sysfileType, String sysBusiType) {
        try {
            String fileUploadPath = SysFileUtil.getUploadPath();
            String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath, mainType, mainId, sysBusiType, fileName);

            SysFile sysFile = new SysFile();
            sysFile.setSysFileId(UUID.randomUUID().toString());
            sysFile.setBusinessId(businessId);
            sysFile.setFileSize(multipartFile.getSize());
            sysFile.setShowName(fileName);
            sysFile.setFileType(fileType);
            sysFile.setFileUrl(relativeFileUrl);
            sysFile.setSysfileType(sysfileType);
            sysFile.setMainId(mainId);
            sysFile.setMainType(mainType);
            sysFile.setSysBusiType(sysBusiType);

            Date now = new Date();
            sysFile.setCreatedBy(SessionUtil.getLoginName());
            sysFile.setModifiedBy(SessionUtil.getLoginName());
            sysFile.setCreatedDate(now);
            sysFile.setModifiedDate(now);

            sysFileRepo.save(sysFile);

            //先保存成功，
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileUploadPath + File.separator + relativeFileUrl));
            stream.write(multipartFile.getBytes());
            stream.close();
            SysFileDto sysFileDto = new SysFileDto();
            BeanCopierUtils.copyProperties(sysFile, sysFileDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "文件上传成功！", sysFileDto);

        } catch (Exception e) {
            return ResultMsg.error("文件上传错误，请联系系统管理员确认！");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysFile sysFile) {
        sysFileRepo.save(sysFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg saveToFtp(long size, String fileName, String businessId, String fileType, String relativeFileUrl,
                               String mainId, String mainType, String sysFileType, String sysBusinessType, Ftp ftp) {
        try {
            SysFile sysFile = new SysFile();
            sysFile.setSysFileId(UUID.randomUUID().toString());
            sysFile.setBusinessId(businessId);
            sysFile.setFileSize(size);
            sysFile.setShowName(fileName);
            sysFile.setFileType(fileType);
            sysFile.setFileUrl(relativeFileUrl);
            sysFile.setSysfileType(sysFileType);
            sysFile.setMainId(mainId);
            sysFile.setMainType(mainType);
            sysFile.setSysBusiType(sysBusinessType);

            Date now = new Date();
            sysFile.setCreatedBy(SessionUtil.getLoginName());
            sysFile.setModifiedBy(SessionUtil.getLoginName());
            sysFile.setCreatedDate(now);
            sysFile.setModifiedDate(now);
            sysFile.setFtp(ftp);
            sysFileRepo.save(sysFile);

            //先保存成功，
            SysFileDto sysFileDto = new SysFileDto();
            BeanCopierUtils.copyProperties(sysFile, sysFileDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "文件上传成功！", sysFileDto);

        } catch (Exception e) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件上传错误，请联系系统管理员确认！");
        }
    }

    @Override
    public PageModelDto<SysFileDto> get(ODataObj odataObj) {
        List<SysFile> sysFileList = sysFileRepo.findByOdata(odataObj);
        List<SysFileDto> sysFileDtoList = new ArrayList<SysFileDto>();
        for (SysFile item : sysFileList) {
            SysFileDto sysFileDto = new SysFileDto();
            sysFileDto.setSysFileId(item.getSysFileId());
            sysFileDto.setBusinessId(item.getBusinessId());
            sysFileDto.setFileUrl(item.getFileUrl());
            sysFileDto.setShowName(item.getShowName());
            sysFileDto.setFileType(item.getFileType());
            sysFileDto.setFileSizeStr(SysFileUtil.getFileSize(item.getFileSize()));
            sysFileDtoList.add(sysFileDto);
        }
        PageModelDto<SysFileDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(sysFileDtoList);
        logger.info("查询文件");
        return pageModelDto;
    }

    /**
     * 删除记录，同时删除文件
     *
     * @param sysFileId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg deleteById(String sysFileId) {
        return sysFileRepo.deleteByFileId(sysFileId);
    }

    /**
     * 根据ID获取附件信息
     *
     * @param sysFileId
     * @return
     */
    @Override
    public SysFile findFileById(String sysFileId) {
        return sysFileRepo.findById(sysFileId);
    }


    /**
     * 根据主业务ID获取附件信息
     *
     * @param mainId
     * @return
     */
    @Override
    public List<SysFileDto> findByMainId(String mainId) {
        List<SysFile> sysFiles = sysFileRepo.findByMainId(mainId);
        List<SysFileDto> sysFileDtoList = new ArrayList<SysFileDto>(sysFiles == null ? 0 : sysFiles.size());
        if (Validate.isList(sysFiles)) {
            sysFiles.forEach(sf -> {
                SysFileDto sysFileDto = new SysFileDto();
                BeanCopierUtils.copyProperties(sf, sysFileDto);
                if (null != sf.getFileSize()) {
                    sysFileDto.setFileSizeStr(SysFileUtil.getFileSize(sf.getFileSize()));
                }
                sysFileDtoList.add(sysFileDto);
            });
        }
        return sysFileDtoList;
    }

    /**
     * 根据主业务ID和分类获取附件信息
     *
     * @param mainId
     * @return
     */
    @Override
    public List<SysFileDto> queryFile(String mainId, String sysBusiType) {
        List<SysFile> sysFiles = sysFileRepo.queryFileList(mainId, sysBusiType);
        List<SysFileDto> sysFileDtoList = new ArrayList<SysFileDto>(sysFiles == null ? 0 : sysFiles.size());
        if (sysFiles != null) {
            sysFiles.forEach(sf -> {
                SysFileDto sysFileDto = new SysFileDto();
                BeanCopierUtils.copyProperties(sf, sysFileDto);
                sysFileDtoList.add(sysFileDto);
            });
        }
        return sysFileDtoList;
    }

    /**
     * 批量更新
     *
     * @param saveFileList
     */
    @Override
    public void bathSave(List<SysFile> saveFileList) {
        sysFileRepo.bathUpdate(saveFileList);
    }

    /**
     * 获取默认的ftpId
     *
     * @return
     */
    @Override
    public String findFtpId() {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(SysConstants.SYS_CONFIG_ENUM.FTPIP.toString());
        if (sysConfigDto == null || !Validate.isString(sysConfigDto.getConfigValue())) {
            return "";
        } else {
            return sysConfigDto.getConfigValue();
        }
    }

    /**
     * 获取文件服务器地址
     *
     * @param relativeFileUrl
     * @return
     */
    @Override
    public String getFtpRoot(String relativeFileUrl) {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(SysConstants.SYS_CONFIG_ENUM.FTPROOT.toString());
        if (!Validate.isObject(sysConfigDto) || !Validate.isString(sysConfigDto.getConfigValue())) {
            return null;
        } else {
            String ftpRoot = sysConfigDto.getConfigValue();
            if (relativeFileUrl.startsWith(File.separator) || relativeFileUrl.startsWith("/")) {
                relativeFileUrl = File.separator + ftpRoot + relativeFileUrl;
            } else {
                relativeFileUrl = File.separator + ftpRoot + relativeFileUrl + File.separator;
            }
            return relativeFileUrl;
        }
    }

    /**
     * 通过业务ID和业务类型删除对应的文件
     *
     * @param businessId
     * @param businessType
     */
    @Override
    public void deleteByBusinessIdAndBusinessType(String businessId, String businessType) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("delete from " + SysFile.class.getSimpleName() + " where " + SysFile_.mainId.getName() + "=:mainId ");
        hqlBuilder.append(" and " + SysFile_.sysBusiType.getName() + "=:businessType");
        hqlBuilder.setParam("mainId", businessId);
        hqlBuilder.setParam("businessType", businessType);
        sysFileRepo.executeHql(hqlBuilder);
    }

    /**
     * 获取远程连接文件
     *
     * @param businessId
     * @param sysFileDtoList
     * @return
     */
    @Override
    public ResultMsg downRemoteFile(String businessId, List<SysFileDto> sysFileDtoList, String userId, String mainType, String busiType) {
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "没有附件信息！");
        if (Validate.isList(sysFileDtoList)) {
            List<SysFile> saveFileList = new ArrayList<>();
            int totalFileCount = sysFileDtoList.size(), errorCount = 0;
            //连接ftp
            Ftp f = ftpRepo.findById(cs.domain.sys.Ftp_.ipAddr.getName(), findFtpId());
            FtpUtils ftpUtils = new FtpUtils();
            FtpClientConfig k = ConfigProvider.getUploadConfig(f);
            String relativeFileUrl = File.separator + mainType + File.separator + businessId + File.separator + busiType;
            //上传到ftp,如果有根目录，则加入根目录
            if (Validate.isString(k.getFtpRoot())) {
                relativeFileUrl = File.separator + k.getFtpRoot() + relativeFileUrl;
            }
            //读取附件
            for (int i = 0, l = totalFileCount; i < l; i++) {
                try {
                    SysFileDto sysFileDto = sysFileDtoList.get(i);
                    String showName = sysFileDto.getShowName();
                    if (!Validate.isString(sysFileDto.getFileUrl())) {
                        continue;
                    }
                    /******  旧的下载方式 begin *****/
                   /* URL url = new URL(sysFileDto.getFileUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置超时间为3秒
                    conn.setConnectTimeout(600000);
                    //防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    //得到输入流*/
                    /******  旧的下载方式 end *****/

                    boolean fileExist = false;
                    String uploadFileName = "";
                    //如果附件已存在，则覆盖，否则新增
                    SysFile sysFile = sysFileRepo.isExistFile(relativeFileUrl, showName);
                    if (null != sysFile) {
                        String fileUrl = sysFile.getFileUrl();
                        String removeRelativeUrl = fileUrl.substring(0, fileUrl.lastIndexOf(File.separator));
                        if (relativeFileUrl.equals(removeRelativeUrl)) {
                            uploadFileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1, fileUrl.length());
                        }
                        fileExist = true;
                    } else {
                        sysFile = new SysFile();
                        sysFile.setMainId(businessId);
                        sysFile.setBusinessId(businessId);
                        sysFile.setMainType(mainType);
                        sysFile.setSysBusiType(busiType);
                        sysFile.setFileSize(sysFileDto.getFileSize());
                        sysFile.setShowName(showName);
                        sysFile.setFileType(showName.substring(showName.lastIndexOf("."), showName.length()));
                        uploadFileName = Tools.generateRandomFilename().concat(sysFile.getFileType());
                    }


                    /******  旧的下载方式 begin *****/
                    /*boolean uploadResult = ftpUtils.putFile(k, relativeFileUrl, uploadFileName, conn.getInputStream());
                    conn.disconnect();*/
                    /******  旧的下载方式 end *****/

                    boolean uploadResult = ftpUtils.putFile(k, relativeFileUrl, uploadFileName, IOStreamUtil.getStreamDownloadOutFile(sysFileDto.getFileUrl()));

                    if (uploadResult) {
                        //保存数据库记录
                        if (fileExist) {
                            sysFile.setModifiedBy(userId);
                            sysFile.setModifiedDate(new Date());
                        } else {
                            sysFile.setFileUrl(relativeFileUrl + File.separator + uploadFileName);
                            sysFile.setCreatedBy(userId);
                            sysFile.setModifiedBy(userId);
                            sysFile.setCreatedDate(new Date());
                            sysFile.setModifiedDate(new Date());
                            sysFile.setFtp(f);
                            sysFile.setSysFileId(UUID.randomUUID().toString());
                            sysFile.setBusinessId(businessId);
                        }
                        saveFileList.add(sysFile);
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    logger.error("保存附件异常：" + e.getMessage());
                    errorCount++;
                }
            }
            //保存附件
            if (Validate.isList(saveFileList)) {
                bathSave(saveFileList);
            }
            if (errorCount == 0) {
                resultMsg.setFlag(true);
                resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode());
                resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue());
            } else {
                if (errorCount == totalFileCount) {
                    resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_FILE_EMPTY.getCode());
                    resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_FILE_EMPTY.getValue());
                } else if (errorCount < totalFileCount) {
                    resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_FILE_NOT_ALL.getCode());
                    resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_FILE_NOT_ALL.getValue());
                }
            }
        } else {
            resultMsg.setFlag(true);
            resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode());
            resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue());
        }
        return resultMsg;

    }

    /**
     * 根据业务ID获取附件信息
     */
    /**
     * 根据业务ID获取相应的附件信息
     *
     * @param businessId
     * @return
     */
    @Override
    public List<SysFileDto> findByBusinessId(String businessId) {
        Criteria criteria = sysFileRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(SysFile_.businessId.getName(), businessId));
        List<SysFile> sysFiles = criteria.list();
        List<SysFileDto> sysFileDtoList = new ArrayList<SysFileDto>(sysFiles == null ? 0 : sysFiles.size());
        if (Validate.isString(sysFiles)) {
            sysFiles.forEach(sf -> {
                SysFileDto sysFileDto = new SysFileDto();
                BeanCopierUtils.copyProperties(sf, sysFileDto);
                sysFileDto.setFileSizeStr(SysFileUtil.getFileSize(sf.getFileSize()));
                sysFileDtoList.add(sysFileDto);
            });
        }
        return sysFileDtoList;
    }

}
