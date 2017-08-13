package cs.service.project;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.*;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.MergeOptionRepo;
import cs.repository.repositoryImpl.project.SignMergeRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.sys.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DispatchDocServiceImpl implements DispatchDocService {
    private static Logger log = Logger.getLogger(DispatchDocServiceImpl.class);
    @Autowired
    private DispatchDocRepo dispatchDocRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private SignService signService;
    @Autowired
    private MergeOptionRepo mergeOptionRepo;
    @Autowired
    private MergeOptionService mergeOptionService;
    @Autowired
    private SignMergeRepo signMergeRepo;

    // 生成文件字号
    @Override
    @Transactional
    public ResultMsg fileNum(String signId,String dispatchId) {
        DispatchDoc dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(),dispatchId);
        if(dispatchDoc == null || !Validate.isString(dispatchDoc.getId())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取收文信息");
        }
        if(Constant.MergeWay.MERGE.getValue().equals(dispatchDoc.getDispatchWay())&&
                !EnumState.YES.getValue().equals(dispatchDoc.getIsMainProject()) ){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前项目为合并发文次项目，由主项目生成发文编号！");
        }

        if(Validate.isString(dispatchDoc.getFileNum())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该发文已经生成了发文编号！");
        }
        //获取发文最大编号
        int curYearMaxSeq = findCurMaxSeq(dispatchDoc.getDispatchDate());
        String fileNum = Constant.DISPATCH_PREFIX+"["+ DateUtils.converToString(dispatchDoc.getDispatchDate(),"yyyy")+"]"+(curYearMaxSeq + 1);
        //如果是合并发文，则更新所有关联的发文编号
        if(Constant.MergeWay.MERGE.getValue().equals(dispatchDoc.getDispatchWay())){
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" update cs_dispatch_doc set "+DispatchDoc_.fileNum.getName()+" =:fileNum ").setParam("fileNum",fileNum);
            sqlBuilder.append(" ,"+DispatchDoc_.fileSeq.getName()+" =:fileSeq").setParam("fileSeq",(curYearMaxSeq + 1));
            sqlBuilder.append(" where signId in (select mergeId from cs_sign_merge where signId = :signId ");
            sqlBuilder.setParam("signId",signId);
            sqlBuilder.append(" and mergeType =:mergeType )").setParam("mergeType",Constant.MergeType.DISPATCH.getValue());

            dispatchDocRepo.executeSql(sqlBuilder);
        }else{
            dispatchDoc.setFileNum(fileNum);
            dispatchDoc.setFileSeq((curYearMaxSeq + 1));
            dispatchDocRepo.save(dispatchDoc);
        }
        signRepo.updateSignProcessState(signId, Constant.SignProcessState.END_DIS_NUM.getValue());
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！",fileNum);

    }

    /**
     * 获取最大发文编号
     * @param dispatchDate
     * @return
     */
    private int findCurMaxSeq(Date dispatchDate) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+DispatchDoc_.fileSeq.getName()+") from cs_dispatch_doc where TO_CHAR("+DispatchDoc_.dispatchDate.getName()+",'yyyy') = :dispatchDate ");
        sqlBuilder.setParam("dispatchDate",DateUtils.converToString(dispatchDate,"yyyy"));
        return dispatchDocRepo.returnIntBySql(sqlBuilder);
    }

    // 保存发文拟稿
    @Override
    @Transactional
    public ResultMsg save(DispatchDocDto dispatchDocDto) {
        if (Validate.isString(dispatchDocDto.getSignId())) {
            //1、先进行业务判断
            // 单个发文
            if (Constant.MergeWay.SINGLE.getValue().equals(dispatchDocDto.getDispatchWay())) {
                if(signMergeRepo.isHaveMerge(dispatchDocDto.getSignId(),Constant.MergeType.DISPATCH.getValue())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，单个发文不能关联其他项目，请先删除关联项目再操作！");
                }
            // 合并发文次项目一定要关联
            }else if(Constant.MergeWay.MERGE.getValue().equals(dispatchDocDto.getDispatchWay())&& EnumState.NO.getValue().equals(dispatchDocDto.getIsMainProject())) {
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
                    if( workProgram.getBranchId() == Constant.SignFlowParams.BRANCH_INDEX1.getValue()){
                       workProgram.setAppalyInvestment(dispatchDocDto.getDeclareValue());
                    }
                }
            }
            sign.setAppalyInvestment(dispatchDocDto.getDeclareValue());
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
                dispatch.setDispatchWay(Constant.MergeWay.MERGE.getValue());   //合并发文
                dispatch.setIsMainProject(EnumState.YES.getValue());
                dispatch.setIsRelated("是");
            } else {
                dispatch.setDispatchWay(Constant.MergeWay.SINGLE.getValue());    //单个发文
                dispatch.setIsMainProject(EnumState.NO.getValue());
                dispatch.setIsRelated("否");     //是否有关联
            }

            dispatch.setDraftDate(now);
            dispatch.setDispatchDate(now);
            dispatch.setDispatchType("项目发文");

            //申报金额，与工作方案的、收文的一致，任何一个地方改了，都要同步更新
            dispatch.setDeclareValue(sign.getAppalyInvestment());

            String fileTitle = "《";
            fileTitle += sign.getProjectname() == null ? "" : sign.getProjectname();
            fileTitle += (sign.getReviewstage() == null ? "" : sign.getReviewstage());
            fileTitle += "》";
            fileTitle += (sign.getIsAdvanced() == null ? "" : sign.getIsAdvanced());
            dispatch.setFileTitle(fileTitle);
           

            // 获取当前用户信息
            dispatch.setUserName(SessionUtil.getLoginName());
            dispatch.setUserId(SessionUtil.getUserInfo().getId());
            dispatch.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
            dispatch.setOrgId(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getId());
            dispatch.setYearPlan(sign.getYearplantype());
            dispatch.setSecretLevel(sign.getSecrectlevel());
            dispatch.setUrgentLevel(sign.getUrgencydegree());
        }
        dispatch.setDispatchStage(sign.getReviewstage());//评审阶段
        BeanCopierUtils.copyProperties(dispatch, dispatchDto);
        dispatchDto.setSignId(signId);
        map.put("dispatch", dispatchDto);

        //如果评审阶段是可研和概算的，才关联到前一阶段
        String reviewStage = sign.getReviewstage();
        if (reviewStage != null && (reviewStage.equals("可行性研究报告") || reviewStage.equals("项目概算")) && sign.getAssociateSign() != null) {
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

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + DispatchDoc.class.getSimpleName() + " where " + DispatchDoc_.sign.getName() + "."
                + Sign_.signid.getName() + " = :signId ");
        hqlBuilder.setParam("signId", signId);

        List<DispatchDoc> list = dispatchDocRepo.findByHql(hqlBuilder);
        if (list != null && list.size() > 0) {
            DispatchDoc dispatchDoc = list.get(0);
            BeanCopierUtils.copyProperties(dispatchDoc, dispatchDocDto);
        }
        return dispatchDocDto;
    }

}
