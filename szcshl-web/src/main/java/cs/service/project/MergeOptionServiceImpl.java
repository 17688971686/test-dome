package cs.service.project;

import cs.common.utils.Validate;
import cs.domain.project.MergeOption_;
import cs.repository.repositoryImpl.project.MergeOptionRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MergeOptionServiceImpl implements MergeOptionService {
    @Autowired
    private MergeOptionRepo mergeOptionRepo;

    /**
     * 是否是合并项目
     * @param businessId
     * @param signId
     * @param businessType
     * @return
     */
    @Override
    public boolean isMerge(String businessId, String signId,String businessType) {
        Criteria criteria = mergeOptionRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(MergeOption_.businessType.getName(),businessType));
        if(Validate.isString(businessId)){
            criteria.add(Restrictions.eq(MergeOption_.businessId.getName(),businessId));
        }
        if(Validate.isString(signId)){
            criteria.add(Restrictions.eq(MergeOption_.signId.getName(),signId));
        }
        int count = Integer.parseInt(criteria.uniqueResult().toString());

        return count > 0 ? true : false;
    }

    /**
     * 主项目是否有关联
     * @param mainBusinessId
     * @param businessType
     * @return
     */
    @Override
    public boolean isHaveLink(String mainBusinessId,String businessType) {
        Criteria criteria = mergeOptionRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(MergeOption_.businessType.getName(),businessType));
        criteria.add(Restrictions.eq(MergeOption_.mainBusinessId.getName(),mainBusinessId));

        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 1 ? true : false; //本身也有一条记录
    }
}
