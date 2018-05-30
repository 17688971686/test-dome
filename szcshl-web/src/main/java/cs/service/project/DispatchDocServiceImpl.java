package cs.service.project;

import cs.common.constants.Constant;
import cs.common.constants.Constant.EnumState;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.constants.SysConstants;
import cs.common.ftp.ConfigProvider;
import cs.common.ftp.FtpClientConfig;
import cs.common.ftp.FtpUtils;
import cs.common.utils.*;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.domain.external.Dept;
import cs.domain.project.*;
import cs.domain.sys.Ftp;
import cs.domain.sys.Ftp_;
import cs.domain.sys.SysFile;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.service.external.DeptService;
import cs.service.sys.SysConfigService;
import cs.service.sys.SysFileService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cs.common.constants.Constant.*;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

@Service
public class DispatchDocServiceImpl implements DispatchDocService {
    private static Logger log = Logger.getLogger(DispatchDocServiceImpl.class);
    @Autowired
    private DispatchDocRepo dispatchDocRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private SignService signService;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private SysFileRepo sysFileRepo;
    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private FtpRepo ftpRepo;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private ProjMaxSeqRepo projMaxSeqRepo;

    @Autowired
    private ExpertReviewRepo expertReviewRepo;

    @Autowired
    private DeptService deptService;

