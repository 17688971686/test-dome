package cs.sql;

import cs.common.HqlBuilder;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;

import java.util.List;

/**
 * Created by Administrator on 2018/12/3 0003.
 */
public class FlowSql {

    public static final String activityProSql = "SELECT arp.NAME_,arp.KEY_ FROM act_re_procdef arp where arp.KEY_ != ? GROUP BY arp.NAME_,arp.KEY_";

    public static final String activityUserSql = "SELECT distinct ACT.USER_ID_ USER_ID from ACT_HI_IDENTITYLINK act where act.PROC_INST_ID_ = ?";

    public static HqlBuilder activityCommentSql(String procInstId, List<String> nodeKeys){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT hm.*, CU.DISPLAYNAME AS DISPLAYNAME_ FROM ( ");
        sqlBuilder.append(" SELECT HC.PROC_INST_ID_,HC.TIME_,HC.MESSAGE_,HA.ACT_ID_,HA.ASSIGNEE_ ");
        sqlBuilder.append(" FROM ACT_HI_COMMENT hc, ACT_HI_ACTINST ha WHERE HC.PROC_INST_ID_ = HA.PROC_INST_ID_ ");
        sqlBuilder.append(" AND HC.TASK_ID_ = HA.TASK_ID_ AND hc.PROC_INST_ID_ = :procInstId ").setParam("procInstId", procInstId);
        if (Validate.isList(nodeKeys)) {
            sqlBuilder.bulidPropotyString("AND", "HA.ACT_ID_", StringUtil.listToString(nodeKeys));
        }
        sqlBuilder.append(" AND ha.ACT_TYPE_ = 'userTask' ) hm,cs_user cu WHERE hm.ASSIGNEE_ = CU.ID ORDER BY hm.TIME_");

        return sqlBuilder;
    }
}
