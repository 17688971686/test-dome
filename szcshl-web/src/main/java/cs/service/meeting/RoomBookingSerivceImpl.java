package cs.service.meeting;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.GetWeekUtils;
import cs.common.utils.Validate;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.WorkProgram;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.meeting.MeetingRoomRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.service.sys.RoleServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

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
        HqlBuilder hql= HqlBuilder.create();
        hql.append(" from "+RoomBooking.class.getSimpleName()+" where "+RoomBooking_.rbDay.getName());
        hql.append(" between rbDay").setParam("rbDay", monday);
        hql.append(" and :rbDay").setParam("rbDay", sunday);
        List<RoomBooking> roomlist =roomBookingRepo.findByHql(hql);
		return roomlist;
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
	public List<MeetingRoomDto> findMeetingAll() {
		List<MeetingRoom> meeting	= meetingRoomRepo.findAll();
		List<MeetingRoomDto> meetingDtos = new ArrayList<>();
		if(meeting !=null && meeting.size() >0){
			meeting.forEach(x->{
				MeetingRoomDto meetingDto = new MeetingRoomDto();
				BeanCopierUtils.copyProperties(x, meetingDto);
				meetingDto.setCreatedDate(x.getCreatedDate());
				meetingDto.setModifiedDate(x.getModifiedDate());
				meetingDtos.add(meetingDto);
			});
		}
		return meetingDtos;
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
				RoomBookingDto roomDto = new RoomBookingDto();
				BeanCopierUtils.copyProperties(x, roomDto);
                roomDtoList.add(roomDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(roomDtoList);
		return pageModelDto;
	}

	protected  int checkRootBook(RoomBookingDto roomBookingDto){
        //判断会议时间是否冲突
        HqlBuilder sqlBuilder= HqlBuilder.create();
        sqlBuilder.append(" select count(ID) from CS_ROOM_BOOKING where ("+RoomBooking_.beginTime.getName());
        sqlBuilder.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
        sqlBuilder.append(" and :endTime").setParam("endTime", roomBookingDto.getEndTime());
        sqlBuilder.append(" or "+RoomBooking_.endTime.getName());
        sqlBuilder.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
        sqlBuilder.append(" and :endTime )").setParam("endTime", roomBookingDto.getEndTime());
        sqlBuilder.append(" and "+RoomBooking_.mrID.getName()+" =:mrId").setParam("mrId",roomBookingDto.getMrID());
        //排除本身
        if(Validate.isString(roomBookingDto.getId())){
            sqlBuilder.append(" and ID <> :id").setParam("id",roomBookingDto.getId());
        }
        return roomBookingRepo.countBySql(sqlBuilder);
    }

	@Override
	@Transactional
	public void createRoom(RoomBookingDto roomBookingDto) {
		if(checkRootBook(roomBookingDto) > 0){
			throw new IllegalArgumentException(String.format("%s 会议室安排时间冲突!!!", roomBookingDto.getBeginTime()));
		}else{
			RoomBooking rb = new RoomBooking();
			BeanCopierUtils.copyProperties(roomBookingDto, rb);
			rb.setId(UUID.randomUUID().toString());
			Date days = roomBookingDto.getRbDay();
			String strdate = DateUtils.toStringDay(days);
			String day=GetWeekUtils.getWeek(days);
			rb.setRbDate(strdate+"("+day+")");//星期几
			Date now = new Date();
			rb.setCreatedDate(now);
			rb.setModifiedDate(now);
			rb.setCreatedBy(currentUser.getLoginName());
			rb.setModifiedBy(currentUser.getLoginName());
			roomBookingRepo.save(rb);
		}
		
	}
	
	@Override
	@Transactional
	public void updateRoom(RoomBookingDto roomBookingDto) {
		if(checkRootBook(roomBookingDto) > 0){
			throw new IllegalArgumentException(String.format("%s 会议室安排时间冲突!!!", roomBookingDto.getBeginTime()));
		}else{
            RoomBooking room =  roomBookingRepo.findById(roomBookingDto.getId());
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
		RoomBooking room = 	roomBookingRepo.getById(id);
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
        if(checkRootBook(roomDto) > 0){
            throw new IllegalArgumentException(String.format("%s 会议室安排时间冲突!!!", roomDto.getBeginTime()));
        }else{
            RoomBooking rb = new RoomBooking();
            BeanCopierUtils.copyProperties(roomDto, rb);
            String strdate = DateUtils.toStringDay(roomDto.getRbDay());
            String stageday=GetWeekUtils.getWeek(roomDto.getRbDay());
            rb.setRbDate(strdate+"("+stageday+")");//星期几
            String stageProject = roomDto.getStageProject();
            rb.setStageProject(stageProject+"("+strdate+"("+stageday+")"+")");

            Date now = new Date();
            rb.setCreatedDate(now);
            rb.setModifiedDate(now);
            rb.setCreatedBy(currentUser.getLoginName());
            rb.setModifiedBy(currentUser.getLoginName());
            roomBookingRepo.save(rb);
            WorkProgram workProgram =workProgramRepo.findById(roomDto.getWorkProgramId());
            if(workProgram != null){
                String d = DateUtils.toStringDay(roomDto.getRbDay());
                String day = GetWeekUtils.getWeek(roomDto.getRbDay());
                workProgram.setWorkStageTime(d+"("+day+")"+roomDto.getBeginTimeStr()+"至"+roomDto.getEndTimeStr());
                workProgram.setMeetingId(roomDto.getMrID());
                BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);
                workProgramRepo.save(workProgram);
            }
        }
	}
}
