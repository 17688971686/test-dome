package cs.repository.repositoryImpl.flow;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.domain.flow.FlowPrincipal;
import cs.domain.flow.FlowPrincipal_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 流程负责人 数据操作实现类
 * author: ldm
 * Date: 2017-9-4 17:47:04
 */
@Repository
public class FlowPrincipalRepoImpl extends AbstractRepository<FlowPrincipal, String> implements FlowPrincipalRepo {
    @Autowired
    private UserRepo userRepo;
    /**
     * 获取所有的流程项目负责人信息
     * @param busiId
     * @return
     */
    @Override
    public List<User> getFlowAllPrinByBusiId(String busiId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " in ( ");
        hqlBuilder.append(" select pu." + FlowPrincipal_.userId.getName() + " from " + FlowPrincipal.class.getSimpleName() + " pu ");
        hqlBuilder.append(" where pu." + FlowPrincipal_.busiId.getName() + " =:busiId ").setParam("busiId", busiId);
        hqlBuilder.append(" )");
        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 获取流程的项目负责人ID（除第一负责人外）
     * @param busiId
     * @return
     */
    @Override
    public List<User> getFlowPrinByBusiId(String busiId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " = ( ");
        hqlBuilder.append(" select pu." + FlowPrincipal_.userId.getName() + " from " + FlowPrincipal.class.getSimpleName() + " pu ");
        hqlBuilder.append(" where pu." + FlowPrincipal_.busiId.getName() + " =:busiId ").setParam("busiId", busiId);
        hqlBuilder.append(" and (pu."+FlowPrincipal_.isMainUser.getName()+" is null or pu."+FlowPrincipal_.isMainUser.getName()+ " =:isMain ) ");
        hqlBuilder.setParam("isMain", Constant.EnumState.NO.getValue());
        hqlBuilder.append(" )");
        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 获取流程的第一负责人
     * @param busiId
     * @return
     */
    @Override
    public User getFlowMainPrinByBusiId(String busiId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " = ( ");
        hqlBuilder.append(" select pu." + FlowPrincipal_.userId.getName() + " from " + FlowPrincipal.class.getSimpleName() + " pu ");
        hqlBuilder.append(" where pu." + FlowPrincipal_.busiId.getName() + " =:busiId ").setParam("busiId", busiId);
        hqlBuilder.append(" and pu."+FlowPrincipal_.isMainUser.getName()+" =:isMain ").setParam("isMain", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" )");
        List<User> userList=userRepo.findByHql(hqlBuilder);
        if(!userList.isEmpty()){
            return userRepo.findByHql(hqlBuilder).get(0);
        }else{
            return null;
        }
    }

    /**
     * 查询负责人信息
     * @param busiId
     * @return
     */
    @Override
    public List<FlowPrincipal> getFlowPrinInfoByBusiId(String busiId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(FlowPrincipal_.busiId.getName(),busiId));
        return criteria.list();
    }

    /**
     * 根据业务ID删除流程负责人
     * @param busiId
     */
    @Override
    public void deletePriUserByBusiId(String busiId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from "+FlowPrincipal.class.getSimpleName()+" where ");
        hqlBuilder.append(FlowPrincipal_.busiId.getName()+" =:busiId ");
        hqlBuilder.setParam("busiId",busiId);
        executeHql(hqlBuilder);
    }

    @Override
    public boolean isHavePriUserByBusiId(String busiId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(FlowPrincipal_.busiId.getName(),busiId));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult==null||totalResult==0)?false:true;
    }
}