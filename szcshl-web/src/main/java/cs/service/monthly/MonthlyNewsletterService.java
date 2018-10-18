package cs.service.monthly;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.File;

/**
 * Description: 月报简报 业务操作接口
 * author: sjy
 * Date: 2017-9-8 11:23:41
 */
public interface MonthlyNewsletterService {
    
    PageModelDto<MonthlyNewsletterDto> get(ODataObj odataObj);

	ResultMsg save(MonthlyNewsletterDto record);

	void update(MonthlyNewsletterDto record);

	MonthlyNewsletterDto findById(String deptId);

	void delete(String id);

	void deletes(String[] ids);

	PageModelDto<MonthlyNewsletterDto> deleteHistoryList(ODataObj odataObj);

	PageModelDto<MonthlyNewsletterDto> mothlyHistoryList(ODataObj odataObj);

	ResultMsg saveTheMonthly(MonthlyNewsletterDto record);

	PageModelDto<MonthlyNewsletterDto> getMonthlyList(ODataObj odataObj);

	//PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(ODataObj odataObj);

	ResultMsg deleteMonthlyData(String id);

	void editTheMonthly(MonthlyNewsletterDto record);

	/**
	 * 生成月报简报模板
	 * @param monthlyNewsletterDto
	 */
	File createMonthTemplate(MonthlyNewsletterDto monthlyNewsletterDto);

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

	ResultMsg restoreMonthlyData(String id);
}
