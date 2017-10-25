package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.AddSuppLetter;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Description: 项目资料补充函 业务操作接口
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
public interface AddSuppLetterService {

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
    AddSuppLetterDto findById(String id);

	/**
	 * 保存补充资料函
	 */
    ResultMsg saveSupp(AddSuppLetterDto addSuppLetterDto);
    
	void delete(String id);
	
    ResultMsg fileNum(String id);

	/**
	 * 新增初始化
	 * @param businessId
	 * @param businessType
	 * @return
	 */
	AddSuppLetterDto initSuppLetter(String businessId, String businessType);

	List<AddSuppLetterDto> initSuppList(String businessId);

	boolean isHaveSuppLetter(String businessId);

	ResultMsg saveMonthlyMultiyear(AddSuppLetterDto record);

	PageModelDto<AddSuppLetterDto> monthlyMultiyearListData(ODataObj odataObj);

	AddSuppLetterDto initMonthlyMutilyear();

	void deletes(String[] ids);

	PageModelDto<AddSuppLetterDto> addsuppListData(ODataObj odataObj);

	PageModelDto<AddSuppLetterDto> addSuppApproveList(ODataObj odataObj);

	void updateApprove(AddSuppLetterDto addSuppLetterDto);

	PageModelDto<AddSuppLetterDto> monthlyAppoveListData(ODataObj odataObj);

    /**
     * 发起项目拟补充资料函流程
     * @param id
     * @return
     */
    ResultMsg startSignSupperFlow(String id);

    /**
     * 拟补充资料函流程处理
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    ResultMsg dealSignSupperFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);
}
