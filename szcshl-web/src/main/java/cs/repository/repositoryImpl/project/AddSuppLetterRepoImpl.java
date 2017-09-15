package cs.repository.repositoryImpl.project;

import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Description: 项目资料补充函 数据操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Repository
public class AddSuppLetterRepoImpl extends AbstractRepository<AddSuppLetter, String> implements AddSuppLetterRepo {
    /**
     * 根据业务ID判断是否有补充资料函
     * @param businessId
     * @return
     */
    @Override
    public boolean isHaveSuppLetter(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(AddSuppLetter_.businessId.getName(),businessId));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult > 0)?true:false;
    }
}