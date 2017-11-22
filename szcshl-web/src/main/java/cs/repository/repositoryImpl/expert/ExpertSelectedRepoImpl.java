package cs.repository.repositoryImpl.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.domain.expert.Expert_;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.repository.AbstractRepository;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 抽取专家 数据操作实现类
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Repository
public class ExpertSelectedRepoImpl extends AbstractRepository<ExpertSelected, String> implements ExpertSelectedRepo {
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private  ExpertRepo expertRepo;
    /**
     * 根据大类，小类和专家类别，综合评分确认已经抽取的专家
     * @param maJorBig
     * @param maJorSmall
     * @param expeRttype
     * @param compositeScore
     * @return
     */
    @Override
    public int findConfirmSeletedEP(String reviewId, String maJorBig, String maJorSmall, String expeRttype, Integer compositeScore,Integer compositeScortEnd) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select count(ID) from cs_expert_selected where expertreviewid =:reviewId ");
        sqlBuilder.setParam("reviewId", reviewId);
        sqlBuilder.append(" and " + ExpertSelected_.isConfrim.getName() + " =:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and " + ExpertSelected_.maJorBig.getName() + " =:maJorBig ");
        sqlBuilder.setParam("maJorBig", maJorBig);
        sqlBuilder.append(" and " + ExpertSelected_.maJorSmall.getName() + " =:maJorSmall ");
        sqlBuilder.setParam("maJorSmall", maJorSmall);
        sqlBuilder.append(" and " + ExpertSelected_.expeRttype.getName() + " =:expeRttype ");
        sqlBuilder.setParam("expeRttype", expeRttype);
        if(compositeScore != null && compositeScore > 0){
            sqlBuilder.append(" and " + ExpertSelected_.compositeScore.getName() + "=:compositeScore");
            sqlBuilder.setParam("compositeScore", compositeScore);
        }
        if(compositeScortEnd != null && compositeScortEnd > 0){
            sqlBuilder.append(" and " + ExpertSelected_.compositeScoreEnd.getName() + "=:compositeScortEnd");
            sqlBuilder.setParam("compositeScortEnd", compositeScortEnd);
        }
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 专家评审基本情况详细统计
     *
     * @param expertReviewCondDto
     * @return
     */
    @Override
    public ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto) {
        Map<String, Object> resultMap = new HashMap<>();
        PageModelDto<ExpertReviewCondDto> pageModelDto = new PageModelDto<ExpertReviewCondDto>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select t.* from (   ");
        sqlBuilder.append("select e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw  from cs_sign s  ");
        sqlBuilder.append("left join cs_expert_review r  ");
        sqlBuilder.append("on s.signid = r.businessid  ");
        sqlBuilder.append("left join cs_expert_selected a  ");
        sqlBuilder.append("on s.signid = a.businessid  ");
        sqlBuilder.append("left join cs_expert e  on a.expertid = e.expertid) t  ");
        sqlBuilder.append("where t.expertid is not null  ");
        //todo:添加查询条件
        if(null != expertReviewCondDto){
            if(StringUtil.isNotEmpty(expertReviewCondDto.getName())){
                sqlBuilder.append("and t.name like '%"+expertReviewCondDto.getName()+"%'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewCondDto.getReviewtype())){
                sqlBuilder.append("and t.isletterrw = '"+expertReviewCondDto.getReviewtype()+"'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewCondDto.getBeginTime())){
                String beginTime = expertReviewCondDto.getBeginTime()+" 00:00:00";
                sqlBuilder.append("and t.reviewdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
            if(StringUtil.isNotEmpty(expertReviewCondDto.getEndTime())){
                String endTime = expertReviewCondDto.getEndTime()+" 23:59:59";
                sqlBuilder.append("and t.reviewdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
        }
        sqlBuilder.append("order by t.expertno,t.isletterrw ");
        List<Object[]> expertReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<ExpertReviewCondDto> expertReviewConDtoList = new ArrayList<ExpertReviewCondDto>();
        String expertId = "";
        if (expertReviewConList.size() > 0) {
            for (int i = 0; i < expertReviewConList.size(); i++) {
                Object[] expertReviewCon = expertReviewConList.get(i);
                ExpertReviewCondDto expertReviewDto = new ExpertReviewCondDto();
                if(StringUtil.isNotEmpty(expertId) && null != expertReviewCon[0]){
                    if(expertId.equals((String)expertReviewCon[0])){
                        ExpertReviewCondBusDto expertReviewCondBusDto = new ExpertReviewCondBusDto();
                        if (null != expertReviewCon[4]) {
                            expertReviewCondBusDto.setReviewDate((Date) expertReviewCon[4]);
                        }else{
                            expertReviewCondBusDto.setReviewDate(null);
                        }
                        if (null != expertReviewCon[5]) {
                            expertReviewCondBusDto.setProjectName((String) expertReviewCon[5]);
                        }else{
                            expertReviewCondBusDto.setProjectName(null);
                        }
                        if (null != expertReviewCon[6]) {
                            expertReviewCondBusDto.setReviewStage((String) expertReviewCon[6]);
                        }else{
                            expertReviewCondBusDto.setReviewStage(null);
                        }
                        if (null != expertReviewCon[7]) {
                            expertReviewCondBusDto.setSignId((String) expertReviewCon[7]);
                        }else{
                            expertReviewCondBusDto.setSignId(null);
                        }
                        if (null != expertReviewCon[8]) {
                            expertReviewCondBusDto.setIsLetterRw((String) expertReviewCon[8]);
                        }else{
                            expertReviewCondBusDto.setIsLetterRw(null);
                        }
                        List<ExpertReviewCondBusDto> expertReviewCondBusDtoList =expertReviewConDtoList.get(expertReviewConDtoList.size()-1).getExpertReviewCondBusDtoList();
                        expertReviewCondBusDtoList .add(expertReviewCondBusDto);
                        expertReviewConDtoList.get(expertReviewConDtoList.size()-1).setExpertReviewCondBusDtoList(null);
                        expertReviewConDtoList.get(expertReviewConDtoList.size()-1).setExpertReviewCondBusDtoList(expertReviewCondBusDtoList);
                        continue;
                    }
                }
                if (null != expertReviewCon[0]) {
                    expertReviewDto.setExpertID((String) expertReviewCon[0]);
                    expertId = expertReviewDto.getExpertID();
                }else{
                    expertReviewDto.setExpertID(null);
                }
                if (null != expertReviewCon[2]) {
                    expertReviewDto.setName((String) expertReviewCon[2]);
                }else{
                    expertReviewDto.setName(null);
                }

                if (null != expertReviewCon[3]) {
                    expertReviewDto.setComPany((String) expertReviewCon[3]);
                }else{
                    expertReviewDto.setComPany(null);
                }
                List<ExpertReviewCondBusDto> expertReviewCondBusDtoList = new ArrayList<ExpertReviewCondBusDto>();
                ExpertReviewCondBusDto expertReviewCondBusDto = new ExpertReviewCondBusDto();
                if (null != expertReviewCon[4]) {
                    expertReviewCondBusDto.setReviewDate((Date) expertReviewCon[4]);
                }else{
                    expertReviewCondBusDto.setReviewDate(null);
                }
                if (null != expertReviewCon[5]) {
                    expertReviewCondBusDto.setProjectName((String) expertReviewCon[5]);
                }else{
                    expertReviewCondBusDto.setProjectName(null);
                }
                if (null != expertReviewCon[6]) {
                    expertReviewCondBusDto.setReviewStage((String) expertReviewCon[6]);
                }else{
                    expertReviewCondBusDto.setReviewStage(null);
                }
                if (null != expertReviewCon[7]) {
                    expertReviewCondBusDto.setSignId((String) expertReviewCon[7]);
                }else{
                    expertReviewCondBusDto.setSignId(null);
                }
                if (null != expertReviewCon[8]) {
                    expertReviewCondBusDto.setIsLetterRw((String) expertReviewCon[8]);
                }else{
                    expertReviewCondBusDto.setIsLetterRw(null);
                }
                expertReviewCondBusDtoList.add(expertReviewCondBusDto);
                expertReviewDto.setExpertReviewCondBusDtoList(expertReviewCondBusDtoList);
                expertReviewConDtoList.add(expertReviewDto);
            }
        }
        resultMap.put("expertReviewConDtoList", expertReviewConDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 专家评审基本情况综合统计
     * @param expertReviewConSimpleDto
     * @return
     */
    @Override
    public ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        Map<String, Object> resultMap = new HashMap<>();
        PageModelDto<ExpertReviewConSimpleDto> pageModelDto = new PageModelDto<ExpertReviewConSimpleDto>();
        //评审、函次数
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select t.expertid,count(t.expertid)reviewCount,t.isletterrw from (   ");
        sqlBuilder.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_sign s   ");
        sqlBuilder.append("left join cs_expert_review r  ");
        sqlBuilder.append("on s.signid = r.businessid  ");
        sqlBuilder.append("left join cs_expert_selected a  ");
        sqlBuilder.append("on s.signid = a.businessid  ");
        sqlBuilder.append("left join cs_expert e  ");
        sqlBuilder.append("on a.expertid = e.expertid) t  ");
        sqlBuilder.append("where t.expertid is not null   ");
        if(null != expertReviewConSimpleDto){
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getName())){
                sqlBuilder.append("and t.name like '%"+expertReviewConSimpleDto.getName()+"%'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getReviewtype())){
                sqlBuilder.append("and t.isletterrw = '"+expertReviewConSimpleDto.getReviewtype()+"'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())){
                String beginTime = expertReviewConSimpleDto.getBeginTime()+" 00:00:00";
                sqlBuilder.append("and t.reviewdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())){
                String endTime = expertReviewConSimpleDto.getEndTime()+" 23:59:59";
                sqlBuilder.append("and t.reviewdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
        }
        sqlBuilder.append("group by t.expertid,t.expertno,t.isletterrw   ");
        sqlBuilder.append("order by t.expertno  ");

        //评审总次数
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append("select t1.expertid,sum(t1.reviewCount) from (  ");
        sqlBuilder1.append("select t.expertid,t.expertno,count(t.expertid) reviewCount,t.isletterrw  from (   ");
        sqlBuilder1.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_sign s  ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on s.signid = r.businessid  ");
        sqlBuilder1.append("left join cs_expert_selected a  ");
        sqlBuilder1.append("on s.signid = a.businessid  ");
        sqlBuilder1.append("left join cs_expert e  ");
        sqlBuilder1.append(" on a.expertid = e.expertid) t  ");
        sqlBuilder1.append(" where t.expertid is not null  ");
        if(null != expertReviewConSimpleDto){
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getName())){
                sqlBuilder1.append("and t.name like '%"+expertReviewConSimpleDto.getName()+"%'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getReviewtype())){
                sqlBuilder1.append("and t.isletterrw = '"+expertReviewConSimpleDto.getReviewtype()+"'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())){
                String beginTime = expertReviewConSimpleDto.getBeginTime()+" 00:00:00";
                sqlBuilder1.append("and t.reviewdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())){
                String endTime = expertReviewConSimpleDto.getEndTime()+" 23:59:59";
                sqlBuilder1.append("and t.reviewdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
        }
        sqlBuilder1.append("group by t.expertid,t.expertno,t.isletterrw ) t1  ");
        sqlBuilder1.append("group by t1.expertid,t1.expertno  ");
        sqlBuilder1.append("order by t1.expertno  ");

        List<Object[]> expertReviewConSimList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<Object[]> expertReviewConSimList1 = expertSelectedRepo.getObjectArray(sqlBuilder1);
        List<ExpertReviewConSimpleDto> expertRevConSimDtoList = new ArrayList<ExpertReviewConSimpleDto>();
        String expertId = "";
        if (expertReviewConSimList.size() > 0) {
            for(int i=0;i<expertReviewConSimList.size();i++){
                Object[] expertReviewConSim = expertReviewConSimList.get(i);
                ExpertReviewConSimpleDto expertReviewSimDto = new ExpertReviewConSimpleDto();
                if (null != expertReviewConSim[0]) {
                    expertReviewSimDto.setExpertID((String) expertReviewConSim[0]);
                    if (expertId.equals(expertReviewSimDto.getExpertID())){
                        ExpertReviewConSimpleDto expertReviewConSimpleDto1 = expertRevConSimDtoList.get(expertRevConSimDtoList.size()-1);
                        if (null != expertReviewConSim[2]) {
                            String temp = (String)expertReviewConSim[2];
                            if (temp.equals("0")){//评审
                                if (null != expertReviewConSim[1]) {
                                    expertReviewConSimpleDto1.setReviewNum((BigDecimal) expertReviewConSim[1]);
                                }else{
                                    expertReviewConSimpleDto1.setReviewNum(null);
                                }
                            }else{//函评
                                if (null != expertReviewConSim[1]) {
                                    expertReviewConSimpleDto1.setLetterRwNum((BigDecimal) expertReviewConSim[1]);
                                }else{
                                    expertReviewConSimpleDto1.setLetterRwNum(null);
                                }
                            }
                        }else{//评审
                            if (null != expertReviewConSim[1]) {
                                expertReviewConSimpleDto1.setReviewNum((BigDecimal) expertReviewConSim[1]);
                            }else{
                                expertReviewConSimpleDto1.setReviewNum(null);
                            }
                        }
                        expertRevConSimDtoList.remove(expertRevConSimDtoList.size()-1);
                        expertRevConSimDtoList.add(expertReviewConSimpleDto1);
                        continue;
                    }
                    expertId = expertReviewSimDto.getExpertID();
                    Expert expert =  expertRepo.findById(expertReviewSimDto.getExpertID());
                    expertReviewSimDto.setName(expert.getName());
                    expertReviewSimDto.setComPany(expert.getComPany());
                    expertReviewSimDto.setTotalNum(getExpertRevTotalNum(expertReviewConSimList1,expertReviewSimDto.getExpertID()));
                }else{
                    expertReviewSimDto.setExpertID(null);
                }

                if (null != expertReviewConSim[2]) {
                    String temp = (String)expertReviewConSim[2];
                    if (temp.equals("0")){//评审
                        if (null != expertReviewConSim[1]) {
                            expertReviewSimDto.setReviewNum((BigDecimal) expertReviewConSim[1]);
                        }else{
                            expertReviewSimDto.setReviewNum(null);
                        }
                    }else{//函评
                        if (null != expertReviewConSim[1]) {
                            expertReviewSimDto.setLetterRwNum((BigDecimal) expertReviewConSim[1]);
                        }else{
                            expertReviewSimDto.setLetterRwNum(null);
                        }
                    }
                }else{//评审
                    if (null != expertReviewConSim[1]) {
                        expertReviewSimDto.setReviewNum((BigDecimal) expertReviewConSim[1]);
                    }else{
                        expertReviewSimDto.setReviewNum(null);
                    }
                }
                expertRevConSimDtoList.add(expertReviewSimDto);
            }

        }
        resultMap.put("expertRevConSimDtoList", expertRevConSimDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 专家评审情况（不规则）
     * @param expertReviewConSimpleDto
     * @return
     */
    @Override
    public ResultMsg expertReviewConComplicatedCount(ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append("select t1.expertid,t1.name,t1.company,sum(t1.reviewCount) from (  ");
        sqlBuilder1.append("select t.expertid,t.expertno,t.name,t.company,count(t.expertid) reviewCount,t.isletterrw  from (   ");
        sqlBuilder1.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_sign s  ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on s.signid = r.businessid  ");
        sqlBuilder1.append("left join cs_expert_selected a  ");
        sqlBuilder1.append("on s.signid = a.businessid  ");
        sqlBuilder1.append("left join cs_expert e  ");
        sqlBuilder1.append(" on a.expertid = e.expertid) t  ");
        sqlBuilder1.append(" where t.expertid is not null  ");
        if(null != expertReviewConSimpleDto){
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getName())){
                sqlBuilder1.append("and t.name like '%"+expertReviewConSimpleDto.getName()+"%'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())){
                String beginTime = expertReviewConSimpleDto.getBeginTime()+" 00:00:00";
                sqlBuilder1.append("and t.reviewdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())){
                String endTime = expertReviewConSimpleDto.getEndTime()+" 23:59:59";
                sqlBuilder1.append("and t.reviewdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
        }
        sqlBuilder1.append("group by t.expertid,t.expertno,t.name,t.company,t.isletterrw ) t1  ");

        sqlBuilder1.append("group by t1.expertid,t1.expertno,t1.name,t1.company  ");
        sqlBuilder1.append("order by t1.expertno   ");
        List<Object[]> expertRevComplicateList = expertSelectedRepo.getObjectArray(sqlBuilder1);
        List<Object []> expertRevCompliDtoList = getExistsConComplicated(expertReviewConSimpleDto,expertRevComplicateList);
        List<ExpertReviewConSimpleDto> expertRevConCompDtoList = new ArrayList<ExpertReviewConSimpleDto>();
        if(expertRevCompliDtoList.size()>0){
            for(int i=0;i<expertRevCompliDtoList.size();i++){
               Object[] tempArr = expertRevCompliDtoList.get(i);
                ExpertReviewConSimpleDto expertReviewConSimpleDto1 = new ExpertReviewConSimpleDto();
                if(null != tempArr[0]){
                    expertReviewConSimpleDto1.setExpertID((String) tempArr[0]);
                }
                if(null != tempArr[1]){
                    expertReviewConSimpleDto1.setName((String) tempArr[1]);
                }
                if(null != tempArr[2]){
                    expertReviewConSimpleDto1.setComPany((String) tempArr[2]);
                }
                if(null != tempArr[3]){
                    expertReviewConSimpleDto1.setTotalNum((BigDecimal) tempArr[3]);
                }
                expertRevConCompDtoList.add(expertReviewConSimpleDto1);
            }
        }
        resultMap.put("expertRevConCompDtoList", expertRevConCompDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 项目评审情况(按类别)
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ResultMsg proReviewConditionByTypeCount(ProReviewConditionDto projectReviewConditionDto) {

        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select p.projecttype,count(s.projectcode) from cs_sign s   ");
        sqlBuilder.append("left join cs_work_program p  ");
        sqlBuilder.append("on s.signid = p.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.processstate = 6  ");//已发文

        //todo:添加查询条件
        if(null != projectReviewConditionDto){
            if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())){
                String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }else if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())){
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }else if(StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())){
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");;
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String beginTime = projectReviewConditionDto.getEndTime()+"-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("group by p.projecttype ");
        sqlBuilder.append("having p.projecttype is not null  ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();
        Integer totalNum = 0 ;
        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                Object[] projectReviewCon = projectReviewConList.get(i);
                ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
                if (null != projectReviewCon[0]) {
                    proReviewConditionDto.setProjectType((String) projectReviewCon[0]);
                }else{
                    proReviewConditionDto.setProjectType(null);
                }
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setProjectTypeCount((BigDecimal) projectReviewCon[1]);
                    totalNum += proReviewConditionDto.getProjectTypeCount().intValue();
                }

                projectReviewConDtoList.add(proReviewConditionDto);
            }
        }
        resultMap.put("protReviewConditionList", projectReviewConDtoList);
        resultMap.put("totalNum",totalNum);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 项目评审情况汇总
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ProReviewConditionDto proReviewConditionSum(ProReviewConditionDto projectReviewConditionDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select sum(projectcount),sum(declarevalue),sum(authorizevalue),sum(ljhj),round(sum(ljhj)/sum(declarevalue),4)*100 from (  ");
        sqlBuilder.append("select s.reviewstage, count(s.projectcode) projectcount,sum(d.declarevalue)/10000 declarevalue,sum(d.authorizevalue)/10000 authorizevalue,(sum(d.declarevalue) -sum(d.authorizevalue))/10000 ljhj,round((sum(d.declarevalue) -sum(d.authorizevalue))/(sum(d.declarevalue)*10000),5)*100 hjl  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d   ");
        sqlBuilder.append("on s.signid = d.signid   ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.processstate = 6  ");//已发文
        //todo:添加查询条件
        if(null != projectReviewConditionDto){
            if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())){
                String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }else if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())){
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }else if(StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())){
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");;
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String beginTime = projectReviewConditionDto.getEndTime()+"-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("group by s.reviewstage ) ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();
        ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
        if (projectReviewConList.size() > 0) {
                Object[] projectReviewCon = projectReviewConList.get(0);

                if (null != projectReviewCon[0]) {
                    proReviewConditionDto.setProCount((BigDecimal) projectReviewCon[0]);
                }
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setDeclareValue((BigDecimal) projectReviewCon[1]);
                }else{
                    proReviewConditionDto.setDeclareValue(null);
                }
                if (null != projectReviewCon[2]) {
                    proReviewConditionDto.setAuthorizeValue((BigDecimal) projectReviewCon[2]);
                }else{
                    proReviewConditionDto.setAuthorizeValue(null);
                }
                if (null != projectReviewCon[3]) {
                    proReviewConditionDto.setLjhj((BigDecimal) projectReviewCon[3]);
                }else{
                    proReviewConditionDto.setLjhj(null);
                }
                if (null != projectReviewCon[4]) {
                    proReviewConditionDto.setHjl((BigDecimal) projectReviewCon[4]);
                }else{
                    proReviewConditionDto.setLjhj(null);
                }
               // projectReviewConDtoList.add(proReviewConditionDto);

        }
        return proReviewConditionDto;
    }

    /**
     *完成项目评审次数
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public Integer proReviewMeetingCount(ProReviewConditionDto projectReviewConditionDto){
        Integer proCount = 0;
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select count(s.projectcode) from cs_sign s ");
        sqlBuilder.append("where 1 = 1 ");
        sqlBuilder.append("and s.signstate='9'  ");
        if(null != projectReviewConditionDto){
            if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())){
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        List projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        if (projectReviewConList.size()>0){
            if (null != projectReviewConList.get(0)) {
                proCount = Integer.valueOf(String.valueOf(projectReviewConList.get(0)));
            }
        }
        return proCount;
    }

    /**
     * 项目签收次数
     * @param projectReviewConditionDto
     * @return
     */
    @Override
   public  Integer proReviewCount(ProReviewConditionDto projectReviewConditionDto){
       Integer proCount = 0;
       Map<String, Object> resultMap = new HashMap<>();
       HqlBuilder sqlBuilder = HqlBuilder.create();
       sqlBuilder.append("select count(s.projectcode) from cs_sign s  ");
       sqlBuilder.append("left join cs_dispatch_doc d ");
       sqlBuilder.append("on s.signid = d.signid ");
       sqlBuilder.append("where 1 = 1 ");
       sqlBuilder.append("and s.signstate = '1'  ");
       sqlBuilder.append("and s.processstate = 6  ");//已发文
       if(null != projectReviewConditionDto){
           if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())){
               String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
               String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
               String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
               String endTime = projectReviewConditionDto.getBeginTime()+"-"+day+" 23:59:59";
               sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
               sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
           }
       }
       List projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
       if (projectReviewConList.size()>0){
           if (null != projectReviewConList.get(0)) {
               proCount =  Integer.valueOf(String.valueOf(projectReviewConList.get(0)));
           }
       }
        return  proCount;
   }

    /**
     * 项目评审情况明细
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public List<ProReviewConditionDto> proReviewConditionDetail(ProReviewConditionDto projectReviewConditionDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.signid, s.reviewstage, s.projectname ,s.isadvanced   from cs_sign s   ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9' ");
        sqlBuilder.append("and s.processstate = 6  ");//已发文
        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();
        //todo:添加查询条件
        if(null != projectReviewConditionDto){
            if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())){
                String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }else if(StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())){
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }else if(StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())){
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");;
                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
                String beginTime = projectReviewConditionDto.getEndTime()+"-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("order by s.reviewstage ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                Object[] projectReviewCon = projectReviewConList.get(i);
                ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setReviewStage((String) projectReviewCon[1]);
                }else{
                    proReviewConditionDto.setReviewStage(null);
                }

                if (null != projectReviewCon[2]) {
                    proReviewConditionDto.setProjectName((String) projectReviewCon[2]);
                }else{
                    proReviewConditionDto.setProjectName(null);
                }
                if (null != projectReviewCon[3]) {
                    proReviewConditionDto.setIsadvanced((String) projectReviewCon[3]);
                    if(proReviewConditionDto.getIsadvanced().equals("9")){
                        proReviewConditionDto.setReviewStage(proReviewConditionDto.getReviewStage()+"（提前介入）");
                    }
                }else{
                    proReviewConditionDto.setProjectName(null);
                }

                projectReviewConDtoList.add(proReviewConditionDto);
            }
        }
        return projectReviewConDtoList;
    }

    /**
     * 项目评审情况汇总(按照申报投资金额)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Integer[] proReviewCondByDeclare(String beginTime , String endTime) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
//        String beginTime = "";
//        String endTime = "";
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.processstate = 6  ");//已发文
        //todo:添加查询条件
//        if(null != projectReviewConditionDto){
            if(StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(endTime)){
//                beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
//                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
//                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
//                endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");

            }
//        }
        sqlBuilder.append("and d.declarevalue < 3000  ");
        sqlBuilder.append("union all  ");
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.processstate = 6  ");//已发文
        sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and d.declarevalue >= 3000  and d.declarevalue < 10000   ");
        sqlBuilder.append("union all  ");
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.processstate = 6  ");//已发文
        sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and d.declarevalue >= 10000  and d.declarevalue < 100000   ");
        sqlBuilder.append("union all  ");
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.processstate = 6  ");//已发文
        sqlBuilder.append("and s.signdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.signdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and d.declarevalue >= 100000   ");

        List projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        Integer[] proCountArr = new Integer[projectReviewConList.size()+1];
        ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
        Integer totalNum = 0;
        if (projectReviewConList.size() > 0) {
            for(int i=0;i<projectReviewConList.size();i++){
                if (null != projectReviewConList.get(i)) {
                    proCountArr[i] = Integer.valueOf(String.valueOf(projectReviewConList.get(i)));
                    totalNum +=  proCountArr[i];
                }
            }
            proCountArr[proCountArr.length-1] = totalNum;
        }
        return proCountArr;
    }

    /**
     * 判断是否存在不规则专家评审次数（专家一周参会超过两次或者一个季度超过12次视为不规则）
     * @param expertReviewConSimpleDto
     * @return
     */
    private List<Object[]> getExistsConComplicated(ExpertReviewConSimpleDto expertReviewConSimpleDto,List<Object[]>expertRevComplicateList ){
        boolean flag = true;
        Integer revCount = 0;
        int begseason=0;
        int begweak=0;
        int begday=-1;
        Date begDate = null;
        Date endDate = null;
        String expertId = "";
        List<Object[]> expertCompliList = new ArrayList<Object[]>();
        for(int i=0; i<expertRevComplicateList.size();i++){
            flag = true;
            revCount = 0;
            Object[] tempArr = expertRevComplicateList.get(i);
            expertId = (String)tempArr[0];
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())){
                begDate = DateUtils.converToDate(expertReviewConSimpleDto.getBeginTime(),"yyyy-MM-dd");
                begseason = DateUtils.getSeason(begDate);
                begweak = DateUtils.weekOfYear(begDate);
                begday = DateUtils.getDayOfWeek1(begDate);
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())){
                endDate = DateUtils.converToDate(expertReviewConSimpleDto.getEndTime(),"yyyy-MM-dd");
            }
            //todo: 不规则暂按会议起始日期不为空计算
            while (flag){
                Date weakTemp =  DateUtils.addDay(begDate,7-begday);
                if(weakTemp.compareTo(endDate)<=0){
                    if(DateUtils.getSeason(begDate)== begseason){
                        //当前季度内参会次数大于12
                        if(revCount>12){
                            expertCompliList.add(tempArr);
                            revCount = 0;
                            break;
                        }
                        //按周计算  周参会次数大于2
                       int weakCount = getExpertRevTimes(begDate,weakTemp,expertId);
                        revCount += weakCount;
                        if(weakCount>2){
                            expertCompliList.add(tempArr);
                            revCount = 0;
                            break;
                        }
                        begDate = DateUtils.addDay(weakTemp,1);
                        begweak = DateUtils.weekOfYear(begDate);
                        begday = DateUtils.getDayOfWeek1(begDate);
                    }else {
                        begseason = DateUtils.getSeason(begDate);
                        revCount = 0;
                        //按周计算  周参会次数大于2
                        int weakCount = getExpertRevTimes(begDate,weakTemp,expertId);
                        revCount += weakCount;
                        if(weakCount>2){
                            expertCompliList.add(tempArr);
                            revCount = 0;
                            break;
                        }
                        begDate = DateUtils.addDay(weakTemp,1);
                        begday = DateUtils.getDayOfWeek1(begDate);
                    }
                }else{
                    //begDate 与 endDate 之间专家参会次数是否大于2
                    int weakCount = getExpertRevTimes(begDate,weakTemp,expertId);
                    if(weakCount>2){
                        expertCompliList.add(tempArr);
                        revCount = 0;
                        break;
                    }
                    flag = false;
                }
            }
        }
        return  expertCompliList;
    }


    /**
     * 专家周评审次数
     * @param begDate
     * @param endDate
     * @param expertId
     * @return
     */
    private int getExpertRevTimes(Date begDate,Date endDate,String expertId){
        Integer count = 0;
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append("select t.expertid,count(t.expertid) from (   ");
        sqlBuilder1.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw  from cs_sign s  ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on s.signid = r.businessid  ");
        sqlBuilder1.append("left join cs_expert_selected a  ");
        sqlBuilder1.append("on s.signid = a.businessid  ");
        sqlBuilder1.append("left join cs_expert e  ");
        sqlBuilder1.append("on a.expertid = e.expertid ) t   ");
        sqlBuilder1.append(" where t.expertid = '"+expertId+"'  ");
        if (null != begDate) {
            String beginTime =   DateUtils.converToString(begDate,null)+" 00:00:00";
            sqlBuilder1.append("and t.reviewdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        }
        if (null != endDate) {
            String endTime =   DateUtils.converToString(endDate,null)+" 23:59:59";
            sqlBuilder1.append("and t.reviewdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
        }
        sqlBuilder1.append(" group by t.expertid  ");
        List<Object[]> expertReviewConSimList1 = expertSelectedRepo.getObjectArray(sqlBuilder1);
        if(expertReviewConSimList1.size()>0){
            Object[] tempArr =  expertReviewConSimList1.get(0);
            if(null != tempArr[1]){
                BigDecimal temp = (BigDecimal) tempArr[1];
                count = temp.intValue();
            }
        }
        return  count;
    }

    /**
     * 获取总参会次数
     * @param expertReviewConSimList1
     * @param expertId
     * @return
     */
    private BigDecimal getExpertRevTotalNum(List<Object[]>expertReviewConSimList1,String expertId){
        BigDecimal totalNum = null  ;
        if(expertReviewConSimList1.size()>0){
            for(int i=0;i<expertReviewConSimList1.size();i++){
                Object[] expertReviewConSim = expertReviewConSimList1.get(i);
                if (null != expertReviewConSim[0]) {
                    String temp = (String) expertReviewConSim[0];
                    if (StringUtil.isNotEmpty(expertId) && StringUtil.isNotEmpty(temp) && expertId.equals(temp)){
                        totalNum = (BigDecimal) expertReviewConSim[1];
                        break;
                    }

                }
            }
        }
        return  totalNum;
    }



    /**
     * 根据业务ID统计已经确认的抽取专家
     * @param businessId
     * @return
     */
    @Override
    public int getSelectEPCount(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(id) from cs_expert_selected where "+ExpertSelected_.businessId.getName()+"=:businessId ");
        sqlBuilder.setParam("businessId",businessId);
        sqlBuilder.append(" and "+ExpertSelected_.isConfrim.getName()+"=:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ExpertSelected_.isJoin.getName()+"=:isJoin ");
        sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ExpertSelected_.selectType.getName()+"=:selectType ");
        sqlBuilder.setParam("selectType", Constant.EnumExpertSelectType.AUTO.getValue());
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 专家抽取统计
     * @param expertSelectHis
     * @return
     */
    @Override
    public List<Object[]> getSelectHis(ExpertSelectHis expertSelectHis,boolean isScore) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT EX.EXPERTID,EX.NAME,EX.COMPANY,EX.EXPERTFIELD,WP.PROJECTNAME,ES.MAJORBIG,ES.MAJORSMALL,ES.EXPERTTYPE,");
        sqlBuilder.append(" ES.SELECTTYPE, ES.ISCONFRIM,WP.REVIEWTYPE,ER.REVIEWDATE,WP.MIANCHARGEUSERNAME ,ES.SCORE,ES.DESCRIBES,WP.WORKREVIVESTAGE ");
        sqlBuilder.append(" FROM CS_EXPERT ex ");
        sqlBuilder.append(" LEFT JOIN CS_EXPERT_SELECTED es ON EX.EXPERTID = ES.EXPERTID ");
        sqlBuilder.append(" LEFT JOIN CS_EXPERT_REVIEW er ON ER.ID = ES.EXPERTREVIEWID ");
        sqlBuilder.append(" LEFT JOIN CS_WORK_PROGRAM wp ON WP.ID = ES.BUSINESSID ");
        sqlBuilder.append(" WHERE ex.state != '3' AND ex.state != '4' AND ES.ID IS NOT NULL ");
        //如果是专家评分，则必须是已确定并且参加会议的人
        if(isScore){
            sqlBuilder.append(" AND ES.ISCONFRIM =:isConfirm ").setParam("isConfirm", Constant.EnumState.YES.getValue());
            sqlBuilder.append(" AND ES.ISJOIN =:isJoin ").setParam("isJoin",Constant.EnumState.YES.getValue());

            if(expertSelectHis.getScore() != null){
                sqlBuilder.append(" AND ES.SCORE >:score ").setParam("score",expertSelectHis.getScore()-1, DoubleType.INSTANCE);
            }

            if(Validate.isString(expertSelectHis.getReviewStage())){
                sqlBuilder.append(" AND WP.WORKREVIVESTAGE =:reviewStage ").setParam("reviewStage",expertSelectHis.getReviewStage(), StringType.INSTANCE);
            }
        }
        if(expertSelectHis != null){
            if(Validate.isString(expertSelectHis.getEpName())){
                sqlBuilder.append(" AND EX.NAME like :epName ").setParam("epName","%"+expertSelectHis.getEpName()+"%");
            }
            if(Validate.isString(expertSelectHis.getBeginTime())){
                sqlBuilder.append(" AND ER.REVIEWDATE > to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime",expertSelectHis.getBeginTime().trim() + " 00:00:00");
            }
            if(Validate.isString(expertSelectHis.getEndTime())){
                sqlBuilder.append(" AND ER.REVIEWDATE < to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime",expertSelectHis.getEndTime().trim() + " 23:59:59");
            }
            if(Validate.isString(expertSelectHis.getReviewType())){
                sqlBuilder.append(" AND WP.REVIEWTYPE =:reviewType ").setParam("reviewType",expertSelectHis.getReviewType());
            }
            if(Validate.isString(expertSelectHis.getSelectType())){
                sqlBuilder.append(" AND ES.SELECTTYPE =:selectType ").setParam("selectType",expertSelectHis.getSelectType());
            }

            if(Validate.isString(expertSelectHis.getMajorBig())){
                sqlBuilder.append(" AND ES.MAJORSMALL =:majorBig ").setParam("majorBig",expertSelectHis.getMajorBig());
            }
            if(Validate.isString(expertSelectHis.getMarjorSmall())){
                sqlBuilder.append(" AND ES.MAJORSMALL =:marjorSmall ").setParam("marjorSmall",expertSelectHis.getMarjorSmall());
            }
            if(Validate.isString(expertSelectHis.getExpertType())){
                sqlBuilder.append(" AND ES.EXPERTTYPE =:expertType ").setParam("expertType",expertSelectHis.getExpertType());
            }
            if(Validate.isString(expertSelectHis.getIsConfirm())){
                sqlBuilder.append(" AND ES.ISCONFRIM =:isConfirm ").setParam("isConfirm",expertSelectHis.getIsConfirm());
            }
        }

        sqlBuilder.append("  ORDER BY EX.EXPERTNO ");

        return getObjectArray(sqlBuilder);
    }

}