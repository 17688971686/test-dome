package cs.repository.repositoryImpl.sys;

import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrgRepoImpl extends AbstractRepository<Org, String> implements OrgRepo {

    @SuppressWarnings("unchecked")
    @Override
    public List<Org> findUserChargeOrg() {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(Org_.orgSLeader.getName(), SessionUtil.getUserInfo().getId()));
        return criteria.list();
    }

    /**
     * 根据账号ID统计是否还有分管部门
     *
     * @param oldOrgSLeader
     * @return
     */
    @Override
    public int checkMngOrg(String oldOrgSLeader) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT COUNT (c.id) FROM cs_org c WHERE c.ORGSLEADER = :sLeader ");
        sqlBuilder.setParam("sLeader", oldOrgSLeader);
        return returnIntBySql(sqlBuilder);
    }

}
