package cs.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.sys.Workday;
import cs.domain.sys.Workday_;
import cs.model.PageModelDto;
import cs.model.sys.WorkdayDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.WorkdayRepo;

@Service
public class WorkdayServiceImpl implements WorkdayService{

	@Autowired
	private WorkdayRepo workdayRepo;
	
	@Autowired
	private ICurrentUser currentUser;
	@Override
	@Transactional
	public PageModelDto<WorkdayDto> getWorkday(ODataObj odataObj) {

		PageModelDto<WorkdayDto> pageDtoList=new PageModelDto<>();
		List<Workday> workdayList=workdayRepo.findByOdata(odataObj);
		List<WorkdayDto> workdayDtoList=new ArrayList<>();
		if(workdayList !=null && workdayList.size()>0){
			for(Workday workday:workdayList){
				WorkdayDto workdayDto=new WorkdayDto();
				BeanCopierUtils.copyProperties(workday, workdayDto);
				workdayDtoList.add(workdayDto);
			}

		}
		pageDtoList.setValue(workdayDtoList);
		pageDtoList.setCount(odataObj.getCount());
		return pageDtoList;
	}

	@Override
	@Transactional
	public void createWorkday(WorkdayDto workdayDto) {
//		boolean isExist=workdayRepo.isExist(workdayDto.getDates());
//		if(!isExist){
		Workday workday=new Workday();
		BeanCopierUtils.copyProperties(workdayDto, workday);
		workday.setId(UUID.randomUUID().toString());
		workday.setCreatedBy(currentUser.getLoginName());
		workday.setCreatedDate(new Date());
		workday.setModifiedBy(currentUser.getLoginName());
		workday.setModifiedDate(new Date());
		workdayRepo.save(workday);
//		}else{
//			throw new IllegalArgumentException(String.format("%s 已经添加,请重新输入！",	workdayDto.getDates()));
//		}
	}

	@Override
	@Transactional
	public WorkdayDto getWorkdayById(String id) {
		
		Workday workday=workdayRepo.findById(id);
		WorkdayDto workdayDto=new WorkdayDto();
		
		BeanCopierUtils.copyProperties(workday, workdayDto);
		
		return workdayDto;
	}

	@Override
	@Transactional
	public void updateWorkday(WorkdayDto workdayDto) {
		Workday workday=workdayRepo.findById(workdayDto.getId());
		BeanCopierUtils.copyProperties(workdayDto, workday);
		workday.setModifiedBy(currentUser.getLoginName());
		workday.setModifiedDate(new Date());
		workdayRepo.save(workday);
	}

	@Override
	@Transactional
	public void deleteWorkday(String id) {
		String[] ids=id.split(",");
		if(ids.length>0){
			for(String workdayId:ids){
				Workday workday=workdayRepo.findById(workdayId);
				workdayRepo.delete(workday);
			}
		}
		
		
	}

	@Override
	@Transactional
	public List<Workday> selectSpecialDays(String status) {
		HqlBuilder hqlBuilder=HqlBuilder.create();
		hqlBuilder.append("select t from "+Workday.class.getSimpleName()+" t where t."+Workday_.status.getName()+"=:status");
//		hqlBuilder.append("select t.dates from CS_WORKDAY t where t.status=:status");
		hqlBuilder.setParam("status", status);
		List<Workday> datesList=workdayRepo.findByHql(hqlBuilder);
		return datesList;
	}



}
