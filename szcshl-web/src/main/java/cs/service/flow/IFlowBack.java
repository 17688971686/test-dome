package cs.service.flow;

import java.util.Map;

/**
 * 流程回退
 * Created by ldm on 2017/8/21.
 */
public interface IFlowBack {

    Map<String,Object> backActivitiId(String businessKey, String curActivitiId);
}
