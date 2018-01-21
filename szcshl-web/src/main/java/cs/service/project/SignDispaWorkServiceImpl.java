package cs.service.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelected;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.*;
import cs.model.PageModelDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 项目信息视图 Service
 *
 * @author ldm
 */
@Service
public class SignDispaWorkServiceImpl implements SignDispaWorkService {
    private static Logger log = Logger.getLogger(SignDispaWorkServiceImpl.class);

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private DispatchDocRepo dispatchDocRepo;
    @Autowired
    private SignBranchRepo signBranchRepo;

    /**
     * 查询个人办理项目
     *
     * @param oDataObj
     * @param isMianUser
     * @return
     */
    @Override
    public PageModelDto<SignDispaWork> findMyDoProject(ODataObj oDataObj, boolean isMianUser) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.or(Restrictions.eq(SignDispaWork_.mUserId.getName(), SessionUtil.getUserId()), Restrictions.like(SignDispaWork_.aUserID.getName(), SessionUtil.getUserId())));
        criteria.add(Restrictions.or(Restrictions.ne(SignDispaWork_.signState.getName(),Constant.EnumState.DELETE.getValue())));

        //criteria.add(Restrictions.like(SignDispaWork_.aUserID.getName() , SessionUtil.getUserId()));
        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if (oDataObj.getSkip() > 0) {
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if (oDataObj.getTop() > 0) {
            criteria.setMaxResults(oDataObj.getTop());
        }

        //处理orderby
        if (Validate.isString(oDataObj.getOrderby())) {
            if (oDataObj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
            }
        }

        List<SignDispaWork> signDispaWorkList = criteria.list();

        pageModelDto.setValue(signDispaWorkList);


        return pageModelDto;
    }

    /**
     * 项目综合查询
     *
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SignDispaWork> getCommQurySign(ODataObj odataObj) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<SignDispaWork>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);

       /* //以下对秘密项目进行限制查看，只有项目负责人、主负责人的部门领导，分管领导、主任可查看
       List<ODataFilterItem> oDataFilterItemList = odataObj.getFilter();
       if(oDataFilterItemList != null &&oDataFilterItemList.size() > 0){
           for(ODataFilterItem oDataFilterItem : oDataFilterItemList){
               if("secrectlevel".equals(oDataFilterItem.getField()) && "秘密".equals(oDataFilterItem.getValue().toString())){
                   //部门负责人
                   if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){

                       criteria.add(Restrictions.eq(SignDispaWork_.ministerName.getName() , SessionUtil.getDisplayName()));
                   }
                   //分管领导
                   else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
                       criteria.add(Restrictions.eq(SignDispaWork_.leaderName.getName() , SessionUtil.getDisplayName()));
                   }
                   //主任
                   else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){

                   }else{
                       //项目负责人
                       criteria.add(Restrictions.like(SignDispaWork_.allPriUser.getName() ,"%" + SessionUtil.getDisplayName() + "%"));
                   }
               }
           }
       }*/

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
     * 同时过滤有分支的项目
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDispaWork> unMergeWPSign(String signId) {
        SignDispaWork mergeSign = signDispaWorkRepo.findById(signId);
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " sd ");
        //已经完成工作方案，但是未评审的项目
        hqlBuilder.append(" where sd." + SignDispaWork_.processState.getName() + " >=:processState1 and sd." + SignDispaWork_.processState.getName() + " <:processState2 ");
        hqlBuilder.setParam("processState1", Constant.SignProcessState.DO_WP.getValue(), IntegerType.INSTANCE);
        hqlBuilder.setParam("processState2", Constant.SignProcessState.END_WP.getValue(), IntegerType.INSTANCE);
        //只能关联同部门的项目
        hqlBuilder.append(" and sd." + SignDispaWork_.mOrgId.getName() + " = :mainOrgId ");
        hqlBuilder.setParam("mainOrgId", mergeSign.getmOrgId());
        //排除自身
        hqlBuilder.append(" and sd." + SignDispaWork_.signid.getName() + " != :self ").setParam("self", signId);
        //排除已关联的项目(不管是主项目还是次项目)
        hqlBuilder.append(" and sd." + SignDispaWork_.signid.getName() + " not in ( select " + SignMerge_.mergeId.getName() );
        hqlBuilder.append(" from " + SignMerge.class.getSimpleName() +" where  " + SignMerge_.mergeType.getName() + " =:mergeType ) ");
        hqlBuilder.setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        hqlBuilder.append(" and sd." + SignDispaWork_.signid.getName() + " not in ( select distinct " + SignMerge_.signId.getName() );
        hqlBuilder.append(" from " + SignMerge.class.getSimpleName() +" where  " + SignMerge_.mergeType.getName() + " =:mergeType2 )");
        hqlBuilder.setParam("mergeType2", Constant.MergeType.WORK_PROGRAM.getValue());
        //排除有分支的项目(合并评审的项目一般只有一个分支)
        hqlBuilder.append(" and (select count(sh." + SignBranch_.signId.getName() + ") from " + SignBranch.class.getSimpleName() + " sh where sh." + SignBranch_.signId.getName() + " = sd."+SignDispaWork_.signid.getName()+") = 1");

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
        SignDispaWork mergeSign = signDispaWorkRepo.findById(signId);
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where ");
        hqlBuilder.append(SignDispaWork_.processState.getName() + " > :processState1  and " + SignDispaWork_.processState.getName() + " <= :processState2 ");
        hqlBuilder.setParam("processState1", Constant.SignProcessState.DO_WP.getValue(), IntegerType.INSTANCE);
        hqlBuilder.setParam("processState2", Constant.SignProcessState.END_DIS_NUM.getValue(), IntegerType.INSTANCE);
        //只能关联同部门的项目
        hqlBuilder.append("and " + SignDispaWork_.mOrgId.getName() + " = :mainOrgId ");
        hqlBuilder.setParam("mainOrgId", mergeSign.getmOrgId());
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
    @Transactional
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
        Date now = new Date();
        String createUserName = SessionUtil.getDisplayName();
        List<String> mergeSignIdList = StringUtil.getSplit(mergeIds, ",");
        List<SignMerge> saveList = new ArrayList<>(mergeSignIdList.size());
        for (String mergeId : mergeSignIdList) {
            SignMerge signMerge = new SignMerge();
            signMerge.setSignId(signId);
            signMerge.setMergeId(mergeId);
            signMerge.setMergeType(mergeType);
            signMerge.setCreatedBy(createUserName);
            signMerge.setModifiedBy(createUserName);
            signMerge.setCreatedDate(now);
            signMerge.setModifiedDate(now);
            //如果是合并评审，要再次确认是否有其他分支，如果有其他分支，则不允许保存
            if(Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType)){
                if(signBranchRepo.allAssistCount(mergeId) > 0){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，选择的项目存在多个部门办理工作方案，不符合合并评审要求，请刷新重新选择！");
                }
            }
            //如果已经合并的，也不能再次合并
            if(signMergeRepo.checkIsMerege(mergeId,mergeType) || signMergeRepo.isHaveMerge(mergeId,mergeType)){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，选择的项目已经是合并项目，不能再次合并，请刷新选择！");
            }
            saveList.add(signMerge);
        }
        signMergeRepo.bathUpdate(saveList);

        //如果是合并评审，还要删除之前的评审方案和预定的会议室信息
        if (Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType)) {
            //获取所有合并评审方案信息
            List<ExpertReview> reviewList = expertReviewRepo.findByIds(ExpertReview_.businessId.getName(), mergeIds, null);
            if (Validate.isList(reviewList)) {
                for (ExpertReview er : reviewList) {
                    expertReviewRepo.delete(er);        //删除评审方案，顺便删除抽取专家信息(级联删除)
                }
            }
            //删除会议室信息
            List<WorkProgram> workProgramList = workProgramRepo.findByIds("signid", mergeIds, null);
            if (Validate.isList(workProgramList)) {
                StringBuffer removeIds = new StringBuffer();
                for (int i = 0, l = workProgramList.size(); i < l; i++) {
                    WorkProgram wp = workProgramList.get(i);
                    if (i > 0) {
                        removeIds.append(",");
                    }
                    removeIds.append(wp.getId());
                }
                roomBookingRepo.deleteById(RoomBooking_.businessId.getName(), removeIds.toString());
            }
            //把所有被合并的项目改为合并评审次项目，同时评审方式也要跟主项目一致
            workProgramRepo.updateWPReivewType(signId, Constant.MergeType.REVIEW_MERGE.getValue(), Constant.EnumState.NO.getValue(), mergeIds);
        } else if (Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
            dispatchDocRepo.updateRWType(Constant.MergeType.DIS_MERGE.getValue(), Constant.EnumState.NO.getValue(), mergeIds);
        }
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
    @Transactional
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
        //如果有解除删除，则解除相应的项目，否则解除所有,把所有被合并的项目改为单个评审
        if (Validate.isString(cancelIds)) {
            hqlBuilder.bulidPropotyString("and", SignMerge_.mergeId.getName(), cancelIds);
            if (Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType)) {
                workProgramRepo.updateWPReivewType(signId, Constant.MergeType.REVIEW_SIGNLE.getValue(), null, cancelIds);
            } else if (Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
                dispatchDocRepo.updateRWType(Constant.MergeType.DIS_SINGLE.getValue(), null, cancelIds);
            }
        }

        signMergeRepo.executeHql(hqlBuilder);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 根据主项目ID，删除所有的合并项目
     *
     * @param signId
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteAllMerge(String signId, String mergeType, String businessId) {
        List<SignMerge> mergeSignList = signMergeRepo.findByIds(SignMerge_.signId.getName(), signId, null);
        if (Validate.isList(mergeSignList)) {
            StringBuffer sbString = new StringBuffer();
            for (int i = 0, l = mergeSignList.size(); i < l; i++) {
                SignMerge sm = mergeSignList.get(i);
                if (i > 0) {
                    sbString.append(",");
                }
                sbString.append(sm.getMergeId());
            }
            signMergeRepo.deleteById(SignMerge_.mergeId.getName(), sbString.toString());

            //修改合并评审项目
            if (Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType)) {
                workProgramRepo.updateWPReivewType(signId, Constant.MergeType.REVIEW_SIGNLE.getValue(), null, sbString.toString());
            } else if (Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
                dispatchDocRepo.updateRWType(Constant.MergeType.DIS_SINGLE.getValue(), null, sbString.toString());
            }
        }
        //修改自身状态
        if (Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType)) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" update " + WorkProgram.class.getSimpleName() + " set " + WorkProgram_.isSigle.getName() + " =:isSigle ");
            hqlBuilder.setParam("isSigle", Constant.MergeType.REVIEW_SIGNLE.getValue());
            hqlBuilder.append(" ," + WorkProgram_.isMainProject.getName() + " =:isMainProject ");
            hqlBuilder.setParam("isMainProject", Constant.EnumState.NO.getValue());
            hqlBuilder.append(" where " + WorkProgram_.id.getName() + " =:id ");
            hqlBuilder.setParam("id", businessId);
            workProgramRepo.executeHql(hqlBuilder);
        } else if (Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" update " + DispatchDoc.class.getSimpleName() + " set " + DispatchDoc_.dispatchWay.getName() + " =:dispatchWay ");
            hqlBuilder.setParam("dispatchWay", Constant.MergeType.DIS_SINGLE.getValue());
            hqlBuilder.append(" ," + DispatchDoc_.isMainProject.getName() + " =:isMainProject ");
            hqlBuilder.setParam("isMainProject", Constant.EnumState.NO.getValue());
            hqlBuilder.append(" where " + DispatchDoc_.id.getName() + " =:id ");
            hqlBuilder.setParam("id", businessId);
            dispatchDocRepo.executeHql(hqlBuilder);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    @Override
    public List<SignDispaWork> getSignDispaWork(String filters) {

        String[] filterArr = filters.split(",");
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select * from V_SIGN_DISP_WORK  ");
        if (filterArr.length > 0 && !"".equals(filterArr[0])) {
            hqlBuilder.append(" where ");
            for (int i = 0; i < filterArr.length; i++) {
                String filter = filterArr[i];
                String[] params = filter.split(":");
                hqlBuilder.append(params[0].substring(1, params[0].length() - 1) + "=:" + params[0].substring(1, params[0].length() - 1));
                hqlBuilder.setParam(params[0].substring(1, params[0].length() - 1), params[1].substring(1, params[1].length() - 1));
                if (i < filterArr.length - 1) {
                    hqlBuilder.append(" and ");
                }
            }
        }

        List<SignDispaWork> signDispaWorkList = signDispaWorkRepo.findBySql(hqlBuilder);
        for (SignDispaWork s : signDispaWorkList) {
            s.setIsAppraising((Constant.EnumState.YES.getValue()).equals(s.getIsAppraising()) ? "是" : "否");
            s.setIsassistproc((Constant.EnumState.YES.getValue()).equals(s.getIsassistproc()) ? "是" : "否");
            s.setIsRelated((Constant.EnumState.YES.getValue()).equals(s.getIsRelated()) ? "是" : "否");
            s.setIshaveeia((Constant.EnumState.YES.getValue()).equals(s.getIshaveeia()) ? "是" : "否");
            s.setIsSupplementary((Constant.EnumState.YES.getValue()).equals(s.getIsSupplementary()) ? "是" : "否");
            s.setIsHaveSuppLetter((Constant.EnumState.YES.getValue()).equals(s.getIsHaveSuppLetter()) ? "是" : "否");
            s.setSignState((Constant.EnumState.STOP.getValue()).equals(s.getSignState()) ? "是" : "否");

        }
        return signDispaWorkList;
    }

    /**
     * 查询发文申请阶段，发放评审费超时的项目信息
     *
     * @return
     */
    @Override
    public PageModelDto<SignDispaWork> findOverSignDispaWork() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where " + SignDispaWork_.processState.getName() + "=:processState ");
        hqlBuilder.setParam("processState", Constant.SignProcessState.DO_DIS.getValue().toString());
        List<SignDispaWork> signDispaWorkList = signDispaWorkRepo.findByHql(hqlBuilder);
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        List<SignDispaWork> resoultList = new ArrayList<>();
        for (SignDispaWork s : signDispaWorkList) {
            HqlBuilder hql = HqlBuilder.create();
            hql.append(" from " + ExpertReview.class.getSimpleName() + " where " + ExpertReview_.businessId.getName() + "=:businessId");
            hql.append(" and " + ExpertReview_.reviewDate.getName() + " is not null ");
            hql.append(" and " + ExpertReview_.payDate.getName() + " is null");
//            hql.append(" and " + ExpertReview_.state.getName() + "<>:state or " + ExpertReview_.state.getName() + " is null");
            hql.setParam("businessId", s.getSignid());
//            hql.setParam("state" , Constant.EnumState.YES.getValue());
            List<ExpertReview> expertReviewList = expertReviewRepo.findByHql(hql);
            if (expertReviewList.size() > 0) {
                for (ExpertReview er : expertReviewList) {
                    if (DateUtils.daysBetween(er.getReviewDate(), new Date()) > 1) {
                        resoultList.add(s);
                    }
                }
            }
        }
        pageModelDto.setValue(resoultList);
        pageModelDto.setCount(resoultList.size());
        return pageModelDto;
    }

    /**
     * 通过时间段 获取项目信息（按评审阶段分组），用于项目查询统计分析
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public ResultMsg findByTime(String startTime, String endTime) {
        return signDispaWorkRepo.findByTime(startTime, endTime);
    }

    /**
     * 通过评审阶段，项目类别，统计项目信息
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public ResultMsg findByTypeAndReview(String startTime, String endTime) {
        return signDispaWorkRepo.findByTypeAndReview(startTime, endTime);
    }

    /**
     * 通过条件查询统计
     * @param queryData
     * @param page
     * @return
     */
    @Override
    public List<SignDispaWork> queryStatistics(String  queryData, int page) {
        return signDispaWorkRepo.queryStatistics(queryData , page);
    }
    /**
     * 通过业务id，判断当前用户是否有权限查看项目详情----用于秘密项目
     * @param signId
     * @return
     */
    @Override
    public ResultMsg findSecretProPermission(String signId) {
        return signDispaWorkRepo.findSecretProPermission(signId);
    }

}
