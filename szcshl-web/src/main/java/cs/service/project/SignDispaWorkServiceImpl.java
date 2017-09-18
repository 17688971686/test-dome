package cs.service.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.domain.project.SignMerge;
import cs.domain.project.SignMerge_;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignMergeRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目信息视图 Service
 * @author  ldm
 */
@Service
public class SignDispaWorkServiceImpl implements SignDispaWorkService {
    private static Logger log = Logger.getLogger(SignDispaWorkServiceImpl.class);

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;

    /**
     * 项目综合查询
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SignDispaWork> getCommQurySign(ODataObj odataObj) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<SignDispaWork>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<SignDispaWork> signDispaWork = criteria.list();
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(signDispaWork);
        return pageModelDto;
    }

    /**
     * 合并评审，关联项目显示出同部门，项目工作方案未审批的项目
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDispaWork> unMergeWPSign(String signId) {
        SignDispaWork mergeSign = signDispaWorkRepo.findById(signId);
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where " + SignDispaWork_.processState.getName() + " =:processState ");
        hqlBuilder.setParam("processState", Constant.SignProcessState.END_WP.getValue(), IntegerType.INSTANCE);
        hqlBuilder.append("and " + SignDispaWork_.mOrgId.getName() +" = :mainOrgId ");
        hqlBuilder.setParam("mainOrgId", mergeSign.getmOrgId());
        hqlBuilder.append(" and " + SignDispaWork_.signid.getName() + " != :self ").setParam("self", signId);
        hqlBuilder.append(" and " + SignDispaWork_.signid.getName() + " not in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());

        return signDispaWorkRepo.findByHql(hqlBuilder);
    }

    /**
     * 获取已合并评审的项目信息
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDispaWork> getMergeWPSignBySignId(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where " + SignDispaWork_.signid.getName());
        hqlBuilder.append(" in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        return signDispaWorkRepo.findByHql(hqlBuilder);
    }

    /**
     * 获取待合并发文的项目
     * (已完成工作方案，但是没有生成发文编号的项目)
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDispaWork> unMergeDISSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where ");
        hqlBuilder.append(SignDispaWork_.processState.getName() + " > :processState1  and " + SignDispaWork_.processState.getName() + " < :processState2 " );
        hqlBuilder.setParam("processState1",Constant.SignProcessState.END_WP.getValue(),IntegerType.INSTANCE);
        hqlBuilder.setParam("processState2",Constant.SignProcessState.END_DIS_NUM.getValue(),IntegerType.INSTANCE);
        //发文编号为空
        hqlBuilder.append(" and (" + SignDispaWork_.dfilenum.getName() + " is null or " + SignDispaWork_.dfilenum.getName() + " = '') ");
        hqlBuilder.append(" and " + SignDispaWork_.signid.getName() + " != :self ").setParam("self", signId);
        hqlBuilder.append(" and " + SignDispaWork_.signid.getName() + " not in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.DISPATCH.getValue());
        return signDispaWorkRepo.findByHql(hqlBuilder);
    }

    /**
     * 获取已选合并发文的项目
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDispaWork> getMergeDISSignBySignId(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where " + SignDispaWork_.signid.getName());
        hqlBuilder.append(" in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.DISPATCH.getValue());
        return signDispaWorkRepo.findByHql(hqlBuilder);
    }

    /**
     * 保存合并评审项目
     *
     * @param signId
     * @param mergeIds
     * @param mergeType
     * @return
     */
    @Override
    public ResultMsg mergeSign(String signId, String mergeIds, String mergeType) {
        if (!Validate.isString(signId)) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取主项目信息！");
        }
        if (!Validate.isString(mergeIds)) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取合并评审项目信息！");
        }
        if (!Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType) && !Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，参数异常，请联系管理员查看！");
        }
        List<String> mergeSignIdList = StringUtil.getSplit(mergeIds, ",");
        List<SignMerge> saveList = new ArrayList<>(mergeSignIdList.size());
        for (String mergeId : mergeSignIdList) {
            SignMerge signMerge = new SignMerge();
            signMerge.setSignId(signId);
            signMerge.setMergeId(mergeId);
            signMerge.setMergeType(mergeType);
            Date now = new Date();
            signMerge.setCreatedBy(SessionUtil.getLoginName());
            signMerge.setModifiedBy(SessionUtil.getLoginName());
            signMerge.setCreatedDate(now);
            signMerge.setModifiedDate(now);
            saveList.add(signMerge);
        }
        signMergeRepo.bathUpdate(saveList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 取消合并评审项目
     *
     * @param signId
     * @param cancelIds
     * @param mergeType
     * @return
     */
    @Override
    public ResultMsg cancelMergeSign(String signId, String cancelIds, String mergeType) {
        if (!Validate.isString(signId)) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取主项目信息！");
        }
        if (!Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType) && !Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，参数异常，请联系管理员查看！");
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from " + SignMerge.class.getSimpleName() + " where ");
        hqlBuilder.append(SignMerge_.signId.getName() + " =:signId ").setParam("signId", signId);
        hqlBuilder.append(" and " + SignMerge_.mergeType.getName() + " =:mergeType ").setParam("mergeType", mergeType);
        //如果有解除删除，则解除相应的项目，否则解除所有
        if (Validate.isString(cancelIds)) {
            hqlBuilder.bulidPropotyString("and", SignMerge_.mergeId.getName(), cancelIds);
        }

        signMergeRepo.executeHql(hqlBuilder);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

}
