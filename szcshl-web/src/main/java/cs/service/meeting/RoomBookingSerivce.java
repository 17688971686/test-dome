package cs.service.meeting;

import java.util.List;
import java.util.Map;

import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.odata.ODataObj;

public interface RoomBookingSerivce {

	PageModelDto<RoomBookingDto> get(ODataObj odataObj);
	void createRoom(RoomBookingDto roomBookingDto);
	void updateRoom(RoomBookingDto roomBookingDto);
	void deleteRoom(String id);
	void deleteRooms(String[] ids);
	List<RoomBookingDto> getRoomList();
	List<RoomBooking> findWeek();
	List<RoomBookingDto> findNextWeek();
	List<MeetingRoomDto> findMeetingAll();
	void saveRoom(RoomBookingDto roomDto, WorkProgramDto workProgramDto);
	void exportThisWeekStage();
	void exportNextWeekStage();
	void exportRoom(String date,String rbType,String mrId);
	
	List<Map> findWeekRoom(String date,String rbType,String mrId); 

	
}
