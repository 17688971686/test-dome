package cs.service.meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.utils.StringUtil;
import cs.domain.meeting.MeetingRoom_;
import cs.domain.meeting.RoomBooking_;
import cs.domain.sys.Org_;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.CurrentUser;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBooking;
import cs.domain.sys.Company;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.sys.CompanyDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.meeting.MeetingRoomRepo;
@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

	private static Logger logger = Logger.getLogger(MeetingRoomServiceImpl.class);

	@Autowired
	private MeetingRoomRepo meetingRoomRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Override
	@Transactional
	public PageModelDto<MeetingRoomDto> get(ODataObj odataObj) {
		List<MeetingRoom> meetingList=	meetingRoomRepo.findByOdata(odataObj);
		List<MeetingRoomDto> meetingDtoList =new ArrayList<MeetingRoomDto>(meetingList.size());
		if(meetingList != null && meetingList.size() > 0){
			meetingList.forEach(x->{
				MeetingRoomDto  meetingDto = new MeetingRoomDto();
				BeanCopierUtils.copyProperties(x, meetingDto);
				meetingDto.setCreatedDate(x.getCreatedDate());
				meetingDto.setModifiedDate(x.getModifiedDate());
				meetingDtoList.add(meetingDto);
			});						
		}		
		PageModelDto<MeetingRoomDto> pageModelDto =new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(meetingDtoList);
		logger.info("查询会议室数据");
		return pageModelDto;
	}

	@Override
	@Transactional
	public void createMeeting(MeetingRoomDto meetingDto) {
		//判断会议室是否已经存在
		Criteria criteria = meetingRoomRepo.getExecutableCriteria();
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq(MeetingRoom_.mrName.getName(),  meetingDto.getMrName()));
		int count = Integer.parseInt(criteria.uniqueResult().toString());

		if(count == 0){
			MeetingRoom mr = new MeetingRoom();
			mr.setId(UUID.randomUUID().toString());
			mr.setAddr(meetingDto.getAddr());
			mr.setCapacity(meetingDto.getCapacity());
			mr.setMrName(meetingDto.getMrName());
			mr.setMrStatus(meetingDto.getMrStatus());
			mr.setMrType(meetingDto.getMrType());
			mr.setNum(meetingDto.getNum());
			mr.setUserName(meetingDto.getUserName());
			mr.setUserPhone(meetingDto.getUserPhone());
			mr.setRemark(meetingDto.getRemark());

			//创建者
			mr.setCreatedBy(currentUser.getLoginName());
			mr.setModifiedBy(currentUser.getLoginName());
			meetingRoomRepo.save(mr);
			logger.info(String.format("创建会议室，会议名:%s", meetingDto.getMrName()));
		}else{
			throw new IllegalArgumentException(String.format("会议室名称：%s 已经存在，请重新输入", meetingDto.getMrName()));
		}
	}

	@Override
	@Transactional
	public void deleteMeeting(String id) {
	    List<String> ids = StringUtil.getSplit(id,",");
        Criteria criteria = roomBookingRepo.getExecutableCriteria();
        criteria.setProjection(Projections.rowCount());
        if(ids.size() == 1){
            criteria.add(Restrictions.eq(RoomBooking_.mrID.getName(),ids.get(0)));
        }else{
            criteria.add(Restrictions.in(RoomBooking_.mrID.getName(), ids));
        }
        int count = Integer.parseInt(criteria.uniqueResult().toString());
	    //判断有没有预定的信息，有则不给删除
        if(count == 0){
            meetingRoomRepo.deleteById(MeetingRoom_.id.getName(),id);
        }else{
            throw new IllegalArgumentException("会议室存在预定信息，请清除之后再执行删除操作！");
        }

	}

	@Override
	@Transactional
	public void updateMeeting(MeetingRoomDto meetingDto) {
		MeetingRoom  meeting =  meetingRoomRepo.findById(meetingDto.getId());
		meeting.setAddr(meetingDto.getAddr());
		meeting.setCapacity(meetingDto.getCapacity());
		meeting.setMrName(meetingDto.getMrName());
		meeting.setMrStatus(meetingDto.getMrStatus());
		meeting.setMrType(meetingDto.getMrType());
		meeting.setNum(meetingDto.getNum());
		meeting.setUserName(meetingDto.getUserName());
		meeting.setUserPhone(meetingDto.getUserPhone());
		meeting.setRemark(meetingDto.getRemark());
		meeting.setModifiedBy(currentUser.getLoginName());
		meetingRoomRepo.save(meeting);
		logger.info(String.format("更新会议室,会议室名:%s", meetingDto.getMrName()));
	}
	
	//更新会议室使用状态
	@Override
	@Transactional
	public void roomUseState(MeetingRoomDto meetingDto) {
		Date now=new Date();
		MeetingRoom  meeting =  meetingRoomRepo.findById(meetingDto.getId());
		meeting.setMrStatus(meetingDto.getMrStatus());
		meeting.setModifiedBy(currentUser.getLoginName());
		meeting.setModifiedDate(now);
		meetingRoomRepo.save(meeting);
		logger.info(String.format("更新会议室使用状态,会议室名:%s", meetingDto.getMrName()));
	}

	@Override
	public MeetingRoomDto findByIdMeeting(String id) {
		MeetingRoom meeting =	meetingRoomRepo.findById(id);
		MeetingRoomDto meetingDto = new MeetingRoomDto();
		BeanCopierUtils.copyProperties(meeting, meetingDto);
		return meetingDto;
	}
	
	

}
