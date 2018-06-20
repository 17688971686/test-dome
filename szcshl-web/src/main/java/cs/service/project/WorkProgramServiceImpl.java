package cs.service.project;

import cs.common.RandomGUID;
import cs.common.constants.Constant;
import cs.common.constants.Constant.EnumState;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelCondition_;
import cs.domain.expert.ExpertSelected_;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.*;
import cs.domain.sys.*;
import cs.domain.sys.Ftp_;
import cs.model.project.ProMeetDto;
import cs.model.project.ProMeetShow;
import cs.model.project.WorkProgramDto;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.sys.SysFileService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private SysFileService sysFileService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private ExpertSelConditionRepo expertSelConditionRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private SignDispaWorkService signDispaWorkService;
    @Autowired
    private FtpRepo ftpRepo;
    /**
     * 保存工作方案
     *
     * @param workProgramDto
     * @param isNeedWorkProgram
     * @return
     */
    @Override
    @Transactional
    public ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram) {
        if (Validate.isString(workProgramDto.getSignId())) {
            WorkProgram workProgram = null;
            Date now = new Date();
            if (Validate.isString(workProgramDto.getId())) {
                //1、自评的工作方案不能选择为合并评审
                if (Constant.MergeType.REVIEW_SELF.getValue().equals(workProgramDto.getReviewType()) && Constant.MergeType.REVIEW_MERGE.getValue().equals(workProgramDto.getIsSigle())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，自评不能选择合并评审，请重新选择评审方式！");
                }
                // 2、自评和单个评审不能有关联
                if (Constant.MergeType.REVIEW_SELF.getValue().equals(workProgramDto.getReviewType()) || Constant.MergeType.REVIEW_SIGNLE.getValue().equals(workProgramDto.getIsSigle())) {
                    if (signMergeRepo.isHaveMerge(workProgramDto.getSignId(), Constant.MergeType.WORK_PROGRAM.getValue())) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，自评和单个评审不能关联其他工作方案，请先在主工作方案中删除关联关系再修改！");
                    }
                }
                // 3、合并评审 次项目
                if (Constant.MergeType.REVIEW_MERGE.getValue().equals(workProgramDto.getIsSigle()) && EnumState.NO.getValue().equals(workProgramDto.getIsMainProject())) {
                    if (!signMergeRepo.checkIsMerege(workProgramDto.getSignId(), Constant.MergeType.WORK_PROGRAM.getValue())) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前评审方式为合并评审次项目，请在主工作方案中挑选此工作方案为次工作方案再保存！");
                    }
                }
                workProgram = workProgramRepo.findById(WorkProgram_.id.getName() ,workProgramDto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);

            } else {
                workProgram = new WorkProgram();
                BeanCopierUtils.copyProperties(workProgramDto, workProgram);
                workProgram.setId(UUID.randomUUID().toString());
                workProgram.setCreatedBy(SessionUtil.getUserId());
                workProgram.setCreatedDate(now);
                workProgram.setStudyQuantum(workProgramDto.getStudyQuantum());//调研时间段
            }

            workProgram.setModifiedBy(SessionUtil.getDisplayName());
            workProgram.setModifiedDate(now);
            //设置关联对象
            Sign sign = workProgram.getSign();
            if (sign == null) {
                sign = signRepo.findById(Sign_.signid.getName(), workProgramDto.getSignId());
            }
            //只有主方案改了，才会更新
            if ((FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue()).equals(workProgram.getBranchId())) {
                //如果有多个分支，则以总申报金额为准，否则，以该项目的申报金额为准
                BigDecimal compareInvestment = workProgram.getTotalInvestment() == null ? workProgram.getAppalyInvestment() : workProgram.getTotalInvestment();
                if (compareInvestment != null && (sign.getAppalyInvestment() == null || (sign.getAppalyInvestment().compareTo(compareInvestment) != 0))) {
                    sign.setAppalyInvestment(compareInvestment);
                }
            }
            //表示正在做工作方案
            sign.setProcessState(Constant.SignProcessState.DO_WP.getValue());
            workProgram.setSign(sign);
            if (Constant.MergeType.REVIEW_SELF.getValue().equals(workProgram.getReviewType())) {
                //清除专家费用，和协审会日期
                workProgram.setExpertCost(null);
                workProgram.setLetterDate(null);
            }

            workProgram.setBaseInfo(EnumState.NO.getValue());
            workProgramRepo.save(workProgram);

            //用于返回页面
            workProgramDto.setId(workProgram.getId());
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", workProgramDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，获取项目信息失败，请联系相关人员处理！");
        }
    }


    /**
     * 根据收文ID初始化 用户待处理的工作方案
     */
    @Override
    public Map<String, Object> initWorkProgram(String signId,String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        WorkProgramDto workProgramDto = new WorkProgramDto();
        String curUserId = SessionUtil.getUserId();
        List<User> priUserList = null;
        //分支
        String branchIndex = "";
        Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
        String mainBrandId = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
        String taskBrandIndex = task.getTaskDefinitionKey().substring(task.getTaskDefinitionKey().length()-1);
        boolean isMainBrand = taskBrandIndex.equals(mainBrandId);
        //1、根据收文ID查询出所有的工作方案ID
        Criteria criteria = workProgramRepo.getExecutableCriteria();
        criteria.createAlias(WorkProgram_.sign.getName(), WorkProgram_.sign.getName());
        criteria.add(Restrictions.eq(WorkProgram_.sign.getName() + "." + Sign_.signid.getName(), signId));
        List<WorkProgram> wpList = criteria.list();
        //2、是否有当前用户负责的工作方案
        boolean isHaveCurUserWP = false;
        WorkProgram mainW = null;
        if (Validate.isList(wpList)) {
            int totalL = wpList.size();
            //遍历第一遍，先找出主分支工作方案
            for (int i = 0; i < totalL; i++) {
                WorkProgram wp = wpList.get(i);
                if (mainBrandId.equals(wp.getBranchId())) {
                    mainW = wp;
                    break;
                }
            }
            if(!isMainBrand){
                wpList = wpList.stream().filter(item->(!Validate.isString(item.getBaseInfo()) || !EnumState.YES.getValue().equals(item.getBaseInfo()))).collect(Collectors.toList());
            }
            if(Validate.isList(wpList)){
                totalL = wpList.size();
                List<WorkProgramDto> wpDtoList = new ArrayList<>();
                for (int i = 0; i < totalL; i++) {
                    WorkProgram wp = wpList.get(i);
                    branchIndex = wp.getBranchId();
                    boolean isBrandUser = false;

                    if(taskBrandIndex.equals(branchIndex)){
                        priUserList =  signPrincipalService.getSignPriUser(signId,branchIndex);
                        for(User user : priUserList){
                            //当前处理人是代人人的时候也要考虑进去
                            if(user.getId().equals(curUserId) || curUserId.equals(user.getTakeUserId())){
                                isBrandUser = true;
                                break;
                            }
                        }
                    }

                    //如果是当前分支用户或者代办用户
                    if(isBrandUser){
                        BeanCopierUtils.copyProperties(wp, workProgramDto);
                        if (Validate.isString(mainW.getId()) && !FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(wp.getBranchId())) {
                            WorkProgramDto mainWPDto = new WorkProgramDto();
                            BeanCopierUtils.copyProperties(mainW, mainWPDto);
                            workProgramDto.setMainWorkProgramDto(mainWPDto);
                        }
                        workProgramRepo.initWPMeetingExp(workProgramDto, wp);
                        isHaveCurUserWP = true;
                    }else{
                        WorkProgramDto wpDto = new WorkProgramDto();
                        BeanCopierUtils.copyProperties(wp, wpDto);
                        if (Validate.isString(mainW.getId()) && !FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(wp.getBranchId())) {
                            WorkProgramDto mainWPDto = new WorkProgramDto();
                            BeanCopierUtils.copyProperties(mainW, mainWPDto);
                            wpDto.setMainWorkProgramDto(mainWPDto);
                        }
                        workProgramRepo.initWPMeetingExp(wpDto, wp);
                        wpDtoList.add(wpDto);
                    }
                }
                resultMap.put("WPList", wpDtoList);
            }
        }
        //3、如果还没填报工作方案，则初始化
        if (isHaveCurUserWP == false) {
            WorkProgramDto mainWPDto = new WorkProgramDto();
            Sign sign = signRepo.findById(Sign_.signid.getName(), signId);
            //取第一分支的负责人
            boolean isMainFlowPri = false;
            if(isMainBrand && Validate.isList(priUserList)){
            }else{
                priUserList= signPrincipalService.getSignPriUser(signId,FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
            }
            for(User user : priUserList){
                //当前处理人是代人人的时候也要考虑进去
                if(user.getId().equals(curUserId) || curUserId.equals(user.getTakeUserId())){
                    isMainFlowPri = true;
                    break;
                }
            }
            //项目基本信息
            workProgramDto.setProjectName(sign.getProjectname());
            workProgramDto.setBuildCompany(sign.getBuiltcompanyName());
            workProgramDto.setDesignCompany(sign.getDesigncompanyName());
            workProgramDto.setAppalyInvestment(sign.getDeclaration());
            workProgramDto.setWorkreviveStage(sign.getReviewstage());
            workProgramDto.setBranchId(taskBrandIndex);
            //默认名称
            workProgramDto.setTitleName(sign.getReviewstage() + Constant.WORKPROGRAM_NAME);
            workProgramDto.setTitleDate(new Date());

            //是否有拟补充资料函
            workProgramDto.setIsHaveSuppLetter(sign.getIsHaveSuppLetter() == null ? Constant.EnumState.NO.getValue() : sign.getIsHaveSuppLetter());
            //拟补充资料函发文日期
            workProgramDto.setSuppLetterDate(sign.getSuppLetterDate());

            if (isMainFlowPri) {
                //判断是否是分办给多个部门办理，如果是，则显示申报总投资金额
                if (signBranchRepo.countBranch(sign.getSignid()) > 1) {
                    workProgramDto.setTotalInvestment(sign.getDeclaration());
                    resultMap.put("showTotalInvestment", EnumState.YES.getValue());
                }
                copySignCommonInfo(workProgramDto, sign);
                //判断是否是关联次项目
                boolean isMerge = signMergeRepo.checkIsMerege(signId, Constant.MergeType.WORK_PROGRAM.getValue());
                if (isMerge) {
                    WorkProgram mainWP = workProgramRepo.findMainReviewWP(signId);
                    if (mainWP != null) {
                        workProgramDto.setReviewType(mainWP.getReviewType());           //评审方式要跟主项目一致
                    }
                    workProgramDto.setIsSigle(Constant.MergeType.REVIEW_MERGE.getValue());
                    workProgramDto.setIsMainProject(EnumState.NO.getValue());
                }
            } else {
                //通过主工作方案 初始化协办分支的公共部分
                if (mainW != null && Validate.isString(mainW.getId())) {
                    BeanCopierUtils.copyProperties(mainW, mainWPDto);
                } else {
                    copySignCommonInfo(mainWPDto, sign);
                }
            }
            workProgramDto.setMainWorkProgramDto(mainWPDto);
        }

        resultMap.put("eidtWP", workProgramDto);
        return resultMap;
    }


    /**
     * 根据收文ID查询
     */
    @Override
    public Map<String, Object> workMaintainList(String signId) {
        Map<String, Object> resultMap = new HashMap<>();
        //1、根据收文ID查询出所有的工作方案ID
        Criteria criteria = workProgramRepo.getExecutableCriteria();
        criteria.createAlias(WorkProgram_.sign.getName(), WorkProgram_.sign.getName());
        criteria.add(Restrictions.eq(WorkProgram_.sign.getName() + "." + Sign_.signid.getName(), signId));
        List<WorkProgram> wpList = criteria.list();
        WorkProgramDto workProgramDto = new WorkProgramDto();

        //2、是否有当前用户负责的工作方案
        WorkProgram mainW = new WorkProgram();
        //工作方案个数
        if (Validate.isList(wpList)) {
            int totalL = wpList.size();
            //遍历第一遍，先找出主分支工作方案
            for (int i = 0; i < totalL; i++) {
                WorkProgram wp = wpList.get(i);
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(wp.getBranchId())) {
                    mainW = wp;
                    break;
                }
            }
            wpList = wpList.stream().filter(item->(!Validate.isString(item.getBaseInfo()) || !EnumState.YES.getValue().equals(item.getBaseInfo()))).collect(Collectors.toList());
            if(Validate.isList(wpList)){
                totalL = wpList.size();
                resultMap.put("showTotalInvestment", (totalL)>1?"9":"0");
                List<WorkProgramDto> wpDtoList = new ArrayList<>();
                for (int i = 0; i < totalL; i++) {
                    WorkProgram wp = wpList.get(i);
                    WorkProgramDto wpDto = new WorkProgramDto();
                    BeanCopierUtils.copyProperties(wp, wpDto);
                    if (Validate.isString(mainW.getId()) && !FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(wp.getBranchId())) {
                        WorkProgramDto mainWPDto = new WorkProgramDto();
                        BeanCopierUtils.copyProperties(mainW, mainWPDto);
                        wpDto.setMainWorkProgramDto(mainWPDto);
                    } else {
                        BeanCopierUtils.copyProperties(wp, workProgramDto);
                        workProgramRepo.initWPMeetingExp(workProgramDto, wp);
                    }
                    workProgramRepo.initWPMeetingExp(wpDto, wp);
                    wpDtoList.add(wpDto);
                }
                resultMap.put("WPList", wpDtoList);
            }
        }
        resultMap.put("eidtWP", workProgramDto);
        return resultMap;
    }

    /**
     * 拷贝主要属性
     *
     * @param workProgramDto
     * @param sign
     */
    @Override
    public void copySignCommonInfo(WorkProgramDto workProgramDto, Sign sign) {
        //来文单位默认全部是：深圳市发展和改革委员会，可改...
        //联系人，就是默认签收表的那个主办处室联系人，默认读取过来但是这边可以给他修改，和主办处室联系人都是独立的两个字段
        workProgramDto.setSendFileUnit(Constant.SEND_FILE_UNIT);
        workProgramDto.setSendFileUser(sign.getMainDeptUserName());
        //获取评审部门
        workProgramDto.setReviewOrgName(signBranchRepo.getOrgDeptNameBySignId(sign.getSignid()));
        //项目第一负责人
        User mainUser = signPrincipalService.getMainPriUser(sign.getSignid());
        if (mainUser != null && Validate.isString(mainUser.getId())) {
            workProgramDto.setMianChargeUserName(mainUser.getDisplayName());
        }
        //项目其它负责人
        workProgramDto.setSecondChargeUserName(signPrincipalService.getAllSecondPriUserName(sign.getSignid()));
    }

    /**
     * 通过业务ID判断是不是主分支
     * @param signId
     * @return
     */
    @Override
    public Boolean mainBranch(String signId) {
        return workProgramRepo.mainBranch(signId);
    }

    /**
     * 更新工作方案专家评审费用
     * @param wpId
     */
    @Override
    public void initExpertCost(String wpId) {
        workProgramRepo.initExpertCost(wpId);
    }

    /**
     * 通过项目负责人获取项目信息
     *
     * @param signId
     * @return
     */
    @Override
    public WorkProgramDto findByPrincipalUser(String signId) {
        WorkProgram workProgram = workProgramRepo.findByPrincipalUser(signId);
        WorkProgramDto workProgramDto = new WorkProgramDto();
        if (workProgram != null) {
            BeanCopierUtils.copyProperties(workProgram, workProgramDto);
        }
        return workProgramDto;
    }

    /**
     * 根据合并评审主项目ID，获取合并评审次项目的工作方案信息
     *
     * @param signid
     * @return
     */
    @Override
    public List<WorkProgramDto> findMergeWP(String signid) {
        List<WorkProgram> wpList = workProgramRepo.findMergeWP(signid);
        if (Validate.isList(wpList)) {
            List<WorkProgramDto> resultList = new ArrayList<>(wpList.size());
            wpList.forEach(wp -> {
                WorkProgramDto wpDto = new WorkProgramDto();
                BeanCopierUtils.copyProperties(wp, wpDto);
                resultList.add(wpDto);
            });
            return resultList;
        }
        return null;
    }

    /**
     * 专家评审方式修改
     *
     * @param signId        项目ID
     * @param workprogramId 工作方案ID
     * @param reviewType    新的评审方式
     * @return
     */
    @Override
    @Transactional
    public ResultMsg updateReviewType(String signId, String workprogramId, String reviewType) {
        WorkProgram workProgram = workProgramRepo.findById(WorkProgram_.id.getName(), workprogramId);
        //原先是专家评审会
        if (Constant.MergeType.REVIEW_MEETING.getValue().equals(workProgram.getReviewType())
                && (Constant.MergeType.REVIEW_SELF.getValue().equals(reviewType)
                || Constant.MergeType.REVIEW_LEETER.getValue().equals(reviewType))) {
            //1、删除预定会议日期
            roomBookingRepo.deleteById(RoomBooking_.businessId.getName(), workprogramId);
        }

        //2、由专家评审会或者专家函评，改为自评
        if (Constant.MergeType.REVIEW_SELF.getValue().equals(reviewType)) {
            //删除抽取条件
            expertSelConditionRepo.deleteById(ExpertSelCondition_.businessId.getName(), workprogramId);
            //删除抽取记录
            expertSelectedRepo.deleteById(ExpertSelected_.businessId.getName(), workprogramId);
            //删除抽取专家时，确认该项目还有分支又抽取专家，如果没有，则删除评审专家记录
            if (expertReviewRepo.isReviewIsEmpty(signId)) {
                expertReviewRepo.deleteById(ExpertReview_.businessId.getName(), signId);
            }

            //如果原先是合并评审主项目，还要修改对应的次项目信息
            if (Constant.MergeType.REVIEW_MEETING.getValue().equals(workProgram.getReviewType())
                    && EnumState.YES.getValue().equals(workProgram.getIsMainProject())) {
                signDispaWorkService.deleteAllMerge(signId, Constant.MergeType.WORK_PROGRAM.getValue(), workprogramId);
                workProgram.setReviewType(Constant.MergeType.REVIEW_SIGNLE.getValue());
                workProgram.setIsMainProject(EnumState.NO.getValue());
            }
            //清除专家费用，和协审会日期
            workProgram.setExpertCost(null);
            workProgram.setLetterDate(null);
        }
        //3、更改评审方式
        workProgram.setReviewType(reviewType);
        workProgram.setExpertCost(null);
        workProgramRepo.save(workProgram);
        //4、回调函数对象
        WorkProgramDto workProgramDto = new WorkProgramDto();
        BeanCopierUtils.copyProperties(workProgram, workProgramDto);
        workProgramRepo.initWPMeetingExp(workProgramDto, workProgram);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", workProgramDto);
    }

    /**
     * 根据当前负责人，删除对应的工作方案信息
     *
     * @param signId
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteBySignId(String signId) {
        try{
            SignPrincipal signPrincipal = signPrincipalService.getPrincipalInfo(SessionUtil.getUserInfo().getId(), signId);
            if (signPrincipal == null) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您不是项目负责人，不能对工作方案进行操作！");
            }
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" delete from cs_work_program where signid =:signid and branchId =:branchId and (baseInfo is null or baseInfo !=:bstate )");
            sqlBuilder.setParam("signid", signId);
            sqlBuilder.setParam("branchId", signPrincipal.getFlowBranch());
            sqlBuilder.setParam("bstate",EnumState.YES.getValue());

            int result = workProgramRepo.executeSql(sqlBuilder);
            //不需要做工作方案
            signBranchRepo.isNeedWP(signId, signPrincipal.getFlowBranch(), EnumState.NO.getValue());
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
        }catch(Exception e){
            log.error("删除工作方案信息异常："+e.getMessage());
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作异常！异常信息已记录，请联系系统管理员处理！");
        }

    }

    /**
     * 删除工作方案信息
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        workProgramRepo.deleteById(WorkProgram_.id.getName(), id);
    }

    /**
     * TODO:目前只是做一个简单的模板生成，后期再完善
     * 生成会前准备材料
     *
     * @param signId
     * @return
     */
    @Override
    @Transactional
    public ResultMsg createMeetingDoc(String signId) {

        String result = "";

        Sign sign = signRepo.findById(Sign_.signid.getName(), signId);
        if (sign == null || StringUtil.isEmpty(sign.getSignid())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该项目已被删除");
        }
        if (sign.getWorkProgramList() != null && sign.getWorkProgramList().size() > 0) {

            for (WorkProgram workProgram : sign.getWorkProgramList()) {
                if (Constant.MergeType.REVIEW_MEETING.getValue().equals(workProgram.getReviewType())) {
                    sysFileService.deleteByBusinessIdAndBusinessType(signId, Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")");
                    //2、生成会前准备材料
                    List<SysFile> saveFile = new ArrayList<>();

                    //获得拟聘专家信息
                    List<Expert> expertList = expertRepo.findByBusinessId(workProgram.getId());

                    //获取项目第一负责人
                    User user = signPrincipalService.getMainPriUser(signId);
                    //获取所有第二负责人信息
                    List<User> secondUserList = signPrincipalService.getAllSecondPriUser(signId);

                    //获得会议信息
                    List<RoomBooking> roomBookings = roomBookingRepo.findByIds(RoomBooking_.businessId.getName(), workProgram.getId(), null);
                    Ftp f = ftpRepo.findById(Ftp_.ipAddr.getName(), sysFileService.findFtpId());

                    if (f == null) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件服务器无法连接，文件无法生成，请联系管理员处理", null);
                    }
                    //2.1 生成签到表
                    try{
                        SysFile sysFile1 = CreateTemplateUtils.createtTemplateSignIn(f, sign, workProgram);
                        if (sysFile1 != null && Validate.isString(sysFile1.getSysFileId())) {
                            saveFile.add(sysFile1);
                        }
                    }catch (Exception e){
                        result += result.length() > 0 ? ",签到表" : "签到表";
                    }


                    //2.2 生成主持人稿
                    try{
                        String expertGl = expertRepo.findExpertGlByBusiness(workProgram.getId());
                        SysFile sysFile2 = CreateTemplateUtils.createTemplateCompere(f, sign, workProgram, expertList, expertGl);
                        if (sysFile2 != null && Validate.isString(sysFile2.getSysFileId())) {
                            saveFile.add(sysFile2);
                        }
                    }catch (Exception e){
                        result += result.length() > 0 ? ",主持人手稿" : "主持人手稿";
                    }

                    //2.3 会议议程
                    try{
                        List<SysFile> sList = CreateTemplateUtils.createTemplateMeeting(f, sign, workProgram, roomBookings);
                        if (sList != null && sList.size() > 0) {
                            for (SysFile sysFile : sList) {
                                if (saveFile != null && Validate.isString(sysFile.getSysFileId())) {
                                    saveFile.add(sysFile);
                                }
                            }
                        }
                    }catch (Exception e){
                        result += result.length() > 0 ? ",会议议程" : "会议议程";
                    }

                    //2.4 邀请函
                    try{
                        SysFile invitation = CreateTemplateUtils.createTemplateInvitation(f, sign, workProgram, expertList, user, roomBookings ,  secondUserList);
                        if (invitation != null && Validate.isString(invitation.getSysFileId())) {
                            saveFile.add(invitation);
                        }
                    }catch (Exception e){
                        result += result.length() > 0 ? ",邀请函" : "邀请函";
                    }


                    //2.5 会议通知
                    try{
                        SysFile notice = CreateTemplateUtils.createTemplateNotice(f, sign, workProgram, user, roomBookings , secondUserList);
                        if (notice != null && Validate.isString(notice.getSysFileId())) {
                            saveFile.add(notice);
                        }
                    }catch (Exception e){
                        result += result.length() > 0 ? ",会议通知" : "会议通知";
                    }

                    //专家评审意见书
                    try{
                        SysFile expertReviewIdea = CreateTemplateUtils.createTemplateExpertReviewIdea(f, sign, workProgram);
                        if (expertReviewIdea != null && Validate.isString(expertReviewIdea.getSysFileId())) {
                            saveFile.add(expertReviewIdea);
                        }
                    }catch (Exception e){
                        result += result.length() > 0 ? ",评审意见书" : "评审意见书";
                    }

                    //相关单位会议通知
                    try{
                        SysFile notice = CreateTemplateUtils.createTemplateUnitNotice(f, sign, workProgram, user, roomBookings , secondUserList);
                        if (notice != null && Validate.isString(notice.getSysFileId())) {
                            saveFile.add(notice);
                        }
                    }catch (Exception e){
                        result += result.length() > 0 ? ",相关单位会议通知" : "相关单位会议通知";
                    }

                    //2.6协审协议书
                  /*  HqlBuilder queryaps = HqlBuilder.create();
                    queryaps.append(" from " + AssistPlanSign.class.getSimpleName() + " where " + AssistPlanSign_.signId.getName() + " =:signID");
                    queryaps.setParam("signID", signId);
                    List<AssistPlanSign> apsList = assistPlanSignRepo.findByHql(queryaps);
                    AssistUnit assistUnit = null;
                    Org org = null;
                    if (!StringUtil.isBlank(sign.getAssistdeptid())) {
                        assistUnit = assistUnitRepo.findById(sign.getAssistdeptid());//乙方
                    }
                    if (!StringUtil.isBlank(sign.getMaindepetid())) {

                        org = orgRepo.findById(sign.getMaindepetid());//甲方
                    }
                    SysFile sysFile3 = CreateTemplateUtils.createTemplateAssist(f,sign, workProgram, apsList, assistUnit, org);
                    if(sysFile3 != null  && Validate.isString(sysFile3.getSysFileId())){

                        saveFile.add(sysFile3);
                    }*/

                    //3、保存文件信息
                    if (saveFile.size() > 0) {
                        Date now = new Date();
                        saveFile.forEach(sf -> {
                            sf.setCreatedDate(now);
                            sf.setModifiedDate(now);
                            sf.setModifiedBy(SessionUtil.getLoginName());
                            sf.setCreatedBy(SessionUtil.getLoginName());
                            //先删除旧数据
                            sysFileRepo.delete(sf.getMainId(), sf.getBusinessId(), sf.getSysBusiType(), sf.getShowName());
                        });
                        sysFileRepo.bathUpdate(saveFile);
                    } else {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件服务器无法连接，文件无法生成，请联系管理员处理", null);
                    }
                    //4、更改工作方案状态
                    workProgram.setIsCreateDoc(EnumState.YES.getValue());
                    workProgramRepo.save(workProgram);
                }
            }
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "该项目没有工作方案，生成会前准备材料失败。" , null);
        }


        if(result.length() > 0){
            return new ResultMsg(true , Constant.MsgCode.OK.getValue() , result + "生成失败！" , null);
        }else{

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功" , null);
        }
    }

    /**
     * 根据ID初始Dto
     *
     * @param workId
     * @return
     */
    @Override
    public WorkProgramDto initWorkProgramById(String workId) {
        WorkProgram work = workProgramRepo.findById(WorkProgram_.id.getName(), workId);
        WorkProgramDto workDto = new WorkProgramDto();
        workDto.setSignId(work.getSign().getSignid());
        BeanCopierUtils.copyProperties(work, workDto);
        String mianBranchId = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
        if (Validate.isString(work.getBranchId()) && !mianBranchId.equals(work.getBranchId())) {
            WorkProgram mainWork = workProgramRepo.findBySignIdAndBranchId(work.getSign().getSignid(), mianBranchId, false);
            if (mainWork != null) {
                workDto.setProjectName(mainWork.getProjectName());
                workDto.setTotalInvestment(mainWork.getTotalInvestment());
                workDto.setSendFileUnit(mainWork.getSendFileUnit());
                workDto.setSendFileUser(mainWork.getSendFileUser());
                workDto.setBuildCompany(mainWork.getBuildCompany());
                workDto.setDesignCompany(mainWork.getDesignCompany());
                workDto.setMainDeptName(mainWork.getMainDeptName());
                workDto.setIsHaveEIA(mainWork.getIsHaveEIA());
                workDto.setProjectType(mainWork.getProjectType());
                workDto.setProjectSubType(mainWork.getProjectSubType());
                workDto.setIndustryType(mainWork.getIndustryType());
                workDto.setContactPerson(mainWork.getContactPerson());
                workDto.setContactPersonPhone(mainWork.getContactPersonPhone());
                workDto.setContactPersonTel(mainWork.getContactPersonTel());
                workDto.setContactPersonFax(mainWork.getContactPersonFax());
                workDto.setReviewOrgName(mainWork.getReviewOrgName());
                workDto.setMianChargeUserName(mainWork.getMianChargeUserName());
                workDto.setSecondChargeUserName(mainWork.getSecondChargeUserName());
            }
        }
        if (Constant.EnumState.NO.getValue().equals(workDto.getIsMainProject()) && EnumState.NO.equals(workDto.getIsSigle())) {//次项目
            //查找主项目信息，并且获取主项目的会议室信息和专家信息
            WorkProgram wp = workProgramRepo.findMainReviewWP(workDto.getSignId());
            workProgramRepo.initWPMeetingExp(workDto, wp);
        } else {
            workProgramRepo.initWPMeetingExp(workDto, work);
        }

        return workDto;
    }

    /**
     * 获取工作方案调研及会议信息
     * @return
     */
    @Override
    public Map<String,Object> findProMeetInfo(){
        Map resultMap = new HashMap<String,Object>();
        List<ProMeetDto> proAmMeetDtoList = workProgramRepo.findAmProMeetInfo();
        List<ProMeetDto> proPmMeetDtoList = workProgramRepo.findPmProMeetInfo();
        resultMap.put("proAmMeetDtoList",proAmMeetInfoUpdate(proAmMeetDtoList));
        resultMap.put("proPmMeetDtoList",proAmMeetInfoUpdate(proPmMeetDtoList));
        return  resultMap;
    }

    /**
     * 初始化项目基本信息
     * @param signId
     * @return
     */
    @Override
    public WorkProgramDto initBaseInfo(String signId) {
        WorkProgramDto workProgramDto = new WorkProgramDto();
        WorkProgram wk = workProgramRepo.findBySignIdAndBranchId(signId, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue(),true);
        if(!Validate.isObject(wk)){
            Sign sign = signRepo.findById(Sign_.signid.getName(), signId);

            workProgramDto.setSignId(signId);
            copySignCommonInfo(workProgramDto, sign);
            workProgramDto.setProjectName(sign.getProjectname());
            workProgramDto.setBuildCompany(sign.getBuiltcompanyName());
            workProgramDto.setDesignCompany(sign.getDesigncompanyName());
            workProgramDto.setAppalyInvestment(sign.getDeclaration());
            workProgramDto.setWorkreviveStage(sign.getReviewstage());
            workProgramDto.setBaseInfo(EnumState.YES.getValue());
            workProgramDto.setBranchId( FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
            workProgramDto.setSendFileUnit(Constant.SEND_FILE_UNIT);
            workProgramDto.setSendFileUser(sign.getMainDeptUserName());
            //默认名称
            workProgramDto.setTitleName(sign.getReviewstage() + Constant.WORKPROGRAM_NAME);
            workProgramDto.setTitleDate(new Date());
        }else{
            BeanCopierUtils.copyProperties(wk,workProgramDto);
        }
        return workProgramDto;
    }

    @Override
    public ResultMsg saveBaseInfo(WorkProgramDto workProgramDto) {
        String wpId = workProgramDto.getId();
        try{
            Sign sign = null;
            WorkProgram workProgram = null;
            if(Validate.isString(wpId)){
                workProgram = workProgramRepo.findById(wpId);
                sign = workProgram.getSign();
                BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto,workProgram);
                workProgram.setSign(sign);
            }else{
                workProgram = new WorkProgram();
                BeanCopierUtils.copyProperties(workProgramDto,workProgram);
                wpId = (new RandomGUID()).valueAfterMD5;
                workProgram.setId(wpId);
                sign = signRepo.findById(Sign_.signid.getName(),workProgramDto.getSignId());
                workProgram.setSign(sign);
                workProgram.setCreatedBy(SessionUtil.getUserId());
                workProgram.setCreatedDate(new Date());
            }
            workProgram.setBaseInfo(EnumState.YES.getValue());
            workProgram.setModifiedBy(SessionUtil.getDisplayName());
            workProgram.setModifiedDate(new Date());
            workProgramRepo.save(workProgram);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),wpId,"保存失败，异常信息已记录，请联系管理员处理！",null);
        }catch(Exception e){
            e.printStackTrace();
            log.error("保存项目基本信息异常："+e.getMessage());
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败，异常信息已记录，请联系管理员处理！");
        }
    }

    private List<ProMeetShow> proAmMeetInfoUpdate(List<ProMeetDto>proMeetDtoList ){
        List<ProMeetShow> proMeetShowList = new ArrayList<ProMeetShow>();
        String[] dateArr = initMeetDateArr();
        ProMeetShow proMeetShow = new ProMeetShow();
        int temp = 1;
        for(int i = 0;i < proMeetDtoList.size();i++ ){
            if(!(temp == proMeetDtoList.get(i).getInnerSeq().intValue())){
                temp++;
                proMeetShowList.add(proMeetShow);
                proMeetShow = new ProMeetShow();
            }
            if(temp == proMeetDtoList.get(i).getInnerSeq().intValue()){
                if(dateArr[0].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(StringUtil.isNotBlank(proMeetDtoList.get(i).getRbName())){
                        proMeetShow.setProName1(proMeetDtoList.get(i).getRbName()+"("+proMeetDtoList.get(i).getAddressName()+")");
                    }else if(StringUtil.isNotBlank(proMeetDtoList.get(i).getProName())){
                        proMeetShow.setProName1(proMeetDtoList.get(i).getProName()+"项目调研");
                    }
                    if((i+1)== proMeetDtoList.size()){
                        proMeetShowList.add(proMeetShow);
                    }
                    continue;
                }else if(dateArr[1].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(StringUtil.isNotBlank(proMeetDtoList.get(i).getRbName())){
                        proMeetShow.setProName2(proMeetDtoList.get(i).getRbName()+"("+proMeetDtoList.get(i).getAddressName()+")");
                    }else if(StringUtil.isNotBlank(proMeetDtoList.get(i).getProName())){
                        proMeetShow.setProName2(proMeetDtoList.get(i).getProName()+"项目调研");
                    }
                    if((i+1)== proMeetDtoList.size()){
                        proMeetShowList.add(proMeetShow);
                    }
                    continue;
                }else if(dateArr[2].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(StringUtil.isNotBlank(proMeetDtoList.get(i).getRbName())){
                        proMeetShow.setProName3(proMeetDtoList.get(i).getRbName()+"("+proMeetDtoList.get(i).getAddressName()+")");
                    }else if(StringUtil.isNotBlank(proMeetDtoList.get(i).getProName())){
                        proMeetShow.setProName3(proMeetDtoList.get(i).getProName()+"项目调研");
                    }
                    if((i+1)== proMeetDtoList.size()){
                        proMeetShowList.add(proMeetShow);
                    }
                    continue;
                }else if(dateArr[3].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(StringUtil.isNotBlank(proMeetDtoList.get(i).getRbName())){
                        proMeetShow.setProName4(proMeetDtoList.get(i).getRbName()+"("+proMeetDtoList.get(i).getAddressName()+")");
                    }else if(StringUtil.isNotBlank(proMeetDtoList.get(i).getProName())){
                        proMeetShow.setProName4(proMeetDtoList.get(i).getProName()+"项目调研");
                    }
                    if((i+1)== proMeetDtoList.size()){
                        proMeetShowList.add(proMeetShow);
                    }
                    continue;
                }else if(dateArr[4].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(StringUtil.isNotBlank(proMeetDtoList.get(i).getRbName())){
                        proMeetShow.setProName5(proMeetDtoList.get(i).getRbName()+"("+proMeetDtoList.get(i).getAddressName()+")");
                    }else if(StringUtil.isNotBlank(proMeetDtoList.get(i).getProName())){
                        proMeetShow.setProName5(proMeetDtoList.get(i).getProName()+"项目调研");
                    }
                    if((i+1)== proMeetDtoList.size()){
                        proMeetShowList.add(proMeetShow);
                    }
                    continue;
                }
            }else{
                if(dateArr[0].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(proMeetShowList.size() > 0){
                        for(int j = 0;j < proMeetShowList.size();j++){
                            if(StringUtil.isBlank(proMeetShowList.get(j).getProName1())) {
                                proMeetShowList.get(j).setProName1(proMeetDtoList.get(i).getProName()+"项目调研");
                                break;
                            }
                            if((j+1) == proMeetShowList.size()){
                                proMeetShow = new ProMeetShow();
                                proMeetShow.setProName1(proMeetDtoList.get(i).getProName()+"项目调研");
                                proMeetShowList.add(proMeetShow);
                                break;
                            }
                        }
                    }else{
                        proMeetShow = new ProMeetShow();
                        proMeetShow.setProName1(proMeetDtoList.get(i).getProName()+"项目调研");
                        proMeetShowList.add(proMeetShow);

                    }
                }else if(dateArr[1].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(proMeetShowList.size() > 0){
                        for(int j = 0;j < proMeetShowList.size();j++){
                            if(StringUtil.isBlank(proMeetShowList.get(j).getProName2())) {
                                proMeetShowList.get(j).setProName2(proMeetDtoList.get(i).getProName()+"项目调研");
                                break;
                            }
                            if((j+1) == proMeetShowList.size()){
                                proMeetShow = new ProMeetShow();
                                proMeetShow.setProName2(proMeetDtoList.get(i).getProName()+"项目调研");
                                proMeetShowList.add(proMeetShow);
                                break;
                            }
                        }
                    }else{
                        proMeetShow = new ProMeetShow();
                        proMeetShow.setProName2(proMeetDtoList.get(i).getProName()+"项目调研");
                        proMeetShowList.add(proMeetShow);
                    }
                }else if(dateArr[2].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(proMeetShowList.size() > 0){
                        for(int j = 0;j < proMeetShowList.size();j++){
                            if(StringUtil.isBlank(proMeetShowList.get(j).getProName3())) {
                                proMeetShowList.get(j).setProName3(proMeetDtoList.get(i).getProName()+"项目调研");
                                break;
                            }
                            if((j+1) == proMeetShowList.size()){
                                proMeetShow = new ProMeetShow();
                                proMeetShow.setProName3(proMeetDtoList.get(i).getProName()+"项目调研");
                                proMeetShowList.add(proMeetShow);
                                break;
                            }
                        }
                    }else{
                        proMeetShow = new ProMeetShow();
                        proMeetShow.setProName3(proMeetDtoList.get(i).getProName()+"项目调研");
                        proMeetShowList.add(proMeetShow);
                    }
                }else if(dateArr[3].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(proMeetShowList.size() > 0){
                        for(int j = 0;j < proMeetShowList.size();j++){
                            if(StringUtil.isBlank(proMeetShowList.get(j).getProName4())) {
                                proMeetShowList.get(j).setProName4(proMeetDtoList.get(i).getProName()+"项目调研");
                                break;
                            }
                            if((j+1) == proMeetShowList.size()){
                                proMeetShow = new ProMeetShow();
                                proMeetShow.setProName4(proMeetDtoList.get(i).getProName()+"项目调研");
                                proMeetShowList.add(proMeetShow);
                                break;
                            }
                        }
                    }else{
                        proMeetShow = new ProMeetShow();
                        proMeetShow.setProName4(proMeetDtoList.get(i).getProName()+"项目调研");
                        proMeetShowList.add(proMeetShow);
                    }
                }else if(dateArr[4].equals(DateUtils.converToString(proMeetDtoList.get(i).getProMeetDate(),""))){
                    if(proMeetShowList.size() > 0){
                        for(int j = 0;j < proMeetShowList.size();j++){
                            if(StringUtil.isBlank(proMeetShowList.get(j).getProName5())) {
                                proMeetShowList.get(j).setProName5(proMeetDtoList.get(i).getProName()+"项目调研");
                                break;
                            }
                            if((j+1) == proMeetShowList.size()){
                                proMeetShow = new ProMeetShow();
                                proMeetShow.setProName5(proMeetDtoList.get(i).getProName()+"项目调研");
                                proMeetShowList.add(proMeetShow);
                                break;
                            }
                        }
                    }else{
                        proMeetShow = new ProMeetShow();
                        proMeetShow.setProName5(proMeetDtoList.get(i).getProName()+"项目调研");
                        proMeetShowList.add(proMeetShow);
                    }
                }

            }
        }

        return proMeetShowList;

    }

    /***
     * 初始化日期数组
     * @return
     */
    private String[]  initMeetDateArr(){
        String dateStr[] = new String[5];
        for(int i = 0;i < dateStr.length;i++){
            dateStr[i] = DateUtils.converToString(DateUtils.addDay(new Date(),i),"");
        }
        return dateStr;
    }


}
