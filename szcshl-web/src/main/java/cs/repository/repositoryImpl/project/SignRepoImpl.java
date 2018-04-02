package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.SignMerge;
import cs.domain.project.SignMerge_;
import cs.domain.project.Sign_;
import cs.model.project.SignDto;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class SignRepoImpl extends AbstractRepository<Sign, String> implements SignRepo {

    /**
     * 修改项目状态
     * @param signId
     * @param stateProperty 状态属性
     * @param stateValue 值
     * @return
     */
    @Override
    public boolean updateSignState(String signId,String stateProperty,String stateValue) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + stateProperty + " =:state ");
        hqlBuilder.setParam("state", stateValue);
        hqlBuilder.bulidPropotyString("where",Sign_.signid.getName(),signId);

        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 更改项目流程状态
     * @param signId
     * @param processState
     * @return
     */
    @Override
    public boolean updateSignProcessState(String signId, Integer processState) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.processState.getName() + " =:processState ");
        hqlBuilder.setParam("processState", processState);
        hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ");
        hqlBuilder.setParam("signid", signId);
        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 根据委里收文编号，不作废的才算新增
     * 获取项目信息
     * @param filecode
     * @return
     */
    @Override
    public Sign findByFilecode(String filecode,String signState) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from  " + Sign.class.getSimpleName());
        hqlBuilder.append(" where " + Sign_.filecode.getName() + " = :filecode ");
        hqlBuilder.setParam("filecode", filecode);
        if(Validate.isString(signState)){
            hqlBuilder.append(" and "+Sign_.signState.getName()+" !=:signState ");
            hqlBuilder.setParam("signState", Constant.EnumState.DELETE.getValue());
        }
        List<Sign> signList = findByHql(hqlBuilder);
        if(Validate.isList(signList)){
            return signList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 根据项目ID获取关联的项目的ID
     * @param signId
     * @return
     */
    @Override
    public boolean checkIsLink(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(*) from cs_associate_sign where signid = :signid or associate_signid=:associate_signid");
        sqlBuilder.setParam("signid",signId).setParam("associate_signid",signId);
        return returnIntBySql(sqlBuilder)>0?true:false;

    }

    /**
     * 根据合并评审主项目ID，查找合并评审项目
     * @param signId
     * @return
     */
    @Override
    public List<Sign> findReviewSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signid.getName() + " in ");
        hqlBuilder.append(" ( select m."+ SignMerge_.mergeId.getName() +" from "+SignMerge.class.getSimpleName()+" m where " +
                "m."+SignMerge_.signId.getName()+" =:signId and m."+SignMerge_.mergeType.getName()+" =:mergeType )");
        hqlBuilder.setParam("signId",signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        List<Sign> signList = findByHql(hqlBuilder);
        return signList;
    }

    /**
     * 根据合并评审次项目ID，查找合并评审主项目
     * @param signId
     * @return
     */
    @Override
    public List<Sign> findMainReviewSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signid.getName() + " = ");
        hqlBuilder.append(" ( select m."+ SignMerge_.signId.getName() +" from "+SignMerge.class.getSimpleName()+" m where " +
                "m."+SignMerge_.mergeId.getName()+" =:signId and m."+SignMerge_.mergeType.getName()+" =:mergeType )");
        hqlBuilder.setParam("signId",signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        List<Sign> signList = findByHql(hqlBuilder);
        return signList;
    }

    /**
     * 根据合并评审主项目ID，判断合并评审次项目是否完成工作方案环节提交
     * @param signid
     * @return
     */
    @Override
    public boolean isMergeSignEndWP(String signid) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(s.signid) from cs_sign s where s.signid in ");
        sqlBuilder.append(" ( select m.mergeid from cs_sign_merge m where m.signid =:signid )");
        sqlBuilder.setParam("signid",signid);
        sqlBuilder.append(" and s.processState < :processState ");
        sqlBuilder.setParam("processState", Constant.SignProcessState.END_WP.getValue(), IntegerType.INSTANCE);

        return returnIntBySql(sqlBuilder)>0?false:true;
    }

    /**
     * 获取未发送给委里的项目信息
     * @return
     */
    @Override
    public List<Sign> findUnSendFGWList() {
        Criteria criteria = getExecutableCriteria();
        //正式签收
        criteria.add(Restrictions.eq(Sign_.issign.getName(), Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.isNotNull(Sign_.filecode.getName()));
        //未发送给发改委的项目
        criteria.add(Restrictions.or(Restrictions.isNull(Sign_.isSendFGW.getName()),Restrictions.ne(Sign_.isSendFGW.getName(), Constant.EnumState.YES.getValue())));
        //排除旧项目
        criteria.add(Restrictions.isNull(Sign_.oldProjectId.getName()));
        criteria.add(Restrictions.isNotNull(Sign_.processInstanceId.getName()));
        //正在进行或者正常结束
        criteria.add(Restrictions.or(Restrictions.eq(Sign_.signState.getName(), Constant.EnumState.PROCESS.getValue()),
                Restrictions.eq(Sign_.signState.getName(), Constant.EnumState.YES.getValue())));
        //已经生成发文编号
        criteria.add(Restrictions.ge(Sign_.processState.getName(), Constant.SignProcessState.END_DIS_NUM.getValue()));

       /* //以下是测试用
        criteria.add(Restrictions.eq(Sign_.signid.getName(), "4a2271a6-5908-4d05-b609-c48ed49d6e3f"));
        */
        List<Sign> resultList = criteria.list();

        return resultList;
    }

    /**
     * 统计项目平均天数，未办结的按当前日期算，已办结的按办结日期算
     * @param signIds
     * @return
     */
    @Override
    public int sumExistDays(String signIds) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT TRUNC(SUM ( CASE WHEN sf.SIGNSTATE = '9' THEN ");
        sqlBuilder.append(" (CASE WHEN sf.fileDate IS NOT NULL THEN (TO_DATE (TO_CHAR (sf.fileDate, 'yyyy-mm-dd'),'yyyy-mm-dd')) ");
        sqlBuilder.append(" WHEN sf.fileDate IS NULL THEN SYSDATE END - sf.recedate) ");
        sqlBuilder.append(" ELSE (SYSDATE - sf.recedate) END ) ) sumDays ");
        sqlBuilder.append(" FROM (SELECT cf.fileDate,");
        sqlBuilder.append(" CASE WHEN CS.receivedate IS NOT NULL THEN CS.receivedate WHEN CS.receivedate IS NULL THEN cs.signdate ELSE SYSDATE END recedate, CS.SIGNSTATE ");
        sqlBuilder.append(" FROM cs_sign cs LEFT JOIN CS_FILE_RECORD cf ON CS.SIGNID = CF.SIGNID ");
        sqlBuilder.bulidPropotyString("where","CS.SIGNID",signIds);
        sqlBuilder.append(" ) sf ");
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 通过收文id查询 评审天数、剩余工作日、收文日期、送来日期、评审总天数等
     * @param signId
     * @return
     */
    @Override
    public SignDto findReviewDayBySignId(String signId) {
        SignDto signDto = new SignDto();
        signDto.setSignid(signId);
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select reviewdays  , surplusdays , signdate  , receivedate ,lengthenDays , lengthenExp , totalReviewdays  from cs_sign  where " + Sign_.signid.getName() + "=:signId");
        hqlBuilder.setParam("signId" , signId);
        List<Object[]> signList = this.getObjectArray(hqlBuilder);
        if(signList != null && signList.size() > 0 ){
            Object[] objects = signList.get(0);
//            signDto.setReviewdays(objects[0] == null ? 0 : Float.valueOf(objects[0].toString()));
            if(signDto.getTotalReviewdays() != null && signDto.getSurplusdays() != null){

                signDto.setReviewdays((signDto.getTotalReviewdays() < signDto.getSurplusdays() || objects[0] == null) ? 0 : Float.valueOf(objects[0].toString()));
            }else{
                signDto.setReviewdays(Float.valueOf(0));
            }
            signDto.setSurplusdays(objects[1] == null ? 0 : Float.valueOf(objects[1].toString()));
            signDto.setSigndate(DateUtils.converToDate(objects[2].toString() , "yyyy-MM-dd"));

            //由于目前 送来日期为空，所以需要判断是否为空
            if(objects[3] != null){

                signDto.setReceivedate(DateUtils.converToDate(objects[3].toString() , "yyyy-MM-dd"));
            }
//            signDto.setGoneDays(signDto.getTotalReviewdays() < signDto.getSurplusdays() ? 0 : signDto.getTotalReviewdays() - signDto.getSurplusdays()); //已逝工作日
            signDto.setOverDays(signDto.getSurplusdays() >= 0 ? 0 : Math.abs(signDto.getSurplusdays())); //延长工作日，如果剩余工作日大于等于0 ，则为0 否则取剩余工作日绝对值
            signDto.setLengthenDays( objects[4] == null ? 0 : Float.valueOf(objects[4].toString()));
            signDto.setLengthenExp(objects[5] == null ? "" : objects[5].toString());


            signDto.setTotalReviewdays(objects[6] == null ? 0 : Float.valueOf(objects[6].toString()));
        }

        return signDto;
    }

    /**
     * 保存评审工作日维护的信息
     * @param signDto
     * @return
     */
    @Override
    public ResultMsg saveReview(SignDto signDto) {

        Sign sign = this.findById(Sign_.signid.getName() , signDto.getSignid());

        BeanCopierUtils.copyPropertiesIgnoreNull(signDto , sign);

        this.save(sign);

//        HqlBuilder hqlBuilder = HqlBuilder.create();
//        hqlBuilder.append("  update cs_sign set  " + Sign_.reviewdays.getName() + "=:reviewDays , ");
//        hqlBuilder.append(Sign_.surplusdays.getName() + "=:surplusdays , ");
//        hqlBuilder.append(Sign_.lengthenDays.getName() + "=:lengthenDays , ");
//        hqlBuilder.append(Sign_.lengthenExp.getName() + "=:lengthenExp ");
//        hqlBuilder.append(" where "  + Sign_.signid.getName() + "=:signId");
//        hqlBuilder.setParam("reviewDays" , signDto.getReviewdays());
//        hqlBuilder.setParam("surplusdays" , signDto.getSurplusdays());
//        hqlBuilder.setParam("lengthenDays" , signDto.getLengthenDays());
//        hqlBuilder.setParam("lengthenExp" , signDto.getLengthenExp());
//        hqlBuilder.setParam("signId" , signDto.getSignid());
//        this.executeHql(hqlBuilder);

        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "保存成功！");
    }

    /**
     * 更新拟拟补充资料函状态
     * @param businessId
     * @param value
     * @param disapDate
     */
    @Override
    public void updateSuppLetterState(String businessId, String value, Date disapDate) {
        try{
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" update cs_sign ");
            sqlBuilder.append(" set " + Sign_.isHaveSuppLetter.getName() + " =:stateValue ").setParam("stateValue", value);
            sqlBuilder.append(" ,"+Sign_.suppLetterDate.getName()+" = to_date(:disapDate,'yyyy-mm-dd') ").setParam("disapDate",DateUtils.converToString(disapDate,"yyyy-MM-dd"));
            sqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ").setParam("signid", businessId);
            sqlBuilder.append(" and "+ Sign_.isHaveSuppLetter.getName() +" is null or "+ Sign_.isHaveSuppLetter.getName() +" != '9'");
            executeSql(sqlBuilder);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * TODO
     * 重新初始化协办部门和协办负责人信息
     * @param sign
     * @param signId
     * @param branchId  排除的分支
     */
    @Override
    public void initAOrgAndUser(Sign sign, String signId, String branchId) {
        /**
         * SELECT CASE WHEN aorgid IS NOT NULL
         THEN TRIM (',' FROM REPLACE (REGEXP_REPLACE (aorgid, '[^@]+[,$]', ''),'@',','))
         ELSE ''
         END aorgid,
         CASE
         WHEN aorgid IS NOT NULL
         THEN TRIM (',' FROM REPLACE (REGEXP_REPLACE (aorgid, '[^,]+[@$]', ''),'@'','))
         ELSE
         ''
         END aorgname
         FROM (SELECT ',' || SUBSTR (orgid, INSTR (orgid, ',') + 1) || ',' aorgid
         FROM (  SELECT MAX (orgid) orgid
         FROM (SELECT csb.SIGNID,
         wm_concat (csb.ORGID || '@' || VOD.NAME) OVER (PARTITION BY csb.SIGNID ORDER BY csb.ISMAINBRABCH DESC) orgid
         FROM V_ORG_DEPT vod, CS_SIGN_BRANCH csb
         WHERE csb.signid = 'fdc903f6-0b0a-4bad-a4c0-8bc228c3c557' and csb.ISMAINBRABCH != '9' and VOD.ID = csb.ORGID)
         ))
         */
        /*//1、查询除了主办部门外，排除分支的其它协办部门信息
        HqlBuilder orgBuilder = HqlBuilder.create();
        orgBuilder.append(" SELECT CASE WHEN aorgid IS NOT NULL THEN TRIM (',' FROM REPLACE (REGEXP_REPLACE (aorgid, '[^@]+[,$]', ''),'@',',')) ELSE '' END aorgid, ");
        orgBuilder.append(" CASE WHEN aorgid IS NOT NULL THEN TRIM (',' FROM REPLACE (REGEXP_REPLACE (aorgid, '[^,]+[@$]', ''),'@'',')) ELSE '' END aorgname ");
        orgBuilder.append(" FROM (SELECT ',' || SUBSTR (orgid, INSTR (orgid, ',') + 1) || ',' aorgid ");
        orgBuilder.append(" FROM (  SELECT MAX (orgid) orgid ");
        orgBuilder.append(" FROM (SELECT csb.SIGNID, wm_concat (csb.ORGID || '@' || VOD.NAME) OVER (PARTITION BY csb.SIGNID ORDER BY csb.ISMAINBRABCH DESC) orgid ");
        orgBuilder.append(" FROM V_ORG_DEPT vod, CS_SIGN_BRANCH csb ");
        orgBuilder.append(" WHERE csb.signid = :signid and csb.ISMAINBRABCH != :mainbranch  ");
        orgBuilder.setParam("signid",signId).setParam("mainbranch", Constant.EnumState.YES.getValue());
        if(Validate.isString(branchId)){
            orgBuilder.append(" and csb.BRANCHID != :branchId ").setParam("branchId",branchId);
        }
        orgBuilder.append(" and VOD.ID = csb.ORGID) )) ");
        List<Object[]> resultList = getObjectArray(orgBuilder);
        if(!Validate.isList(resultList)){
            sign.setaOrgId("");
            sign.setaOrgName("");
        }else{
            for(Object[] obj : resultList){
                sign.setaOrgId(obj[0]==null?"":obj[0].toString());
                sign.setaOrgName(obj[1]==null?"":obj[1].toString());
            }
        }*/

        //查询除了主办分支外的其它负责人
        /**
         * SELECT CASE WHEN auserid IS NOT NULL THEN TRIM (',' FROM REPLACE (REGEXP_REPLACE (auserid, '[^@]+[,$]', ''),'@', ',')) ELSE '' END auserid,
         CASE WHEN auserid IS NOT NULL THEN TRIM ( ',' FROM REPLACE (REGEXP_REPLACE (auserid, '[^,]+[@$]', ''), '@', ',')) ELSE '' END ausername
         FROM (SELECT ',' || SUBSTR (userid, INSTR (userid, ',') + 1) || ',' auserid
         FROM (SELECT MAX (userId) userid
         FROM (SELECT sp.signid, wm_concat ( SP.USERID || '@' || CU.DISPLAYNAME) OVER (PARTITION BY sp.SIGNID ORDER BY SP.ISMAINUSER DESC, sp.SORT) userId
         FROM CS_SIGN_PRINCIPAL2 sp, cs_user cu
         WHERE     sp.signid = 'fdc903f6-0b0a-4bad-a4c0-8bc228c3c557' AND SP.FLOWBRANCH != '1'
         AND SP.USERID = CU.ID)));
         */
        HqlBuilder userBuilder = HqlBuilder.create();
        userBuilder.append(" SELECT CASE WHEN auserid IS NOT NULL THEN TRIM (',' FROM REPLACE (REGEXP_REPLACE (auserid, '[^@]+[,$]', ''),'@', ',')) ELSE '' END auserid,");
        userBuilder.append(" CASE WHEN auserid IS NOT NULL THEN TRIM ( ',' FROM REPLACE (REGEXP_REPLACE (auserid, '[^,]+[@$]', ''), '@', ',')) ELSE '' END ausername ");
        userBuilder.append(" FROM (SELECT ',' || SUBSTR (userid, INSTR (userid, ',') + 1) || ',' auserid ");
        userBuilder.append(" FROM (SELECT MAX (userId) userid ");
        userBuilder.append(" FROM (SELECT sp.signid, wm_concat ( SP.USERID || '@' || CU.DISPLAYNAME) OVER (PARTITION BY sp.SIGNID ORDER BY SP.ISMAINUSER DESC, sp.SORT) userId ");
        userBuilder.append(" FROM CS_SIGN_PRINCIPAL2 sp, cs_user cu ");
        userBuilder.append(" WHERE sp.signid = :signid AND SP.FLOWBRANCH != '1'  ");
        userBuilder.setParam("signid",signId);
        if(Validate.isString(branchId)){
            userBuilder.append(" and SP.FLOWBRANCH != :branchId ").setParam("branchId",branchId);
        }
        userBuilder.append(" and AND SP.USERID = CU.ID) )) ");
        List<Object[]> resultList = getObjectArray(userBuilder);
        if(!Validate.isList(resultList)){
            sign.setaUserID("");
            sign.setaUserName("");
        }else{
            for(Object[] obj : resultList){
                sign.setaUserID(obj[0]==null?"":obj[0].toString());
                sign.setaUserName(obj[1]==null?"":obj[1].toString());
            }
        }
    }
}
