package cs.service.project;

import java.util.List;

import cs.common.utils.Validate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.domain.project.SignPrincipal;
import cs.domain.project.SignPrincipal_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.repositoryImpl.project.SignPrincipalRepo;
import cs.repository.repositoryImpl.sys.UserRepo;

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
     * @return
     */
    @Override
    public boolean isMainPri(String userId, String signId) {
        Criteria criteria = signPrincipalRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignPrincipal_.userId.getName(), userId));
        criteria.add(Restrictions.eq(SignPrincipal_.signId.getName(), signId));
        criteria.add(Restrictions.eq(SignPrincipal_.isMainUser.getName(), Constant.EnumState.YES.getValue()));
        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 0 ? true : false;
    }

    /**
     * 确认是否为项目负责人
     *
     * @param userId
     * @param signId
     * @return
     */
    @Override
    public boolean isFlowPri(String userId, String signId) {
        Criteria criteria = signPrincipalRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignPrincipal_.userId.getName(), userId));
        criteria.add(Restrictions.eq(SignPrincipal_.signId.getName(), signId));
        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 0 ? true : false;
    }

    /**
     * 验证是否主项目负责人（第一负责人或者第二负责人）
     *
     * @param userId
     * @param signId
     * @return
     */
    @Override
    public boolean isMainFlowPri(String userId, String signId) {
        Criteria criteria = signPrincipalRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(SignPrincipal_.userId.getName(), userId));
        criteria.add(Restrictions.eq(SignPrincipal_.signId.getName(), signId));
        criteria.add(Restrictions.eq(SignPrincipal_.flowBranch.getName(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue()));
        int count = Integer.parseInt(criteria.uniqueResult().toString());
        return count > 0 ? true : false;
    }


    /**
     * 查询分支负责人
     *
     * @param signId
     * @param branchId ，没有分支，则查询所有
     * @return
     */
    @Override
    public List<User> getSignPriUser(String signId, String branchId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u ");
        hqlBuilder.append(" left join " + SignPrincipal.class.getSimpleName() + " pu  on pu." + SignPrincipal_.userId.getName() + " = u." + User_.id.getName());
        hqlBuilder.append(" where pu." + SignPrincipal_.signId.getName() + " =:signId ").setParam("signId", signId);
        //如果有分支，则查询分支负责人，否则查询所有
        if (Validate.isString(branchId)) {
            hqlBuilder.append(" and pu." + SignPrincipal_.flowBranch.getName() + " =:branchId ").setParam("branchId", branchId);
        }
        hqlBuilder.append(" order by pu." + SignPrincipal_.flowBranch.getName() + "," + SignPrincipal_.isMainUser.getName() + " desc");

        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 获取项目第一负责人(一个项目只有一个总负责人)
     *
     * @param signId
     * @return
     */
    @Override
    public User getMainPriUser(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " = ( ");
        hqlBuilder.append(" select pu." + SignPrincipal_.userId.getName() + " from " + SignPrincipal.class.getSimpleName() + " pu ");
        hqlBuilder.append(" where pu." + SignPrincipal_.signId.getName() + " =:signId ").setParam("signId", signId);
        hqlBuilder.append(" and pu." + SignPrincipal_.isMainUser.getName() + " =:isMainUser ").setParam("isMainUser", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" )");
        List<User> userList = userRepo.findByHql(hqlBuilder);
        if (Validate.isList(userList)) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取所有第二负责人信息
     *
     * @param signId
     * @return
     */
    @Override
    public List<User> getAllSecondPriUser(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u ");
        hqlBuilder.append(" left join " + SignPrincipal.class.getSimpleName() + " pu on pu." + SignPrincipal_.userId.getName() + " = u." + User_.id.getName());
        hqlBuilder.append(" where pu." + SignPrincipal_.signId.getName() + " =:signId ").setParam("signId", signId);
        hqlBuilder.append(" and (pu." + SignPrincipal_.isMainUser.getName() + " is null or pu." + SignPrincipal_.isMainUser.getName() + " =:isMainUser) ");
        hqlBuilder.setParam("isMainUser", Constant.EnumState.NO.getValue());
        hqlBuilder.append(" order by pu." + SignPrincipal_.sort.getName() + " nulls last ");

        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 查询项目负责人信息
     *
     * @param userId
     * @param signId
     * @return
     */
    @Override
    public SignPrincipal getPrincipalInfo(String userId, String signId) {
        Criteria criteria = signPrincipalRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(SignPrincipal_.userId.getName(), userId));
        criteria.add(Restrictions.eq(SignPrincipal_.signId.getName(), signId));
        return (SignPrincipal) criteria.uniqueResult();
    }

    @Override
    public String prinUserName(String businessId, String orgId) {
        List<User> userList = signPrincipalRepo.getPrinUserList(businessId, orgId);
        String resultUser = "";
        if (Validate.isList(userList)) {
            for (User pUser : userList) {
                resultUser += pUser.getDisplayName() + "  ";
            }
        }
        return resultUser;
    }

    @Override
    public String getAllSecondPriUserName(String signid) {
        StringBuffer stringBuffer = new StringBuffer();
        List<User> allSecondPriUser = getAllSecondPriUser(signid);
        if(Validate.isList(allSecondPriUser)){
            for(int i=0,l=allSecondPriUser.size();i<l;i++){
                User user = allSecondPriUser.get(i);
                if(i > 0){
                    stringBuffer.append(",");
                }
                stringBuffer.append(user.getDisplayName());
            }
            return stringBuffer.toString();
        }else{
            return "";
        }

    }

}