package cs.service.meeting;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import cs.common.Response;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.sys.RoleDto;
import cs.repository.odata.ODataObj;

public interface RoomBookingSerivce {

	PageModelDto<RoomBookingDto> get(ODataObj odataObj);
	Response createRoom(RoomBookingDto roomBookingDto);
	void updateRoom(RoomBookingDto roomBookingDto);
	void deleteRoom(String id);
	void deleteRooms(String[] ids);
	List<RoomBookingDto> getList(ODataObj oDataObj);
	List<RoomBooking> findAll();
	List<RoomBooking> findWeek();
	List<RoomBooking> findStageNextWeek();
	List<RoomBooking> findNextWeek();
	List<MeetingRoom> findMeetingAll();

	
}
