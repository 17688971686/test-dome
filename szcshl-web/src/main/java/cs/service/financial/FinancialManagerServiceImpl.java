package cs.service.financial;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.sys.User;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignAssistCostDto;
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
        FinancialManager domain = financialManagerRepo.findById(FinancialManager_.id.getName(),record.getId());
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
     * 初始化财务报销页面
     *
     * @param businessId   业务ID
     * @param businessType 类型
     * @return
     */
    @Override
    public Map<String, Object> initfinancialData(String businessId, String businessType) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (Validate.isString(businessType)) {

        }
        List<FinancialManager> financiallist = financialManagerRepo.findByIds(FinancialManager_.businessId.getName(), businessId, null);
        map.put("financiallist", financiallist);
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
        List<Object[]> signAssistList = assistPlanRepo.signAssistCostList(signAssistCostDto);
        if (Validate.isList(signAssistList)) {
            List<SignAssistCostDto> resultList = new ArrayList<>(signAssistList.size());
            for (int i = 0, l = signAssistList.size(); i < l; i++) {
                SignAssistCostDto saDto = new SignAssistCostDto();
                Object[] obj = signAssistList.get(i);
                String signId = obj[0] == null ? "" : obj[0].toString();
                String projectName = obj[1] == null ? "" : obj[1].toString();
                String assistUnit = obj[2] == null ? "" : obj[2].toString();
                String assistPlanNo = obj[3] == null ? "" : obj[3].toString();
                BigDecimal planCost = obj[4] == null ? null : ObjectUtils.getBigDecimal(obj[4]);
                BigDecimal factCost = obj[5] == null ? null : ObjectUtils.getBigDecimal(obj[5]);
                Date payDate = DateUtils.converToDate(obj[6] == null ? "" : obj[6].toString(), null);
                BigDecimal applyCost = obj[7] == null ? null : ObjectUtils.getBigDecimal(obj[7]);
                String lightState = obj[8] == null ? "" : obj[8].toString();

                String changeUserName = "";
                List<User> priUserList = signPrincipalService.getSignPriUser(signId, null);
                if (Validate.isList(priUserList)) {
                    for (int n = 0, m = priUserList.size(); n < m; n++) {
                        if (n > 0) {
                            changeUserName += ",";
                        }
                        changeUserName += priUserList.get(n).getDisplayName();
                    }
                }

                saDto.setSignId(signId);
                saDto.setProjectName(projectName);
                saDto.setAssistUnit(assistUnit);
                saDto.setAssistPlanNo(assistPlanNo);
                saDto.setChangeUserName(changeUserName);
                saDto.setPlanCost(planCost);
                saDto.setFactCost(factCost);
                saDto.setPayDate(payDate);
                saDto.setApplyCost(applyCost);
                saDto.setIsLightUp(lightState);

                if (isShowDetail) {
                    List<FinancialManager> fmList = financialManagerRepo.findByIds(FinancialManager_.businessId.getName(), signId, null);
                    if (Validate.isList(fmList)) {
                        List<FinancialManagerDto> fmDtoList = new ArrayList<>(fmList.size());
                        fmList.forEach(fl -> {
                            FinancialManagerDto fmDto = new FinancialManagerDto();
                            BeanCopierUtils.copyProperties(fl, fmDto);
                            fmDtoList.add(fmDto);
                        });
                        saDto.setCostList(fmDtoList);
                    }
                }
                resultList.add(saDto);
            }
            return resultList;
        }
        return null;
    }

    /**
     * 批量保存评审费
     * @param record
     * @return
     */
    @Override
    public ResultMsg save(FinancialManagerDto[] record) {
        if(record == null || record.length == 0){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败，请先填写费用项信息！");
        }
        List<FinancialManager> saveList = new ArrayList<>(record.length);
        for(int i=0,l=record.length;i<l;i++){
            FinancialManagerDto dto = record[i];
            if(!Validate.isString(dto.getChargeName()) || !Validate.isObject(dto.getCharge())){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败，存在费用名称或者费用没有填写！");
            }
            FinancialManager financialManager = new FinancialManager();
            Date now = new Date();
            if (Validate.isString(dto.getId())) {
                financialManager = financialManagerRepo.findById(dto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(dto,financialManager);
            }else{
                BeanCopierUtils.copyProperties(dto, financialManager);
                financialManager .setId(UUID.randomUUID().toString());
                financialManager.setCreatedDate(now);
                financialManager.setCreatedBy(SessionUtil.getDisplayName());
            }
            financialManager.setModifiedBy(SessionUtil.getDisplayName());
            financialManager.setModifiedDate(now);
            saveList.add(financialManager);
        }
        financialManagerRepo.bathUpdate(saveList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！",saveList);

    }

}