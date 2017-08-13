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
        sqlBuilder.bulidIdString("where",ExpertSelCondition_.id.getName(),ids);

        executeSql(sqlBuilder);
    }
}