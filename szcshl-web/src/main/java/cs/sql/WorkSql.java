package cs.sql;

import cs.ahelper.projhelper.ProjUtil;
import cs.common.HqlBuilder;
import cs.common.constants.Constant;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;

/**
 * Created by ldm on 2018/7/17 0017.
 */
public class WorkSql {

    public static String getReWorkSql(){
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" SELECT bu.bId,bu.gId,bu.userNames,vg.name FROM ( ");
        hqlBuilder.append(" SELECT bi.bId, bi.gId, wm_concat (CU.DISPLAYNAME) AS userNames ");
        hqlBuilder.append(" FROM ( SELECT br.BRANCHID AS bid, BR.ORGID AS gid, SP.USERID AS urid ");
        hqlBuilder.append(" FROM CS_SIGN_BRANCH br, CS_SIGN_PRINCIPAL2 sp ");
        hqlBuilder.append(" WHERE BR.SIGNID = ? AND BR.SIGNID = SP.SIGNID AND BR.BRANCHID = SP.FLOWBRANCH ");
        hqlBuilder.append(" ORDER BY SP.SORT) bi,CS_USER cu ");
        hqlBuilder.append(" WHERE bi.urid = CU.ID ");
        hqlBuilder.append(" GROUP BY bi.bId, bi.gId) bu,V_ORG_DEPT vg WHERE bu.gId = vg.id");

