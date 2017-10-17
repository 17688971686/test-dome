package cs.repository.repositoryImpl.expert;

import cs.domain.expert.ExpertSelCondition;
import cs.repository.IRepository;


/**
 * Description: 专家抽取条件 数据操作实现接口
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
public interface ExpertSelConditionRepo extends IRepository<ExpertSelCondition, String> {

    void updateSelectIndexById(String ids);

    /**
     * 统计专家抽取设定人数
     * @param minBusinessId
     * @return
     */
    int getExtractEPCount(String minBusinessId);
}
