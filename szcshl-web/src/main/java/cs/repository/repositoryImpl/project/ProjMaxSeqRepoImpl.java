package cs.repository.repositoryImpl.project;

import cs.common.RandomGUID;
import cs.common.utils.Validate;
import cs.domain.project.ProjMaxSeq;
import cs.domain.project.ProjMaxSeq_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by ldm on 2018/4/11.
 */
@Repository
public class ProjMaxSeqRepoImpl extends AbstractRepository<ProjMaxSeq, String> implements ProjMaxSeqRepo {

    @Override
    public ProjMaxSeq findByDate(String year, String type) {
        int intYear = Integer.parseInt(year);
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(ProjMaxSeq_.year.getName(),intYear));
        criteria.add(Restrictions.eq(ProjMaxSeq_.type.getName(),type));
        ProjMaxSeq projMaxSeq = (ProjMaxSeq) criteria.uniqueResult();
        //如果没有值，则返回默认值
        if(!Validate.isObject(projMaxSeq) || !Validate.isString(projMaxSeq.getId())){
            projMaxSeq = new ProjMaxSeq();
            projMaxSeq.setId((new RandomGUID()).valueAfterMD5);
            projMaxSeq.setSeq(0);
            projMaxSeq.setYear(intYear);
            projMaxSeq.setType(type);
        }
        return projMaxSeq;
    }
}