    /**
     * 生成发文编号，生成发文编号之前，要先上传项目评审意见
     *
     * @param signId
     * @param dispatchId
     * @return
     */
    @Override
    @Transactional
    public ResultMsg fileNum(String signId, String dispatchId) {
        DispatchDoc dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(), dispatchId);
        if (dispatchDoc == null || !Validate.isString(dispatchDoc.getId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取收文信息");
        }
        if (Constant.MergeType.DIS_MERGE.getValue().equals(dispatchDoc.getDispatchWay()) &&
                !EnumState.YES.getValue().equals(dispatchDoc.getIsMainProject())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前项目为合并发文次项目，由主项目生成发文编号！");
        }

        if (Validate.isString(dispatchDoc.getFileNum())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该发文已经生成了发文编号！");
        }

        Date now = new Date();
        if(!Validate.isObject(dispatchDoc.getDispatchDate())){
            dispatchDoc.setDispatchDate(now);
        }
        //是否是设备清单（国产设备的发文编号为：深投审设[xxxx],其它阶段为：深投审[xxxx],）
        String seqType = EnumState.PROCESS.getValue();
        String fileNum = DISPATCH_PREFIX;
        if (DEVICE_BILL_HOMELAND.equals(dispatchDoc.getDispatchStage()) || DEVICE_BILL_IMPORT.equals(dispatchDoc.getDispatchStage())) {
            seqType = EnumState.STOP.getValue();
            fileNum = DISPATCH_EQUIPMENT_PREFIX;
        }
        String yearName = DateUtils.converToString(dispatchDoc.getDispatchDate(), DateUtils.DATE_YEAR);
        int maxSeq = dispatchDocRepo.getMaxSeq(yearName,seqType) + 1;

        fileNum = fileNum + "[" + yearName + "]" + maxSeq;
        dispatchDoc.setFileNum(fileNum);
        dispatchDoc.setFileSeq(maxSeq);
        //更新发文日期
        dispatchDoc.setDispatchDate(now);
        dispatchDocRepo.save(dispatchDoc);
        //如果是合并发文，则更新所有关联的发文编号
        if (Constant.MergeType.DIS_MERGE.getValue().equals(dispatchDoc.getDispatchWay())) {
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" update cs_dispatch_doc set " + DispatchDoc_.fileNum.getName() + " =:fileNum ").setParam("fileNum", dispatchDoc.getFileNum());
            sqlBuilder.append(" ," + DispatchDoc_.fileSeq.getName() + " =:fileSeq").setParam("fileSeq", dispatchDoc.getFileSeq());
            sqlBuilder.append(" where signId in (select mergeId from cs_sign_merge where signId = :signId ");
            sqlBuilder.setParam("signId", signId);
            sqlBuilder.append(" and mergeType =:mergeType )").setParam("mergeType", Constant.MergeType.DISPATCH.getValue());
            dispatchDocRepo.executeSql(sqlBuilder);
        }
        //更改项目信息
        Sign sign = signRepo.findById(Sign_.signid.getName(), signId);
        //1、流程状态修改
        sign.setProcessState(Constant.SignProcessState.END_DIS_NUM.getValue());
        //2、发文日期
        sign.setDispatchdate(now);
        sign.setDocnum(fileNum);
        //3、把亮灯状态去掉
        sign.setIsLightUp(signEnumState.NOLIGHT.getValue());
        //4、发文后剩余工作日
        sign.setDaysafterdispatch(sign.getSurplusdays());
        signRepo.save(sign);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！请立刻添加发文号到评审意见（审核意见）并重新上传。", fileNum);
    }

    // 保存发文拟稿
    @Override
    @Transactional
    public ResultMsg save(DispatchDocDto dispatchDocDto) {
        if (Validate.isString(dispatchDocDto.getSignId())) {
            //1、先进行业务判断
            // 单个发文
            if (Constant.MergeType.DIS_SINGLE.getValue().equals(dispatchDocDto.getDispatchWay())) {
                if (signMergeRepo.isHaveMerge(dispatchDocDto.getSignId(), Constant.MergeType.DISPATCH.getValue())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，单个发文不能关联其他项目，请先删除关联项目再操作！");
                }
                // 合并发文次项目一定要关联
            } else if (Constant.MergeType.DIS_MERGE.getValue().equals(dispatchDocDto.getDispatchWay()) && EnumState.NO.getValue().equals(dispatchDocDto.getIsMainProject())) {
                if (!signMergeRepo.checkIsMerege(dispatchDocDto.getSignId(), Constant.MergeType.DISPATCH.getValue())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前出文方式为合并发文次项目，请在主项目挑选此项目为次项目再发文！");
                }
            }
            //2、执行保存操作
            Date now = new Date();
            DispatchDoc dispatchDoc = null;
            if (!Validate.isString(dispatchDocDto.getId())) {
                dispatchDoc = new DispatchDoc();
                BeanCopierUtils.copyProperties(dispatchDocDto, dispatchDoc);
                dispatchDoc.setId(UUID.randomUUID().toString());
                dispatchDoc.setDraftDate(now);
                dispatchDoc.setCreatedBy(SessionUtil.getLoginName());
                dispatchDoc.setCreatedDate(now);
                dispatchDocDto.setId(dispatchDoc.getId());
            } else {
                dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(), dispatchDocDto.getId());
                BeanCopierUtils.copyPropertiesIgnoreProps(dispatchDocDto, dispatchDoc, SysConstants.defaultIgnore);
            }
            dispatchDoc.setModifiedBy(SessionUtil.getLoginName());
            dispatchDoc.setModifiedDate(now);

            //完成发文
            Sign sign = signRepo.findById(Sign_.signid.getName(), dispatchDocDto.getSignId());
            //如果是未生成发文编号的，没有发文日期
            if(null != dispatchDoc.getDispatchDate()){
                boolean isSuperUser = SUPER_ACCOUNT.equals(SessionUtil.getLoginName());
                //如果是负责人提交，则要更新填报的发文日期
                if(!isSuperUser){
                    dispatchDoc.setDispatchDate(now);
                }
                //如果已经生成发文编号，则要更新
                if(sign.getProcessState() >= SignProcessState.END_DIS_NUM.getValue()){
                    sign.setDispatchdate(dispatchDoc.getDispatchDate());
                }
            }

            //如果还没更新状态，则更新，已更新状态的，则不做改动
            if(sign.getProcessState() < Constant.SignProcessState.DO_DIS.getValue()){
                sign.setProcessState(Constant.SignProcessState.DO_DIS.getValue());
            }
            sign.setDispatchDoc(dispatchDoc);
            // 收文、工作方案(主项目)、发文的报审金额一致
            List<WorkProgram> workProgrmList = sign.getWorkProgramList();
            if (Validate.isList(workProgrmList)) {
                for (WorkProgram workProgram : workProgrmList) {
                    if (workProgram.getBranchId() == FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue()) {
                        workProgram.setAppalyInvestment(dispatchDocDto.getDeclareValue());
                    }
                }
            }
            sign.setAppalyInvestment(dispatchDocDto.getDeclareValue());
            sign.setAuthorizeValue(dispatchDocDto.getAuthorizeValue());
            sign.setWorkProgramList(workProgrmList);
            dispatchDoc.setSign(sign);
            dispatchDocRepo.save(dispatchDoc);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", dispatchDocDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取收文信息，请联系管理员处理！");
        }

    }

    /**
     * 初始化发文编辑页面
     * @param signId
     * @return
     */
    @Override
    public Map<String, Object> initDispatchData(String signId) {
        Date now = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        DispatchDocDto dispatchDto = new DispatchDocDto();
        //1、获取发文信息
        Sign sign = signRepo.findById(signId);
        DispatchDoc dispatch = sign.getDispatchDoc();
        //2、如果为空，则初始化发文数据
        if (dispatch == null || !Validate.isString(dispatch.getId())) {
            dispatch = new DispatchDoc();
            //(1)、判断项目是否为关联项目
            boolean isMerge = signMergeRepo.checkIsMerege(signId, MergeType.DISPATCH.getValue());
            if (isMerge) {
                dispatch.setDispatchWay(MergeType.DIS_MERGE.getValue());   //合并发文
            } else {
                dispatch.setDispatchWay(MergeType.DIS_SINGLE.getValue());    //单个发文
            }
            dispatch.setIsMainProject(EnumState.NO.getValue());
            //是否已经有阶段关联
            dispatch.setIsRelated(signRepo.checkIsLink(signId) ? EnumState.YES.getValue() : EnumState.NO.getValue());
            dispatch.setDraftDate(now);
            dispatch.setDispatchType("项目发文");
            //年度计划、紧急程度
            dispatch.setYearPlan(sign.getYearplantype());
            dispatch.setSecretLevel(sign.getSecrectlevel());
            dispatch.setUrgentLevel(sign.getUrgencydegree());
            //申报金额，与工作方案的、收文的一致，任何一个地方改了，都要同步更新
            dispatch.setDeclareValue(sign.getAppalyInvestment());
            //发文范围(艾传荣副巡视员（默认） + 投资处（获取收文主办室处） + 中心领导（默认）)
            String dispatchScope = "";
            Dept generalDept = deptService.findByDeptName("分管副主任");
            if (generalDept != null) {
                dispatchScope = generalDept.getDeptUserName() == null ? "" : generalDept.getDeptUserName() + "  ";
            }
            dispatchScope += sign.getMaindeptName() == null ? "" : sign.getMaindeptName() + "  ";
            dispatchScope += Constant.DIS_SCOPE_ZXLD;
            dispatch.setDispatchScope(dispatchScope);
            dispatch.setPrintCount(5);
            dispatch.setBranchCount(sign.getBranchCount());
            //发文标题
            String fileTitle = "深圳市投资项目评审中心关于";
            fileTitle += sign.getProjectname() == null ? "" : sign.getProjectname();
            if (Constant.STAGE_BUDGET.equals(sign.getReviewstage())) {
                fileTitle += "项目的总概算的审核意见";
            } else {
                fileTitle += "项目的评审意见";
            }
            dispatch.setFileTitle(fileTitle);

            // 获取当前用户信息
            dispatch.setUserName(SessionUtil.getDisplayName());
            dispatch.setUserId(SessionUtil.getUserId());
            dispatch.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
            dispatch.setOrgId(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getId());

        } else {
            String related = signRepo.checkIsLink(signId) ? EnumState.YES.getValue() : EnumState.NO.getValue();
            if (!related.equals(dispatch.getIsRelated())) {
                dispatch.setIsRelated(related);
                dispatchDocRepo.save(dispatch);
            }
        }
        dispatch.setDispatchStage(sign.getReviewstage());//评审阶段
        BeanCopierUtils.copyProperties(dispatch, dispatchDto);
        dispatchDto.setSignId(signId);
        map.put("dispatch", dispatchDto);

        //如果评审阶段是项目建议书、可研、概算和资金申请报告的，才关联到前一阶段
        String reviewStage = sign.getReviewstage();
        if (Validate.isString(reviewStage) && sign.getAssociateSign() != null &&
                (Constant.STAGE_STUDY.equals(reviewStage) || Constant.STAGE_BUDGET.equals(reviewStage)
                        || Constant.STAGE_SUG.equals(reviewStage) || Constant.APPLY_REPORT.equals(reviewStage))) {
            List<Sign> associateSigns = signService.getAssociates(sign.getAssociateSign().getSignid());
            if (associateSigns != null && associateSigns.size() > 0) {
                List<DispatchDocDto> associateDispatchDtos = new ArrayList<DispatchDocDto>(associateSigns.size());
                associateSigns.forEach(associateSign -> {
                    Sign asSign = signRepo.getById(associateSign.getSignid());
                    DispatchDoc associateDispatch = asSign.getDispatchDoc();
                    if (associateDispatch != null && associateDispatch.getId() != null) {
                        //关联发文
                        DispatchDocDto associateDis = new DispatchDocDto();
                        BeanCopierUtils.copyProperties(associateDispatch, associateDis);
                        SignDto signDto = new SignDto();
                        signDto.setReviewstage(asSign.getReviewstage());
                        associateDis.setSignDto(signDto);
                        associateDispatchDtos.add(associateDis);
                    }
                });
                map.put("associateDispatchs", associateDispatchDtos);
            }
        }

        SignDto signDto = new SignDto();
        BeanUtils.copyProperties(sign, signDto, new String[]{Sign_.workProgramList.getName(),
                Sign_.dispatchDoc.getName(), Sign_.fileRecord.getName(), Sign_.associateSign.getName()});
        map.put("sign", signDto);
        return map;
    }

    @Override
    public DispatchDocDto initDispatchBySignId(String signId) {
        DispatchDocDto dispatchDocDto = new DispatchDocDto();

        DispatchDoc dispatchDoc = dispatchDocRepo.findById("signid", signId);
        if (dispatchDoc != null) {
            BeanCopierUtils.copyProperties(dispatchDoc, dispatchDocDto);
        }

        return dispatchDocDto;
    }

    /**
     * 生成发文模板
     *
     * @param signId
     * @return
     */
    @Override
    public ResultMsg createDisPatchTemplate(String signId) {
        //如果无法连接ftp,不进行任何操作
        Ftp f = ftpRepo.findById(Ftp_.ipAddr.getName(), sysFileService.findFtpId());
        if (f == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件服务器无法连接，评审报告文件无法自动生成，请联系管理员处理", null);
        }
        FtpClientConfig k = ConfigProvider.getUploadConfig(f);
        FtpUtils ftpUtils = new FtpUtils();
        boolean isLink = ftpUtils.checkLink(k);
        if(!isLink){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "文件服务器无法连接，评审报告文件无法自动生成，请联系管理员处理", null);
        }

        //先通过业务ID删除之前生成的评审报告文件
        sysFileService.deleteByBusinessIdAndBusinessType(signId, Constant.SysFileType.TEMOLATE.getValue());
        Sign sign = signRepo.findById(Sign_.signid.getName(), signId);

        //主分支工作方案
        WorkProgram workProgram = null;
        List<WorkProgram> workProgramList = sign.getWorkProgramList();
        if (workProgramList != null && workProgramList.size() > 0) {
            for (int i = 0; i < workProgramList.size(); i++) {
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(workProgramList.get(i).getBranchId())) {
                    workProgram = workProgramList.get(i);
                    break;
                }
            }
        }

        //获取评审组名单中的总顾问和顾问
        String generalCounsel = ""; //总顾问
        String counselor = ""; //顾问
        Dept generalDept = deptService.findByDeptName("分管副主任");
        if (generalDept != null) {
            generalCounsel = generalDept.getDeptUserName() == null ? " " : generalDept.getDeptUserName();
        }

        Dept dept = deptService.findByDeptName(sign.getMaindeptName());
        if (dept != null) {
            counselor = dept.getDeptUserName() == null ? "" : dept.getDeptUserName();
        }
        List<SysFile> sysFileList = new ArrayList<>();
        //获取专家评审方案
        ExpertReview expertReview = expertReviewRepo.findByBusinessId(signId);
        List<ExpertSelected> expertSelectedList = null;
        //获得拟聘专家信息
        if(Validate.isObject(expertReview) && Validate.isList(expertReview.getExpertSelectedList())){
            expertSelectedList = expertReview.getExpertSelectedList();
        }

        StringBuffer result = new StringBuffer();
        int errorCount = 0;
        switch (sign.getReviewstage()){
            //可行性研究报告
            case Constant.STAGE_STUDY:
                SysFile studyOpinion = CreateTemplateUtils.createStudyTemplateOpinion(f, sign);
                if (studyOpinion != null && Validate.isString(studyOpinion.getSysFileId())) {
                    sysFileList.add(studyOpinion);
                }else{
                    errorCount ++ ;
                    result.append("【评审意见】");
                }

                SysFile studyEstimate = CreateTemplateUtils.createStudyTemplateEstimate(f, sign);
                if (studyEstimate != null && Validate.isString(studyEstimate.getSysFileId())) {
                    sysFileList.add(studyEstimate);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【投资估算表】");
                }

                SysFile studyRoster = CreateTemplateUtils.createStudyTemplateRoster(f, sign, expertSelectedList, workProgram, generalCounsel, counselor);
                if (studyRoster != null && Validate.isString(studyRoster.getSysFileId())) {
                    sysFileList.add(studyRoster);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【评审组名单】");
                }
                break;
            //项目概算
            case Constant.STAGE_BUDGET:
                SysFile budgetEstimate = CreateTemplateUtils.createBudgetTemplateEstimate(f, sign);
                if (budgetEstimate != null && Validate.isString(budgetEstimate.getSysFileId())) {
                    sysFileList.add(budgetEstimate);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【投资估算表】");
                }

                SysFile budgetOpinion = CreateTemplateUtils.createBudgetTemplateOpinion(f, sign);
                if (budgetOpinion != null && Validate.isString(budgetOpinion.getSysFileId())) {
                    sysFileList.add(budgetOpinion);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【评审意见】");
                }

                SysFile budgetProjectCost = CreateTemplateUtils.createBudgetTemplateProjectCost(f, sign);
                if (budgetProjectCost != null && Validate.isString(budgetProjectCost.getSysFileId())) {
                    sysFileList.add(budgetProjectCost);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【建安工程费用】");
                }

                SysFile budgetRoster = CreateTemplateUtils.createBudgetTemplateRoster(f, sign, expertSelectedList, workProgram, generalCounsel, counselor);
                if (budgetRoster != null && Validate.isString(budgetRoster.getSysFileId())) {
                    sysFileList.add(budgetRoster);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【评审组名单】");
                }
                break;
            //资金申请报告
            case Constant.APPLY_REPORT:
                SysFile reportEstimate = CreateTemplateUtils.createReportTemplateEstimate(f, sign);
                if (reportEstimate != null && Validate.isString(reportEstimate.getSysFileId())) {
                    sysFileList.add(reportEstimate);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【投资估算表】");
                }

                SysFile reportOpinion = CreateTemplateUtils.createReportTemplateOpinion(f, sign);
                if (reportOpinion != null && Validate.isString(reportOpinion.getSysFileId())) {
                    sysFileList.add(reportOpinion);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【评审意见】");
                }

                SysFile reportRoster = CreateTemplateUtils.createReportTemplateRoster(f, sign, expertSelectedList, workProgram, generalCounsel, counselor);
                if (reportRoster != null && Validate.isString(reportRoster.getSysFileId())) {
                    sysFileList.add(reportRoster);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【评审组名单】");
                }
                break;
            //项目建议书以及其他评审阶段
            default:
                SysFile sugEstimate = CreateTemplateUtils.createSugTemplateEstime(f, sign);
                if (sugEstimate != null && Validate.isString(sugEstimate.getSysFileId())) {
                    sysFileList.add(sugEstimate);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【投资估算表】");
                }

                SysFile sugOpinion = CreateTemplateUtils.createSugTemplateOpinion(f, sign);
                if (sugOpinion != null && Validate.isString(sugOpinion.getSysFileId())) {
                    sysFileList.add(sugOpinion);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【评审意见】");
                }

                SysFile sugRoster = CreateTemplateUtils.createSugTemplateRoster(f, sign, expertSelectedList, workProgram, generalCounsel, counselor);
                if (sugRoster != null && Validate.isString(sugRoster.getSysFileId())) {
                    sysFileList.add(sugRoster);
                }else{
                    if(errorCount > 0){
                        result.append(",");
                    }
                    result.append("【评审组名单】");
                }
        }

        //3、保存文件信息
        if (Validate.isList(sysFileList)) {
            Date now = new Date();
            sysFileList.forEach(sf -> {
                sf.setCreatedDate(now);
                sf.setModifiedDate(now);
                sf.setModifiedBy(SessionUtil.getLoginName());
                sf.setCreatedBy(SessionUtil.getLoginName());
                //先删除旧数据
                sysFileRepo.delete(sf.getMainId(), sf.getBusinessId(), sf.getSysBusiType(), sf.getShowName());
            });
            sysFileRepo.bathUpdate(sysFileList);
            signService.updateSignTemplate(signId);//修改是否生成发文模板状态
        }
        if (result.toString().length() > 0) {
            return new ResultMsg(true, MsgCode.OK.getValue(), result.toString() + "文件生成失败！", null);
        } else {
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "评审报告文件生成成功！", null);
        }
    }

}
