package cs.service.meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;

import cs.common.ICurrentUser;
import cs.common.Response;
import cs.common.utils.DateUtils;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBooking;
import cs.model.PageModelDto;
import cs.model.meeting.RoomBookingDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.meeting.MeetingRoomRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.service.sys.RoleServiceImpl;

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
			Date rbDay = item.getRbDay();
			String day = DateUtils.toString(rbDay);
			roomDto.setRbDay(day);
			Date bengin =	item.getBeginTime();
			String start =DateUtils.toString(bengin);
			roomDto.setBeginTime(start);
			Date end = item.getEndTime();
			String endTime = DateUtils.toString(end);
			roomDto.setEndTime(endTime);
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
		List<RoomBooking> rb = roomBookingRepo.thisWeekRoomStage();
		return rb ;
	}
	//本周全部会议安排
	@Override
	@Transactional
	public List<RoomBooking> findWeek() {
		 List<RoomBooking> rb= roomBookingRepo.findWeekBook();
		return rb;
	}
	//导出下周评审会议安排
	@Override
	@Transactional
	public List<RoomBooking> findStageNextWeek() {
		List<RoomBooking> room =roomBookingRepo.findStageNextWeek();
		return room;
	}
	//导出下周全部会议安排
	@Override
	@Transactional
	public List<RoomBooking> findNextWeek() {
		List<RoomBooking> room =roomBookingRepo.findNextWeek();
		return room;
	}
	
	@Override
	@Transactional
	public List<MeetingRoom> findMeetingAll() {
		List<MeetingRoom> meeting	= meetingRoomRepo.findAll();
		return meeting;
	}
	
	/* (non-Javadoc)
	 * @see cs.service.meeting.RoomBookingSerivce#get(cs.repository.odata.ODataObj)
	 */
	@Override
	@Transactional
	public PageModelDto<RoomBookingDto> get(ODataObj odataObj) {
		
		List<RoomBooking> roomList=	roomBookingRepo.findByOdata(odataObj);
		List<RoomBookingDto> roomDtoList = new ArrayList<>();
		for(RoomBooking item : roomList){
			
			RoomBookingDto  roomDto = new RoomBookingDto();
			
			roomDto.setId(item.getId());
			roomDto.setMrID(item.getMrID());
			Date rbDay = item.getRbDay();
			String  day=DateUtils.toString(rbDay);
			roomDto.setRbDay(day);
			Date bengin =	item.getBeginTime();
			String start =DateUtils.toStringHours(bengin);
			roomDto.setBeginTime(start);
			Date end = item.getEndTime();
			String endTime = DateUtils.toStringHours(end);
			roomDto.setEndTime(endTime);
			roomDto.setRbName(item.getRbName());
			roomDto.setRemark(item.getRemark());
			roomDto.setContent(item.getContent());
			roomDto.setDueToPeople(item.getDueToPeople());
			roomDto.setHost(item.getHost());
			roomDto.setMrID(item.getMrID());
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
	public Response createRoom(RoomBookingDto roomBookingDto) {
		//判断会议名称是否存在
		Response response =new Response();
		Criteria criteria =	roomBookingRepo.getSession().createCriteria(RoomBooking.class);	
		criteria.add(Restrictions.eq("rbName", roomBookingDto.getRbName()));
		List<RoomBooking>  room = criteria.list();
		//String hql =" from RoomBooking where  beginTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"' or endTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"'";
		//List<RoomBooking> roomb =  roomBookingRepo.findByHql(hql);
		//Criteria criteriaroom = roomBookingRepo.getSession().createCriteria(RoomBooking.class);
		//criteriaroom.add(Restrictions.between("beginTime",roomBookingDto.getBeginTime(),roomBookingDto.getEndTime()));
//		criteriaroom.add(Restrictions.between("endTime", roomBookingDto.getBeginTime(), roomBookingDto.getEndTime()));
		//List<RoomBooking>  roomlist=criteriaroom.list();
		if(room.isEmpty()){
//			if(roomlist !=null && roomlist.size()>0){
//				response.setMessage("会议安排时间冲突!!!");
//				throw new IllegalArgumentException(String.format("会议安排时间冲突", roomBookingDto.getBeginTime()));
//			}else{
				
				RoomBooking rb = new RoomBooking();
				
				rb.setId(UUID.randomUUID().toString());
				rb.setMrID(roomBookingDto.getMrID());
				String rbday = roomBookingDto.getRbDay();
				Date date = DateUtils.toDateDay(rbday);
				rb.setRbDay(date);
				String begin=roomBookingDto.getBeginTime();
				Date start =DateUtils.toDate(begin);
				rb.setBeginTime(start);
				String endTime = roomBookingDto.getEndTime();
				Date end =DateUtils.toDate(endTime);
				rb.setEndTime(end);
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
//			}
		}else{
			throw new IllegalArgumentException(String.format("会议名称：%s 已经存在，请重新输入", roomBookingDto.getRbName()));
		}
		
		return response;
	}
	
	@Override
	@Transactional
	public void updateRoom(RoomBookingDto roomBookingDto) {
		Response response =new Response();
		
		RoomBooking room =  roomBookingRepo.findById(roomBookingDto.getId());
//		String hql =" from RoomBooking where  beginTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"' or endTime between '"+roomBookingDto.getBeginTime()+"' and '"+roomBookingDto.getEndTime()+"'";
//		List<RoomBooking> roomb =  roomBookingRepo.findByHql(hql);
//		if(roomb !=null && roomb.size()>1){
//			response.setMessage("会议安排时间冲突!!!");
//		}else{
//			room.setRbDay(roomBookingDto.getRbDay());
//			room.setBeginTime(roomBookingDto.getBeginTime());
//			room.setEndTime(roomBookingDto.getEndTime());
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
//		}
		
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
		
	}
	

}
