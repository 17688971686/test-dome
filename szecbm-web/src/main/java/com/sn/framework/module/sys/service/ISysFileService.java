package com.sn.framework.module.sys.service;

import com.sn.framework.core.common.ResultMsg;
import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.domain.Ftp;
import com.sn.framework.module.sys.domain.SysFile;
import com.sn.framework.module.sys.model.SysFileDto;

import java.util.List;

/**
 * Created by ldm on 2018/7/11 0011.
 */
public interface ISysFileService extends ISService<SysFileDto> {

    /**
     * 根据业务ID查询
     * @param businessId
     * @return
     */
    List<SysFileDto> findByBusinessId(String businessId);

    /**
     * 根据主业务ID查询附件
     * @param mainId
     * @return
     */
    List<SysFileDto> findByMainId(String mainId);

    /**
     * 保存附件到ftp
     * @param size
     * @param fileName
     * @param businessId
     * @param fileType
     * @param relativeFileUrl
     * @param mainId
     * @param mainType
     * @param sysfileType
     * @param sysBusiType
     * @param ftp
     * @return
     */
    ResultMsg saveToFtp(long size, String fileName, String businessId, String fileType, String relativeFileUrl,
                        String mainId, String mainType, String sysfileType, String sysBusiType, Ftp ftp);

    /**
     * 保存远程连接的文件
     * @param businessId
     * @param sysFileDtoList
     * @param userId
     * @param mainType
     * @param busiType
     * @return
     */
    ResultMsg downRemoteFile(String businessId, List<SysFileDto> sysFileDtoList, String userId, String mainType, String busiType);

    /**
     * 保存
     * @param sysFile
     */
    void save(SysFile sysFile);

    /**
     * 校验文件是否已经存在
     * @param relativeFileUrl
     * @param fileName
     * @return
     */
    SysFile isExistFile(String relativeFileUrl, String fileName);
}
