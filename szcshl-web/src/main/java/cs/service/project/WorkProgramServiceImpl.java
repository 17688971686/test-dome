package cs.service.project;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.model.project.WorkProgramDto;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;

@Service
public class WorkProgramServiceImpl implements WorkProgramService {
	private static Logger log = Logger.getLogger(WorkProgramServiceImpl.class);
	@Autowired
	private WorkProgramRepo workProgramRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private SignRepo signRepo;
	
	@Override
	@Transactional
	public void save(WorkProgramDto workProgramDto) throws Exception {
				
		if(Validate.isString(workProgramDto.getSignId())){
			WorkProgram workProgram = new WorkProgram(); 		
			BeanCopierUtils.copyProperties(workProgramDto, workProgram);
			
			Date now = new Date();
			workProgram.setCreatedBy(currentUser.getLoginName());
			workProgram.setCreatedDate(now);
			workProgram.setModifiedBy(currentUser.getLoginName());
			workProgram.setModifiedDate(now);
			//标题时间
			workProgram.setTitleDate(now);
			//补充资料函发文日期
			String sup=workProgramDto.getSuppLetterDate();
			Date supdate=DateUtils.toDateString(sup);
			workProgram.setSuppLetterDate(supdate);
			//调研开始时间
			String beginTiem = workProgramDto.getStudyBeginTime();
			Date start = DateUtils.toDateString(beginTiem);
			workProgram.setStudyBeginTime(start);
			//调研结束时间
			String endTime=	workProgramDto.getStudyEndTime();
			Date end = DateUtils.toDateString(endTime);
			workProgram.setStudyEndTime(end);
			Sign sign = signRepo.findById(workProgramDto.getSignId());
			workProgram.setSign(sign);
			if(!Validate.isString(workProgramDto.getId())){
				workProgram.setId(UUID.randomUUID().toString());
			}			
			workProgramRepo.save(workProgram);
			
			sign.setIsreviewcompleted(EnumState.YES.getValue());
			sign.setWorkProgram(workProgram);
			signRepo.save(sign);
			
		}else{
			log.info("工作方案添加操作：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
	}

	@Override
	public WorkProgramDto initWorkBySignId(String signId) {
		Sign sign  =signRepo.findById(signId);
		if(sign !=null){
			WorkProgram work =sign.getWorkProgram();
			if(work !=null && Validate.isString(work.getId())){
				
				WorkProgramDto workDto = new WorkProgramDto();
				/*//补充资料函发文日期
				Date suppLetter=work.getSuppLetterDate();
				String suppletDate = DateUtils.toStringDay(suppLetter);
				workDto.setSuppLetterDate(suppletDate);
				//调研开始时间
				Date beginTime =work.getStudyBeginTime();
				String start =DateUtils.toStringDay(beginTime);
				workDto.setStudyBeginTime(start);
				//调研结束时间
				Date endTime =	work.getStudyEndTime();
				String end =DateUtils.toStringDay(endTime);
				workDto.setStudyEndTime(end);*/
				//标题时间
				Date title=	work.getTitleDate();
				String titleDate = DateUtils.toStringDay(title);
				workDto.setTitleDate(titleDate);
				BeanCopierUtils.copyProperties(work, workDto);
				workDto.setSignId(signId);
				return workDto;
			}/*else{
				work.setProjectName(sign.getProjectname());
			}*/
		}
		
		return null;
	}

}
