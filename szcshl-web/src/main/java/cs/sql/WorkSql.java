package cs.sql;

import cs.common.HqlBuilder;

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

}