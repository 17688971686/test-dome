package cs.repository.repositoryImpl.project;

import cs.domain.project.SignPrincipal;
import cs.domain.project.SignPrincipal_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

/**
 * Description: 项目-负责人中间表 数据操作实现类
 * author: ldm
 * Date: 2017-6-16 14:48:31
 */
@Repository
public class SignPrincipalRepoImpl extends AbstractRepository<SignPrincipal, String> implements SignPrincipalRepo {

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<User> getPrinUserList(String businessId, String orgId) {
        String querySql = " SELECT cp2.USERID FROM CS_SIGN_PRINCIPAL2 cp2, CS_SIGN_BRANCH cb WHERE cp2.SIGNID = cb.SIGNID AND CP2.FLOWBRANCH = CB.BRANCHID ";
        querySql += "AND CP2.SIGNID = ? AND CB.ORGID = ?";
        Object[] values = new Object[]{businessId,orgId};
        Criteria criteria = userRepo.getExecutableCriteria();
        Type[] types = new Type[]{StringType.INSTANCE,StringType.INSTANCE};
        criteria.add(Restrictions.sqlRestriction(" {alias}."+ User_.id.getName()+" in ( "+querySql+")",values, types));
        return criteria.list();
    }
}