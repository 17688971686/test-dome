package cs.service.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
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
import cs.domain.project.SignPrincipal;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.domain.sys.Org;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.domain.sys.User;
import cs.model.expert.ExpertDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.repositoryImpl.expert.ExpertRepo;
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

    @Override
    @Transactional
    public ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram){
        if (Validate.isString(workProgramDto.getSignId())) {
            WorkProgram workProgram = null;
            Date now = new Date();
            if (Validate.isString(workProgramDto.getId())) {
                //1、自评的工作方案不能选择为合并评审
                if("自评".equals(workProgramDto.getReviewType()) && "合并评审".equals(workProgramDto.getIsSigle())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，自评不能选择合并评审，请重新选择评审方式！");
                }
                // 2、自评和单个评审不能有关联
                if ("自评".equals(workProgramDto.getReviewType()) || "单个评审".equals(workProgramDto.getIsSigle())) {
                    if(signMergeRepo.isHaveMerge(workProgramDto.getSignId(),Constant.MergeType.WORK_PROGRAM.getValue())){
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，自评和单个评审不能关联其他工作方案，请先删除关联关系！");
                    }
                }
                // 3、合并评审 次项目
                if("合并评审".equals(workProgramDto.getIsSigle()) && EnumState.NO.getValue().equals(workProgramDto.getIsMainProject())){
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
            }
            workProgram.setModifiedBy(SessionUtil.getUserInfo().getId());
            workProgram.setModifiedDate(now);
            //设置关联对象
            Sign sign = workProgram.getSign();
            if(sign == null ){
                sign = signRepo.findById(Sign_.signid.getName(),workProgramDto.getSignId());
            }
            //只有主方案改了，才会更新
            if((Constant.SignFlowParams.BRANCH_INDEX1.getValue()).equals(workProgram.getBranchId())
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
                workProgramDto.setTitleName(sign.getReviewstage() + Constant.WORKPROGRAM_NAME);

                workProgramDto.setTitleDate(new Date());
                //来文单位默认全部是：深圳市发展和改革委员会，可改...
                //联系人，就是默认签收表的那个主办处室联系人，默认读取过来但是这边可以给他修改，和主办处室联系人都是独立的两个字段
                workProgramDto.setSendFileUnit(Constant.SEND_FILE_UNIT);
                workProgramDto.setSendFileUser(sign.getMainDeptUserName());

                //获取评审部门
                List<Org> orgList = workProgramRepo.getReviewOrg(signId);
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
        List<RoomBooking> roomBookings = workProgram.getRoomBookings();
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
        List<Expert> expertList = expertRepo.findByWorkProgramId(workProgram.getId());
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
         //String fileLocation,String mainType,String mainId, String sysBusiType, String fileName
        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,Constant.SysFileType.SIGN.getValue(), signId,Constant.SysFileType.MEETING.getValue(),showName);

        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.SIGN_IN.getKey(),path + File.separator +relativeFileUrl);
	        if(docFile != null){
	            //String sysFileId, String businessId, String fileUrl, String showName, Integer fileSize, String fileType, String mainId, String sysfileType, String sysBusiType
	            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
	                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
	                    signId,Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
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
        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,Constant.SysFileType.SIGN.getValue(), signId,Constant.SysFileType.MEETING.getValue(),showName);
        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.COMPERE.getKey(),path + File.separator +relativeFileUrl);
        if(docFile != null){
            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
                    signId,Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
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
        			relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,Constant.SysFileType.SIGN.getValue(), signId,Constant.SysFileType.MEETING.getValue(),showName);
        			docFile = TemplateUtil.createDoc(dataMap,Constant.Template.MEETING_AM.getKey(),path + File.separator +relativeFileUrl);
        		        if(docFile != null){
                            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
                                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
                                    signId,Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
                        }
        		        
        		}else{
        			//（下午）
	    			 showName = Constant.Template.MEETING_PM.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
	    		        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,Constant.SysFileType.SIGN.getValue(), signId,Constant.SysFileType.MEETING.getValue(),showName);
	    		        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.MEETING_PM.getKey(),path + File.separator +relativeFileUrl);
	    		        if(docFile != null){

                            saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
                                    Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
                                    signId,Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
                        }
        		}
			}
        }
       
        
       
        //2.4 邀请函
        for (Expert expert : expertList) {
        	dataMap.put("expertName",expert.getName());
	        showName = Constant.Template.INVITATION.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
	        relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,Constant.SysFileType.SIGN.getValue(), signId,Constant.SysFileType.MEETING.getValue(),showName);
	        docFile = TemplateUtil.createDoc(dataMap,Constant.Template.INVITATION.getKey(),path + File.separator +relativeFileUrl);
	        if(docFile != null){
                saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
                        Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
                        signId,Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
            }
        }
        
        //2.5 会议通知
        	dataMap.put("studyBeginTime",DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        	showName = Constant.Template.UNIT_NOTICE.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        	relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,Constant.SysFileType.SIGN.getValue(), signId,Constant.SysFileType.MEETING.getValue(),showName);
        	docFile = TemplateUtil.createDoc(dataMap,Constant.Template.UNIT_NOTICE.getKey(),path + File.separator +relativeFileUrl);
        	if(docFile != null){

                saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
                        Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
                        signId,Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
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
        	relativeFileUrl =  SysFileUtil.generatRelativeUrl(path,Constant.SysFileType.SIGN.getValue(), signId,Constant.SysFileType.MEETING.getValue(),showName);
        	docFile = TemplateUtil.createDoc(dataMap,Constant.Template.ASSIST.getKey(),path + File.separator +relativeFileUrl);
        	if(docFile != null){

                saveFile.add(new SysFile(UUID.randomUUID().toString(),workprogramId, relativeFileUrl, showName,
                        Integer.valueOf(String.valueOf(docFile.length())), Constant.Template.OUTPUT_SUFFIX.getKey(),
                        signId,Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(), Constant.SysFileType.MEETING.getValue()));
            }
        	
	        //3、保存文件信息
	        if(saveFile.size() > 0){
	            Date now = new Date();
	            saveFile.forEach(sf->{
	                sf.setCreatedDate(now);
	                sf.setModifiedDate(now);
	                sf.setCreatedBy(SessionUtil.getLoginName());
	                sf.setModifiedBy(SessionUtil.getLoginName());
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
