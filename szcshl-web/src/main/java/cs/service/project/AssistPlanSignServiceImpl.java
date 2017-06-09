package cs.service.project;

import cs.common.ICurrentUser;
import cs.common.service.ServiceImpl;
import cs.common.utils.BeanCopierUtils;
import cs.domain.project.AssistPlanSign;
import cs.model.PageModelDto;
import cs.model.project.AssistPlanSignDto;
import cs.repository.odata.ODataObj;

import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 协审项目 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
@Service
public class AssistPlanSignServiceImpl  implements AssistPlanSignService {

	@Autowired
	private AssistPlanSignRepo assistPlanSignRepo;
	@Autowired
	private ICurrentUser currentUser;
	

	
}