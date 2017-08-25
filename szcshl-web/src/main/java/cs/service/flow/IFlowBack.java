package cs.service.flow;

/**
 * 流程回退
 * Created by ldm on 2017/8/21.
 */
public interface IFlowBack {

    String backActivitiId(String businessKey,String curActivitiId);
}
