package cs.repository.repositoryImpl.history;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.history.*;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelectedDto;
import cs.model.history.ExpertSelectedHisDto;
import cs.model.history.RoomBookingHisDto;
import cs.model.history.WorkProgramHisDto;
import cs.model.meeting.RoomBookingDto;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WorkProgramHisRepoImpl extends AbstractRepository<WorkProgramHis,String> implements WorkProgramHisRepo {

    @Autowired
    private RoomBookingHisRepo roomBookingHisRepo;

    @Autowired
    private ExpertSelectedHisRepo expertSelectedHisRepo;


    @Override
    public void initWPMeetingExp(WorkProgramHisDto workProgramHisDto, WorkProgramHis workProgramHis) {
        if (workProgramHis != null) {
            //1、初始化会议室预定情况
            List<RoomBookingHis> roomBookings = roomBookingHisRepo.findByIds(RoomBooking_.businessId.getName(), workProgramHis.getId(), null);
            if (Validate.isList(roomBookings)) {
                List<RoomBookingHisDto> roomBookingDtos = new ArrayList<>(roomBookings.size());
                roomBookings.forEach(r -> {
                    RoomBookingHisDto rbDto = new RoomBookingHisDto();
                    BeanCopierUtils.copyProperties(r, rbDto);
                    rbDto.setBeginTimeStr(DateUtils.converToString(rbDto.getBeginTime(), "HH:mm"));
                    rbDto.setEndTimeStr(DateUtils.converToString(rbDto.getEndTime(), "HH:mm"));
                    roomBookingDtos.add(rbDto);
                });
                workProgramHisDto.setRoomBookingHisDtoList(roomBookingDtos);
            }
            //2、拟聘请专家
            List<ExpertSelectedHis> expertSelectedList = expertSelectedHisRepo.findByIds(ExpertSelectedHis_.businessId.getName(),workProgramHis.getId(), null);
            if(Validate.isList(expertSelectedList)){
                List<ExpertSelectedHisDto> expertSelectedHisDtoList = new ArrayList<>();
                expertSelectedList.forEach(x -> {
                    ExpertSelectedHisDto expertSelectedDto = new ExpertSelectedHisDto();
                    BeanCopierUtils.copyProperties(x, expertSelectedDto);
                    ExpertDto expertDto = new ExpertDto();
                    BeanCopierUtils.copyProperties(x.getExpert(), expertDto);
                    expertSelectedDto.setExpertDto(expertDto);
                    expertSelectedHisDtoList.add(expertSelectedDto);
                });
                workProgramHisDto.setExpertSelectedHisDtoList(expertSelectedHisDtoList);
            }

        }
    }

    @Override
    public List<WorkProgramHis> findBySignAndBranch(String signid, String branchId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(WorkProgramHis_.signId.getName(),signid));
        criteria.add(Restrictions.eq(WorkProgramHis_.branchId.getName(),branchId));
        return criteria.list();
    }
}
