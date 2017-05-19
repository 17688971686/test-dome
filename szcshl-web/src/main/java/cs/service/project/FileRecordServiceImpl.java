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
import cs.common.utils.DateUtils;
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
			//存档编号
			if(!Validate.isString(fileRecord.getFileNo())){
				fileRecord.setFileNo(NumIncreaseUtils.getFileRecordNo(""));
			}
			//存档日期
			if(Validate.isString(fileRecordDto.getFileDate())){
				fileRecord.setFileDate(DateUtils.ConverToDate(fileRecordDto.getFileDate()));
			}else{
				fileRecord.setFileDate(now);
			}
			//表格打印日期
			if(Validate.isString(fileRecordDto.getPrintDate())){
				fileRecord.setPrintDate(DateUtils.ConverToDate(fileRecordDto.getFileDate()));
			}else{
				fileRecord.setPrintDate(now);
			}
			fileRecordRepo.save(fileRecord);	
			
			//更新归档编号
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
        	fileRecordDto.setFileDate(DateUtils.convertDateToString(fileRecord.getFileDate()));
        	fileRecordDto.setPrintDate(DateUtils.convertDateToString(fileRecord.getPrintDate()));
        }else{
        	//如果是新增，则要初始化
        	Sign sign = signRepo.findById(signid);
        	fileRecordDto.setProjectName(sign.getProjectname());
        	fileRecordDto.setProjectName(sign.getProjectname());
        	fileRecordDto.setProjectCode(sign.getProjectcode());
        	fileRecordDto.setProjectCompany(sign.getBuiltcompanyid());	//建设单位名称
        	fileRecordDto.setProjectChargeUser(userRepo.findById(sign.getmFlowMainUserId()).getDisplayName());
        	fileRecordDto.setFileNumber(Validate.isString(sign.getFilenum())?sign.getFilenum():"(无发文)");
        }
		return fileRecordDto;

	}

}
