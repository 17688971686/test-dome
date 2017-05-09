package cs.service.project;

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
import cs.domain.project.FileRecord;
import cs.domain.project.Sign;
import cs.model.project.FileRecordDto;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;

@Service
public class FileRecordServiceImpl implements FileRecordService {
	private static Logger log = Logger.getLogger(FileRecordServiceImpl.class);
	@Autowired
	private FileRecordRepo fileRecordRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private SignRepo signRepo;
	
	@Override
	@Transactional
	public void save(FileRecordDto fileRecordDto) throws Exception{
		if(Validate.isString(fileRecordDto.getSignId())){
			FileRecord fileRecord = new FileRecord(); 		
			BeanCopierUtils.copyProperties(fileRecordDto, fileRecord);
			
			Date now = new Date();
			fileRecord.setCreatedBy(currentUser.getLoginName());
			fileRecord.setCreatedDate(now);
			fileRecord.setModifiedBy(currentUser.getLoginName());
			fileRecord.setModifiedDate(now);
			
			Sign sign = signRepo.findById(fileRecordDto.getSignId());
			fileRecord.setSign(sign);
			if(!Validate.isString(fileRecord.getFileRecordId())){
				fileRecord.setFileRecordId(UUID.randomUUID().toString());
			}			
			fileRecordRepo.save(fileRecord);	
			
			//更新归档编号
			sign.setFilenum(fileRecordDto.getFileNo());
			sign.setFileRecord(fileRecord);
			signRepo.save(sign);
		}else{
			log.info("提交归档信息异常：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
		
	}

	@Override
	public FileRecordDto initBySignId(String signid) {
		Sign sign = signRepo.findById(signid);
		if(sign != null){
			FileRecord fileRecord = sign.getFileRecord();
			if(fileRecord != null && Validate.isString(fileRecord.getFileRecordId())){
				FileRecordDto fileRecordDto = new FileRecordDto();
				BeanCopierUtils.copyProperties(fileRecord, fileRecordDto);
				fileRecordDto.setSignId(signid);
				return fileRecordDto;
			}
		}
		return null;
	}

}
