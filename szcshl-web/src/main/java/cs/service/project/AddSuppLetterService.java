package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.project.AddSuppLetter;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.Date;
import java.util.List;

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

	/**
	 * 月报简报查询
	 * @param odataObj
	 * @return
	 */
	PageModelDto<AddSuppLetterDto> monthlyMultiyearListData(ODataObj odataObj);

	AddSuppLetterDto initMonthlyMutilyear();

	void deletes(String[] ids);

	/**
	 * 拟补充资料函查询
	 * @param odataObj
	 * @return
	 */
	PageModelDto<AddSuppLetterDto> addsuppListData(ODataObj odataObj);

	PageModelDto<AddSuppLetterDto> addSuppApproveList(ODataObj odataObj);



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

	/**
	 * 更新拟补充资料函状态
	 * @param businessId
	 * @param businessType
	 * @param disapDate
	 */
	void updateSuppLetterState(String businessId, String businessType, Date disapDate);

	/**
	 * 初始化最大收文编号
	 * @param addSuppLetter
	 */
	void initFileNum(AddSuppLetter addSuppLetter);

	/**
	 * 检查是否还有正在审批的拟补充资料函
	 * @param signId
	 * @param fileType
	 * @return
	 */
    ResultMsg checkIsApprove(String signId, String fileType);

	/**
	 * 结束流程
	 * @param businessKey
	 * @return
	 */
	ResultMsg endFlow(String businessKey);
}
