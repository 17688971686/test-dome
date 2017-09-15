package cs.service.meeting;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.ExpertReview;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.domain.sys.SysFile;
import cs.domain.topic.WorkPlan;
import cs.domain.topic.WorkPlan_;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.WorkProgramDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.meeting.MeetingRoomRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.topic.WorkPlanRepo;
import cs.service.sys.RoleServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RoomBookingSerivceImpl implements RoomBookingSerivce{

	private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
	@Autowired
	private RoomBookingRepo roomBookingRepo;
	@Autowired
	private MeetingRoomRepo meetingRoomRepo;
	@Autowired
	private WorkProgramRepo workProgramRepo;
    @Autowired
    private WorkPlanRepo workPlanRepo;
	@Autowired
	private SysFileRepo sysFileRepo;
	@Autowired
	private ExpertReviewRepo expertReviewRepo;

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

	/**
	 * 导出本周评审会议安排
	 */
	@Override
	public void exportThisWeekStage() {
		Calendar cal =Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//获取本周一到周五的日期
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date  monday=cal.getTime();
		String MONDAY =df.format(monday);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		Date tuseday = cal.getTime();
		String TUESDAY = df.format(tuseday);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		Date wednesday =cal.getTime();
		String WEDNESDAY = df.format(wednesday);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		Date thursday = cal.getTime();
		String THURSDAY = df.format(thursday);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		Date friday = cal.getTime();
		String FRIDAT = df.format(friday);

		String path =SysFileUtil.getUploadPath();//文件路劲
		List<SysFile> sysfile = new ArrayList<>();
		String showName = "";
		String relativeFileUrl = "";
		String stageProject  = "";
		String roomId = "";
		File docFile = null;
		List<RoomBooking> rb= roomBookingRepo.findWeekBook();
		List<RoomBookingDto> roomDtos = new ArrayList<>();
		if(rb !=null && rb.size() >0){
			rb.forEach(x->{
				RoomBookingDto roomDto = new RoomBookingDto();
				BeanCopierUtils.copyProperties(x, roomDto);
				roomDto.setCreatedDate(x.getCreatedDate());
				roomDto.setModifiedDate(x.getModifiedDate());
				roomDtos.add(roomDto);
			});
		}
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("MONDAY",MONDAY);
		dataMap.put("TUESDAY",TUESDAY);
		dataMap.put("WEDNESDAY",WEDNESDAY);
		dataMap.put("THURSDAY",THURSDAY);
		dataMap.put("FRIDAT",FRIDAT);
		dataMap.put("rbNamelist",roomDtos);

		showName = Constant.Template.THIS_STAGE_MEETING.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
		//String fileLocation,String mainType,String mainId, String sysBusiType, String fileName
		relativeFileUrl = SysFileUtil.generatRelativeUrl(path, Constant.SysFileType.MEETTINGROOM.getValue(), roomId,null,showName);
		String pathFile = path + File.separator + relativeFileUrl;
		docFile =  TemplateUtil.createDoc(dataMap, Constant.Template.THIS_STAGE_MEETING.getKey(),pathFile);
		if(docFile != null){
			sysfile.add(new SysFile(UUID.randomUUID().toString(),UUID.randomUUID().toString(),relativeFileUrl,showName,
					Integer.valueOf(String.valueOf(docFile.length())),Constant.Template.OUTPUT_SUFFIX.getKey(),
					null,roomId,Constant.SysFileType.STAGEMEETING.getValue(), Constant.SysFileType.MEETING.getValue()));
		}
		if(sysfile.size() > 0){

			Date now = new Date();
			sysfile.forEach(sf->{
				sf.setCreatedDate(now);
				sf.setModifiedDate(now);
				sf.setCreatedBy(SessionUtil.getLoginName());
				sf.setModifiedBy(SessionUtil.getLoginName());
			});
			sysFileRepo.bathUpdate(sysfile);
		}

	}
	/**
	 * 导出下周评审会安排
	 */
	@Override
	public void exportNextWeekStage() {

		Calendar cal =Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		//增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		//星期一
		cal.add(Calendar.DAY_OF_WEEK, 1);
		Date nextMonday=cal.getTime();
		String MONDAY = df.format(nextMonday);
		//星期二
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		Date nextTuesday = cal.getTime();
		String TUESDAY = df.format(nextTuesday);
		//星期三
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		Date nextWednesday = cal.getTime();
		String WEDNESDAY =df.format(nextWednesday);
		//星期四
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		Date nextThursday = cal.getTime();
		String THURSDAY =   df.format(nextThursday);
		//星期五
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		Date nextFriday = cal.getTime();
		String FRIDAY = df.format(nextFriday);
		//星期六
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		Date nextSatday = cal.getTime();
		//获取下周星期日
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		Date nextSunday=cal.getTime();

		String path =SysFileUtil.getUploadPath();//文件路劲
		List<SysFile> sysfile = new ArrayList<>();
		String showName = "";
		String relativeFileUrl = "";
		String stageProject  = "";
		String roomId = "";
		File docFile = null;
		List<SysFile> saveFile = new ArrayList<>();
		List<RoomBookingDto> room =roomBookingRepo.findStageNextWeek();
		Map<String,Object> dataMap = new HashMap<>();

		dataMap.put("MONDAY",MONDAY);
		dataMap.put("TUESDAY",TUESDAY);
		dataMap.put("WEDNESDAY",WEDNESDAY);
		dataMap.put("THURSDAY",THURSDAY);
		dataMap.put("FRIDAY",FRIDAY);
		dataMap.put("roomlist",room);

		showName = Constant.Template.NEXT_STAGE_MEETING.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        relativeFileUrl = SysFileUtil.generatRelativeUrl(path, Constant.SysFileType.MEETTINGROOM.getValue(), roomId,null,showName);
        docFile =  TemplateUtil.createDoc(dataMap, Constant.Template.NEXT_STAGE_MEETING.getKey(), path+File.separator +relativeFileUrl);

		if(docFile !=null){
			sysfile.add(new SysFile(UUID.randomUUID().toString(),roomId,relativeFileUrl,showName,
					Integer.valueOf(String.valueOf(docFile.length())),Constant.Template.OUTPUT_SUFFIX.getKey(),
					null,roomId,Constant.SysFileType.STAGEMEETING.getValue(), Constant.SysFileType.MEETING.getValue()));
		}

		if(saveFile.size() >0){
			Date now = new Date();
			saveFile.forEach(sfile ->{
				sfile.setCreatedDate(now);
				sfile.setModifiedDate(now);
				sfile.setCreatedBy(SessionUtil.getLoginName());
				sfile.setModifiedBy(SessionUtil.getLoginName());
			});
		}
		sysFileRepo.bathUpdate(saveFile);

	}

	//本周全部会议安排
	@Override
	@Transactional
	public List<RoomBooking> findWeek() {
		List<RoomBooking> rb= roomBookingRepo.findWeekBook();
		return rb;
	}


	//导出下周全部会议安排
	@Override
	@Transactional
	public List<RoomBookingDto> findNextWeek() {
		List<RoomBookingDto> room =roomBookingRepo.findNextWeek();
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
		sqlBuilder.append(" select count(ID) from CS_ROOM_BOOKING where " + RoomBooking_.mrID.getName()+" =:mrId");
		sqlBuilder.setParam("mrId",roomBookingDto.getMrID()) ;
		//排除本身
		if(Validate.isString(roomBookingDto.getId())){
			sqlBuilder.append(" and ID <> :id").setParam("id",roomBookingDto.getId());
		}
		sqlBuilder.append(" and("+RoomBooking_.beginTime.getName());
		sqlBuilder.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
		sqlBuilder.append(" and :endTime").setParam("endTime", roomBookingDto.getEndTime());
		sqlBuilder.append(" or "+RoomBooking_.endTime.getName());
		sqlBuilder.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
		sqlBuilder.append(" and :endTime )").setParam("endTime", roomBookingDto.getEndTime());

		return roomBookingRepo.returnIntBySql(sqlBuilder);
	}

	@Override
	@Transactional
	public void deleteRoom(String id) {
		roomBookingRepo.deleteById(RoomBooking_.id.getName(),id);
	}

	/**
	 * 保存会议预定信息
	 * @param roomDto
	 * @return
	 */
	@Override
	@Transactional
	public ResultMsg saveRoom(RoomBookingDto roomDto) {
		if(checkRootBook(roomDto) > 0){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"预定的会议室时间跟其他会议有冲突！");
		}else{
			Date now = new Date();
			RoomBooking rb = new RoomBooking();
			if(Validate.isString(roomDto.getId())){
				rb = roomBookingRepo.getById(roomDto.getId());
				BeanCopierUtils.copyPropertiesIgnoreNull(roomDto,rb);
			}else{
				BeanCopierUtils.copyProperties(roomDto, rb);
				rb.setId(UUID.randomUUID().toString());
                rb.setCreatedBy(SessionUtil.getDisplayName());
                rb.setCreatedDate(now);
			}
			MeetingRoom meeting= meetingRoomRepo.findById(roomDto.getMrID());
			rb.setAddressName(meeting.getAddr());
			String strdate = DateUtils.toStringDay(roomDto.getRbDay());
			String stageday = GetWeekUtils.getWeek(roomDto.getRbDay());
			rb.setRbDate(strdate+"("+stageday+")");//星期几
			String stageProject = roomDto.getStageProject();
			rb.setStageProject(stageProject+"("+strdate+"("+stageday+")"+")");
            rb.setModifiedDate(now);
            rb.setModifiedBy(SessionUtil.getDisplayName());
			roomBookingRepo.save(rb);

			//根据业务类型，更新专家评审会事件
            if(Validate.isString(rb.getBusinessId()) && Validate.isString(rb.getBusinessType())){
                expertReviewRepo.updateReviewDate(rb.getBusinessId(),rb.getBusinessType(),rb.getRbDay());
            }
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！",rb);
		}
	}

	/**
	 * 导出本周评审会议安排
	 */
	@Override
	public void exportRoom(String date,String rbType,String mrId) {
		String [] dates=date.split("-");
		String start=dates[0].replace("/", "-");
		String end=dates[1].replaceAll("/", "-");
		List<Map> roomBookMap=findWeekRoom(start,rbType,mrId);
		if("0".equals(rbType)){
			rbType="评审会";
		}
		if("1".equals(rbType)){
			rbType="全部会";
		}
		List<List<String>> rbNameList=new ArrayList<>();
		//		int j=0;
		for(int i=0;i<13;i++){
			List<String> rbNames=new ArrayList<>();
			for(int j=0;j<roomBookMap.size();j++){
				Object obj=roomBookMap.get(j);
				Object[] objArr= (Object[]) obj;
				if((String) objArr[1]!=null){
					String[] str=((String) objArr[1]).split(",");
					if(i<=str.length-1){
						rbNames.add(str[i]);
					}else{
						rbNames.add("");
//						i=0;
					}
				}else{
					rbNames.add("");
				}
			}
			rbNameList.add(rbNames);
		}

		List<String> timeList=new ArrayList<String>();
		timeList.add("星期一");
		timeList.add("星期二");
		timeList.add("星期三");
		timeList.add("星期四");
		timeList.add("星期五");
		timeList.add("星期六");
		timeList.add("星期日");

		String path =SysFileUtil.getUploadPath();//文件路劲
		List<SysFile> sysfile = new ArrayList<>();
		String showName = "";
		String relativeFileUrl = "";
		String stageProject  = "";
		String roomId = "";
		File docFile = null;
		List<RoomBooking> rb= roomBookingRepo.findWeekBook();
		List<RoomBookingDto> roomDtos = new ArrayList<>();
		if(rb !=null && rb.size() >0){
			rb.forEach(x->{
				RoomBookingDto roomDto = new RoomBookingDto();
				BeanCopierUtils.copyProperties(x, roomDto);
				roomDto.setCreatedDate(x.getCreatedDate());
				roomDto.setModifiedDate(x.getModifiedDate());
				roomDtos.add(roomDto);
			});
		}
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("timeList", timeList);
		dataMap.put("TITLE",rbType);
		dataMap.put("START",start);
		dataMap.put("END",end);
		dataMap.put("contentList",rbNameList);
		showName = Constant.Template.EXPORTROOM.getValue()+Constant.Template.OUTPUT_SUFFIX.getKey();
        relativeFileUrl = SysFileUtil.generatRelativeUrl(path, Constant.SysFileType.MEETTINGROOM.getValue(), roomId,null,showName);
        String pathFile = path + File.separator + relativeFileUrl;
		docFile =  TemplateUtil.createDoc(dataMap, Constant.Template.EXPORTROOM.getKey(),pathFile);
		if(docFile != null){
			sysfile.add(new SysFile(UUID.randomUUID().toString(),UUID.randomUUID().toString(),relativeFileUrl,showName,
					Integer.valueOf(String.valueOf(docFile.length())),Constant.Template.OUTPUT_SUFFIX.getKey(),
					null,roomId,Constant.SysFileType.STAGEMEETING.getValue(), Constant.SysFileType.MEETING.getValue()));
		}
		if(sysfile.size() > 0){

			Date now = new Date();
			sysfile.forEach(sf->{
				sf.setCreatedDate(now);
				sf.setModifiedDate(now);
				sf.setCreatedBy(SessionUtil.getLoginName());
				sf.setModifiedBy(SessionUtil.getLoginName());
			});
			sysFileRepo.bathUpdate(sysfile);
		}

	}

	/* 
	 * 获取一周每天的会议安排信息
	 */
	@Override
	public List<Map> findWeekRoom(String date,String rbType,String mrId) {
		HqlBuilder sqlBuilder=HqlBuilder.create();
		sqlBuilder.append("select dd.wDate,rbdd.rbName from (select trunc(to_date('"+date+"','yyyy-mm-dd'),'iw') as wdate from dual union ");
		sqlBuilder.append(" select trunc (to_date(:dates,'yyyy-mm-dd'),'iw') +1 as wDate from dual union ");
		sqlBuilder.append(" select trunc (to_date(:dates,'yyyy-mm-dd'),'iw') +2 as wDate from dual union ");
		sqlBuilder.append(" select trunc (to_date(:dates,'yyyy-mm-dd'),'iw') +3 as wDate from dual union ");
		sqlBuilder.append(" select trunc (to_date(:dates,'yyyy-mm-dd'),'iw') +4 as wDate from dual union ");
		sqlBuilder.append(" select trunc (to_date(:dates,'yyyy-mm-dd'),'iw') +5 as wDate from dual union ");
		sqlBuilder.append(" select trunc (to_date(:dates,'yyyy-mm-dd'),'iw') +6 as wDate from dual ) dd  ");
		sqlBuilder.append(" left join ( select rbd.rbday,wm_concat(rbd.rbName) rbName from (");
		sqlBuilder.append("select rb."+RoomBooking_.rbName.getName()+",rb."+ RoomBooking_.rbDay.getName()+" from CS_ROOM_BOOKING rb");
		sqlBuilder.append(" where "+RoomBooking_.rbDay.getName()+" > (trunc(to_date(:dates,'yyyy-mm-dd'),'iw')-1)");
		sqlBuilder.append(" and "+RoomBooking_.rbDay.getName()+" < (trunc (to_date(:dates,'yyyy-mm-dd'),'iw')+7) ");
		sqlBuilder.append(" and "+RoomBooking_.mrID.getName()+"=:mrId ");
		if("0".equals(rbType)){
			sqlBuilder.append(" and workProgramId is not null");
		}
		/*if("1".equals(rbType)){
			sqlBuilder.append(" and workProgramId is null");
		}*/
		sqlBuilder.append(") rbd ");
		sqlBuilder.append("group by rbd."+RoomBooking_.rbDay.getName()+") rbdd on rbdd.rbday = dd.wDate order by dd.wDate");
		sqlBuilder.setParam("dates", date);
		sqlBuilder.setParam("mrId", mrId);
		List<Map> roomMap=roomBookingRepo.findMapListBySql(sqlBuilder);
		return roomMap;
	}

    /**
     * 根据业务ID和业务类型初始化会议室预定的值
     * @param businessId
     * @param businessType
     * @return
     */
    @Override
    public RoomBookingDto initDefaultValue(String businessId, String businessType) {
        RoomBookingDto roomBookingDto = new RoomBookingDto();
        if(Constant.BusinessType.SIGN_WP.getValue().equals(businessType)){
            WorkProgram wp = workProgramRepo.findById(WorkProgram_.id.getName(),businessId);
            roomBookingDto.setStageOrgName(wp.getReviewOrgName());
            roomBookingDto.setRbName(wp.getProjectName());
        }else if(Constant.BusinessType.TOPIC.getValue().equals(businessType)){
            WorkPlan wp = workPlanRepo.findById(WorkPlan_.id.getName(),businessId);
            roomBookingDto.setStageOrgName(wp.getTopicName());
        }
        roomBookingDto.setHost(SessionUtil.getDisplayName());
        roomBookingDto.setDueToPeople(SessionUtil.getDisplayName());
        if(Validate.isString(businessId)){
            roomBookingDto.setBusinessId(businessId);
        }
        if(Validate.isString(businessType)){
            roomBookingDto.setBusinessType(businessType);
        }
        roomBookingDto.setBeginTime(null);
        roomBookingDto.setEndTime(null);
        roomBookingDto.setRbDate(null);
        return roomBookingDto;
    }

}
