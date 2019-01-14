package cs.sql;

import cs.common.HqlBuilder;

/**
 * Created by Administrator on 2018/7/20 0020.
 */
public class ProjSql {


    public static HqlBuilder countPorj(){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select COUNT(signid)  as SIGNNUMBER ,reviewstage  ");
        sqlBuilder.append(" from SIGN_DISP_WORK t where signstate<>7 and signstate<>2 group by t.reviewstage ");
        return sqlBuilder;
    }

    public static HqlBuilder countDealPorj(){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select t.signid, t.projectname,t.receivedate,t.surplusdays,t.processInstanceId ");
        sqlBuilder.append(" from SIGN_DISP_WORK t where signstate<>7 and signstate<>2 ");
        return sqlBuilder;
    }

    public static HqlBuilder updateRunFlowName(String processInstanceId,String newName){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update ACT_RU_EXECUTION set NAME_ =:newName where PROC_INST_ID_ =:processInstanceId ");
        sqlBuilder.setParam("newName",newName).setParam("processInstanceId",processInstanceId);
        return sqlBuilder;
    }

    public static HqlBuilder updateHisFlowName(String processInstanceId, String newName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update ACT_HI_PROCINST set NAME_ =:newName where PROC_INST_ID_ =:processInstanceId ");
        sqlBuilder.setParam("newName",newName).setParam("processInstanceId",processInstanceId);
        return sqlBuilder;
    }

    public static String ProjCountSql(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" case reviewstage when '项目建议书' then 1 when '可行性研究报告' then 2 when '项目概算' then 3 ");
        stringBuffer.append(" when '资金申请报告' then 4  when '进口设备' then 5  when '设备清单（国产）'then  6  when '设备清单（进口）'then 7 else 8 end ");
        return stringBuffer.toString();
    }

    public static HqlBuilder countAchievement(String deptIds, String userId, int level, String beginTime, String endTime) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT sd.signid as signId,sd.projectname as projName,sd.dispatchdate as dispatchDate,sd.filenum as fileNum, ");
        sqlBuilder.append(" sd.declarevalue as declareValue,sd.authorizevalue as authorizeValue,sd.extravalue as extraValue, ");
        sqlBuilder.append(" sd.extrarate as extraRate,pu.orgid as orgId,pu.userid as userId,pu.ismainuser as isMainUser,pu.branchid as branchId, ");
        sqlBuilder.append(" pu.displayname as userName,pu.userOrgId,pu.deptIds ");
        sqlBuilder.append(" FROM (SELECT s.signid,s.projectname,d.dispatchdate,d.filenum,d.declarevalue,d.authorizevalue,d.extravalue,d.extrarate ");
        sqlBuilder.append(" FROM cs_sign s, cs_dispatch_doc d WHERE s.signid = d.signid ");
        sqlBuilder.append(" AND s.processstate >= 6 AND s.signstate != 7 ");
        sqlBuilder.append(" AND D.dispatchdate >= TO_DATE (?,'yyyy-mm-dd hh24:mi:ss')");
        sqlBuilder.append(" AND D.dispatchdate <= TO_DATE (?,'yyyy-mm-dd hh24:mi:ss')");
        sqlBuilder.setParam("beginTime",beginTime).setParam("endTime",endTime);
        sqlBuilder.append(" ) sd ,");
        sqlBuilder.append(" (SELECT pbo.signid,pbo.branchid,pbo.orgid,ppu.userid,ppu.ismainuser,cu.displayname, cu.orgid as userOrgid, cu.deptIds as deptIds ");
        sqlBuilder.append(" FROM CS_SIGN_BRANCH pbo, CS_SIGN_PRINCIPAL2 ppu ,");
        //有些旧数据迁移的时候，评审部门只有一个，但是人员是跨部门的，这里要把人员的所在部门和所在组别信息查询出来，方便过滤
        sqlBuilder.append(" (select ncu.id,ncu.displayname,ncu.orgid,deptorg.deptIds from cs_user ncu left join (select USERLIST_ID as deptUserId,wm_concat(SYSDEPTLIST_ID) as deptIds from CS_DEPT_CS_USER group by USERLIST_ID) deptorg on ncu.ID = deptUserId) cu ");
        sqlBuilder.append(" WHERE pbo.signid = ppu.signid AND pbo.branchid = ppu.flowbranch  and cu.id = ppu.userId ");
        if(level == 0){
            //如果是普通用户，则按用户ID查询
            sqlBuilder.append(" and PPU.USERID = ? ").setParam("userId",userId);
        }else{
            //如果是部长或者分管主任，则按部门查询
            sqlBuilder.bulidJdbcPropotyString("and","pbo.orgid",deptIds);
        }

        sqlBuilder.append(" ) pu where sd.signid = pu.signid ORDER BY sd.signid, sd.dispatchdate,pu.branchid");
        return sqlBuilder;
    }
}
