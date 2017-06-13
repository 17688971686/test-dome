package cs.repository.repositoryImpl.project;

import cs.domain.project.AssistUnit;
import cs.repository.IRepository;


/**
 * Description: 协审单位 数据操作实现接口
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
public interface AssistUnitRepo extends IRepository<AssistUnit, String> {
	
	int getUnitSortMax();
	
	boolean isUnitExist(String unitName);
}
