package cs.service.topic;

import cs.common.ResultMsg;
import cs.domain.topic.TopicInfo;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.topic.TopicInfoDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Description: 课题研究 业务操作接口
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
public interface TopicInfoService {
    
    PageModelDto<TopicInfoDto> get(ODataObj odataObj);

    ResultMsg save(TopicInfoDto record);

	ResultMsg startFlow(TopicInfoDto record);

	void update(TopicInfoDto record);

	TopicInfoDto findById(String deptId);

	ResultMsg delete(String id);

    TopicInfoDto findDetailById(String id);

    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

}
