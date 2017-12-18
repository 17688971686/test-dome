package cs.service.sys;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.Sign;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class SysFileServiceImpl implements SysFileService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private SysFileRepo sysFileRepo;

    @Autowired
    private SignRepo signRepo;

    @Override
    @Transactional
    public ResultMsg save(byte[] bytes, String fileName, String businessId, String fileType,
                          String mainId,String mainType, String sysfileType, String sysBusiType) {
        try {
            String fileUploadPath = SysFileUtil.getUploadPath();
            String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath, mainType,mainId, sysBusiType, fileName);

            SysFile sysFile = new SysFile();
            sysFile.setSysFileId(UUID.randomUUID().toString());
            sysFile.setBusinessId(businessId);
            sysFile.setFileSize(bytes.length);
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
            stream.write(bytes);
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
    public ResultMsg saveToFtp(byte[] bytes, String fileName, String businessId, String fileType, String mainId, String mainType, String sysfileType, String sysBusiType, String ftpIp, String port, String ftpUser, String ftpPwd, String ftpBasePath, String ftpFilePath) {
        try {
            String fileUploadPath = SysFileUtil.getUploadPath();
            String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath, mainType,mainId, sysBusiType, fileName);

            SysFile sysFile = new SysFile();
            sysFile.setSysFileId(UUID.randomUUID().toString());
            sysFile.setBusinessId(businessId);
            sysFile.setFileSize(bytes.length);
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
            sysFile.setFtpIp(ftpIp);
            sysFile.setPort(port);
            sysFile.setFtpUser(ftpUser);
            sysFile.setFtpPwd(ftpPwd);
            sysFile.setFtpBasePath(ftpBasePath);
            sysFile.setFtpFilePath(ftpFilePath);

            sysFileRepo.save(sysFile);

            //先保存成功，
         /*   BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileUploadPath + File.separator + relativeFileUrl));
            stream.write(bytes);
            stream.close();*/
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
    public void deleteById(String sysFileId) {
        List<SysFile> fileList = sysFileRepo.findByIds(SysFile_.sysFileId.getName(), sysFileId, null);
        if (fileList != null && fileList.size() > 0) {
            String path = SysFileUtil.getUploadPath();
            fileList.forEach(f -> {
                if(SessionUtil.getLoginName().equals(f.getCreatedBy())){
                    sysFileRepo.delete(f);
                   // SysFileUtil.deleteFile(path + f.getFileUrl());
                    FtpUtil.removeFile(f.getFtpIp(),f.getPort()!=null?Integer.parseInt(f.getPort()):0,f.getFtpUser(),f.getFtpPwd(),f.getFtpBasePath(),f.getShowName(),"");
                }else{
                    throw new IllegalArgumentException("您没有权限删除该文件！");
                }
            });
        }
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
     * 根据ID获取附件信息
     * @param sysfileId
     * @return
     */
    public SysFile findFileByIdGet(String sysfileId) {
        SysFile sysFile = sysFileRepo.findByIdGet(sysfileId);
        return sysFile;
    }

    /**
     * 根据主业务ID获取附件信息
     * @param mainId
     * @return
     */
    @Override
    public List<SysFileDto> findByMainId(String mainId) {
        Criteria criteria = sysFileRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(SysFile_.mainId.getName(),mainId));
        List<SysFile> sysFiles = criteria.list();
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
                sysFileDtoList.add(sysFileDto);
            });
        }
        return sysFileDtoList;
    }

}
