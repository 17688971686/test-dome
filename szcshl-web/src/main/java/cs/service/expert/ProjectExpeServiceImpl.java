package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.domain.expert.Expert;
import cs.domain.expert.ProjectExpe;
import cs.domain.expert.ProjectExpe_;
import cs.model.expert.ProjectExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ProjectExpeRepo;
import cs.service.sys.UserServiceImpl;

@Service
public class ProjectExpeServiceImpl implements ProjectExpeService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
    private ExpertRepo expertRepo;
	@Autowired
    private ProjectExpeRepo projectExpeRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	public List<ProjectExpeDto> getProject(ODataObj odataObj) {
		List<ProjectExpe> projectExpeList = projectExpeRepo.findByOdata(odataObj);
		List<ProjectExpeDto> listProjectDto=new ArrayList<>();
		for (ProjectExpe item : projectExpeList) {
			ProjectExpeDto projectDto = new ProjectExpeDto();
			BeanCopierUtils.copyProperties(item,projectDto);			
			listProjectDto.add( projectDto);
		}
		logger.info("查找项目经验");
		return listProjectDto;
	}
     
    @Override
	@Transactional
	public void createProject(ProjectExpeDto projectExpeDto) {
		ProjectExpe project = new ProjectExpe();
		BeanCopierUtils.copyProperties(projectExpeDto,project);
		project.setPeID(UUID.randomUUID().toString());
		
		Date now = new Date();
		project.setCreatedBy(currentUser.getLoginName());
		project.setModifiedBy(currentUser.getLoginName());
		project.setCreatedDate(now);
		project.setModifiedDate(now);
		
		project.setExpert(expertRepo.findById(projectExpeDto.getExpertID()));
		projectExpeRepo.save(project);
		logger.info(String.format("添加项目经验,项目名称为:%s", project.getProjectName()));
	}
		
    @Override
	@Transactional
	public void deleteProject(String id) {	
    	
    	String[] ids=id.split(",");
    	for(String projectId:ids){
    		ProjectExpe projectExp=projectExpeRepo.findById(projectId);
    		if(projectExp!=null){
    			projectExpeRepo.delete(projectExp);
    		}
    	}
//    	projectExpeRepo.deleteById(ProjectExpe_.peID.getName(), id);		
	}
	
	@Override
	@Transactional
	public void updateProject(ProjectExpeDto projectExpeDto) {
		ProjectExpe projectExpe = projectExpeRepo.findById(projectExpeDto.getPeID());
		
		BeanCopierUtils.copyProperties(projectExpeDto,projectExpe);
		
		projectExpe.setModifiedBy(currentUser.getLoginName());
		projectExpe.setModifiedDate(new Date());
		
		projectExpeRepo.save(projectExpe);
		logger.info(String.format("更新项目经验,单位名称为О:%s", projectExpeDto.getProjectName()));
	}
}
