package cs.repository.repositoryImpl.meeting;

import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RoomBookingRepoImpl extends AbstractRepository<RoomBooking, String>  implements RoomBookingRepo{
    /*
	@Override
	public List<RoomBooking> findByHql(String hql) {		
		return findByHql(hql);
	}*/

	/*@Override
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
	}*/

    /**
     * 根据业务ID获取评审会日期
     * @param businessId
     * @return
     */
    @Override
    public Date getMeetingDateByBusinessId(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(RoomBooking_.businessId.getName(),businessId));
        criteria.addOrder(Order.asc(RoomBooking_.createdDate.getName()));
        List<RoomBooking> list = criteria.list();
        if(Validate.isList(list)){
            return list.get(0).getRbDay();
        }
        return null;
    }

    /**
     * 根据业务ID判断是否已经预定有会议室
     * @param businessId
     * @return
     */
    @Override
    public boolean isHaveBookMeeting(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(RoomBooking_.businessId.getName(),businessId));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalResult > 0 ;
    }

    /**
     * 根据业务ID，更改会议室预定状态
     * @param businessId
     * @param status
     */
    @Override
    public void updateStateByBusinessId(String businessId, String status) {
        if(Validate.isString(businessId) && Validate.isString(status)){
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" update "+RoomBooking.class.getSimpleName()+" set ");
            hqlBuilder.append(RoomBooking_.rbStatus.getName()+" =:rbStatus ");
            hqlBuilder.setParam("rbStatus",status);
            hqlBuilder.append(" where "+RoomBooking_.businessId.getName()+" =:businessId ");
            hqlBuilder.setParam("businessId",businessId);
            executeHql(hqlBuilder);
        }
    }

    @Override
    public void deleteByBusinessId(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from CS_ROOM_BOOKING where BUSINESSID =:businessId ");
        sqlBuilder.setParam("businessId", businessId);
        executeSql(sqlBuilder);
    }

    @Override
    public List<RoomBooking> findByBusinessId(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(RoomBooking_.businessId.getName(),businessId));
        return criteria.list();
    }

}
