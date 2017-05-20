package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant.EnumExpertState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.NumIncreaseUtils;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.Expert_;
import cs.domain.expert.ProjectExpe;
import cs.domain.expert.WorkExpe;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ProjectExpeDto;
import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ProjectExpeRepo;
import cs.repository.repositoryImpl.expert.WorkExpeRepo;
import cs.service.sys.UserServiceImpl;

@Service
public class ExpertServiceImpl implements ExpertService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private ExpertRepo expertRepo;
	@Autowired
	private WorkExpeRepo workExpeRepo;
	@Autowired
	private ProjectExpeRepo projectExpeRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	public PageModelDto<ExpertDto> get(ODataObj odataObj) {
		List<Expert> listExpert = expertRepo.findByOdata(odataObj);
		PageModelDto<ExpertDto> pageModelDto = new PageModelDto<>();
		List<ExpertDto> listExpertDto = new ArrayList<>();
		for (Expert item : listExpert) {
			ExpertDto expertDto = new ExpertDto();
			BeanCopierUtils.copyProperties(item, expertDto);	
			listExpertDto.add(expertDto);
		}
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(listExpertDto);
		return pageModelDto;
	}		
		
	@Override
	@Transactional
	public String createExpert(ExpertDto expertDto) {
		List<Expert> list = expertRepo.findExpertByIdCard(expertDto.getIdCard());
		
		if (list == null || list.size() == 0) {// 重复专家查询
			Expert expert = new Expert();
			BeanCopierUtils.copyProperties(expertDto, expert);
			//设置默认属性
			expert.setState(EnumExpertState.AUDITTING.getValue());
			expert.setExpertID(UUID.randomUUID().toString());
			expert.setExpertNo(NumIncreaseUtils.getExpertNo());
			Date now = new Date();
			expert.setCreatedDate(now);
			expert.setCreatedBy(currentUser.getLoginName());
			expert.setModifiedBy(currentUser.getLoginName());
			expert.setModifiedDate(now);
			//设置返回值
			expertDto.setExpertID(expert.getExpertID());
			expertRepo.save(expert);
			logger.info(String.format("添加专家,专家名为:%s", expert.getName()));
		} else {
			throw new IllegalArgumentException(String.format("身份证号为%s 的专家已存在,请重新输入", expertDto.getIdCard()));
		}
		return expertDto.getExpertID();
	}

	@Override
	@Transactional
	public void deleteExpert(String id) {		
		Expert expert = expertRepo.findById(id);
		if (expert != null) {
			List<WorkExpe> workList = expert.getWork();
			for (WorkExpe workExpe : workList) {
				workExpeRepo.delete(workExpe);
			}
			List<ProjectExpe> projectList = expert.getProject();
			for (ProjectExpe projectExpe : projectList) {
				projectExpeRepo.delete(projectExpe);
			}
			expertRepo.delete(expert);
			logger.info(String.format("删除专家,专家名为:%s", expert.getName()));
		}			
	}
	
	@Override
	@Transactional
	public void deleteExpert(String[] ids) {
		for (String id : ids) {
			this.deleteExpert(id);
		}
		logger.info("删除专家");
	}
	
	@Override
	@Transactional
	public void updateAudit(String ids,String state) {	
		if(Validate.isString(ids)){
			HqlBuilder hqlBuilder = HqlBuilder.create();
			hqlBuilder.append(" update "+Expert.class.getSimpleName() +" set "+ Expert_.state.getName() + " = :state ");
			hqlBuilder.setParam("state", state);
			String[] idArr = ids.split(",");
	        if (idArr.length > 1) {
	        	hqlBuilder.append( " where "+Expert_.expertID.getName()+" in ( ");	  
	        	int totalL = idArr.length;
	        	for(int i=0;i<totalL;i++){
	        		if(i==totalL-1){
	        			hqlBuilder.append(" :id"+i).setParam("id"+i, idArr[i]);
	        		}else{
	        			hqlBuilder.append(" :id"+i+",").setParam("id"+i, idArr[i]);
	        		}	        		
	        	}
	        	hqlBuilder.append(" )");
	        } else {
	        	hqlBuilder.append( " where "+Expert_.expertID.getName()+" = :id ");
	        	hqlBuilder.setParam("id", ids);
	        }
	        expertRepo.executeHql(hqlBuilder);
	        logger.info("专家审核");
		}						
	}
	
	@Override
	@Transactional
	public void updateExpert(ExpertDto expertDto) {
		Expert expert = expertRepo.findById(expertDto.getExpertID());
		BeanCopierUtils.copyPropertiesIgnoreNull(expertDto, expert);
		
		expert.setModifiedDate(new Date());
		expert.setModifiedBy(currentUser.getLoginName());
		expertRepo.save(expert);
		logger.info(String.format("更新专家,专家名为:%s", expertDto.getName()));
	}

	@Override
	public ExpertDto findById(String id) {			
		Expert expert = expertRepo.findById(id);
		ExpertDto expertDto = new ExpertDto();
		if(expert != null){
			BeanCopierUtils.copyProperties(expert, expertDto);
			//工作经验
			if(expert.getWork() != null && expert.getWork().size() > 0){
				List<WorkExpeDto> workDtoList=new ArrayList<>(expert.getWork().size());
				expert.getWork().forEach(ew ->{
					WorkExpeDto workDto=new WorkExpeDto();
					BeanCopierUtils.copyProperties(ew, workDto);				
					workDtoList.add(workDto);
				});
				expertDto.setWork(workDtoList);
			}
			//项目经验
			if(expert.getProject() != null && expert.getProject().size() > 0){
				List<ProjectExpeDto> projectDtoList=new ArrayList<>(expert.getProject().size());
				(expert.getProject()).forEach(ep ->{
					ProjectExpeDto projectDto=new ProjectExpeDto();
					BeanCopierUtils.copyProperties(ep, projectDto);							
					projectDtoList.add(projectDto);
				});
				expertDto.setProject(projectDtoList);
			}			 			
		}
		return expertDto;
	}

	/**
	 * 查询重名专家
	 */
	@Override
	public List<ExpertDto> findAllRepeat() {	
		List<Expert> list = expertRepo.findAllRepeat();
		List<ExpertDto> dtoList = new ArrayList<ExpertDto>();
		if(list != null && list.size() > 0){
			list.forEach(el ->{
				ExpertDto expertDto = new ExpertDto();
				BeanCopierUtils.copyProperties(el, expertDto);
				dtoList.add(expertDto);
			});
		}
		return dtoList;
	}
}
