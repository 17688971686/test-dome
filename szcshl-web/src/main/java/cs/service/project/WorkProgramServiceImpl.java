package cs.service.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.SysFileUtil;
import cs.common.utils.TemplateUtil;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.Expert_;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.AssistPlan;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.AssistUnit;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.domain.sys.Org;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.domain.sys.User;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import cs.repository.repositoryImpl.project.AssistUnitRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.service.sys.UserService;

@Service
public class WorkProgramServiceImpl implements WorkProgramService {
    private static Logger log = Logger.getLogger(WorkProgramServiceImpl.class);
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private ICurrentUser currentUser;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private UserService userService;
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

    @Override
    @Transactional
    public void save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram) throws Exception {
        if (Validate.isString(workProgramDto.getSignId())) {
            WorkProgram workProgram = null;
            Date now = new Date();

            if (!Validate.isString(workProgramDto.getId())) {
                workProgram = new WorkProgram();
                BeanCopierUtils.copyProperties(workProgramDto, workProgram);
                workProgram.setId(UUID.randomUUID().toString());
                workProgram.setCreatedBy(currentUser.getLoginUser().getId());
                workProgram.setCreatedDate(now);
            } else {
                workProgram = workProgramRepo.findById(workProgramDto.getId());
                //判断是否是单个评审和次项目,删除关联项目
                if (Validate.isString(workProgram.getIsSigle()) && Validate.isString(workProgram.getIsMainProject()) &&
                        (workProgram.getIsSigle().equals(Constant.EnumState.PROCESS.getValue()) || workProgram.getIsMainProject().equals(Constant.EnumState.NO.getValue()))) {
                    this.deleteMegreWokr(workProgram.getId());
                }
                BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);
            }

            workProgram.setModifiedBy(currentUser.getLoginUser().getId());
            workProgram.setModifiedDate(now);

            Sign sign = signRepo.findById(workProgramDto.getSignId());
            if (!Validate.isString(workProgram.getTitleName())) {
                workProgram.setTitleName(sign.getReviewstage() + Constant.WORKPROGRAM_NAME);    //默认名称
            }
            //判断是否是主流程
            boolean isMainFlow = false;
            if ((Validate.isString(workProgramDto.getIsMain()) && workProgramDto.getIsMain().equals(EnumState.YES.getValue()))
                    || signPrincipalService.isFlowPri(currentUser.getLoginUser().getId(), sign.getSignid(), EnumState.YES.getValue())) {
                isMainFlow = true;
            }
            if (isMainFlow) {
                workProgram.setIsMain(EnumState.YES.getValue());
                sign.setIsreviewCompleted(EnumState.YES.getValue());
            } else {
                workProgram.setIsMain(Constant.EnumState.NO.getValue());
                sign.setIsreviewACompleted(EnumState.YES.getValue());
            }

            //是否需要工作方案（默认需要）
            if (isNeedWorkProgram != null) {
                sign.setIsNeedWrokPrograml(isNeedWorkProgram == true ? EnumState.YES.getValue() : EnumState.NO.getValue());
            } else {
                sign.setIsNeedWrokPrograml(EnumState.YES.getValue());
            }
            //申报金额，与工作方案的、收文的一致，任何一个地方改了，都要同步更新
            sign.setAppalyInvestment(workProgram.getAppalyInvestment());
            workProgram.setSign(sign);
            workProgramRepo.save(workProgram);

            //sign.getWorkProgramList().add(workProgram);
            //signRepo.save(sign);
            //用于返回页面
            workProgramDto.setId(workProgram.getId());
        } else {
            log.info("工作方案添加操作：无法获取收文ID（SignId）信息");
            throw new Exception(Constant.ERROR_MSG);
        }
    }

    /**
     * 删除项目关联
     */
    @Transactional
    public void deleteMegreWokr(String workId) {
        /*MergeDispa merge = mergeDispaRepo.getById(workId);
        if (merge != null) {
            mergeDispaRepo.delete(merge);
        }*/
    }

    /**
     * 根据收文ID初始化用户待处理的工作方案
     */
    @Override
    public WorkProgramDto initWorkBySignId(String signId, String isMain) {
        //是否为项目负责人标识,如果两者都不是，则是审核领导
        boolean isMainUser = false, isAssistUser = false;

        WorkProgramDto workProgramDto = new WorkProgramDto();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + WorkProgram.class.getSimpleName() + " where " + WorkProgram_.sign.getName() + "." + Sign_.signid.getName() + " = :signId ");
        hqlBuilder.setParam("signId", signId);

        //如果有传入的参数，则优先按传入的参数查询
        if (Validate.isString(isMain)) {
            hqlBuilder.append(" and " + WorkProgram_.isMain.getName() + " = :isMain ").setParam("isMain", isMain);
        }else{
            isMainUser = signPrincipalService.isFlowPri(currentUser.getLoginUser().getId(), signId, EnumState.YES.getValue());
            if (isMainUser) {
                isMain = EnumState.YES.getValue();
            } else {
                isAssistUser = signPrincipalService.isFlowPri(currentUser.getLoginUser().getId(), signId, EnumState.NO.getValue());
                if (isAssistUser) {
                    isMain = EnumState.NO.getValue();
                }
            }
            if (Validate.isString(isMain)) {
                hqlBuilder.append(" and " + WorkProgram_.isMain.getName() + " = :isMain ").setParam("isMain", isMain);
            }
        }
        List<WorkProgram> list = workProgramRepo.findByHql(hqlBuilder);

        if (list != null && list.size() > 0) {
            //如果是创建人的上级领导，则显示
            WorkProgram workProgram = null;
            for (int i = 0, l = list.size(); i < l; i++) {
                workProgram = list.get(i);
                UserDto checkUser = userService.findById(workProgram.getCreatedBy());
                if (userService.curUserIsSuperLeader(checkUser) || isMainUser || isAssistUser) {
                    BeanCopierUtils.copyProperties(workProgram, workProgramDto);
                    //初始化会议室预定情况
                    List<RoomBooking> roomBookings = workProgram.getRoomBookings();
                    if (roomBookings != null && roomBookings.size() > 0) {
                        List<RoomBookingDto> roomBookingDtos = new ArrayList<RoomBookingDto>(roomBookings.size());
                        roomBookings.forEach(r -> {
                            RoomBookingDto rbDto = new RoomBookingDto();
                            BeanCopierUtils.copyProperties(r, rbDto);
                            rbDto.setBeginTimeStr(DateUtils.converToString(rbDto.getBeginTime(), "HH:mm"));
                            rbDto.setEndTimeStr(DateUtils.converToString(rbDto.getEndTime(), "HH:mm"));
                            roomBookingDtos.add(rbDto);
                        });
                        workProgramDto.setRoomBookingDtos(roomBookingDtos);
                    }
                    break;
                }
            }

        //如果没有查到数据，则初始化
        }else{
            Sign sign = signRepo.findById(signId);
            workProgramDto.setProjectName(sign.getProjectname());
            workProgramDto.setBuildCompany(sign.getBuiltcompanyName());
            workProgramDto.setDesignCompany(sign.getDesigncompanyName());
            workProgramDto.setTitleName(sign.getReviewstage() + Constant.WORKPROGRAM_NAME);    //默认名称
            workProgramDto.setWorkreviveStage(sign.getReviewstage());//评审阶段
            workProgramDto.setTitleDate(new Date());
            //来文单位默认全部是：深圳市发展和改革委员会，可改...
            //联系人，就是默认签收表的那个主办处室联系人，默认读取过来但是这边可以给他修改，和主办处室联系人都是独立的两个字段
            workProgramDto.setSendFileUnit(Constant.SEND_FILE_UNIT);
            workProgramDto.setSendFileUser(sign.getMainDeptUserName());

            //查询项目的第一第二负责人
            User mainUser = signPrincipalService.getMainPriUser(signId, isMainUser==true?EnumState.YES.getValue():EnumState.NO.getValue());
            List<User> secondPriUserList = signPrincipalService.getAllSecondPriUser(signId, isMainUser==true?EnumState.YES.getValue():EnumState.NO.getValue());

            if(mainUser != null && Validate.isString(mainUser.getId())){
                workProgramDto.setMianChargeUserId(mainUser.getId());
                workProgramDto.setMianChargeUserName(mainUser.getDisplayName());
                workProgramDto.setReviewOrgId(mainUser.getOrg().getId());
                workProgramDto.setReviewOrgName(mainUser.getOrg().getName());
                workProgramDto.setIsMain(isMainUser==true?EnumState.YES.getValue():EnumState.NO.getValue());
            }
            if (secondPriUserList != null && secondPriUserList.size() > 0) {
                String seUserIds = "",seUserName = "";
                for(User u:secondPriUserList){
                    seUserIds += u.getId() + ",";
                    seUserName += u.getDisplayName() + ",";
                }
                workProgramDto.setSecondChargeUserId(seUserIds.substring(0,seUserIds.length()-1));
                workProgramDto.setSecondChargeUserName(seUserName.substring(0,seUserName.length()-1));
            }
        }

        return workProgramDto;
    }

    //待选项目列表
    @Override
    public List<SignDto> waitProjects(SignDto signDto) {
        HqlBuilder hqlBuilder = HqlBuilder.create(" from " + Sign.class.getSimpleName()).append(" where ");
        if (StringUtils.isNotBlank(signDto.getSignid())) {
            String[] linkSids = signDto.getSignid().split(",");
            hqlBuilder.append(Sign_.signid.getName()).append(" not in( ");
            for (int i = 0, j = 0; i < linkSids.length; i++) {
                if (StringUtils.isNotBlank(linkSids[i])) {
                    if (j != 0) {
                        hqlBuilder.append(",");
                    }
                    hqlBuilder.append(":linkSids" + i).setParam("linkSids" + i, linkSids[i]);
                    j++;
                }
            }
            hqlBuilder.append(")");
            hqlBuilder.append(" and ");

        }
        //是否完成发文
        hqlBuilder.append(Sign_.isDispatchCompleted.getName())
                .append(" not in(:isDispatchCompleted,:isDispatchCompleted1)").setParam("isDispatchCompleted", Constant.EnumState.NO.getValue())
                .setParam("isDispatchCompleted1", "null");
        //是否发起流程
        hqlBuilder.append(" and " + Sign_.folwState.getName()).append(" not in(:folwState,:folwState1,:folwState2)")
                .setParam("folwState", Constant.EnumState.STOP.getValue()).setParam("folwState1", Constant.EnumState.DELETE.getValue()).setParam("folwState2", Constant.EnumState.NO.getValue());
        //收文状态
        hqlBuilder.append(" and " + Sign_.signState.getName()).append(" not in(:signState,:signState1,:signState2)")
                .setParam("signState", Constant.EnumState.STOP.getValue()).setParam("signState1", Constant.EnumState.DELETE.getValue()).setParam("signState2", Constant.EnumState.NO.getValue());

        if (StringUtils.isNotBlank(signDto.getProjectname())) {
            hqlBuilder.append(" and " + Sign_.projectname.getName()).append("=:projectname").setParam("projectname", signDto.getProjectname());
        }
        if (StringUtils.isNoneBlank(signDto.getBuiltcompanyName())) {
            hqlBuilder.append(" and " + Sign_.builtcompanyName.getName()).append("=:builtcompanyName").setParam("builtcompanyName", signDto.getBuiltcompanyName());
        }
        if (StringUtils.isNotBlank(signDto.getReviewstage())) {
            hqlBuilder.append(" and " + Sign_.reviewstage.getName()).append("=:reviewstage").setParam("reviewstage", signDto.getReviewstage());
        }
        if (signDto.getStartTime() != null && signDto.getEndTime() != null) {
            hqlBuilder.append(" and " + Sign_.signdate.getName()).append(" between ").append(":startTime").append(" and ").append(":endTime").setParam("startTime", signDto.getStartTime()).setParam("endTime", signDto.getEndTime());
        }
        List<Sign> list = signRepo.findByHql(hqlBuilder);
        List<SignDto> signDtoList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            list.forEach(x -> {
                SignDto sDto = new SignDto();
                BeanCopierUtils.copyProperties(x, sDto);
                signDtoList.add(sDto);
            });
        }

        return signDtoList;
    }

    @Override
    public List<SignDto> selectedProject(String[] ids) {
        List<SignDto> signDtos = new ArrayList<>();
        for (String id : ids) {
            if (Validate.isString(id)) {
                SignDto signDto = new SignDto();
                Sign s = signRepo.findById(id);
                BeanCopierUtils.copyProperties(s, signDto);
                signDtos.add(signDto);
            }
        }
        return signDtos;
    }

    @Override
    @Transactional
    public void mergeAddWork(String signId, String linkSignId) {
       /* Date now = new Date();
        Sign sign = signRepo.findById(signId);
        MergeDispa merge = new MergeDispa();
        List<WorkProgram> works = sign.getWorkProgramList();
        for (WorkProgram work : works) {
            merge.setBusinessId(work.getId());
            merge.setType(work.getReviewType());
        }
        merge.setSignId(signId);
        merge.setLinkSignId(linkSignId);
        merge.setCreatedBy(currentUser.getLoginName());
        merge.setModifiedBy(currentUser.getLoginName());
        merge.setCreatedDate(now);
        merge.setModifiedDate(now);
        mergeDispaRepo.save(merge);*/
    }

    @Override
    public Map<String, Object> getInitSeleSignByIds(String bussnessId) {
      Map<String, Object> map = new HashMap<>();
        /*  MergeDispa merge = mergeDispaRepo.getById(bussnessId);
        List<SignDto> signDtos = null;
        String linkSignId = "";
        if (merge != null && Validate.isString(merge.getBusinessId())) {
            linkSignId = merge.getLinkSignId();
            signDtos = new ArrayList<>();
            String[] ids = linkSignId.split(",");
            if (ids != null) {
                for (String id : ids) {
                    if (Validate.isString(id)) {
                        SignDto signDto = new SignDto();
                        Sign sign = signRepo.findById(id);
                        BeanCopierUtils.copyProperties(sign, signDto);
                        signDto.setCreatedDate(sign.getCreatedDate());
                        signDto.setModifiedDate(sign.getModifiedDate());
                        signDtos.add(signDto);
                    }
                }
            }
        }
        map.put("signDtoList", signDtos);
        map.put("linkSignId", linkSignId);*/
        return map;
    }

    @Override
    public Map<String, Object> getInitRelateData(String signId) {
        Map<String, Object> map = new HashMap<String, Object>();
       /* String linkSignId = "";
        Sign sign = signRepo.findById(signId);
        List<WorkProgram> work = sign.getWorkProgramList();
        for (WorkProgram wp : work) {
            String workId = wp.getId();
            MergeDispa merge = mergeDispaRepo.getById(workId);
            if (merge != null && Validate.isString(merge.getBusinessId())) {
                linkSignId = merge.getLinkSignId();
            }
        }
        map.put("linkSignId", linkSignId);
        //查询系统上传文件
        Criteria file = sysFileRepo.getSession().createCriteria(SysFile.class);
        file.add(Restrictions.eq("businessId", sign.getSignid()));
        List<SysFile> sysFilelist = file.list();
        if (sysFilelist != null) {
            map.put("sysFilelist", sysFilelist);
        }*/
        return map;
    }

    /**
     * 根据项目ID删除工作方案
     *
     * @param signId
     */
    @Override
    public void deleteBySignId(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from cs_work_program where signid =:signid");
        sqlBuilder.setParam("signid", signId);
        workProgramRepo.executeSql(sqlBuilder);
        //更改收文状态
        Sign sign = signRepo.findById(signId);
        sign.setIsNeedWrokPrograml(EnumState.NO.getValue());
        sign.setIsreviewCompleted(EnumState.NO.getValue());
        sign.setIsreviewACompleted(EnumState.NO.getValue());

        signRepo.save(sign);
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
            queryHql.append(" from "+SysFile.class.getSimpleName()+" where "+ SysFile_.sysSingId.getName()+" =:signId ");
            queryHql.setParam("signId",signId);
            queryHql.append(" and "+ SysFile_.businessId.getName()+" =:businessId ");
            queryHql.setParam("businessId",workprogramId);
            queryHql.append(" and "+ SysFile_.sysfileType.getName()+" =:sysfileType ");
            queryHql.setParam("sysfileType",Constant.SysFileType.WORKPROGRAM.getValue());   //大类
            queryHql.append(" and "+ SysFile_.sysMinType.getName()+" =:sysMinType ");
            queryHql.setParam("sysMinType",Constant.SysFileType.MEETING.getValue());        //小类

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
        String showName = "",relativeFileUrl="";
        File docFile = null;
        Map<String,Object> dataMap = new HashMap<>();
        
        dataMap.put("projectBackGround", workProgram.getProjectBackGround());
	    dataMap.put("buildSize", workProgram.getBuildSize());
	    dataMap.put("buildContent", workProgram.getBuildContent());
	    dataMap.put("appalyInvestment", workProgram.getAppalyInvestment());//申报金额
	    dataMap.put("mianChargeUserName",workProgram.getMianChargeUserName() ); //第一负责人
	    dataMap.put("secondChargeUserName",workProgram.getSecondChargeUserName() ); //第二负责人
	    dataMap.put("ministerName",workProgram.getMinisterName() );//部长名
	    dataMap.put("mainPoint",workProgram.getMainPoint());//拟评审重点问题
	    dataMap.put("workStageTime",workProgram.getWorkStageTime());//评审时间
     	dataMap.put("meetingAddress",workProgram.getMeetingAddress());//会议具体地点
     	dataMap.put("contactPerson",workProgram.getContactPerson());//联系人
     	dataMap.put("contactPersonTel",workProgram.getContactPersonTel());//联系电话
    	dataMap.put("contactPersonFax",workProgram.getContactPersonFax());//传真
	    
	    dataMap.put("leaderName",sign.getLeaderName() );//中心领导名
	    dataMap.put("maindeptName", sign.getMaindeptName());//主办事处名称
	    dataMap.put("assistdeptName", sign.getAssistdeptName());//协办事处名称
	    dataMap.put("mainDeptName", sign.getMaindeptName());//主管部门名称
	    dataMap.put("builtcompanyName", sign.getBuiltcompanyName());//建设单位
	    dataMap.put("designcompanyName", sign.getDesigncompanyName());//编制单位
	    
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("reviewStage",sign.getReviewstage());
        
        
        //获得拟聘专家信息
    	 HqlBuilder sqlBuilder = HqlBuilder.create();
         sqlBuilder.append(" select  e.* from cs_expert_review er,cs_work_program wp,cs_expert_selected es,cs_expert e");
         sqlBuilder.append(" where er."+ExpertReview_.id.getName()+" = wp.expertreviewid");
         sqlBuilder.append(" and er."+ExpertReview_.id.getName()+" =es.expertreviewid");
         sqlBuilder.append(" and es.expertid =e."+Expert_.expertID.getName());
         sqlBuilder.append(" and wp." +WorkProgram_.id.getName()+" =:workProgramId");
         sqlBuilder.setParam("workProgramId", workprogramId);
         List<Expert> expertList=expertRepo.findBySql(sqlBuilder);
         //获得会议信息
         List<RoomBooking> rbList=workProgram.getRoomBookings();
         Date compareDate=DateUtils.converToDate("12:00", "HH:MM");
         for (RoomBooking roomBooking : rbList) {
         	dataMap.put("addressName", roomBooking.getAddressName());//会议地点
         	
         	if(DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(),compareDate)==1 &&
    				 DateUtils.compareIgnoreSecond(roomBooking.getEndTime(),compareDate)==-1){
     			 dataMap.put("lastTime", "全天");//天数
         	}else{
         		 dataMap.put("lastTime", "半天");//天数
         	}
         }
         
        //2.1 生成签到表
        showName = Constant.Template.SIGN_IN.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path, signId,workprogramId,showName);
      /*  for (Expert expert : expertList) {
        	 dataMap.put("expertName",expert.getName());
        	 dataMap.put("comPany",expert.getComPany());
        	 dataMap.put("job",expert.getJob());//职务
        	 dataMap.put("post",expert.getPost());//职称
        	 dataMap.put("phone",expert.getPhone());
        }*/
        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.SIGN_IN.getKey(),path + File.separator +relativeFileUrl);
	        if(docFile != null){
	            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
	                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
	                    null, signId, Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
	        }
	        
        //2.2 生成主持人表
        String expertName="";
        for (Expert expert : expertList) {
        	if(!StringUtil.isBlank(expertName)){
        		expertName+="、";
        	}
        	expertName+=expert.getName();
        }
        dataMap.put("expertName", expertName);
	    //相关单位
        String depat=sign.getMaindeptName()+","+sign.getAssistdeptName()+","+workProgram.getMainDeptName();
        dataMap.put("depat",depat);
        //评审中心项目组成员
        String projectWorker=workProgram.getLeaderName()+","+workProgram.getMinisterName()+","+workProgram.getMianChargeUserName()+","+workProgram.getSecondChargeUserName();
        dataMap.put("projectWorker",projectWorker);
        showName = Constant.Template.COMPERE.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,signId,workprogramId,showName);
        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.COMPERE.getKey(),path + File.separator +relativeFileUrl);
        if(docFile != null){
            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
                    null, signId, Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
        }
        
        //2.3 生成会前准备材料
        //获得会议预定信息
        if(!rbList.isEmpty()){
        	for (RoomBooking roomBooking : rbList) {
        		
        		dataMap.put("rbDate", roomBooking.getRbDate());//会议日期显示星期
        		dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(),"HH:MM"));//会议开始时间
        		dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(),"HH:MM"));//会议结束时间
        		
        		if(DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(),compareDate)==1){
        			//（上午）
        			showName = Constant.Template.MEETING_AM.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        			relativeFileUrl =  SysFileUtil.generatRelativeUrl(path, signId,workprogramId,showName);
        			docFile = TemplateUtil.createDoc(dataMap,Constant.Template.MEETING_AM.getKey(),path + File.separator +relativeFileUrl);
        		        if(docFile != null){
        		            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
        		                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
        		                    null, signId, Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
        		        }
        		        
        		}else{
        			//（下午）
	    			 showName = Constant.Template.MEETING_PM.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
	    		        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path, signId,workprogramId,showName);
	    		        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.MEETING_PM.getKey(),path + File.separator +relativeFileUrl);
	    		        if(docFile != null){
	    		        	
	    		        	saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
	    		        			Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
	    		        			null, signId, Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
	    		        }
        		}
			}
        }
       
        
       
        //2.4 邀请函
        for (Expert expert : expertList) {
        	dataMap.put("expertName",expert.getName());
	        showName = Constant.Template.INVITATION.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
	        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path, signId,workprogramId,showName);
	        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.INVITATION.getKey(),path + File.separator +relativeFileUrl);
	        if(docFile != null){
	            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
	                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
	                    null, signId, Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
	        }
        }
        
        //2.5 会议通知
        	dataMap.put("studyBeginTime",DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        	showName = Constant.Template.UNIT_NOTICE.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        	relativeFileUrl =  SysFileUtil.generatRelativeUrl(path, signId,workprogramId,showName);
        	docFile = TemplateUtil.createDoc(dataMap,Constant.Template.UNIT_NOTICE.getKey(),path + File.separator +relativeFileUrl);
        	if(docFile != null){
        		
        		saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
        				Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
        				null, signId, Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
        	}
        	
        	//协审协议书
        	dataMap.put("assistDeptUserName", sign.getAssistdeptName()); //协办事处联系人
     	    dataMap.put("mainDeptUserName", sign.getMainDeptUserName()); //主办事处联系人
     	    
    	    HqlBuilder queryaps = HqlBuilder.create();
    	    queryaps.append(" from "+AssistPlanSign.class.getSimpleName()+" where "+AssistPlanSign_.signId.getName()+" =:signID");
    	    queryaps.setParam("signID", signId);
    	    List<AssistPlanSign> apsList=assistPlanSignRepo.findByHql(queryaps);
    	    if(!apsList.isEmpty()){
    	    	
    	    	AssistPlanSign assistPlanSign=apsList.get(0);
    	    	AssistPlan assistPlan=assistPlanSign.getAssistPlan();
    	    	float assistDays=assistPlanSign.getAssistDays();
    	    	Date reportTime=assistPlan.getReportTime();
    	    	Date finishTime=DateUtils.addDay(reportTime, (int)assistDays);
    	    	dataMap.put("estimateCost", assistPlanSign.getEstimateCost()); //建设规模
    	    	dataMap.put("finishTime", DateUtils.converToString(finishTime, "yyyy年MM月dd日")); //建设规模
    	    	dataMap.put("assistCost",assistPlanSign.getAssistCost()); //协审费用
    	    }
        	if(!StringUtil.isBlank(sign.getAssistdeptid())){
        		
	    	    AssistUnit assistUnit=assistUnitRepo.findById(sign.getAssistdeptid());//乙方
	    	    dataMap.put("unitName",assistUnit.getUnitName());
	    	    dataMap.put("address",assistUnit.getAddress());
	    	    dataMap.put("phoneNum",assistUnit.getPhoneNum());
	    	    dataMap.put("principalName",assistUnit.getPrincipalName());
	    	    dataMap.put("contactFax",assistUnit.getContactFax());
        	}
        	if(!StringUtil.isBlank(sign.getMaindepetid())){
        		
	    	    Org org=orgRepo.findById(sign.getMaindepetid());//甲方   
	    	    dataMap.put("orgName",org.getName());
	    	    dataMap.put("orgAddress",org.getOrgAddress());
	    	    dataMap.put("orgMLeader",org.getOrgMLeader());
	    	    dataMap.put("orgPhone",org.getOrgPhone());
	    	    dataMap.put("orgFax",org.getOrgFax());
        	}
        	   
        	showName = Constant.Template.ASSIST.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        	relativeFileUrl =  SysFileUtil.generatRelativeUrl(path, signId,workprogramId,showName);
        	docFile = TemplateUtil.createDoc(dataMap,Constant.Template.ASSIST.getKey(),path + File.separator +relativeFileUrl);
        	if(docFile != null){
        		
        		saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
        				Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
        				null, signId, Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
        	}
        	
	        //3、保存文件信息
	        if(saveFile.size() > 0){
	        	
	            Date now = new Date();
	            saveFile.forEach(sf->{
	                sf.setCreatedDate(now);
	                sf.setModifiedDate(now);
	                sf.setCreatedBy(currentUser.getLoginName());
	                sf.setModifiedBy(currentUser.getLoginName());
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
