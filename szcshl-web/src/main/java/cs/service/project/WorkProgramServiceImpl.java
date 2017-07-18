package cs.service.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.domain.expert.ExpertReview;
import cs.domain.project.*;
import cs.repository.repositoryImpl.project.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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
    @Autowired
    private MergeOptionRepo mergeOptionRepo;
    @Autowired
    private MergeOptionService mergeOptionService;

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
                    if(mergeOptionService.isMerge(workProgramDto.getId(),null,Constant.MergeType.WORK_PROGRAM.getValue())){
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，自评和单个评审不能关联其他工作方案，请先删除关联关系！");
                    }
                }
                // 3、合并评审
                if("合并评审".equals(workProgramDto.getIsSigle()) ){
                    //主项目
                    if (EnumState.YES.getValue().equals(workProgramDto.getIsMainProject())) {
                        if (!mergeOptionService.isHaveLink(workProgramDto.getId(), Constant.MergeType.WORK_PROGRAM.getValue())) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前工作方案为合并评审主方案，请先选择要合并评审的工作方案！");
                        }
                    //次项目
                    } else {
                        if (!mergeOptionService.isMerge(workProgramDto.getId(), null, Constant.MergeType.WORK_PROGRAM.getValue())) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前评审方式为合并评审次项目，请在主工作方案中挑选此工作方案为次工作方案再保存！");
                        }
                    }
                }
                workProgram = workProgramRepo.findById(workProgramDto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);
            } else {
                workProgram = new WorkProgram();
                BeanCopierUtils.copyProperties(workProgramDto, workProgram);
                workProgram.setId(UUID.randomUUID().toString());
                workProgram.setCreatedBy(currentUser.getLoginUser().getId());
                workProgram.setCreatedDate(now);
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

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！",workProgramDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，获取项目信息失败，请联系相关人员处理！");
        }
    }

    /**
     * 根据收文ID初始化用户待处理的工作方案
     */
    @Override
    public WorkProgramDto initWorkProgram(String signId, String workProgramId) {
        WorkProgramDto workProgramDto = new WorkProgramDto();
        //如果有工作方案ID，则按工作方案ID查询
        if(Validate.isString(workProgramId)){
            WorkProgram workProgram = workProgramRepo.findById(workProgramId);
            initWorkProgramDto(workProgram,workProgramDto);

        //没有则按收文ID查询(主要是负责人填报环节)
        }else{
            //是否主项目负责人
            boolean isMainUser = signPrincipalService.isFlowPri(currentUser.getLoginUser().getId(), signId, EnumState.YES.getValue());
            boolean isHaveWP = false;
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" from "+WorkProgram.class.getSimpleName()+" where "+WorkProgram_.sign.getName()+"."+Sign_.signid.getName());
            hqlBuilder.append(" =:signId").setParam("signId",signId);

            List<WorkProgram> wpList = workProgramRepo.findByHql(hqlBuilder);
            if(wpList != null && wpList.size() > 0){
                for(WorkProgram wp : wpList){
                    if((EnumState.YES.getValue()).equals(wp.getIsMain()) && isMainUser){
                        initWorkProgramDto(wp,workProgramDto);
                        isHaveWP = true;
                        break;
                    }else if((EnumState.NO.getValue()).equals(wp.getIsMain()) && !isMainUser){
                        initWorkProgramDto(wp,workProgramDto);
                        isHaveWP = true;
                        break;
                    }
                }
            }

            //如果没有，则初始化
            if(!isHaveWP){
                Sign sign = signRepo.findById(Sign_.signid.getName(),signId);
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
        }

        return workProgramDto;
    }

    private void initWorkProgramDto(WorkProgram workProgram,WorkProgramDto workProgramDto){
        BeanCopierUtils.copyProperties(workProgram, workProgramDto);
        //初始化会议室预定情况
        List<RoomBooking> roomBookings = workProgram.getRoomBookings();
        if (roomBookings != null && roomBookings.size() > 0) {
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
    }

    /**
     * 查询可以选择的合并评审工作方案列表
     * @param mainBusinessId
     * @return
     */
    @Override
    public List<WorkProgramDto> waitSeleWP(String mainBusinessId) {
        List<WorkProgramDto> resultList = new ArrayList<>();
        Criteria criteria = workProgramRepo.getExecutableCriteria();
        criteria.add(Restrictions.ne(WorkProgram_.reviewType.getName(),"自评"));        //自评项目不能进行选择
        criteria.add(Restrictions.eq(WorkProgram_.isSigle.getName(),"单个评审"));       //单个评审的才能进行合并操作
        criteria.add(Restrictions.sqlRestriction(" (expertReviewId is null or expertReviewId = '' ) and id not in ( select " + MergeOption_.businessId.getName() + " from cs_merge_option where " + MergeOption_.mainBusinessId.getName() + " = '"+mainBusinessId+"' and "+MergeOption_.businessType.getName()+"='"+Constant.MergeType.WORK_PROGRAM.getValue()+"')"));
        List<WorkProgram> workProgramList = criteria.list();
        if(workProgramList != null ){
            workProgramList.forEach(wp ->{
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(wp.getSign(),signDto);
                WorkProgramDto nWPDto = new WorkProgramDto();
                BeanCopierUtils.copyProperties(wp,nWPDto);
                nWPDto.setSignDto(signDto);
                resultList.add(nWPDto);
            });
        }
        return resultList;
    }


    /**
     * 保存合并评审工作方案
     * @param mainBusinessId
     * @param signId
     * @param businessId
     * @param linkSignId
     */
    @Override
    @Transactional
    public void mergeWork(String mainBusinessId, String signId,String businessId,String linkSignId) {
        List<String> businessIdList = StringUtil.getSplit(businessId, ",");
        List<String> linkSignIdList = StringUtil.getSplit(linkSignId, ",");
        int totalLength = businessIdList == null ? 0 : businessIdList.size();
        List<MergeOption> saveList = new ArrayList<>(totalLength);

        if (totalLength > 0) {
            Date now = new Date();
            String createUserId = currentUser.getLoginUser().getId();
            //判断是否已经添加了主项目
            Criteria criteria = mergeOptionRepo.getExecutableCriteria();
            criteria.add(Restrictions.eq(MergeOption_.mainBusinessId.getName(), mainBusinessId));
            criteria.add(Restrictions.eq(MergeOption_.businessId.getName(), mainBusinessId));
            criteria.add(Restrictions.eq(MergeOption_.businessType.getName(), Constant.MergeType.WORK_PROGRAM.getValue()));

            MergeOption mergeOption = (MergeOption) criteria.uniqueResult();
            if (mergeOption == null || !Validate.isString(mergeOption.getId())) {
                mergeOption = new MergeOption();
                mergeOption.setBusinessId(mainBusinessId);
                mergeOption.setMainBusinessId(mainBusinessId);
                mergeOption.setSignId(signId);
                mergeOption.setBusinessType(Constant.MergeType.WORK_PROGRAM.getValue());
                mergeOption.setCreatedBy(createUserId);
                mergeOption.setModifiedBy(createUserId);
                mergeOption.setCreatedDate(now);
                mergeOption.setModifiedDate(now);
                saveList.add(mergeOption);
            }
            for (int i=0,l=businessIdList.size();i<l;i++) {
                MergeOption mergeOptionLk = new MergeOption();
                mergeOptionLk.setMainBusinessId(mainBusinessId);
                mergeOptionLk.setBusinessId(businessIdList.get(i));
                mergeOptionLk.setSignId(linkSignIdList.get(i));
                mergeOptionLk.setBusinessType(Constant.MergeType.WORK_PROGRAM.getValue());
                mergeOptionLk.setCreatedBy(createUserId);
                mergeOptionLk.setModifiedBy(createUserId);
                mergeOptionLk.setCreatedDate(now);
                mergeOptionLk.setModifiedDate(now);
                saveList.add(mergeOptionLk);
            }
        }

        if (Validate.isList(saveList)) {
            mergeOptionRepo.bathUpdate(saveList);
            //修改工作方案状态
            addMergeWPReview(mainBusinessId,businessId);
        }

    }

    /**
     * 删除合并评审
     * @param mainBusinessId
     * @param businessId
     */
    @Override
    @Transactional
    public void deleteMergeWork(String mainBusinessId, String businessId) {
        if (Validate.isString(mainBusinessId)) {
            boolean isDeleteAll = true;
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" delete from " + MergeOption.class.getSimpleName());
            hqlBuilder.append(" where " + MergeOption_.mainBusinessId.getName() + " =:mainBusinessId").setParam("mainBusinessId", mainBusinessId);
            hqlBuilder.append(" and "+MergeOption_.businessType.getName()+" =:businessType ").setParam("businessType",Constant.MergeType.WORK_PROGRAM.getValue());
            //删除合并评审ID
            if (Validate.isString(businessId)) {
                List<String> delLinkIds = StringUtil.getSplit(businessId, ",");
                int linkNum = mergeOptionService.getMainLinkNum(mainBusinessId,Constant.MergeType.WORK_PROGRAM.getValue());
                //不是删除所有合并评审的工作方案
                if((linkNum -1) > delLinkIds.size()){
                    isDeleteAll = false;
                    hqlBuilder.bulidIdString("and",MergeOption_.businessId.getName(),businessId);
                }
            }
            //更新所有删除合并评审的工作方案状态
            delMergeWPReview(mainBusinessId,isDeleteAll==true?null:businessId);

            //更改所有的合并评审改为单个评审
            mergeOptionRepo.executeHql(hqlBuilder);
        }
    }

    /**
     * 删除合并评审
     * @param mainBusinessId
     * @param businessId
     */
    private void delMergeWPReview(String mainBusinessId, String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE cs_work_program  SET "+WorkProgram_.isSigle.getName()+" = '单个评审', "+WorkProgram_.isMainProject.getName()+" = '"+EnumState.NO.getValue()+"',expertReviewId = null ");
        //如果只删除部分，则更改部分的工作方案状态
        if(Validate.isString(businessId)){
            sqlBuilder.bulidIdString("where",WorkProgram_.id.getName(),businessId);
        //如果删除所有，则主工作方案的评审方案不修改
        }else{
            sqlBuilder.append(" where " + WorkProgram_.id.getName() + " in ( ");
            sqlBuilder.append(" select "+MergeOption_.businessId.getName()+" from cs_merge_option where "+MergeOption_.mainBusinessId.getName()+" =:mainBusinessId ");
            sqlBuilder.setParam("mainBusinessId",mainBusinessId);
            sqlBuilder.append(" and "+MergeOption_.businessType.getName()+"=:businessType").setParam("businessType",Constant.MergeType.WORK_PROGRAM.getValue());
            sqlBuilder.append(" and "+MergeOption_.businessId.getName()+" <> :businessId").setParam("businessId",mainBusinessId);
            sqlBuilder.append(" )");
        }

        workProgramRepo.executeSql(sqlBuilder);
    }

    /**
     * 把关联的项目改为合并评审此项目，并更新评审方案
     * @param mainBusinessId
     * @param businessId
     */
    private void addMergeWPReview(String mainBusinessId,String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE cs_work_program  SET "+WorkProgram_.isSigle.getName()+" = '合并评审', "+WorkProgram_.isMainProject.getName()+" = '"+EnumState.NO.getValue()+"',");
        sqlBuilder.append(" expertReviewId = (select expertReviewId from cs_work_program where "+WorkProgram_.id.getName()+" =:mId ) ").setParam("mId",mainBusinessId);
        sqlBuilder.bulidIdString("where",WorkProgram_.id.getName(),businessId);
        workProgramRepo.executeSql(sqlBuilder);
    }

    /**
     * 获取已选合并评审项目列表
     * @param mainBusinessId
     * @return
     */
    @Override
    public List<WorkProgramDto> getSeleWPByMainId(String mainBusinessId) {
        List<WorkProgramDto> resultList = new ArrayList<>();
        Criteria criteria = workProgramRepo.getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(" id IN ( select " + MergeOption_.businessId.getName() + " from cs_merge_option where " + MergeOption_.mainBusinessId.getName() + " = '"+mainBusinessId+"' and "+MergeOption_.businessType.getName()+"='"+Constant.MergeType.WORK_PROGRAM.getValue()+"')"));
        List<WorkProgram> workProgramList = criteria.list();
        if(workProgramList != null ){
            workProgramList.forEach(wp ->{
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(wp.getSign(),signDto);
                WorkProgramDto nWPDto = new WorkProgramDto();
                BeanCopierUtils.copyProperties(wp,nWPDto);
                nWPDto.setSignDto(signDto);
                resultList.add(nWPDto);
            });
        }
        return resultList;
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
