package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertSelected_;
import cs.domain.project.*;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.domain.sys.User_;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.sys.OrgRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkProgramRepoImpl extends AbstractRepository<WorkProgram,String> implements WorkProgramRepo {

    @Autowired
    private OrgRepo orgRepo;
    /**
     * 根据收文编号，查询对应工作方案
     * @param signId
     * @return
     */
    @Override
    public WorkProgram findByPrincipalUser(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.createAlias(WorkProgram_.sign.getName(), WorkProgram_.sign.getName());
        criteria.add(Restrictions.eq(WorkProgram_.sign.getName()+ "." + Sign_.signid.getName(), signId));
        criteria.add(Restrictions.sqlRestriction(" branchId = (select flowBranch from cs_sign_principal2 where signId ='"+signId+"' and userId ='"+ SessionUtil.getUserInfo().getId()+"' )"));
        return (WorkProgram)criteria.uniqueResult();
    }



    /**
     * 根据收文ID和分支ID查询对应的工作方案
     * @param signId
     * @param branchId
     * @return
     */
    @Override
    public WorkProgram findBySignIdAndBranchId(String signId, String branchId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+WorkProgram.class.getSimpleName()+" where "+WorkProgram_.sign.getName()+"."+Sign_.signid.getName()+" =:signId ");
        hqlBuilder.setParam("signId",signId);
        hqlBuilder.append(" and "+WorkProgram_.branchId.getName()+" =:branchId ").setParam("branchId",branchId);
        List<WorkProgram> wpList = findByHql(hqlBuilder);
        if(Validate.isList(wpList)){
            return wpList.get(0);
        }
        return null;
    }

    /**
     * 初始化专家评审费，默认每个专家1000元
     * @param id
     */
    @Override
    public void initExpertCost(String id) {
        //1、统计已经确认的专家个数
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_WORK_PROGRAM wp SET wp.expertCost = 1000 * (SELECT COUNT (ID) FROM cs_expert_selected es " );
        sqlBuilder.append(" WHERE es."+ ExpertSelected_.isConfrim.getName()+" = :isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" AND es."+ ExpertSelected_.isJoin.getName()+" = :isJoin ");
        sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" AND es."+ExpertSelected_.businessId.getName()+" = wp.id ) ");
        sqlBuilder.append(" WHERE wp.id = :id");
        sqlBuilder.setParam("id",id);
        executeSql(sqlBuilder);
    }


    /**
     * 根据项目ID，更改合并评审状态
     * @param isSigle
     * @param isMain
     * @param mergeIds
     */
    @Override
    public void updateWPReivewType(String signId,String isSigle, String isMain, String mergeIds) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_work_program set "+WorkProgram_.isSigle.getName()+" =:isSigle ");
        sqlBuilder.setParam("isSigle",isSigle);
        if(Validate.isString(isMain)){
            sqlBuilder.append(" , "+WorkProgram_.isMainProject.getName()+" =:isMainProject ");
            sqlBuilder.setParam("isMainProject",isMain);
        }else{
            sqlBuilder.append(" , "+WorkProgram_.isMainProject.getName()+" = null ");
        }
        //如果是合并评审，则评审方式改为审主项目一致
        if(Constant.MergeType.REVIEW_MERGE.getValue().equals(isSigle)){
            sqlBuilder.append(","+WorkProgram_.reviewType.getName()+" = (select wp.reviewType from cs_work_program wp where wp.signid =:signid and wp.branchId = :branchId ) ");
            sqlBuilder.setParam("signid",signId);
            sqlBuilder.setParam("branchId", FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        }
        sqlBuilder.bulidPropotyString("where","signid",mergeIds);

        executeSql(sqlBuilder);
    }

    /**
     * 根据项目ID获取合并评审主工作方案信息
     * @param signId
     * @return
     */
    @Override
    public WorkProgram findMainReviewWP(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(" signid = (SELECT signid FROM CS_SIGN_MERGE where mergeid = '"+signId+"' and mergeType = '"+Constant.MergeType.WORK_PROGRAM.getValue()+"')"));
        criteria.add(Restrictions.eq(WorkProgram_.branchId.getName(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue()));
        List<WorkProgram> wpList = criteria.list();
        if(Validate.isList(wpList)){
            return wpList.get(0);
        }

        return null;
    }
}
