package cs.service.meeting;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import cs.common.Response;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.RoleDto;
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

	
}
