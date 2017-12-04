package cs.service.financial;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.ProjectReviewCostDto;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignAssistCostDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 财务管理 业务操作接口
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
public interface FinancialManagerService {

	void save(FinancialManagerDto record);

	void update(FinancialManagerDto record);

	FinancialManagerDto findById(String deptId);

	void delete(String id);


	BigDecimal sunCount(String signid);

	/**
	 * 初始化财务办理页面
	 * @param businessId   业务ID
	 * @param businessType 业务类型
	 * @return
	 */
	Map<String, Object> initfinancialData(String businessId,String chargeType,String businessType);

    /**
     * 协审费统计列表
     * @param signAssistCostDto
     * @param isShowDetail
     * @return
     */
	List<SignAssistCostDto> signAssistCostList(SignAssistCostDto signAssistCostDto,boolean isShowDetail);

    /**
     * 保存费用项信息
     * @param record
     * @return
     */
	ResultMsg save(FinancialManagerDto[] record);
}
