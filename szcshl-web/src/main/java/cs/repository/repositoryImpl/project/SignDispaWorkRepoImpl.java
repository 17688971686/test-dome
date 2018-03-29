package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

import static cs.common.Constant.SUPER_ROLE;

/**
 * Description: 项目统计视图 数据操作实现类
 * author: ldm
 * Date: 2017-7-10 9:35:22
 */
@Repository
public class SignDispaWorkRepoImpl extends AbstractRepository<SignDispaWork, String> implements SignDispaWorkRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 通过时间段 获取项目信息（按评审阶段分组），用于项目查询统计分析
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public ResultMsg findByTime(String startTime, String endTime) {
        Date start = DateUtils.converToDate(startTime , "yyyy-MM-dd");
        Date end = DateUtils.converToDate(endTime , "yyyy-MM-dd");
        List<Map<String , Object[]>> resultList = new ArrayList<>();
        if(DateUtils.daysBetween(start , end) >0){
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" select reviewstage , sum(appalyinvestment) appalyinvestment , sum(authorizeValue) authorizeValue , count(projectcode) projectCount from v_sign_disp_work" );
            hqlBuilder.append(" where " + SignDispaWork_.signdate.getName() + " >=:start and " + SignDispaWork_.signdate.getName() + "<=:end");
            hqlBuilder.append(" and " + SignDispaWork_.processState.getName() + ">=:processState");
            //排除预签收项目
            hqlBuilder.append(" and (ispresign <>:ispresign or ispresign is null)");
            hqlBuilder.append(" group by " + SignDispaWork_.reviewstage.getName());
            hqlBuilder.append(" order by " +  SignDispaWork_.reviewstage.getName() + " desc");
            hqlBuilder.setParam("start" , start);
            hqlBuilder.setParam("end" , end);
            hqlBuilder.setParam("processState" , Constant.SignProcessState.END_DIS_NUM.getValue());//已发文
            hqlBuilder.setParam("ispresign" , Constant.EnumState.YES.getValue());
            List<Object[]> objctList = this.getObjectArray(hqlBuilder);
            Map< String , Object[]> map = new HashMap<>();
            Object[] initData = new Object[]{0 , 0};
            map.put( Constant.STAGE_SUG , initData);
            map.put(Constant.STAGE_STUDY , initData);
            map.put(Constant.STAGE_BUDGET, initData);
            map.put(Constant.APPLY_REPORT, initData);
            map.put(Constant.DEVICE_BILL_HOMELAND, initData);
            map.put(Constant.DEVICE_BILL_IMPORT, initData);
            map.put(Constant.IMPORT_DEVICE, initData);
            map.put(Constant.OTHERS, initData);
            if(objctList != null && objctList.size()>0){
                for(int i = 0 ; i<objctList.size() ; i++){
                    Object[] obj = objctList.get(i);

                    double appalyinvestment = ((BigDecimal) obj[1] ) == null ? 0 : ((BigDecimal) obj[1]).divide(new BigDecimal(10000)).doubleValue();

                    double authorizeValue = ((BigDecimal) obj[2] ) == null ? 0 : ((BigDecimal) obj[2]).divide(new BigDecimal(10000)).doubleValue();

                    int num = obj[3] == null ? 0 : new BigDecimal(obj[3].toString()).intValue();

                    double ratio = 0; //审定/申报比例

                    double extraRate = 0;//核减（增）率

                    if(appalyinvestment != 0){

                        ratio =Integer.valueOf( String.format("%.00f" , authorizeValue/appalyinvestment *100) );

                    }

                    if(authorizeValue != 0 ){
                        extraRate =Integer.valueOf( String.format("%.00f" , (appalyinvestment - authorizeValue)/appalyinvestment *100) );
                    }

                    Object[] value = {appalyinvestment , authorizeValue , num  , ratio , extraRate}; //[申报金额，审定金额，数目 , 审定/申报 ， 核减（增率）]
                    if((Constant.STAGE_SUG).equals((String)obj[0])){
                        map.put( Constant.STAGE_SUG , value);
                    }else if((Constant.STAGE_STUDY).equals((String)obj[0])){
                        map.put(Constant.STAGE_STUDY , value);
                    }else if((Constant.STAGE_BUDGET).equals((String)obj[0])){
                        map.put(Constant.STAGE_BUDGET, value);
                    }else if((Constant.APPLY_REPORT).equals((String)obj[0])){
                        map.put(Constant.APPLY_REPORT, value);
                    }else if((Constant.DEVICE_BILL_HOMELAND).equals((String)obj[0])){
                        map.put(Constant.DEVICE_BILL_HOMELAND, value);
                    }else if((Constant.DEVICE_BILL_IMPORT).equals((String)obj[0])){
                        map.put(Constant.DEVICE_BILL_IMPORT, value);
                    }else if((Constant.IMPORT_DEVICE).equals((String)obj[0])){
                        map.put(Constant.IMPORT_DEVICE, value);
                    }else if((Constant.OTHERS).equals((String)obj[0])){
                        map.put(Constant.OTHERS, value);
                    }

                }
                resultList.add(map);
            }
            return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "查询数据成功" , resultList);
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "结束日期必须大于开始日期！" , null);
        }


    }

    /**
     * 通过评审阶段，项目类别，统计项目信息
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public ResultMsg findByTypeAndReview(String startTime, String endTime) {
        List<Map<String ,Object[]>> resultList = new ArrayList<>();
        //map("评审阶段" , [市政工程，房建工程，信息工程，设备采购，其他])
        Object[] sugs = new Object[]{0,0,0,0,0};//建议书
        Object[] studys = new Object[]{0,0,0,0,0};//可行性研究报告
        Object[] budgets = new Object[]{0,0,0,0,0};//项目概算
        Object[] reports = new Object[]{0,0,0,0,0};//资金申请报告
        Object[] homelands = new Object[]{0,0,0,0,0};//设备清单（国产）
        Object[] imports = new Object[]{0,0,0,0,0};//设备清单（进口）
        Object[] devices = new Object[]{0,0,0,0,0};//进口设备
        Object[] others = new Object[]{0,0,0,0,0};//其它

        Date start = DateUtils.converToDate(startTime , "yyyy-MM-dd");
        Date end = DateUtils.converToDate(endTime , "yyyy-MM-dd");
        if(DateUtils.daysBetween(start , end) >0){
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append("select " + SignDispaWork_.reviewstage.getName() + "," + SignDispaWork_.projectType.getName() + ",count("+ SignDispaWork_.projectcode.getName() + ") projectNum from v_sign_disp_work ");
            hqlBuilder.append(" where " + SignDispaWork_.signdate.getName() + " >=:start and " + SignDispaWork_.signdate.getName() + "<=:end");
            hqlBuilder.append(" and " + SignDispaWork_.processState.getName() + ">=:processState");
            //排除预签项目
            hqlBuilder.append(" and ( ispresign <>:ispresign  or ispresign is null)");

            hqlBuilder.append(" group by " + SignDispaWork_.projectType.getName() + " ," + SignDispaWork_.reviewstage.getName() );
            hqlBuilder.append(" having " + SignDispaWork_.projectType.getName() + " is not null ") ;
            hqlBuilder.append(" order by " + SignDispaWork_.reviewstage.getName() +" desc , " + SignDispaWork_.projectType.getName() );
            hqlBuilder.setParam("start" , start);
            hqlBuilder.setParam("end" , end);
            hqlBuilder.setParam("processState" , Constant.SignProcessState.END_DIS_NUM.getValue()); //已发文
            hqlBuilder.setParam("ispresign" , Constant.EnumState.YES.getValue());

            List<Object[]> objctList = this.getObjectArray(hqlBuilder);
            if(objctList != null && objctList.size()>0){
                for(int i=0 ; i<objctList.size() ; i++){
                    Object[] objects =objctList.get(i);
                    if((Constant.STAGE_SUG).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                sugs[0] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "房建工程":
                                sugs[1] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "信息":
                                sugs[2] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "设备采购":
                                sugs[3] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "其他":
                                sugs[4] = Integer.parseInt((objects[2]).toString());
                                break;
                            default:
                                break;
                        }
                    }else if((Constant.STAGE_STUDY).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                studys[0] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "房建工程":
                                studys[1] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "信息工程":
                                studys[2] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "设备采购":
                                studys[3] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "其他":
                                studys[4] = Integer.parseInt((objects[2]).toString());
                                break;
                            default:
                                break;
                        }
                    }else if((Constant.STAGE_BUDGET).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                budgets[0] = Integer.parseInt((objects[2]).toString());
                                break;
                            case "房建工程":
                                budgets[1] = Integer.parseInt(objects[2].toString());
                                break;
                            case "信息工程":
                                budgets[2] = Integer.parseInt(objects[2].toString());
                                break;
                            case "设备采购":
                                budgets[3] = Integer.parseInt(objects[2].toString());
                                break;
                            case "其他":
                                budgets[4] = Integer.parseInt((String)objects[2]);
                                break;
                            default:
                                break;
                        }
                    }else if((Constant.APPLY_REPORT).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                reports[0] = Integer.parseInt(objects[2].toString());
                                break;
                            case "房建工程":
                                reports[1] = Integer.parseInt(objects[2].toString());
                                break;
                            case "信息工程":
                                reports[2] = Integer.parseInt(objects[2].toString());
                                break;
                            case "设备采购":
                                reports[3] = Integer.parseInt(objects[2].toString());
                                break;
                            case "其他":
                                reports[4] = Integer.parseInt(objects[2].toString());
                                break;
                            default:
                                break;
                        }
                    }else if((Constant.DEVICE_BILL_HOMELAND).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                homelands[0] = Integer.parseInt(objects[2].toString());
                                break;
                            case "房建工程":
                                homelands[1] = Integer.parseInt(objects[2].toString());
                                break;
                            case "信息工程":
                                homelands[2] = Integer.parseInt(objects[2].toString());
                                break;
                            case "设备采购":
                                homelands[3] = Integer.parseInt(objects[2].toString());
                                break;
                            case "其他":
                                homelands[4] = Integer.parseInt(objects[2].toString());
                                break;
                            default:
                                break;
                        }
                    }else if((Constant.DEVICE_BILL_IMPORT).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                imports[0] = Integer.parseInt(objects[2].toString());
                                break;
                            case "房建工程":
                                imports[1] = Integer.parseInt(objects[2].toString());
                                break;
                            case "信息工程":
                                imports[2] = Integer.parseInt(objects[2].toString());
                                break;
                            case "设备采购":
                                imports[3] = Integer.parseInt(objects[2].toString());
                                break;
                            case "其他":
                                imports[4] = Integer.parseInt(objects[2].toString());
                                break;
                            default:
                                break;
                        }
                    }else if((Constant.IMPORT_DEVICE).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                devices[0] = Integer.parseInt(objects[2].toString());
                                break;
                            case "房建工程":
                                devices[1] = Integer.parseInt(objects[2].toString());
                                break;
                            case "信息工程":
                                devices[2] = Integer.parseInt(objects[2].toString());
                                break;
                            case "设备采购":
                                devices[3] = Integer.parseInt(objects[2].toString());
                                break;
                            case "其他":
                                devices[4] = Integer.parseInt(objects[2].toString());
                                break;
                            default:
                                break;
                        }
                    }else if((Constant.OTHERS).equals((String)objects[0])){
                        switch ((String)objects[1]){
                            case "市政工程":
                                others[0] = Integer.parseInt(objects[2].toString());
                                break;
                            case "房建工程":
                                others[1] = Integer.parseInt(objects[2].toString());
                                break;
                            case "信息工程":
                                others[2] = Integer.parseInt(objects[2].toString());
                                break;
                            case "设备采购":
                                others[3] = Integer.parseInt(objects[2].toString());
                                break;
                            case "其他":
                                others[4] = Integer.parseInt(objects[2].toString());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            Map<String ,Object[]> sugMap = new HashMap<>();
            sugMap.put(Constant.STAGE_SUG, sugs);
            Map<String , Object[]> studyMap = new HashMap<>();
            studyMap.put(Constant.STAGE_STUDY, studys);
            Map<String , Object[]> budgetMap = new HashMap<>();
            budgetMap.put(Constant.STAGE_BUDGET, budgets);
            Map<String , Object[]> reportMap = new HashMap<>();
            reportMap.put(Constant.APPLY_REPORT, reports);
            Map<String , Object[]> homeLandMap = new HashMap<>();
            homeLandMap.put(Constant.DEVICE_BILL_HOMELAND, homelands);
            Map<String , Object[]> importMap = new HashMap<>();
            importMap.put(Constant.DEVICE_BILL_IMPORT, imports);
            Map<String , Object[]> deviceMap = new HashMap<>();
            deviceMap.put(Constant.IMPORT_DEVICE, devices);
            Map<String ,Object[]> otherMap = new HashMap<>();
            otherMap.put(Constant.OTHERS, others);
            resultList.add(budgetMap);
            resultList.add(sugMap);
            resultList.add(deviceMap);
            resultList.add(reportMap);
            resultList.add(importMap);
            resultList.add(homeLandMap);
            resultList.add(studyMap);
            resultList.add(otherMap);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultList);
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "结束日期必须大于开始日期" , null) ;
        }

    }


    /**
     * 通过收文id获取项目信息
     * @param signId
     * @return
     */
    @Override
    public SignDispaWork findSDPBySignId(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignDispaWork_.signid.getName() , signId));
        List<SignDispaWork> signDispaWorkList = criteria.list();
        if(signDispaWorkList!=null && signDispaWorkList.size()>0){
            return signDispaWorkList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 通过条件查询统计
     * @param queryData
     * @param page
     * @return
     */
    @Override
    public List<SignDispaWork> queryStatistics(String queryData, int page) {
        String[]  queryArr = null ;
        if(Validate.isString(queryData)){
            queryData = queryData.replaceAll("\\\\" , "");
            queryArr = queryData.split(",");
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        try{
            hqlBuilder.append("select * from (select a.* , rownum rn from (");
            hqlBuilder.append("select * from V_SIGN_DISP_WORK where signstate != '7' " );
            if (queryArr != null && queryArr.length > 0 && !"".equals(queryArr[0])) {
                hqlBuilder.append(" and ");
                for (int i = 0; i < queryArr.length; i++) {
                    String filter = queryArr[i];
                    String[] params = filter.split(":");

                    //对中文乱码进行处理
                    String value = params[1].substring(1, params[1].length() - 1);
                    if(value.equals(new String(value.getBytes("iso8859-1"), "iso8859-1"))){

                        value =  new String((params[1].substring(1, params[1].length() - 1) ).getBytes("iso8859-1"), "UTF-8");
                    }

                    //项目签收日期
                    if("signDateBegin".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("signdate>=to_date('" + params[1].substring(1, params[1].length() - 1) + "', 'yyyy-Mm-dd')");
                    }else if("signDateEnd".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("signdate<=to_date('" + params[1].substring(1, params[1].length() - 1) + "', 'yyyy-Mm-dd')");
                    }
                    //发文日期
                    else if("dispatchDateBegin".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("dispatchDate>=to_date('" + params[1].substring(1, params[1].length() - 1) + "', 'yyyy-Mm-dd')");
                    }else if("dispatchdateEnd".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("dispatchDate<=to_date('" + params[1].substring(1, params[1].length() - 1) + "', 'yyyy-Mm-dd')");

                    }
                    //归档日期
                    else if("fileDateBegin".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("fileDate>=to_date('" + params[1].substring(1, params[1].length() - 1) + "', 'yyyy-Mm-dd')");
                    }else if("fileDateEnd".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("fileDate<=to_date('" + params[1].substring(1, params[1].length() - 1) + "', 'yyyy-Mm-dd')");

                    }
                    //申报投资
                    else if("appalyInvestmentMin".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("appalyInvestment>=" + new BigDecimal(params[1].substring(1, params[1].length() - 1)));
                    }else if("appalyInvestmentMax".equals(params[0].substring(1, params[0].length() - 1))){
                        hqlBuilder.append("appalyInvestment<=" + new BigDecimal(params[1].substring(1, params[1].length() - 1)));

                    }else{

                        hqlBuilder.append(params[0].substring(1, params[0].length() - 1) + " like '%" + value + "%'");
//                        hqlBuilder.setParam(params[0].substring(1, params[0].length() - 1), value);
                    }
                    if (i < queryArr.length - 1) {
                        hqlBuilder.append(" and ");
                    }
                }
            }
            hqlBuilder.append(" ) a ) where rn >" + (page * 100) + " and rn <" + ((page+1)*100+1));
        }catch (Exception e){
            e.printStackTrace();
        }
        List<SignDispaWork> signDispaWorkList = findBySql(hqlBuilder);
        return signDispaWorkList;
    }


    /**
     * 通过业务id，判断当前用户是否有权限查看项目详情----用于秘密项目
     * @param signId
     * @return
     */
    @Override
    public ResultMsg findSecretProPermission(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select " + SignDispaWork_.signid.getName() + " from V_SIGN_DISP_WORK where " + SignDispaWork_.signid.getName() + "=:signId" );
        hqlBuilder.setParam("signId" , signId);

        //部门负责人
        if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
            hqlBuilder.append(" and " + SignDispaWork_.ministerName.getName() + "=:ministerName").setParam("ministerName" , SessionUtil.getDisplayName());
        }
        //分管领导
        else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
            hqlBuilder.append(" and " + SignDispaWork_.leaderName.getName() + "=:leaderName").setParam("leaderName" , SessionUtil.getDisplayName());
        }
        //主任或者超级管理员
        else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue()) || SessionUtil.hashRole(SUPER_ROLE)){

        }else{
            //项目负责人
            hqlBuilder.append(" and " + SignDispaWork_.allPriUser.getName() + " like '%" + SessionUtil.getDisplayName() + "%'");
        }

        List<Object[]> objList =  getObjectArray(hqlBuilder);

        if(objList!=null && objList.size()>0){
            return new ResultMsg(true , Constant.MsgCode.OK.getValue() , null);

        }else{

            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "您无权限查看此项目信息！");
        }
    }


    @Override
    //在办项目数量统计
    public List<Map<String,Object>> dataskCount(){
        //for mysql
        String statisticsSql = " select COUNT(signid)  as SIGNNUMBER ,reviewstage   from V_SIGN_DISP_WORK t where signstate<>7 and signstate<>2 group by t.reviewstage";
        List<Map<String,Object>> statList=jdbcTemplate.queryForList(statisticsSql);

        return statList;
    }

    @Override
    //在办项目处理情况统计(折线图)
    public  List<Object[]> dtasksLineSign(String curUserId, List<String> orgIdList,int leaderFlag){
        HqlBuilder sqlBuilder = HqlBuilder.create();




        sqlBuilder.append("SELECT s.projectname AS projectname,s.reviewstage AS reviewstage, s.surplusdays AS surplusdays   FROM cs_sign s");
        sqlBuilder.append("  LEFT JOIN cs_work_program w  ON s.signid = w.signid AND W.BRANCHID = '1'");
       //主办部门
        sqlBuilder.append("  LEFT JOIN (SELECT o.id oid, o.name oname, B.SIGNID bsignid  FROM V_ORG_DEPT o, CS_SIGN_BRANCH b  ");
        sqlBuilder.append("  WHERE O.ID = B.ORGID AND B.ISMAINBRABCH = '9') mo ON mo.bsignid = s.signid");
       //协办部门
        sqlBuilder.append("  LEFT JOIN ( SELECT wm_concat (o.id) secondOrgid, wm_concat (o.name) secondOrgName, ab.SIGNID absignid  FROM V_ORG_DEPT o, CS_SIGN_BRANCH ab");
        sqlBuilder.append("  WHERE O.ID = ab.ORGID AND ab.ISMAINBRABCH != '9' AND ab.ISNEEDWP = '9'  GROUP BY ab.signid");
        sqlBuilder.append("    ) ao on ao.absignid = s.signid");
        //第一负责人

        sqlBuilder.append(" LEFT JOIN  (SELECT U.ID mUserId, u.displayname mUserName, csp1.signid csignid  FROM CS_USER u, CS_SIGN_PRINCIPAL2 csp1");
        sqlBuilder.append("  WHERE csp1.userid = u.id AND csp1.ismainuser = '9' ");
        if(leaderFlag==0){
            //普通用户
        sqlBuilder.append(" AND u.id='"+curUserId+"' ");
        }
        sqlBuilder.append(" ) csp ON csp.csignid = s.signid ");
         //第二负责人
        sqlBuilder.append("  LEFT JOIN  (SELECT wm_concat (u2.id) secondUserId, wm_concat (u2.displayname) secondUserName, csp2.signid c2signid ");
        sqlBuilder.append("  FROM CS_USER u2, CS_SIGN_PRINCIPAL2 csp2  WHERE csp2.userid = u2.id AND csp2.ismainuser = '0'   ");
        if(leaderFlag==0){
            sqlBuilder.append(" AND u2.id='"+curUserId+"'");
        }
        sqlBuilder.append(" GROUP BY csp2.signid) csp22 ");
        sqlBuilder.append("  ON csp22.c2signid = s.signid   ORDER BY S.CREATEDDATE DESC ");

        List<Object[]> objectList = getObjectArray(sqlBuilder);
        return objectList;
    }

}