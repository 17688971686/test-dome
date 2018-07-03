package cs.repository.repositoryImpl.project;

import cs.domain.project.SignMerge;
import cs.domain.project.SignMerge_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SignMergeRepoImpl extends AbstractRepository<SignMerge, String> implements SignMergeRepo {


    /**
     * 根据项目ID判断该项目是否有关联
     * @param signId
     * @param mergeType
     * @return
     */
    @Override
    public boolean isHaveMerge(String signId, String mergeType) {
        Criteria criteria = getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignMerge_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignMerge_.mergeType.getName(),mergeType));
        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 0 ? true : false;
    }

    /**
     * 判断是否是关联项目
     * @param signId
     * @param mergeType
     * @return
     */
    @Override
    public boolean checkIsMerege(String signId, String mergeType) {
        Criteria criteria = getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignMerge_.mergeId.getName(),signId));
        criteria.add(Restrictions.eq(SignMerge_.mergeType.getName(),mergeType));
        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 0 ? true : false;
    }

    @Override
    public List<SignMerge> findByType(String signId, String mergeType) {
        Criteria criteria = getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignMerge_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignMerge_.mergeType.getName(),mergeType));
        return criteria.list();
    }

}