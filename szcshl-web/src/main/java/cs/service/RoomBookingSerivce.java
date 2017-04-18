package cs.service;

import java.util.List;

import cs.domain.RoomBooking;
import cs.model.PageModelDto;
import cs.model.RoleDto;
import cs.model.RoomBookingDto;
import cs.repository.odata.ODataObj;

public interface RoomBookingSerivce {

	PageModelDto<RoomBookingDto> get(ODataObj odataObj);
	void createRoom(RoomBookingDto roomBookingDto);
	void updateRoom(RoomBookingDto roomBookingDto);
	void deleteRoom(String id);
	void deleteRooms(String[] ids);
	List<RoomBooking> getList(ODataObj oDataObj);
}
