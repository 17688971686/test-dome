package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.common.HqlBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnit_;
import cs.model.project.AssistUnitDto;
import cs.repository.AbstractRepository;

/**
 * Description: 协审单位 数据操作实现类
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
@Repository
public class AssistUnitRepoImpl extends AbstractRepository<AssistUnit, String> implements AssistUnitRepo {

    @Override
    public int getUnitSortMax() {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select max(unitSort) from cs_as_unit  ");
        return returnIntBySql(sqlBuilder);
    }

    @Override
    public boolean isUnitExist(String unitName) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(AssistUnit_.unitName.getName(), unitName));
        List<AssistUnit> assistUnitList = criteria.list();
        return !assistUnitList.isEmpty();
    }

    @Override
    public List<AssistUnit> getAssistUnitByPlanId(String planId) {
        Criteria criteria = getExecutableCriteria();
        criteria.createAlias("assistPlanList", "assistPlanList").add(Restrictions.eq("assistPlanList.id", planId));
        return criteria.list();
    }

    /**
     * 校验有没有协审单位
     *
     * @param businessKey
     * @return
     */
    @Override
    public boolean checkAssistUnitBySignId(String businessKey) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("SELECT COUNT (csp.id) FROM CS_AS_PLANSIGN csp WHERE CSP.SIGNID =:signId AND ASSISTUNITID IS NOT NULL AND PLANID IS NOT NULL");
        sqlBuilder.setParam("signId",businessKey);
        int resultInt = returnIntBySql(sqlBuilder);
        return resultInt > 0 ? true : false;
    }

}