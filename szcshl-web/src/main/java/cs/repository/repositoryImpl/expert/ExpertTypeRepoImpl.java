package cs.repository.repositoryImpl.expert;

import cs.common.HqlBuilder;
import cs.domain.expert.ExpertType;
import cs.domain.expert.ExpertType_;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpertTypeRepoImpl extends AbstractRepository<ExpertType, String> implements ExpertTypeRepo {

    /**
     * 判断专家的突出专业是否已经存在
     *
     * @param maJorBig
     * @param maJorSmall
     * @param expertType
     * @param expertId
     * @return
     */
    @Override
    public boolean checkExpertTypeExist(String maJorBig, String maJorSmall, String expertType, String expertId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(" + ExpertType_.id.getName() + ") from cs_expert_type ");
        sqlBuilder.append(" where " + ExpertType_.maJorBig.getName() + " =:maJorBig ").setParam("maJorBig", maJorBig);
        sqlBuilder.append(" and " + ExpertType_.maJorSmall.getName() + " =:maJorSmall ").setParam("maJorSmall", maJorSmall);
        sqlBuilder.append(" and " + ExpertType_.expertType.getName() + " =:expertType ").setParam("expertType", expertType);
        sqlBuilder.append(" and expertID =:expertID ").setParam("expertID", expertId);
        int count = returnIntBySql(sqlBuilder);
        return (count > 0) ? true : false;
    }

}
