package cs.service.project;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.domain.meeting.RoomBooking_;
import cs.domain.sys.*;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.CreateTemplateUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.SysFileUtil;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.Expert_;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.AssistUnit;
import cs.domain.project.Sign;
import cs.domain.project.SignPrincipal;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.expert.ExpertDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import cs.repository.repositoryImpl.project.AssistUnitRepo;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignMergeRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;

@Service
public class WorkProgramServiceImpl implements WorkProgramService {
    private static Logger log = Logger.getLogger(WorkProgramServiceImpl.class);
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private SysFileRepo sysFileRepo;
    @Autowired
    private AssistPlanSignRepo assistPlanSignRepo;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private AssistUnitRepo assistUnitRepo;
    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;

    @Override
    @Transactional
    public ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram){
        if (Validate.isString(workProgramDto.getSignId())) {
            WorkProgram workProgram = null;
            Date now = new Date();
            if (Validate.isString(workProgramDto.getId())) {
                //1、自评的工作方案不能选择为合并评审
                if(Constant.MergeType.REVIEW_SELF.getValue().equals(workProgramDto.getReviewType()) && Constant.MergeType.REVIEW_MERGE.getValue().equals(workProgramDto.getIsSigle())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，自评不能选择合并评审，请重新选择评审方式！");
                }
                // 2、自评和单个评审不能有关联
                if (Constant.MergeType.REVIEW_SELF.getValue().equals(workProgramDto.getReviewType()) || Constant.MergeType.REVIEW_SIGNLE.getValue().equals(workProgramDto.getIsSigle())) {
                    if(signMergeRepo.isHaveMerge(workProgramDto.getSignId(),Constant.MergeType.WORK_PROGRAM.getValue())){
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，自评和单个评审不能关联其他工作方案，请先删除关联关系！");
                    }
                }
                // 3、合并评审 次项目
                if(Constant.MergeType.REVIEW_MERGE.getValue().equals(workProgramDto.getIsSigle()) && EnumState.NO.getValue().equals(workProgramDto.getIsMainProject())){
                    if (!signMergeRepo.checkIsMerege(workProgramDto.getSignId(),Constant.MergeType.WORK_PROGRAM.getValue())) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前评审方式为合并评审次项目，请在主工作方案中挑选此工作方案为次工作方案再保存！");
                    }
                }
                workProgram = workProgramRepo.findById(workProgramDto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);
            } else {
                workProgram = new WorkProgram();
                BeanCopierUtils.copyProperties(workProgramDto, workProgram);
                workProgram.setId(UUID.randomUUID().toString());
                workProgram.setCreatedBy(SessionUtil.getUserInfo().getId());
                workProgram.setCreatedDate(now);
                workProgram.setStudyQuantum(workProgramDto.getStudyQuantum());//调研时间段
            }
            workProgram.setModifiedBy(SessionUtil.getUserInfo().getId());
            workProgram.setModifiedDate(now);
            //设置关联对象
            Sign sign = workProgram.getSign();
            if(sign == null ){
                sign = signRepo.findById(Sign_.signid.getName(),workProgramDto.getSignId());
            }
            //只有主方案改了，才会更新
            if((FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue()).equals(workProgram.getBranchId())
                    && sign.getAppalyInvestment() != workProgram.getAppalyInvestment()){
                sign.setAppalyInvestment(workProgram.getAppalyInvestment());
            }
            //表示正在做工作方案
            sign.setProcessState(Constant.SignProcessState.DO_WP.getValue());
            workProgram.setSign(sign);
            workProgramRepo.save(workProgram);

            //完成分支工作方案
            signBranchRepo.finishWP(workProgramDto.getSignId(),workProgram.getBranchId());
            //用于返回页面
            workProgramDto.setId(workProgram.getId());
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！",workProgramDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，获取项目信息失败，请联系相关人员处理！");
        }
    }

    /**
     * 根据收文ID初始化 用户待处理的工作方案
     */
    @Override
    public Map<String,Object> initWorkProgram(String signId) {
        Map<String,Object> resultMap = new HashMap<>();
        WorkProgramDto workProgramDto = new WorkProgramDto();

        //1、根据收文ID查询出所有的工作方案ID
        Criteria criteria = workProgramRepo.getExecutableCriteria();
        criteria.createAlias(WorkProgram_.sign.getName(), WorkProgram_.sign.getName());
        criteria.add(Restrictions.eq(WorkProgram_.sign.getName()+ "." + Sign_.signid.getName(), signId));
        List<WorkProgram> wpList = criteria.list();
        //2、是否有当前用户负责的工作方案
        boolean isHaveCurUserWP = false;

        SignPrincipal signPrincipal = signPrincipalService.getPrincipalInfo(SessionUtil.getUserInfo().getId(),signId);
        if(Validate.isList(wpList)){
            List<WorkProgramDto> wpDtoList = new ArrayList<>();
            for(WorkProgram wp : wpList){
                if((signPrincipal.getFlowBranch()).equals(wp.getBranchId())){
                    initWorkProgramDto(wp,workProgramDto);
                    isHaveCurUserWP = true;
                }else{
                    WorkProgramDto wpDto = new WorkProgramDto();
                    initWorkProgramDto(wp,wpDto);
                    wpDtoList.add(wpDto);
                }
            }
            resultMap.put("WPList",wpDtoList);
        }
        //3、主流程负责人，可以填写主要信息
        if(isHaveCurUserWP == false ){
            Sign sign = signRepo.findById(Sign_.signid.getName(),signId);
            workProgramDto.setWorkreviveStage(sign.getReviewstage());
            workProgramDto.setBranchId(signPrincipal.getFlowBranch());
            workProgramDto.setTitleName(sign.getReviewstage() + Constant.WORKPROGRAM_NAME);    //默认名称
            workProgramDto.setProjectName(sign.getProjectname());
            workProgramDto.setAppalyInvestment(sign.getDeclaration());//申报投资
            //是否有拟补充资料函
            workProgramDto.setIsHaveSuppLetter(sign.getIsHaveSuppLetter() == null?  Constant.EnumState.YES.getValue():sign.getIsHaveSuppLetter());
            //拟补充资料函发文日期
            workProgramDto.setSuppLetterDate(sign.getSuppLetterDate());
            if(signPrincipalService.isMainFlowPri(SessionUtil.getUserInfo().getId(),signId)){
                //判断是否是关联次项目
                boolean isMerge =signMergeRepo.checkIsMerege(signId, Constant.MergeType.WORK_PROGRAM.getValue());
                if(isMerge){
                    workProgramDto.setReviewType("合并评审");
                    workProgramDto.setIsMainProject(EnumState.NO.getValue());
                }
                //项目基本信息
                workProgramDto.setProjectName(sign.getProjectname());
                workProgramDto.setBuildCompany(sign.getBuiltcompanyName());
                workProgramDto.setDesignCompany(sign.getDesigncompanyName());
                workProgramDto.setAppalyInvestment(sign.getDeclaration());
                //是否有拟补充资料函
                workProgramDto.setIsHaveSuppLetter(sign.getIsHaveSuppLetter()==null?Constant.EnumState.YES.getValue():sign.getIsHaveSuppLetter());
                //拟补充资料函发文日期
                workProgramDto.setSuppLetterDate(sign.getSuppLetterDate());
                workProgramDto.setTitleName(sign.getReviewstage() + Constant.WORKPROGRAM_NAME);

                workProgramDto.setTitleDate(new Date());
                //来文单位默认全部是：深圳市发展和改革委员会，可改...
                //联系人，就是默认签收表的那个主办处室联系人，默认读取过来但是这边可以给他修改，和主办处室联系人都是独立的两个字段
                workProgramDto.setSendFileUnit(Constant.SEND_FILE_UNIT);
                workProgramDto.setSendFileUser(sign.getMainDeptUserName());

                //获取评审部门
                List<OrgDept> orgList = signBranchRepo.getOrgDeptBySignId(signId);
                if(Validate.isList(orgList)){
                    StringBuffer orgName = new StringBuffer();
                    for(int i=0,l=orgList.size();i<l;i++){
                        if(i > 0){
                            orgName.append(",");
                        }
                        orgName.append(orgList.get(i).getName());
                    }
                    workProgramDto.setReviewOrgName(orgName.toString());
                }
                //项目第一负责人
                User mainUser = signPrincipalService.getMainPriUser(signId);
                if(mainUser != null && Validate.isString(mainUser.getId())){
                    workProgramDto.setMianChargeUserName(mainUser.getDisplayName());
                }
                //项目其它负责人
                List<User> secondPriUserList = signPrincipalService.getAllSecondPriUser(signId);
                if (Validate.isList(secondPriUserList)) {
                    String seUserName = "";
                    for(User u:secondPriUserList){
                        seUserName += u.getDisplayName() + ",";
                    }
                    workProgramDto.setSecondChargeUserName(seUserName.substring(0,seUserName.length()-1));
                }
            }
        }

        resultMap.put("eidtWP",workProgramDto);
        return resultMap;
    }

    /**
     * entity 转成 dto
     * @param workProgram
     * @param workProgramDto
     */
    @Override
    public void initWorkProgramDto(WorkProgram workProgram,WorkProgramDto workProgramDto){
        BeanCopierUtils.copyProperties(workProgram, workProgramDto);
        //1、初始化会议室预定情况
        List<RoomBooking> roomBookings = roomBookingRepo.findByIds(RoomBooking_.businessId.getName(),workProgram.getId(),null);
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
        List<Expert> expertList = expertRepo.findByBusinessId(workProgram.getId());
        if(Validate.isList(expertList)){
            List<ExpertDto> expertDtoList = new ArrayList<>(expertList.size());
            expertList.forEach( el ->{
                ExpertDto expertDto = new ExpertDto();
                el.setPhoto(null);
                BeanCopierUtils.copyProperties(el,expertDto);
                expertDtoList.add(expertDto);
            });
            workProgramDto.setExpertDtoList(expertDtoList);
        }
    }

    /**
     * 通过收文id获取工作方案
     * @param signId
     * @return
     */
    @Override
    public WorkProgramDto findByPrincipalUser(String signId) {

        WorkProgram workProgram = workProgramRepo.findByPrincipalUser(signId);
        WorkProgramDto workProgramDto = new WorkProgramDto();
        if(workProgram !=null){
            BeanCopierUtils.copyProperties(workProgram , workProgramDto);
        }else{
            workProgramDto.setId("");
        }
        return workProgramDto;
    }

    /**
     * 根据项目ID删除工作方案
     *
     * @param signId
     */
    @Override
    @Transactional
    public ResultMsg deleteBySignId(String signId) {
        SignPrincipal signPrincipal = signPrincipalService.getPrincipalInfo(SessionUtil.getUserInfo().getId(),signId);
        if(signPrincipal == null){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"您不是项目负责人，不能对工作方案进行操作！");
        }
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from cs_work_program where signid =:signid and branchId =:branchId ");
        sqlBuilder.setParam("signid", signId);
        sqlBuilder.setParam("branchId", signPrincipal.getFlowBranch());
        int result = workProgramRepo.executeSql(sqlBuilder);
        //不需要做工作方案
        signBranchRepo.isNeedWP(signId,signPrincipal.getFlowBranch(),EnumState.NO.getValue());
        if(result < 0){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"您不是项目负责人，不能对工作方案进行操作！");
        }else{
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
        }
    }

    /**
     * 删除工作方案信息
     * @param id
     */
    @Override
    public void delete(String id) {
        workProgramRepo.deleteById(WorkProgram_.id.getName(),id);
    }

    /**
     * TODO:目前只是做一个简单的模板生成，后期再完善
     * 生成会前准备材料
     * @param signId
     * @param workprogramId
     * @return
     */
    @Override
    @Transactional
    public ResultMsg createMeetingDoc(String signId, String workprogramId) {
        Sign sign = signRepo.findById(Sign_.signid.getName(),signId);
        if(sign == null || StringUtil.isEmpty(sign.getSignid())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法收文信息");
        }
        WorkProgram workProgram = workProgramRepo.findById(WorkProgram_.id.getName(),workprogramId);
        if(workProgram == null || StringUtil.isEmpty(workProgram.getId())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取工作方案信息");
        }
        String path = SysFileUtil.getUploadPath();
        //1、如果已经生成会前准备材料，则先删除之前的文件
        if(EnumState.YES.getValue().equals(workProgram.getIsCreateDoc())){
            HqlBuilder queryHql = HqlBuilder.create();
            queryHql.append(" from "+SysFile.class.getSimpleName()+" where "+ SysFile_.mainId.getName()+" =:signId ");
            queryHql.setParam("signId",signId);
            queryHql.append(" and "+ SysFile_.businessId.getName()+" =:businessId ");
            queryHql.setParam("businessId",workprogramId);
            queryHql.append(" and "+ SysFile_.sysfileType.getName()+" =:sysfileType ");
            queryHql.setParam("sysfileType",Constant.SysFileType.WORKPROGRAM.getValue());   //模块类型
            queryHql.append(" and "+ SysFile_.sysBusiType.getName()+" =:sysBusiType ");
            queryHql.setParam("sysBusiType",Constant.SysFileType.MEETING.getValue());        //业务类型

            List<SysFile> fileList = sysFileRepo.findByHql(queryHql);
            if(fileList != null && fileList.size() > 0){
                fileList.forEach(f->{
                    sysFileRepo.delete(f);
                    SysFileUtil.deleteFile(path + f.getFileUrl());
                });
            }
        }
        //2、生成会前准备材料
        List<SysFile> saveFile = new ArrayList<>();

        //获得拟聘专家信息
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select  e.* from cs_expert_review er,cs_work_program wp,cs_expert_selected es,cs_expert e");
        sqlBuilder.append(" where er."+ExpertReview_.id.getName()+" = wp.expertreviewid");
        sqlBuilder.append(" and er."+ExpertReview_.id.getName()+" =es.expertreviewid");
        sqlBuilder.append(" and es.expertid =e."+Expert_.expertID.getName());
        sqlBuilder.append(" and wp." +WorkProgram_.id.getName()+" =:workProgramId");
        sqlBuilder.setParam("workProgramId", workprogramId);
        List<Expert> expertList=expertRepo.findBySql(sqlBuilder);

        User user = signPrincipalService.getMainPriUser(signId);//获取项目第一负责人

        //获得会议信息
        List<RoomBooking> roomBookings = roomBookingRepo.findByIds(RoomBooking_.businessId.getName(),workProgram.getId(),null);

        //2.1 生成签到表
        saveFile.add(CreateTemplateUtils.createtTemplateSignIn(sign ,workProgram));

        //2.2 生成主持人表
        saveFile.add(CreateTemplateUtils.createTemplateCompere(sign , workProgram ,expertList));

        //2.3 会议议程
        List<SysFile> sList = CreateTemplateUtils.createTemplateMeeting(sign , workProgram,roomBookings);
        if(sList != null && sList.size() >0){
            for(SysFile sysFile : sList){
                saveFile.add(sysFile);
            }
        }

        //2.4 邀请函
        for (Expert expert : expertList) {
           SysFile  invitation = CreateTemplateUtils.createTemplateInvitation(sign , workProgram , expert,user,roomBookings);
           if(invitation !=null){
               saveFile.add(invitation);
           }
        }

        //2.5 会议通知

        SysFile notice = CreateTemplateUtils.createTemplateNotice(sign ,workProgram,user,roomBookings);
        if(notice !=null){
            saveFile.add(notice);
        }


        //协审协议书
        HqlBuilder queryaps = HqlBuilder.create();
        queryaps.append(" from "+AssistPlanSign.class.getSimpleName()+" where "+AssistPlanSign_.signId.getName()+" =:signID");
        queryaps.setParam("signID", signId);
        List<AssistPlanSign> apsList=assistPlanSignRepo.findByHql(queryaps);
        AssistUnit assistUnit = null;
        Org org = null;
        if(!StringUtil.isBlank(sign.getAssistdeptid())){
            assistUnit=assistUnitRepo.findById(sign.getAssistdeptid());//乙方
        }
        if(!StringUtil.isBlank(sign.getMaindepetid())){

            org=orgRepo.findById(sign.getMaindepetid());//甲方
        }
        saveFile.add(CreateTemplateUtils.createTemplateAssist(sign , workProgram ,apsList , assistUnit , org));

        //3、保存文件信息
        if(saveFile.size() > 0){
            Date now = new Date();
            saveFile.forEach(sf->{
                sf.setCreatedDate(now);
                sf.setModifiedDate(now);
                sf.setModifiedBy(SessionUtil.getLoginName());
                sf.setCreatedBy(SessionUtil.getLoginName());
            });
            sysFileRepo.bathUpdate(saveFile);
        }
        //4、更改工作方案状态
        workProgram.setIsCreateDoc(EnumState.YES.getValue());
        workProgramRepo.save(workProgram);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功");
    }

	@Override
	public WorkProgramDto initWorkProgramById(String workId) {
		WorkProgram work = workProgramRepo.findById(workId);
		WorkProgramDto workDto = new WorkProgramDto();
		BeanCopierUtils.copyProperties(work, workDto);
		return workDto;
	}

}
