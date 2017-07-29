package cs.service.external;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.utils.SessionUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant.EnumState;
import cs.common.utils.BeanCopierUtils;
import cs.domain.external.Dept;
import cs.domain.external.OfficeUser;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.repository.repositoryImpl.external.OfficeUserRepo;

@Service
public class DeptServiceImpl implements DeptService {

	private static Logger logger = Logger.getLogger(DeptServiceImpl.class);
	@Autowired
	private DeptRepo deptRepo;
	@Autowired
	private OfficeUserRepo officeUserRepo;
	@Override
	public PageModelDto<DeptDto> get(ODataObj odataObj) {
		List<Dept> deptList = deptRepo.findByOdata(odataObj);
		List<DeptDto> deptDtoList = new ArrayList<DeptDto>();
		
			deptList.forEach(x->{
				DeptDto deptDto = new DeptDto();
				BeanCopierUtils.copyProperties(x, deptDto);
				deptDto.setCreatedDate(x.getCreatedDate());
				deptDto.setModifiedDate(x.getModifiedDate());
				
				deptDtoList.add(deptDto);
			});		
		PageModelDto<DeptDto> pageModelDto = new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(deptDtoList);		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(DeptDto record) {
		Dept dept = new Dept(); 
		BeanCopierUtils.copyProperties(record, dept); 
		Date now = new Date();
		dept.setCreatedBy(SessionUtil.getLoginName());
		dept.setModifiedBy(SessionUtil.getLoginName());
		dept.setCreatedDate(now);
		dept.setModifiedDate(now);
		dept.setDeptId(UUID.randomUUID().toString());
		dept.setStatus(EnumState.NORMAL.getValue());
		deptRepo.save(dept);
	}

	@Override
	@Transactional
	public void update(DeptDto record) {
		Dept dept = deptRepo.findById(record.getDeptId());
		dept.setAddress(record.getAddress());
		dept.setDeptName(record.getDeptName());
		dept.setDeptType(record.getDeptType());
		dept.setDeptOfficeId(record.getDeptOfficeId());
		dept.setModifiedBy(SessionUtil.getLoginName());
		dept.setModifiedDate(new Date());
		deptRepo.save(dept);
		logger.info(String.format("更新办事处", record.getDeptName()));
	}

	@Override
	public DeptDto findById(String deptId) {
		
		Dept dept = deptRepo.findById(deptId);
		DeptDto deptDto = new DeptDto();
		BeanCopierUtils.copyProperties(dept, deptDto);
		List<OfficeUser> officeList =dept.getOffices();
		if(officeList !=null && officeList.size()>0){
			List<OfficeUserDto> officeDtoList = new ArrayList<OfficeUserDto>(officeList.size());
			officeList.forEach(u ->{
				OfficeUserDto officeDto = new OfficeUserDto();
				BeanCopierUtils.copyProperties(u, officeDto);
				officeDtoList.add(officeDto);
			});
			deptDto.setOffices(officeDtoList);
			
		}
		return deptDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		
		String[] ids = id.split(",");
        if (ids.length > 1) {
        	int l = ids.length;
        	List<Dept> deleteList = new ArrayList<Dept>(l);
        	for(int i=0;i<l;i++){
        		Dept dept = deptRepo.findById(ids[i]);
        		dept.setStatus(EnumState.DELETE.getValue());
        		deleteList.add(dept);
        	}
        	deptRepo.bathUpdate(deleteList);
        } else {
        	Dept dept = deptRepo.findById(id);
        	dept.setStatus(EnumState.DELETE.getValue());
        	deptRepo.save(dept);
        }
		
	}

	@Override
	public PageModelDto<OfficeUserDto> getDeptOfficeUsers(String officeId) {
		PageModelDto pageModelDto = new PageModelDto();
		List<OfficeUserDto> officeDtos = new ArrayList<>();
		Dept  dept =deptRepo.findById(officeId);
		if(dept!=null){
			dept.getOffices().forEach(x->{
				OfficeUserDto officeDto = new OfficeUserDto();
				officeDto.setOfficeID(x.getOfficeID());
				officeDto.setOfficeDesc(x.getOfficeDesc());
				officeDto.setOfficeEmail(x.getOfficePhone());
				officeDto.setOfficeUserName(x.getOfficeUserName());
				officeDtos.add(officeDto);
			});
			pageModelDto.setValue(officeDtos);
			pageModelDto.setCount(officeDtos.size());
			logger.info(String.format("查找办事处人员，办事处%s", dept.getDeptId()));
		}
		return pageModelDto;
	}

	@Override
	@Transactional
	public void addOfficeUserToDept(String deptId, String officeId) {
		Dept  dept =deptRepo.findById(deptId);
		if(dept!=null){
			OfficeUser office = officeUserRepo.findById(officeId);
			if(office !=null){
				office.setDept(dept);
			}
			officeUserRepo.save(office);
			logger.info(String.format("添加用户到办事处,办事处%s,用户:%s", dept.getDeptId(), office.getOfficeUserName()));			
		}
	}

	@Override
	@Transactional
	public void removeOfficeUserDepts(String[] ids, String deptId) {
		Dept dept = deptRepo.findById(deptId);
		if(dept!=null){
			for(String id : ids){
				this.removeOfficeUserDept(id, deptId);
			}
			logger.info(String.format("批量删除办事处用户,办事处%s", dept.getDeptId()));
		}
	}

	@Override
	@Transactional
	public void removeOfficeUserDept(String officeId, String deptId) {
		Dept dept = deptRepo.findById(deptId);
		if(dept!=null){
			OfficeUser office=officeUserRepo.findById(officeId);
			if(office !=null){
				office.setDept(null);
			}
			officeUserRepo.save(office);
			logger.info(String.format("从办事处移除用户,办事处%s,用户:%s", dept.getDeptId(), office.getOfficeUserName()));
		}
	}

	@Override
	public PageModelDto<OfficeUserDto> getOfficeUsersNotInDept(String deptId, ODataObj odataObj) {
		PageModelDto<OfficeUserDto> pageModelDto = new  PageModelDto<>();
		List<OfficeUserDto> officeDtos  = new ArrayList<>();
		Dept dept =deptRepo.findById(deptId);
		List<String> officeIds =new ArrayList<>();
		if(dept !=null){
			List<OfficeUser> offices= officeUserRepo.getOfficeNotIn(officeIds,odataObj);
			offices.forEach(x->{
				OfficeUserDto officeDto = new OfficeUserDto();
				officeDto.setOfficeID(x.getOfficeID());
				officeDto.setOfficeDesc(x.getOfficeDesc());
				officeDto.setOfficeEmail(x.getOfficeEmail());
				officeDto.setOfficePhone(x.getOfficePhone());
				officeDto.setOfficeUserName(x.getOfficeUserName());
				officeDto.setCreatedDate(x.getCreatedDate());
				officeDtos.add(officeDto);
			});
			pageModelDto.setValue(officeDtos);
			pageModelDto.setCount(officeDtos.size());
		}
		 
		return pageModelDto;
	}


}