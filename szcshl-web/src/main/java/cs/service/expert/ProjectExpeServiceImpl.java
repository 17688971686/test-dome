package cs.service.expert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.DateUtils;
import cs.domain.expert.Expert;
import cs.domain.expert.ProjectExpe;
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
			ProjectExpeDto projectDto=new ProjectExpeDto();
			projectDto.setProjectbeginTime(DateUtils.ConverToString(item.getProjectbeginTime()));
			projectDto.setProjectendTime(DateUtils.ConverToString(item.getProjectendTime()));
			projectDto.setProjectName(item.getProjectName());
			projectDto.setProjectType(item.getProjectType());
			projectDto.setPeID(item.getPeID());
			listProjectDto.add( projectDto);
		}
		logger.info("查找项目经验");
		return listProjectDto;
	}
     
    @Override
	@Transactional
	public void createProject(ProjectExpeDto projectExpeDto) {
    	Expert expert=expertRepo.findById(projectExpeDto.getExpertID());
		ProjectExpe project = new ProjectExpe();
		project.setPeID(UUID.randomUUID().toString());
		project.setProjectName(projectExpeDto.getProjectName());
		try {
			if(projectExpeDto.getProjectbeginTime()!=null){
				project.setProjectbeginTime(DateUtils.ConverToDate(projectExpeDto.getProjectbeginTime()));
			}
			if(projectExpeDto.getProjectendTime()!=null){
				project.setProjectendTime(DateUtils.ConverToDate(projectExpeDto.getProjectendTime()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		project.setProjectType(projectExpeDto.getProjectType());
		project.setExpert(expert);
		
		Date now = new Date();
		project.setCreatedBy(currentUser.getLoginName());
		project.setModifiedBy(currentUser.getLoginName());
		project.setCreatedDate(now);
		project.setModifiedDate(now);
		
		projectExpeRepo.save(project);
		logger.info(String.format("添加项目经验,项目名称为:%s", project.getProjectName()));
	}
		
    @Override
	@Transactional
	public void deleteProject(String id) {			
		ProjectExpe projectExpe = projectExpeRepo.findById(id);
		if (projectExpe != null) {
			projectExpeRepo.delete(projectExpe);
			logger.info(String.format("删除项目经验,单位名称为О:%s", projectExpe.getProjectName()));				
		}
	}
    
	@Override
	@Transactional
	public void deleteProject(String[] ids) {
		for (String id : ids) {
			this.deleteProject(id);
		}
		logger.info("删除项目经验");
	}
	
	@Override
	@Transactional
	public void updateProject(ProjectExpeDto projectExpeDto) {
		ProjectExpe projectExpe = projectExpeRepo.findById(projectExpeDto.getPeID());
		try {
			projectExpe.setProjectbeginTime (DateUtils.ConverToDate(projectExpeDto.getProjectbeginTime()));
			projectExpe.setProjectendTime(DateUtils.ConverToDate(projectExpeDto.getProjectendTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		projectExpe.setProjectName(projectExpeDto.getProjectName());
		projectExpe.setProjectType(projectExpeDto.getProjectType());
		
		projectExpe.setModifiedBy(currentUser.getLoginName());
		projectExpe.setModifiedDate(new Date());
		
		projectExpeRepo.save(projectExpe);
		logger.info(String.format("更新项目经验,单位名称为О:%s", projectExpeDto.getProjectName()));
	}
}
