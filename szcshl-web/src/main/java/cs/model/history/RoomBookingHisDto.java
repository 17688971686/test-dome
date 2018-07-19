package cs.model.history;

import cs.domain.meeting.RoomBookBase;

public class RoomBookingHisDto extends RoomBookBase {

    private String beginTimeStr;
    private String endTimeStr;

    public String getBeginTimeStr() {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr) {
        this.beginTimeStr = beginTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }
}
