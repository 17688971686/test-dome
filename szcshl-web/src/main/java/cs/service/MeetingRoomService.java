package cs.service;

import cs.domain.MeetingRoom;
import cs.model.MeetingRoomDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

public interface MeetingRoomService {

	PageModelDto<MeetingRoomDto> get(ODataObj odataObj);

	void createMeeting(MeetingRoomDto meetingDto);

	void deleteMeeting(String id);

	void deleteMeeting(String[] ids);
	
	void updateMeeting(MeetingRoomDto meetingDto);
}
