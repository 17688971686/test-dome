package cs.service.project;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.model.project.AssistPlanSignDto;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


	@Override
	public List<AssistPlanSignDto> findBySignId(String signId) {
        Criteria criteria = assistPlanSignRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(AssistPlanSign_.signId.getName(),signId));
        List<AssistPlanSign> list = criteria.list();
        List<AssistPlanSignDto> resultList = new ArrayList<>(list == null?0:list.size());
        if(list != null && list.size() > 0){
            list.forEach( l -> {
                AssistPlanSignDto assistPlanSignDto = new AssistPlanSignDto();
                BeanCopierUtils.copyProperties(l,assistPlanSignDto);
                resultList.add(assistPlanSignDto);
            });
        }
		return resultList;
	}
}