package cs.service.meeting;

import cs.domain.meeting.MeetingRoom;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.repository.odata.ODataObj;

public interface MeetingRoomService {

	PageModelDto<MeetingRoomDto> get(ODataObj odataObj);

	void createMeeting(MeetingRoomDto meetingDto);

	void deleteMeeting(String id);
	
	void updateMeeting(MeetingRoomDto meetingDto);

	MeetingRoomDto findByIdMeeting(String id);
	
	void roomUseState(MeetingRoomDto meetingDto);
}
