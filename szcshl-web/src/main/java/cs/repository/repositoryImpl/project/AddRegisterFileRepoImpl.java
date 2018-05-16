package cs.repository.repositoryImpl.project;

import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.AddRegisterFile_;
import cs.model.project.AddRegisterFileDto;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
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
        if(null != businessType ){
            hqlBuilder.append(" and businessType=:businessType").setParam("businessType" , businessType);
        }

        executeSql(hqlBuilder);

    }

    /**
     * 通过业务ID和业务类型查询
     * @param businessId
     * @param businessType
     * @return
     */
    @Override
    public List<AddRegisterFile> findByBusIdAndBusType(String businessId, Integer businessType) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " +AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.businessId.getName() + "=:businessId");
        hqlBuilder.append(" and " + AddRegisterFile_.businessType.getName() + "=:businessType");
        hqlBuilder.setParam("businessId" , businessId);
        hqlBuilder.setParam("businessType" , businessType.toString());
        List<AddRegisterFile> addRegisterFileList = findByHql(hqlBuilder);
        return addRegisterFileList;
    }

    /**
     * 通过业务ID过滤不为图纸和拟补充的材料(补充：并且通过类型对报审和归档的其他资料进行区别)
     * @param businessId
     * @param type
     * @return
     */
    @Override
    public List<AddRegisterFile> findByBusIdNoAndBusType(String businessId , String type) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " +AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.businessId.getName() + "=:businessId");
//        hqlBuilder.append(" and " + AddRegisterFile_.businessType.getName() + "!=3");
//        hqlBuilder.append(" and " + AddRegisterFile_.businessType.getName() + "!=2");
        //报审阶段
        if(Validate.isString(type) && "SIGN".equals(type)){
            hqlBuilder.append(" and " + AddRegisterFile_.businessType.getName() + " in( 1,4)");

        }
        //归档阶段
        else if(Validate.isString(type) && "FILERECORD".equals(type)){
            hqlBuilder.append(" and " + AddRegisterFile_.businessType.getName() + " in(5,6,7)");
        }
        hqlBuilder.setParam("businessId" , businessId);
        List<AddRegisterFile> addRegisterFileList = findByHql(hqlBuilder);
        return addRegisterFileList;
    }


    /**
     * 通过id和类型进行查询
     * @param ids
     * @param type
     * @return
     */
    @Override
    public List<AddRegisterFile> findByIdAndBusType(String ids, Integer type) {
        if(Validate.isString(ids)){
            String[] idArr = ids.split(",");
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append("select * from cs_add_registerfile where " + AddRegisterFile_.id.getName() + " in(" );
            for(int i =0 ; i < idArr.length ; i++){
                hqlBuilder.append(" '" + idArr[i] + "'");
                if(i <idArr.length - 1){
                    hqlBuilder.append(",");
                }
            }
            hqlBuilder.append(" ) and " + AddRegisterFile_.businessType.getName() + "=:businessType");
            hqlBuilder.setParam("businessType" , type.toString());
            List<AddRegisterFile> addRegisterFileList = findBySql(hqlBuilder);
            return addRegisterFileList;
        }else{

            return null;
        }
    }
}