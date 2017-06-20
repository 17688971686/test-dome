package cs.service.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.SignPrincipal;
import cs.domain.project.SignPrincipal_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.repositoryImpl.project.SignPrincipalRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 项目-负责人中间表
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Service
public class SignPrincipalServiceImpl implements SignPrincipalService {

    @Autowired
    private SignPrincipalRepo signPrincipalRepo;
    @Autowired
    private UserRepo userRepo;

    /**
     * 验证是否为第一负责人(总负责人)
     *
     * @param userId
     * @param signId
     * @param isMainFolw
     * @return
     */
    @Override
    public boolean isMainPri(String userId, String signId, String isMainFolw) {
        Criteria criteria = signPrincipalRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignPrincipal_.userId.getName(), userId));
        criteria.add(Restrictions.eq(SignPrincipal_.singId.getName(), signId));
        criteria.add(Restrictions.eq(SignPrincipal_.isMainFlow.getName(), isMainFolw));
        criteria.add(Restrictions.eq(SignPrincipal_.isMainUser.getName(), Constant.EnumState.YES.getValue()));

        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 0 ? true : false;
    }

    /**
     * 确认是否为项目负责人
     *
     * @param userId
     * @param signId
     * @param isMainFolw
     * @return
     */
    @Override
    public boolean isFlowPri(String userId, String signId, String isMainFolw) {
        Criteria criteria = signPrincipalRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignPrincipal_.userId.getName(), userId));
        criteria.add(Restrictions.eq(SignPrincipal_.singId.getName(), signId));
        criteria.add(Restrictions.eq(SignPrincipal_.isMainFlow.getName(), isMainFolw));
        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 0 ? true : false;
    }


    /**
     * 获取项目负责人信息
     *
     * @param signId
     * @param isMainFolw
     * @return
     */
    @Override
    public List<User> getSignPriUser(String signId, String isMainFolw) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " in ( ");
        hqlBuilder.append(" select pu." + SignPrincipal_.userId.getName() + " from " + SignPrincipal.class.getSimpleName() + " pu ");
        hqlBuilder.append(" where pu." + SignPrincipal_.singId.getName() + " =:signId ").setParam("signId", signId);
        if (Validate.isString(isMainFolw)) {
            hqlBuilder.append(" and pu." + SignPrincipal_.isMainFlow.getName() + " =:isMainFolw ").setParam("isMainFolw", isMainFolw);
        }
        hqlBuilder.append(" )");

        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 获取项目第一负责人(总负责人)
     *
     * @param signId
     * @param isMainFolw
     * @return
     */
    @Override
    public User getMainPriUser(String signId, String isMainFolw) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " = ( ");
        hqlBuilder.append(" select pu." + SignPrincipal_.userId.getName() + " from " + SignPrincipal.class.getSimpleName() + " pu ");
        hqlBuilder.append(" where pu." + SignPrincipal_.singId.getName() + " =:signId ").setParam("signId", signId);
        hqlBuilder.append(" and pu." + SignPrincipal_.isMainFlow.getName() + " =:isMainFolw ").setParam("isMainFolw", isMainFolw);
        hqlBuilder.append(" and pu." + SignPrincipal_.isMainUser.getName() + " =:isMainUser ").setParam("isMainUser", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" )");
        List<User> userList=userRepo.findByHql(hqlBuilder);
        if(!userList.isEmpty()){
        	
        	return userRepo.findByHql(hqlBuilder).get(0);
        }else{
        	return null;
        }
    }

    /**
     * 获取第二负责人，当有三个的时候，获取序号靠前用户
     *
     * @param signId
     * @param isMainFolw
     * @return
     */
    @Override
    public User getSecondPriUser(String signId, String isMainFolw) {
        List<User> secondUserList = getAllSecondPriUser(signId,isMainFolw);
        if(secondUserList != null && secondUserList.size() > 0){
            return getAllSecondPriUser(signId,isMainFolw).get(0);
        }else{
            return null;
        }
    }

    /**
     * 获取所有第二负责人信息
     *
     * @param signId
     * @param isMainFolw
     * @return
     */
    @Override
    public List<User> getAllSecondPriUser(String signId, String isMainFolw) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u ");
        hqlBuilder.append(" left join " + SignPrincipal.class.getSimpleName() + " pu on pu." + SignPrincipal_.userId.getName() + " = u." + User_.id.getName());
        hqlBuilder.append(" where pu." + SignPrincipal_.singId.getName() + " =:signId ").setParam("signId", signId);
        if(Validate.isString(isMainFolw)){
            hqlBuilder.append(" and pu." + SignPrincipal_.isMainFlow.getName() + " =:isMainFolw ").setParam("isMainFolw", isMainFolw);
        }
        hqlBuilder.append(" and pu." + SignPrincipal_.isMainUser.getName() + " =:isMainUser ").setParam("isMainUser", Constant.EnumState.NO.getValue());
        hqlBuilder.append(" order by pu."+SignPrincipal_.sort.getName()+" nulls last ");

        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 根据用户类型查询负责人信息
     *
     * @param signId
     * @param isMainFolw
     * @param userType
     * @return
     */
    @Override
    public User getPriUserByType(String signId, String isMainFolw, String userType) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " = ( ");
        hqlBuilder.append(" select pu." + SignPrincipal_.userId.getName() + " from " + SignPrincipal.class.getSimpleName() + " pu ");
        hqlBuilder.append(" where pu." + SignPrincipal_.singId.getName() + " =:signId ").setParam("signId", signId);
        hqlBuilder.append(" and pu." + SignPrincipal_.isMainFlow.getName() + " =:isMainFolw ").setParam("isMainFolw", isMainFolw);
        hqlBuilder.append(" and pu." + SignPrincipal_.userType.getName() + " =:userType ").setParam("userType", userType);
        hqlBuilder.append(" )");
        return userRepo.findByHql(hqlBuilder).get(0);
    }


}