        return hqlBuilder.getHqlString();
    }

    public static HqlBuilder copyExpertSelected(String businessId){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" INSERT INTO CS_HIS_EXPERT_SELECTED (ID,BUSINESSID,COMPOSITESCORE,");
        sqlBuilder.append(" COMPOSITESCOREEND,CONDITIONID, CREATEBY,DESCRIBES,");
        sqlBuilder.append(" EXPERTTYPE,EXPERTSEQ,ISCONFRIM,ISJOIN,ISLETTERRW,ISSPLIT,");
        sqlBuilder.append(" MAJORBIG,MAJORSMALL,ONECOST,REMARK,REVIEWCOST, REVIEWTAXES,");
        sqlBuilder.append(" SCORE,SELECTINDEX,SELECTTYPE,TOTALCOST,EXPERTREVIEWID,EXPERTID)");
        sqlBuilder.append(" SELECT sel.ID,sel.BUSINESSID,sel.COMPOSITESCORE,sel.COMPOSITESCOREEND,");
        sqlBuilder.append(" sel.CONDITIONID,sel.CREATEBY,sel.DESCRIBES,sel.EXPERTTYPE,sel.EXPERTSEQ,");
        sqlBuilder.append(" sel.ISCONFRIM,sel.ISJOIN,sel.ISLETTERRW,sel.ISSPLIT,sel.MAJORBIG,");
        sqlBuilder.append(" sel.MAJORSMALL,sel.ONECOST,sel.REMARK,sel.REVIEWCOST,sel.REVIEWTAXES,");
        sqlBuilder.append(" sel.SCORE,sel.SELECTINDEX,sel.SELECTTYPE,sel.TOTALCOST,sel.EXPERTREVIEWID,");
        sqlBuilder.append(" sel.EXPERTID");
        sqlBuilder.append(" FROM CS_EXPERT_SELECTED sel WHERE sel.BUSINESSID = :businessId");
        sqlBuilder.setParam("businessId",businessId);

        return sqlBuilder;
    }

    public static HqlBuilder copyExpertReview(String reviewId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" INSERT INTO CS_HIS_EXPERT_REVIEW (ID,CREATEDBY,CREATEDDATE,MODIFIEDBY,MODIFIEDDATE,BUSINESSID,");
        sqlBuilder.append(" BUSINESSTYPE,EXPRETCOUNT,EXTRACTINFO,FINISHEXTRACT,PAYDATE,REVIEWCOST,REVIEWDATE,");
        sqlBuilder.append(" REVIEWTAXES,REVIEWTITLE,SELECTINDEX,STATE,TOTALCOST) ");
        sqlBuilder.append(" SELECT cer.ID,cer.CREATEDBY,cer.CREATEDDATE,cer.MODIFIEDBY,cer.MODIFIEDDATE,cer.BUSINESSID,");
        sqlBuilder.append(" cer.BUSINESSTYPE,cer.EXPRETCOUNT,cer.EXTRACTINFO,cer.FINISHEXTRACT,cer.PAYDATE,cer.REVIEWCOST,");
        sqlBuilder.append(" cer.REVIEWDATE,cer.REVIEWTAXES,cer.REVIEWTITLE,cer.SELECTINDEX,cer.STATE,cer.TOTALCOST");
        sqlBuilder.append(" FROM CS_EXPERT_REVIEW cer WHERE cer.ID = :reviewId ");
        sqlBuilder.setParam("reviewId",reviewId);
        return sqlBuilder;
    }

    public static HqlBuilder copyExpertCondition(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" INSERT INTO CS_HIS_EXPERT_CONDITION (ID,ALTERNATIVENUM,BUSINESSID,COMPOSITESCORE,");
        sqlBuilder.append(" COMPOSITESCOREEND, CREATEBY,EXPERTTYPE,MAJORBIG,MAJORSMALL,OFFICIALNUM,");
        sqlBuilder.append(" SELECTINDEX,SORT,EXPERTREVIEWID)");
        sqlBuilder.append(" SELECT sec.ID,sec.ALTERNATIVENUM,sec.BUSINESSID,sec.COMPOSITESCORE,sec.COMPOSITESCOREEND,");
        sqlBuilder.append(" sec.CREATEBY,sec.EXPERTTYPE,sec.MAJORBIG,sec.MAJORSMALL,sec.OFFICIALNUM,sec.SELECTINDEX,");
        sqlBuilder.append(" sec.SORT,sec.EXPERTREVIEWID FROM CS_EXPERT_CONDITION sec WHERE sec.BUSINESSID = :businessId");
        sqlBuilder.setParam("businessId",businessId);

        return sqlBuilder;
    }

    public static HqlBuilder updateWPProjName(String signId,String newName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_WORK_PROGRAM SET PROJECTNAME = :newName where SIGNID =:signId ");
        sqlBuilder.setParam("newName",newName).setParam("signId",signId);
        return sqlBuilder;
    }

    public static HqlBuilder updateWPHisProjName(String signId, String newName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_HIS_WORK_PROGRAM SET PROJECTNAME = :newName where SIGNID =:signId ");
        sqlBuilder.setParam("newName", ProjUtil.getReFlowName(newName)).setParam("signId",signId);
        return sqlBuilder;
    }

    public static HqlBuilder updateRunFlowName(String signId, String newName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update ACT_RU_EXECUTION set NAME_ =:newName where PROC_INST_ID_ in ");
        sqlBuilder.setParam("newName",ProjUtil.getReFlowName(newName));
        sqlBuilder.append(" (select PROCESSINSTANCEID from CS_WORK_PROGRAM where SIGNID = :signId) ");
        sqlBuilder.setParam("signId",signId);
        return sqlBuilder;
    }

    public static HqlBuilder updateHisFlowName(String signId, String newName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update ACT_HI_PROCINST set NAME_ =:newName where PROC_INST_ID_ in ");
        sqlBuilder.setParam("newName",newName);
        sqlBuilder.append(" (select PROCESSINSTANCEID from CS_WORK_PROGRAM where SIGNID = :signId) ");
        sqlBuilder.setParam("signId",signId);
        return sqlBuilder;
    }


    /**
     * 删除工作方案
     * @param signId
     * @param brandId
     * @return
     */
    public static HqlBuilder deleteWorkProgran(String signId,String brandId){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from cs_work_program where signid =:signid and branchId =:branchId and (baseInfo is null or baseInfo !=:bstate )");
        sqlBuilder.setParam("signid", signId);
        sqlBuilder.setParam("branchId", brandId);
        sqlBuilder.setParam("bstate", Constant.EnumState.YES.getValue());

        return sqlBuilder;
    }

    /**
     * 更新工作方案状态
     * @param signId
     * @param brandIds
     * @return
     */
    public static HqlBuilder updateWPState(String signId, String brandIds,String state) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_work_program set state = :state where signid =:signid ");
        sqlBuilder.setParam("state", state);
        sqlBuilder.setParam("signid", signId);
        sqlBuilder.bulidPropotyString("and", WorkProgram_.branchId.getName(),brandIds);
        return sqlBuilder;
    }
}
