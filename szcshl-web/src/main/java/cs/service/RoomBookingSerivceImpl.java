package cs.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.Response;
import cs.domain.MeetingRoom;
import cs.domain.RoomBooking;
import cs.model.PageModelDto;
import cs.model.RoomBookingDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.MeetingRoomRepo;
import cs.repository.repositoryImpl.RoomBookingRepo;

@Service
public class RoomBookingSerivceImpl implements RoomBookingSerivce{

	private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private RoomBookingRepo roomBookingRepo;
	@Autowired
	private MeetingRoomRepo meetingRoomRepo;
	@Override
	@Transactional
	public List<RoomBookingDto> getList(ODataObj oDataObj) {
		List<RoomBooking> roomList=	roomBookingRepo.findByOdata(oDataObj);
		List<RoomBookingDto> roomDtoList = new ArrayList<>();
		for(RoomBooking item : roomList){
			
			RoomBookingDto  roomDto = new RoomBookingDto();
			
			roomDto.setId(item.getId());
			roomDto.setRbDay(item.getRbDay());
			roomDto.setBeginTime(item.getBeginTime());
			roomDto.setEndTime(item.getEndTime());
			roomDto.setRbName(item.getRbName());
			roomDto.setRemark(item.getRemark());
			roomDto.setContent(item.getContent());
			roomDto.setDueToPeople(item.getDueToPeople());
			roomDto.setHost(item.getHost());
			roomDto.setMrID(item.getMrID());//
			roomDto.setRbType(item.getRbType());
			roomDto.setRbStatus(item.getRbStatus());
			roomDto.setAddressName(item.getAddressName());
			
			roomDto.setCreatedBy(currentUser.getLoginName());
			roomDto.setModifiedBy(currentUser.getLoginName());
			
			roomDtoList.add(roomDto);
		}
		return roomDtoList;
	}
	//本周评审会议安排
	@Override
	@Transactional
	public List<RoomBooking> findAll() {
		
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //获取本周一的日期
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monday=df.format(cal.getTime());
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        String sunday=df.format(cal.getTime());
      
        String hql="from RoomBooking where rbDay between '"+monday+"' and '"+sunday+"' order by rbDay asc";
        List<RoomBooking> rb= roomBookingRepo.getListByHQL(hql);
		return rb ;
	}
	//本周全部会议安排
	@Override
	@Transactional
	public List<RoomBooking> findWeek() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //获取本周一的日期
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monday=df.format(cal.getTime());
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        String sunday=df.format(cal.getTime());
        String hql="from RoomBooking where rbDay between '"+monday+"' and '"+sunday+"' order by rbDay asc";
        List<RoomBooking> rb= roomBookingRepo.getListByHQL(hql);
		return rb;
	}
	//导出下周平射会议安排
	@Override
	@Transactional
	public List<RoomBooking> findStageNextWeek() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //获取下周星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        String nextMonday=df.format(cal.getTime());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //获取下周星期日
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        String nextSunday=df.format(cal.getTime());
        String hql="from RoomBooking where rbDay between '"+nextMonday+"' and '"+nextSunday+"' order by rbDay asc";
    	List<RoomBooking> room =roomBookingRepo.getListByHQL(hql);
		return room;
	}
	//导出下周全部会议安排
	@Override
	@Transactional
	public List<RoomBooking> findNextWeek() {
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //获取下周星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        String nextMonday=df.format(cal.getTime());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //获取下周星期日
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        String nextSunday=df.format(cal.getTime());
    	String hql="from RoomBooking where rbDay between '"+nextMonday+"' and '"+nextSunday+"' order by rbDay asc";
    	List<RoomBooking> room =roomBookingRepo.getListByHQL(hql);
		return room;
	}
	
	@Override
	@Transactional
	public List<MeetingRoom> findMeetingAll() {
		List<MeetingRoom> meeting	= meetingRoomRepo.findAll();
		return meeting;
	}
	
	@Override
	@Transactional
	public PageModelDto<RoomBookingDto> get(ODataObj odataObj) {
		
		List<RoomBooking> roomList=	roomBookingRepo.findByOdata(odataObj);
		List<RoomBookingDto> roomDtoList = new ArrayList<>();
		for(RoomBooking item : roomList){
			
			RoomBookingDto  roomDto = new RoomBookingDto();
			
			roomDto.setId(item.getId());
			roomDto.setMrID(item.getMrID());
			roomDto.setRbDay(item.getRbDay());
			roomDto.setBeginTime(item.getBeginTime());
			roomDto.setEndTime(item.getEndTime());
			roomDto.setRbName(item.getRbName());
			roomDto.setRemark(item.getRemark());
			roomDto.setContent(item.getContent());
			roomDto.setDueToPeople(item.getDueToPeople());
			roomDto.setHost(item.getHost());
			roomDto.setMrID(item.getMrID());//
			roomDto.setRbType(item.getRbType());
			roomDto.setRbStatus(item.getRbStatus());
			roomDto.setAddressName(item.getAddressName());
			
			roomDto.setCreatedBy(currentUser.getLoginName());
			roomDto.setModifiedBy(currentUser.getLoginName());
			
			roomDtoList.add(roomDto);
		}
		
		PageModelDto<RoomBookingDto> pageModelDto = new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(roomDtoList);
		logger.info("预定会议查询");
		return pageModelDto;
	}
	@Override
	@Transactional
	public void createRoom(RoomBookingDto roomBookingDto) {
		//判断会议名称是否存在
		Criteria criteria =	roomBookingRepo.getSession().createCriteria(RoomBooking.class);	
		criteria.add(Restrictions.eq("rbName", roomBookingDto.getRbName()));
		List<RoomBooking>  room = criteria.list();
		String hql =" from RoomBooking where  beginTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"' or endTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"'";
		Response response =new Response();
		List<RoomBooking> roomb =  roomBookingRepo.getListByHQL(hql);
		if(room.isEmpty()){
			
			if(roomb !=null && roomb.size()>0){
			
				response.setMessage("会议安排时间冲突!!!");
			}else{
				
				RoomBooking rb = new RoomBooking();
				
				rb.setId(UUID.randomUUID().toString());
				rb.setMrID(roomBookingDto.getMrID());
				rb.setRbDay(roomBookingDto.getRbDay());
				rb.setBeginTime(roomBookingDto.getBeginTime());
				rb.setEndTime(roomBookingDto.getEndTime());
				rb.setContent(roomBookingDto.getContent());
				rb.setRbType(roomBookingDto.getRbType());
				rb.setRbName(roomBookingDto.getRbName());
				rb.setHost(roomBookingDto.getHost());
				rb.setDueToPeople(roomBookingDto.getDueToPeople());
				rb.setRemark(roomBookingDto.getRemark());
				rb.setRbStatus(roomBookingDto.getRbStatus());
				rb.setAddressName(roomBookingDto.getAddressName());
				rb.setCreatedBy(currentUser.getLoginName());
				rb.setModifiedBy(currentUser.getLoginName());
				roomBookingRepo.save(rb);
			}
		}else{
			
			throw new IllegalArgumentException(String.format("会议名称：%s 已经存在，请重新输入", roomBookingDto.getRbName()));

		}
	}
	
	@Override
	@Transactional
	public void updateRoom(RoomBookingDto roomBookingDto) {
		Response response =new Response();
		
		RoomBooking room =  roomBookingRepo.findById(roomBookingDto.getId());
		String hql =" from RoomBooking where  beginTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"' or endTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"'";
		List<RoomBooking> roomb =  roomBookingRepo.getListByHQL(hql);
		if(room !=null && roomb.size()>1){
			response.setMessage("会议安排时间冲突!!!");
		}else{
			room.setRbDay(roomBookingDto.getRbDay());
			room.setBeginTime(roomBookingDto.getBeginTime());
			room.setEndTime(roomBookingDto.getEndTime());
			room.setContent(roomBookingDto.getContent());
			room.setRbType(roomBookingDto.getRbType());
			room.setRbName(roomBookingDto.getRbName());
			room.setHost(roomBookingDto.getHost());
			room.setDueToPeople(roomBookingDto.getDueToPeople());
			room.setRemark(roomBookingDto.getRemark());
			room.setRbStatus(roomBookingDto.getRbStatus());
			room.setAddressName(roomBookingDto.getAddressName());
			room.setModifiedBy(currentUser.getLoginName());
			roomBookingRepo.save(room);
			logger.info(String.format("更新会议室预定,会议名称:%s", roomBookingDto.getRbName()));
		}
		
	}
	@Override
	@Transactional
	public void deleteRoom(String id) {
		
	RoomBooking room = 	roomBookingRepo.findById(id);
		if(room != null){
			roomBookingRepo.delete(room);
			logger.info(String.format("删除会议室预定,会议名称:%s", room.getRbName()));
		}
	}
	@Override
	public void deleteRooms(String[] ids) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	
	
	
	
	
	


}
