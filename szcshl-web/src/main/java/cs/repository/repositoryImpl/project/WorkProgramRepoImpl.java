package cs.repository.repositoryImpl.project;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.utils.*;
import cs.domain.expert.ExpertSelected_;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.*;
import cs.model.expert.ExpertSelectedDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.ProMeetDto;
import cs.model.project.WorkProgramDto;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.expert.ExpertNewInfoRepo;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class WorkProgramRepoImpl extends AbstractRepository<WorkProgram,String> implements WorkProgramRepo {

    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private ExpertNewInfoRepo expertNewInfoRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;

    /**
     * 根据收文编号，查询对应工作方案
     *
     * @param signId
     * @return
     */
    @Override
    public WorkProgram findByPrincipalUser(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.createAlias(WorkProgram_.sign.getName(), WorkProgram_.sign.getName());
        criteria.add(Restrictions.eq(WorkProgram_.sign.getName() + "." + Sign_.signid.getName(), signId));
        criteria.add(Restrictions.sqlRestriction(" branchId = (select flowBranch from cs_sign_principal2 where signId ='" + signId + "' and userId ='" + SessionUtil.getUserInfo().getId() + "' )"));
        return (WorkProgram) criteria.uniqueResult();
    }


    /**
     * 根据收文ID和分支ID查询对应的工作方案
     *
     * @param signId
     * @param branchId
     * @param isBaseInfo
     * @return
     */
    @Override
    public WorkProgram findBySignIdAndBranchId(String signId, String branchId, boolean isBaseInfo) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + WorkProgram.class.getSimpleName() + " where " + WorkProgram_.sign.getName() + "." + Sign_.signid.getName() + " =:signId ");
        hqlBuilder.setParam("signId", signId);
        hqlBuilder.append(" and " + WorkProgram_.branchId.getName() + " =:branchId ").setParam("branchId", branchId);
        if(isBaseInfo){
            hqlBuilder.append(" and " + WorkProgram_.baseInfo.getName() + " =:wpstate ").setParam("wpstate", Constant.EnumState.YES.getValue());
        }
        List<WorkProgram> wpList = findByHql(hqlBuilder);
        if (Validate.isList(wpList)) {
            return wpList.get(0);
        }
        return null;
    }

    /**
     * 初始化专家评审费，默认每个专家1000元(未评审的情况下才能改)
     *
     * @param id
     */
    @Override
    public void initExpertCost(String id) {
        //1、统计已经确认的专家个数
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_WORK_PROGRAM wp SET wp.expertCost = 1000 * (SELECT COUNT (ID) FROM cs_expert_selected es ");
        sqlBuilder.append(" WHERE es." + ExpertSelected_.isConfrim.getName() + " = :isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" AND es." + ExpertSelected_.isJoin.getName() + " = :isJoin ");
        sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" AND es." + ExpertSelected_.businessId.getName() + " = wp.id ) ");
        sqlBuilder.append(" WHERE wp.id = :id and (wp.ministerName is null or wp.ministerName = '') ");
        sqlBuilder.setParam("id", id);
        executeSql(sqlBuilder);
    }


    /**
     * 根据项目ID，更改合并评审状态
     *
     * @param isSigle
     * @param isMain
     * @param mergeIds
     */
    @Override
    public void updateWPReivewType(String signId, String isSigle, String isMain, String mergeIds) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_work_program set " + WorkProgram_.isSigle.getName() + " =:isSigle ");
        sqlBuilder.setParam("isSigle", isSigle);
        if (Validate.isString(isMain)) {
            sqlBuilder.append(" , " + WorkProgram_.isMainProject.getName() + " =:isMainProject ");
            sqlBuilder.setParam("isMainProject", isMain);
        } else {
            sqlBuilder.append(" , " + WorkProgram_.isMainProject.getName() + " = null ");
        }
        //如果是合并评审，则评审方式改为审主项目一致
        if (Constant.MergeType.REVIEW_MERGE.getValue().equals(isSigle)) {
            sqlBuilder.append("," + WorkProgram_.reviewType.getName() + " = (select wp.reviewType from cs_work_program wp where wp.signid =:signid and wp.branchId = :branchId ) ");
            sqlBuilder.setParam("signid", signId);
            sqlBuilder.setParam("branchId", FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());

            //单个评审或者单个发文，还要删除合并表对应的记录
        } else if (Constant.MergeType.REVIEW_SIGNLE.getValue().equals(isSigle) || Constant.MergeType.DIS_SINGLE.getValue().equals(isSigle)) {
            HqlBuilder sqlBuilder1 = HqlBuilder.create();
            sqlBuilder1.append("delete from CS_SIGN_MERGE ");
            sqlBuilder1.bulidPropotyString("where", SignMerge_.mergeId.getName(), mergeIds);
            signMergeRepo.executeSql(sqlBuilder1);
        }
        sqlBuilder.bulidPropotyString("where", "signid", mergeIds);
        executeSql(sqlBuilder);
    }

    /**
     * 根据项目ID获取合并评审主工作方案信息
     *
     * @param signId
     * @return
     */
    @Override
    public WorkProgram findMainReviewWP(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(" signid = (SELECT signid FROM CS_SIGN_MERGE where mergeid = '" + signId + "' and mergeType = '" + Constant.MergeType.WORK_PROGRAM.getValue() + "')"));
        criteria.add(Restrictions.eq(WorkProgram_.branchId.getName(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue()));
        List<WorkProgram> wpList = criteria.list();
        if (Validate.isList(wpList)) {
            return wpList.get(0);
        }

        return null;
    }

    @Override
    public void initWPMeetingExp(WorkProgramDto workProgramDto, WorkProgram wp) {
        if (wp != null) {
            //1、初始化会议室预定情况
            List<RoomBooking> roomBookings = roomBookingRepo.findByIds(RoomBooking_.businessId.getName(), wp.getId(), null);
            if (Validate.isList(roomBookings)) {
                List<RoomBookingDto> roomBookingDtos = new ArrayList<>(roomBookings.size());
                roomBookings.forEach(r -> {
                    RoomBookingDto rbDto = new RoomBookingDto();
                    BeanCopierUtils.copyProperties(r, rbDto);
                    rbDto.setBeginTimeStr(DateUtils.converToString(rbDto.getBeginTime(), "HH:mm"));
                    rbDto.setEndTimeStr(DateUtils.converToString(rbDto.getEndTime(), "HH:mm"));
                    roomBookingDtos.add(rbDto);
                });
                workProgramDto.setRoomBookingDtos(roomBookingDtos);
            }
            //2、拟聘请专家
            List<ExpertSelectedDto> expertSelectedDtoList = new ArrayList<>();
            expertSelectedDtoList = expertSelectedRepo.findByBusinessId(wp.getId());
            workProgramDto.setExpertSelectedDtoList(expertSelectedDtoList);
        }
    }

    /**
     * 根据合并评审主项目ID，获取合并评审次项目的工作方案信息
     *
     * @param signid
     * @return
     */
    @Override
    public List<WorkProgram> findMergeWP(String signid) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + WorkProgram.class.getSimpleName() + " where " + WorkProgram_.sign.getName() + "." + Sign_.signid.getName() + " in ");
        hqlBuilder.append(" (select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName() + " where ");
        hqlBuilder.append(SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signid).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());

        return findByHql(hqlBuilder);
    }

    /**
     * 根据ID判断是否是想验证的评审方式
     *
     * @param reviewType
     * @param workProgramId
     * @return
     */
    @Override
    public boolean checkReviewType(String reviewType, String workProgramId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(wp.id) from CS_WORK_PROGRAM wp where WP.ID = :workProgramId and WP.REVIEWTYPE = :reviewType ");
        sqlBuilder.setParam("workProgramId", workProgramId).setParam("reviewType", reviewType);
        return (returnIntBySql(sqlBuilder) > 0);
    }

    /**
     * 更新工作方案的拟补充资料函
     *
     * @param businessId
     * @param value
     * @param disapDate
     */
    @Override
    public void updateSuppLetterState(String businessId, String value, Date disapDate) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_work_program ");
        sqlBuilder.append(" set " + WorkProgram_.isHaveSuppLetter.getName() + " =:stateValue ").setParam("stateValue", value);
        sqlBuilder.append(" ," + WorkProgram_.suppLetterDate.getName() + " = to_date(:disapDate,'yyyy-mm-dd') ").setParam("disapDate", DateUtils.converToString(disapDate, "yyyy-MM-dd"));
        sqlBuilder.append(" where signid =:signid ").setParam("signid", businessId);
        sqlBuilder.append(" and " + WorkProgram_.isHaveSuppLetter.getName() + " is null or " + WorkProgram_.isHaveSuppLetter.getName() + " != '9'");
        executeSql(sqlBuilder);
    }


    /**
     * 通过业务ID判断是不是主分支
     *
     * @param signId
     * @return
     */
    @Override
    public Boolean mainBranch(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select id from cs_work_program where signid =:signid ").setParam("signid", signId);
        hqlBuilder.append(" and branchId = '1'");
        return this.executeSql(hqlBuilder) > 0 ? true : false;
    }

    /**
     * 获取工作方案调研及会议信息
     *
     * @return
     */
    @Override
    public List<ProMeetDto> findAmProMeetInfo() {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select a.*,row_number() over(partition by a.rbday order by a.rbday) innerSeq from  ( " +
                " select t.rbday,t.rbname,t.addressname from cs_room_booking t " +
                " where t.rbday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4)" +
                " and to_number(to_char(t.begintime,'hh24')) between 8 and 12 " +
                " and to_number(to_char(t.endtime,'hh24')) between 8 and 12 " +
                " union all " +
                " select t.rbday,t.rbname,t.addressname from cs_room_booking t " +
                " where t.rbday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4) " +
                " and to_number(to_char(t.begintime,'hh24')) between 8 and 12 " +
                " and to_number(to_char(t.endtime,'hh24')) > 12 " +
                "  ) a order by innerSeq,rbday ");
        List<Object[]> objList = getObjectArray(sqlBuilder);
        List<ProMeetDto> proAmMeetDtoList = new ArrayList<ProMeetDto>();
        for (int i = 0; i < objList.size(); i++) {
            ProMeetDto proMeetDto = new ProMeetDto();
            Object[] objRow = objList.get(i);
            if (null != objRow[0]) {
                proMeetDto.setProMeetDate((Date) objRow[0]);
            }

            if (null != objRow[1]) {
                proMeetDto.setRbName((String)objRow[1]);
            }

            if (null != objRow[2]) {
                proMeetDto.setAddressName((String)objRow[2]);
            }

            if (null != objRow[3]) {
                proMeetDto.setInnerSeq((BigDecimal) objRow[3]);
            }
            proAmMeetDtoList.add(proMeetDto);
        }


        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append(" select a.*,row_number() over(partition by a.studyallday order by a.studyallday) innerSeq from(" +
                " select t.studyallday,t.projectname from CS_WORK_PROGRAM t " +
                " where t.studyallday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4) " +
                " and to_number(to_char(t.studybegintime,'hh24')) between 8 and 12 " +
                " and to_number(to_char(t.studyendtime,'hh24')) between 8 and 12 " +
                " union all " +
                " select  t.studyallday,t.projectname from CS_WORK_PROGRAM t " +
                " where t.studyallday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4) " +
                " and ( (to_number(to_char(t.studybegintime,'hh24')) between 8 and 12 and to_number(to_char(t.studyendtime,'hh24')) > 12)" +
                " or (t.studyquantum='全天') ) " +
                " ) a order by innerSeq,studyallday");
        List<Object[]>objList2 = getObjectArray(sqlBuilder1);
        for (int i = 0; i < objList2.size(); i++) {
            ProMeetDto proMeetDto = new ProMeetDto();
            Object[] objRow = objList2.get(i);
            if (null != objRow[0]) {
                proMeetDto.setProMeetDate((Date) objRow[0]);
            }

            if (null != objRow[1]) {
                proMeetDto.setProName((String)objRow[1]);
            }

            if (null != objRow[2]) {
                proMeetDto.setInnerSeq((BigDecimal) objRow[2]);
            }
            proAmMeetDtoList.add(proMeetDto);
        }
        return proAmMeetDtoList;
    }

    /**
     * 获取工作方案调研及会议信息
     * @return
     */
    @Override
    public  List<ProMeetDto> findPmProMeetInfo(){
        HqlBuilder sqlBuilder = HqlBuilder.create();

        sqlBuilder.append(" select a.*,row_number() over(partition by a.rbday order by a.rbday) innerSeq from (" +
                " select t.rbday,t.rbname,t.addressname from cs_room_booking t " +
                " where t.rbday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4)" +
                " and to_number(to_char(t.begintime,'hh24')) > 12 " +
                " union all " +
                " select t.rbday,t.rbname,t.addressname from cs_room_booking t " +
                " where t.rbday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4) " +
                " and to_number(to_char(t.begintime,'hh24')) between 8 and 12 " +
                " and to_number(to_char(t.endtime,'hh24')) > 12 " +
                "  ) a  order by innerSeq,rbday");
        List<Object[]> objList = getObjectArray(sqlBuilder);
        List<ProMeetDto> proAmMeetDtoList = new ArrayList<ProMeetDto>();
        for (int i = 0; i < objList.size(); i++) {
            ProMeetDto proMeetDto = new ProMeetDto();
            Object[] objRow = objList.get(i);
            if (null != objRow[0]) {
                proMeetDto.setProMeetDate((Date) objRow[0]);
            }

            if (null != objRow[1]) {
                proMeetDto.setRbName((String)objRow[1]);
            }

            if (null != objRow[2]) {
                proMeetDto.setAddressName((String)objRow[2]);
            }

            if (null != objRow[3]) {
                proMeetDto.setInnerSeq((BigDecimal) objRow[3]);
            }
            proAmMeetDtoList.add(proMeetDto);
        }


        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append(" select a.*,row_number() over(partition by a.studyallday order by a.studyallday) innerSeq from( " +
                " select t.studyallday,t.projectname from CS_WORK_PROGRAM t " +
                " where t.studyallday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4) " +
                " and to_number(to_char(t.studybegintime,'hh24')) > 12 " +
                " union all " +
                " select  t.studyallday,t.projectname from CS_WORK_PROGRAM t " +
                " where t.studyallday between TRUNC(SYSDATE) and TRUNC(SYSDATE + 4) " +
                " and ( (to_number(to_char(t.studybegintime,'hh24')) between 8 and 12 and to_number(to_char(t.studyendtime,'hh24')) > 12)" +
                " or (t.studyquantum='全天') ) " +
                " ) a order by innerSeq,studyallday");
        List<Object[]>objList2 = getObjectArray(sqlBuilder1);
        for (int i = 0; i < objList2.size(); i++) {
            ProMeetDto proMeetDto = new ProMeetDto();
            Object[] objRow = objList2.get(i);
            if (null != objRow[0]) {
                proMeetDto.setProMeetDate((Date) objRow[0]);
            }

            if (null != objRow[1]) {
                proMeetDto.setProName((String)objRow[1]);
            }

            if (null != objRow[2]) {
                proMeetDto.setInnerSeq((BigDecimal) objRow[2]);
            }
            proAmMeetDtoList.add(proMeetDto);
        }
        return proAmMeetDtoList;
    }

}
