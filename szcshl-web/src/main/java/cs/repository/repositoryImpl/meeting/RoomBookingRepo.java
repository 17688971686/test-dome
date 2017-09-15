package cs.repository.repositoryImpl.meeting;

import java.util.List;
import java.util.Date;

import cs.domain.meeting.RoomBooking;
import cs.model.meeting.RoomBookingDto;
import cs.repository.IRepository;

public interface RoomBookingRepo extends IRepository<RoomBooking, String> {

    List<RoomBooking> findByHql(String hql);

    List<RoomBooking> findWeekBook();

    List<RoomBooking> thisWeekRoomStage();

    List<RoomBookingDto> findStageNextWeek();

    List<RoomBookingDto> findNextWeek();

    Date getMeetingDateByBusinessId(String businessId);
}
