package cs.service.project;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.NumIncreaseUtils;
import cs.common.utils.Validate;
import cs.domain.project.FileRecord;
import cs.domain.project.FileRecord_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.model.project.FileRecordDto;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.UserRepo;

@Service
public class FileRecordServiceImpl implements FileRecordService {
	private static Logger log = Logger.getLogger(FileRecordServiceImpl.class);
	@Autowired
	private FileRecordRepo fileRecordRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private SignRepo signRepo;
	@Autowired
	private UserRepo userRepo;
	
	@Override
	@Transactional
	public void save(FileRecordDto fileRecordDto) throws Exception{
		if(Validate.isString(fileRecordDto.getSignId())){
			FileRecord fileRecord = null;	
			
			Date now = new Date();														
			if(!Validate.isString(fileRecordDto.getFileRecordId())){
				fileRecord = new FileRecord(); 	
				BeanCopierUtils.copyProperties(fileRecordDto, fileRecord);		
				fileRecord.setFileRecordId(UUID.randomUUID().toString());	
				fileRecord.setCreatedBy(currentUser.getLoginName());	
				fileRecord.setCreatedDate(now);
			}else{
				fileRecord = fileRecordRepo.findById(fileRecordDto.getFileRecordId());
				BeanCopierUtils.copyPropertiesIgnoreNull(fileRecordDto, fileRecord);				
			}
			fileRecord.setModifiedBy(currentUser.getLoginName());			
			fileRecord.setModifiedDate(now);
			fileRecord.setFileDate(fileRecord.getFileDate()==null?now:fileRecord.getFileDate());
			fileRecord.setPrintDate(fileRecord.getPrintDate()==null?now:fileRecord.getPrintDate());
			
			Sign sign = signRepo.findById(fileRecordDto.getSignId());
			fileRecord.setSign(sign);
			//设置归档编号(评估类)
			if(!Validate.isString(fileRecord.getFileNo())){
				fileRecord.setFileNo(NumIncreaseUtils.getFileRecordNo(Constant.FileNumType.PD.getValue()));
			}
			fileRecordRepo.save(fileRecord);	
			
			//更新收文信息			
			sign.setFilenum(fileRecord.getFileNo());
			sign.setFileRecord(fileRecord);
			signRepo.save(sign);
		}else{
			log.info("提交归档信息异常：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
		
	}

	@Override
	public FileRecordDto initBySignId(String signid) {
		FileRecordDto fileRecordDto = new FileRecordDto();
		
		HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+FileRecord.class.getSimpleName()+" where "+FileRecord_.sign.getName()+"."+Sign_.signid.getName()+" = :signId ");
        hqlBuilder.setParam("signId", signid);               
        
        List<FileRecord> list = fileRecordRepo.findByHql(hqlBuilder);
        if(list != null && list.size() > 0){
        	FileRecord  fileRecord = list.get(0);
        	BeanCopierUtils.copyProperties(fileRecord,fileRecordDto);
        }else{
        	//如果是新增，则要初始化
        	Sign sign = signRepo.findByIds(Sign_.signid.getName(),signid,"").get(0);
        	fileRecordDto.setProjectName(sign.getProjectname());
        	fileRecordDto.setProjectName(sign.getProjectname());
        	fileRecordDto.setProjectCode(sign.getProjectcode());
        	fileRecordDto.setProjectCompany(sign.getBuiltcompanyid());	//建设单位名称
        	fileRecordDto.setProjectChargeUser(userRepo.findById(sign.getmFlowMainUserId()).getDisplayName());
        	fileRecordDto.setFileNumber("");//文号
        }
		return fileRecordDto;

	}

}
