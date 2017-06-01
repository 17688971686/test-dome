package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertGlory;
import cs.domain.expert.WorkExpe;
import cs.model.expert.ExpertGloryDto;
import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertGloryRepo;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.WorkExpeRepo;
import cs.service.sys.UserServiceImpl;

@Service
public class ExpertGloryServiceImpl implements ExpertGloryService{
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
    private ExpertRepo expertRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Autowired
	private ExpertGloryRepo expertGloryRepo;
	
	@Override
	public List<ExpertGloryDto> getGlory(ODataObj odataObj){
		List<ExpertGlory> listGlory = expertGloryRepo.findByOdata(odataObj);
		List<ExpertGloryDto> listGlorytDto = null;
		if(Validate.isList(listGlory)){
			listGlorytDto = new ArrayList<>(listGlory.size());
			for (ExpertGlory item : listGlory) {
				ExpertGloryDto gloryDto = new ExpertGloryDto();
				BeanCopierUtils.copyProperties(item, gloryDto);
				listGlorytDto.add( gloryDto);
			}
		}		
		logger.info("查询专家聘书");
		return listGlorytDto;
	}
	@Override
	@Transactional
	public void createGlory(ExpertGloryDto expertGloryDto){
		//WorkExpe findWork = workExpeRepo.findWorkByName(workExpeDto.getCompanyName());
				// 工作经验不存在	
				//if (findWork==null) {		
					ExpertGlory glory = new ExpertGlory();
					BeanCopierUtils.copyProperties(expertGloryDto, glory);
					
					glory.setgID(UUID.randomUUID().toString());										
					Date now = new Date();
					glory.setCreatedBy(currentUser.getLoginName());
					glory.setModifiedBy(currentUser.getLoginName());
					glory.setCreatedDate(now);
					glory.setModifiedDate(now);
					
					glory.setExpert(expertRepo.findById(expertGloryDto.getExpertID()));
					expertGloryRepo.save(glory);				
					logger.info(String.format("添加专家聘书,聘书名称:%s", glory.getgQualifications()));
				//} else {
				//	throw new IllegalArgumentException(String.format("公司名%s 已经存在", workExpeDto.getCompanyName()));
				//}
	}
	
	@Override
	@Transactional
	public void deleteGlory(String[] ids){
		for (String id : ids) {
			this.deleteGlory(id);
		}
		logger.info("删除专家聘书");
	}
	@Override
	@Transactional
	public void deleteGlory(String id){
		ExpertGlory expertGlory = expertGloryRepo.findById(id);
		if (expertGlory != null&&StringUtils.isNotBlank(expertGlory.getgID())) {
			expertGloryRepo.delete(expertGlory);
			logger.info(String.format("删除专家聘书,聘书名称为:%s", expertGlory.getgQualifications()));			
		}
	}
	@Override
	@Transactional
	public void updateGlory(ExpertGloryDto expertGloryDto){
		ExpertGlory expertGlory = expertGloryRepo.findById(expertGloryDto.getgID());
		BeanCopierUtils.copyPropertiesIgnoreNull(expertGloryDto, expertGlory);
		expertGloryRepo.save(expertGlory);
		logger.info(String.format("更新专家聘书,单位名称为:%s", expertGloryDto.getgQualifications()));
	}
}
