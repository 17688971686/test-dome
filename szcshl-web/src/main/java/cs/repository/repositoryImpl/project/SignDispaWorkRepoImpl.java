package cs.repository.repositoryImpl.project;

import cs.ahelper.projhelper.ProjUtil;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.ProjectConstant;
import cs.common.constants.SysConstants;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.domain.project.Sign_;
import cs.domain.sys.SysDept;
import cs.model.project.Achievement;
import cs.repository.AbstractRepository;
import cs.service.flow.FlowService;
import cs.sql.ProjSql;
import cs.xss.XssShieldUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import static cs.common.constants.Constant.SUPER_ROLE;

/**
 * Description: 项目统计视图 数据操作实现类
 * author: ldm
 * Date: 2017-7-10 9:35:22
 */
@Repository
public class SignDispaWorkRepoImpl extends AbstractRepository<SignDispaWork, String> implements SignDispaWorkRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FlowService flowService;

    @Autowired
    private SignRepo signRepo;

    /**
     * 通过时间段 获取项目信息（按评审阶段分组），用于项目查询统计分析
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public ResultMsg findByTime(String startTime, String endTime) {
        Date start = DateUtils.converToDate(startTime, DateUtils.DATE_PATTERN);
        Date end = DateUtils.converToDate(endTime, DateUtils.DATE_PATTERN);
        List<Map<String, Object[]>> resultList = new ArrayList<>();
        if (DateUtils.daysBetween(start, end) > 0) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" select reviewstage , sum(appalyinvestment) appalyinvestment , sum(authorizeValue) authorizeValue , ");
            hqlBuilder.append(" count(projectcode) projectCount from sign_disp_work ");
            hqlBuilder.append(" where " + SignDispaWork_.signdate.getName() + " >=:start and " + SignDispaWork_.signdate.getName() + " <=:end ");
            hqlBuilder.setParam("start", start).setParam("end", end);
            //已发文
            hqlBuilder.append(" and " + SignDispaWork_.processState.getName() + " >=:processState ");
            hqlBuilder.setParam("processState", Constant.SignProcessState.END_DIS_NUM.getValue());
            //排除预签收项目
            hqlBuilder.append(" and (ispresign <>:ispresign or ispresign is null) ");
            hqlBuilder.setParam("ispresign", Constant.EnumState.YES.getValue());
            hqlBuilder.append(" group by " + SignDispaWork_.reviewstage.getName());
            hqlBuilder.append(" order by " + SignDispaWork_.reviewstage.getName() + " desc");

            List<Object[]> objctList = this.getObjectArray(hqlBuilder);
            Map<String, Object[]> map = new HashMap<>();
            Object[] initData = new Object[]{0, 0};
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEREPORT.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEHOMELAND.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEIMPORT.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEDEVICE.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.REGISTERCODE.getZhCode(), initData);
            map.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEOTHER.getZhCode(), initData);
            if (objctList != null && objctList.size() > 0) {
                for (int i = 0, l = objctList.size(); i < l; i++) {
                    Object[] obj = objctList.get(i);
                    double appalyinvestment = ((BigDecimal) obj[1]) == null ? 0 : ((BigDecimal) obj[1]).divide(new BigDecimal(10000)).doubleValue();
                    double authorizeValue = ((BigDecimal) obj[2]) == null ? 0 : ((BigDecimal) obj[2]).divide(new BigDecimal(10000)).doubleValue();
                    int num = obj[3] == null ? 0 : new BigDecimal(obj[3].toString()).intValue();
                    double ratio = 0; //审定/申报比例
                    double extraRate = 0;//核减（增）率
                    if (appalyinvestment != 0) {
                        ratio = Integer.parseInt(String.format("%.00f", authorizeValue / appalyinvestment * 100));
                    }

                    if (authorizeValue != 0) {
                        extraRate = Integer.parseInt(String.format("%.00f", (appalyinvestment - authorizeValue) / appalyinvestment * 100));
                    }

                    //[申报金额，审定金额，数目 , 审定/申报 ， 核减（增率）]
                    Object[] value = {appalyinvestment, authorizeValue, num, ratio, extraRate};
                    String reviewStateName = obj[0].toString();
                    ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByZhCode(reviewStateName);
                    if (Validate.isObject(reviewStateEnum)) {
                        map.put(reviewStateName, value);
                    }
                }
                resultList.add(map);
            }
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultList);
        } else {
            return ResultMsg.error("结束日期必须大于开始日期！");
        }
    }

    /**
     * 通过评审阶段，项目类别，统计项目信息
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public ResultMsg findByTypeAndReview(String startTime, String endTime) {
        List<Map<String, Object[]>> resultList = new ArrayList<>();
        //map("评审阶段" , [市政工程，房建工程，信息工程，设备采购，其他])
        //建议书
        Object[] sugs = new Object[]{0, 0, 0, 0, 0};
        //登记赋码
        Object[] regcode = new Object[]{0, 0, 0, 0, 0};
        //可行性研究报告
        Object[] studys = new Object[]{0, 0, 0, 0, 0};
        //项目概算
        Object[] budgets = new Object[]{0, 0, 0, 0, 0};
        //资金申请报告
        Object[] reports = new Object[]{0, 0, 0, 0, 0};
        //设备清单（国产）
        Object[] homelands = new Object[]{0, 0, 0, 0, 0};
        //设备清单（进口）
        Object[] imports = new Object[]{0, 0, 0, 0, 0};
        //进口设备
        Object[] devices = new Object[]{0, 0, 0, 0, 0};
        //其它
        Object[] others = new Object[]{0, 0, 0, 0, 0};

        Date start = DateUtils.converToDate(startTime, DateUtils.DATE_PATTERN);
        Date end = DateUtils.converToDate(endTime, DateUtils.DATE_PATTERN);
        if (DateUtils.daysBetween(start, end) > 0) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append("select " + SignDispaWork_.reviewstage.getName() + "," + SignDispaWork_.projectType.getName() + ",count(" + SignDispaWork_.projectcode.getName() + ") projectNum from sign_disp_work ");
            hqlBuilder.append(" where " + SignDispaWork_.signdate.getName() + " >=:start and " + SignDispaWork_.signdate.getName() + "<=:end");
            hqlBuilder.append(" and " + SignDispaWork_.processState.getName() + ">=:processState");
            //排除预签项目
            hqlBuilder.append(" and ( ispresign <>:ispresign  or ispresign is null)");

            hqlBuilder.append(" group by " + SignDispaWork_.projectType.getName() + " ," + SignDispaWork_.reviewstage.getName());
            hqlBuilder.append(" having " + SignDispaWork_.projectType.getName() + " is not null ");
            hqlBuilder.append(" order by " + SignDispaWork_.reviewstage.getName() + " desc , " + SignDispaWork_.projectType.getName());
            hqlBuilder.setParam("start", start);
            hqlBuilder.setParam("end", end);
            //已发文
            hqlBuilder.setParam("processState", Constant.SignProcessState.END_DIS_NUM.getValue());
            hqlBuilder.setParam("ispresign", Constant.EnumState.YES.getValue());

            List<Object[]> objctList = this.getObjectArray(hqlBuilder);
            if (objctList != null && objctList.size() > 0) {
                for (int i = 0, l = objctList.size(); i < l; i++) {
                    Object[] objects = objctList.get(i);
                    String reviewStageName =  objects[0].toString(),projectType = (objects[1]).toString(),projectCount = (objects[2]).toString();
                    ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByZhCode(reviewStageName);
                    if(Validate.isObject(reviewStateEnum)){
                        switch (reviewStateEnum){
                            /**
                             * 项目建议书
                             */
                            case STAGESUG:
                                ProjUtil.countStageNum(sugs, projectType,projectCount);
                                break;
                            /**
                             * 可行性研究报告
                             */
                            case STAGESTUDY:
                                ProjUtil.countStageNum(studys, projectType,projectCount);
                                break;
                            /**
                             * 项目概算
                             */
                            case STAGEBUDGET:
                                ProjUtil.countStageNum(budgets, projectType,projectCount);
                                break;
                            /**
                             * 登记赋码
                             */
                            case REGISTERCODE:
                                ProjUtil.countStageNum(regcode, projectType,projectCount);
                                break;
                            /**
                             * 资金申请报告
                             */
                            case STAGEREPORT:
                                ProjUtil.countStageNum(reports, projectType,projectCount);
                                break;
                            /**
                             * 进口设备
                             */
                            case STAGEDEVICE:
                                ProjUtil.countStageNum(devices, projectType,projectCount);
                                break;
                            /**
                             * 设备清单国产
                             */
                            case STAGEHOMELAND:
                                ProjUtil.countStageNum(homelands, projectType,projectCount);
                                break;
                            /**
                             * 设备清单进口
                             */
                            case STAGEIMPORT:
                                ProjUtil.countStageNum(imports, projectType,projectCount);
                                break;
                            /**
                             * 其它
                             */
                            case STAGEOTHER:
                                ProjUtil.countStageNum(others, projectType,projectCount);
                                break;
                            default:
                                    ;
                        }
                    }
                }
            }
            Map<String, Object[]> sugMap = new HashMap<>();
            sugMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode(), sugs);
            Map<String, Object[]> studyMap = new HashMap<>();
            studyMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode(), studys);
            Map<String, Object[]> budgetMap = new HashMap<>();
            budgetMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode(), budgets);
            Map<String, Object[]> reportMap = new HashMap<>();
            reportMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEREPORT.getZhCode(), reports);
            Map<String, Object[]> homeLandMap = new HashMap<>();
            homeLandMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEHOMELAND.getZhCode(), homelands);
            Map<String, Object[]> importMap = new HashMap<>();
            importMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEIMPORT.getZhCode(), imports);
            Map<String, Object[]> deviceMap = new HashMap<>();
            deviceMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEDEVICE.getZhCode(), devices);
            Map<String, Object[]> otherMap = new HashMap<>();
            otherMap.put(ProjectConstant.REVIEW_STATE_ENUM.STAGEOTHER.getZhCode(), others);
            //登记赋码，新增的阶段
            Map<String, Object[]> regCodeMap = new HashMap<>();
            regCodeMap.put(ProjectConstant.REVIEW_STATE_ENUM.REGISTERCODE.getZhCode(), regcode);
            resultList.add(budgetMap);
            resultList.add(sugMap);
            resultList.add(deviceMap);
            resultList.add(reportMap);
            resultList.add(importMap);
            resultList.add(homeLandMap);
            resultList.add(studyMap);
            resultList.add(otherMap);
            resultList.add(regCodeMap);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultList);
        } else {
            return ResultMsg.error("结束日期必须大于开始日期");
        }
    }

    /**
     * 通过收文id获取项目信息
     *
     * @param signId
     * @return
     */
    @Override
    public SignDispaWork findSDPBySignId(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignDispaWork_.signid.getName(), signId));
        List<SignDispaWork> signDispaWorkList = criteria.list();
        if (signDispaWorkList != null && signDispaWorkList.size() > 0) {
            return signDispaWorkList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 通过条件查询统计
     *
     * @param queryData
     * @param page
     * @return
     */
    @Override
    public List<SignDispaWork> queryStatistics(String queryData, int page) {
        String[] queryArr = null;
        if (Validate.isString(queryData)) {
            //反转数据，要不然转义之后，数据无法解析
            queryData = XssShieldUtil.getInstance().unStripXss(queryData);
            queryData = queryData.replaceAll("\\\\", "");
            queryArr = queryData.split(SysConstants.SEPARATE_COMMA);
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select * from (select a.* , rownum rn from (");
        hqlBuilder.append("select * from SIGN_DISP_WORK where signstate != '7' and issign = 9 ");
        if (queryArr != null && queryArr.length > 0 && !"".equals(queryArr[0])) {
            for (int i = 0; i < queryArr.length; i++) {
                String filter = queryArr[i];
                String[] params = filter.split(":");
                //对中文乱码进行处理
                String paramName = params[0].substring(1, params[0].length() - 1), value = params[1];
                if (!Validate.isString(paramName) || "fztype".equals(paramName) || "displayName".equals(paramName) || !Validate.isString(value)) {
                    continue;
                }
                if (value.indexOf("\"") > -1) {
                    value = params[1].substring(1, params[1].length() - 1);
                    try {
                        if (value.equals(new String(value.getBytes("iso8859-1"), "iso8859-1"))) {
                            value = new String((params[1].substring(1, params[1].length() - 1)).getBytes("iso8859-1"), "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                hqlBuilder.append(" and ");
                switch (paramName) {
                    //签收日期
                    case "signDateBegin":
                        hqlBuilder.append(" signdate>=to_date(:beginTime1, 'yyyy-mm-dd hh24:mi:ss') ");
                        hqlBuilder.setParam("beginTime1", value + " 00:00:00");
                        break;
                    case "signDateEnd":
                        hqlBuilder.append(" signdate<=to_date(:endTime1, 'yyyy-mm-dd hh24:mi:ss') ");
                        hqlBuilder.setParam("endTime1", value + " 23:59:59");
                        break;
                    //发文日期
                    case "dispatchDateBegin":
                        hqlBuilder.append(" dispatchDate>=to_date(:beginTime2, 'yyyy-mm-dd hh24:mi:ss') ");
                        hqlBuilder.setParam("beginTime2", value + " 00:00:00");
                        break;
                    case "dispatchdateEnd":
                        hqlBuilder.append(" dispatchDate<=to_date(:endTime2, 'yyyy-mm-dd hh24:mi:ss') ");
                        hqlBuilder.setParam("endTime2", value + " 23:59:59");
                        break;
                    //归档日期
                    case "fileDateBegin":
                        hqlBuilder.append(" fileDate>=to_date(:beginTime3, 'yyyy-mm-dd hh24:mi:ss') ");
                        hqlBuilder.setParam("beginTime3", value + " 00:00:00");
                        break;
                    case "fileDateEnd":
                        hqlBuilder.append(" fileDate<=to_date(:endTime3, 'yyyy-mm-dd hh24:mi:ss') ");
                        hqlBuilder.setParam("endTime3", value + " 23:59:59");
                        break;
                    //评审天数
                    case "beginReviewdays":
                        hqlBuilder.append(" reviewdays>=:reviewdays1 ");
                        hqlBuilder.setParam("reviewdays1", value);
                        break;
                    case "endReviewdays":
                        hqlBuilder.append(" reviewdays<=:reviewdays2 ");
                        hqlBuilder.setParam("reviewdays2", value);
                        break;
                    //提前介入
                    case "isAdvanced":
                        if ("提前介入".equals(value)) {
                            hqlBuilder.append("isAdvanced = '" + Constant.EnumState.YES.getValue() + "'");
                        } else if ("正常".equals(value)) {
                            hqlBuilder.append("isAdvanced != '" + Constant.EnumState.YES.getValue() + "'");
                        }
                        break;
                    //申报投资
                    case "appalyInvestmentMin":
                        hqlBuilder.append(" appalyInvestment>=:appalyInvestment1 ");
                        hqlBuilder.setParam("appalyInvestment1", new BigDecimal(value));
                        break;
                    case "appalyInvestmentMax":
                        hqlBuilder.append(" appalyInvestment<=:appalyInvestment2 ");
                        hqlBuilder.setParam("appalyInvestment2", new BigDecimal(value));
                        break;
                    case "processState":
                        int processState = Integer.parseInt(value);
                        switch (processState) {
                            case 1:
                            case 2:
                                //在办项目或者暂停项目
                                hqlBuilder.append(" signState = :signState1 ").setParam("signState1", processState);
                                hqlBuilder.append(" and processState < :processState1 ").setParam("processState1", Constant.SignProcessState.END_WP.getValue());
                                break;
                            case 17:
                                //未发送存档
                                hqlBuilder.append(" processState >= :processState2 ").setParam("processState2", Constant.SignProcessState.IS_START.getValue());
                                hqlBuilder.append(" and processState <= :processState3 ").setParam("processState3", Constant.SignProcessState.SEND_CW.getValue());
                                break;
                            case 68:
                                //已发文未存档
                                hqlBuilder.append(" ( processState = :processState4 or processState =:processState5 or processState =:processState6 ) ");
                                hqlBuilder.setParam("processState4", Constant.SignProcessState.END_DIS_NUM.getValue());
                                hqlBuilder.setParam("processState5", Constant.SignProcessState.SEND_CW.getValue());
                                hqlBuilder.setParam("processState6", Constant.SignProcessState.SEND_FILE.getValue());
                                break;
                            case 69:
                                //已发文项目
                                hqlBuilder.append(" processState >=:processState7 ").setParam("processState7", Constant.SignProcessState.END_DIS_NUM.getValue());
                                break;
                            case 89:
                                //已发送存档
                                hqlBuilder.append(" processState >=:processState8 ").setParam("processState8", Constant.SignProcessState.SEND_FILE.getValue());
                                break;
                            case 24:
                                //曾经暂停
                                hqlBuilder.append(" isProjectStop =:isProjectStop1 ").setParam("isProjectStop1", Constant.EnumState.YES.getValue());
                                hqlBuilder.append(" signState =:signState2 ").setParam("signState2", Constant.EnumState.PROCESS.getValue());
                                break;
                            case 8:
                                //已发送存档
                                hqlBuilder.append(" processState =:processState9 ").setParam("processState9", Constant.SignProcessState.SEND_FILE.getValue());
                                break;
                            case 9:
                                hqlBuilder.append(" processState =:processState10 ").setParam("processState10", Constant.SignProcessState.FINISH.getValue());
                                break;
                            default:
                                ;
                        }
                        break;
                    case "dispatchType":
                        if ("非暂不实施项目".equals(value)) {
                            //非暂不实施项目=项目发文+退文
                            hqlBuilder.append(" (dispatchType ='项目发文' or dispatchType ='项目退文') ");
                        } else if ("非退文项目".equals(value)) {
                            //非退文项目=暂不实施+项目发文
                            hqlBuilder.append(" (dispatchType ='项目发文' or dispatchType ='暂不实施') ");
                        } else {
                            hqlBuilder.append(" dispatchType ='" + value + "' ");
                        }
                        break;
                    default:
                        hqlBuilder.append(paramName + " like :paramValue" + i + " ").setParam("paramValue" + i, "%" + value + "%");
                }
            }
        }
        hqlBuilder.append(" ) a ) where rn >" + (page * 100) + " and rn <" + ((page + 1) * 100 + 1));
        return findBySql(hqlBuilder);
    }


    /**
     * 通过业务id，判断当前用户是否有权限查看项目详情----用于秘密项目
     *
     * @param signId
     * @return
     */
    @Override
    public ResultMsg findSecretProPermission(String signId) {
        /**
         * 如果是主任和超级管理员，则不用做判断
         * 归档员也可以查看所有项目（2018-04-28）
         */
        boolean isHaveAuth = SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.FILER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.SUPER_LEADER.getValue())
                || SessionUtil.hashRole(SUPER_ROLE);
        if (isHaveAuth) {
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), null);
        }
        boolean isHavePermission = false;
        SignDispaWork signDispaWork = findById(signId);
        String curUserId = SessionUtil.getUserId();
        Sign sign = signRepo.findById(Sign_.signid.getName(), signId);
        //1、先判断是否是新项目
        if (Validate.isString(signDispaWork.getOldProjectId())) {
            //部长或者普通人员
            isHavePermission = (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue()) && SessionUtil.getUserId().equals(signDispaWork.getmOrgId()));
            if (!isHavePermission) {
                isHavePermission = curUserId.equals(signDispaWork.getmUserId()) || (Validate.isString(signDispaWork.getaUserID()) && signDispaWork.getaUserID().indexOf(curUserId) > -1)
                        || (Validate.isString(sign.getaUserID()) && sign.getaUserID().indexOf(curUserId) > -1);
            }
        } else {
            //新项目，是经办人才行
            if (Validate.isString(signDispaWork.getProcessInstanceId())) {
                List<String> userIdList = flowService.findUserIdByProcessInstanceId(signDispaWork.getProcessInstanceId());
                if (userIdList.contains(curUserId) || (Validate.isString(sign.getaUserID()) && sign.getaUserID().indexOf(curUserId) > -1)) {
                    isHavePermission = true;
                }
            }
        }
        if (isHavePermission) {
            return ResultMsg.ok(null);
        } else {
            return ResultMsg.error("您无权限查看此项目信息！");
        }
    }

    /**
     * 在办项目数量统计
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> dataskCount() {
        HqlBuilder hqlBuilder = ProjSql.countPorj();
        return signRepo.findByJdbc(hqlBuilder);
    }

    /**
     * 在办项目处理情况统计
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> dtasksLineSign() {
        HqlBuilder hqlBuilder = ProjSql.countDealPorj();
        return signRepo.findByJdbc(hqlBuilder);
    }

    /**
     * 业绩统计查询
     *
     * @param year
     * @param quarter
     * @param deptIds
     * @param userId
     * @param level
     * @return
     */
    @Override
    public List<Achievement> countAchievement(String year, String quarter, String deptIds, String userId, int level, List<SysDept> deptList) {
        String[] queryTimes = ProjUtil.getQueryTime(year, quarter);
        String beginTime = queryTimes[0], endTime = queryTimes[1];
        HqlBuilder hqlBuilder = ProjSql.countAchievement(deptIds, userId, level, beginTime, endTime, deptList);
        List<Achievement> achievementList = jdbcTemplate.query(hqlBuilder.getHqlString(), hqlBuilder.getJdbcValue(), new BeanPropertyRowMapper<Achievement>(Achievement.class));
        return achievementList;
    }

}