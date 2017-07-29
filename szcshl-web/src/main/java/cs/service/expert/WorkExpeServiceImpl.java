package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.utils.SessionUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.utils.BeanCopierUtils;
import cs.domain.expert.WorkExpe;
import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.WorkExpeRepo;
import cs.service.sys.UserServiceImpl;

@Service
public class WorkExpeServiceImpl implements WorkExpeService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
    private ExpertRepo expertRepo;
	@Autowired
    private WorkExpeRepo workExpeRepo;
	
	@Override
	public List<WorkExpeDto> getWork(ODataObj odataObj) {
		List<WorkExpe> listWork = workExpeRepo.findByOdata(odataObj);
		List<WorkExpeDto> listWorktDto = null;
		if(listWork != null && listWork.size() > 0){
			listWorktDto = new ArrayList<>(listWork.size());
			for (WorkExpe item : listWork) {
				WorkExpeDto workDto = new WorkExpeDto();
				BeanCopierUtils.copyProperties(item, workDto);
				listWorktDto.add( workDto);
			}
		}		
		logger.info("查询工作经验");
		return listWorktDto;
	}
	
	@Override
	@Transactional
	public void createWork(WorkExpeDto workExpeDto) {
		//WorkExpe findWork = workExpeRepo.findWorkByName(workExpeDto.getCompanyName());
		// 工作经验不存在	
		//if (findWork==null) {		
			WorkExpe work = new WorkExpe();
			BeanCopierUtils.copyProperties(workExpeDto, work);
			
			work.setWeID(UUID.randomUUID().toString());										
			Date now = new Date();
			work.setCreatedBy(SessionUtil.getLoginName());
			work.setModifiedBy(SessionUtil.getLoginName());
			work.setCreatedDate(now);
			work.setModifiedDate(now);
			
			work.setExpert(expertRepo.findById(workExpeDto.getExpertID()));
			workExpeRepo.save(work);				
			logger.info(String.format("添加工作经验,单位名称:%s", work.getCompanyName()));
		//} else {
		//	throw new IllegalArgumentException(String.format("公司名%s 已经存在", workExpeDto.getCompanyName()));
		//}
	}
		
	@Override
	@Transactional
	public void deleteWork(String id) {		
		WorkExpe workExpe = workExpeRepo.findById(id);
		if (workExpe != null) {
			workExpeRepo.delete(workExpe);
			logger.info(String.format("删除工作经验,单位名称为:%s", workExpe.getCompanyName()));			
		}
	}
	
	@Override
	@Transactional
	public void deleteWork(String[] ids) {
		for (String id : ids) {
			this.deleteWork(id);
		}
		logger.info("删除工作经验");
	}
	
	@Override
	@Transactional
	public void updateWork(WorkExpeDto workExpeDto) {
		WorkExpe workExpe = workExpeRepo.findById(workExpeDto.getWeID());
		BeanCopierUtils.copyPropertiesIgnoreNull(workExpeDto, workExpe);
		workExpeRepo.save(workExpe);
		logger.info(String.format("更新工作经验,单位名称为:%s", workExpeDto.getCompanyName()));
	}
}
