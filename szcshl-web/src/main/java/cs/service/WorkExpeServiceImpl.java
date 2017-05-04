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

import cs.domain.Expert;
import cs.domain.WorkExpe;
import cs.model.ExpertDto;
import cs.model.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.ExpertRepo;
import cs.repository.repositoryImpl.WorkExpeRepo;

@Service
public class WorkExpeServiceImpl implements WorkExpeService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
    private ExpertRepo expertRepo;
	@Autowired
    private WorkExpeRepo workExpeRepo;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public List<WorkExpeDto> getWork(ODataObj odataObj) {
		List<WorkExpe> listWork = workExpeRepo.findByOdata(odataObj);
		//PageModelDto<WorkExpeDto> pageModelDto = new PageModelDto<>();
		List<WorkExpeDto> listWorktDto=new ArrayList<>();
		for (WorkExpe item : listWork) {
			WorkExpeDto workDto=new WorkExpeDto();
			workDto.setWeID(item.getWeID());
			workDto.setBeginTime(item.getBeginTime());
			workDto.setExpertID(item.getExpert().getExpertID());
			workDto.setEndTime(item.getEndTime());
			workDto.setJob(item.getJob());
			workDto.setCompanyName(item.getCompanyName());
			listWorktDto.add( workDto);
		}
		//pageModelDto.setCount(odataObj.getCount());
		//pageModelDto.setValue(listWorktDto);
		logger.info("查询工作经验");
		return listWorktDto;
	}
	@Override
	@Transactional
	public void createWork(WorkExpeDto workExpeDto) {
		WorkExpe findWork=workExpeRepo.findWorkByName(workExpeDto.getCompanyName());
		if (findWork==null) {// 工作经验不存在
			
			WorkExpe work = new WorkExpe();
			Expert expert = expertRepo.findById(workExpeDto.getExpertID());
			work.setWeID(UUID.randomUUID().toString());
			work.setBeginTime(workExpeDto.getBeginTime());
			work.setEndTime(workExpeDto.getEndTime());
			work.setCompanyName(workExpeDto.getCompanyName());
			work.setJob(workExpeDto.getJob());
			work.setCreate_Time(formatter.format(new Date()));
			work.setExpert(expert);
			workExpeRepo.save(work);
				
			logger.info(String.format("添加工作经验,单位名称:%s", work.getCompanyName()));
		} else {
			throw new IllegalArgumentException(String.format("公司名%s 已经存在", workExpeDto.getCompanyName()));
		}
	}
	@Override
	@Transactional
	public void createWork(WorkExpe workExpe) {
		WorkExpe findWork=workExpeRepo.findWorkByName(workExpe.getCompanyName());
		if (findWork==null) {// 工作经验不存在
			//Expert expert=new Expert();
			workExpeRepo.save(workExpe);
			logger.info(String.format("添加工作经验,单位名称:%s", workExpe.getCompanyName()));
		} else {
			throw new IllegalArgumentException(String.format("公司名%s 已经存在", workExpe.getCompanyName()));
		}
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
		workExpe.setBeginTime(workExpeDto.getBeginTime());
		workExpe.setEndTime(workExpeDto.getEndTime());
		workExpe.setCompanyName(workExpeDto.getCompanyName());
		workExpe.setJob(workExpeDto.getJob());
		workExpeRepo.save(workExpe);
		logger.info(String.format("更新工作经验,单位名称为:%s", workExpeDto.getCompanyName()));
	}
}
