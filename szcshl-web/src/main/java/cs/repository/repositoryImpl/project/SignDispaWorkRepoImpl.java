package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.*;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.model.PageModelDto;
import cs.model.expert.ProReviewConditionDto;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 项目统计视图 数据操作实现类
 * author: ldm
 * Date: 2017-7-10 9:35:22
 */
@Repository
public class SignDispaWorkRepoImpl extends AbstractRepository<SignDispaWork, String> implements SignDispaWorkRepo {
    @Override
    public List<SignDispaWork> reviewProject(String expertId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where " + SignDispaWork_.signid.getName() + " in (");
        hqlBuilder.append(" select " + ExpertReview_.businessId.getName() + " from " + ExpertReview.class.getSimpleName() + " where " + ExpertReview_.id.getName() + " in (");
        hqlBuilder.append(" select " + ExpertSelected_.expertReview.getName() + "." + ExpertReview_.id.getName() + " from " + ExpertSelected.class.getSimpleName() + " where ");
        hqlBuilder.append(" " + ExpertSelected_.isConfrim.getName() + "=:isConfrim");
        hqlBuilder.append(" and " + ExpertSelected_.isJoin.getName() + "=:isJoin");
        hqlBuilder.append(" and " + ExpertSelected_.expert.getName() + "." + Expert_.expertID.getName() + "=:expertId))");
        hqlBuilder.setParam("isConfrim" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("isJoin" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("expertId" , expertId);

        List<SignDispaWork> signDispaWorkList = this.findByHql(hqlBuilder);
      /*  PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        pageModelDto.setValue(signDispaWorkList);
        pageModelDto.setCount(signDispaWorkList.size());*/
        return signDispaWorkList;
    }

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
            hqlBuilder.append(" where " + SignDispaWork_.signdate.getName() + " >:start and " + SignDispaWork_.signdate.getName() + "<:end");
            hqlBuilder.append(" and " + SignDispaWork_.processState.getName() + ">=:processState");
            hqlBuilder.append(" group by " + SignDispaWork_.reviewstage.getName());
            hqlBuilder.append(" order by " +  SignDispaWork_.reviewstage.getName() + " desc");
            hqlBuilder.setParam("start" , start);
            hqlBuilder.setParam("end" , end);
            hqlBuilder.setParam("processState" , Constant.SignProcessState.END_DIS_NUM.getValue());//已发文
            List<Object[]> objctList = this.getObjectArray(hqlBuilder);

            if(objctList != null && objctList.size()>0){
                for(int i = 0 ; i<objctList.size() ; i++){
                    Object[] obj = objctList.get(i);
                    Map< String , Object[]> map = new HashMap<>();
                    Object[] value = {((BigDecimal) obj[1] ) == null ? 0 : ((BigDecimal) obj[1]).divide(new BigDecimal(10000)) ,
                            ((BigDecimal) obj[2] ) == null ? 0 : ((BigDecimal) obj[2]).divide(new BigDecimal(10000)) ,
                            obj[3] == null ? 0 : obj[3]}; //[申报金额，审定金额，数目]
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
                    resultList.add(map);
                }
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
            hqlBuilder.append(" where " + SignDispaWork_.signdate.getName() + " >:start and " + SignDispaWork_.signdate.getName() + "<:end");
            hqlBuilder.append(" and " + SignDispaWork_.processState.getName() + ">=:processState");
            hqlBuilder.append(" group by " + SignDispaWork_.projectType.getName() + " ," + SignDispaWork_.reviewstage.getName() );
            hqlBuilder.append(" having " + SignDispaWork_.projectType.getName() + " is not null ") ;
            hqlBuilder.append(" order by " + SignDispaWork_.reviewstage.getName() +" desc , " + SignDispaWork_.projectType.getName() );
            hqlBuilder.setParam("start" , start);
            hqlBuilder.setParam("end" , end);
            hqlBuilder.setParam("processState" , Constant.SignProcessState.END_DIS_NUM.getValue()); //已发文
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
            resultList.add(sugMap);
            resultList.add(studyMap);
            resultList.add(budgetMap);
            resultList.add(reportMap);
            resultList.add(homeLandMap);
            resultList.add(importMap);
            resultList.add(deviceMap);
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

            queryArr = queryData.split(",");
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        try{
        hqlBuilder.append("select * from (select a.* , rownum rn from (");
        hqlBuilder.append("select * from V_SIGN_DISP_WORK " );
        if (queryArr != null && queryArr.length > 0 && !"".equals(queryArr[0])) {
            hqlBuilder.append(" where ");
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

                    hqlBuilder.append(params[0].substring(1, params[0].length() - 1) + "=:" + params[0].substring(1, params[0].length() - 1));
                    hqlBuilder.setParam(params[0].substring(1, params[0].length() - 1), value);
                }
                if (i < queryArr.length - 1) {
                    hqlBuilder.append(" and ");
                }
            }
        }
        hqlBuilder.append(" ) a ) where rn >" + (page * 50) + " and rn <" + ((page+1)*50+1));
        }catch (Exception e){
            e.printStackTrace();
        }
        List<SignDispaWork> signDispaWorkList = findBySql(hqlBuilder);
        return signDispaWorkList;
    }

}