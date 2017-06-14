package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.domain.project.AssistUnitUser;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

/**
 * Description: 协审单位用户 数据操作实现接口
 * author: ldm
 * Date: 2017-6-9 9:37:54
 */
public interface AssistUnitUserRepo extends IRepository<AssistUnitUser, String> {
	
	List<AssistUnitUser> getUserNotIn(ODataObj odataObj,List<String> assistUnitUserIds);
	
	boolean isUserExist(String userName);
}