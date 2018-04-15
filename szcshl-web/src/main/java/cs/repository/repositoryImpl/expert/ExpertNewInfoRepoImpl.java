package cs.repository.repositoryImpl.expert;

import cs.domain.expert.*;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 专家聘书 数据操作实现类
 * author: zsl
 * Date: 2017-6-19 19:13:35
 */
@Repository
public class ExpertNewInfoRepoImpl extends AbstractRepository<ExpertNewInfo, String> implements ExpertNewInfoRepo {
    @Autowired
    private ExpertNewInfoRepo expertNewInfoRepo;
    /**
     * 根据业务ID获取选取的专家信息(确认并参加会议的专家)
     * @param businessId
     * @return
     */
    @Override
    public List<ExpertNewInfo> findByBusinessId(String businessId) {
        Criteria criteria = expertNewInfoRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(ExpertNewInfo_.businessId.getName(),businessId));
        criteria.add(Restrictions.eq(ExpertNewInfo_.isJoin.getName(),"9"));
        criteria.add(Restrictions.eq(ExpertNewInfo_.isConfrim.getName(),"9"));
       return criteria.list();

    }
}