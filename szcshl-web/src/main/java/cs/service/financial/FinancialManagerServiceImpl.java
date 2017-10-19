package cs.service.financial;

import cs.common.HqlBuilder;
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

        if (record.getId() == null) {
            domain.setId(UUID.randomUUID().toString());
        }
        domain.setCreatedBy(SessionUtil.getLoginName());
        domain.setModifiedBy(SessionUtil.getLoginName());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        financialManagerRepo.save(domain);
    }

    @Override
    @Transactional
    public void update(FinancialManagerDto record) {
        FinancialManager domain = financialManagerRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        //domain.setModifiedBy(currentUser.getLoginName());
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
		hql.append(" where " +FinancialManager_.signid.getName()+" =:businessId");
	    hql.setParam("businessId", businessId);


		return  new BigDecimal(financialManagerRepo.returnIntBySql(hql));
	}

    @Override
    public Map<String, Object> initfinancialData(String businessId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
        FinancialManagerDto financialDto = new FinancialManagerDto();
        financialDto.setProjectName(sign.getProjectname());
        financialDto.setSignid(sign.getSignid());
        financialDto.setPaymentData(new Date());

        financialDto.setAssissCost(sign.getDeclaration());                    //计划协审费用
        financialDto.setAssistBuiltcompanyName(sign.getBuiltcompanyName());//协审单位

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + FinancialManager.class.getSimpleName() + " where " + FinancialManager_.businessId.getName() + " =:businessId");
        hqlBuilder.setParam("businessId", businessId);
        List<FinancialManager> financiallist = financialManagerRepo.findByHql(hqlBuilder);

        map.put("financiallist", financiallist);
        map.put("financialDto", financialDto);
        return map;
    }

    /**
     * 协审费用统计
     * @param signAssistCostDto
     * @param isShowDetail 是否显示费用详情
     * @return
     */
    @Override
    public List<SignAssistCostDto> signAssistCostList(SignAssistCostDto signAssistCostDto,boolean isShowDetail) {
        List<Object[]> signAssistList = assistPlanRepo.signAssistCostList(signAssistCostDto);
        if(Validate.isList(signAssistList)){
            List<SignAssistCostDto> resultList = new ArrayList<>(signAssistList.size());
            for(int i=0,l=signAssistList.size();i<l;i++){
                SignAssistCostDto saDto = new SignAssistCostDto();
                Object[] obj = signAssistList.get(i);
                String signId = obj[0]==null?"":obj[0].toString();
                String projectName = obj[1]==null?"":obj[1].toString();
                String assistUnit = obj[2]==null?"":obj[2].toString();
                String assistPlanNo = obj[3]==null?"":obj[3].toString();
                BigDecimal planCost = obj[4]==null?null:ObjectUtils.getBigDecimal(obj[4]);
                BigDecimal factCost = obj[5]==null?null:ObjectUtils.getBigDecimal(obj[5]);
                Date payDate =  DateUtils.converToDate(obj[6]==null?"":obj[6].toString(),null);
                BigDecimal applyCost = obj[7]==null?null:ObjectUtils.getBigDecimal(obj[7]);
                String lightState = obj[8]==null?"":obj[8].toString();

                String changeUserName = "";
                List<User> priUserList = signPrincipalService.getSignPriUser(signId,null);
                if(Validate.isList(priUserList)){
                    for(int n=0,m=priUserList.size();n<m;n++){
                        if(n > 0){
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

                if(isShowDetail){
                    List<FinancialManager> fmList = financialManagerRepo.findByIds(FinancialManager_.businessId.getName(),signId,null);
                    if(Validate.isList(fmList)){
                        List<FinancialManagerDto> fmDtoList = new ArrayList<>(fmList.size());
                        fmList.forEach( fl -> {
                            FinancialManagerDto fmDto = new FinancialManagerDto();
                            BeanCopierUtils.copyProperties(fl,fmDto);
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

}