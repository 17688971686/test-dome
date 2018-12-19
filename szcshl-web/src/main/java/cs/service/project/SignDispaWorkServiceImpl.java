package cs.service.project;

import cs.ahelper.projhelper.ProjUtil;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.SysConstants;
import cs.common.utils.*;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.flow.RuProcessTask;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.*;
import cs.domain.sys.OrgDept;
import cs.domain.sys.OrgDept_;
import cs.domain.sys.Workday;
import cs.model.PageModelDto;
import cs.model.project.Achievement;
import cs.model.project.AchievementSumDto;
import cs.model.project.SignDispaWorkDto;
import cs.quartz.unit.QuartzUnit;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.service.sys.UserService;
import cs.service.sys.WorkdayService;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cs.common.constants.FlowConstant.FLOW_SIGN_FW;
import static cs.common.constants.SysConstants.SEPARATE_COMMA;

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
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrgDeptRepo orgDeptRepo;

    /**
     * 查询个人经办项目
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
        criteria.add(Restrictions.or(Restrictions.eq(SignDispaWork_.mUserId.getName(), SessionUtil.getUserId()), Restrictions.like(SignDispaWork_.aUserID.getName(), "%" + SessionUtil.getUserId() + "%")));
        criteria.add(Restrictions.ne(SignDispaWork_.signState.getName(), Constant.EnumState.DELETE.getValue()));
        /*   criteria.add(Restrictions.like(SignDispaWork_.aUserID.getName() ,"%"+SessionUtil.getUserId()+"%"));*/
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
        pageModelDto.setValue(Validate.isList(signDispaWorkList) ? signDispaWorkList : new ArrayList<>());
        return pageModelDto;
    }

    /**
     * 项目综合查询
     *
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SignDispaWorkDto> getCommQurySign(ODataObj odataObj) {
        PageModelDto<SignDispaWorkDto> pageModelDto = new PageModelDto<SignDispaWorkDto>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        Integer processState = null;
        String dispatchType = "", signDateValue = "";
        if (Validate.isList(odataObj.getFilter())) {
            Object value;
            for (ODataFilterItem item : odataObj.getFilter()) {
                value = item.getValue();
                if (null == value) {
                    continue;
                }
                //项目状态查询修改
                if (SignDispaWork_.processState.getName().equals(item.getField())) {
                    processState = Integer.parseInt(item.getValue().toString());
                    switch (processState) {
                        case 1:
                        case 2:
                            //在办项目或者暂停项目
                            criteria.add(Restrictions.eq(SignDispaWork_.signState.getName(), String.valueOf(processState)));
                            criteria.add(Restrictions.le(SignDispaWork_.processState.getName(), Constant.SignProcessState.END_WP.getValue()));
                            break;
                        case 17:
                            //未发送存档
                            criteria.add(Restrictions.ge(SignDispaWork_.processState.getName(), Constant.SignProcessState.IS_START.getValue()));
                            criteria.add(Restrictions.le(SignDispaWork_.processState.getName(), Constant.SignProcessState.SEND_CW.getValue()));
                            break;
                        case 68:
                            //已发文未存档
                            criteria.add(Restrictions.or(Restrictions.eq(SignDispaWork_.processState.getName(), Constant.SignProcessState.END_DIS_NUM.getValue()),
                                    Restrictions.eq(SignDispaWork_.processState.getName(), Constant.SignProcessState.SEND_CW.getValue()),
                                    Restrictions.eq(SignDispaWork_.processState.getName(), Constant.SignProcessState.SEND_FILE.getValue())));
                            break;
                        case 69:
                            //已发文项目
                            criteria.add(Restrictions.ge(SignDispaWork_.processState.getName(), Constant.SignProcessState.END_DIS_NUM.getValue()));
                            break;
                        case 89:
                            //已发送存档
                            criteria.add(Restrictions.ge(SignDispaWork_.processState.getName(), Constant.SignProcessState.SEND_FILE.getValue()));
                            break;
                        case 24:
                            //曾经暂停
                            criteria.add(Restrictions.eq(SignDispaWork_.isProjectStop.getName(), Constant.EnumState.YES.getValue()));
                            criteria.add(Restrictions.eq(SignDispaWork_.signState.getName(), Constant.EnumState.PROCESS.getValue()));
                            break;
                        case 8:
                            criteria.add(Restrictions.eq(SignDispaWork_.processState.getName(), Constant.SignProcessState.SEND_FILE.getValue()));
                            break;
                        case 9:
                            criteria.add(Restrictions.eq(SignDispaWork_.processState.getName(), Constant.SignProcessState.FINISH.getValue()));
                            break;
                            default:
                                ;
                    }
                    continue;
                }

                //项目发文
                if (SignDispaWork_.dispatchType.getName().equals(item.getField())) {
                    dispatchType = item.getValue().toString();
                    if ("非暂不实施项目".equals(dispatchType)) {
                        //非暂不实施项目=项目发文+退文
                        criteria.add(Restrictions.or(Restrictions.eq(SignDispaWork_.dispatchType.getName(), "项目发文"),
                                Restrictions.eq(SignDispaWork_.dispatchType.getName(), "项目退文")));
                    } else if ("非退文项目".equals(dispatchType)) {
                        //非退文项目=暂不实施+项目发文
                        criteria.add(Restrictions.or(Restrictions.eq(SignDispaWork_.dispatchType.getName(), "项目发文"),
                                Restrictions.eq(SignDispaWork_.dispatchType.getName(), "暂不实施")));
                    } else {
                        criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(), value));
                    }
                    continue;
                }
                //提前介入
                if (Sign_.isAdvanced.getName().equals(item.getField())) {
                    if (!Constant.EnumState.YES.getValue().equals(value.toString())) {
                        Disjunction disj = Restrictions.disjunction();
                        disj.add(Restrictions.isNull(Sign_.isAdvanced.getName()));
                        disj.add(Restrictions.ne(Sign_.isAdvanced.getName(), Constant.EnumState.YES.getValue()));
                        criteria.add(disj);
                        continue;
                    }
                }
                //办理进度
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(), value));
            }
        }
        criteria.addOrder(Order.desc(SignDispaWork_.signdate.getName()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<SignDispaWork> signDispaWork = criteria.list();
        List<SignDispaWorkDto> resultList = new ArrayList<>();
        signDispaWork.forEach(sd -> {
            SignDispaWorkDto signDispaWorkDto = new SignDispaWorkDto();
            BeanCopierUtils.copyProperties(sd, signDispaWorkDto);
            resultList.add(signDispaWorkDto);
        });
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(resultList);
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
        hqlBuilder.append(" and sd." + SignDispaWork_.signid.getName() + " not in ( select " + SignMerge_.mergeId.getName());
        hqlBuilder.append(" from " + SignMerge.class.getSimpleName() + " where  " + SignMerge_.mergeType.getName() + " =:mergeType ) ");
        hqlBuilder.setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        hqlBuilder.append(" and sd." + SignDispaWork_.signid.getName() + " not in ( select distinct " + SignMerge_.signId.getName());
        hqlBuilder.append(" from " + SignMerge.class.getSimpleName() + " where  " + SignMerge_.mergeType.getName() + " =:mergeType2 )");
        hqlBuilder.setParam("mergeType2", Constant.MergeType.WORK_PROGRAM.getValue());
        //排除有分支的项目(合并评审的项目一般只有一个分支)
        hqlBuilder.append(" and (select count(sh." + SignBranch_.signId.getName() + ") from " + SignBranch.class.getSimpleName() + " sh where sh." + SignBranch_.signId.getName() + " = sd." + SignDispaWork_.signid.getName() + ") = 1");

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
     * 获取待合并发文的项目,正在做发文项目
     * (正在运行，没有生成发文编号的项目)
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDispaWork> unMergeDISSign(String signId) {
        SignDispaWork mergeSign = signDispaWorkRepo.findById(signId);
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select sd from " + SignDispaWork.class.getSimpleName() + " sd , ");
        hqlBuilder.append(RuProcessTask.class.getSimpleName() + " rt where sd.signid = rt.businessKey ");
        //正式签收
        hqlBuilder.append(" and sd.issign = :signState ");
        hqlBuilder.setParam("signState", Constant.EnumState.YES.getValue(), StringType.INSTANCE);
        //排除本身
        hqlBuilder.append(" and sd.signid != :selfId ");
        hqlBuilder.setParam("selfId", signId, StringType.INSTANCE);
        //排除已经合并发文的项目
        hqlBuilder.append(" and sd.signid not in (select mergeId from " + SignMerge.class.getSimpleName() + " where mergeType = :mergeType1 ) ");
        hqlBuilder.setParam("mergeType1", Constant.MergeType.DIS_MERGE.getValue());
        hqlBuilder.append(" and sd.signid not in (select signId from " + SignMerge.class.getSimpleName() + " where mergeType = :mergeType2 ) ");
        hqlBuilder.setParam("mergeType2", Constant.MergeType.DIS_MERGE.getValue());
        //正在做发文项目
        hqlBuilder.append(" and rt.nodeDefineKey = :nodeKey and rt.processState = :processState ");
        hqlBuilder.setParam("nodeKey", FLOW_SIGN_FW, StringType.INSTANCE);
        hqlBuilder.setParam("processState", "1", StringType.INSTANCE);
        //只能关联同部门的项目
        hqlBuilder.append(" and sd.mOrgId = :mainOrgId ");
        hqlBuilder.setParam("mainOrgId", mergeSign.getmOrgId());
        //未生成发文编号的项目
        hqlBuilder.append(" and (sd.dfilenum is null or sd.dfilenum = '') ");

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
        String createUserName = SessionUtil.getLoginName();
        List<String> mergeSignIdList = StringUtil.getSplit(mergeIds, SEPARATE_COMMA);
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
            if (Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType)) {
                if (signBranchRepo.allAssistCount(mergeId) > 0) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，选择的项目存在多个部门办理工作方案，不符合合并评审要求，请刷新重新选择！");
                }
            }
            //如果已经合并的，也不能再次合并
            if (signMergeRepo.checkIsMerege(mergeId, mergeType) || signMergeRepo.isHaveMerge(mergeId, mergeType)) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，选择的项目已经是合并项目，不能再次合并，请刷新选择！");
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
        hqlBuilder.append("select * from SIGN_DISP_WORK  ");
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
     *
     * @param queryData
     * @param page
     * @return
     */
    @Override
    public List<SignDispaWork> queryStatistics(String queryData, int page) {
        return signDispaWorkRepo.queryStatistics(queryData, page);
    }

    /**
     * 通过业务id，判断当前用户是否有权限查看项目详情----用于秘密项目
     *
     * @param signId
     * @return
     */
    @Override
    public ResultMsg findSecretProPermission(String signId) {
        return signDispaWorkRepo.findSecretProPermission(signId);
    }

    /**
     * 超级管理员修改收文日期，重新计算剩余工作日
     *
     * @param oldSignDate
     * @param signDate
     * @return
     */
    @Override
    public ResultMsg countWeekDays(Date oldSignDate, Date signDate) {
        Long countDay = 0L;
        if (DateUtils.compareIgnoreSecond(oldSignDate, signDate) < 0) {
            List<Workday> workdayList = workdayService.getBetweenTimeDay(oldSignDate, signDate);
            countDay = (long) countBetweentDay(workdayList, oldSignDate, signDate);
        } else {
            List<Workday> workdayList = workdayService.getBetweenTimeDay(signDate, oldSignDate);
            countDay = -(long) countBetweentDay(workdayList, signDate, oldSignDate);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功", countDay);
    }

    /**
     * 部门业绩统计信息
     *
     * @param achievementSumDto
     * @return
     */
    @Override
    public Map<String, Object> countAchievementSum(AchievementSumDto achievementSumDto) {
        Map<String, Object> resultMap = new HashMap<>();
        //当前用户级别,默认为普通用户
        int level = 0;
        //当前用户ID
        String userId = SessionUtil.getUserId();
        //分管组织部门列表
        List<OrgDept> orgDeptList = new ArrayList<>();
        //一、查询用户等级
        Map<String, Object> levelMap = userService.getUserLevel();
        if (Validate.isMap(levelMap) && null != levelMap.get("leaderFlag")) {
            level = Integer.parseInt(levelMap.get("leaderFlag").toString());
            resultMap.put("level", level);
            if (level > 0) {
                Criteria criteria = orgDeptRepo.getExecutableCriteria();
                switch (level) {
                    case 1:
                        break;
                    case 2:
                        criteria.add(Restrictions.eq(OrgDept_.sLeaderID.getName(), SessionUtil.getUserId()));
                        break;
                    case 3:
                    case 4:
                        criteria.add(Restrictions.eq(OrgDept_.directorID.getName(), SessionUtil.getUserId()));
                        break;
                    default:
                        ;
                }
                //加上排序
                criteria.addOrder(Order.asc(OrgDept_.sort.getName()));

                orgDeptList = criteria.list();
                if(Validate.isList(orgDeptList)){
                    if(Validate.isString(achievementSumDto.getDeptIds())){
                        //如果参数有部门ID，则只查询对应部门列表
                        List<OrgDept> newOrgDeptList = new ArrayList<>();
                        List<String> ids = StringUtil.getSplit(achievementSumDto.getDeptIds(),SysConstants.SEPARATE_COMMA);
                        for(OrgDept orgDept : orgDeptList){
                            for(String id : ids){
                                if(id.equals(orgDept.getId())){
                                    newOrgDeptList.add(orgDept);
                                }
                            }
                        }
                        orgDeptList = newOrgDeptList;
                    }else{
                        //参数没有部门ID，则初始化ID
                        StringBuffer orgIdString = new StringBuffer();
                        for (int i = 0, l = orgDeptList.size(); i < l; i++) {
                            if (i > 0) {
                                orgIdString.append(SysConstants.SEPARATE_COMMA);
                            }
                            OrgDept orgDept = orgDeptList.get(i);
                            orgIdString.append(orgDept.getId());
                        }
                        achievementSumDto.setDeptIds(orgIdString.toString());
                    }
                }
                resultMap.put("orgDeptList", orgDeptList);

            }
        }
        //二、查询业绩统计信息
        List<Achievement> countList = signDispaWorkRepo.countAchievement(achievementSumDto.getYear(), achievementSumDto.getQuarter(), achievementSumDto.getDeptIds(), userId, level);
        //三、统计，
        countAchievementDetail(resultMap,level,countList,orgDeptList);
        return resultMap;
    }

    /**
     * 对查询出来的业绩信息进行统计
     * @param resultMap
     * @param level
     * @param countList
     * @param orgDeptList
     */
    @Override
    public void countAchievementDetail(Map<String, Object> resultMap,int level,List<Achievement> countList,List<OrgDept> orgDeptList) {
        //分3个档次统计
        switch (level) {
            //1、主任和分管主任统计，按分管部门统计
            case 1:
            case 2:
                Map<String,AchievementSumDto> orgDeptCount = ProjUtil.countOrgDept(countList,orgDeptList);
                List<Achievement> deptDetailList = ProjUtil.orgDeptDetail(countList,orgDeptList,orgDeptCount);
                resultMap.put("orgDeptCount",orgDeptCount);
                resultMap.put("orgDeptDetailList",deptDetailList);
                break;
            //、部长和组长统计，统计一个部门
            case 3:
            case 4:
                resultMap.put("orgDeptSum",ProjUtil.sumOrgDeptAchievement(countList,orgDeptList));
                break;
            //普通用户统计
            case 0:
                resultMap.put("userSum",ProjUtil.sumUserAchievement(countList));
                break;
            default:
                ;
        }
    }

    private int countBetweentDay(List<Workday> workdayList, Date beginDate, Date endDate) {
        int result = 0;
        int countDay = (int) DateUtils.daysBetween(beginDate, endDate);
        result = countDay;
        if (Validate.isList(workdayList)) {
            Date date = beginDate;
            for (int i = 0; i < countDay; i++) {
                Workday workday = workdayList.get(i);
                if (DateUtils.compareIgnoreSecond(date, workday.getDates()) == 0) {
                    //将工作日改为休息日
                    if (!QuartzUnit.isWeekend(workday.getDates()) && "1".equals(workday.getStatus())) {
                        result--;
                        //将休息日改成工作日的
                    } else if (QuartzUnit.isWeekend(workday.getDates()) && "2".equals(workday.getStatus())) {
                        result++;
                    }
                    break;
                }
            }
        } else {
            Date date = beginDate;
            for (int i = 0; i < countDay; i++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                        || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    result--;
                }
                cal.add(Calendar.DATE, 1);
                date = cal.getTime();
            }
        }
        return result;
    }

}
