package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.AddRegisterFile;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 登记补充资料 业务操作接口
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
public interface AddRegisterFileService {

    PageModelDto<AddRegisterFileDto> get(ODataObj odataObj);

    void save(AddRegisterFileDto addRegisterFileDtos);

    AddRegisterFileDto findById(String deptId);

    void deleteRegisterFile(String id);

    Map<String, Object> initprint(String signid);

    List<AddRegisterFileDto> findbySuppdate(String suppDate);

    List<AddRegisterFileDto> findByBusinessId(String businessId);

    List<AddRegisterFileDto> initRegisterFileData(ODataObj odataObj);

    ResultMsg bathSave(AddRegisterFileDto[] addRegisterFileDtos);

    /**
     * 通过业务id 和 业务类型查询
     * @param businessId
     * @param businessType
     * @return
     */
    List<AddRegisterFile> findByBusIdAndBusType(String businessId , String businessType);
}
