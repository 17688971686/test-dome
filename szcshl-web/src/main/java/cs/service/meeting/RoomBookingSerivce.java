package cs.service.meeting;

import cs.common.ResultMsg;
import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.repository.odata.ODataObj;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface RoomBookingSerivce {

    PageModelDto<RoomBookingDto> get(ODataObj odataObj);

    ResultMsg saveRoom(RoomBookingDto roomDto);

    /**
     * 删除会议室
     * @param id
     * @param dueToPeople
     * @return
     */
    ResultMsg deleteRoom(String id , String dueToPeople);

    List<RoomBookingDto> getRoomList();

   /* List<RoomBooking> findWeek();*/

   /* List<RoomBookingDto> findNextWeek();*/

    List<MeetingRoomDto> findMeetingAll();

    /*void exportThisWeekStage();*/

    /*void exportNextWeekStage();*/

    File exportRoom(String date, String rbType, String mrId);

    List<Object[]> findWeekRoom(String date, String rbType, String mrId);

    RoomBookingDto initDefaultValue(String businessId, String businessType);

    /**
     * 查询日程数据源
     * @param bookInfo
     * @return
     */
    List<RoomBookingDto> queryBookInfo(RoomBookingDto bookInfo);
}
