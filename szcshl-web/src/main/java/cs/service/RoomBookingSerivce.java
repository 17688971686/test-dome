package cs.service;

import cs.model.PageModelDto;
import cs.model.RoleDto;
import cs.model.RoomBookingDto;
import cs.repository.odata.ODataObj;

public interface RoomBookingSerivce {

	PageModelDto<RoomBookingDto> get(ODataObj odataObj);
	void createRole(RoomBookingDto roomBookingDto);
	void updateRole(RoomBookingDto roomBookingDto);
	void deleteRole(String id);
	void deleteRoles(String[] ids);
}
