package cs.sql;

import cs.common.HqlBuilder;

/**
 * Created by ldm on 2018/7/20 0020.
 */
public class ReviewSql {

    public static HqlBuilder updateReviewTitleName(String signId, String newName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update CS_EXPERT_REVIEW set REVIEWTITLE =:newName where BUSINESSID =:businessId ");
        sqlBuilder.setParam("newName","《"+newName+"》评审会评审费发放表").setParam("businessId",signId);
        return sqlBuilder;
    }

    public static HqlBuilder resetAllExtract(String id) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        /**
         * 原先是修改所有的抽取状态，现在只修改评审费发放日期
         * update CS_EXPERT_REVIEW set EXTRACTINFO = null,SELECTINDEX = 0,FINISHEXTRACT = 0,PAYDATE = null where ID =:id
         */
        sqlBuilder.append(" update CS_EXPERT_REVIEW set PAYDATE = null where ID =:id ");
        sqlBuilder.setParam("id",id);
        return sqlBuilder;
    }
}
