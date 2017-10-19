package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.project.AssistPlan;
import cs.model.project.SignAssistCostDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        sqlBuilder.append(" AU.UNITNAME,CP.PLANNAME,CPS.assistCost,fm.charge,FM.PAYMENTDATA,CPS.estimateCost,CS.ISLIGHTUP ");
        sqlBuilder.append(" FROM CS_AS_PLAN cp, CS_AS_PLANSIGN cps ");
        sqlBuilder.append(" LEFT JOIN CS_AS_UNIT au ON AU.ID = CPS.ASSISTUNITID ");
        sqlBuilder.append(" LEFT JOIN CS_FINANCIAL_MANAGER fm ON FM.BUSINESSID = CPS.SIGNID AND FM.CHARGENAME = '协审费' ");
        sqlBuilder.append(" LEFT JOIN CS_sign cs on CS.SIGNID = CPS.SIGNID ");
        sqlBuilder.append(" WHERE CP.PLANSTATE = :planState AND CP.ID = CPS.PLANID ");
        //后面要改成9，只有完成的才可以发放费用
        sqlBuilder.setParam("planState", Constant.EnumState.PROCESS.getValue());
        sqlBuilder.append(" ORDER BY CP.PLANNAME, CPS.SPLITNUM ");

        return getObjectArray(sqlBuilder);
    }
}