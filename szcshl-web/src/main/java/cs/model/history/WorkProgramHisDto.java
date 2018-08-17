package cs.model.history;

import cs.domain.project.WorkBase;

import java.util.List;

/**
 * Created by ldm on 2018/7/16 0016.
 */
public class WorkProgramHisDto extends WorkBase {

    private List<RoomBookingHisDto> roomBookingHisDtoList;
    private List<ExpertSelectedHisDto> expertSelectedHisDtoList;

    public List<ExpertSelectedHisDto> getExpertSelectedHisDtoList() {
        return expertSelectedHisDtoList;
    }

    public void setExpertSelectedHisDtoList(List<ExpertSelectedHisDto> expertSelectedHisDtoList) {
        this.expertSelectedHisDtoList = expertSelectedHisDtoList;
    }

    public List<RoomBookingHisDto> getRoomBookingHisDtoList() {
        return roomBookingHisDtoList;
    }

    public void setRoomBookingHisDtoList(List<RoomBookingHisDto> roomBookingHisDtoList) {
        this.roomBookingHisDtoList = roomBookingHisDtoList;
    }
}
