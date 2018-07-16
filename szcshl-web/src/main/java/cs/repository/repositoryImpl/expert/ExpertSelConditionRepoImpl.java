package cs.repository.repositoryImpl.expert;

import cs.common.HqlBuilder;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelCondition_;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 专家抽取条件 数据操作实现类
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
@Repository
public class ExpertSelConditionRepoImpl extends AbstractRepository<ExpertSelCondition, String> implements ExpertSelConditionRepo {

    /**
     * 更新抽取次数
     * @param ids
     */
    @Override
    public void updateSelectIndexById(String ids) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("update cs_expert_condition set "+ ExpertSelCondition_.selectIndex.getName()+" = (");
        sqlBuilder.append(ExpertSelCondition_.selectIndex.getName()+"+1) ");
        sqlBuilder.bulidPropotyString("where",ExpertSelCondition_.id.getName(),ids);
        executeSql(sqlBuilder);
    }

    /**
     * 统计专家抽取设定人数
     * @param businessId
     * @return
     */
    @Override
    public int getExtractEPCount(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select sum(officialNum) from cs_expert_condition where "+ ExpertSelCondition_.businessId.getName()+"=:businessId ");
        sqlBuilder.setParam("businessId",businessId);
        return returnIntBySql(sqlBuilder);
    }

    @Override
    public void deleteByBusinessId(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from CS_EXPERT_CONDITION where BUSINESSID =:businessId ");
        sqlBuilder.setParam("businessId", businessId);
        executeSql(sqlBuilder);
    }
}