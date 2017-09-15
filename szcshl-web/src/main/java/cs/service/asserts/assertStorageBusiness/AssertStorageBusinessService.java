package cs.service.asserts.assertStorageBusiness;

import cs.common.ResultMsg;
import cs.domain.asserts.assertStorageBusiness.AssertStorageBusiness;
import cs.model.PageModelDto;
import cs.model.asserts.assertStorageBusiness.AssertStorageBusinessDto;
import cs.model.asserts.goodsDetail.GoodsDetailDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Description: 固定资产申购流程 业务操作接口
 * author: zsl
 * Date: 2017-9-15 14:45:23
 */
public interface AssertStorageBusinessService {
    
    PageModelDto<AssertStorageBusinessDto> get(ODataObj odataObj);

	void save(AssertStorageBusinessDto record);

	void update(AssertStorageBusinessDto record);

	AssertStorageBusinessDto findById(String deptId);

	void delete(String id);

	ResultMsg saveGoodsDetailList(GoodsDetailDto[] goodsDetailDtoList, AssertStorageBusiness assertStorageBus);

	ResultMsg startFlow(GoodsDetailDto[] goodsDetailDtoList,AssertStorageBusiness assertStorageBus);

	//流程处理begin
	ResultMsg startNewFlow(String signid);

	ResultMsg stopFlow(String signid, ProjectStopDto projectStopDto);

	ResultMsg restartFlow(String signid);

	ResultMsg endFlow(String signid);

	ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);
	//流程处理end

}
