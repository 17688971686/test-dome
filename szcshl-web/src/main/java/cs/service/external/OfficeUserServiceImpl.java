package cs.service.external;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.external.Dept;
import cs.domain.external.OfficeUser;
import cs.domain.sys.Org;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.repository.repositoryImpl.external.OfficeUserRepo;
import cs.service.sys.UserServiceImpl;

@Service
public class OfficeUserServiceImpl implements OfficeUserService{
	private static Logger logger = Logger.getLogger(OfficeUserServiceImpl.class);
	@Autowired
	private OfficeUserRepo officeUserRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private DeptRepo deptRepo;
	@Override
	public PageModelDto<OfficeUserDto> get(ODataObj odataObj) {
		PageModelDto<OfficeUserDto> pageModelDto = new PageModelDto<OfficeUserDto>();
		List<OfficeUser> OfficeUserList = officeUserRepo.findByOdata(odataObj);
		List<OfficeUserDto> resultDtoList = new ArrayList<OfficeUserDto>(OfficeUserList.size());
		
		if(OfficeUserList != null && OfficeUserList.size() > 0){
			OfficeUserList.forEach(x->{
				OfficeUserDto modelDto = new OfficeUserDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				//cannot copy 
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());
				
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(OfficeUserDto record) {
		OfficeUser domain = new OfficeUser(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		domain.setOfficeID(UUID.randomUUID().toString());
		//添加所在办事处
		if(Validate.isString(record.getDepartId())){
			Dept dept =deptRepo.findById(record.getDepartId());
			domain.setDept(dept);
		}
		officeUserRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(OfficeUserDto record) {
		OfficeUser domain = officeUserRepo.findById(record.getOfficeID());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		officeUserRepo.save(domain);
	}

	@Override
	public OfficeUserDto findById(String id) {	
		OfficeUserDto modelDto = new OfficeUserDto();
		if(Validate.isString(id)){
			OfficeUser domain = officeUserRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		OfficeUser office = officeUserRepo.getById(id);
		if (office != null) {
			officeUserRepo.delete(office);
			logger.info(String.format("删除办事处", office.getOfficeID()));
		}
		
		
	}
	@Override
	@Transactional
	public void deletes(String[] ids) {
		for (String id : ids) {
			this.delete(id);
		}
		logger.info("批量删除办事处联系人");
	}
	@Override
	public List<OfficeUserDto> officeUserList() {
		
		List<OfficeUser> officeUserList=officeUserRepo.findAll();
		List<OfficeUserDto> officeUserDtoList= new ArrayList<>();
		if(officeUserList !=null && officeUserList.size() >0){
			officeUserList.forEach(x->{
				OfficeUserDto officeUserDto = new OfficeUserDto();
				BeanCopierUtils.copyProperties(x, officeUserDto);
				officeUserDto.setCreatedDate(x.getCreatedDate());
				officeUserDto.setModifiedDate(x.getModifiedDate());
				officeUserDtoList.add(officeUserDto);
			});
			
		}
		return officeUserDtoList;
	}

	@Override
	public List<DeptDto> getDepts() {
		List<Dept> depts=deptRepo.findAll();
		List<DeptDto> deptDtos = new ArrayList<>();
		if(depts !=null && depts.size() >0){
			depts.forEach(x->{
				DeptDto deptDto = new DeptDto();
				BeanCopierUtils.copyProperties(x, deptDto);
				deptDto.setCreatedDate(x.getCreatedDate());
				deptDto.setModifiedDate(x.getModifiedDate());
				deptDtos.add(deptDto);
			});
		}
		return deptDtos;
	}

	@Override
	public List<OfficeUserDto> findOfficeUserByDeptId(String deptId) {
		List<OfficeUser> officelist=officeUserRepo.findOfficeUserByDeptId(deptId);
		List<OfficeUserDto> officeUserDtos=new ArrayList<>();
		if(officelist!=null && officelist.size()>0){
			officelist.forEach(x ->{
				OfficeUserDto officeUserDto = new OfficeUserDto();
				BeanCopierUtils.copyProperties(x, officeUserDto);
				officeUserDto.setCreatedDate(x.getCreatedDate());
				officeUserDto.setModifiedBy(x.getModifiedBy());
				officeUserDtos.add(officeUserDto);
			});
		}
		return officeUserDtos;
	}

	

	
	
}
