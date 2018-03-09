package cs.repository.repositoryImpl.project;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.UnitScore;
import cs.domain.project.UnitScore_;
import cs.domain.sys.Company;
import cs.domain.sys.Company_;
import cs.model.project.UnitScoreDto;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.sys.CompanyRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnitScoreRepoImpl extends AbstractRepository<UnitScore, String> implements UnitScoreRepo {

    @Override
    public UnitScore findUnitScore(String signid){
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(UnitScore_.signid.getName(),signid));
        List<UnitScore> unitScoreList=criteria.list();
        if(Validate.isList(unitScoreList)){
            return unitScoreList.get(0);
        }else{
            return null;
        }

    }

}
