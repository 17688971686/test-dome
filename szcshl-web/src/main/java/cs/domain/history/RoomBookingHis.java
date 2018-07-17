package cs.domain.history;

import cs.domain.meeting.RoomBooking;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ldm on 2018/7/16 0016.
 */
@Entity
@Table(name="cs_his_room_booking")
public class RoomBookingHis extends RoomBooking {
}
