package cs.service.topic;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.topic.ContractDto;
import cs.model.topic.TopicInfoDto;
import cs.model.topic.TopicMaintainDto;
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

    ResultMsg updateTopic(TopicInfoDto record);

    TopicInfoDto findById(String deptId);

    ResultMsg delete(String id);

    ResultMsg deleteContract(String ids);

    ResultMsg deleteTopicMaintain(String ids);

    TopicInfoDto findDetailById(String id);

    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

    /**
     * 处理发改委审核之后的方法
     * @param resultMsg
     * @param topicInfoDto
     * @return
     */
    ResultMsg dealReturnAudit(ResultMsg resultMsg,TopicInfoDto topicInfoDto);

    /***
     * 保存合同信息
     * @param contractDtoList
     * @return
     */
    ResultMsg saveContractDetailList(ContractDto[] contractDtoList);

    /**
     * 保存课题信息
     * @param topicMaintainDtoArray
     * @return
     */
    ResultMsg saveTopicDetailList(TopicMaintainDto[] topicMaintainDtoArray);
}
