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

    /**
     * 根据业务ID查询最先预定的会议室日期
     * @param businessId
     * @return
     */
    Date getMeetingDateByBusinessId(String businessId);

    /**
     * 根据业务ID判断是否已经预定有会议室
     * @param businessId
     * @return
     */
    boolean isHaveBookMeeting(String businessId);
}
