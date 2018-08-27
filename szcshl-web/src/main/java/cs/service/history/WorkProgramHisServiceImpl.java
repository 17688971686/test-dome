package cs.service.history;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.history.*;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelectedDto;
import cs.model.history.ExpertSelectedHisDto;
import cs.model.history.RoomBookingHisDto;
import cs.model.history.WorkProgramHisDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.history.*;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.sql.MeettingSql;
import cs.sql.ReviewSql;
import cs.sql.WorkSql;
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
    public boolean copyWorkProgram(WorkProgram workProgram,String signId,String newWorkProgramId) throws RuntimeException {
        //1、工作方案留痕
        WorkProgramHis workProgramHis = new WorkProgramHis();
        BeanCopierUtils.copyProperties(workProgram,workProgramHis);
        workProgramHis.setSignId(signId);
        workProgramHisRepo.save(workProgramHis);
        //用hql删除(原有的工作方案不删除)
        workProgramRepo.deleteById(WorkProgram_.id.getName(),workProgram.getId());

        //2、会议预定留痕
        List<String> idList = new ArrayList<>();
        List<RoomBooking> roomBookList = roomBookingRepo.findByBusinessId(workProgram.getId());
        if(Validate.isList(roomBookList)){
            List<RoomBookingHis> roomBookingHisList = roomBookList.stream().map(item->{
                RoomBookingHis roomBookingHis = new RoomBookingHis();
                BeanCopierUtils.copyProperties(item,roomBookingHis);
                idList.add(item.getId());
                return roomBookingHis;
            }).collect(Collectors.toList());
            roomBookingHisRepo.bathUpdate(roomBookingHisList);
        }

       /* 原有记录不删除
       if(Validate.isList(idList)){
            roomBookingRepo.deleteById(RoomBooking_.id.getName(),StringUtils.join(idList.toArray(), SysConstants.SEPARATE_COMMA));
        }*/
       //更新业务ID
        roomBookingRepo.executeSql(MeettingSql.updateBusinessId(workProgram.getId(),newWorkProgramId));

        boolean isHaveExpertReview = false;
        ExpertReviewHis expertReviewHis = null;
        //3、专家抽取方案
        ExpertReview expertReview = expertReviewRepo.findByBusinessId(signId);
        if(Validate.isObject(expertReview) && Validate.isString(expertReview.getId())){
            isHaveExpertReview = true;
            expertReviewHis = expertReviewHisRepo.findByBusinessId(signId);
            if(!Validate.isObject(expertReviewHis) || !Validate.isString(expertReviewHis.getId())){
                expertSelectedHisRepo.executeSql(WorkSql.copyExpertReview(expertReview.getId()));
            }
            //重置评审方案整体抽取方案（包括专家抽取，评审费发放等）
            expertReviewRepo.executeSql(ReviewSql.resetAllExtract(expertReview.getId()));
        }
        if(isHaveExpertReview){
            //直接通过语句复制
            expertSelectedHisRepo.executeSql(WorkSql.copyExpertSelected(workProgram.getId()));
            //更新业务ID
            expertSelectedRepo.executeSql(WorkSql.updateSeleBusinessId(workProgram.getId(),newWorkProgramId));
           /* 原有记录不删除
           expertSelectedRepo.deleteByBusinessId(workProgram.getId());*/

            //4、选择的专家留痕
            /*List<ExpertSelected> expertSelectedList = expertSelectedRepo.findAllByBusinessId(workProgram.getId());
            if(Validate.isList(expertSelectedList)){
                for(ExpertSelected ep : expertSelectedList){
                    ExpertSelectedHis expertSelectedHis = new ExpertSelectedHis();
                    BeanCopierUtils.copyPropertiesIgnoreProps(ep,expertSelectedHis,ExpertSelected_.expert.getName());
                    if(Validate.isObject(ep.getExpert()) || Validate.isString(ep.getExpert().getExpertID())){
                        Expert expert = expertRepo.findById(Expert_.expertID.getName(), ep.getExpert().getExpertID());
                        expertSelectedHis.setExpert(expert);
                    }
                    expertSelectedHis.setExpertReviewId(expertReview.getId());
                    expertSelectedHis.setId(null);
                    expertSelectedHisRepo.save(expertSelectedHis);
                }
                expertSelectedRepo.deleteByBusinessId(workProgram.getId());
            }*/

            expertSelConditionHisRepo.executeSql(WorkSql.copyExpertCondition(workProgram.getId()));
            //更新业务ID
            expertSelConditionRepo.executeSql(WorkSql.updateConditionBusinessId(workProgram.getId(),newWorkProgramId));
            /*原有记录不删除
            expertSelConditionRepo.deleteByBusinessId(workProgram.getId());*/

            //5、抽取条件留痕
           /* List<ExpertSelCondition> expertSelConditionList = expertSelConditionRepo.findAllByBusinessId(workProgram.getId());
            if(Validate.isList(expertSelConditionList)){
                for(ExpertSelCondition esc : expertSelConditionList){
                    ExpertSelConditionHis expertSelConditionHis = new ExpertSelConditionHis();
                    BeanCopierUtils.copyProperties(esc,expertSelConditionHis);
                    expertSelConditionHis.setExpertReviewId(expertReview.getId());
                    expertSelConditionHis.setId(null);
                    expertSelConditionHisRepo.save(expertSelConditionHis);
                }
                expertSelConditionRepo.deleteByBusinessId(workProgram.getId());
            }*/
        }
        return true;
    }

    /**
     * 工作方案历史信息
     * @param signid
     * @param branchId
     * @return
     */
    @Override
    public List<WorkProgramHisDto> findBySignAndBranch(String signid, String branchId) {
        List<WorkProgramHis> workProgramHisList = workProgramHisRepo.findBySignAndBranch(signid,branchId);
        if(Validate.isList(workProgramHisList)){
            int countSize = workProgramHisList.size();
            List<WorkProgramHisDto> resultList = new ArrayList<>(countSize);
            for(int i=0;i<countSize;i++){
                WorkProgramHis workProgramHis = workProgramHisList.get(i);
                WorkProgramHisDto workProgramHisDto = new WorkProgramHisDto();
                BeanCopierUtils.copyProperties(workProgramHis,workProgramHisDto);
                workProgramHisRepo.initWPMeetingExp(workProgramHisDto, workProgramHis);
                resultList.add(workProgramHisDto);
            }
            return resultList;
        }
        return null;
    }

    @Override
    public WorkProgramHis findById(String wpId) {
        return workProgramHisRepo.findById(wpId);
    }

    @Override
    public void initWPMeetingExp(WorkProgramDto workProgramDto, WorkProgramHis his) {
        //1、初始化会议室预定情况
        List<RoomBookingHis> roomBookings = roomBookingHisRepo.findByIds(RoomBooking_.businessId.getName(), his.getId(), null);
        if (Validate.isList(roomBookings)) {
            List<RoomBookingDto> roomBookingDtos = new ArrayList<>(roomBookings.size());
            roomBookings.forEach(r -> {
                RoomBookingDto rbDto = new RoomBookingDto();
                BeanCopierUtils.copyProperties(r, rbDto);
                rbDto.setBeginTimeStr(DateUtils.converToString(rbDto.getBeginTime(), "HH:mm"));
                rbDto.setEndTimeStr(DateUtils.converToString(rbDto.getEndTime(), "HH:mm"));
                roomBookingDtos.add(rbDto);
            });
            workProgramDto.setRoomBookingDtos(roomBookingDtos);
        }
        //2、拟聘请专家
        List<ExpertSelectedHis> expertSelectedList = expertSelectedHisRepo.findByIds(ExpertSelectedHis_.businessId.getName(),his.getId(), null);
        if(Validate.isList(expertSelectedList)){
            List<ExpertSelectedDto> expertSelectedHisDtoList = new ArrayList<>();
            expertSelectedList.forEach(x -> {
                ExpertSelectedDto expertSelectedDto = new ExpertSelectedDto();
                BeanCopierUtils.copyProperties(x, expertSelectedDto);
                ExpertDto expertDto = new ExpertDto();
                BeanCopierUtils.copyProperties(x.getExpert(), expertDto);
                expertSelectedDto.setExpertDto(expertDto);
                expertSelectedHisDtoList.add(expertSelectedDto);
            });
            workProgramDto.setExpertSelectedDtoList(expertSelectedHisDtoList);
        }
    }
}
