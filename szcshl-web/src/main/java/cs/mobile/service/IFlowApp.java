package cs.mobile.service;

import java.util.Map;

/**
 * 流程初始化参数接口
 * Created by ldm on 2017/8/21.
 */
public interface IFlowApp {
    Map<String,Object> getFlowBusinessMap(String businessKey, String taskDefinitionKey,String userid);
}
