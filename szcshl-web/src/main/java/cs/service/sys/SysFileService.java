package cs.service.sys;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;

/**
 * @author lqs
 *         文件管理服务接口
 */
public interface SysFileService {

    ResultMsg save(byte[] bytes, String fileName, String businessId, String fileType,
                   String mainId,String mainType, String sysfileType, String sysBusiType);

    void deleteById(String sysFileId);

    PageModelDto<SysFileDto> get(ODataObj odataObj);

    SysFile findFileById(String sysfileId);

    List<SysFileDto> findByBusinessId(String businessId);

    List<SysFileDto> findByMainId(String mainId);
}
