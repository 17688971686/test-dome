package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.domain.expert.*;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.model.PageModelDto;
import cs.model.expert.ProReviewConditionDto;
import cs.repository.AbstractRepository;
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
    public PageModelDto<SignDispaWork> reviewProject(String expertId) {
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

        List<SignDispaWork> signDispaWorkList =this.findByHql(hqlBuilder);
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        pageModelDto.setValue(signDispaWorkList);
        pageModelDto.setCount(signDispaWorkList.size());
        return pageModelDto;
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
            hqlBuilder.append(" group by " + SignDispaWork_.reviewstage.getName());
            hqlBuilder.append(" order by " +  SignDispaWork_.reviewstage.getName() + " desc");
            hqlBuilder.setParam("start" , start);
            hqlBuilder.setParam("end" , end);
            List<Object[]> objctList = this.getObjectArray(hqlBuilder);

            if(objctList != null && objctList.size()>0){
                for(int i = 0 ; i<objctList.size() ; i++){
                    Object[] obj = objctList.get(i);
                    Map< String , Object[]> map = new HashMap<>();
                    Object[] value = {((BigDecimal) obj[1] ) == null ? 0 : ((BigDecimal) obj[1]).divide(new BigDecimal(10000)) ,
                            ((BigDecimal) obj[2] ) == null ? 0 : ((BigDecimal) obj[2]).divide(new BigDecimal(10000)) ,
                            obj[3] == null ? 0 : obj[3]}; //[申报金额，审定金额，数目]
                    if((Constant.ProjectStage.STAGE_SUG.getValue()).equals((String)obj[0])){
                        map.put( Constant.ProjectStage.STAGE_SUG.getValue() , value);
                    }else if((Constant.ProjectStage.STAGE_STUDY.getValue()).equals((String)obj[0])){
                        map.put(Constant.ProjectStage.STAGE_STUDY.getValue() , value);
                    }else if((Constant.ProjectStage.STAGE_BUDGET.getValue()).equals((String)obj[0])){
                        map.put(Constant.ProjectStage.STAGE_BUDGET.getValue() , value);
                    }else if((Constant.ProjectStage.APPLY_REPORT.getValue()).equals((String)obj[0])){
                        map.put(Constant.ProjectStage.APPLY_REPORT.getValue() , value);
                    }else if((Constant.ProjectStage.DEVICE_BILL_HOMELAND.getValue()).equals((String)obj[0])){
                        map.put(Constant.ProjectStage.DEVICE_BILL_HOMELAND.getValue() , value);
                    }else if((Constant.ProjectStage.DEVICE_BILL_IMPORT.getValue()).equals((String)obj[0])){
                        map.put(Constant.ProjectStage.DEVICE_BILL_IMPORT.getValue() , value);
                    }else if((Constant.ProjectStage.IMPORT_DEVICE.getValue()).equals((String)obj[0])){
                        map.put(Constant.ProjectStage.IMPORT_DEVICE.getValue() , value);
                    }else if((Constant.ProjectStage.OTHERS.getValue()).equals((String)obj[0])){
                        map.put(Constant.ProjectStage.OTHERS.getValue() , value);
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
            hqlBuilder.append(" group by " + SignDispaWork_.projectType.getName() + " ," + SignDispaWork_.reviewstage.getName() );
            hqlBuilder.append(" having " + SignDispaWork_.projectType.getName() + " is not null ") ;
            hqlBuilder.append(" order by " + SignDispaWork_.reviewstage.getName() +" desc , " + SignDispaWork_.projectType.getName() );
            hqlBuilder.setParam("start" , start);
            hqlBuilder.setParam("end" , end);
            List<Object[]> objctList = this.getObjectArray(hqlBuilder);
            if(objctList != null && objctList.size()>0){
                for(int i=0 ; i<objctList.size() ; i++){
                    Object[] objects =objctList.get(i);
                    if((Constant.ProjectStage.STAGE_SUG.getValue()).equals((String)objects[0])){
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
                    }else if((Constant.ProjectStage.STAGE_STUDY.getValue()).equals((String)objects[0])){
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
                    }else if((Constant.ProjectStage.STAGE_BUDGET.getValue()).equals((String)objects[0])){
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
                    }else if((Constant.ProjectStage.APPLY_REPORT.getValue()).equals((String)objects[0])){
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
                    }else if((Constant.ProjectStage.DEVICE_BILL_HOMELAND.getValue()).equals((String)objects[0])){
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
                    }else if((Constant.ProjectStage.DEVICE_BILL_IMPORT.getValue()).equals((String)objects[0])){
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
                    }else if((Constant.ProjectStage.IMPORT_DEVICE.getValue()).equals((String)objects[0])){
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
                    }else if((Constant.ProjectStage.OTHERS.getValue()).equals((String)objects[0])){
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
            sugMap.put(Constant.ProjectStage.STAGE_SUG.getValue() , sugs);
            Map<String , Object[]> studyMap = new HashMap<>();
            studyMap.put(Constant.ProjectStage.STAGE_STUDY.getValue() , studys);
            Map<String , Object[]> budgetMap = new HashMap<>();
            budgetMap.put(Constant.ProjectStage.STAGE_BUDGET.getValue() , budgets);
            Map<String , Object[]> reportMap = new HashMap<>();
            reportMap.put(Constant.ProjectStage.APPLY_REPORT.getValue() , reports);
            Map<String , Object[]> homeLandMap = new HashMap<>();
            homeLandMap.put(Constant.ProjectStage.DEVICE_BILL_HOMELAND.getValue() , homelands);
            Map<String , Object[]> importMap = new HashMap<>();
            importMap.put(Constant.ProjectStage.DEVICE_BILL_IMPORT.getValue() , imports);
            Map<String , Object[]> deviceMap = new HashMap<>();
            deviceMap.put(Constant.ProjectStage.IMPORT_DEVICE.getValue() , devices);
            Map<String ,Object[]> otherMap = new HashMap<>();
            otherMap.put(Constant.ProjectStage.OTHERS.getValue() , others);
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

}