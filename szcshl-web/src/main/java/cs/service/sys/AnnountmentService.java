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
	
	
	void createAnnountment(AnnountmentDto annountmentDto);
	
	String findAnOrg();
	
	AnnountmentDto findAnnountmentById(String anId);
	
	void updateAnnountment(AnnountmentDto annountmentDto);
	
	
	void deleteAnnountment(String id);
	
	List<AnnountmentDto> getHomePageAnnountment();
	
	AnnountmentDto postAritle(String id);
	
	AnnountmentDto nextArticle(String id);

	void updateIssueState(String ids, String issueState);

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
}
