package cs.repository.repositoryImpl.project;

import cs.common.utils.Validate;
import cs.domain.project.UnitScore;
import cs.domain.project.UnitScore_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnitScoreRepoImpl extends AbstractRepository<UnitScore, String> implements UnitScoreRepo {

    @Override
    public UnitScore findUnitScore(String signid) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(UnitScore_.signid.getName(), signid));
        List<UnitScore> unitScoreList = criteria.list();
        if (Validate.isList(unitScoreList)) {
            UnitScore resultUnitScore = null;
            for(UnitScore unitScore : unitScoreList){
                if(null == unitScore.getCompany()){
                    delete(unitScore);
                }else{
                    resultUnitScore = unitScore;
                }
            }
            return resultUnitScore;
        } else {
            return null;
        }

    }

}
