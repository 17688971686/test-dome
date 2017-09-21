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


}
