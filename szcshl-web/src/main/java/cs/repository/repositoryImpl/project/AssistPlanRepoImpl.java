package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.ObjectUtils;
import cs.common.utils.Validate;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.project.AssistPlan;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignAssistCostDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public List<Object[]> signAssistCostList(SignAssistCostDto signAssistCostDto,boolean isShowDetail) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT CS.SIGNID,CS.PROJECTNAME,CP.PLANNAME,PSU.UNITNAME,CSP.USERNAME,CPT.ASSISTCOST, ");
        sqlBuilder.append(" FMC.FACTCOST,CDD.DECLAREVALUE,CDD.AUTHORIZEVALUE,CS.ISLIGHTUP,CS.PROCESSSTATE,FMC.PAYDAY ");
        if(isShowDetail){
            sqlBuilder.append(" ,FMD.CHARGE,FMD.CHARGENAME,FMD.PAYMENTDATA ");
        }
        sqlBuilder.append(" FROM (SELECT CPH.ASSISTCOST, CPH.SIGNID, CPH.PLANID FROM CS_AS_PLANSIGN CPH WHERE CPH.ASSISTTYPE = '合并项目' ");
        sqlBuilder.append(" UNION SELECT SUM (CPD.ASSISTCOST), CPD.SIGNID, CPD.PLANID FROM CS_AS_PLANSIGN CPD WHERE CPD.ASSISTTYPE = '独立项目' GROUP BY CPD.SIGNID, CPD.PLANID) CPT ");
        sqlBuilder.append(" LEFT JOIN CS_AS_PLAN CP ON CP.ID = CPT.PLANID ");
        sqlBuilder.append(" LEFT JOIN (  ");
        sqlBuilder.append(" SELECT wm_concat (PU.UNITSHORTNAME) UNITNAME, PS.SIGNID FROM CS_AS_PLANSIGN PS, CS_AS_UNIT PU ");
        sqlBuilder.append(" WHERE PS.ASSISTUNITID = PU.ID AND PS.ASSISTTYPE = '独立项目' GROUP BY PS.SIGNID ");
        sqlBuilder.append(" ) PSU ON PSU.SIGNID = CPT.SIGNID ");
        sqlBuilder.append(" LEFT JOIN CS_SIGN CS ON CS.SIGNID = CPT.SIGNID ");
        sqlBuilder.append(" LEFT JOIN CS_DISPATCH_DOC CDD ON CDD.SIGNID = CPT.SIGNID ");
        sqlBuilder.append(" LEFT JOIN ( ");
        sqlBuilder.append(" SELECT SUM (FM.CHARGE) FACTCOST,MAX(FM.PAYMENTDATA) PAYDAY,FM.BUSINESSID FROM CS_FINANCIAL_MANAGER FM ");
        sqlBuilder.append(" WHERE FM.CHARGETYPE = '2' AND FM.CHARGENAME = '协审费' GROUP BY FM.BUSINESSID ");
        sqlBuilder.append(" ) FMC ON FMC.BUSINESSID = CPT.SIGNID ");
        sqlBuilder.append(" LEFT JOIN( ");
        sqlBuilder.append(" SELECT wm_concat (u.displayname) userName, csp1.signid csignid FROM CS_USER u, CS_SIGN_PRINCIPAL2 csp1 ");
        sqlBuilder.append(" WHERE csp1.userid = u.id GROUP BY csp1.signid ");
        sqlBuilder.append(" ) csp ON csp.csignid = CPT.SIGNID ");
        if(isShowDetail){
            sqlBuilder.append(" LEFT JOIN CS_FINANCIAL_MANAGER FMD ON CPT.SIGNID = FMD.BUSINESSID AND FMD.CHARGETYPE = '2' AND FMD.CHARGENAME = '协审费' ");
        }
        //表示协审项目
        sqlBuilder.append(" WHERE CS.ISASSISTPROC = :isassistproc");
        sqlBuilder.setParam("isassistproc", Constant.EnumState.YES.getValue());

        //有状态的要加上状态
        if(isShowDetail){
            sqlBuilder.append(" AND CP.PLANSTATE = :planState ");
            sqlBuilder.setParam("planState", Constant.EnumState.YES.getValue());
        }

        //加上查询条件
        if(signAssistCostDto != null){
            if(Validate.isString(signAssistCostDto.getProjectName())){
                sqlBuilder.append(" AND CS.PROJECTNAME like :projectName ").setParam("projectName","%"+signAssistCostDto.getProjectName()+"%");
            }
            if(Validate.isString(signAssistCostDto.getAssistPlanNo())){
                sqlBuilder.append(" AND CP.PLANNAME like :planName ").setParam("planName","%"+signAssistCostDto.getAssistPlanNo()+"%");
            }
            if(Validate.isString(signAssistCostDto.getAssistUnit())){
                sqlBuilder.append(" AND PSU.UNITNAME like :assistUnit ").setParam("assistUnit","%"+signAssistCostDto.getAssistUnit()+"%");
            }
            if(Validate.isString(signAssistCostDto.getBeginTime())){
                sqlBuilder.append(" AND CS.SIGNDATE > to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime",signAssistCostDto.getBeginTime().trim() + " 00:00:00");
            }
            if(Validate.isString(signAssistCostDto.getEndTime())){
                sqlBuilder.append(" AND CS.SIGNDATE < to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime",signAssistCostDto.getEndTime() + " 23:59:59");
            }

            if(Validate.isString(signAssistCostDto.getSignId())){
                sqlBuilder.append(" AND CS.SIGNID  = :signId ").setParam("signId",signAssistCostDto.getSignId());
            }
        }

        sqlBuilder.append(" ORDER BY CS.SIGNDATE DESC ");

        return getObjectArray(sqlBuilder);
    }

    /**
     * 根据业务ID，更新协审计划状态
     * @param businessId
     * @param value
     */
    @Override
    public void updatePlanStateByBusinessId(String businessId, String value) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_AS_PLAN SET planState = :planState ").setParam("planState", value);
        sqlBuilder.append(" WHERE id = (SELECT DISTINCT planid FROM CS_AS_PLANSIGN WHERE SIGNID =:signId ) ");
        sqlBuilder.setParam("signId",businessId);
        executeSql(sqlBuilder);
    }

    /**
     * 根据项目ID查询出对应的协审费用
     * @param signIdParam
     * @return
     */
    @Override
    public List<SignAssistCostDto> findSignCostBySignId(String signIdParam) {
        /**
         *
         SELECT CS.SIGNID,
         CASE CP.PROJECTNAME WHEN null THEN CS.PROJECTNAME || CP.SPLITNUM WHEN '' THEN CS.PROJECTNAME || CP.SPLITNUM ELSE CP.PROJECTNAME END PROJECTNAME,
         CU.UNITNAME,
         CSP.USERNAME,
         CP.ASSISTCOST,
         CDD.DECLAREVALUE,
         CDD.AUTHORIZEVALUE
         FROM CS_AS_PLANSIGN CP
         LEFT JOIN CS_AS_UNIT CU ON CU.ID = CP.ASSISTUNITID
         LEFT JOIN CS_SIGN CS ON CS.SIGNID = CP.SIGNID
         LEFT JOIN CS_DISPATCH_DOC CDD ON CDD.SIGNID = CP.SIGNID
         LEFT JOIN
         (  SELECT wm_concat (u.displayname) userName, csp1.signid csignid
         FROM CS_USER u, CS_SIGN_PRINCIPAL2 csp1
         WHERE csp1.userid = u.id
         GROUP BY csp1.signid) csp
         ON csp.csignid = CP.signid
         WHERE CP.SIGNID = '6ccc3697-a151-45c0-8910-0f8c369cd594'
         */
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT CS.SIGNID,CASE CP.PROJECTNAME WHEN null THEN CS.PROJECTNAME || CP.SPLITNUM WHEN '' THEN CS.PROJECTNAME || CP.SPLITNUM ELSE CP.PROJECTNAME END PROJECTNAME,");
        sqlBuilder.append(" CU.UNITNAME,CSP.USERNAME,CP.ASSISTCOST,CDD.DECLAREVALUE,CDD.AUTHORIZEVALUE ");
        sqlBuilder.append(" FROM CS_AS_PLANSIGN CP ");
        sqlBuilder.append(" LEFT JOIN CS_AS_UNIT CU ON CU.ID = CP.ASSISTUNITID ");
        sqlBuilder.append(" LEFT JOIN CS_SIGN cs ON CS.SIGNID = CP.SIGNID ");
        sqlBuilder.append(" LEFT JOIN CS_DISPATCH_DOC CDD ON CDD.SIGNID = CP.SIGNID ");
        sqlBuilder.append(" LEFT JOIN ( SELECT wm_concat (u.displayname) userName, csp1.signid csignid FROM CS_USER u, CS_SIGN_PRINCIPAL2 csp1 WHERE csp1.userid = u.id GROUP BY csp1.signid) csp ON csp.csignid = CP.signid ");
        sqlBuilder.append(" WHERE CP.SIGNID = :signId ").setParam("signId",Validate.isString(signIdParam)?signIdParam:"1");
        List<Object[]> objectList = getObjectArray(sqlBuilder);
        if (Validate.isList(objectList)) {
            List<SignAssistCostDto> resultList = new ArrayList<>(objectList.size());
            for (int i = 0, l = objectList.size(); i < l; i++) {
                SignAssistCostDto saDto = new SignAssistCostDto();
                Object[] obj = objectList.get(i);
                //项目ID
                String signId = obj[0] == null ? "" : obj[0].toString();
                //项目名称
                String projectName = obj[1] == null ? "" : obj[1].toString();
                //协审单位
                String assistUnit = obj[2] == null ? "" : obj[2].toString();
                //项目负责人
                String changeUserName = obj[3] == null ? "" : obj[3].toString();
                //协审计划费用
                BigDecimal planCost = obj[4] == null ? null : ObjectUtils.getBigDecimal(obj[4]);
                //申报金额
                BigDecimal declareValue = obj[5] == null ? null : ObjectUtils.getBigDecimal(obj[5]);
                //审定金额
                BigDecimal authorizeValue = obj[6] == null ? null : ObjectUtils.getBigDecimal(obj[6]);

                saDto.setSignId(signId);
                saDto.setProjectName(projectName);
                saDto.setAssistUnit(assistUnit);
                saDto.setChangeUserName(changeUserName);
                saDto.setPlanCost(planCost);
                saDto.setDeclareValue(declareValue);
                saDto.setAuthorizeValue(authorizeValue);
                resultList.add(saDto);
            }
            return resultList;
        }
        return null;
    }
}