package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.project.AssistPlan;
import cs.model.project.SignAssistCostDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Handler;

/**
 * Description: 协审方案 数据操作实现类
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Repository
public class AssistPlanRepoImpl extends AbstractRepository<AssistPlan, String> implements AssistPlanRepo {
    /**
     * 项目协审费详情
     * @param signAssistCostDto
     * @return
     */
    @Override
    public List<Object[]> signAssistCostList(SignAssistCostDto signAssistCostDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT CPS.SIGNID,CASE CPS.SPLITNUM WHEN NULL THEN CPS.PROJECTNAME WHEN 1 THEN CPS.PROJECTNAME ELSE CPS.PROJECTNAME || '[' || CPS.SPLITNUM || ']' END PROJECTNAME, ");
        sqlBuilder.append(" AU.UNITNAME,CP.PLANNAME,CPS.assistCost,fm.charge,FM.PAYMENTDATA,CPS.estimateCost,CS.ISLIGHTUP,CS.PROCESSSTATE ");
        sqlBuilder.append(" FROM CS_AS_PLAN cp, CS_AS_PLANSIGN cps ");
        sqlBuilder.append(" LEFT JOIN CS_AS_UNIT au ON AU.ID = CPS.ASSISTUNITID ");
        sqlBuilder.append(" LEFT JOIN CS_FINANCIAL_MANAGER fm ON FM.BUSINESSID = CPS.SIGNID  "); //AND FM.CHARGENAME = '协审费'
        sqlBuilder.append(" LEFT JOIN CS_sign cs on CS.SIGNID = CPS.SIGNID ");
        sqlBuilder.append(" WHERE CP.PLANSTATE = :planState AND CP.ID = CPS.PLANID ");
        //后面要改成9，只有完成的才可以发放费用
        sqlBuilder.setParam("planState", Constant.EnumState.PROCESS.getValue());
        //加上查询条件
        if(signAssistCostDto != null){
            if(Validate.isString(signAssistCostDto.getProjectName())){
                sqlBuilder.append(" AND CPS.PROJECTNAME like :projectName ").setParam("projectName","%"+signAssistCostDto.getProjectName()+"%");
            }
            if(Validate.isString(signAssistCostDto.getAssistPlanNo())){
                sqlBuilder.append(" AND CP.PLANNAME like :planName ").setParam("planName","%"+signAssistCostDto.getAssistPlanNo()+"%");
            }
            if(Validate.isString(signAssistCostDto.getAssistUnit())){
                sqlBuilder.append(" AND AU.UNITNAME like :assistUnit ").setParam("assistUnit","%"+signAssistCostDto.getAssistUnit()+"%");
            }
            if(Validate.isString(signAssistCostDto.getBeginTime())){
                sqlBuilder.append(" AND FM.PAYMENTDATA > to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime",signAssistCostDto.getBeginTime().trim() + " 00:00:00");
            }
            if(Validate.isString(signAssistCostDto.getEndTime())){
                sqlBuilder.append(" AND FM.PAYMENTDATA > to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime",signAssistCostDto.getEndTime() + " 23:59:59");
            }

            if(Validate.isString(signAssistCostDto.getSignId())){
                sqlBuilder.append(" AND CPS.SIGNID  = :signId ").setParam("signId",signAssistCostDto.getSignId());
            }
        }

        sqlBuilder.append(" ORDER BY CP.PLANNAME, CPS.SPLITNUM ");

        return getObjectArray(sqlBuilder);
    }
}