package cs.service.project;

import cs.model.project.AssistPlanSignDto;

import java.util.List;

/**
 * Description: 协审项目 业务操作接口
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
public interface AssistPlanSignService {
	List<AssistPlanSignDto> getPlanSignByPlanId(String planId);

	void savePlanSign(AssistPlanSignDto[] planSigns);
	List<AssistPlanSignDto> findBySignId(String signId);
}
