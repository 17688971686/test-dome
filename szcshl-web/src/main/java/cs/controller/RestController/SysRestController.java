package cs.controller.RestController;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.ahelper.IgnoreAnnotation;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.flow.FlowDto;
import cs.model.project.SignDto;
import cs.model.topic.TopicInfoDto;
import cs.service.flow.FlowService;
import cs.service.project.SignService;
import cs.service.topic.TopicInfoService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统接口controller
 * Created by ldm on 2017/8/25.
 */
@RestController
@RequestMapping(name = "系统接口", path = "intfc")
@IgnoreAnnotation
public class SysRestController {

    @Autowired
    private SignService signService;
    @Autowired
    private TopicInfoService topicInfoService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private HttpClientOperate httpClientOperate;
    /**
     * 项目签收信息
     * @param signDtoJson
     * @return
     */
    @RequestMapping(name="项目签收信息",value = "/pushProject", method = RequestMethod.POST)
    public ResultMsg pushProject(@RequestParam String signDtoJson) {
        //json转出对象
        SignDto signDto = JSON.parseObject(signDtoJson,SignDto.class);
        return signService.pushProject(signDto);
    }


    /**
     * 课题研究审核
     * @param resultMsg
     * @return
     */
    @RequestMapping(name="课题研究审核",value = "/auditTopicResult", method = RequestMethod.POST)
    public ResultMsg auditTopicResult(@RequestBody ResultMsg resultMsg) {
        TopicInfoDto topicInfoDto = (TopicInfoDto) resultMsg.getReObj();
        if(topicInfoDto == null){
            return new ResultMsg(false, Constant.MsgCode.OBJ_NULL.getValue(), "获取课题信息！");
        }
        if(!Validate.isString(topicInfoDto.getProcessInstanceId())){
            return new ResultMsg(false, Constant.MsgCode.MAIN_VALUE_NULL.getValue(), "获取不到流程实例数据！");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(topicInfoDto.getProcessInstanceId()).singleResult();
        Task task = null;
        if (Validate.isString(topicInfoDto.getTaskId())) {
            task = taskService.createTaskQuery().taskId(topicInfoDto.getTaskId()).active().singleResult();
        } else {
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        }
        if (task == null) {
            return new ResultMsg(false, Constant.MsgCode.MAIN_VALUE_NULL.getValue(), "获取不到任务ID！");
        }
        ResultMsg resultObj = null;
        //方案通过，进行下一步处理
        if(resultMsg.isFlag() || "01".equals(resultMsg.getReCode())){
            FlowDto flowDto = new FlowDto();
            flowDto.setDealOption(resultMsg.getReMsg());
            return topicInfoService.dealFlow(processInstance, task,flowDto);
        }else{
            switch (resultMsg.getReCode()){
                case "02":              //方案修改,流程回退
                    FlowDto flowDto = new FlowDto();
                    flowDto.setTaskId(task.getId());
                    flowDto.setDealOption(resultMsg.getReMsg());
                    resultObj = flowService.rollBackLastNode(flowDto);
                    break;
                case "03":              //方案不通过，直接终止
                    topicInfoDto.setState(Constant.EnumState.FORCE.getValue());
                    topicInfoService.save(topicInfoDto);
                    runtimeService.deleteProcessInstance(processInstance.getId(), resultMsg.getReMsg());
                    resultObj = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
                    break;
            }
        }
        return resultObj;
    }


    @RequestMapping(name="项目签收信息",value = "/testJson")
    public void testJson() throws IOException {
        String REST_SERVICE_URI = "http://localhost:8080/szcshl-web/intfc/";
        SignDto signDto = new SignDto();
        signDto.setSignid("122");
        signDto.setCreatedBy("系统管理员");
        Map<String, String> params = new HashMap<>();
        params.put("signDtoJson",JSON.toJSONString(signDto));
        HttpResult hst = httpClientOperate.doPost(REST_SERVICE_URI+"/pushProject", params);
        System.out.println(hst.toString());
    }
}
