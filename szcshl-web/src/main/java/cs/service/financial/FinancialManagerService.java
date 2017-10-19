package cs.service.financial;

import java.math.BigDecimal;
import java.util.Map;

import cs.model.PageModelDto;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 财务管理 业务操作接口
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
public interface FinancialManagerService {
    
    PageModelDto<SignDto> get(ODataObj odataObj);

	void save(FinancialManagerDto record);

	void update(FinancialManagerDto record);

	FinancialManagerDto findById(String deptId);

	void delete(String id);


	BigDecimal sunCount(String signid);

	Map<String, Object> initfinancialData(String signid);

	PageModelDto<SignDto> assistCostCountGet(ODataObj odataObj);

}
