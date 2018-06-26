package cs.service.sys;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.QuartzManager;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Quartz;
import cs.domain.sys.Quartz_;
import cs.model.PageModelDto;
import cs.model.sys.QuartzDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.ProjectStopRepo;
import cs.repository.repositoryImpl.sys.QuartzRepo;
import cs.service.expert.ExpertReviewService;
import cs.service.flow.FlowService;
import cs.service.project.ProjectStopService;
import cs.service.project.SignService;
import cs.service.restService.SignRestService;
import org.activiti.engine.RuntimeService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Description: 定时器配置 业务操作实现类
 * author: ldm
 * Date: 2017-6-20 10:47:42
 */
@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private QuartzRepo quartzRepo;
    //把所有定时器需要用到的service都引进来
    @Autowired
    private LogService logService;
    @Autowired
    private ExpertReviewService expertReviewService;
    @Autowired
    private ProjectStopService projectStopService;
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private SignService signService;
    @Autowired
    private SignRestService signRestService;
    @Autowired
    private ProjectStopRepo projectStopRepo;
    @Autowired
    private RuntimeService runtimeService;


    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SMSContent smsContent;
    @Autowired
    private SMSLogService smsLogService;

    @Override
    public PageModelDto<QuartzDto> get(ODataObj odataObj) {
        PageModelDto<QuartzDto> pageModelDto = new PageModelDto<QuartzDto>();
        List<Quartz> resultList = quartzRepo.findByOdata(odataObj);
        List<QuartzDto> resultDtoList = new ArrayList<QuartzDto>(resultList == null ? 0 : resultList.size());
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                QuartzDto modelDto = new QuartzDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     * 保存定时器
     *
     * @param record
     * @return
     */
    @Override
    @Transactional
    public ResultMsg save(QuartzDto record) {
        Quartz domain = new Quartz();
        Date now = new Date();
        if (Validate.isString(record.getId())) {
            domain = quartzRepo.findById(record.getId());
            if (Constant.EnumState.YES.getValue().equals(domain.getCurState())) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该定时器正在运行，请先停用再执行更改操作！");
            }
            BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        } else {
            BeanCopierUtils.copyProperties(record, domain);
            domain.setCreatedBy(SessionUtil.getUserId());
            domain.setCreatedDate(now);
            //默认执行方式为手动执行
            if (!Validate.isString(domain.getRunWay())) {
                domain.setRunWay(Constant.EnumState.NO.getValue());
            }
            //默认当前状态位未执行
            domain.setCurState(Constant.EnumState.NO.getValue());
            //是否在用，默认为在用
            domain.setIsEnable(Constant.EnumState.YES.getValue());
        }
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setModifiedDate(now);
        quartzRepo.save(domain);
        ResultMsg returnResult = null;
        //如果默认为自动执行，则马上启动
        if (Constant.EnumState.YES.getValue().equals(domain.getRunWay())) {
            returnResult = quartzExecute(domain.getId());
        }
        if(returnResult == null){
            returnResult = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
        }
        return returnResult;
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @Override
    public QuartzDto findById(String id) {
        QuartzDto modelDto = new QuartzDto();
        Quartz domain = quartzRepo.findById(id);
        BeanCopierUtils.copyProperties(domain, modelDto);
        return modelDto;
    }

    /**
     * 停用定时器
     * @param id
     */
    @Override
    @Transactional
    public ResultMsg delete(String id) {
        Quartz quartz = quartzRepo.findById(Quartz_.id.getName(), id);
        if (quartz == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该定时器已被删除！");
        }
        if(Constant.EnumState.YES.getValue().equals(quartz.getCurState())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该定时器正在运行，不能进行此操作！");
        }
        quartz.setIsEnable(Constant.EnumState.NO.getValue());
        quartzRepo.save(quartz);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 查询系统在用，并且是自动启动的项目
     * @return
     */
    @Override
    public List<Quartz> findDefaultQuartz() {
        Criteria criteria = quartzRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Quartz_.runWay.getName(),Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.eq(Quartz_.isEnable.getName(),Constant.EnumState.YES.getValue()));
        return criteria.list();
    }

    /**
     * 执行定时器
     *
     * @param quartzId
     * @return
     */
    @Override
    public ResultMsg quartzExecute(String quartzId) {
        try {
            Quartz quartz = quartzRepo.findById(Quartz_.id.getName(), quartzId);
            if (quartz == null) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该定时器已被删除！");
            }
            String time = quartz.getCronExpression();
            if(!Validate.isString(time)){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该定时器执行表达式没设置！");
            }
            SchedulerFactory schedulderFactory = new StdSchedulerFactory();
            Scheduler sched = schedulderFactory.getScheduler();
            String cls = quartz.getClassName();
            String jobName = quartz.getQuartzName();

            //把所有用到的service加入到参数中去
            Map<String,Object> params = new HashMap<>();
            params.put("logService",logService);
            params.put("expertReviewService",expertReviewService);
            params.put("projectStopService",projectStopService);
            params.put("workdayService",workdayService);
            params.put("flowService",flowService);
            params.put("signService",signService);
            params.put("signRestService",signRestService);
            params.put("projectStopRepo",projectStopRepo);
            params.put("runtimeService",runtimeService);
            params.put("sysConfigService",sysConfigService);
            params.put("smsContent",smsContent);
            params.put("smsLogService",smsLogService);

            if (Job.class.isAssignableFrom(Class.forName(cls))) {
                QuartzManager.addJob(sched, jobName, Class.forName(cls), time,params);
                //设置状态
                quartz.setCurState(Constant.EnumState.YES.getValue());
                quartz.setIsEnable(Constant.EnumState.YES.getValue());
                quartzRepo.save(quartz);
                return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
            } else {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，设置参数不正确！");
            }
        } catch (Exception e) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败:"+e.getMessage());
        }
    }

    /**
     * 暂停定时器
     * @param quartzId
     * @return
     */
    @Override
    public ResultMsg quartzStop(String quartzId) {
        try {
            Quartz quartz = quartzRepo.findById(Quartz_.id.getName(), quartzId);
            if (quartz == null) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该定时器已被删除！");
            }
            SchedulerFactory schedulderFactory = new StdSchedulerFactory();
            Scheduler sched = schedulderFactory.getScheduler();
            String jobName = quartz.getQuartzName();
            QuartzManager.removeJob(sched, jobName);
            //更改状态
            quartz.setCurState(Constant.EnumState.NO.getValue());
            quartzRepo.save(quartz);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
        }catch(Exception e){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败："+e.getMessage());
        }
    }


}