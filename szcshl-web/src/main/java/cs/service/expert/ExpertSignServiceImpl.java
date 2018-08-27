package cs.service.expert;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertSign;
import cs.domain.expert.ExpertSign_;
import cs.model.expert.ExpertSignDto;
import cs.repository.repositoryImpl.expert.ExpertSignRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpertSignServiceImpl implements ExpertSignService {

    @Autowired
    private ExpertSignRepo expertSignRepo;

    /**
     * 通过专家id 获取专家评审过的项目信息
     * @param expertId
     * @return
     */
    @Override
    public List<ExpertSignDto> reviewProject(String expertId) {
        Criteria criteria = expertSignRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(ExpertSign_.expertId.getName(),expertId));
        criteria.addOrder(Order.desc(ExpertSign_.signdate.getName()));
        List<ExpertSign> expertSignList = criteria.list();
        List<ExpertSignDto> resultList = new ArrayList<>();
        if(Validate.isList(expertSignList)){
            expertSignList.forEach(es->{
                ExpertSignDto expertSignDto = new ExpertSignDto();
                BeanCopierUtils.copyProperties(es,expertSignDto);
                resultList.add(expertSignDto);
            });
        }
        return resultList;
    }
}
