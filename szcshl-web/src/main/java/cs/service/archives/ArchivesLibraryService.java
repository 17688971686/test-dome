package cs.service.archives;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.archives.ArchivesLibraryDto;
import cs.model.flow.FlowDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Description: 档案借阅管理 业务操作接口
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
public interface ArchivesLibraryService {
    
    PageModelDto<ArchivesLibraryDto> get(ODataObj odataObj);

	ResultMsg save(ArchivesLibraryDto record);

	void update(ArchivesLibraryDto record);

	ArchivesLibraryDto findById(String deptId);

	void delete(String id);

    /**
     * 发起流程
     * @param id
     * @return
     */
    ResultMsg startFlow(String id);
	/**
	 * 流程处理
	 * @param processInstance
	 * @param task
	 * @param flowDto
	 * @return
	 */
	ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

}
