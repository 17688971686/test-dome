package cs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.domain.RoomBooking;
import cs.model.PageModelDto;
import cs.model.RoomBookingDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.RoomBookingRepo;

@Service
public class RoomBookingSerivceImpl implements RoomBookingSerivce{

	private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private RoomBookingRepo roomBookingRepo;
	@Override
	@Transactional
	public List<RoomBooking> getList(ODataObj oDataObj) {
		List<RoomBooking> roomList=	roomBookingRepo.findByOdata(oDataObj);
		return roomList;
	}
	@Override
	@Transactional
	public PageModelDto<RoomBookingDto> get(ODataObj odataObj) {
		
		List<RoomBooking> roomList=	roomBookingRepo.findByOdata(odataObj);
		List<RoomBookingDto> roomDtoList = new ArrayList<>();
		for(RoomBooking item : roomList){
			
			RoomBookingDto  roomDto = new RoomBookingDto();
			
			roomDto.setId(UUID.randomUUID().toString());
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
	public void createRoom(RoomBookingDto roomBookingDto) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateRoom(RoomBookingDto roomBookingDto) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteRoom(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteRooms(String[] ids) {
		// TODO Auto-generated method stub
		
	}

	

	

}
