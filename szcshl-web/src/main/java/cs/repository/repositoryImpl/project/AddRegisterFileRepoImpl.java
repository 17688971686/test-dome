package cs.repository.repositoryImpl.project;

import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.AddRegisterFile_;
import cs.model.project.AddRegisterFileDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 登记补充资料 数据操作实现类
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
@Repository
public class AddRegisterFileRepoImpl extends AbstractRepository<AddRegisterFile, String> implements AddRegisterFileRepo {

    /**
     * 根据业务ID查询
     * @param businessId
     * @return
     */
    @Override
    public List<AddRegisterFile> findByBusinessId(String businessId) {
        return findByIds(AddRegisterFile_.businessId.getName(),businessId,null);
    }

    /**
     * 通过业务id 和业务类型 进行删除
     * @param businessId
     * @param businessType
     */
    @Override
    public void deleteByBusIdAndBusType(String businessId, String businessType) {

        HqlBuilder hqlBuilder =  HqlBuilder.create();
        hqlBuilder.append("delete from cs_add_registerfile where " + AddRegisterFile_.businessId.getName() + "=:businessId");
        hqlBuilder.setParam("businessId" , businessId);
        if(Validate.isString(businessType)){
            hqlBuilder.append(" and businessType=:businessType").setParam("businessType" , businessType);
        }

       executeSql(hqlBuilder);

    }
}