package cs.service.sys;

import java.util.List;
import java.util.Map;

import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;

/**
 * @author lqs
 *         文件管理服务接口
 */
public interface SysFileService {

    SysFileDto save(byte[] bytes, String fileName, String businessId, String fileType,
                    String sysSignId, String sysfileType, String sysMinType);

    void deleteById(String sysFileId);

    PageModelDto<SysFileDto> get(ODataObj odataObj);

    SysFile findFileById(String sysfileId);

    List<SysFileDto> findByBusinessId(String businessId);

    List<SysFileDto> findBySysFileSignId(String signid);

    Map<String, Object> initFileUploadlist(String signid);

    List<SysFile> sysFileByIds(String signid);
}
