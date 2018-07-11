package com.sn.framework.module.sys.service.impl;

import com.google.common.collect.Lists;
import com.sn.framework.core.common.ResultMsg;
import com.sn.framework.core.common.Validate;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.core.util.BeanCopierUtils;
import com.sn.framework.core.util.SysFileUtil;
import com.sn.framework.module.sys.domain.Ftp;
import com.sn.framework.module.sys.domain.SysFile;
import com.sn.framework.module.sys.helper.SysFileHelper;
import com.sn.framework.module.sys.model.SysFileDto;
import com.sn.framework.module.sys.repo.ISysFileRepo;
import com.sn.framework.module.sys.service.ISysFileService;

import cs.repository.repositoryImpl.sys.SysFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by ldm on 2018/7/11 0011.
 */
@Service
public class SysFileServiceImpl extends SServiceImpl<ISysFileRepo, SysFile, SysFileDto> implements ISysFileService {

    @Autowired
    private ISysFileRepo sysFileRepo;

    @Override
    public List<SysFileDto> findByBusinessId(String businessId) {
        List<SysFile> sysFiles = sysFileRepo.findByBusinessId(businessId);
        return SysFileHelper.create(sysFiles).listTransToDto();
    }

    @Override
    public List<SysFileDto> findByMainId(String mainId) {
        List<SysFile> sysFiles = sysFileRepo.findByMainId(mainId);
        return SysFileHelper.create(sysFiles).listTransToDto();
    }

    @Override
    public ResultMsg saveToFtp(long size, String fileName, String businessId, String fileType, String relativeFileUrl, String mainId, String mainType, String sysfileType, String sysBusiType, Ftp ftp) {
        return null;
    }

    @Override
    public ResultMsg downRemoteFile(String businessId, List<cs.model.sys.SysFileDto> sysFileDtoList, String userId, String mainType, String busiType) {
        return null;
    }

    @Override
    public void save(SysFile sysFile) {
        sysFileRepo.save(sysFile);
    }

    @Override
    public SysFile isExistFile(String relativeFileUrl, String fileName) {
        return sysFileRepo.isExistFile(relativeFileUrl,fileName);
    }
}
