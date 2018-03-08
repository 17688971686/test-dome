package cs.repository.repositoryImpl.sys;

import cs.common.ResultMsg;
import cs.domain.sys.SysFile;
import cs.repository.IRepository;
import java.util.List;

public interface SysFileRepo extends IRepository<SysFile, String> {

	List<SysFile> queryFileList(String mainId,String sysBusiType);

	/**
	 * 根据主业务ID获取附件信息
	 * @param mainId
	 * @return
	 */
    List<SysFile> findByMainId(String mainId);

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
	 * @param FileUrl
	 * @param fileName
	 * @return
	 */
	SysFile isExistFile(String fileUrl,String fileName);
}
