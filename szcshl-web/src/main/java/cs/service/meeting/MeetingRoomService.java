package cs.service.meeting;

import cs.common.ResultMsg;
import cs.domain.meeting.MeetingRoom;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.repository.odata.ODataObj;

import java.util.List;

public interface MeetingRoomService {

	PageModelDto<MeetingRoomDto> get(ODataObj odataObj);

    ResultMsg createMeeting(MeetingRoomDto meetingDto);

    ResultMsg deleteMeeting(String id);

    ResultMsg updateMeeting(MeetingRoomDto meetingDto);

	MeetingRoomDto findByIdMeeting(String id);
	
	void roomUseState(MeetingRoomDto meetingDto);

    List<MeetingRoomDto> findAll();

	/**
	 * 刷新缓存
	 */
	void fleshMeetingCache();
}
