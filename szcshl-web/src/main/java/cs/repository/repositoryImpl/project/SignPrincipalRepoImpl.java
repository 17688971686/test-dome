package cs.repository.repositoryImpl.project;

import cs.common.HqlBuilder;
import cs.domain.project.SignBranch_;
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

    /**
     * 根据项目和部门ID查询对应的项目负责人
     * @param businessId
     * @param orgId
     * @return
     */
    @Override
    public List<User> getPrinUserList(String businessId, String orgId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select u.* from cs_user u,cs_sign_principal2 pu,CS_SIGN_BRANCH b ");
        sqlBuilder.append(" WHERE u.id = pu.userId AND PU.FLOWBRANCH = B.BRANCHID AND PU.SIGNID = B.SIGNID");
        sqlBuilder.append(" and pu."+SignPrincipal_.signId.getName()+" =:signId ").setParam("signId",businessId);
        sqlBuilder.append(" and b."+ SignBranch_.orgId.getName()+" =:orgId ").setParam("orgId",orgId);
        sqlBuilder.append(" order by pu."+SignPrincipal_.isMainUser.getName()+" desc,pu."+SignPrincipal_.sort.getName());
        List<User> resultList = userRepo.findBySql(sqlBuilder);
        return resultList;
    }
}