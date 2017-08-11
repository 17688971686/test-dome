package cs.service.financial;

import java.util.List;
import java.util.Map;

import cs.domain.financial.FinancialManager;
import cs.model.PageModelDto;
import cs.model.financial.FinancialManagerDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 财务管理 业务操作接口
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
public interface FinancialManagerService {
    
    PageModelDto<FinancialManagerDto> get(ODataObj odataObj);

	void save(FinancialManagerDto record);

	void update(FinancialManagerDto record);

	FinancialManagerDto findById(String deptId);

	void delete(String id);


	Integer sunCount(String signid);

	Map<String, Object> initfinancialData(String signid);

}
