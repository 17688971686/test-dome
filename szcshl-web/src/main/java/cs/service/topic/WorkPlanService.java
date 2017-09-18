package cs.service.topic;

import cs.common.ResultMsg;
import cs.domain.topic.WorkPlan;
import cs.model.PageModelDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;

/**
 * Description:  业务操作接口
 * author: ldm
 * Date: 2017-9-4 15:33:03
 */
public interface WorkPlanService {

    PageModelDto<WorkPlanDto> get(ODataObj odataObj);

    ResultMsg save(WorkPlanDto record);

    void update(WorkPlanDto record);

    WorkPlanDto findById(String deptId);

    void delete(String id);

    WorkPlanDto initByTopicId(String topicId);

    WorkPlanDto findLinkBusiness(WorkPlan workPlan,WorkPlanDto workPlanDto);
}
