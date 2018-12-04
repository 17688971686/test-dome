package cs.sql;

import cs.common.HqlBuilder;

/**
 * Created by Administrator on 2018/7/20 0020.
 */
public class ProjSql {

    public static final String COUNT_PROJ = "select COUNT(signid)  as SIGNNUMBER ,reviewstage  from SIGN_DISP_WORK t where signstate<>7 and signstate<>2 group by t.reviewstage";
    public static final String COUNT_DEAL_PROJ ="select t.signid, t.projectname,t.receivedate,t.surplusdays,t.processInstanceId from SIGN_DISP_WORK t where signstate<>7 and signstate<>2";
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
}
