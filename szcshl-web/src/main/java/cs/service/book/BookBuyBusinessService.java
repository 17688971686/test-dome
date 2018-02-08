package cs.service.book;

import cs.common.ResultMsg;
import cs.domain.book.BookBuyBusiness;
import cs.model.PageModelDto;
import cs.model.book.BookBuyBusinessDto;
import cs.model.book.BookBuyDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Description: 图书采购申请业务信息 业务操作接口
 * author: zsl
 * Date: 2017-9-8 10:26:20
 */
public interface BookBuyBusinessService {
    
    PageModelDto<BookBuyBusinessDto> get(ODataObj odataObj);

	void save(BookBuyBusinessDto record);

	void update(BookBuyBusinessDto record);

	BookBuyBusinessDto findById(String deptId);

	ResultMsg delete(String id);

	void deleteByBusinessId(String businessId);

	ResultMsg saveBooksDetailList(BookBuyDto[] bookList,BookBuyBusiness bookBuyBus);

	ResultMsg startFlow(BookBuyDto[] bookList,BookBuyBusiness bookBuyBus);

	//流程处理begin
	ResultMsg stopFlow(String signid, ProjectStopDto projectStopDto);

	ResultMsg restartFlow(String signid);

	ResultMsg endFlow(String signid);

	ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);
	//流程处理end



}
