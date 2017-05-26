package cs.service.meeting;

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

import cs.common.CurrentUser;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.meeting.MeetingRoom;
import cs.domain.sys.Company;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.sys.CompanyDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.meeting.MeetingRoomRepo;
@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

	private static Logger logger = Logger.getLogger(MeetingRoomServiceImpl.class);

	@Autowired
	private MeetingRoomRepo meetingRoomRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Override
	@Transactional
	public PageModelDto<MeetingRoomDto> get(ODataObj odataObj) {
		List<MeetingRoom> meetingList=	meetingRoomRepo.findByOdata(odataObj);
		List<MeetingRoomDto> meetingDtoList =new ArrayList<>();
		for( MeetingRoom item : meetingList){
			
			MeetingRoomDto mrd =new MeetingRoomDto();
			
			mrd.setId(item.getId());
			mrd.setAddr(item.getAddr());
			mrd.setMrName(item.getMrName());
			mrd.setCapacity(item.getCapacity());
			mrd.setCreateDate(item.getCreateDate());
			mrd.setMrStatus(item.getMrStatus());
			mrd.setMrType(item.getMrType());
			mrd.setNum(item.getNum());
			mrd.setUserName(item.getUserName());
			mrd.setUserPhone(item.getUserPhone());
			mrd.setRemark(item.getRemark());
			meetingDtoList.add(mrd);
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
		Criteria criteria =meetingRoomRepo.getSession().createCriteria(MeetingRoom.class);
		criteria.add(Restrictions.eq("mrName", meetingDto.getMrName()));
		List<MeetingRoom> mrs = criteria.list();
		if(mrs.isEmpty()){
			
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
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mr.setCreateDate(meetingDto.getCreateDate());//
			mr.setCreateDate(sdf.format(new Date()));
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
		MeetingRoom meeting =	meetingRoomRepo.findById(id);
		if(meeting !=null){
			
			meetingRoomRepo.delete(meeting);
			logger.info(String.format("删除会议室, 会议室mrName:%s", meeting.getMrName()));
		}
	
	}

	@Override
	@Transactional
	public void deleteMeeting(String[] ids) {

		for(String id : ids){
			this.deleteMeeting(id);
		}
		logger.info("批量删除会议室");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		meeting.setCreateDate(meetingDto.getCreateDate());//
		meeting.setModifiedBy(currentUser.getLoginName());
		meetingRoomRepo.save(meeting);
		
		logger.info(String.format("更新会议室,会议室名:%s", meetingDto.getMrName()));
	}

	@Override
	public MeetingRoomDto findByIdMeeting(String id) {
		MeetingRoom meeting =	meetingRoomRepo.findById(id);
		MeetingRoomDto meetingDto = new MeetingRoomDto();
		BeanCopierUtils.copyProperties(meeting, meetingDto);
		return meetingDto;
	}
	
	

}
