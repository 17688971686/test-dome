package cs.service.expert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.expert.Expert;
import cs.domain.expert.ProjectExpe;
import cs.domain.expert.WorkExpe;
import cs.domain.project.Sign;
import cs.domain.sys.Dict;
import cs.domain.sys.Role;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ProjectExpeDto;
import cs.model.expert.WorkExpeDto;
import cs.model.project.SignDto;
import cs.model.sys.DictDto;
import cs.model.sys.RoleDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ProjectExpeRepo;
import cs.repository.repositoryImpl.expert.WorkExpeRepo;
import cs.repository.repositoryImpl.sys.DictRepo;
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
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		@Override
		public List<ExpertDto> findAll() {
			
			return null;
		}
	
		@Override
		public PageModelDto<ExpertDto> get(ODataObj odataObj) {
			List<Expert> listExpert = expertRepo.findByOdata(odataObj);
			PageModelDto<ExpertDto> pageModelDto = new PageModelDto<>();
			List<ExpertDto> listExpertDto=new ArrayList<>();
			for (Expert item : listExpert) {
				ExpertDto expertDto=new ExpertDto();
				/*expertDto.setIdCard(item.getIdCard());
				expertDto.setAcaDemy(item.getAcaDemy());
				expertDto.setAddRess(item.getAddRess());
				expertDto.setComPany(item.getComPany());
				expertDto.setDegRee(item.getDegRee());
				expertDto.setEmail(item.getEmail());
				expertDto.setExpertID(item.getExpertID());
				expertDto.setExpeRttype(item.getExpeRttype());
				expertDto.setFax(item.getFax());
				expertDto.setName(item.getName());
				expertDto.setSex(item.getSex());
				expertDto.setPhone(item.getPhone());
				expertDto.setUserPhone(item.getUserPhone());
				expertDto.setMaJor(item.getMaJor());
				expertDto.setTitle(item.getTitle());
				expertDto.setJob(item.getJob());
				expertDto.setProcoSttype(item.getProcoSttype());
				expertDto.setProteChtype(item.getProteChtype());
				expertDto.setRemark(item.getRemark());
				expertDto.setQualifiCations(item.getQualifiCations());
				expertDto.setZipCode(item.getZipCode());*/
				ExpertToExpertDto(item, expertDto);
				if(item.getBirthDay()!=null){
					expertDto.setBirthDay(formatter.format(item.getBirthDay()));
				}
				if(item.getCreateDate()!=null){
					expertDto.setCreateDate(formatter.format(item.getCreateDate()));
				}
				List<WorkExpeDto> workDtoList=new ArrayList<>();
				for (WorkExpe workExpe : item.getWork()) {
					WorkExpeDto workDto=new WorkExpeDto();
					workDto.setWeID(workExpe.getWeID());
					workDto.setBeginTime(formatter.format(workExpe.getBeginTime()));
					workDto.setEndTime(formatter.format(workExpe.getEndTime()));
					workDto.setExpertID(workExpe.getExpert().getExpertID());
					workDto.setJob(workExpe.getJob());
					workDto.setCompanyName(workExpe.getCompanyName());
					workDtoList.add(workDto);
				}
				 expertDto.setWork(workDtoList);
				 
				 List<ProjectExpeDto> projectDtoList=new ArrayList<>();
				 for (ProjectExpe projectExpe : item.getProject()) {
					 ProjectExpeDto projectDto=new ProjectExpeDto();
						projectDto.setProjectbeginTime(formatter.format(projectExpe.getProjectbeginTime()));
						projectDto.setProjectendTime(formatter.format(projectExpe.getProjectendTime()));
						projectDto.setProjectName(projectExpe.getProjectName());
						projectDto.setProjectType(projectExpe.getProjectType());
						projectDto.setPeID(projectExpe.getPeID());
						projectDtoList.add(projectDto);
				 }
				 
				 expertDto.setProject(projectDtoList);
				 listExpertDto.add(expertDto);
			}
			pageModelDto.setCount(odataObj.getCount());
			pageModelDto.setValue(listExpertDto);
			return pageModelDto;
		}
		
		@Override
		@Transactional
		public String createExpert(ExpertDto expertDto) {
			Expert findExpert=expertRepo.findExpertByName(expertDto.getName());
			Expert expert = new Expert();
			if (findExpert==null) {// 重复专家查询
				/*expert.setExpertID(UUID.randomUUID().toString());
				expert.setIdCard(expertDto.getIdCard());
				expert.setAcaDemy(expertDto.getAcaDemy());
				expert.setAddRess(expertDto.getAddRess());
				expert.setBirthDay(expertDto.getBirthDay());
				expert.setComPany(expertDto.getComPany());
				expert.setDegRee(expertDto.getDegRee());
				expert.setEmail(expertDto.getEmail());
				expert.setExpeRttype(expertDto.getExpeRttype());
				expert.setFax(expertDto.getFax());
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
				expert.setModifiedBy(currentUser.getLoginName());
				expert.setCreatedBy(currentUser.getLoginName());*/
				ExpertDtoToExpert(expertDto,expert);
				try {
					if(expertDto.getCreateDate()!=null){
					expert.setCreateDate(formatter.parse(expertDto.getCreateDate()));
					}
					if(expertDto.getBirthDay()!=null){
					expert.setBirthDay(formatter.parse(expertDto.getBirthDay()));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				expert.setState("1");
				expert.setExpertID(UUID.randomUUID().toString());
				expertRepo.save(expert);
				//expert=expertRepo.findExpertByName(expertDto.getName());
				logger.info(String.format("添加专家,专家名为:%s", expert.getName()));
			} else {
				throw new IllegalArgumentException(String.format("专家名%s 已存在,请重新输入", expertDto.getName()));
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
			//WorkExpe work=workExpeRepo.delete(entity);
			
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
		public void updateAudit(String[] ids,String flag) {
			
			for (String id : ids) {
				Expert expert = expertRepo.findById(id);
				switch (Integer.parseInt(flag)) {
				case 1:
					expert.setState("2");
					break;
				case 2:
					expert.setState("3");
					break;
				case 3:
					expert.setState("4");
					break;
				case 4:
					expert.setState("5");
					break;
				case 5:
					expert.setState("1");
					break;
					
				default:
					break;
				}
				expertRepo.save(expert);
				expert.setModifiedBy(currentUser.getLoginName());
			}
			logger.info("专家审核");
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
			expert.setModifiedDate(new Date());
			expert.setModifiedBy(currentUser.getLoginName());*/
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



        
}
