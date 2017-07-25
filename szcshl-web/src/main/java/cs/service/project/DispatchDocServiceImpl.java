package cs.service.project;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.*;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.MergeOptionRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.service.sys.UserService;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
    private ICurrentUser currentUser;
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

    /**
     * 查询已经选择的合并发文项目
     *
     * @param mainBussnessId
     * @return
     */
    @Override
    public List<SignDto> getSeleSignByMainBusiId(String mainBussnessId) {
        List<SignDto> resultList = new ArrayList<>();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + Sign.class.getSimpleName() + " where " + Sign_.signid.getName() + " in ( ");
        hqlBuilder.append(" select " + MergeOption_.signId.getName() + " from " + MergeOption.class.getSimpleName() );
        hqlBuilder.append(" where " + MergeOption_.mainBusinessId.getName() + " = :dispatchId ").setParam("dispatchId", mainBussnessId);
        hqlBuilder.append(" and "+ MergeOption_.businessType.getName() + "=:businessType )").setParam("businessType",  Constant.MergeType.DISPATCH.getValue());
        List<Sign> list = signRepo.findByHql(hqlBuilder);
        if (list != null) {
            list.forEach(sl -> {
                SignDto slDto = new SignDto();
                BeanCopierUtils.copyProperties(sl, slDto);
                resultList.add(slDto);
            });
        }
        return resultList;
    }

    /**
     * 获取待合并发文项目
     *
     * @param signDto
     * @return
     */
    @Override
    public List<SignDto> getSignForMerge(SignDto signDto, String dispatchId) {
        List<SignDto> resultList = new ArrayList<>();
        if (Validate.isString(signDto.getSignid())) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" from " + Sign.class.getSimpleName() + " s  ");
            //1、查找还没发文的项目()
            hqlBuilder.append("where (s." + Sign_.isDispatchCompleted.getName() + " is null or s." + Sign_.isDispatchCompleted.getName() + " =:isDispatchCompleted )").setParam("isDispatchCompleted", EnumState.NO.getValue());

            if (Validate.isString(signDto.getReviewstage())) {
                hqlBuilder.append(" and s." + Sign_.reviewstage.getName() + " =:reviewstage ").setParam("reviewstage", signDto.getReviewstage());
            }
            if (Validate.isString(signDto.getProjectname())) {
                hqlBuilder.append(" and s." + Sign_.projectname.getName() + " like :projectname").setParam("projectname", signDto.getProjectname());
            }
            if (Validate.isString(signDto.getBuiltcompanyName())) {
                hqlBuilder.append(" and s." + Sign_.builtcompanyName.getName() + " like :builtcompanyName").setParam("builtcompanyName", signDto.getBuiltcompanyName());
            }
            if (signDto.getStartTime() != null && signDto.getEndTime() != null) {
                hqlBuilder.append(" and s." + Sign_.signdate.getName()).append(" between ").append(":startTime").setParam("startTime", signDto.getStartTime());
                hqlBuilder.append(" and ").append(":endTime").setParam("endTime", signDto.getEndTime());
            }

            //2、排除本次已选的项目
            hqlBuilder.append(" and s." + Sign_.signid.getName() + " not in ( select " + MergeOption_.signId.getName() + " from " + MergeOption.class.getSimpleName() + " ms where ms." + MergeOption_.mainBusinessId.getName() + " = :dispatchId )").setParam("dispatchId", dispatchId);

            List<Sign> list = signRepo.findByHql(hqlBuilder);
            if (list != null) {
                list.forEach(sl -> {
                    SignDto slDto = new SignDto();
                    BeanCopierUtils.copyProperties(sl, slDto);
                    resultList.add(slDto);
                });
            }
        }
        return resultList;
    }

    /**
     * 合并发文保存
     *
     * @param signId         //主项目ID
     * @param mainBusinessId //主业务ID
     * @param linkSignId     //关联项目ID (用“,”相隔)
     * @throws Exception
     */
    @Override
    @Transactional
    public void mergeDispa(String signId, String mainBusinessId, String linkSignId){
        List<String> linkSignIdList = StringUtil.getSplit(linkSignId, ",");
        List<MergeOption> saveList = new ArrayList<>(linkSignIdList == null ? 0 : linkSignIdList.size());

        if (linkSignIdList != null) {
            Date now = new Date();
            String createUserId = currentUser.getLoginUser().getId();
            //判断是否已经添加了主项目
            Criteria criteria = mergeOptionRepo.getExecutableCriteria();
            criteria.add(Restrictions.eq(MergeOption_.mainBusinessId.getName(), mainBusinessId));
            criteria.add(Restrictions.eq(MergeOption_.businessId.getName(), mainBusinessId));
            criteria.add(Restrictions.eq(MergeOption_.businessType.getName(), Constant.MergeType.DISPATCH.getValue()));

            MergeOption mergeOption = (MergeOption) criteria.uniqueResult();
            if (mergeOption == null || !Validate.isString(mergeOption.getId())) {
                mergeOption = new MergeOption();
                mergeOption.setBusinessId(mainBusinessId);
                mergeOption.setMainBusinessId(mainBusinessId);
                mergeOption.setSignId(signId);
                mergeOption.setBusinessType(Constant.MergeType.DISPATCH.getValue());
                mergeOption.setCreatedBy(createUserId);
                mergeOption.setModifiedBy(createUserId);
                mergeOption.setCreatedDate(now);
                mergeOption.setModifiedDate(now);
                saveList.add(mergeOption);
            }
            for (String linkId : linkSignIdList) {
                MergeOption mergeOptionLk = new MergeOption();
                mergeOptionLk.setMainBusinessId(mainBusinessId);
                mergeOptionLk.setSignId(linkId);
                mergeOptionLk.setBusinessType(Constant.MergeType.DISPATCH.getValue());
                mergeOptionLk.setCreatedBy(createUserId);
                mergeOptionLk.setModifiedBy(createUserId);
                mergeOptionLk.setCreatedDate(now);
                mergeOptionLk.setModifiedDate(now);
                saveList.add(mergeOptionLk);
            }
        }
        if (Validate.isList(saveList)) {
            mergeOptionRepo.bathUpdate(saveList);
        }
    }

    /**
     * 删除关联项目信息
     *
     * @param mainBusinessId
     * @param removeSignIds
     */
    @Override
    @Transactional
    public void deleteMergeDispa(String mainBusinessId, String removeSignIds) {
        if (Validate.isString(mainBusinessId)) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" delete from " + MergeOption.class.getSimpleName());
            hqlBuilder.append(" where " + MergeOption_.mainBusinessId.getName() + " =:mainBusinessId").setParam("mainBusinessId", mainBusinessId);
            hqlBuilder.append(" and "+MergeOption_.businessType.getName()+" =:businessType ").setParam("businessType",Constant.MergeType.DISPATCH.getValue());
            //删除指定的收文ID
            if (Validate.isString(removeSignIds)) {
                List<String> delLinkIds = StringUtil.getSplit(removeSignIds, ",");
                if (delLinkIds.size() == 1) {
                    hqlBuilder.append(" and " + MergeOption_.signId.getName() + " =:signId ").setParam("signId", delLinkIds.get(0));
                } else {
                    hqlBuilder.append(" and " + MergeOption_.signId.getName() + " in ( ");
                    for (int i = 0, l = delLinkIds.size(); i < l; i++) {
                        if (i > 0) {
                            hqlBuilder.append(",");
                        }
                        hqlBuilder.append(" :delId" + i).setParam("delId" + i, delLinkIds.get(i));
                    }
                    hqlBuilder.append(" ) ");
                }

                //删除所有，则要修改发文的发文方式(单个发文)
            } else {
                HqlBuilder updateBuilder = HqlBuilder.create();
                updateBuilder.append("update " + DispatchDoc.class.getSimpleName() + " set " + DispatchDoc_.dispatchWay.getName() + " =:dispatchWay ");
                updateBuilder.setParam("dispatchWay", Constant.MergeWay.SINGLE.getValue());
                updateBuilder.append(" ," + DispatchDoc_.isMainProject.getName() + " =:isMainProject").setParam("isMainProject", EnumState.NO.getValue());
                updateBuilder.append(" where " + DispatchDoc_.id.getName() + " =:id").setParam("id", mainBusinessId);
                mergeOptionRepo.executeHql(updateBuilder);
            }
            mergeOptionRepo.executeHql(hqlBuilder);
        }
    }

    // 生成文件字号
    @Override
    @Transactional
    public ResultMsg fileNum(String dispatchId) {
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
            sqlBuilder.append(" where signId in (select signId from cs_merge_option where mainBusinessId = :mainBusinessId ");
            sqlBuilder.setParam("mainBusinessId",dispatchDoc.getId());
            sqlBuilder.append(" and businessType =:businessType )").setParam("businessType",Constant.MergeType.DISPATCH.getValue());

            dispatchDocRepo.executeSql(sqlBuilder);
        }else{
            dispatchDoc.setFileNum(fileNum);
            dispatchDoc.setFileSeq((curYearMaxSeq + 1));
            dispatchDocRepo.save(dispatchDoc);
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), fileNum);

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
                if(mergeOptionService.isMerge(null,dispatchDocDto.getSignId(),Constant.MergeType.DISPATCH.getValue())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，单个发文不能关联其他项目，请先删除关联项目再操作！");
                }
            // 合并发文次项目一定要关联
            }else if(Constant.MergeWay.MERGE.getValue().equals(dispatchDocDto.getDispatchWay())) {
                //主项目(有ID才判断)
                if(EnumState.YES.getValue().equals(dispatchDocDto.getIsMainProject()) ){
                    if(Validate.isString(dispatchDocDto.getId()) && !mergeOptionService.isHaveLink(dispatchDocDto.getId(),Constant.MergeType.DISPATCH.getValue())){
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前出文方式为合并发文主项目，请先进行项目关联！");
                    }
                //次项目
                }else{
                    if(!mergeOptionService.isMerge(null,dispatchDocDto.getSignId(),Constant.MergeType.DISPATCH.getValue())){
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，当前出文方式为合并发文次项目，请在主项目挑选此项目为次项目再发文！");
                    }
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
                dispatchDoc.setCreatedBy(currentUser.getLoginName());
                dispatchDoc.setCreatedDate(now);

                dispatchDocDto.setId(dispatchDoc.getId());
            } else {
                dispatchDoc = dispatchDocRepo.findById(DispatchDoc_.id.getName(),dispatchDocDto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(dispatchDocDto, dispatchDoc);
            }
            dispatchDoc.setModifiedBy(currentUser.getLoginName());
            dispatchDoc.setModifiedDate(now);

            Sign sign = signRepo.findById(Sign_.signid.getName(),dispatchDocDto.getSignId());
            sign.setIsDispatchCompleted(EnumState.YES.getValue());
            sign.setDispatchDoc(dispatchDoc);
            // 收文、工作方案、发文的报审金额一致
            List<WorkProgram> workProgrmList = sign.getWorkProgramList();
            if (Validate.isList(workProgrmList)) {
                for (WorkProgram workProgram : workProgrmList) {
                    workProgram.setAppalyInvestment(dispatchDocDto.getDeclareValue());
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
            //(1)、判断项目是否为关联项目
            Criteria mergeCriteria = mergeOptionRepo.getExecutableCriteria();
            mergeCriteria.add(Restrictions.eq(MergeOption_.signId.getName(), signId));
            mergeCriteria.add(Restrictions.eq(MergeOption_.businessType.getName(), Constant.MergeType.DISPATCH.getValue()));
            //MergeOption mergeOption = (MergeOption) mergeCriteria.uniqueResult();
            Integer totalResult = ((Number) mergeCriteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

            dispatch = new DispatchDoc();
            dispatch.setDraftDate(now);
            dispatch.setDispatchDate(now);
            if (totalResult == null || totalResult == 0) {
                dispatch.setDispatchWay(Constant.MergeWay.SINGLE.getValue());    //单个发文
                dispatch.setIsRelated("否");     //是否有关联
            } else {
                dispatch.setDispatchWay(Constant.MergeWay.MERGE.getValue());   //合并发文
                dispatch.setIsRelated("是");
            }
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
            dispatch.setUserName(currentUser.getLoginUser().getLoginName());
            dispatch.setUserId(currentUser.getLoginUser().getId());
            dispatch.setOrgName(currentUser.getLoginUser().getOrg() == null ? "" : currentUser.getLoginUser().getOrg().getName());
            dispatch.setOrgId(currentUser.getLoginUser().getOrg() == null ? "" : currentUser.getLoginUser().getOrg().getId());
            dispatch.setYearPlan(sign.getYearplantype());
            dispatch.setSecretLevel(sign.getSecrectlevel());
            dispatch.setUrgentLevel(sign.getUrgencydegree());
        }

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

        // 获取主办处联系人
        List<UserDto> userList = userService.findUserByOrgId(sign.getmOrgId());
        map.put("mainUserList", userList);

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
