package cs.sql;

import cs.common.HqlBuilder;

/**
 * Created by Administrator on 2018/7/20 0020.
 */
public class ReviewSql {

    public static HqlBuilder updateReviewTitleName(String signId, String newName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update CS_EXPERT_REVIEW set REVIEWTITLE =:newName where BUSINESSID =:businessId ");
        sqlBuilder.setParam("newName","《"+newName+"》评审会评审费发放表").setParam("businessId",signId);
        return sqlBuilder;
    }
}
