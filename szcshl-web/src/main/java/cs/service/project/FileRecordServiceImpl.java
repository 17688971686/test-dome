package cs.service.project;

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
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.domain.sys.User;
import cs.model.project.FileRecordDto;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
	@Autowired
	private SysFileRepo sysFileRepo;
	@Autowired
	private SignPrincipalService signPrincipalService;

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

    /**
     * 根据收文ID初始化归档信息
     * @param signid
     * @return
     */
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
            User priUser = signPrincipalService.getMainPriUser(signid, Constant.EnumState.YES.getValue());
        	Sign sign = signRepo.findByIds(Sign_.signid.getName(),signid,"").get(0);
        	fileRecordDto.setProjectName(sign.getProjectname());
//        	fileRecordDto.setProjectName(sign.getProjectname());
        	fileRecordDto.setProjectCode(sign.getProjectcode());
        	fileRecordDto.setFileReviewstage(sign.getReviewstage());//评审阶段
        	fileRecordDto.setProjectCompany(sign.getDesigncompanyName());//编制单位名称
        	fileRecordDto.setProjectChargeUser(priUser==null?"":priUser.getDisplayName());
        	//设置默认文件标题
			String fileTitle="《";
			fileTitle+=sign.getProjectname()==null?"":sign.getProjectname();
			fileTitle+=(sign.getReviewstage()==null?"":sign.getReviewstage());
			fileTitle+="》";
			fileTitle+=(sign.getIsAdvanced()==null?"":sign.getIsAdvanced());
			fileRecordDto.setFileTitle(fileTitle);
			
        	fileRecordDto.setFileNumber("");//文号
        }
		return fileRecordDto;
	}

	@Override
	public List<SysFile> sysFileByIds(String signid) {
		Sign sign = signRepo.findById(signid);
		HqlBuilder hql = HqlBuilder.create();
		hql.append(" from "+SysFile.class.getSimpleName()+" where "+SysFile_.businessId.getName()).append("=:businessId").setParam("businessId", sign.getSignid());
		List<SysFile> sysFiles = sysFileRepo.findByHql(hql);
		return sysFiles;
	}

}
