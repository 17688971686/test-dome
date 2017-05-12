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
import cs.common.utils.DateUtils;
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
	public List<ExpertDto> findAll() {
		
		return null;
	}
	
	@Override
	public PageModelDto<ExpertDto> get(ODataObj odataObj) {
		List<Expert> listExpert = expertRepo.findByOdata(odataObj);
		PageModelDto<ExpertDto> pageModelDto = new PageModelDto<>();
		List<ExpertDto> listExpertDto = new ArrayList<>();
		for (Expert item : listExpert) {
			ExpertDto expertDto=new ExpertDto();
			ExpertToExpertDto(item, expertDto);
			if(item.getBirthDay() != null){
				expertDto.setBirthDay(DateUtils.toString(item.getBirthDay()));
			}
			if(item.getGraduateDate() != null){
				expertDto.setGraduateDate(DateUtils.toString(item.getGraduateDate()));
			}
			 listExpertDto.add(expertDto);
		}
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(listExpertDto);
		return pageModelDto;
	}		
		
	@Override
	@Transactional
	public String createExpert(ExpertDto expertDto) {
		Expert findExpert=expertRepo.findExpertByName(expertDto.getIdCard());
		Expert expert = new Expert();
		if (findExpert==null) {// 重复专家查询
			ExpertDtoToExpert(expertDto,expert);
			try {
				if(expertDto.getGraduateDate() != null){
					expert.setGraduateDate(DateUtils.ConverToDate(expertDto.getGraduateDate()));
				}
				if(expertDto.getBirthDay() != null){
					expert.setBirthDay(DateUtils.ConverToDate(expertDto.getBirthDay()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			expert.setState(EnumExpertState.AUDITTING.getValue());
			expert.setExpertID(UUID.randomUUID().toString());
			expert.setExpertNo(NumIncreaseUtils.getExpertNo());
			expertRepo.save(expert);
			logger.info(String.format("添加专家,专家名为:%s", expert.getName()));
		} else {
			throw new IllegalArgumentException(String.format("身份证号为%s 的专家已存在,请重新输入", expertDto.getIdCard()));
		}
		return expert.getExpertID();
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
	
	
	private void ExpertDtoToExpert(ExpertDto expertDto,Expert expert){
		BeanCopierUtils.copyProperties(expertDto, expert);
        Date now = new Date();
        expert.setCreatedBy(currentUser.getLoginName());
        expert.setCreatedDate(now);
        expert.setModifiedBy(currentUser.getLoginName());
        expert.setModifiedDate(now);
	}
	
	private void ExpertToExpertDto(Expert expert,ExpertDto expertDto){
		BeanCopierUtils.copyProperties(expert, expertDto);
		expertDto.setCreatedDate(expert.getCreatedDate());
		expertDto.setModifiedDate(expert.getModifiedDate());
	}
	@Override
	@Transactional
	public void updateExpert(ExpertDto expertDto) {
		Expert expert = expertRepo.findById(expertDto.getExpertID());
		/*List<WorkExpeDto> workDtoList=expertDto.getWork();
		List<WorkExpe> workList=new ArrayList<>();
		WorkExpe work=new WorkExpe();
		for (WorkExpeDto workExpeDto : workDtoList) {
			BeanCopierUtils.copyPropertiesIgnoreNull(workExpeDto, work);
			workList.add(work);
		}
		List<ProjectExpeDto> projectDtoList=expertDto.getProject();
		List<ProjectExpe> projectList=new ArrayList<>();
		ProjectExpe project=new ProjectExpe();
		for (ProjectExpeDto projectDto : projectDtoList) {
			BeanCopierUtils.copyPropertiesIgnoreNull(projectDto, project);
			projectList.add(project);
		}
		expert.setWork(workList);
		expert.setProject(projectList);
		*/
		BeanCopierUtils.copyPropertiesIgnoreNull(expertDto, expert);
		
		/*expert.setIdCard(expertDto.getIdCard());
		expert.setAcaDemy(expertDto.getAcaDemy());
		expert.setAddRess(expertDto.getAddRess());
		expert.setBirthDay(expertDto.getBirthDay());
		expert.setComPany(expertDto.getComPany());
		expert.setDegRee(expertDto.getDegRee());
		expert.setEmail(expertDto.getEmail());
		expert.setExpeRttype(expertDto.getExpeRttype());
		expert.setCreateDate(expertDto.getCreateDate());
		expert.setName(expertDto.getName());
		expert.setSex(expertDto.getSex());
		expert.setPhone(expertDto.getPhone());
		expert.setUserPhone(expertDto.getUserPhone());
		expert.setMaJor(expertDto.getMaJor());
		expert.setTitle(expertDto.getTitle());
		expert.setJob(expertDto.getJob());
		expert.setProcoSttype(expertDto.getProcoSttype());
		expert.setProteChtype(expertDto.getProteChtype());
		expert.setRemark(expertDto.getRemark());
		expert.setQualifiCations(expertDto.getQualifiCations());
		expert.setZipCode(expertDto.getZipCode());
		*/
		expert.setModifiedDate(new Date());
		expert.setModifiedBy(currentUser.getLoginName());
		expertRepo.save(expert);
		logger.info(String.format("更新专家,专家名为:%s", expertDto.getName()));
	}
	@Override
	public Expert findExpertByName(String expertName) {
		return expertRepo.findExpertByName(expertName);
	}

	@Override
	public PageModelDto<ExpertDto> searchMuti(ExpertDto expertDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpertDto findById(String id) {			
		Expert expert = expertRepo.findById(id);
		ExpertDto expertDto = new ExpertDto();
		if(expert != null){
			BeanCopierUtils.copyProperties(expert, expertDto);
			if(expert.getBirthDay() != null){
				expertDto.setBirthDay(DateUtils.toString(expert.getBirthDay()));
			}
			if(expert.getGraduateDate() != null){
				expertDto.setGraduateDate(DateUtils.toString(expert.getGraduateDate()));
			}
			List<WorkExpeDto> workDtoList=new ArrayList<>();
			for (WorkExpe workExpe : expert.getWork()) {
				WorkExpeDto workDto=new WorkExpeDto();
				workDto.setWeID(workExpe.getWeID());
				workDto.setBeginTime(DateUtils.toString(workExpe.getBeginTime()));
				workDto.setEndTime(DateUtils.toString(workExpe.getEndTime()));
				workDto.setExpertID(workExpe.getExpert().getExpertID());
				workDto.setJob(workExpe.getJob());
				workDto.setCompanyName(workExpe.getCompanyName());
				workDtoList.add(workDto);
			}
			 expertDto.setWork(workDtoList);
			 
			 List<ProjectExpeDto> projectDtoList=new ArrayList<>();
			 for (ProjectExpe projectExpe : expert.getProject()) {
				 ProjectExpeDto projectDto=new ProjectExpeDto();
					projectDto.setProjectbeginTime(DateUtils.toString(projectExpe.getProjectbeginTime()));
					projectDto.setProjectendTime(DateUtils.toString(projectExpe.getProjectendTime()));
					projectDto.setProjectName(projectExpe.getProjectName());
					projectDto.setProjectType(projectExpe.getProjectType());
					projectDto.setPeID(projectExpe.getPeID());
					projectDtoList.add(projectDto);
			 }
			 
			 expertDto.setProject(projectDtoList);
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
