package cs.service.history;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelected;
import cs.domain.history.*;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBookBase;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.WorkProgram;
import cs.model.project.DispatchDocDto;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.history.*;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkProgramHisServiceImpl implements WorkProgramHisService {
    private static Logger log = Logger.getLogger(WorkProgramHisServiceImpl.class);

    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private WorkProgramHisRepo workProgramHisRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private RoomBookingHisRepo roomBookingHisRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private ExpertReviewHisRepo expertReviewHisRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private ExpertSelectedHisRepo expertSelectedHisRepo;
    @Autowired
    private ExpertSelConditionRepo expertSelConditionRepo;
    @Autowired
    private ExpertSelConditionHisRepo expertSelConditionHisRepo;

    @Override
    public boolean copyWorkProgram(WorkProgram workProgram,String signId) throws RuntimeException {
        //1、工作方案留痕
        WorkProgramHis workProgramHis = new WorkProgramHis();
        BeanCopierUtils.copyProperties(workProgram,workProgramHis);
        workProgramHis.setSignId(signId);
        workProgramHisRepo.save(workProgramHis);
        workProgramRepo.delete(workProgram);

        //2、会议预定留痕
        List<RoomBooking> roomBookList = roomBookingRepo.findByBusinessId(workProgram.getId());
        if(Validate.isList(roomBookList)){
            List<RoomBookingHis> roomBookingHisList = roomBookList.stream().map(item->{
                RoomBookingHis roomBookingHis = new RoomBookingHis();
                BeanCopierUtils.copyProperties(item,roomBookingHis);
                roomBookingRepo.delete(item);
                return roomBookingHis;
            }).collect(Collectors.toList());
            roomBookingHisRepo.bathUpdate(roomBookingHisList);
        }

        //3、专家抽取方案
        ExpertReviewHis expertReviewHis = expertReviewHisRepo.findByBusinessId(signId);
        if(!Validate.isObject(expertReviewHis) || !Validate.isString(expertReviewHis.getId())){
            ExpertReview expertReview = expertReviewRepo.findByBusinessId(signId);
            expertReviewHis = new ExpertReviewHis();
            BeanCopierUtils.copyProperties(expertReview,expertReviewHis);
            expertReviewHisRepo.save(expertReviewHis);
            expertReviewRepo.delete(expertReview);
        }
        //4、选择的专家留痕
        List<ExpertSelected> expertSelectedList = expertSelectedRepo.findAllByBusinessId(workProgram.getId());
        if(Validate.isList(expertSelectedList)){
            List<ExpertSelectedHis> expertSelectedHisList = new ArrayList<>();
            for(ExpertSelected ep : expertSelectedList){
                ExpertSelectedHis expertSelectedHis = new ExpertSelectedHis();
                BeanCopierUtils.copyProperties(ep,expertSelectedHis);
                expertSelectedHis.setExpertReviewHis(expertReviewHis);
                expertSelectedHisList.add(expertSelectedHis);
            }
            expertSelectedHisRepo.bathUpdate(expertSelectedHisList);
            expertSelectedRepo.deleteByBusinessId(workProgram.getId());
        }
        //5、抽取条件留痕
        List<ExpertSelCondition> expertSelConditionList = expertSelConditionRepo.findAllByBusinessId(workProgram.getId());
        if(Validate.isList(expertSelConditionList)){
            List<ExpertSelConditionHis> expertSelConditionHisList = new ArrayList<>();
            for(ExpertSelCondition esc : expertSelConditionList){
                ExpertSelConditionHis expertSelConditionHis = new ExpertSelConditionHis();
                BeanCopierUtils.copyProperties(esc,expertSelConditionHis);
                expertSelConditionHis.setExpertReviewHis(expertReviewHis);
                expertSelConditionHisList.add(expertSelConditionHis);
            }
            expertSelConditionHisRepo.bathUpdate(expertSelConditionHisList);
            expertSelConditionRepo.deleteByBusinessId(workProgram.getId());
        }

        return true;
    }
}
