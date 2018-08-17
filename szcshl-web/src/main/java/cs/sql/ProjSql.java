package cs.sql;

import cs.common.HqlBuilder;

/**
 * Created by Administrator on 2018/7/20 0020.
 */
public class ProjSql {

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
}
