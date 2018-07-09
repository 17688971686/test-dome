package cs.mobile.service;

import cs.common.ResultMsg;
import cs.domain.sys.User;
import cs.model.flow.FlowDto;
import cs.model.sys.UserDto;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Created by Administrator on 2018/2/28.
 */
public interface FlowAppService {

    /**
     * 项目流程处理
     * @param processInstance
     * @param task
     * @param flowDto
     * @param userDto
     * @return
     */
    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto) throws Exception;

    /**
     * 图书流程处理
     */
    ResultMsg bookDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);

    /**
     * 月报流程处理
     */
    ResultMsg monthlyDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);

    /**
     * 通知公告流程处理
     */
    ResultMsg annountDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);

    /**
     * 拟补充资料函流程处理
     */
    ResultMsg suppletterDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);



}
