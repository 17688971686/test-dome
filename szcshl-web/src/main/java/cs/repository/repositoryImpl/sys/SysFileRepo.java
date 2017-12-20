package cs.repository.repositoryImpl.sys;

import java.util.List;

import cs.domain.sys.SysFile;
import cs.repository.IRepository;

public interface SysFileRepo extends IRepository<SysFile, String> {

	List<SysFile> queryFileList(String mainId,String sysBusiType);

	/**
	 * 根据主业务ID获取附件信息
	 * @param mainId
	 * @return
	 */
    List<SysFile> findByMainId(String mainId);
}
