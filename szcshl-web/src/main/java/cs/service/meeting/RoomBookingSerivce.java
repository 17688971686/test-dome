package cs.service.meeting;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.odata.ODataObj;

public interface RoomBookingSerivce {

    PageModelDto<RoomBookingDto> get(ODataObj odataObj);

    ResultMsg saveRoom(RoomBookingDto roomDto);

    void deleteRoom(String id);

    List<RoomBookingDto> getRoomList();

    List<RoomBooking> findWeek();

    List<RoomBookingDto> findNextWeek();

    List<MeetingRoomDto> findMeetingAll();

    void exportThisWeekStage();

    void exportNextWeekStage();

    void exportRoom(String date, String rbType, String mrId);

    List<Map> findWeekRoom(String date, String rbType, String mrId);


}
