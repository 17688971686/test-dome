package cs.repository.repositoryImpl.meeting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.meeting.RoomBooking;
import cs.model.meeting.RoomBookingDto;
import cs.repository.AbstractRepository;

@Repository
public class RoomBookingRepoImpl extends AbstractRepository<RoomBooking, String>  implements RoomBookingRepo{

	@Override
	public List<RoomBooking> findByHql(String hql) {		
		return findByHql(hql);
	}

	@Override
	public List<RoomBooking> findWeekBook() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //获取本周一的日期
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
      //  String monday=df.format(cal.getTime());
        Date monday= cal.getTime();
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date sunday = cal.getTime();
        Criteria ct = getExecutableCriteria();
        List<RoomBooking> roomList= ct.add(Restrictions.between("rbDay",monday, sunday)).list(); 
        
		return roomList;
	}

	@Override
	public List<RoomBooking> thisWeekRoomStage() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //获取本周一的日期
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date  monday=cal.getTime();
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date sunday=cal.getTime();
        Criteria criteria =getExecutableCriteria();
        List<RoomBooking> roomList= criteria.add(Restrictions.between("rbDay", monday, sunday)).list();
		return roomList;
	}

	@Override
	public List<RoomBookingDto> findStageNextWeek() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //获取下周星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextMonday=cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //获取下周星期日
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextSunday=cal.getTime();
        Criteria criteria =getExecutableCriteria();
        List<RoomBookingDto> roomList= criteria.add(Restrictions.between("rbDay", nextMonday, nextSunday)).list();
		return roomList;
	}

	@Override
	public List<RoomBookingDto> findNextWeek() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //获取下周星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextMonday=cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //获取下周星期日
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextSunday=cal.getTime();
        Criteria criteria =getExecutableCriteria();
        List<RoomBookingDto> roomList= criteria.add(Restrictions.between("rbDay", nextMonday, nextSunday)).list();
		return roomList;
	}


}
