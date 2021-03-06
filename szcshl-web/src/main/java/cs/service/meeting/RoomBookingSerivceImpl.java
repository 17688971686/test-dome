package cs.service.meeting;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.constants.ProjectConstant;
import cs.common.utils.*;
import cs.domain.meeting.MeetingRoom;
import cs.domain.meeting.MeetingRoom_;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.domain.topic.WorkPlan;
import cs.domain.topic.WorkPlan_;
import cs.model.PageModelDto;
import cs.model.meeting.MeetingRoomDto;
import cs.model.meeting.RoomBookingDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.meeting.MeetingRoomRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.topic.WorkPlanRepo;
import cs.service.sys.RoleServiceImpl;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

@Service
public class RoomBookingSerivceImpl implements RoomBookingSerivce {

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
    private MeetingRoomService meetingRoomService;

    @Override
    public List<RoomBookingDto> getRoomList() {
        List<RoomBooking> roomList = roomBookingRepo.findAll();
        List<RoomBookingDto> roomDtos = new ArrayList<>();
        if (roomList != null && roomList.size() > 0) {
            roomList.forEach(x -> {
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
     *//*
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

		showName = Constant.Template.THIS_STAGE_MEETING.getValue()+Constant.Template.WORD_SUFFIX.getKey();
		//String fileLocation,String mainType,String mainId, String sysBusiType, String fileName
		relativeFileUrl = SysFileUtil.generatRelativeUrl(path, Constant.SysFileType.MEETTINGROOM.getValue(), roomId,null,showName);
		String pathFile = path + File.separator + relativeFileUrl;
		docFile =  TemplateUtil.createDoc(dataMap, Constant.Template.THIS_STAGE_MEETING.getKey(),pathFile);
		*//*if(docFile != null){
			sysfile.add(new SysFile(UUID.randomUUID().toString(),UUID.randomUUID().toString(),relativeFileUrl,showName,
					Integer.valueOf(String.valueOf(docFile.length())),Constant.Template.WORD_SUFFIX.getKey(),
					null,roomId,Constant.SysFileType.STAGEMEETING.getValue(), Constant.SysFileType.MEETING.getValue()));
		}*//*
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

	}*/

    /**
     * 导出下周评审会安排
     */
    /*@Override
    public void exportNextWeekStage() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //星期一
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextMonday = cal.getTime();
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
        String WEDNESDAY = df.format(nextWednesday);
        //星期四
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date nextThursday = cal.getTime();
        String THURSDAY = df.format(nextThursday);
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
        Date nextSunday = cal.getTime();

        //文件路径
        String path = SysFileUtil.getUploadPath();
        List<SysFile> sysfile = new ArrayList<>();
        String showName = "";
        String relativeFileUrl = "";
        String stageProject = "";
        String roomId = "";
        File docFile = null;
        List<SysFile> saveFile = new ArrayList<>();
        List<RoomBookingDto> room = roomBookingRepo.findStageNextWeek();
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("MONDAY", MONDAY);
        dataMap.put("TUESDAY", TUESDAY);
        dataMap.put("WEDNESDAY", WEDNESDAY);
        dataMap.put("THURSDAY", THURSDAY);
        dataMap.put("FRIDAY", FRIDAY);
        dataMap.put("roomlist", room);

        showName = Constant.Template.NEXT_STAGE_MEETING.getValue() + Constant.Template.WORD_SUFFIX.getKey();
        relativeFileUrl = SysFileUtil.generatRelativeUrl(path, Constant.SysFileType.MEETTINGROOM.getValue(), roomId, null, showName);
        docFile = TemplateUtil.createDoc(dataMap, Constant.Template.NEXT_STAGE_MEETING.getKey(), path + File.separator + relativeFileUrl);

		*//*if(docFile !=null){
			sysfile.add(new SysFile(UUID.randomUUID().toString(),roomId,relativeFileUrl,showName,
					Integer.valueOf(String.valueOf(docFile.length())),Constant.Template.WORD_SUFFIX.getKey(),
					null,roomId,Constant.SysFileType.STAGEMEETING.getValue(), Constant.SysFileType.MEETING.getValue()));
		}*//*

        if (saveFile.size() > 0) {
            Date now = new Date();
            saveFile.forEach(sfile -> {
                sfile.setCreatedDate(now);
                sfile.setModifiedDate(now);
                sfile.setCreatedBy(SessionUtil.getLoginName());
                sfile.setModifiedBy(SessionUtil.getLoginName());
            });
        }
        sysFileRepo.bathUpdate(saveFile);

    }*/

	/*//本周全部会议安排
	@Override
	@Transactional
	public List<RoomBooking> findWeek() {
		List<RoomBooking> rb= roomBookingRepo.findWeekBook();
		return rb;
	}*/


	/*//导出下周全部会议安排
	@Override
	@Transactional
	public List<RoomBookingDto> findNextWeek() {
		List<RoomBookingDto> room =roomBookingRepo.findNextWeek();
		return room;
	}*/

    @Override
    public List<MeetingRoomDto> findMeetingAll() {
        return meetingRoomService.findAll();
    }

    /* (non-Javadoc)
     * @see cs.service.meeting.RoomBookingSerivce#get(cs.repository.odata.ODataObj)
     */
    @Override
    public PageModelDto<RoomBookingDto> get(ODataObj odataObj) {
        PageModelDto<RoomBookingDto> pageModelDto = new PageModelDto<RoomBookingDto>();
        List<RoomBookingDto> roomDtoList = new ArrayList<>();
        List<RoomBooking> roomList = roomBookingRepo.findByOdata(odataObj);
        if (Validate.isList(roomList)) {
            roomList.forEach(x -> {
                RoomBookingDto roomDto = new RoomBookingDto();
                BeanCopierUtils.copyProperties(x, roomDto);
                roomDtoList.add(roomDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(roomDtoList);
        return pageModelDto;
    }

    protected int checkRootBook(RoomBookingDto roomBookingDto) {
        //判断会议时间是否冲突
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(ID) from CS_ROOM_BOOKING where " + RoomBooking_.mrID.getName() + " =:mrId");
        sqlBuilder.setParam("mrId", roomBookingDto.getMrID());
        //排除本身
        if (Validate.isString(roomBookingDto.getId())) {
            sqlBuilder.append(" and ID <> :id").setParam("id", roomBookingDto.getId());
        }
        sqlBuilder.append(" and(" + RoomBooking_.beginTime.getName());
        sqlBuilder.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
        sqlBuilder.append(" and :endTime").setParam("endTime", roomBookingDto.getEndTime());
        sqlBuilder.append(" or " + RoomBooking_.endTime.getName());
        sqlBuilder.append(" between :beginTime").setParam("beginTime", roomBookingDto.getBeginTime());
        sqlBuilder.append(" and :endTime )").setParam("endTime", roomBookingDto.getEndTime());

        return roomBookingRepo.returnIntBySql(sqlBuilder);
    }

    /**
     * 删除会议室
     * 1、普通预定的会议室，没到会议日期前，可以修改和删除
     * 2、业务预定的会议室，没提交审核之前（不管有没有到会议日期），可以修改和删除。
     * 3、提交审核的会议室，不可删除，admin可以修改，其他人不可修改和删除。
     * 4、审核通过的会议室，不可修改和删除（不管有没有到会议日期也不管用户是谁）。
     *
     * @param id
     * @param dueToPeople
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteRoom(String id, String dueToPeople) {
        RoomBooking roomBooking = roomBookingRepo.findById(RoomBooking_.id.getName(), id);
        if (roomBooking == null) {
            return ResultMsg.error("该会议室预定信息已被删除！");
        } else {
            boolean isSuper = SessionUtil.getLoginName().equals(SUPER_ACCOUNT);
            if(isSuper){
                roomBookingRepo.delete(roomBooking);
                return ResultMsg.ok( "操作成功！");
            }else{
                //业务预定的会议室
                if (Validate.isString(roomBooking.getBusinessId())) {
                    if (Constant.EnumState.YES.getValue().equals(roomBooking.getRbStatus()) || Constant.EnumState.PROCESS.getValue().equals(roomBooking.getRbStatus())) {
                        return ResultMsg.error( "会议室已经提交审核，不能进行删除操作！");
                    }
                    if (Constant.EnumState.NO.getValue().equals(roomBooking.getRbStatus())) {
                        if (SessionUtil.getUserId().equals(roomBooking.getCreatedBy())) {
                            roomBookingRepo.delete(roomBooking);
                            return ResultMsg.ok( "操作成功！");
                        } else {
                            return ResultMsg.error("操作失败，您没有删除权限！");
                        }
                    }
                    //旧数据默认不可删除
                    return ResultMsg.error( "预定的会议室不可删除！");
                    //普通预定的会议室
                } else {
                    if ((new Date()).after(roomBooking.getBeginTime()) || Constant.EnumState.YES.getValue().equals(roomBooking.getRbStatus())) {
                        roomBooking.setRbStatus(Constant.EnumState.YES.getValue());
                        roomBookingRepo.save(roomBooking);
                        return ResultMsg.error( "操作失败，只能在预定会议的时间段之前进行删除操作！");
                    }
                    if (SessionUtil.getUserId().equals(roomBooking.getCreatedBy())) {
                        roomBookingRepo.delete(roomBooking);
                        return ResultMsg.ok("操作成功！");
                    } else {
                        return ResultMsg.error("您没有该权限，删除失败！");
                    }
                }
            }
        }
    }

    /**
     * 保存会议预定信息
     *
     * @param roomDto 1、普通预定的会议室，没到会议日期前，可以修改和删除
     *                2、业务预定的会议室，没提交审核之前（不管有没有到会议日期），可以修改和删除。
     *                3、提交审核的会议室，不可删除，admin可以修改，其他人不可修改和删除。
     *                4、审核通过的会议室，不可修改和删除（不管有没有到会议日期也不管用户是谁）。
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg saveRoom(RoomBookingDto roomDto) {
        if (checkRootBook(roomDto) > 0) {
            return ResultMsg.error("预定的会议室时间跟其他会议有冲突！");
        } else if ((roomDto.getBeginTime()).after(roomDto.getEndTime())) {
            return ResultMsg.error("开始时间不能大于结束时间!");
        } else {
            Date now = new Date();
            RoomBooking roomBooking = new RoomBooking();
            if (Validate.isString(roomDto.getId())) {
                roomBooking = roomBookingRepo.getById(roomDto.getId());
                //业务预定的会议室
                if (Validate.isString(roomBooking.getBusinessId())) {
                    if (Constant.EnumState.YES.getValue().equals(roomBooking.getRbStatus())) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "会议室已经提交审核，不能进行修改操作！");
                    }
                    //已经提交审核的，只有系统管理员才能审核
                    if (Constant.EnumState.PROCESS.getValue().equals(roomBooking.getRbStatus())) {
                        if (!SessionUtil.getLoginName().equals(SUPER_ACCOUNT)) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您没有修改权限，请联系系统管理员处理！");
                        }
                    }
                    if (Constant.EnumState.NO.getValue().equals(roomBooking.getRbStatus())) {
                        if (!SessionUtil.getUserId().equals(roomBooking.getCreatedBy())
                                && !SessionUtil.getLoginName().equals(SUPER_ACCOUNT)) {
                            return  ResultMsg.error("您没有修改权限！");
                        }
                    }
                    //普通预定的会议室
                } else {
                    if ((new Date()).after(roomBooking.getBeginTime()) || Constant.EnumState.YES.getValue().equals(roomBooking.getRbStatus())) {
                        roomBooking.setRbStatus(Constant.EnumState.YES.getValue());
                        roomBookingRepo.save(roomBooking);
                        return ResultMsg.error("操作失败，只能在预定会议的时间段之前进行修改操作！");
                    }
                    if (Constant.EnumState.NO.getValue().equals(roomBooking.getRbStatus())) {
                        if (!SessionUtil.getUserId().equals(roomBooking.getCreatedBy())
                                && !SessionUtil.getLoginName().equals(SUPER_ACCOUNT)) {
                            return  ResultMsg.error("您没有修改权限！");
                        }
                    }
                }
                BeanCopierUtils.copyProperties(roomDto, roomBooking);
            } else {
                if ((new Date()).after(roomDto.getBeginTime())) {
                    return  ResultMsg.error("操作失败，只能选择当前日期之后的时间进行预定！");
                }
                BeanCopierUtils.copyProperties(roomDto, roomBooking);
                roomBooking.setId(UUID.randomUUID().toString());
                roomBooking.setCreatedBy(SessionUtil.getUserId());
                roomBooking.setCreatedDate(now);
                //设定为审批状态
                roomBooking.setRbStatus(Constant.EnumState.NO.getValue());
            }
            //会议室名称，还是要保存的
            if (Validate.isString(roomDto.getMrID())) {
                MeetingRoom meeting = meetingRoomRepo.findById(MeetingRoom_.id.getName(), roomDto.getMrID());
                roomBooking.setAddressName(meeting.getAddr());
            }

            String strdate = DateUtils.toStringDay(roomDto.getRbDay());
            String stageday = GetWeekUtils.getWeek(roomDto.getRbDay());
            roomBooking.setRbDate(strdate + "(" + stageday + ")");//星期几
            roomBooking.setStageProject(Validate.isString(roomDto.getStageProject()) ? roomDto.getStageProject() : "" + "(" + strdate + "(" + stageday + ")" + ")");
            roomBooking.setModifiedDate(now);
            roomBooking.setModifiedBy(SessionUtil.getUserId());
    /*        String beginTime = DateUtils.converToString(roomDto.getBeginTime(),"yyyy-MM-dd");
            String endTime = DateUtils.converToString(roomDto.getEndTime(),"yyyy-MM-dd");
            String rbDay = DateUtils.converToString(roomDto.getRbDay(),"yyyy-MM-dd");

            if(null != roomDto && Validate.isString(roomDto.getBusinessId()) && Validate.isString(roomDto.getMainFlag())){
               List<RoomBooking>  rbList = roomBookingRepo.findByIds("businessId",roomDto.getBusinessId(),"");
                if(Validate.isList(rbList)){
                    for (RoomBooking rb : rbList){
                       String beginTimeTemp = DateUtils.converToString(rb.getBeginTime(),"yyyy-MM-dd");
                       String endTimeTemp = DateUtils.converToString(rb.getEndTime(),"yyyy-MM-dd");
                       String rbDayTemp = DateUtils.converToString(rb.getRbDay(),"yyyy-MM-dd");
                        if(rb.getMrID().equals(roomDto.getMrID()) && rbDayTemp.equals(rbDay) && beginTimeTemp.equals(beginTime) && endTimeTemp.equals(endTime)){
                            roomBookingRepo.delete(rb);
                            break;
                        }
                    }
                }
            }*/
            roomBookingRepo.save(roomBooking);
            BeanCopierUtils.copyProperties(roomBooking, roomDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", roomDto);
        }
    }


    /**
     * 导出会议室安排
     *
     * @param date
     * @param rbType
     * @param mrId
     */
    @Override
    public File exportRoom(String date, String rbType, String mrId) {
        String[] dates = date.split("-");
        String start = dates[0].replace("/", "-");
        String end = dates[1].replaceAll("/", "-");
        //获取一周的会议预定信息
        List<Object[]> roomBookMap = findWeekRoom(start, rbType, mrId);
        if ("0".equals(rbType)) {
            rbType = "评审会";
        }
        if ("1".equals(rbType)) {
            rbType = "全部会";
        }
        List<List<String>> rbNameList = new ArrayList<>();
        //定义行数
        int rows = 13;
        for (int i = 0; i < rows; i++) {
            List<String> rbNames = new ArrayList<>(7);
            //遍历每一天的会议预定情况
            if(roomBookMap.size() > 0){
                for (int j = 0; j < roomBookMap.size(); j++) {

                    Object[] objArr = roomBookMap.get(j);
                    //判断该数组是否有值，并判断行数是否小于该数组的长度
                    //通过行数作为下标来获取对应的值
                    if (objArr != null && objArr.length > 0 && i < objArr.length) {
                        rbNames.add((String) objArr[i]);
                    } else {
                        rbNames.add("");
                    }
                }
            }else{
                for(int k = 0 ; k < 7 ; k ++ ){
                    rbNames.add("");
                }
            }

            rbNameList.add(rbNames);
        }

        List<String> timeList = new ArrayList<String>();
        timeList.add("星期一");
        timeList.add("星期二");
        timeList.add("星期三");
        timeList.add("星期四");
        timeList.add("星期五");
        timeList.add("星期六");
        timeList.add("星期日");

        String path = SysFileUtil.getUploadPath();//文件路劲
        String showName = "";
        String relativeFileUrl = "";
        String roomId = "";
        File docFile = null;
//		List<RoomBooking> rb= roomBookingRepo.findWeekBook();
//		List<RoomBookingDto> roomDtos = new ArrayList<>();
//		if(rb !=null && rb.size() >0){
//			rb.forEach(x->{
//				RoomBookingDto roomDto = new RoomBookingDto();
//				BeanCopierUtils.copyProperties(x, roomDto);
//				roomDto.setCreatedDate(x.getCreatedDate());
//				roomDto.setModifiedDate(x.getModifiedDate());
//				roomDtos.add(roomDto);
//			});
//		}
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("timeList", timeList);
        dataMap.put("TITLE", rbType);
        dataMap.put("START", start);
        dataMap.put("END", end);
        dataMap.put("contentList", rbNameList);
        showName = Constant.Template.EXPORTROOM.getValue() + Constant.Template.WORD_SUFFIX.getKey();
        relativeFileUrl = SysFileUtil.generatRelativeUrl(path, Constant.SysFileType.MEETTINGROOM.getValue(), roomId, null, showName);
        String pathFile = path + File.separator + relativeFileUrl;
        docFile = TemplateUtil.createDoc(dataMap, Constant.Template.EXPORTROOM.getKey(), pathFile);
        return docFile;
    }

    /**
     * 获取一周每天的会议安排信息
     * 集合格式：{[周一会议预定数据1，周一会议预定数据2],...,[周日会议预定数据1 ， 周日会议预定数据1]}
     *
     * @param date
     * @param rbType
     * @param mrId
     * @return
     */
    @Override
    public List<Object[]> findWeekRoom(String date, String rbType, String mrId) {
        Date beginDate = DateUtils.converToDate(date, DateUtils.DATE_PATTERN);
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select rb.rbName,rb.rbDay,rb.beginTime,rb.endTime , mr.mrname ");
        sqlBuilder.append(" from CS_ROOM_BOOKING  rb ");
        sqlBuilder.append(" left join (select " + MeetingRoom_.id.getName() + ", " + MeetingRoom_.mrName.getName() + " mrname from cs_meeting_room ) mr ");
        sqlBuilder.append(" on mr." + RoomBooking_.id.getName() + "=rb." + RoomBooking_.mrID.getName());
        sqlBuilder.append(" where " + RoomBooking_.rbDay.getName() + " > (trunc(to_date(:rbDay,'yyyy-mm-dd'),'iw')-1)  ");
        sqlBuilder.append(" and " + RoomBooking_.rbDay.getName() + " < (trunc (to_date(:rbDay,'yyyy-mm-dd'),'iw')+7)");

        //判断是全部的会议，还是评审会，0：表示评审会
        if (Constant.MEET_BOOK_ENUM.REVIEW.getBookCode().equals(rbType)) {
            sqlBuilder.append(" and " + RoomBooking_.businessType.getName() + "=:businessType ");
            sqlBuilder.setParam("businessType", ProjectConstant.BUSINESS_TYPE.SIGN_WP.toString());
        }

        sqlBuilder.append(" order by rb." + RoomBooking_.rbDay.getName() + " asc");
        sqlBuilder.setParam("rbDay", date);

        List<Object[]> objectList = roomBookingRepo.getObjectArray(sqlBuilder);

        List<Object[]> resultList = new ArrayList<>();

        if (objectList != null && objectList.size() > 0) {
            // 1、单独定义存储周一至周日 会议预定的每一条记录
            Object[] obj1 = new Object[objectList.size()];
            Object[] obj2 = new Object[objectList.size()];
            Object[] obj3 = new Object[objectList.size()];
            Object[] obj4 = new Object[objectList.size()];
            Object[] obj5 = new Object[objectList.size()];
            Object[] obj6 = new Object[objectList.size()];
            Object[] obj7 = new Object[objectList.size()];

            for (int i = 0, j = 0; i < objectList.size() && j < objectList.size(); i++, j++) {
                Object[] objs = objectList.get(i);
                Date rbDay = (Date) objs[1];
                // 2、计算会议预定时间与周一时间差几天
                long day = DateUtils.daysBetween(beginDate, rbDay);
                //3、通过判断时间之差，来确认是星期几，并判断存储该星期的数组是否已经有值，如果没有则从下标0开始存
                if (day == 0) {
                    if (obj1[0] == null) {
                        j = 0;
                    }
                    obj1[j] = jointContent(objs);
                } else if (day == 1) {
                    if (obj2[0] == null) {
                        j = 0;
                    }
                    obj2[j] = jointContent(objs);
                } else if (day == 2) {
                    if (obj3[0] == null) {
                        j = 0;
                    }
                    obj3[j] = jointContent(objs);
                } else if (day == 3) {
                    if (obj4[0] == null) {
                        j = 0;
                    }
                    obj4[j] = jointContent(objs);
                } else if (day == 4) {
                    if (obj5[0] == null) {
                        j = 0;
                    }
                    obj5[j] = jointContent(objs);
                } else if (day == 5) {
                    if (obj6[0] == null) {
                        j = 0;
                    }
                    obj6[j] = jointContent(objs);
                } else if (day == 6) {
                    if (obj7[0] == null) {
                        j = 0;
                    }
                    obj7[j] = jointContent(objs);
                }

            }
            //4、将数组添加到集合
            resultList.add(obj1);
            resultList.add(obj2);
            resultList.add(obj3);
            resultList.add(obj4);
            resultList.add(obj5);
            resultList.add(obj6);
            resultList.add(obj7);
        }

        return resultList;
    }

    /**
     * 根据业务ID和业务类型初始化会议室预定的值
     *
     * @param businessId
     * @param businessType
     * @return
     */
    @Override
    public RoomBookingDto initDefaultValue(String businessId, String businessType) {
        RoomBookingDto roomBookingDto = new RoomBookingDto();
        roomBookingDto.setHost(SessionUtil.getDisplayName());
        roomBookingDto.setDueToPeople(SessionUtil.getDisplayName());
        if (Validate.isString(businessId)) {
            roomBookingDto.setBusinessId(businessId);
        }
        if (Validate.isString(businessType)) {
            roomBookingDto.setBusinessType(businessType);
            ProjectConstant.BUSINESS_TYPE businessTypeEnum = ProjectConstant.BUSINESS_TYPE.valueOf(businessType);
            if(Validate.isObject(businessTypeEnum)){
                switch (businessTypeEnum){
                    /**
                     * 项目工作方案
                     */
                    case SIGN_WP:
                        WorkProgram workProgram = workProgramRepo.findById(WorkProgram_.id.getName(), businessId);
                        roomBookingDto.setStageOrgName(workProgram.getReviewOrgName());
                        roomBookingDto.setRbName(workProgram.getProjectName());
                        break;
                    /**
                     * 课题工作方案（目前该方案以停用）
                     */
                    case TOPIC_WP:
                        WorkPlan workPlan = workPlanRepo.findById(WorkPlan_.id.getName(), businessId);
                        roomBookingDto.setRbName(workPlan.getTopicName());
                        break;
                    default:
                        ;
                }
            }
        }
        roomBookingDto.setBeginTime(null);
        roomBookingDto.setEndTime(null);
        roomBookingDto.setRbDate(null);
        return roomBookingDto;
    }

    /**
     * 查询日程数据（主要根据会议ID，开始日期和结束日期查询）
     *
     * @param bookInfo
     * @return
     */
    @Override
    public List<RoomBookingDto> queryBookInfo(RoomBookingDto bookInfo) {
        List<RoomBookingDto> returnList = new ArrayList<>();
        Criteria criteria = roomBookingRepo.getExecutableCriteria();
        if (Validate.isString(bookInfo.getMrID())) {
            criteria.add(Restrictions.eq(RoomBooking_.mrID.getName(), bookInfo.getMrID()));
        }
        if (Validate.isString(bookInfo.getBeginTimeStr())) {
            criteria.add(Restrictions.ge(RoomBooking_.rbDay.getName(), DateUtils.converToDate(bookInfo.getBeginTimeStr(), null)));
        }
        if (Validate.isString(bookInfo.getEndTimeStr())) {
            criteria.add(Restrictions.le(RoomBooking_.rbDay.getName(), DateUtils.converToDate(bookInfo.getEndTimeStr(), null)));
        }
        List<RoomBooking> resutList = criteria.list();
        resutList.forEach(rl -> {
            RoomBookingDto rbDto = new RoomBookingDto();
            BeanCopierUtils.copyProperties(rl, rbDto);
            //迁移过来的数据有空值，这里要判断下
            if (!Validate.isString(rbDto.getContent())) {
                rbDto.setContent("");
            }
            if (!Validate.isString(rbDto.getRemark())) {
                rbDto.setRemark("");
            }
            returnList.add(rbDto);
        });
        return returnList;
    }



    /**
     * 内容的拼接
     * 格式：XXX会议 换行 (时间) 换行 XXX会议室
     *
     * @param objects
     * @return
     */
    public String jointContent(Object[] objects) {
        String result = "";
        String rbName = objects[0] == null ? "" : objects[0] + "会议";
        //时间格式：yyyy-MM-dd HH:mm:ss 获取  HH:mm
        String begin = DateUtils.getTimeIgnoreSecond((Date) objects[2]) == null ? "" : DateUtils.getTimeIgnoreSecond((Date) objects[2]);
        String end = DateUtils.getTimeIgnoreSecond((Date) objects[3]) == null ? "" : DateUtils.getTimeIgnoreSecond((Date) objects[3]);
        String rbDate = "<w:br />" + "(" + begin + "-" + end + ")";
        String ress = objects[4] == null ? "" : "<w:br />" + objects[4];
        result = rbName + rbDate + ress;
        return result;
    }

}
