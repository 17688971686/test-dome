package cs.repository.repositoryImpl.project;

import cs.domain.project.AssistPlan;
import cs.model.project.SignAssistCostDto;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 协审方案 数据操作实现接口
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
public interface AssistPlanRepo extends IRepository<AssistPlan, String> {
    /**
     * 项目协审费用查询
     * @param signAssistCostDto
     * @return
     */
    List<Object[]> signAssistCostList(SignAssistCostDto signAssistCostDto,String planState);

    /**
     * 根据业务ID，更新协审计划状态
     * @param businessId
     * @param value
     */
    void updatePlanStateByBusinessId(String businessId, String value);
}
