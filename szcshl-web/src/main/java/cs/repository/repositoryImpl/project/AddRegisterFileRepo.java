package cs.repository.repositoryImpl.project;

import cs.domain.project.AddRegisterFile;
import cs.model.project.AddRegisterFileDto;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 登记补充资料 数据操作实现接口
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
public interface AddRegisterFileRepo extends IRepository<AddRegisterFile, String> {
    List<AddRegisterFile> findByBusinessId(String businessId);

    /**
     * 通过业务id 和业务类型 进行删除
     * @param businessId
     * @param businessType
     */
    void deleteByBusIdAndBusType(String businessId , String businessType);

    /**
     * 通过业务ID和业务类型查询
     * @param businessId
     * @param businessType
     * @return
     */
    List<AddRegisterFile> findByBusIdAndBusType(String businessId , Integer businessType);

    /**
     * 通过业务ID过滤不为图纸和拟补充的材料(补充：并且通过类型对报审和归档的其他资料进行区别)
     * @param businessId
     * @param type
     * @return
     */
    List<AddRegisterFile> findByBusIdNoAndBusType(String businessId , String type);

}
