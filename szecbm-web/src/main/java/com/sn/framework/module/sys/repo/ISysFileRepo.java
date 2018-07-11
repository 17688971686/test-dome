package com.sn.framework.module.sys.repo;

import com.sn.framework.core.common.ResultMsg;
import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.SysFile;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11 0011.
 */
public interface ISysFileRepo extends IRepository<SysFile,String> {

    List<SysFile> queryFileList(String mainId, String sysBusiType);

    /**
     * 根据主业务ID获取附件信息
     * @param mainId
     * @return
     */
    List<SysFile> findByMainId(String mainId);

    /**
     * 根据业务ID获取附件信息
     * @param businessId
     * @return
     */
    List<SysFile> findByBusinessId(String businessId);
    /**
     * 删除附件记录信息（4个参数都不能为空，以免删除数据）
     * @param mainId
     * @param businessId
     * @param sysBusiType
     * @param showName
     */
    void delete(String mainId, String businessId, String sysBusiType, String showName);

    /**
     * 根据ID删除附件信息
     * @param sysFileId
     * @return
     */
    ResultMsg deleteByFileId(String sysFileId);


    /**
     * 是否存在同名文件
     * @param fileUrl
     * @param fileName
     * @return
     */
    SysFile isExistFile(String fileUrl,String fileName);
}
