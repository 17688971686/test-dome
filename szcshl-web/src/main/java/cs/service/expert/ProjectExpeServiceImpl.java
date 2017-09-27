package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.utils.BeanCopierUtils;
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

	/**
	 * 保存项目经历信息
	 * @param projectExpeDto
	 * @return
	 */
	@Override
	@Transactional
	public ResultMsg saveProject(ProjectExpeDto projectExpeDto) {
		ProjectExpe project = new ProjectExpe();
        Date now = new Date();
		if(!Validate.isString(projectExpeDto.getPeID())){
            projectExpeDto.setPeID(UUID.randomUUID().toString());
            projectExpeDto.setCreatedBy(SessionUtil.getDisplayName());
            projectExpeDto.setCreatedDate(now);
        }
        projectExpeDto.setModifiedBy(SessionUtil.getLoginName());
        projectExpeDto.setModifiedDate(now);

		BeanCopierUtils.copyProperties(projectExpeDto,project);
		project.setExpert(expertRepo.findById(projectExpeDto.getExpertID()));
		projectExpeRepo.save(project);
		return new ResultMsg(true , Constant.MsgCode.OK.getValue(),"保存成功！",projectExpeDto);
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
		
		projectExpe.setModifiedBy(SessionUtil.getLoginName());
		projectExpe.setModifiedDate(new Date());
		
		projectExpeRepo.save(projectExpe);
		logger.info(String.format("更新项目经验,单位名称为О:%s", projectExpeDto.getProjectName()));
	}
}
