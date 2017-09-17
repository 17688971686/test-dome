package cs.service.topic;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.domain.topic.WorkPlan;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import cs.repository.repositoryImpl.topic.WorkPlanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:  业务操作实现类
 * author: ldm
 * Date: 2017-9-4 15:33:03
 */
@Service
public class WorkPlanServiceImpl implements WorkPlanService {

    @Autowired
    private WorkPlanRepo workPlanRepo;
    @Autowired
    private TopicInfoRepo topicInfoRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private ExpertRepo expertRepo;


    @Override
    public PageModelDto<WorkPlanDto> get(ODataObj odataObj) {
        PageModelDto<WorkPlanDto> pageModelDto = new PageModelDto<WorkPlanDto>();
        List<WorkPlan> resultList = workPlanRepo.findByOdata(odataObj);
        List<WorkPlanDto> resultDtoList = new ArrayList<WorkPlanDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
            resultList.forEach(x -> {
                WorkPlanDto modelDto = new WorkPlanDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    @Override
    @Transactional
    public ResultMsg save(WorkPlanDto record) {
        WorkPlan domain = new WorkPlan();

        Date now = new Date();
        if(!Validate.isString(record.getTopicId())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，请重新处理！");
        }
        if(!Validate.isString(record.getId())){
            BeanCopierUtils.copyProperties(record, domain);
            domain.setCreatedBy(SessionUtil.getUserId());
            domain.setCreatedDate(now);

            TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),record.getTopicId());
            if(Validate.isString(topicInfo.getIsFinishPlan()) || Constant.EnumState.NO.getValue().equals(topicInfo.getIsFinishPlan())){
                topicInfo.setIsFinishPlan(Constant.EnumState.YES.getValue());
                topicInfoRepo.save(topicInfo);
            }
            domain.setTopicInfo(topicInfo);
        }else{
            domain = workPlanRepo.findById(record.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        }

        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        workPlanRepo.save(domain);
        BeanCopierUtils.copyProperties(domain, record);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！",record);
    }

    @Override
    @Transactional
    public void update(WorkPlanDto record) {
        WorkPlan domain = workPlanRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(new Date());

        workPlanRepo.save(domain);
    }

    @Override
    public WorkPlanDto findById(String id) {
        WorkPlanDto modelDto = new WorkPlanDto();
        if (Validate.isString(id)) {
            WorkPlan domain = workPlanRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
        }
        return modelDto;
    }

    @Override
    @Transactional
    public void delete(String id) {

    }

    /**
     * 根据课题ID初始化
     *
     * @param topicId
     * @return
     */
    @Override
    public WorkPlanDto initByTopicId(String topicId) {
        //1、工作方案基本信息
        WorkPlan workPlan = workPlanRepo.findById("topId", topicId);
        if (workPlan == null || !Validate.isString(workPlan.getId())) {
            workPlan = new WorkPlan();
            TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), topicId);
            workPlan.setTopicName(topicInfo.getTopicName());
            workPlan.setCooperator(topicInfo.getCooperator());
        }
        WorkPlanDto result = new WorkPlanDto();
        BeanCopierUtils.copyProperties(workPlan, result);
        //2、会议室预定情况
        List<RoomBooking> bookList = roomBookingRepo.findByIds(RoomBooking_.businessId.getName(),workPlan.getId(),null);
        if (Validate.isList(bookList)) {
            List<RoomBookingDto> roomBookingDtoList = new ArrayList<>(bookList.size());
            bookList.forEach(r -> {
                RoomBookingDto rbDto = new RoomBookingDto();
                BeanCopierUtils.copyProperties(r, rbDto);
                rbDto.setBeginTimeStr(DateUtils.converToString(rbDto.getBeginTime(), "HH:mm"));
                rbDto.setEndTimeStr(DateUtils.converToString(rbDto.getEndTime(), "HH:mm"));
                roomBookingDtoList.add(rbDto);
            });
            result.setRoomDtoList(roomBookingDtoList);
        }
        //3、拟聘请专家
        List<Expert> expertList = expertRepo.findByBusinessId(workPlan.getId());
        if(Validate.isList(expertList)){
            List<ExpertDto> expertDtoList = new ArrayList<>(expertList.size());
            expertList.forEach( el ->{
                ExpertDto expertDto = new ExpertDto();
                el.setPhoto(null);
                BeanCopierUtils.copyProperties(el,expertDto);
                expertDtoList.add(expertDto);
            });
            result.setExpertDtoList(expertDtoList);
        }
        return result;
    }

}