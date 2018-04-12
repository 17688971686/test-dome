package cs.service.project;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.domain.project.*;
import cs.domain.sys.Ftp;
import cs.domain.sys.Ftp_;
import cs.domain.sys.SysFile;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.service.sys.SysConfigService;
import cs.service.sys.SysFileService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static cs.common.Constant.RevireStageKey.KEY_CHECKFILE;

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
    private ExpertReviewRepo expertReviewRepo;

    /**
     * 生成发文编号，生成发文编号之前，要先上传项目评审意见
     * @param signId
     * @param dispatchId
     * @return
     */
    @Override
    @Transactional
    public ResultMsg fileNum(String signId,String dispatchId) {
        DispatchDoc dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(),dispatchId);
        if(dispatchDoc == null || !Validate.isString(dispatchDoc.getId())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取收文信息");
        }
        if(Constant.MergeType.DIS_MERGE.getValue().equals(dispatchDoc.getDispatchWay())&&
                !EnumState.YES.getValue().equals(dispatchDoc.getIsMainProject()) ){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前项目为合并发文次项目，由主项目生成发文编号！");
        }

        if(Validate.isString(dispatchDoc.getFileNum())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该发文已经生成了发文编号！");
        }
        /*//1、获取附件列表
        boolean isUploadMainFile = false;
        List<SysFile> fileList = sysFileRepo.findByMainId(signId);
        //默认每个类型都没检测
        String checFileName = "";
        Map<String,Boolean> checkTypeMap = new HashMap<>();
        SysConfigDto sysConfigDto = sysConfigService.findByKey(KEY_CHECKFILE.getValue());
        if(sysConfigDto == null || !Validate.isString(sysConfigDto.getConfigValue())){
            checFileName = Constant.DEFAULT_CHECK_FILE;
        }else{
            checFileName = sysConfigDto.getConfigValue();
            if(checFileName.indexOf("；") > -1){
                checFileName = checFileName.replace("；",";");
            }
            if(checFileName.indexOf("，") > -1){
                checFileName = checFileName.replace("，",",");
            }
        }
        //需要检测的文件类型
        List<String> typeFileList = StringUtil.getSplit(checFileName,";");
        for(String fileType : typeFileList){
            checkTypeMap.put(fileType,false);
        }

        if(Validate.isList(fileList)){
            int checkCount = typeFileList.size(),successCount = 0;
            for(int i=0,l=fileList.size();i<l;i++){
                SysFile sysFile = fileList.get(i);
                String showName = sysFile.getShowName().substring(0,sysFile.getShowName().lastIndexOf("."));
                for (Map.Entry<String,Boolean> entry : checkTypeMap.entrySet()) {
                    if(entry.getValue() == false){
                        //项目概算不用上传投资匡算表或投资估算表
                        if(Constant.STAGE_BUDGET.equals(dispatchDoc.getDispatchStage()) ){
                            if("评审意见,审核意见".equals(entry.getKey())){
                                entry.setValue(true);
                                successCount++;
                                break;
                            }
                        }else if(entry.getKey().contains(showName)){
                            entry.setValue(true);
                            successCount++;
                        }
                    }
                }
                if(Constant.STAGE_BUDGET.equals(dispatchDoc.getDispatchStage()) && successCount == 1) {
                    isUploadMainFile = true;
                    break;
                }else if(successCount == checkCount){
                    isUploadMainFile = true;
                    break;
                }
            }
        }
        if(!isUploadMainFile){
            StringBuffer errorBuffer = new StringBuffer();
            for (Map.Entry<String,Boolean> entry : checkTypeMap.entrySet()) {
                //项目概算不用上传投资匡算表或投资估算表
                if(Constant.STAGE_BUDGET.equals(dispatchDoc.getDispatchStage()) ){
                    if("评审意见,审核意见".equals(entry.getKey()) && entry.getValue() == false){

                        errorBuffer.append(entry.getKey().replaceAll(",","或者")+";");
                    }
                }else{
                    if(entry.getValue() == false){
                        errorBuffer.append(entry.getKey().replaceAll(",","或者")+";");
                    }
                }

            }
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，您还没上传"+errorBuffer.toString()+"附件信息！");
        }*/
        //获取发文最大编号
        int curYearMaxSeq = findCurMaxSeq(dispatchDoc.getDispatchDate());
        curYearMaxSeq = curYearMaxSeq + 1;
        String fileNumValue = "";
        if(curYearMaxSeq < 1000){
            fileNumValue = String.format("%03d", Integer.valueOf(curYearMaxSeq));
        }else{
            fileNumValue = curYearMaxSeq+"";
        }
        fileNumValue = Constant.DISPATCH_PREFIX+"["+ DateUtils.converToString(dispatchDoc.getDispatchDate(),"yyyy")+"]"+fileNumValue;
        dispatchDoc.setFileNum(fileNumValue);
        dispatchDoc.setFileSeq(curYearMaxSeq);
        dispatchDocRepo.save(dispatchDoc);
        //如果是合并发文，则更新所有关联的发文编号
        if(Constant.MergeType.DIS_MERGE.getValue().equals(dispatchDoc.getDispatchWay())){
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" update cs_dispatch_doc set "+DispatchDoc_.fileNum.getName()+" =:fileNum ").setParam("fileNum",dispatchDoc.getFileNum());
            sqlBuilder.append(" ,"+DispatchDoc_.fileSeq.getName()+" =:fileSeq").setParam("fileSeq",dispatchDoc.getFileSeq());
            sqlBuilder.append(" where signId in (select mergeId from cs_sign_merge where signId = :signId ");
            sqlBuilder.setParam("signId",signId);
            sqlBuilder.append(" and mergeType =:mergeType )").setParam("mergeType",Constant.MergeType.DISPATCH.getValue());

            dispatchDocRepo.executeSql(sqlBuilder);
        }
        //更改项目信息
        Sign sign = signRepo.findById(Sign_.signid.getName(),signId);
        //1、流程状态修改
        sign.setProcessState( Constant.SignProcessState.END_DIS_NUM.getValue());
        //2、发文日期等修改
        sign.setExpectdispatchdate(new Date());
        sign.setDocnum(fileNumValue);
        //3、发文后剩余工作日
        sign.setDaysafterdispatch(sign.getSurplusdays());
        signRepo.save(sign);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！",fileNumValue);

    }

    /**
     * 获取最大发文编号
     * @param dispatchDate
     * @return
     */
    private int findCurMaxSeq(Date dispatchDate) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+DispatchDoc_.fileSeq.getName()+") from cs_dispatch_doc where "+DispatchDoc_.dispatchDate.getName()+" between ");
        sqlBuilder.append(" to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss' )");
        sqlBuilder.setParam("beginTime", DateUtils.converToString(dispatchDate,"yyyy")+"-01-01 00:00:00");
        sqlBuilder.setParam("endTime", DateUtils.converToString(dispatchDate,"yyyy")+"-12-31 23:59:59");
        return dispatchDocRepo.returnIntBySql(sqlBuilder);
    }

    // 保存发文拟稿
    @Override
    @Transactional
    public ResultMsg save(DispatchDocDto dispatchDocDto) {
        if (Validate.isString(dispatchDocDto.getSignId())) {
            //1、先进行业务判断
            // 单个发文
            if (Constant.MergeType.DIS_SINGLE.getValue().equals(dispatchDocDto.getDispatchWay())) {
                if(signMergeRepo.isHaveMerge(dispatchDocDto.getSignId(),Constant.MergeType.DISPATCH.getValue())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，单个发文不能关联其他项目，请先删除关联项目再操作！");
                }
                // 合并发文次项目一定要关联
            }else if(Constant.MergeType.DIS_MERGE.getValue().equals(dispatchDocDto.getDispatchWay())&& EnumState.NO.getValue().equals(dispatchDocDto.getIsMainProject())) {
                if(!signMergeRepo.checkIsMerege(dispatchDocDto.getSignId(),Constant.MergeType.DISPATCH.getValue())){
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
                dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(),dispatchDocDto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(dispatchDocDto, dispatchDoc);
            }
            dispatchDoc.setModifiedBy(SessionUtil.getLoginName());
            dispatchDoc.setModifiedDate(now);

            //完成发文
            Sign sign = signRepo.findById(Sign_.signid.getName(),dispatchDocDto.getSignId());
            sign.setProcessState(Constant.SignProcessState.DO_DIS.getValue());
            sign.setDispatchDoc(dispatchDoc);
            // 收文、工作方案(主项目)、发文的报审金额一致
            List<WorkProgram> workProgrmList = sign.getWorkProgramList();
            if (Validate.isList(workProgrmList)) {
                for (WorkProgram workProgram : workProgrmList) {
                    if( workProgram.getBranchId() == FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue()){
                        workProgram.setAppalyInvestment(dispatchDocDto.getDeclareValue());
                    }
                }
            }
            sign.setAppalyInvestment(dispatchDocDto.getDeclareValue());
            sign.setAuthorizeValue(dispatchDocDto.getAuthorizeValue());
            sign.setWorkProgramList(workProgrmList);
            dispatchDoc.setSign(sign);
            dispatchDocRepo.save(dispatchDoc);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！",dispatchDocDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取收文信息，请联系管理员处理！");
        }

    }

    /**
     * 初始化发文编辑页面
     *
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
            boolean isMerge =signMergeRepo.checkIsMerege(signId, Constant.MergeType.DISPATCH.getValue());
            if (isMerge) {
                dispatch.setDispatchWay(Constant.MergeType.DIS_MERGE.getValue());   //合并发文
            } else {
                dispatch.setDispatchWay(Constant.MergeType.DIS_SINGLE.getValue());    //单个发文
            }
            dispatch.setIsMainProject(EnumState.NO.getValue());
            //是否已经有阶段关联
            dispatch.setIsRelated(signRepo.checkIsLink(signId)?EnumState.YES.getValue():EnumState.NO.getValue());
            dispatch.setDraftDate(now);
            dispatch.setDispatchDate(now);
            dispatch.setDispatchType("项目发文");
            //年度计划、紧急程度
            dispatch.setYearPlan(sign.getYearplantype());
            dispatch.setSecretLevel(sign.getSecrectlevel());
            dispatch.setUrgentLevel(sign.getUrgencydegree());
            //申报金额，与工作方案的、收文的一致，任何一个地方改了，都要同步更新
            dispatch.setDeclareValue(sign.getAppalyInvestment());
            //发文范围(艾传荣副巡视员（默认） + 投资处（获取收文主办室处） + 中心领导（默认）)
            dispatch.setDispatchScope(Constant.DIS_SCOPE_XSY+" "+sign.getMaindeptName()==null?"":sign.getMaindeptName()+" "+Constant.DIS_SCOPE_ZXLD);
            dispatch.setPrintCount(5);
            //发文标题
            String fileTitle = "深圳市投资项目评审中心关于";
            fileTitle += sign.getProjectname() == null ? "" : sign.getProjectname();
            if(Constant.STAGE_BUDGET.equals(sign.getReviewstage())){
                fileTitle += "项目的总概算的审核意见";
            }else{
                fileTitle += "项目的评审意见";
            }
//            fileTitle += sign.getProjectname() == null ? "" : sign.getProjectname();
//            fileTitle += (sign.getReviewstage() == null ? "" : sign.getReviewstage());
//            fileTitle += "》";
//            fileTitle += (sign.getIsAdvanced() == null ? "" : sign.getIsAdvanced());
            dispatch.setFileTitle(fileTitle);

            // 获取当前用户信息
            dispatch.setUserName(SessionUtil.getDisplayName());
            dispatch.setUserId(SessionUtil.getUserId());
            dispatch.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
            dispatch.setOrgId(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getId());

        }else{
            String related = signRepo.checkIsLink(signId)?EnumState.YES.getValue():EnumState.NO.getValue();
            if(!related.equals(dispatch.getIsRelated())){
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
                        || Constant.STAGE_SUG.equals(reviewStage) || Constant.APPLY_REPORT.equals(reviewStage)) ) {
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

        DispatchDoc dispatchDoc = dispatchDocRepo.findById("signid",signId);
        if(dispatchDoc != null){
            BeanCopierUtils.copyProperties(dispatchDoc, dispatchDocDto);
        }

        return dispatchDocDto;
    }

    /**
     * 生成发文模板
     * @param signId
     * @return
     */
    @Override
    public ResultMsg createDisPatchTemplate(String signId) {
//        SignDispaWork signDispaWork = signDispaWorkRepo.findById(SignDispaWork_.signid.getName() , signId);
        sysFileService.deleteByBusinessIdAndBusinessType(signId , Constant.SysFileType.TEMOLATE.getValue());
        WorkProgram workProgram = workProgramRepo.findByPrincipalUser(signId);

        Sign sign = signRepo.findById(Sign_.signid.getName() , signId);

        //获取专家评审方案
        ExpertReview expertReview = expertReviewRepo.findByBusinessId(signId);

        List<SysFile> sysFileList = new ArrayList<>();

        if(expertReview != null){
            //获得拟聘专家信息
//            List<Expert> expertList=expertRepo.findByBusinessId(workProgram.getId());

            List<ExpertSelected> expertSelectedList  = expertReview.getExpertSelectedList();

            Ftp f = ftpRepo.findById(Ftp_.ipAddr.getName(),sysFileService.findFtpId());
            //可行性研究报告
            if(Constant.STAGE_STUDY.equals(sign.getReviewstage())){
                SysFile studyOpinion = CreateTemplateUtils.createStudyTemplateOpinion(f,sign );
                if(studyOpinion != null && Validate.isString(studyOpinion.getSysFileId())){
                    sysFileList.add(studyOpinion);
                }

                SysFile studyEstimate = CreateTemplateUtils.createStudyTemplateEstimate(f,sign );
                if(studyEstimate != null  && Validate.isString(studyEstimate.getSysFileId())){
                    sysFileList.add(studyEstimate);
                }

                SysFile studyRoster = CreateTemplateUtils.createStudyTemplateRoster(f,sign , expertSelectedList);
                if(studyRoster != null   && Validate.isString(studyRoster.getSysFileId())){
                    sysFileList.add(studyRoster);
                }
            }else if(Constant.STAGE_BUDGET.equals(sign.getReviewstage())){//项目概算

                SysFile budgetEstimate = CreateTemplateUtils.createBudgetTemplateEstimate(f,sign );
                if(budgetEstimate != null && Validate.isString(budgetEstimate.getSysFileId())){
                    sysFileList.add(budgetEstimate);
                }

                SysFile budgetOpinion = CreateTemplateUtils.createBudgetTemplateOpinion(f,sign );
                if(budgetOpinion != null && Validate.isString(budgetOpinion.getSysFileId())){
                    sysFileList.add(budgetOpinion);
                }

                SysFile budgetProjectCost = CreateTemplateUtils.createBudgetTemplateProjectCost(f,sign );
                if(budgetProjectCost != null && Validate.isString(budgetProjectCost.getSysFileId())){
                    sysFileList.add(budgetProjectCost);
                }

                SysFile budgetRoster = CreateTemplateUtils.createBudgetTemplateRoster(f,sign  ,expertSelectedList);
                if(budgetRoster != null && Validate.isString(budgetRoster.getSysFileId())){
                    sysFileList.add(budgetRoster);
                }
            }else if(Constant.APPLY_REPORT.equals(sign.getReviewstage())){//资金申请报告
                SysFile reportEstimate = CreateTemplateUtils.createReportTemplateEstimate(f,sign );
                if(reportEstimate != null && Validate.isString(reportEstimate.getSysFileId())){
                    sysFileList.add(reportEstimate);
                }

                SysFile reportOpinion = CreateTemplateUtils.createReportTemplateOpinion(f,sign );
                if(reportOpinion != null  && Validate.isString(reportOpinion.getSysFileId())){
                    sysFileList.add(reportOpinion);
                }

                SysFile reportRoster = CreateTemplateUtils.createReportTemplateRoster(f,sign , expertSelectedList);
                if(reportRoster != null  && Validate.isString(reportRoster.getSysFileId())){
                    sysFileList.add(reportRoster);
                }
            }else{//项目建议书以及其他评审阶段
                SysFile sugEstimate = CreateTemplateUtils.createSugTemplateEstime(f,sign );
                if(sugEstimate != null  && Validate.isString(sugEstimate.getSysFileId())){
                    sysFileList.add(sugEstimate);
                }

                SysFile sugOpinion = CreateTemplateUtils.createSugTemplateOpinion(f,sign );
                if(sugOpinion != null && Validate.isString(sugOpinion.getSysFileId())){
                    sysFileList.add(sugOpinion);
                }

                SysFile sugRoster = CreateTemplateUtils.createSugTemplateRoster(f, sign , expertSelectedList);
                if(sugRoster != null && Validate.isString(sugRoster.getSysFileId())){
                    sysFileList.add(sugRoster);
                }
            }
        }

        //3、保存文件信息
        if(sysFileList!=null && sysFileList.size() > 0){
            Date now = new Date();
            sysFileList.forEach(sf->{
                sf.setCreatedDate(now);
                sf.setModifiedDate(now);
                sf.setModifiedBy(SessionUtil.getLoginName());
                sf.setCreatedBy(SessionUtil.getLoginName());
                //先删除旧数据
                sysFileRepo.delete(sf.getMainId(),sf.getBusinessId(),sf.getSysBusiType(),sf.getShowName());
            });
            sysFileRepo.bathUpdate(sysFileList);
            signService.updateSignTemplate(signId);//修改是否生成发文模板状态
            return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "评审报告文件生成成功！" , null);
        }else{
            return  new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "文件服务器无法连接，评审报告文件无法自动生成，请联系管理员处理" , null);
        }


    }

}
