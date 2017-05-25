package cs.service.meeting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.GetWeekUtils;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.WorkProgram;
import cs.model.PageModelDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.meeting.MeetingRoomRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
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
	@Autowired
	private WorkProgramRepo workProgramRepo;
	
	@Override
	public List<RoomBookingDto> getRoomList() {
		List<RoomBooking> roomList=	roomBookingRepo.findAll();
		List<RoomBookingDto> roomDtos = new ArrayList<>();
		if(roomList !=null && roomList.size() >0){
			roomList.forEach(x->{
				RoomBookingDto roomDto = new RoomBookingDto();
				BeanCopierUtils.copyProperties(x, roomDto);
				roomDto.setCreatedDate(x.getCreatedDate());
				roomDto.setModifiedDate(x.getModifiedDate());
				roomDtos.add(roomDto);
			});
		}
		return roomDtos;
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
	public PageModelDto<RoomBookingDto> get(ODataObj odataObj) {
		PageModelDto<RoomBookingDto> pageModelDto = new PageModelDto<RoomBookingDto>();
		List<RoomBooking> roomList=	roomBookingRepo.findByOdata(odataObj);
		List<RoomBookingDto> roomDtoList = new ArrayList<RoomBookingDto>(roomList.size());
		if(roomList != null && roomList.size() > 0){
			roomList.forEach(x->{
				RoomBookingDto  roomDto = new RoomBookingDto();
				BeanCopierUtils.copyProperties(x, roomDto);
				roomDto.setCreatedDate(x.getCreatedDate());
				roomDto.setModifiedDate(x.getModifiedDate());
				roomDtoList.add(roomDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(roomDtoList);
		return pageModelDto;
	}
	@Override
	@Transactional
	public void createRoom(RoomBookingDto roomBookingDto) {
		//判断会议名称是否存在
		Criteria criteria =	roomBookingRepo.getSession().createCriteria(RoomBooking.class);	
		criteria.add(Restrictions.eq("rbName", roomBookingDto.getRbName()));
		List<RoomBooking>  room = criteria.list();	
		RoomBookingDto roomDto = new RoomBookingDto();
		HqlBuilder hql= HqlBuilder.create();
		hql.append(" from "+RoomBooking.class.getSimpleName()+" where "+RoomBooking_.beginTime.getName());
		hql.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
		hql.append(" and :endTime").setParam("endTime", roomBookingDto.getEndTime());
		hql.append(" or "+RoomBooking_.endTime.getName());
		hql.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
		hql.append(" and :endTime").setParam("endTime", roomBookingDto.getEndTime());
		List<RoomBooking> roomList = roomBookingRepo.findByHql(hql);
		if(room.isEmpty()){	
			if(roomList != null && roomList.size() > 0){
				throw new IllegalArgumentException(String.format("%s 会议室安排时间冲突!!!", roomBookingDto.getBeginTime()));
			}else{
				RoomBooking rb = new RoomBooking();
				BeanCopierUtils.copyProperties(roomBookingDto, rb);
				rb.setId(UUID.randomUUID().toString());
				Date now = new Date();
				rb.setCreatedDate(now);
				rb.setModifiedDate(now);
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
		RoomBooking room =  roomBookingRepo.findById(roomBookingDto.getId());
		HqlBuilder hql= HqlBuilder.create();
		hql.append(" from "+RoomBooking.class.getSimpleName()+" where "+RoomBooking_.beginTime.getName());
		hql.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
		hql.append(" and :endTime").setParam("endTime", roomBookingDto.getEndTime());
		hql.append(" or "+RoomBooking_.endTime.getName());
		hql.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
		hql.append(" and :endTime").setParam("endTime", roomBookingDto.getEndTime());
		List<RoomBooking> roomList = roomBookingRepo.findByHql(hql);
		if(roomList !=null && roomList.size() > 1){
			throw new IllegalArgumentException(String.format("%s 会议室安排时间冲突!!!", roomBookingDto.getBeginTime()));
		}else{
			//RoomBooking room = new RoomBooking();
			BeanCopierUtils.copyPropertiesIgnoreNull(roomBookingDto, room);
			room.setModifiedBy(currentUser.getLoginName());
			room.setModifiedDate(new Date());
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
		
	}
	@Override
	@Transactional
	public void saveRoom(RoomBookingDto roomDto, WorkProgramDto workProgramDto) {
		//判断会议名称是否存在
		Criteria criteria =	roomBookingRepo.getSession().createCriteria(RoomBooking.class);	
		criteria.add(Restrictions.eq("rbName", roomDto.getRbName()));
		List<RoomBooking>  room = criteria.list();	
		RoomBookingDto roomDtos = new RoomBookingDto();
		HqlBuilder hql= HqlBuilder.create();
		hql.append(" from "+RoomBooking.class.getSimpleName()+" where "+RoomBooking_.beginTime.getName());
		hql.append(" between :beginTime").setParam("beginTime", roomDto.getBeginTime());
		hql.append(" and :endTime").setParam("endTime", roomDto.getEndTime());
		hql.append(" or "+RoomBooking_.endTime.getName());
		hql.append(" between :beginTime").setParam("beginTime", roomDto.getBeginTime());
		hql.append(" and :endTime").setParam("endTime", roomDto.getEndTime());
		List<RoomBooking> roomList = roomBookingRepo.findByHql(hql);
		if(room.isEmpty()){	
			if(roomList != null && roomList.size() > 0){
				throw new IllegalArgumentException(String.format("%s 会议室安排时间冲突!!!", roomDto.getBeginTime()));
			}else{
				RoomBooking rb = new RoomBooking();
				BeanCopierUtils.copyProperties(roomDto, rb);
				rb.setId(UUID.randomUUID().toString());
				Date now = new Date();
				rb.setCreatedDate(now);
				rb.setModifiedDate(now);
				rb.setCreatedBy(currentUser.getLoginName());
				rb.setModifiedBy(currentUser.getLoginName());
				roomBookingRepo.save(rb);
				//"ea31bbab-35ae-49d8-82b4-58f193f4a237"
				WorkProgram workProgram =workProgramRepo.findById(roomDto.getWorkProgramId());
				if(workProgram != null){
					Date rbDay = roomDto.getRbDay();
					String addr = roomDto.getMrID();
					Date start = roomDto.getBeginTime();
					Date end = roomDto.getEndTime();
					DateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String d=datef.format(rbDay);
					String s =df.format(start);
					String e =df.format(end);
					String day=GetWeekUtils.getWeek(rbDay);
					workProgram.setWorkStageTime(d+"("+day+")"+s+"至"+e);
					workProgram.setMeetingId(addr);
					BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);
					workProgramRepo.save(workProgram);
				}
			}
				
		}else{
			throw new IllegalArgumentException(String.format("会议名称：%s 已经存在，请重新输入", roomDto.getRbName()));
		}
		
	}
	

}
