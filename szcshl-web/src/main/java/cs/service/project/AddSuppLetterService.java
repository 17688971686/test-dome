package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.AddSuppLetter;
import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 项目资料补充函 业务操作接口
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
public interface AddSuppLetterService {
    
    AddSuppLetterDto getbyId(String id);
    
    ResultMsg addSupp(AddSuppLetterDto addSuppLetterDto);
    
	void delete(String id);
	
    ResultMsg fileNum(String id);

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

	void monthlyApproveEdit(AddSuppLetterDto addSuppLetterDto);

	void saveSupp(AddSuppLetterDto addSuppLetterDto);

	/**
	 * 查询主页上的拟补资料函信息
	 * @return
	 */
	List<AddSuppLetterDto> findHomeAddSuppLetter();

	/**
	 * 查询主页上的月报简报信息列表
	 * @return
	 */
	List<AddSuppLetterDto> findHomeMonthly();

	/**
	 * 统计 拟补充资料函数审批目
	 * @return
	 */
	int countSuppLetter();

	/**
	 * 统计 月报简报审批处理数目
	 * @return
	 */
	int countMonthly();


}
