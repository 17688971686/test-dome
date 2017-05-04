package cs.service;

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
import cs.common.utils.Validate;
import cs.domain.Sign;
import cs.domain.WorkProgram;
import cs.model.WorkProgramDto;
import cs.repository.repositoryImpl.SignRepo;
import cs.repository.repositoryImpl.WorkProgramRepo;

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
	public void createWork(WorkProgramDto workProgramDto) throws Exception {
				
		if(Validate.isString(workProgramDto.getSignId())){
			WorkProgram workProgram = new WorkProgram(); 		
			BeanCopierUtils.copyProperties(workProgramDto, workProgram);
			
			Date now = new Date();
			workProgram.setCreatedBy(currentUser.getLoginName());
			workProgram.setCreatedDate(now);
			workProgram.setModifiedBy(currentUser.getLoginName());
			workProgram.setModifiedDate(now);
			
			Sign sign = signRepo.findById(workProgramDto.getSignId());
			workProgram.setSign(sign);
			workProgram.setId(UUID.randomUUID().toString());
			workProgramRepo.save(workProgram);
			
			sign.setIsreviewcompleted(EnumState.YES.getValue());
			sign.setWorkProgram(workProgram);
			signRepo.save(sign);
			
		}else{
			log.info("工作方案添加操作：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
	}

}
