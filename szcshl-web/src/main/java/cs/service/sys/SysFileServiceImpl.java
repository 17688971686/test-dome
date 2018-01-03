package cs.service.sys;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.sys.Ftp;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignRepo;
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

import static cs.common.Constant.FTP_IP1;
import static cs.common.Constant.MsgCode;

@Service
public class SysFileServiceImpl implements SysFileService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private SysFileRepo sysFileRepo;

    @Autowired
    private SignRepo signRepo;

    @Autowired
    private FtpRepo ftpRepo;

    @Override
    @Transactional
    public ResultMsg save(MultipartFile multipartFile, String fileName, String businessId, String fileType,
                          String mainId, String mainType, String sysfileType, String sysBusiType) {
        try {
            String fileUploadPath = SysFileUtil.getUploadPath();
            String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath, mainType,mainId, sysBusiType, fileName);

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
            BeanCopierUtils.copyProperties(sysFile,sysFileDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"文件上传成功！",sysFileDto);

        } catch (Exception e) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"文件上传错误，请联系系统管理员确认！");
        }
    }

    @Transactional
    @Override
    public void update(SysFile sysFile) {
        sysFileRepo.save(sysFile);
    }

    @Override
    @Transactional
    public ResultMsg saveToFtp(long size, String fileName, String businessId, String fileType,String relativeFileUrl,
                               String mainId, String mainType, String sysfileType, String sysBusiType, Ftp ftp) {
        try {
            SysFile sysFile = new SysFile();
            sysFile.setSysFileId(UUID.randomUUID().toString());
            sysFile.setBusinessId(businessId);
            sysFile.setFileSize(size);
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
            sysFile.setFtp(ftp);
            sysFileRepo.save(sysFile);

            //先保存成功，
            SysFileDto sysFileDto = new SysFileDto();
            BeanCopierUtils.copyProperties(sysFile,sysFileDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"文件上传成功！",sysFileDto);

        } catch (Exception e) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"文件上传错误，请联系系统管理员确认！");
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
    @Transactional
    public ResultMsg deleteById(String sysFileId) {
        ResultMsg resultMsg = null;
        try{
            //连接ftp
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            Ftp f = ftpRepo.findById(cs.domain.sys.Ftp_.ipAddr.getName(),propertyUtil.readProperty(FTP_IP1));
            boolean linkSucess = FtpUtil.connectFtp(f);
            if(linkSucess){
                List<SysFile> fileList = sysFileRepo.findByIds(SysFile_.sysFileId.getName(), sysFileId, null);
                if (Validate.isList(fileList)) {
                    for(int i=0,l=fileList.size();i<l;i++){
                        SysFile fl = fileList.get(i);
                        if(SessionUtil.getLoginName().equals(fl.getCreatedBy())){
                            //先删除ftp上的附件，
                            String remoteFilePath = new String(fl.getFileUrl().getBytes("GBK"), "iso-8859-1");
                            //再删除本地附件
                            FtpUtil.getFtp().deleteFile(remoteFilePath);
                            sysFileRepo.delete(fl);
                        }else{
                            resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"您没有权限删除【"+fl.getShowName()+"】文件！");
                            break;
                        }
                    }
                }else{
                    resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，该文件已被删除！");
                }
            }else{
                resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，无法连接文件服务器！");
            }
        }catch (Exception e){
            resultMsg  = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，连接文件服务器异常！");
        }finally {
            FtpUtil.closeFtp();
        }
        if(resultMsg == null){
            resultMsg = new ResultMsg(true,MsgCode.OK.getValue(),"删除成功！");
        }
        return resultMsg;
    }

    /**
     * 根据ID获取附件信息
     * @param sysfileId
     * @return
     */
    @Override
    public SysFile findFileById(String sysfileId) {
        SysFile sysFile = sysFileRepo.findById(sysfileId);
        return sysFile;
    }


    /**
     * 根据主业务ID获取附件信息
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
                if(null != sf.getFileSize()){
                    sysFileDto.setFileSizeStr(SysFileUtil.getFileSize(sf.getFileSize()));
                }
                sysFileDtoList.add(sysFileDto);
            });
        }
        return sysFileDtoList;
    }

    /**
     * 根据主业务ID和分类获取附件信息
     * @param mainId
     * @return
     */
    @Override
    public List<SysFileDto> queryFile(String mainId,String sysBusiType) {
        List<SysFile> sysFiles = sysFileRepo.queryFileList(mainId,sysBusiType);
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
     * @param saveFileList
     */
    @Override
    public void bathSave(List<SysFile> saveFileList) {
        sysFileRepo.bathUpdate(saveFileList);
    }

    /**
     * 根据业务ID获取附件信息
     */
    /**
     * 根据业务ID获取相应的附件信息
     * @param businessId
     * @return
     */
    @Override
    public List<SysFileDto> findByBusinessId(String businessId) {
        Criteria criteria = sysFileRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(SysFile_.businessId.getName(),businessId));
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
