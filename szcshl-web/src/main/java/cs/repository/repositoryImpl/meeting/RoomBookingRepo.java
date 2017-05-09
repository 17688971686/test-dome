package cs.repository.repositoryImpl.meeting;

import java.util.List;

import cs.domain.meeting.RoomBooking;
import cs.repository.IRepository;

public interface RoomBookingRepo extends IRepository<RoomBooking, String> {

	List<RoomBooking> findByHql(String hql);
	List<RoomBooking> findWeekBook();
	List<RoomBooking> thisWeekRoomStage();
	List<RoomBooking> findStageNextWeek();
	List<RoomBooking> findNextWeek();

}
