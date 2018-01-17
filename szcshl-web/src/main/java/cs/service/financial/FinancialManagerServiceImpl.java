package cs.service.financial;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.sys.User;
import cs.model.expert.ExpertReviewDto;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignAssistCostDto;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.financial.FinancialManagerRepo;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.project.SignPrincipalService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 财务管理 业务操作实现类
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
@Service
public class FinancialManagerServiceImpl implements FinancialManagerService {

    private static Logger log = Logger.getLogger(FinancialManagerServiceImpl.class);
    @Autowired
    private FinancialManagerRepo financialManagerRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private AssistPlanRepo assistPlanRepo;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;

    @Override
    @Transactional
    public void save(FinancialManagerDto record) {
        FinancialManager domain = new FinancialManager();
        BeanCopierUtils.copyProperties(record, domain);
        Date now = new Date();

        if (!Validate.isString(record.getId())) {
            domain.setId(UUID.randomUUID().toString());
            domain.setCreatedBy(SessionUtil.getDisplayName());
            domain.setCreatedDate(now);
        }

        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(now);
        financialManagerRepo.save(domain);
    }

    @Override
    @Transactional
    public void update(FinancialManagerDto record) {
        FinancialManager domain = financialManagerRepo.findById(FinancialManager_.id.getName(), record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(new Date());
        financialManagerRepo.save(domain);
    }

    @Override
    public FinancialManagerDto findById(String id) {
        FinancialManagerDto modelDto = new FinancialManagerDto();
        if (Validate.isString(id)) {
            FinancialManager domain = financialManagerRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
        }
        return modelDto;
    }

    @Override
    @Transactional
    public void delete(String id) {
        List<String> idFinancial = StringUtil.getSplit(id, ",");
        if (idFinancial != null && idFinancial.size() > 0) {
            for (int i = 0; i < idFinancial.size(); i++) {
                financialManagerRepo.deleteById(FinancialManager_.id.getName(), idFinancial.get(i));
            }
        }
    }

    @Override
    public BigDecimal sunCount(String businessId) {
        HqlBuilder hql = HqlBuilder.create();
        hql.append(" select sum(charge) from CS_FINANCIAL_MANAGER ");
        hql.append(" where " + FinancialManager_.businessId.getName() + " =:businessId");
        hql.setParam("businessId", businessId);
        return new BigDecimal(financialManagerRepo.returnIntBySql(hql));
    }

    /**
     * 初始化财务报销页面（是否协审通过业务数据判断）
     *
     * @param businessId   业务ID
     * @param businessType 类型
     * @return
     */
    @Override
    public Map<String, Object> initfinancialData(String businessId, String chargeType, String businessType) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<FinancialManagerDto> financialDtolist = new ArrayList<>();
        List<FinancialManager> financiallist = financialManagerRepo.findByIds(FinancialManager_.businessId.getName(), businessId, null);
        //如果还没评审费用，则初始化两条记录
        if (!Validate.isList(financiallist)) {
            //如果是评审费，则初始化两条记录，1是专家费，2是专家税费
            if (Constant.EnumState.PROCESS.getValue().equals(chargeType) && expertReviewRepo.isHaveEPReviewCost(businessId)) {
                ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);
                if (Validate.isObject(expertReview) && Validate.isObject(expertReview.getPayDate()) && Validate.isList(expertReview.getExpertSelectedList())) {
                    BigDecimal expertExpense = BigDecimal.ZERO;
                    BigDecimal expertTaxes = BigDecimal.ZERO;
                    for (int i = 0, l = expertReview.getExpertSelectedList().size(); i < l; i++) {
                        ExpertSelected expertSelected = expertReview.getExpertSelectedList().get(i);
                        if (Constant.EnumState.YES.getValue().equals(expertSelected.getIsConfrim()) && Constant.EnumState.YES.getValue().equals(expertSelected.getIsJoin())) {
                            expertExpense = Arith.safeAdd(expertExpense, expertSelected.getReviewCost());
                            expertTaxes = Arith.safeAdd(expertTaxes, expertSelected.getReviewTaxes());
                        }
                    }
                    Date now = new Date();
                    financiallist = new ArrayList<>();
                    //新增两条记录
                    FinancialManager financialManager = new FinancialManager();
                    financialManager.setId(UUID.randomUUID().toString());
                    financialManager.setBusinessId(businessId);
                    financialManager.setChargeName("专家评审费");
                    financialManager.setCharge(expertExpense);
                    financialManager.setCreatedBy(SessionUtil.getDisplayName());
                    financialManager.setCreatedDate(now);
                    financialManager.setModifiedBy(SessionUtil.getDisplayName());
                    financialManager.setModifiedDate(now);
                    financiallist.add(financialManager);

                    FinancialManager financialManager1 = new FinancialManager();
                    financialManager1.setId(UUID.randomUUID().toString());
                    financialManager1.setBusinessId(businessId);
                    financialManager1.setChargeName("应缴税");
                    financialManager1.setCharge(expertTaxes);
                    financialManager1.setCreatedBy(SessionUtil.getDisplayName());
                    financialManager1.setCreatedDate(now);
                    financialManager1.setModifiedBy(SessionUtil.getDisplayName());
                    financialManager1.setModifiedDate(now);
                    financiallist.add(financialManager1);

                    financialManagerRepo.bathUpdate(financiallist);
                }
            }
        }
        if (Validate.isList(financiallist)) {
            financiallist.forEach(fl -> {
                FinancialManagerDto financialManagerDto = new FinancialManagerDto();
                BeanCopierUtils.copyProperties(fl, financialManagerDto);
                financialDtolist.add(financialManagerDto);
            });
        }
        map.put("financiallist", financialDtolist);
        /*if (Validate.isString(businessType)) {
            FinancialManagerDto dto = new FinancialManagerDto();
            if(Constant.BusinessType.SIGN.getValue().equals(businessType)){
                Sign sign = signRepo.findById(Sign_.signid.getName(),businessId);
                dto.setProjectName(sign.getProjectname());
                dto.setBusinessId(businessId);
                if(Validate.isList(financiallist)){
                    dto.setPaymentData(financiallist.get(0).getPaymentData());
                }
               *//* //如果是协审项目，则查询协审费
                if(Constant.EnumState.YES.getValue().equals(sign.getIsassistflow())){
                    SignAssistCostDto signAssistCost = new SignAssistCostDto();
                    signAssistCost.setSignId(businessId);
                    List<SignAssistCostDto> signAssistCostList = signAssistCostList(signAssistCost,false);
                    if(Validate.isList(signAssistCostList)){
                        signAssistCost = signAssistCostList.get(0);
                        dto.setProjectName(signAssistCost.getProjectName());
                        dto.setBusinessId(businessId);
                        dto.setPaymentData(signAssistCost.getPayDate());
                        dto.setAssistUnit(signAssistCost.getAssistUnit());
                        dto.setAssissCost(signAssistCost.getPlanCost());
                    }
                }else{
                    dto.setProjectName(sign.getProjectname());
                    dto.setBusinessId(businessId);
                    if(Validate.isList(financiallist)){
                        dto.setPaymentData(financiallist.get(0).getPaymentData());
                    }
                }*//*
            }
            map.put("financialDto", dto);
        }*/
        return map;
    }

    /**
     * 协审费用统计
     *
     * @param signAssistCostDto
     * @param isShowDetail      是否显示费用详情
     * @return
     */
    @Override
    public List<SignAssistCostDto> signAssistCostList(SignAssistCostDto signAssistCostDto, boolean isShowDetail) {
        List<Object[]> signAssistList = assistPlanRepo.signAssistCostList(signAssistCostDto, isShowDetail);
        if (Validate.isList(signAssistList)) {
            if (isShowDetail) {
                return initSignAssistCostDetailList(signAssistList);
            } else {
                return initSignAssistCostList(signAssistList);
            }
        }
        return null;
    }

    private List<SignAssistCostDto> initSignAssistCostDetailList(List<Object[]> signAssistList) {
        List<SignAssistCostDto> resultList = new ArrayList<>(signAssistList.size());
        SignAssistCostDto saDto = null;
        List<FinancialManagerDto> financialManagerDtoList = null;
        String oldSignId = "";
        for (int i = 0, l = signAssistList.size(); i < l; i++) {
            boolean isSame = false;
            Object[] obj = signAssistList.get(i);
            //项目ID
            String signId = obj[0] == null ? "" : obj[0].toString();
            if(!Validate.isString(oldSignId) || !signId.equals(oldSignId)){
                if(Validate.isList(financialManagerDtoList)){
                    saDto.setCostList(financialManagerDtoList);
                    resultList.add(saDto);
                }
                oldSignId = signId;
                saDto = new SignAssistCostDto();
                financialManagerDtoList = new ArrayList<>();
            }else{
                isSame = true;
            }

            if(!isSame){
                //项目名称
                String projectName = obj[1] == null ? "" : obj[1].toString();
                //协审计划编号
                String assistPlanNo = obj[2] == null ? "" : obj[2].toString();
                //协审单位
                String assistUnit = obj[3] == null ? "" : obj[3].toString();
                //项目负责人
                String changeUserName = obj[4] == null ? "" : obj[4].toString();
                //协审计划费用
                BigDecimal planCost = obj[5] == null ? null : ObjectUtils.getBigDecimal(obj[5]);
                //协审实际费用
                BigDecimal factCost = obj[6] == null ? null : ObjectUtils.getBigDecimal(obj[6]);
                //申报金额
                BigDecimal declareValue = obj[7] == null ? null : ObjectUtils.getBigDecimal(obj[7]);
                //审定金额
                BigDecimal authorizeValue = obj[8] == null ? null : ObjectUtils.getBigDecimal(obj[8]);
                //警示灯状态
                String lightState = obj[9] == null ? "" : obj[9].toString();
                //项目流程状态
                Integer processState = obj[10] == null ? null : Integer.parseInt(obj[10].toString());
                //付款日期
                Date payDate = DateUtils.converToDate(obj[11] == null ? "" : obj[11].toString(), null);

                saDto.setSignId(signId);
                saDto.setProjectName(projectName);
                saDto.setAssistUnit(assistUnit);
                saDto.setAssistPlanNo(assistPlanNo);
                saDto.setChangeUserName(changeUserName);
                saDto.setPlanCost(planCost);
                saDto.setFactCost(factCost);
                saDto.setDeclareValue(declareValue);
                saDto.setAuthorizeValue(authorizeValue);
                saDto.setIsLightUp(lightState);
                saDto.setProcessState(processState);
                saDto.setPayDate(payDate);
            }

            //细项
            FinancialManagerDto financialManagerDto = new FinancialManagerDto();
            BigDecimal chargeCost = obj[12] == null ? null : ObjectUtils.getBigDecimal(obj[12]);
            financialManagerDto.setCharge(chargeCost);
            String chargeName = obj[13] == null ? "" : obj[13].toString();
            financialManagerDto.setChargeName(chargeName);
            //付款日期
            Date chargeDate = DateUtils.converToDate(obj[14] == null ? "" : obj[14].toString(), null);
            financialManagerDto.setPaymentData(chargeDate);
            financialManagerDtoList.add(financialManagerDto);
            //最后一个元素
            if( i == l-1){
                saDto.setCostList(financialManagerDtoList);
                resultList.add(saDto);
            }
        }
        return resultList;
    }

    private List<SignAssistCostDto> initSignAssistCostList(List<Object[]> signAssistList) {
        List<SignAssistCostDto> resultList = new ArrayList<>(signAssistList.size());
        for (int i = 0, l = signAssistList.size(); i < l; i++) {
            Object[] obj = signAssistList.get(i);
            SignAssistCostDto saDto = new SignAssistCostDto();
            //项目ID
            String signId = obj[0] == null ? "" : obj[0].toString();
            //项目名称
            String projectName = obj[1] == null ? "" : obj[1].toString();
            //协审计划编号
            String assistPlanNo = obj[2] == null ? "" : obj[2].toString();
            //协审单位
            String assistUnit = obj[3] == null ? "" : obj[3].toString();
            //项目负责人
            String changeUserName = obj[4] == null ? "" : obj[4].toString();
            //协审计划费用
            BigDecimal planCost = obj[5] == null ? null : ObjectUtils.getBigDecimal(obj[5]);
            //协审实际费用
            BigDecimal factCost = obj[6] == null ? null : ObjectUtils.getBigDecimal(obj[6]);
            //申报金额
            BigDecimal declareValue = obj[7] == null ? null : ObjectUtils.getBigDecimal(obj[7]);
            //审定金额
            BigDecimal authorizeValue = obj[8] == null ? null : ObjectUtils.getBigDecimal(obj[8]);
            //警示灯状态
            String lightState = obj[9] == null ? "" : obj[9].toString();
            //项目流程状态
            Integer processState = obj[10] == null ? null : Integer.parseInt(obj[10].toString());
            //付款日期
            Date payDate = DateUtils.converToDate(obj[11] == null ? "" : obj[11].toString(), null);


            saDto.setSignId(signId);
            saDto.setProjectName(projectName);
            saDto.setAssistUnit(assistUnit);
            saDto.setAssistPlanNo(assistPlanNo);
            saDto.setChangeUserName(changeUserName);
            saDto.setPlanCost(planCost);
            saDto.setFactCost(factCost);
            saDto.setDeclareValue(declareValue);
            saDto.setAuthorizeValue(authorizeValue);
            saDto.setIsLightUp(lightState);
            saDto.setProcessState(processState);
            saDto.setPayDate(payDate);
            resultList.add(saDto);
        }
        return resultList;
    }

    /**
     * 批量保存评审费
     *
     * @param record
     * @return
     */
    @Override
    public ResultMsg save(FinancialManagerDto[] record) {
        String businessId = "", chargeType = "";
        if (record == null || record.length == 0) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存失败，请先填写费用项信息！");
        }
        List<FinancialManager> saveList = new ArrayList<>(record.length);
        for (int i = 0, l = record.length; i < l; i++) {
            FinancialManagerDto dto = record[i];
            if (!Validate.isString(dto.getChargeName()) || !Validate.isObject(dto.getCharge())) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存失败，存在费用名称或者费用没有填写！");
            }
            if (!Validate.isString(businessId)) {
                businessId = dto.getBusinessId();
            }
            if (!Validate.isString(chargeType)) {
                chargeType = dto.getChargeType();
            }
            FinancialManager financialManager = new FinancialManager();
            Date now = new Date();
            if (Validate.isString(dto.getId())) {
                financialManager = financialManagerRepo.findById(dto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(dto, financialManager);
            } else {
                BeanCopierUtils.copyProperties(dto, financialManager);
                financialManager.setId(UUID.randomUUID().toString());
                financialManager.setCreatedDate(now);
                financialManager.setCreatedBy(SessionUtil.getDisplayName());
            }
            if (financialManager.getPaymentData() == null) {
                financialManager.setPaymentData(now);
            }
            financialManager.setModifiedBy(SessionUtil.getDisplayName());
            financialManager.setModifiedDate(now);
            //默认是评审费
            if (!Validate.isString(financialManager.getChargeType())) {
                financialManager.setChargeType(Constant.EnumState.PROCESS.getValue());
            }
            saveList.add(financialManager);
        }
        financialManagerRepo.bathUpdate(saveList);
        //如果是协审费，要更改协审计划状态
        if (Constant.EnumState.STOP.getValue().equals(chargeType)) {
            assistPlanRepo.updatePlanStateByBusinessId(businessId, Constant.EnumState.YES.getValue());
        }
        //用于返回页面
        List<FinancialManagerDto> resultList = new ArrayList<>(saveList.size());
        saveList.forEach(l -> {
            FinancialManagerDto fmDto = new FinancialManagerDto();
            BeanCopierUtils.copyPropertiesIgnoreNull(l, fmDto);
            resultList.add(fmDto);
        });
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", resultList);

    }

    /**
     * 根据项目ID查询出对应的协审费用
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignAssistCostDto> findSignCostBySignId(String signId) {
        return assistPlanRepo.findSignCostBySignId(signId);
    }

}