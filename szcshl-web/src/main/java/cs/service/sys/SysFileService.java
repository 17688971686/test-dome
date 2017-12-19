package cs.service.sys;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.common.utils.Ftp;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lqs
 *         文件管理服务接口
 */
public interface SysFileService {

    ResultMsg save(MultipartFile multipartFile, String fileName, String businessId, String fileType,
                   String mainId, String mainType, String sysfileType, String sysBusiType);

    public void update(SysFile sysFile);

    ResultMsg saveToFtp(long size, String fileName, String businessId, String fileType,String relativeFileUrl,
                        String mainId, String mainType, String sysfileType, String sysBusiType, Ftp ftp);

    ResultMsg deleteById(String sysFileId);

    PageModelDto<SysFileDto> get(ODataObj odataObj);

    SysFile findFileById(String sysfileId);

    SysFile findFileByIdGet(String sysfileId);

    List<SysFileDto> findByBusinessId(String businessId);

    List<SysFileDto> findByMainId(String mainId);

    List<SysFileDto> queryFile(String mainId,String sysBusiType);
}
