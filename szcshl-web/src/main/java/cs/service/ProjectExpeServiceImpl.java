package cs.service;

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
import cs.domain.Expert;
import cs.domain.ProjectExpe;
import cs.model.ProjectExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.ExpertRepo;
import cs.repository.repositoryImpl.ProjectExpeRepo;

@Service
public class ProjectExpeServiceImpl implements ProjectExpeService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	 @Autowired
     private ExpertRepo expertRepo;
	 @Autowired
     private ProjectExpeRepo projectExpeRepo;
     @Autowired
     private ICurrentUser currentUser;
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public List<ProjectExpeDto> getProject(ODataObj odataObj) {
		List<ProjectExpe> listWork = projectExpeRepo.findByOdata(odataObj);
		//PageModelDto<WorkExpeDto> pageModelDto = new PageModelDto<>();
		List<ProjectExpeDto> listProjectDto=new ArrayList<>();
		for (ProjectExpe item : listWork) {
			ProjectExpeDto projectDto=new ProjectExpeDto();
			projectDto.setProjectbeginTime(item.getProjectbeginTime());
			projectDto.setProjectendTime(item.getProjectendTime());
			projectDto.setProjectName(item.getProjectName());
			projectDto.setProjectType(item.getProjectType());
			projectDto.setCreateTime(formatter.format(new Date()));
			projectDto.setPeID(item.getPeID());
			listProjectDto.add( projectDto);
		}
		//pageModelDto.setCount(odataObj.getCount());
		//pageModelDto.setValue(listWorktDto);
		logger.info("查找项目经验");
		return listProjectDto;
	}
     
     @Override
		@Transactional
		public void createProject(ProjectExpeDto projectExpeDto) {
    	 ProjectExpe findProject=projectExpeRepo.findProjectByName(projectExpeDto.getProjectName());
			if (findProject==null) {// 项目经验不存在
				Expert expert=expertRepo.findById(projectExpeDto.getExpertID());
				ProjectExpe project = new ProjectExpe();
				project.setPeID(UUID.randomUUID().toString());
				project.setProjectName(projectExpeDto.getProjectName());
				project.setProjectbeginTime(projectExpeDto.getProjectbeginTime());
				project.setProjectendTime(projectExpeDto.getProjectendTime());
				project.setProjectType(projectExpeDto.getProjectType());
				project.setExpert(expert);
				project.setCreateTime(formatter.format(new Date()));
				projectExpeRepo.save(project);
				logger.info(String.format("添加羡慕经验,项目名称为:%s", project.getProjectName()));
			} else {
				throw new IllegalArgumentException(String.format("项目为%s 已存在,请重新输入", projectExpeDto.getProjectName()));
			}
		}
		
     @Override
		@Transactional
		public void deleteProject(String id) {
			
			ProjectExpe projectExpe = projectExpeRepo.findById(id);
			if (projectExpe != null) {
				projectExpeRepo.delete(projectExpe);
				logger.info(String.format("鍒犻櫎椤圭洰缁忛獙,椤圭洰鍚嶇О:%s", projectExpe.getProjectName()));
				
			}
		}
		@Override
		@Transactional
		public void deleteProject(String[] ids) {
			for (String id : ids) {
				this.deleteProject(id);
			}
			logger.info("鎵归噺鍒犻櫎涓撳");
		}
		
		@Override
		@Transactional
		public void updateProject(ProjectExpeDto projectExpeDto) {
			ProjectExpe projectExpe = projectExpeRepo.findById(projectExpeDto.getPeID());
			projectExpe.setProjectbeginTime (projectExpeDto.getProjectbeginTime());
			projectExpe.setProjectendTime(projectExpeDto.getProjectendTime());
			projectExpe.setProjectName(projectExpeDto.getProjectName());
			projectExpe.setProjectType(projectExpeDto.getProjectType());
			projectExpeRepo.save(projectExpe);
			logger.info(String.format("鏇存柊椤圭洰缁忛獙,椤圭洰鍚嶇О:%s", projectExpeDto.getProjectName()));
		}
}
