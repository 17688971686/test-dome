package cs.sql;

import cs.common.HqlBuilder;

/**
 * Created by Administrator on 2018/7/31 0031.
 */
public class MeettingSql {

    public static HqlBuilder updateProjNameSql(String signId, String newName){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_ROOM_BOOKING SET RBNAME = :newName where BUSINESSID in (select ID from CS_WORK_PROGRAM where signid = :signId) ");
        sqlBuilder.setParam("newName",newName).setParam("signId",signId);
        return sqlBuilder;
    }

    /**
     * 更新业务ID
     * @param oldBusinessId
     * @param newBusinessId
     * @return
     */
    public static HqlBuilder updateBusinessId(String oldBusinessId, String newBusinessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_ROOM_BOOKING SET BUSINESSID = :newBusinessid where BUSINESSID = :oldBusinessId ");
        sqlBuilder.setParam("newBusinessid",newBusinessId).setParam("oldBusinessId",oldBusinessId);
        return sqlBuilder;
    }
}
