package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertSelected_;
import cs.domain.project.Sign;
import cs.domain.project.SignMerge;
import cs.domain.project.SignMerge_;
import cs.domain.project.Sign_;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SignRepoImpl extends AbstractRepository<Sign, String> implements SignRepo {

    /**
     * 更改流程状态
     *
     * @param state
     * @return
     */
    @Override
    public boolean updateSignState(String signId, String state) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.signState.getName() + " =:state ");
        hqlBuilder.setParam("state", state);
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
     * 查找合并评审项目
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

}
