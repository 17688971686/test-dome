package cs.repository.repositoryImpl.project;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.project.*;
import cs.domain.sys.*;
import cs.model.project.SignDto;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SignRepoImpl extends AbstractRepository<Sign, String> implements SignRepo {
    @Autowired
    private SignPrincipalRepo signPrincipalRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private SignBranchRepo signBranchRepo;

    /**
     * 修改项目状态
     *
     * @param signId
     * @param stateProperty 状态属性
     * @param stateValue    值
     * @return
     */
    @Override
    public boolean updateSignState(String signId, String stateProperty, String stateValue) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + stateProperty + " =:state ");
        hqlBuilder.setParam("state", stateValue);
        hqlBuilder.bulidPropotyString("where", Sign_.signid.getName(), signId);

        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 更改项目流程状态
     *
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
     *
     * @param filecode
     * @return
     */
    @Override
    public Sign findByFilecode(String filecode, String signState) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from  " + Sign.class.getSimpleName());
        hqlBuilder.append(" where " + Sign_.filecode.getName() + " = :filecode ");
        hqlBuilder.setParam("filecode", filecode);
        if (Validate.isString(signState)) {
            hqlBuilder.append(" and " + Sign_.signState.getName() + " !=:signState ");
            hqlBuilder.setParam("signState", Constant.EnumState.DELETE.getValue());
        }
        List<Sign> signList = findByHql(hqlBuilder);
        if (Validate.isList(signList)) {
            return signList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据项目ID获取关联的项目的ID
     *
     * @param signId
     * @return
     */
    @Override
    public boolean checkIsLink(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(*) from cs_associate_sign where signid = :signid or associate_signid=:associate_signid");
        sqlBuilder.setParam("signid", signId).setParam("associate_signid", signId);
        return returnIntBySql(sqlBuilder) > 0 ? true : false;

    }

    /**
     * 根据合并评审主项目ID，查找合并评审项目
     *
     * @param signId
     * @return
     */
    @Override
    public List<Sign> findReviewSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signid.getName() + " in ");
        hqlBuilder.append(" ( select m." + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName() + " m where " +
                "m." + SignMerge_.signId.getName() + " =:signId and m." + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        List<Sign> signList = findByHql(hqlBuilder);
        return signList;
    }

    /**
     * 根据合并评审次项目ID，查找合并评审主项目
     *
     * @param signId
     * @return
     */
    @Override
    public List<Sign> findMainReviewSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signid.getName() + " = ");
        hqlBuilder.append(" ( select m." + SignMerge_.signId.getName() + " from " + SignMerge.class.getSimpleName() + " m where " +
                "m." + SignMerge_.mergeId.getName() + " =:signId and m." + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        List<Sign> signList = findByHql(hqlBuilder);
        return signList;
    }

    /**
     * 根据合并评审主项目ID，判断合并评审次项目是否完成工作方案环节提交
     *
     * @param signid
     * @return
     */
    @Override
    public boolean isMergeSignEndWP(String signid) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(s.signid) from cs_sign s where s.signid in ");
        sqlBuilder.append(" ( select m.mergeid from cs_sign_merge m where m.signid =:signid )");
        sqlBuilder.setParam("signid", signid);
        sqlBuilder.append(" and s.processState < :processState ");
        sqlBuilder.setParam("processState", Constant.SignProcessState.END_WP.getValue(), IntegerType.INSTANCE);

        return returnIntBySql(sqlBuilder) > 0 ? false : true;
    }

    /**
     * 获取未发送给委里的项目信息
     *
     * @return
     */
    @Override
    public List<Sign> findUnSendFGWList() {
        Criteria criteria = getExecutableCriteria();
        //正式签收
        criteria.add(Restrictions.eq(Sign_.issign.getName(), Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.isNotNull(Sign_.filecode.getName()));
        //未发送给发改委的项目,或者不用回传的数据
        criteria.add(Restrictions.sqlRestriction("((" + Sign_.isSendFGW.getName() + " != '9' and " + Sign_.isSendFGW.getName() + " != '2') or " + Sign_.isSendFGW.getName() + " is null )"));
        //criteria.add(Restrictions.or(Restrictions.isNull(Sign_.isSendFGW.getName()), Restrictions.ne(Sign_.isSendFGW.getName(), Constant.EnumState.YES.getValue())));
        //排除旧项目
        criteria.add(Restrictions.isNull(Sign_.oldProjectId.getName()));
        criteria.add(Restrictions.isNotNull(Sign_.processInstanceId.getName()));
        //正在进行或者正常结束
        criteria.add(Restrictions.or(Restrictions.eq(Sign_.signState.getName(), Constant.EnumState.PROCESS.getValue()),
                Restrictions.eq(Sign_.signState.getName(), Constant.EnumState.YES.getValue())));
        //已经生成发文编号
        criteria.add(Restrictions.ge(Sign_.processState.getName(), Constant.SignProcessState.END_DIS_NUM.getValue()));

        List<Sign> signList = criteria.list();
        //过滤掉手工签收的项目，即收文编号为0000结尾的项目  //
        List<Sign> resultList = signList.stream().filter(s -> (!s.getFilecode().endsWith("0000"))).collect(Collectors.toList());
        return resultList;
    }

    /**
     * 统计项目平均天数，未办结的按当前日期算，已办结的按办结日期算
     *
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
        sqlBuilder.bulidPropotyString("where", "CS.SIGNID", signIds);
        sqlBuilder.append(" ) sf ");
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 通过收文id查询 评审天数、剩余工作日、收文日期、送来日期、评审总天数等
     *
     * @param signId
     * @return
     */
    @Override
    public SignDto findReviewDayBySignId(String signId) {
        SignDto signDto = new SignDto();
        signDto.setSignid(signId);
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select reviewdays  , surplusdays , signdate  , receivedate ,lengthenDays , lengthenExp , totalReviewdays  from cs_sign  where " + Sign_.signid.getName() + "=:signId");
        hqlBuilder.setParam("signId", signId);
        List<Object[]> signList = this.getObjectArray(hqlBuilder);
        if (signList != null && signList.size() > 0) {
            Object[] objects = signList.get(0);
//            signDto.setReviewdays(objects[0] == null ? 0 : Float.valueOf(objects[0].toString()));
            if (signDto.getTotalReviewdays() != null && signDto.getSurplusdays() != null) {

                signDto.setReviewdays((signDto.getTotalReviewdays() < signDto.getSurplusdays() || objects[0] == null) ? 0 : Float.valueOf(objects[0].toString()));
            } else {
                signDto.setReviewdays(Float.valueOf(0));
            }
            signDto.setSurplusdays(objects[1] == null ? 0 : Float.valueOf(objects[1].toString()));
            if(objects[2] != null){
                signDto.setSigndate(DateUtils.converToDate(objects[2].toString(), "yyyy-MM-dd"));
            }

            //由于目前 送来日期为空，所以需要判断是否为空
            if (objects[3] != null) {
                signDto.setReceivedate(DateUtils.converToDate(objects[3].toString(), "yyyy-MM-dd"));
            }
//            signDto.setGoneDays(signDto.getTotalReviewdays() < signDto.getSurplusdays() ? 0 : signDto.getTotalReviewdays() - signDto.getSurplusdays()); //已逝工作日
            signDto.setOverDays(signDto.getSurplusdays() >= 0 ? 0 : Math.abs(signDto.getSurplusdays())); //延长工作日，如果剩余工作日大于等于0 ，则为0 否则取剩余工作日绝对值
            signDto.setLengthenDays(objects[4] == null ? 0 : Float.valueOf(objects[4].toString()));
            signDto.setLengthenExp(objects[5] == null ? "" : objects[5].toString());


            signDto.setTotalReviewdays(objects[6] == null ? 0 : Float.valueOf(objects[6].toString()));
        }

        return signDto;
    }

    @Override
    public SignDto findSignByFileCode(SignDto signDto) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select signId  , surplusdays , signdate  , receivedate ,lengthenDays , lengthenExp , totalReviewdays  from cs_sign  where " + Sign_.filecode.getName() + "=:filecode");
        hqlBuilder.setParam("filecode", signDto.getFilecode());
        List<Object[]> signList = this.getObjectArray(hqlBuilder);
        if (signList != null && signList.size() > 0) {
            Object[] objects = signList.get(0);
            if (Validate.isObject(objects[0])){
                signDto.setSignid((String) objects[0]);
            }
        }
        return signDto;
    }

    /**
     * 保存评审工作日维护的信息
     *
     * @param signDto
     * @return
     */
    @Override
    public ResultMsg saveReview(SignDto signDto) {
        Sign sign = this.findById(Sign_.signid.getName(), signDto.getSignid());
        BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
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

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！");
    }

    /**
     * 更新拟拟补充资料函状态
     *
     * @param businessId
     * @param value
     * @param disapDate
     */
    @Override
    public void updateSuppLetterState(String businessId, String value, Date disapDate) {
        try {
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" update cs_sign ");
            sqlBuilder.append(" set " + Sign_.isHaveSuppLetter.getName() + " =:stateValue ").setParam("stateValue", value);
            sqlBuilder.append(" ," + Sign_.suppLetterDate.getName() + " = to_date(:disapDate,'yyyy-mm-dd') ").setParam("disapDate", DateUtils.converToString(disapDate, "yyyy-MM-dd"));
            sqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ").setParam("signid", businessId);
            sqlBuilder.append(" and " + Sign_.isHaveSuppLetter.getName() + " is null or " + Sign_.isHaveSuppLetter.getName() + " != '9'");
            executeSql(sqlBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     * 重新初始化协办部门和协办负责人信息
     *
     * @param sign
     * @param signId
     * @param branchId 排除的分支
     */
    @Override
    public void initAOrgAndUser(Sign sign, String signId, String branchId) {
        Criteria criteria = signPrincipalRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(SignPrincipal_.signId.getName(), signId));
        criteria.add(Restrictions.ne(SignPrincipal_.flowBranch.getName(), branchId));
        criteria.addOrder(Order.asc(SignPrincipal_.sort.getName()));
        List<SignPrincipal> allSignPrincipal = criteria.list();
        if (Validate.isList(allSignPrincipal)) {
            String aUserId = "", aUserName = "";
            for (SignPrincipal signPrincipal : allSignPrincipal) {
                User user = userRepo.getCacheUserById(signPrincipal.getUserId());
                //主负责人
                if (Constant.EnumState.YES.getValue().equals(signPrincipal.getIsMainUser())) {
                    sign.setmUserId(user.getId());
                    sign.setmUserName(user.getDisplayName());
                    //协办负责人
                } else {
                    if (Validate.isString(aUserId)) {
                        aUserId += ",";
                        aUserName += ",";
                    }
                    aUserId += user.getId();
                    aUserName += user.getDisplayName();
                }
            }
            sign.setaUserID(aUserId);
            sign.setaUserName(aUserName);
        } else {
            sign.setmUserId("");
            sign.setmUserName("");
            sign.setaUserID("");
            sign.setaUserName("");
        }
    }

    /**
     * 校验是否是调概项目
     *
     * @param businessKey
     * @return
     */
    @Override
    public boolean checkAssistSign(String businessKey) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(signid) from cs_sign where signid =:signid and isassistflow =:isassistflow ");
        sqlBuilder.setParam("signid", businessKey);
        sqlBuilder.setParam("isassistflow", Constant.EnumState.YES.getValue());
        int resultInt = returnIntBySql(sqlBuilder);
        return resultInt > 0 ? true : false;
    }

    /**
     * 获取最大的收文编号
     *
     * @param yearName
     * @param signType
     * @return
     */
    @Override
    public int getMaxSignSeq(String yearName, String signType) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + Sign_.signSeq.getName() + ") from cs_sign where to_char(" + Sign_.signdate.getName() + ", 'yyyy') = :yearName ");
        sqlBuilder.setParam("yearName", yearName);
        sqlBuilder.append(" and " + Sign_.dealOrgType.getName() + " =:signType ").setParam("signType", signType);
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 通过signId查询平均评审天数和工作日
     *
     * @param signIds
     * @return
     */
    @Override
    public ResultMsg findAVGDayId(String signIds) {
        double totalReviewDays = 0, reviewDays = 0, stopDay = 0;
        double[] reslut = new double[]{0, 0, 0};
        if (Validate.isString(signIds)) {
            String[] ids = signIds.split(",");
            if (ids != null && ids.length > 0) {
                for (String id : ids) {
                    Sign sign = this.findById(Sign_.signid.getName(), id);
                    if (sign != null) {
                        if (sign.getSigndate() != null && sign.getDispatchdate() != null) {
                            totalReviewDays += DateUtils.daysBetween(sign.getSigndate(), sign.getDispatchdate());
                        } else if (sign.getSigndate() != null && sign.getDispatchdate() == null) {
                            totalReviewDays += DateUtils.daysBetween(sign.getSigndate(), new Date());
                        }
                        reviewDays += sign.getReviewdays();
                        if (sign.getProjectStopList() != null && sign.getProjectStopList().size() > 0) {
                            for (ProjectStop ps : sign.getProjectStopList()) {
                                if (ps != null && ps.getPausedays() != null) {
                                    stopDay += ps.getPausedays();
                                }
                            }
                        }
                    }

                }
                reslut[0] = totalReviewDays;
                reslut[1] = totalReviewDays / ids.length;
                reslut[2] = (reviewDays + stopDay) / ids.length;
            }
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", reslut);
        } else {

            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败！", null);
        }
    }

    /**
     * 保存项目维护中的添加评审部门
     *
     * @param signId
     * @param orgIds
     * @return
     */
    @Override
    public ResultMsg addAOrg(String signId, String orgIds) {

        Sign sign = this.findById(Sign_.signid.getName(), signId);
        if (sign != null && Validate.isString(orgIds)) {
            String aOrgIds = sign.getaOrgId();
            String aorgName = sign.getaOrgName();
            boolean flag = false;

            //1、主、协均为空  2、主不为空且不等，协为空  3、主不为空且不等，协不为空且不等
            boolean b = (!Validate.isString(sign.getmOrgId()) && !Validate.isString(sign.getaOrgId()))
                    || (Validate.isString(sign.getmOrgId()) && sign.getmOrgId().indexOf(orgIds) < 0 && !Validate.isString(sign.getaOrgId()))
                    || (Validate.isString(sign.getmOrgId()) && sign.getmOrgId().indexOf(orgIds) < 0 && Validate.isString(sign.getaOrgId()) && sign.getaOrgId().indexOf(orgIds) < 0);

            OrgDept orgDept = orgDeptRepo.findOrgDeptById(orgIds);

            if ( b ) {
                flag = true;

                if(Validate.isString(sign.getaOrgId())){
                    aOrgIds += "," + orgDept.getId();
                    aorgName += "," + orgDept.getName();
                }else{
                    aOrgIds = orgDept.getId();
                    aorgName = orgDept.getName();
                }
            }

            sign.setaOrgId(aOrgIds);
            sign.setaOrgName(aorgName);
            this.save(sign);

            if (flag) {

                return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", null);
            } else {

                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该部门已经存在，不能重复添加！", null);
            }
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败", null);
        }

    }

    /**
     * 移除在维护项目中添加的评审部门
     *
     * @param signId
     * @param orgIds
     * @return
     */
    @Override
    public ResultMsg deleteAOrg(String signId, String orgIds) {

        //先获取项目分支，判断是否存在该部门，不存在则说明是在项目维护中添加的部门，则是可以删除的，分支表存在的部门是不可以删除的
        SignBranch signBranch = signBranchRepo.findByOrgDirector(signId, orgIds);
        if (signBranch == null) {
            OrgDept orgDept = orgDeptRepo.findOrgDeptById(orgIds);
            Sign sign = this.findById(Sign_.signid.getName(), signId);
            if (sign != null && orgDept != null ) {
                String aOrgName = sign.getaOrgName();
                String aOrgId = sign.getaOrgId();
                if (Validate.isString(aOrgName) && Validate.isString(aOrgId) ) {

                    if (aOrgId.indexOf(orgDept.getId()) > -1
                            || aOrgName.indexOf(orgDept.getName()) > -1) {

                        //去掉前后的，
                        sign.setaOrgName(removeStr(aOrgName, orgDept.getName()));

                        sign.setaOrgId(removeStr(aOrgId, orgDept.getId()));

                        this.save(sign);
                        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "移除成功！", null);
                    }
                }

            }
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "移除失败！", null);
        } else {

            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该部门不可移除！", null);
        }

    }

    /**
     * 保存项目维护中的添加负责人
     * @param signId
     * @param userId
     * @return
     */
    @Override
    public ResultMsg addSecondUser(String signId, String userId) {
        Sign sign = this.findById(Sign_.signid.getName() , signId);
        User user = userRepo.findById(User_.id.getName() , userId);
        if(sign != null  && user != null){

            boolean flag = false;
            String mUserName = sign.getmUserName();  //第一负责人
            String aUserName = sign.getaUserName();  //第二负责人

            //1、第一、二均为空 2、第一不为空且不等，第二为空  3、第一不为空且不等，第二不为空且不等
            boolean b = (!Validate.isString(mUserName) && !Validate.isString(aUserName))
                    || (Validate.isString(mUserName) && mUserName.indexOf(user.getDisplayName()) < 0  && !Validate.isString(aUserName))
                    || (Validate.isString(mUserName) && mUserName.indexOf(user.getDisplayName()) < 0 && Validate.isString(aUserName) && aUserName.indexOf(user.getDisplayName()) < 0);

            if(b){
                flag = true;
                if(Validate.isString(aUserName)){
                    sign.setaUserID(sign.getaUserID() + "," + user.getId());
                    sign.setaUserName(aUserName + "," + user.getDisplayName());
                }else{
                    sign.setaUserID(user.getId());
                    sign.setaUserName(user.getDisplayName());
                }

                //添加负责人时，如果部门不存在，则评审部门也需添加对应的部门
                //1、主、协均为空  2、主不为空且不等，协为空  3、主不为空且不等，协不为空且不等
                boolean b2 = (!Validate.isString(sign.getmOrgId()) && !Validate.isString(sign.getaOrgId()))
                        || (Validate.isString(sign.getmOrgId()) && sign.getmOrgId().indexOf(user.getOrg().getId()) < 0 && !Validate.isString(sign.getaOrgId()))
                        || (Validate.isString(sign.getmOrgId()) && sign.getmOrgId().indexOf(user.getOrg().getId()) < 0 && Validate.isString(sign.getaOrgId()) && sign.getaOrgId().indexOf(user.getOrg().getId()) < 0);

                OrgDept orgDept = orgDeptRepo.findOrgDeptById(user.getOrg().getId());
                String aOrgIds = sign.getaOrgId();
                String aorgName = sign.getaOrgName();

                if ( b2 ) {
                    if(Validate.isString(sign.getaOrgId())){
                        aOrgIds += "," + orgDept.getId();
                        aorgName += "," + orgDept.getName();
                    }else{
                        aOrgIds = orgDept.getId();
                        aorgName = orgDept.getName();
                    }
                }
                sign.setaOrgId(aOrgIds);
                sign.setaOrgName(aorgName);
            }

            if(flag){
                return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "操作成功！" , null);
            }else{
                return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "该用户已经存在，添加失败！！" , null);
            }

        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "操作失败！" , null);
        }
    }

    /**
     * 删除项目维护中添加的负责人
     * @param signId
     * @param userId
     * @return
     */
    @Override
    public ResultMsg deleteSecondUser(String signId , String userId) {
        Sign sign = this.findById(Sign_.signid.getName() , signId);
        User user = userRepo.findById(User_.id.getName() , userId);
        if(sign != null && user != null
                && Validate.isString(sign.getaUserID()) && sign.getaUserID().indexOf(user.getId()) > -1){
            List<User> userList =  signPrincipalRepo.getPrinUserList(signId , user.getOrg().getId());
            if(userList != null && userList.size() > 0){
                for(User u : userList){
                    if(user.getId().equals(u.getId())){
                        return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "该用户不能删除！" , null);
                    }
                }
                String aUserId = removeStr(sign.getaUserID() , user.getId());
                String aUserName = removeStr(sign.getaUserName() , user.getDisplayName());
                sign.setaUserID(aUserId);
                sign.setaUserName(aUserName);
                return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "移除成功！" , null);

            }else{
                String aUserId = removeStr(sign.getaUserID() , user.getId());
                String aUserName = removeStr(sign.getaUserName() , user.getDisplayName());
                sign.setaUserName(aUserName);
                sign.setaUserID(aUserId);

                return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "移除成功！" , null);
            }
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "没有该负责人！"  , null);
        }

    }

    /**
     * 保存是否能多选专家
     * @param signId
     * @param isMoreExpert
     * @return
     */
    @Override
    public ResultMsg saveMoreExpert(String signId, String isMoreExpert) {
        if(Validate.isString(signId) && Validate.isString(isMoreExpert)){
            Sign sign = this.findById(Sign_.signid.getName() , signId);
            if(sign != null){
                sign.setIsMoreExpert(isMoreExpert);
                return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "操作成功！" , null);
            }else{
                return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "操作失败！" , null);
            }
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "操作失败！" , null);
        }
    }


    /**
     * 移除字符串中的部分元素
     *
     * @return
     */
    private String removeStr(String str, String removeStr) {
        String replaceStr = str;
        if (Validate.isString(str) && Validate.isString(removeStr)) {
            replaceStr = str.replaceAll(removeStr, "").replaceAll(",,", ",");
            int first = replaceStr.indexOf(","), last = replaceStr.lastIndexOf(",");

            if (last == replaceStr.length() - 1 && last != -1) {
                replaceStr = replaceStr.substring(0, replaceStr.length() - 1);
            }
            if (first == 0) {
                replaceStr = replaceStr.substring(1, replaceStr.length());
            }
        }
        return replaceStr;
    }
}