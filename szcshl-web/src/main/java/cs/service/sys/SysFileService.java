package cs.service.sys;

import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;

/**
 * @author lqs
 * 文件管理服务接口
 * 
 * */
public interface SysFileService {

	/**
	 * 保存文件
	 * @param bytes 文件数据
	 * @param fileName 文件名称
	 * @param businessId 业务ID
	 * @param fileType 文件类型
	 * @param urlGenerator 文件url生成器
	 * */
	public SysFileDto save(byte[] bytes,String fileName,String businessId,String fileType,String module,String proccessInstanceId);

	public void deleteById(String sysFileId);
	
	PageModelDto<SysFileDto> get(ODataObj odataObj);
}
