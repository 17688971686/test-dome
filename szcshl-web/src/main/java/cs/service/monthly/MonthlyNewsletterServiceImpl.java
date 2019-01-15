package cs.service.monthly;

import cs.common.constants.Constant;
import cs.common.constants.Constant.EnumState;
import cs.common.constants.Constant.MsgCode;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.domain.project.AgentTask;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.expert.ProReviewConditionDto;
import cs.model.flow.FlowDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.monthly.MonthlyNewsletterRepo;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.expert.ExpertSelectedService;
import cs.service.project.AgentTaskService;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.sys.UserService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * Description: 月报简报 业务操作实现类
 * author: sjy
 * Date: 2017-9-8 11:23:41
 */
@Service
public class MonthlyNewsletterServiceImpl implements MonthlyNewsletterService {

    @Autowired
    private MonthlyNewsletterRepo monthlyNewsletterRepo;
    @Autowired
    private ExpertSelectedService expertSelectedService;
    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AgentTaskService agentTaskService;
    @Autowired
    private UserService userService;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public PageModelDto<MonthlyNewsletterDto> get(ODataObj odataObj) {
        PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
        Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();

        if(Validate.isList(odataObj.getFilter()) ){
            for(ODataFilterItem oDataFilterItem : odataObj.getFilter()){
                if("monthlyType".equals(oDataFilterItem.getField()) && "1".equals(oDataFilterItem.getValue())){

                    criteria.add(Restrictions.or(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName() , oDataFilterItem.getValue()) , Restrictions.eq(MonthlyNewsletter_.monthlyType.getName() , "5")));
                }
                if("monthlyType".equals(oDataFilterItem.getField()) && "2".equals(oDataFilterItem.getValue())){

                    criteria.add(Restrictions.or(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName() , oDataFilterItem.getValue()) , Restrictions.eq(MonthlyNewsletter_.monthlyType.getName() , "7")));
                }
            }
        }
        criteria.addOrder(Order.desc(MonthlyNewsletter_.createdDate.getName()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }

        List<MonthlyNewsletter> resultList = criteria.list();
        List<MonthlyNewsletterDto> resultDtoList = new ArrayList<>();
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                MonthlyNewsletterDto modelDto = new MonthlyNewsletterDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     * 保存月报简报历史数据
     *
     * @return
     */
    @Override
    @Transactional
    public ResultMsg save(MonthlyNewsletterDto record) {
        MonthlyNewsletter domain = new MonthlyNewsletter();
        Date now = new Date();
        if(Validate.isString(record.getId())){
            domain =   monthlyNewsletterRepo.findById(record.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(record,domain);
        }else{
            BeanCopierUtils.copyProperties(record, domain);
            domain.setId(UUID.randomUUID().toString());
            domain.setCreatedBy(SessionUtil.getUserId());
            domain.setAddTime(now);
            domain.setAuthorizedUser(SessionUtil.getDisplayName());
            domain.setAuthorizedTime(now);
            domain.setMonthlyType(EnumState.NORMAL.getValue());
            domain.setCreatedDate(now);
        }
        domain.setModifiedDate(now);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        monthlyNewsletterRepo.save(domain);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", record);
    }

    @Override
    @Transactional
    public void update(MonthlyNewsletterDto record) {
        MonthlyNewsletter domain = monthlyNewsletterRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(new Date());

        monthlyNewsletterRepo.save(domain);
    }

    @Override
    public MonthlyNewsletterDto findById(String id) {
        MonthlyNewsletterDto modelDto = new MonthlyNewsletterDto();
        if (Validate.isString(id)) {
            MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
        }
        return modelDto;
    }

    /**
     * 删除月报简报历史记录
     */
    @Override
    @Transactional
    public void delete(String id) {
        MonthlyNewsletterDto monthlyDto = new MonthlyNewsletterDto();
        MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
        domain.setMonthlyType(Constant.EnumState.DELETE.getValue());
        BeanCopierUtils.copyProperties(domain, monthlyDto);
        monthlyNewsletterRepo.save(domain);
    }

    @Override
    public void deletes(String[] ids) {
        for (String id : ids) {
            this.delete(id);
        }
    }

    /**
     * 删除月报简报历史列表
     */
    @Override
    public PageModelDto<MonthlyNewsletterDto> deleteHistoryList(ODataObj odataObj) {
        PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
        Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.DELETE.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getTop());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<MonthlyNewsletter> monthlist = criteria.list();
        List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());

        if (monthlist != null && monthlist.size() > 0) {
            monthlist.forEach(x -> {
                MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
                BeanCopierUtils.copyProperties(x, monthDto);
                monthDtos.add(monthDto);
            });
        }
        pageModelDto.setValue(monthDtos);

        return pageModelDto;
    }

    /**
     * 月报简报历史数据列表
     */
    @Override
    public PageModelDto<MonthlyNewsletterDto> mothlyHistoryList(ODataObj odataObj) {
        PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
        Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.NORMAL.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getTop());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<MonthlyNewsletter> monthlist = criteria.list();
        List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());

        if (monthlist != null && monthlist.size() > 0) {
            monthlist.forEach(x -> {
                MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
                BeanCopierUtils.copyProperties(x, monthDto);
                monthDtos.add(monthDto);
            });
        }
        pageModelDto.setValue(monthDtos);

        return pageModelDto;
    }

    /**
     * 修改ldm (2019-01-15)
     * 保存月报简报
     * @return
     */
    @Override
    public ResultMsg saveTheMonthly(MonthlyNewsletterDto record) {
        boolean isUpdate = false;
        MonthlyNewsletter domain = null;
        if (Validate.isString(record.getId())) {
            domain = monthlyNewsletterRepo.findById(record.getId());
            if(Validate.isObject(domain) && Validate.isString(domain.getId())){
                isUpdate = true;
            }
        }
        if(!isUpdate){
            domain = new MonthlyNewsletter(new Date(),Constant.EnumState.PROCESS.getValue(),SessionUtil.getDisplayName());
        }
        //拷贝信息
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        //如果是新增，则要初始化信息
        monthlyNewsletterRepo.save(domain);
        if(!isUpdate) {
            BeanCopierUtils.copyProperties(domain, record);
        }
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", record);
    }

    /**
     * 年度月报简报,不用分页，筛选出每年第一条记录即可
     */
    @Override
    public PageModelDto<MonthlyNewsletterDto> getMonthlyList(ODataObj odataObj) {
        PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("SELECT t.*  FROM (SELECT ROW_NUMBER () OVER (PARTITION BY REPORTMULTIYEAR ORDER BY theMonths) rn " );
        sqlBuilder.append(" ,m.* FROM CS_MONTHLY_NEWSLETTER m WHERE m."+MonthlyNewsletter_.monthlyType.getName()+" =:monthlyType) ");
        sqlBuilder.setParam("monthlyType",EnumState.PROCESS.getValue());
        sqlBuilder.append(" t  WHERE t.rn = 1 and t.reportMultiyear is not null ");
        sqlBuilder.append(" ORDER BY t.reportMultiyear DESC ");
        List<MonthlyNewsletter> monthlist = monthlyNewsletterRepo.findBySql(sqlBuilder);
        List<MonthlyNewsletterDto> monthDtos = new ArrayList<>(monthlist == null ? 0 : monthlist.size());
        //取每年添加第一条即可
        if (Validate.isList(monthlist)) {
            monthlist.forEach(x -> {
                MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
                BeanCopierUtils.copyProperties(x, monthDto);
                monthDtos.add(monthDto);
            });
        }
        pageModelDto.setValue(monthDtos);

        return pageModelDto;
    }

    /**
     * 删除月报简报管理数据列表
     */
    /*@Override
    public PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(ODataObj odataObj) {
        PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
        Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.STOP.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getTop());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<MonthlyNewsletter> monthlist = criteria.list();
        List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());

        if (monthlist != null && monthlist.size() > 0) {
            monthlist.forEach(x -> {
                MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
                BeanCopierUtils.copyProperties(x, monthDto);
                monthDtos.add(monthDto);
            });
        }
        pageModelDto.setValue(monthDtos);

        return pageModelDto;
    }*/


    /**
     * 更改月报简报状态
     */
    @Override
    @Transactional
    public ResultMsg deleteMonthlyData(String id) {
        /*MonthlyNewsletterDto monthlyDto = new MonthlyNewsletterDto();
        MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
        domain.setMonthlyType(Constant.EnumState.STOP.getValue());
        BeanCopierUtils.copyProperties(domain, monthlyDto);
        monthlyNewsletterRepo.save(domain);*/
        return monthlyNewsletterRepo.updateMonthlyType(id,Constant.EnumState.STOP.getValue());
    }



    /**
     * 恢复月报简报状态
     */
    @Override
    @Transactional
    public ResultMsg restoreMonthlyData(String id) {
        return monthlyNewsletterRepo.updateMonthlyType(id, EnumState.PROCESS.getValue());
    }

    @Override
    @Transactional
    public void editTheMonthly(MonthlyNewsletterDto record) {
        MonthlyNewsletter domain = monthlyNewsletterRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(new Date());
        monthlyNewsletterRepo.save(domain);
    }

    /**
     * 生成月报简报模板
     *
     * @param monthlyNewsletterDto
     */
    @Override
    public File createMonthTemplate(MonthlyNewsletterDto monthlyNewsletterDto) {
        //todo:测试初始化数据
/*		monthlyNewsletterDto.setReportMultiyear("2017");
        monthlyNewsletterDto.setTheMonths("09");
		monthlyNewsletterDto.setStartMoultiyear("2017");
		monthlyNewsletterDto.setStaerTheMonths("01");
		monthlyNewsletterDto.setEndTheMonths("09");*/
        File docFile = null;
        ProReviewConditionDto proReviewConditionCur = new ProReviewConditionDto();//汇总当前月
        ProReviewConditionDto proReviewConditionSum = new ProReviewConditionDto();//累计至当前月
        ProReviewConditionDto backDispatchTotalCur = new ProReviewConditionDto();//当前月退文汇总
        Integer reviewCount = 0;//当月专家评审会次数
        Integer signCount = 0;//当月签收项目数
        //当月月报
        if (StringUtil.isNotEmpty(monthlyNewsletterDto.getReportMultiyear()) && StringUtil.isNotEmpty(monthlyNewsletterDto.getTheMonths())) {
            ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
            proReviewConditionDto.setBeginTime(monthlyNewsletterDto.getReportMultiyear() + "-" + monthlyNewsletterDto.getTheMonths());
            ResultMsg resultMsg = expertSelectedService.proReviewConditionCount(proReviewConditionDto);
            Map<String, Object> resultMap = (Map<String, Object>) resultMsg.getReObj();
            List<ProReviewConditionDto> proReviewConditionDtoList = (List<ProReviewConditionDto>) resultMap.get("protReviewConditionList");
            //当前月汇总
            proReviewConditionCur = expertSelectedService.proReviewConditionSum(proReviewConditionDto);
            //获取退文汇总
            backDispatchTotalCur = expertSelectedService.getBackDispatchSum(proReviewConditionDto);
            //后取退文明细
            List<ProReviewConditionDto>backDispatchList = expertSelectedService.getBackDispatchInfo(proReviewConditionDto);
            //获取提前介入项目信息
            ProReviewConditionDto acvanceCurDto =  expertSelectedService.getAdvancedCon(proReviewConditionDto);
            //专家评审明细
            List<ProReviewConditionDto> proReviewCondDetailList = expertSelectedService.proReviewConditionDetail(proReviewConditionDto);
            //获取当前月专家评审会情况
            String[] expertReviewMeeting = expertSelectedService.expertReviewMeeting(proReviewConditionDto);

            //附件一内容
            Map<String , List<ProReviewConditionDto>> attachementContentList = monthlyNewsletterRepo.proReviewConditionCount(proReviewConditionDto);

            //评审阶段排序
            String[] reviewStageArr = new String[]{Constant.REGISTER_CODE , Constant.STAGE_SUG , Constant.STAGE_STUDY
                    , Constant.STAGE_BUDGET , Constant.APPLY_REPORT , Constant.OTHERS , Constant.IMPORT_DEVICE ,
                    Constant.DEVICE_BILL_HOMELAND , Constant.DEVICE_BILL_IMPORT , "提前介入"};
            Map<String, List<ProReviewConditionDto>> proReviewCondDetailMap = new LinkedHashMap<String, List<ProReviewConditionDto>>();
            int index = 1 ;
            for(int i = 0 ; i < reviewStageArr.length ; i ++ ){
                List<ProReviewConditionDto> proReviewConditionDetailList = new ArrayList<ProReviewConditionDto>();
                String key = "";
                boolean flag = false;
                for (int j = 0; j < proReviewCondDetailList.size(); j++) {
                    if (StringUtil.isNotEmpty(proReviewCondDetailList.get(j).getReviewStage()) &&
                            proReviewCondDetailList.get(j).getReviewStage().equals(reviewStageArr[i])) {
                        proReviewConditionDetailList.add(proReviewCondDetailList.get(j));
                        flag =true;
                        key =   NumUtils.NumberToChn(index) + "、" + reviewStageArr[i];
                    }
                }
                if(flag){
                    index++ ;
                }

                proReviewCondDetailMap.put(key, proReviewConditionDetailList);

            }

          /*  Map<String, List<ProReviewConditionDto>> proReviewCondDetailMap = new LinkedHashMap<String, List<ProReviewConditionDto>>();
            for (int i = 0; i < proReviewConditionDtoList.size(); i++) {
                List<ProReviewConditionDto> proReviewConditionDetailList = new ArrayList<ProReviewConditionDto>();
                String key = "";
                for (int j = 0; j < proReviewCondDetailList.size(); j++) {
                    if (StringUtil.isNotEmpty(proReviewConditionDtoList.get(i).getReviewStage()) &&
                            StringUtil.isNotEmpty(proReviewCondDetailList.get(j).getReviewStage()) &&
                            proReviewConditionDtoList.get(i).getReviewStage().equals(proReviewCondDetailList.get(j).getReviewStage())) {
                        proReviewConditionDetailList.add(proReviewCondDetailList.get(j));
                        key =  proReviewConditionDtoList.get(i).getReviewStage();
                    }
                    if (j == (proReviewCondDetailList.size() - 1)) {
                        proReviewCondDetailMap.put(key, proReviewConditionDetailList);
                        break;
                    }
                }
            }*/
            signCount = expertSelectedService.proReviewCount(proReviewConditionDto);
            reviewCount = expertSelectedService.proReviewMeetingCount(proReviewConditionDto);//本月完成項目评审次数
            //至当前月月报
            resultMsg = null;
            resultMap.clear();
            List<ProReviewConditionDto> proReviewConditionDtoAllList = new ArrayList<ProReviewConditionDto>();
            List<ProReviewConditionDto> proReviewConditionByTypeAllList = new ArrayList<ProReviewConditionDto>();
            Map<String , List<ProReviewConditionDto>> attachementContentAllList = new HashMap<>();
            ProReviewConditionDto proReviewConditionSumLast = new ProReviewConditionDto();
            List<ProReviewConditionDto>backDispatchListAll = new ArrayList<>();
            Integer[] proCountArr = null; //按投资金额的项目数
            Integer totalNum = 0; //项目数
            ProReviewConditionDto acvanceTotalDto = new ProReviewConditionDto();
            if (StringUtil.isNotEmpty(monthlyNewsletterDto.getStartMoultiyear()) && StringUtil.isNotEmpty(monthlyNewsletterDto.getStaerTheMonths()) && StringUtil.isNotEmpty(monthlyNewsletterDto.getEndTheMonths())) {
                proReviewConditionDto.setBeginTime(monthlyNewsletterDto.getStartMoultiyear() + "-" + monthlyNewsletterDto.getStaerTheMonths());
                proReviewConditionDto.setEndTime(monthlyNewsletterDto.getStartMoultiyear() + "-" + monthlyNewsletterDto.getEndTheMonths());
                resultMsg = expertSelectedService.proReviewConditionCount(proReviewConditionDto);
                resultMap = (Map<String, Object>) resultMsg.getReObj();
                //获取提前介入项目信息
                acvanceTotalDto = expertSelectedService.getAdvancedCon(proReviewConditionDto);
                proReviewConditionDtoAllList = (List<ProReviewConditionDto>) resultMap.get("protReviewConditionList");
                proReviewConditionSum = expertSelectedService.proReviewConditionSum(proReviewConditionDto);
                backDispatchListAll = expertSelectedService.getBackDispatchInfo(proReviewConditionDto);
                //获取去年统计信息
                ProReviewConditionDto lastP = new ProReviewConditionDto();
                int lastYear = new Integer(monthlyNewsletterDto.getStartMoultiyear()) - 1;
                lastP.setBeginTime( String.valueOf(lastYear) + "-" + monthlyNewsletterDto.getStaerTheMonths());
                lastP.setEndTime(String.valueOf(lastYear) + "-" + monthlyNewsletterDto.getEndTheMonths());
                proReviewConditionSumLast = expertSelectedService.proReviewConditionSum(lastP);

                //项目类别
                resultMap.clear();
                //附件er内容
                attachementContentAllList = monthlyNewsletterRepo.proReviewConditionCount(proReviewConditionDto);

                resultMsg = expertSelectedService.proReviewConditionByTypeCount(proReviewConditionDto);
                resultMap = (Map<String, Object>) resultMsg.getReObj();
                proReviewConditionByTypeAllList = (List<ProReviewConditionDto>) resultMap.get("protReviewConditionList");
                totalNum = (Integer) resultMap.get("totalNum");
                //投资金额
                String beginTime = proReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String[] timeArr = proReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String endTime = proReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                proCountArr = expertSelectedService.proReviewCondByDeclare(beginTime, endTime);
            }

            Map<String , Object> paramsMap = new HashMap<>();
            paramsMap.put("monthlyNewsletterDto" , monthlyNewsletterDto);
            paramsMap.put("signCount" , signCount);
            paramsMap.put("reviewCount" , reviewCount);
            paramsMap.put("proReviewConditionDtoList" , proReviewConditionDtoList);
            paramsMap.put("proReviewConditionDtoAllList" , proReviewConditionDtoAllList);
            paramsMap.put("proReviewConditionByTypeAllList" , proReviewConditionByTypeAllList);
            paramsMap.put("totalNum" , totalNum);
            paramsMap.put("proReviewConditionCur" , proReviewConditionCur);
            paramsMap.put("proReviewConditionSum" , proReviewConditionSum);
            paramsMap.put("proReviewConditionSumLast" , proReviewConditionSumLast);
            paramsMap.put("proReviewCondDetailMap" , proReviewCondDetailMap);
            paramsMap.put("proCountArr" , proCountArr);
            paramsMap.put("acvanceCurDto" , acvanceCurDto);
            paramsMap.put("acvanceTotalDto" , acvanceTotalDto);
            paramsMap.put("backDispatchTotalCur" , backDispatchTotalCur);
            paramsMap.put("backDispatchList" , backDispatchList);
            paramsMap.put("expertReviewMeeting" , expertReviewMeeting);
            paramsMap.put("attachmentContentList" , attachementContentList );
            paramsMap.put("attachmentContentAllList" , attachementContentAllList);
            paramsMap.put("backDispatchListAll" , backDispatchListAll);

            docFile = CreateTemplateUtils.createMonthTemplate(paramsMap);
        }
        return docFile;
    }

    /**
     * 月报简报发起流程
     *
     * @param id
     * @return
     */
    @Override
    public ResultMsg startFlow(String id) {
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), id);
        String assigneeValue;
        Map<String, Object> variables = new HashMap<>();       //流程参数
        if (addSuppLetter == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "发起流程失败，该项目已不存在！");
        }
        if (Validate.isString(addSuppLetter.getProcessInstanceId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该项目已发起流程！");
        }
        if (SessionUtil.getUserInfo().getOrg() == null || !Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgDirector())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您所在部门还没设置部长，请联系管理员进行设置！");
        }

        //查询部门领导
        User user = userRepo.findOrgDirector(addSuppLetter.getCreatedBy());//查询用户的所在部门领导
        if (!Validate.isObject(user) && !Validate.isString(user.getId())) {//判断是否有部门领导
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + user.getOrg().getName() + "】的部长未设置，请先设置！");
        }
        //1、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.MONTHLY_BULLETIN_FLOW, id,
                ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER.getValue(), SessionUtil.getUserId()));

        //2、设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), addSuppLetter.getTitle());

        //3、更改项目状态
        addSuppLetter.setProcessInstanceId(processInstance.getId());
        addSuppLetter.setAppoveStatus(Constant.EnumState.NO.getValue());
        addSuppLetterRepo.save(addSuppLetter);

        //4、跳过第一环节（填报）
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息

        List<AgentTask> agentTaskList = new ArrayList<>();
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
            assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.MONTH_FG);
            variables=ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), assigneeValue);
            variables.put(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue(), true);
            taskService.complete(task.getId(), variables);
        }else{
            assigneeValue = userService.getTaskDealId(user.getId(), agentTaskList, FlowConstant.MONTH_BZ);
            variables=ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_BZ.getValue(), assigneeValue);
            variables.put(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue(), false);
            taskService.complete(task.getId(), variables);
        }
        //如果是代办，还要更新环节名称和任务ID
        if (Validate.isList(agentTaskList)) {
            agentTaskService.updateAgentInfo(agentTaskList,processInstance.getId(),processInstance.getName());
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);


        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),task.getId(), "操作成功！",processDefinitionEntity.getName());
    }

    /**
     * 月报简报流程处理
     *
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg dealSignSupperFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "",                            //流程处理人
                nextNodeKey = "",                              //下一环节名称
                curUserId = SessionUtil.getUserId();           //当前用户ID
        Map<String, Object> variables = new HashMap<>();       //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        boolean isNextUser = false,                            //是否是下一环节处理人（主要是处理领导审批，部长审批）
                isAgentTask = agentTaskService.isAgentTask(task.getId(),curUserId); //是否代办
        List<AgentTask> agentTaskList = new ArrayList<>();
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(businessId);

        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.MONTH_YB:
                flowDto.setDealOption("");
                if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.MONTH_FG);
                    variables=ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), assigneeValue);
                    variables.put(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue(), true);
                }else{
                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgDirector(), agentTaskList,FlowConstant.MONTH_BZ);
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_BZ.getValue(), assigneeValue);
                    variables.put(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue(), false);
                }
                break;
            //部长审批
            case FlowConstant.MONTH_BZ:
                if(isAgentTask){
                    addSuppLetter.setDeptMinisterId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    addSuppLetter.setDeptMinisterId(curUserId);
                }
                addSuppLetter.setDeptMinisterName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));

                addSuppLetter.setDeptMinisterDate(new Date());
                addSuppLetter.setDeptMinisterIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(EnumState.PROCESS.getValue());
                addSuppLetterRepo.save(addSuppLetter);
                assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.MONTH_FG);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), assigneeValue);
                //下一环节还是自己处理
                if(assigneeValue.equals(SessionUtil.getUserId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.MONTH_FG;
                }
                break;
            //分管领导审批
            case FlowConstant.MONTH_FG:
                if(isAgentTask){
                    addSuppLetter.setDeptSLeaderId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    addSuppLetter.setDeptSLeaderId(curUserId);
                }
                addSuppLetter.setDeptSLeaderName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                addSuppLetter.setDeptSleaderDate(new Date());
                addSuppLetter.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(EnumState.STOP.getValue());
                addSuppLetterRepo.save(addSuppLetter);
                //查询部门领导
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                String userId = dealUserList.get(0).getId();
                assigneeValue = userService.getTaskDealId(userId, agentTaskList, FlowConstant.MONTH_ZR);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if(assigneeValue.equals(SessionUtil.getUserId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.MONTH_ZR;
                }
                break;

            //主任审批
            case FlowConstant.MONTH_ZR:
                if(isAgentTask){
                    addSuppLetter.setDeptDirectorId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    addSuppLetter.setDeptDirectorId(curUserId);
                }
                addSuppLetter.setDeptDirectorName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                addSuppLetter.setDeptDirectorDate(new Date());
                addSuppLetter.setDeptDirectorIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(EnumState.YES.getValue());
                //判断生成序号
                int curYearMaxSeq = addSuppLetterRepo.findybMaxSeq(addSuppLetter.getFileType())+1;
                addSuppLetter.setMonthlySeq(curYearMaxSeq);

                //查询年份
                String year = DateUtils.converToString(addSuppLetter.getSuppLetterTime(),"yyyy");
                //生成存档编号,年份+类型+存档年份+存档序号
                String fileNumber = year + Constant.FILE_RECORD_KEY.YD.getValue() + DateUtils.converToString(addSuppLetter.getSuppLetterTime(), "yy") + curYearMaxSeq;
                addSuppLetter.setFileCode(fileNumber);
                addSuppLetterRepo.save(addSuppLetter);
                break;
            default:
                break;
        }
        taskService.addComment(task.getId(), processInstance.getId(), (flowDto == null) ? "" : flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
            //如果下一环节还是自己
            if(isNextUser){
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for(Task t:nextTaskList){
                    if(nextNodeKey.equals(t.getTaskDefinitionKey())){
                        return dealSignSupperFlow(processInstance,t,flowDto);
                    }
                }
            }
        }
        //如果是代办，还要更新环节名称和任务ID
        if (Validate.isList(agentTaskList)) {
            agentTaskService.updateAgentInfo(agentTaskList,processInstance.getId(),processInstance.getName());
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        //当下一个处理人还是自己的时候，任务ID是已经改变了的，所以这里要返回任务ID
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), task.getId(),"操作成功！",null);
    }

    @Override
    public ResultMsg endFlow(String businessKey) {
        MonthlyNewsletter monthlyNewsletter = monthlyNewsletterRepo.findById(MonthlyNewsletter_.id.getName(),businessKey);
        if(Validate.isObject(monthlyNewsletter)){
            if(!SessionUtil.getUserId().equals(monthlyNewsletter.getCreatedBy()) && !SUPER_ACCOUNT.equals(SessionUtil.getLoginName())){
                return ResultMsg.error("您无权进行删除流程操作！");
            }
            monthlyNewsletter.setMonthlyType(EnumState.DELETE.getValue());
            monthlyNewsletterRepo.save(monthlyNewsletter);
        }
        return ResultMsg.ok("操作成功！");
    }

}