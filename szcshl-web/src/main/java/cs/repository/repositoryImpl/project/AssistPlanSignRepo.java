package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.common.ResultMsg;
import cs.domain.project.AssistPlanSign;
import cs.model.project.AssistPlanSignDto;
import cs.repository.IRepository;

/**
 * Description: 协审项目 数据操作实现接口
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
public interface AssistPlanSignRepo extends IRepository<AssistPlanSign, String> {
	
	List<AssistPlanSign> getAssistPlanSignByAssistPlanId(String assistPlanId);

	/**
	 * 通过收文Id 获取协审单位和协审费用
	 * @param signId
	 * @return
	 */
	ResultMsg findAssistPlanSignById(String signId);
	
	
}
