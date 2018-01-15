package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.*;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SignRepoImpl extends AbstractRepository<Sign, String> implements SignRepo {

    /**
     * 修改项目状态
     * @param signId
     * @param stateProperty 状态属性
     * @param stateValue 值
     * @return
     */
    @Override
    public boolean updateSignState(String signId,String stateProperty,String stateValue) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + stateProperty + " =:state ");
        hqlBuilder.setParam("state", stateValue);
        hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ");
        hqlBuilder.setParam("signid", signId);

        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 更改项目流程状态
     * @param signId
     * @param processState
     * @return
     */
    @Override
    public boolean updateSignProcessState(String signId, Integer processState) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.processState.getName() + " =:processState ");
        hqlBuilder.setParam("processState", processState);
        hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ");
        hqlBuilder.setParam("signid", signId);
        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 根据委里收文编号，获取项目信息
     * @param filecode
     * @return
     */
    @Override
    public Sign findByFilecode(String filecode) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from  " + Sign.class.getSimpleName());
        hqlBuilder.append(" where " + Sign_.filecode.getName() + " = :filecode ");
        hqlBuilder.setParam("filecode", filecode);

        List<Sign> signList = findByHql(hqlBuilder);
        if(Validate.isList(signList)){
            return signList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 根据项目ID获取关联的项目的ID
     * @param signId
     * @return
     */
    @Override
    public boolean checkIsLink(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(*) from cs_associate_sign where signid = :signid or associate_signid=:associate_signid");
        sqlBuilder.setParam("signid",signId).setParam("associate_signid",signId);
        return returnIntBySql(sqlBuilder)>0?true:false;

    }

    /**
     * 根据合并评审主项目ID，查找合并评审项目
     * @param signId
     * @return
     */
    @Override
    public List<Sign> findReviewSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signid.getName() + " in ");
        hqlBuilder.append(" ( select m."+ SignMerge_.mergeId.getName() +" from "+SignMerge.class.getSimpleName()+" m where " +
                "m."+SignMerge_.signId.getName()+" =:signId and m."+SignMerge_.mergeType.getName()+" =:mergeType )");
        hqlBuilder.setParam("signId",signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        List<Sign> signList = findByHql(hqlBuilder);
        return signList;
    }

    /**
     * 根据合并评审次项目ID，查找合并评审主项目
     * @param signId
     * @return
     */
    @Override
    public List<Sign> findMainReviewSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signid.getName() + " = ");
        hqlBuilder.append(" ( select m."+ SignMerge_.signId.getName() +" from "+SignMerge.class.getSimpleName()+" m where " +
                "m."+SignMerge_.mergeId.getName()+" =:signId and m."+SignMerge_.mergeType.getName()+" =:mergeType )");
        hqlBuilder.setParam("signId",signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        List<Sign> signList = findByHql(hqlBuilder);
        return signList;
    }

    /**
     * 根据合并评审主项目ID，判断合并评审次项目是否完成工作方案环节提交
     * @param signid
     * @return
     */
    @Override
    public boolean isMergeSignEndWP(String signid) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(s.signid) from cs_sign s where s.signid in ");
        sqlBuilder.append(" ( select m.mergeid from cs_sign_merge m where m.signid =:signid )");
        sqlBuilder.setParam("signid",signid);
        sqlBuilder.append(" and s.processState < :processState ");
        sqlBuilder.setParam("processState", Constant.SignProcessState.END_WP.getValue(), IntegerType.INSTANCE);

        return returnIntBySql(sqlBuilder)>0?false:true;
    }

    /**
     * 获取未发送给委里的项目信息
     * @return
     */
    @Override
    public List<Sign> findUnSendFGWList() {
        Criteria criteria = getExecutableCriteria();
        //未发送给发改委的项目
        criteria.add(Restrictions.ne(Sign_.isSendFGW.getName(), Constant.EnumState.YES.getValue()));
        //正式签收
        criteria.add(Restrictions.eq(Sign_.issign.getName(), Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.isNotNull(Sign_.filecode.getName()));
        //排除旧项目
        criteria.add(Restrictions.isNull(Sign_.oldProjectId.getName()));
        criteria.add(Restrictions.isNotNull(Sign_.processInstanceId.getName()));
        //正在进行或者正常结束
        criteria.add(Restrictions.or(Restrictions.eq(Sign_.signdate.getName(), Constant.EnumState.PROCESS.getValue()),
                Restrictions.eq(Sign_.signdate.getName(), Constant.EnumState.YES.getValue())));
        //已经生成发文编号
        criteria.add(Restrictions.ge(Sign_.processState.getName(), Constant.SignProcessState.END_DIS_NUM.getValue()));

       /* //以下是测试用
        criteria.add(Restrictions.eq(Sign_.signid.getName(), "4a2271a6-5908-4d05-b609-c48ed49d6e3f"));
        */
        List<Sign> resultList = criteria.list();

        return resultList;
    }
}
