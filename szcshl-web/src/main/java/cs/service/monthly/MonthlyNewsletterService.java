package cs.service.monthly;

import cs.common.ResultMsg;
import cs.domain.monthly.MonthlyNewsletter;
import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;

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

	PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(ODataObj odataObj);

	void deleteMonthlyDatas(String[] ids);

	void deleteMonthlyData(String id);

	void editTheMonthly(MonthlyNewsletterDto record);


}
