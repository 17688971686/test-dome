package cs.service.sys;

import java.util.List;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface AnnountmentService {
	
	PageModelDto<AnnountmentDto> findByCurUser(ODataObj odataobj);


	ResultMsg createAnnountment(AnnountmentDto annountmentDto);
	
	String findAnOrg();
	
	AnnountmentDto findAnnountmentById(String anId);
	
	void updateAnnountment(AnnountmentDto annountmentDto);

	/**
	 * 删除通知公告
	 * @param id
	 * @return
	 */
	ResultMsg deleteAnnountment(String id);
	
	List<AnnountmentDto> getHomePageAnnountment();
	
	AnnountmentDto postAritle(String id);
	
	AnnountmentDto nextArticle(String id);

    /**
     * 更改发布状态
     * @param ids
     * @param issueState
     * @return
     */
    ResultMsg updateIssueState(String ids, String issueState);

	PageModelDto<AnnountmentDto> findByIssue(ODataObj odataobj);

	/**
	 * 月报简报发起流程
	 * @param id
	 * @return
	 */
	ResultMsg startFlow(String id);

	/**
	 * 月报简报流程处理
	 * @param processInstance
	 * @param task
	 * @param flowDto
	 * @return
	 */
	ResultMsg dealSignSupperFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

	/**
	 * 结束流程
	 * @param businessKey
	 * @return
	 */
    ResultMsg endFlow(String businessKey);
}
