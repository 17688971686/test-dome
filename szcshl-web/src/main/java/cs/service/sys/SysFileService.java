package cs.service.sys;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.sys.Ftp;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    /**
     * 删除附件
     * @param sysFileId
     * @return
     */
    ResultMsg deleteById(String sysFileId);

    PageModelDto<SysFileDto> get(ODataObj odataObj);

    SysFile findFileById(String sysfileId);

    List<SysFileDto> findByBusinessId(String businessId);

    List<SysFileDto> findByMainId(String mainId);

    List<SysFileDto> queryFile(String mainId,String sysBusiType);

    /**
     * 批量保存
     * @param saveFileList
     */
    void bathSave(List<SysFile> saveFileList);

    /**
     * 获取默认的ftpId
     * @return
     */
    String findFtpId();

    /**
     * 获取文件服务根目录
     * @return
     */
    String getFtpRoot(String relativeUrl);


    /**
     * 通过业务ID和业务类型删除对应的文件
     * @param businessId
     * @param businessType
     */
    void deleteByBusinessIdAndBusinessType(String businessId , String businessType);

    /**
     * 保存远程连接的文件
     * @param businessId
     * @param sysFileDtoList
     */
    ResultMsg downRemoteFile(String businessId,List<SysFileDto> sysFileDtoList,String userId,String mainType,String busiType) throws MissingServletRequestParameterException;
}
