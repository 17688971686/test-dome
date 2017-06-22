package cs.service.sys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.common.utils.BeanCopierUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.SysFileUtil;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;

@Service
public class SysFileServiceImpl implements SysFileService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private SysFileRepo sysFileRepo;

    @Autowired
    private SignRepo signRepo;

    @Autowired
    private WorkProgramRepo workProgramRepo;

    @Autowired
    private ICurrentUser currentUser;

    @Override
    @Transactional
    public SysFileDto save(byte[] bytes, String fileName, String businessId, String fileType, String sysSignId, String sysfileType, String sysMinType) {
        SysFileDto sysFileDto = new SysFileDto();
        SysFile sysFile = new SysFile();
        logger.debug("Begin save file " + fileName + "-" + businessId);
        try {
            String fileUploadPath = SysFileUtil.getUploadPath();
            String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath, sysSignId, businessId, fileName);

            sysFile.setSysFileId(UUID.randomUUID().toString());
            sysFile.setBusinessId(businessId);
            sysFile.setFileSize(bytes.length);
            sysFile.setShowName(fileName);
            sysFile.setFileType(fileType);
            sysFile.setFileUrl(relativeFileUrl);
            sysFile.setSysSingId(sysSignId);
            sysFile.setSysfileType(sysfileType);
            sysFile.setSysMinType(sysMinType);

            Date now = new Date();
            sysFile.setCreatedBy(currentUser.getLoginName());
            sysFile.setModifiedBy(currentUser.getLoginName());
            sysFile.setCreatedDate(now);
            sysFile.setModifiedDate(now);

            sysFileRepo.save(sysFile);

            //先保存成功，再传输文件
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileUploadPath + File.separator + relativeFileUrl));
            stream.write(bytes);
            stream.close();
            BeanUtils.copyProperties(sysFile, sysFileDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysFileDto;
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
                sysFileRepo.delete(f);
                SysFileUtil.deleteFile(path + f.getFileUrl());
            });
        }
    }

    @Override
    public SysFile findFileById(String sysfileId) {
        SysFile sysFile = sysFileRepo.findById(sysfileId);
        return sysFile;
    }

    @Override
    public List<SysFileDto> findBySysFileSignId(String signid) {
        HqlBuilder hql = HqlBuilder.create();
        hql.append(" from " + SysFile.class.getSimpleName() + " where " + SysFile_.sysSingId.getName());
        hql.append("=:sysSingId").setParam("sysSingId", signid);
        List<SysFile> sysFiles = sysFileRepo.findByHql(hql);
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

    @Override
    public List<SysFileDto> findByBusinessId(String businessId) {
        HqlBuilder hql = HqlBuilder.create();
        hql.append(" from " + SysFile.class.getSimpleName() + " where " + SysFile_.businessId.getName());
        hql.append("=:businessId").setParam("businessId", businessId);
        List<SysFile> sysFiles = sysFileRepo.findByHql(hql);
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

    @Override
    public Map<String, Object> initFileUploadlist(String signid) {

        Map<String, Object> map = new HashMap<String, Object>();
        Sign sign = signRepo.findById(signid);

        HqlBuilder hql = HqlBuilder.create();
        hql.append(" from " + SysFile.class.getSimpleName() + " where " + SysFile_.sysSingId.getName()).append("=:sysSingId").setParam("sysSingId", sign.getSignid());
        List<SysFile> sysFiles = sysFileRepo.findByHql(hql);
        List<SysFileDto> sysFileDtoList = new ArrayList<SysFileDto>();
        for (SysFile item : sysFiles) {
            SysFileDto sysFileDto = new SysFileDto();
            sysFileDto.setSysFileId(item.getSysFileId());
            sysFileDto.setBusinessId(item.getBusinessId());
            sysFileDto.setFileUrl(item.getFileUrl());
            sysFileDto.setShowName(item.getShowName());
            sysFileDto.setFileType(item.getFileType());
            sysFileDto.setSysfileType(item.getSysfileType());
            sysFileDto.setCreatedDate(item.getCreatedDate());
            sysFileDto.setCreatedBy(currentUser.getLoginName());
            sysFileDto.setModifiedBy(currentUser.getLoginName());
            sysFileDtoList.add(sysFileDto);
        }

        //收文
        if (sysFileDtoList != null) {
            map.put("sysFileDtoList", sysFileDtoList);
        }

        return map;
    }

}
