package cs.service.sys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.SysFileUtil;
import cs.common.utils.Validate;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.SysFileRepo;

@Service
public class SysFileServiceImpl implements SysFileService{
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);	
	@Autowired
	private SysFileRepo sysFileRepo;	
	
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	@Transactional
	public SysFileDto save(byte[] bytes, String fileName, String businessId, String fileType, String module,String proccessInstanceId)  {
		SysFileDto sysFileDto = new SysFileDto();
		SysFile sysFile = new SysFile();
		logger.debug("Begin save file "+fileName+"-"+businessId);
		try{
			String fileUploadPath = SysFileUtil.getUploadPath();
			String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath,module,businessId,fileName);
						
			sysFile.setSysFileId(UUID.randomUUID().toString());
			sysFile.setBusinessId(businessId);
			sysFile.setProccessInstanceId(proccessInstanceId);
			sysFile.setFileSize(bytes.length);
			sysFile.setShowName(fileName);
			sysFile.setFileType(fileType);
			sysFile.setFileUrl(relativeFileUrl);
			
			Date now = new Date();
			sysFile.setCreatedBy(currentUser.getLoginName());
			sysFile.setModifiedBy(currentUser.getLoginName());
			sysFile.setCreatedDate(now);
			sysFile.setModifiedDate(now);
			
			sysFileRepo.save(sysFile);
			
			//先保存成功，再传输文件
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileUploadPath + File.separator +relativeFileUrl));
			stream.write(bytes);
			stream.close();
			BeanUtils.copyProperties(sysFile, sysFileDto);
		}catch(Exception e){
			throw new RuntimeException(e);
		}				
		return sysFileDto;
	}
	
	@Override
	public PageModelDto<SysFileDto> get(ODataObj odataObj) {
		List<SysFile> sysFileList=sysFileRepo.findByOdata(odataObj);
		List<SysFileDto> sysFileDtoList= new ArrayList<SysFileDto>();
		for(SysFile item:sysFileList){
			SysFileDto sysFileDto=new SysFileDto();
			sysFileDto.setSysFileId(item.getSysFileId());
			sysFileDto.setBusinessId(item.getBusinessId());
			sysFileDto.setFileUrl(item.getFileUrl());
			sysFileDto.setShowName(item.getShowName());
			sysFileDto.setFileType(item.getFileType());
			sysFileDtoList.add(sysFileDto);
		}
		PageModelDto<SysFileDto> pageModelDto=new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(sysFileDtoList);
		logger.info("查询文件");
		return pageModelDto;
	}
	

	@Override
	@Transactional
	public void deleteById(String sysFileId) {				
		SysFile sysFile = sysFileRepo.findById(sysFileId);
		if(sysFile != null && Validate.isString(sysFile.getSysFileId())){
			sysFileRepo.delete(sysFile);
		}		
	}

}
