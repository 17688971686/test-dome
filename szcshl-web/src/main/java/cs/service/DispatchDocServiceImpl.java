package cs.service;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.DispatchDoc;
import cs.domain.Sign;
import cs.model.DispatchDocDto;
import cs.repository.repositoryImpl.DispatchDocRepo;
import cs.repository.repositoryImpl.SignRepo;

@Service
public class DispatchDocServiceImpl implements DispatchDocService {
	private static Logger log = Logger.getLogger(DispatchDocServiceImpl.class);
	@Autowired
	private DispatchDocRepo dispatchDocRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private SignRepo signRepo;
	
	@Override
	@Transactional
	public void save(DispatchDocDto dispatchDocDto) throws Exception{
		if(Validate.isString(dispatchDocDto.getSignId())){
			DispatchDoc dispatchDoc = new DispatchDoc(); 		
			BeanCopierUtils.copyProperties(dispatchDocDto, dispatchDoc);
			
			Date now = new Date();
			dispatchDoc.setCreatedBy(currentUser.getLoginName());
			dispatchDoc.setCreatedDate(now);
			dispatchDoc.setModifiedBy(currentUser.getLoginName());
			dispatchDoc.setModifiedDate(now);
			
			Sign sign = signRepo.findById(dispatchDocDto.getSignId());
			//dispatchDoc.setSign(sign);
			if(!Validate.isString(dispatchDoc.getId())){
				dispatchDoc.setId(UUID.randomUUID().toString());
			}			
			dispatchDocRepo.save(dispatchDoc);
									
		}else{
			log.info("提交收文信息异常：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
	}

}
