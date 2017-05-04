package cs.service;

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
import cs.domain.Dict;
import cs.domain.Expert;
import cs.domain.ProjectExpe;
import cs.domain.Role;
import cs.domain.User;
import cs.domain.WorkExpe;
import cs.model.DictDto;
import cs.model.ExpertDto;
import cs.model.PageModelDto;
import cs.model.ProjectExpeDto;
import cs.model.RoleDto;
import cs.model.UserDto;
import cs.model.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.DictRepo;
import cs.repository.repositoryImpl.ExpertRepo;
import cs.repository.repositoryImpl.ProjectExpeRepo;
import cs.repository.repositoryImpl.WorkExpeRepo;

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
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				expertDto.setIdCard(item.getIdCard());
				expertDto.setAcaDemy(item.getAcaDemy());
				expertDto.setAddRess(item.getAddRess());
				expertDto.setBirthDay(item.getBirthDay());
				expertDto.setComPany(item.getComPany());
				expertDto.setDegRee(item.getDegRee());
				expertDto.setEmail(item.getEmail());
				expertDto.setExpertID(item.getExpertID());
				expertDto.setExpeRttype(item.getExpeRttype());
				expertDto.setFax(item.getFax());
				expertDto.setCreateDate(item.getCreateDate());
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
				expertDto.setZipCode(item.getZipCode());
				
				List<WorkExpeDto> workDtoList=new ArrayList<>();
				for (WorkExpe workExpe : item.getWork()) {
					WorkExpeDto workDto=new WorkExpeDto();
					workDto.setWeID(workExpe.getWeID());
					workDto.setBeginTime(workExpe.getBeginTime());
					workDto.setExpertID(workExpe.getExpert().getExpertID());
					workDto.setEndTime(workExpe.getEndTime());
					workDto.setJob(workExpe.getJob());
					workDto.setCompanyName(workExpe.getCompanyName());
					workDtoList.add(workDto);
				}
				 expertDto.setWork(workDtoList);
				 
				 List<ProjectExpeDto> projectDtoList=new ArrayList<>();
				 for (ProjectExpe projectExpe : item.getProject()) {
					 ProjectExpeDto projectDto=new ProjectExpeDto();
						projectDto.setProjectbeginTime(projectExpe.getProjectbeginTime());
						projectDto.setProjectendTime(projectExpe.getProjectendTime());
						projectDto.setProjectName(projectExpe.getProjectName());
						projectDto.setProjectType(projectExpe.getProjectType());
						projectDto.setCreateTime(formatter.format(new Date()));
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
				expert.setExpertID(UUID.randomUUID().toString());
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
				expert.setCreatedBy(currentUser.getLoginName());
				expert.setState("1");
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
		
		@Override
		@Transactional
		public void updateExpert(ExpertDto expertDto) {
			Expert expert = expertRepo.findById(expertDto.getExpertID());
			expert.setIdCard(expertDto.getIdCard());
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



        
}
