package cs.service.sharing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sharing.SharingPlatlform;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sharing.SharingPlatlformRepo;
import cs.repository.repositoryImpl.sys.UserRepo;

/**
 * Description: 共享平台 业务操作实现类
 * author: sjy
 * Date: 2017-7-11 10:40:17
 */
@Service
public class SharingPlatlformServiceImpl  implements SharingPlatlformService {

	private static Logger logger = Logger.getLogger(SharingPlatlformServiceImpl.class);
	@Autowired
	private SharingPlatlformRepo sharingPlatlformRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public PageModelDto<SharingPlatlformDto> get(ODataObj odataObj) {
		PageModelDto<SharingPlatlformDto> pageModelDto = new PageModelDto<SharingPlatlformDto>();
		List<SharingPlatlform> resultList = sharingPlatlformRepo.findByOdata(odataObj);
		List<SharingPlatlformDto> resultDtoList = new ArrayList<SharingPlatlformDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				SharingPlatlformDto modelDto = new SharingPlatlformDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());
				
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(SharingPlatlformDto record) {
		SharingPlatlform domain = new SharingPlatlform(); 
		BeanCopierUtils.copyProperties(record, domain); 
		domain.setSharId(UUID.randomUUID().toString());
		Date now = new Date();
		domain.setIsPublish(record.getIsPublish());
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setPublishDate(now);
		domain.setPublishUsername(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		sharingPlatlformRepo.save(domain);
		record.setSharId(domain.getSharId());
		
	}

	@Override
	@Transactional
	public void update(SharingPlatlformDto record) {
		SharingPlatlform domain = sharingPlatlformRepo.findById(record.getSharId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setPublishDate(now);
		domain.setPublishUsername(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		sharingPlatlformRepo.save(domain);
	}

	@Override
	public SharingPlatlformDto findById(String id) {		
		SharingPlatlformDto modelDto = new SharingPlatlformDto();
		if(Validate.isString(id)){
			SharingPlatlform domain = sharingPlatlformRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		
	SharingPlatlform sharing = sharingPlatlformRepo.findById(id);
	if(sharing !=null){
		if(sharing.getCreatedBy().equals(currentUser.getLoginName())){
			sharingPlatlformRepo.delete(sharing);
			logger.info(String.format("删除发现信息，发布主题:%s", sharing.getTheme()));
		}
		else if(currentUser.getLoginName().equals("admin")){
			sharingPlatlformRepo.delete(sharing);
			logger.info(String.format("删除发现信息，发布主题:%s", sharing.getTheme()));
		}else{
			  throw new IllegalArgumentException("您不是发布人员，不能对其进行删除操作！");
		}
	}
		
	}

	@Override
	public void deletes(String[] ids) {
		for(String id :ids){
			this.delete(id);
		}
		logger.info("批量删除发布信息");
		
	}

	@Override
	public SharingPlatlformDto postSharingAritle(String id) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append("with res as(select cs_sharing_Platform.*,row_number() over(order by isPublish desc,publishDate desc)t from  cs_sharing_Platform)");
		hqlBuilder.append(" select res.* from res where t=(");
		hqlBuilder.append(" select res.t-1 from res where res.sharId=:id)");
		hqlBuilder.setParam("id", id);
		List<SharingPlatlform> sharinglist = sharingPlatlformRepo.findBySql(hqlBuilder);
		SharingPlatlformDto sharingDto = new SharingPlatlformDto();
		if(sharinglist.size() > 0 ){
			BeanCopierUtils.copyProperties(sharinglist.get(0), sharingDto);
		}
		return sharingDto;
	}

	@Override
	public SharingPlatlformDto nextSharingArticle(String id) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append("with res as(select cs_sharing_Platform.*,row_number() over(order by isPublish desc,publishDate desc)t from  cs_sharing_Platform)");
		hqlBuilder.append(" select res.* from res where t=(");
		hqlBuilder.append(" select res.t+1 from res where res.sharId=:id)");
		hqlBuilder.setParam("id", id);
		List<SharingPlatlform> sharinglist = sharingPlatlformRepo.findBySql(hqlBuilder);
		SharingPlatlformDto sharingDto = new SharingPlatlformDto();
		if(sharinglist.size() > 0 ){
			BeanCopierUtils.copyProperties(sharinglist.get(0), sharingDto);
		}
		return sharingDto;
	}


	@Override
	@Transactional
	public void updatePublishStatus(SharingPlatlformDto sharingDto) {
		SharingPlatlform domain = sharingPlatlformRepo.findById(sharingDto.getSharId());
		if(domain !=null){
		
			if(domain.getIsPublish().equals("0") && domain.getPublishUsername().equals(currentUser.getLoginName())){
				domain.setIsPublish(EnumState.YES.getValue());
				BeanCopierUtils.copyPropertiesIgnoreNull(sharingDto, domain);
				Date now = new Date();
				domain.setCreatedBy(currentUser.getLoginName());
				domain.setModifiedBy(currentUser.getLoginName());
				domain.setPublishDate(now);
				domain.setPublishUsername(currentUser.getLoginName());
				domain.setCreatedDate(now);
				domain.setModifiedDate(now);
			}else if(domain.getIsPublish().equals("9") && domain.getPublishUsername().equals(currentUser.getLoginName())){
				domain.setIsPublish(EnumState.NO.getValue());
				BeanCopierUtils.copyPropertiesIgnoreNull(sharingDto, domain);
				Date now = new Date();
				domain.setCreatedBy(currentUser.getLoginName());
				domain.setModifiedBy(currentUser.getLoginName());
				domain.setPublishDate(now);
				domain.setPublishUsername(currentUser.getLoginName());
				domain.setCreatedDate(now);
				domain.setModifiedDate(now);
			}else{
				 throw new IllegalArgumentException("您不是发布人员，不能对该记录操作！");
			}	
		}
		
		sharingPlatlformRepo.save(domain);
		
	}

	@Override
	public UserDto findUser(String loginName) {
		User user =   userRepo.findUserByName(loginName);
		UserDto userDto = new UserDto();
		if(user.getLoginName().equals(currentUser.getLoginName())){
			BeanCopierUtils.copyProperties(user, userDto);
			return userDto;
		}else{
			throw new IllegalArgumentException("只能选择登录本人！");
		}
		
	}
	
}