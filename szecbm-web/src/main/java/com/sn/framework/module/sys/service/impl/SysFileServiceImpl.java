package com.sn.framework.module.sys.service.impl;

import com.sn.framework.core.common.ResultMsg;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.core.util.BeanCopierUtils;
import com.sn.framework.module.sys.domain.Ftp;
import com.sn.framework.module.sys.domain.SysFile;
import com.sn.framework.module.sys.helper.SysFileHelper;
import com.sn.framework.module.sys.model.SysFileDto;
import com.sn.framework.module.sys.repo.ISysFileRepo;
import com.sn.framework.module.sys.service.ISysFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author zsl
 * @date 2018/7/11 0011
 */
@Service
public class SysFileServiceImpl extends SServiceImpl<ISysFileRepo, SysFile, SysFileDto> implements ISysFileService {


    @Override
    public List<SysFileDto> findByBusinessId(String businessId) {
        List<SysFile> sysFiles = baseRepo.findByBusinessId(businessId);
        return SysFileHelper.create(sysFiles).listTransToDto();
}

    @Override
    public List<SysFileDto> findByMainId(String mainId) {
        List<SysFile> sysFiles = baseRepo.findByMainId(mainId);
        return SysFileHelper.create(sysFiles).listTransToDto();
    }

    @Override
    @Transactional
    public ResultMsg saveToFtp(long size, String fileName, String businessId, String fileType, String relativeFileUrl, String mainId, String mainType, String sysfileType, String sysBusiType, Ftp ftp) {
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
            sysFile.setCreatedBy(SessionUtil.getDisplayName());
            sysFile.setModifiedBy(SessionUtil.getDisplayName());
            sysFile.setCreatedDate(now);
            sysFile.setModifiedDate(now);
            sysFile.setFtp(ftp);
            baseRepo.save(sysFile);

            //先保存成功，
            SysFileDto sysFileDto = new SysFileDto();
            BeanCopierUtils.copyProperties(sysFile,sysFileDto);
            return new ResultMsg(true, "ok","文件上传成功！",sysFileDto);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResultMsg downRemoteFile(String businessId, List<SysFileDto> sysFileDtoList, String userId, String mainType, String busiType) {
        return null;
    }

    @Override
    public void save(SysFile sysFile) {
        baseRepo.save(sysFile);
    }

    @Override
    public SysFile isExistFile(String relativeFileUrl, String fileName) {
        return baseRepo.isExistFile(relativeFileUrl,fileName);
    }

    @Override
    @Transactional
    public ResultMsg deleteByFileId(String sysFileId) {
      return   baseRepo.deleteByFileId(sysFileId);
    }


}
