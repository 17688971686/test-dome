package cs.service.sys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.ahelper.RealPathResolverImpl;
import cs.common.ICurrentUser;
import cs.common.utils.SysFileUtil;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
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
	
	@Autowired
	private RealPathResolverImpl realPathResolverImpl;
	
	@Override
	@Transactional
	public SysFileDto save(byte[] bytes, String fileName, String businessId, String fileType, String module,String processInstanceId)  {
		SysFileDto sysFileDto = new SysFileDto();
		SysFile sysFile = new SysFile();
		logger.debug("Begin save file "+fileName+"-"+businessId);
		try{
			String fileUploadPath = SysFileUtil.getUploadPath();
			String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath,module,businessId,fileName);
						
			sysFile.setSysFileId(UUID.randomUUID().toString());
			sysFile.setBusinessId(businessId);
			sysFile.setProcessInstanceId(processInstanceId);
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
	@Transactional
	public SysFileDto savePhoto(byte[] bytes, String fileName, String businessId, String fileType,String module )  {
		Criteria criteria=sysFileRepo.getSession().createCriteria(SysFile.class);
		criteria.add(Restrictions.eq(SysFile_.businessId.getName(), businessId));
		List<SysFile> sysFileList=criteria.list();
		SysFileDto sysFileDto = new SysFileDto();
		SysFile sysFile=null;
		//如果没有已生成的记录则创建
		if(sysFileList==null||sysFileList.size()==0){
			sysFile=new SysFile();
			sysFile.setSysFileId(UUID.randomUUID().toString());
			
		}else{
			sysFile = sysFileList.get(0);
			//删除路径下的旧头像
			File delfile=new File(sysFile.getFileUrl());
			if(delfile.exists() && delfile.isFile()){
				delfile.delete();
			}
		}
		logger.debug("Begin save file "+fileName+"-"+businessId);
		try{
			//String fileLocation="/contents/expert";
			String urlPath=realPathResolverImpl.get("contents/expert");
			String realPath=urlPath+"//"+fileName;
			String relativePath="contents/expert/"+fileName;
			/*String fileUploadPath = SysFileUtil.getUploadPath();
			String relativeFileUrl = SysFileUtil.generatRelativeUrl(fileUploadPath,module,businessId,fileName);*/
			//String relativeFileUrl=SysFileUtil.generatRelativeUrl(fileLocation, module, businessId, fileName);
			sysFile.setBusinessId(businessId);
			sysFile.setFileSize(bytes.length);
			sysFile.setShowName(fileName);
			sysFile.setFileType(fileType);
			sysFile.setFileUrl(relativePath);
			
			Date now = new Date();
			sysFile.setCreatedBy(currentUser.getLoginName());
			sysFile.setModifiedBy(currentUser.getLoginName());
			sysFile.setCreatedDate(now);
			sysFile.setModifiedDate(now);
			
			sysFileRepo.save(sysFile);
			
			File file = new File(urlPath);
			System.out.println( file.getParentFile());
			//如果不存在该文件夹则创建
			if(!(file.exists()) && !file.isDirectory()){
				file.mkdir();
			}
			
			//先保存成功，再传输文件
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(realPath));
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
		sysFileRepo.deleteById(SysFile_.sysFileId.getName(), sysFileId);
		//SysFile sysFile = sysFileRepo.findById(sysFileId);	//有bug
		
		/*SysFile sysFile = sysFileRepo.getById(sysFileId);
		if(sysFile != null && Validate.isString(sysFile.getSysFileId())){
			sysFileRepo.delete(sysFile);
		}*/		
	}

}
