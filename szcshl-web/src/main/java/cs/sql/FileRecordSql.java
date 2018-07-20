package cs.sql;

import cs.common.HqlBuilder;

/**
 * Created by Administrator on 2018/7/20 0020.
 */
public class FileRecordSql {

    public static HqlBuilder updateProjNameSql(String signId,String newName){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_FILE_RECORD SET PROJECTNAME = :newName where SIGNID =:signId ");
        sqlBuilder.setParam("newName",newName).setParam("signId",signId);
        return sqlBuilder;
    }
}
