package cs.service.project;

import cs.domain.project.AgentTask;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by ldm on 2018/5/21.
 */
public interface AgentTaskService {
    /**
     * 初始化代理对象，没有任务ID
     * @param user
     * @return
     */
    AgentTask initNew(User user,String nodeKey);

    /**
     * 更新并保存代办人信息
     * @param agentTaskList
     * @param processInstandId
     */
    void updateAgentInfo(List<AgentTask> agentTaskList, String processInstandId,String processName);

    /**
     * 分页查询
     * @param odataObj
     * @return
     */
    PageModelDto<AgentTask> get(ODataObj odataObj);
}
