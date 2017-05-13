package cs.service.external;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant.EnumState;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.external.Dept;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.external.DeptRepo;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptRepo deptRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	public PageModelDto<DeptDto> get(ODataObj odataObj) {
		PageModelDto<DeptDto> pageModelDto = new PageModelDto<DeptDto>();
		List<Dept> deptList = deptRepo.findByOdata(odataObj);
		List<DeptDto> deptDtoList = new ArrayList<DeptDto>(deptList.size());
		
		if(deptList != null && deptList.size() > 0){
			deptList.forEach(x->{
				DeptDto deptDto = new DeptDto();
				BeanCopierUtils.copyProperties(x, deptDto);
				//cannot copy 
				deptDto.setCreatedDate(x.getCreatedDate());
				deptDto.setModifiedDate(x.getModifiedDate());
				
				deptDtoList.add(deptDto);
			});						
		}		
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
		dept.setCreatedBy(currentUser.getLoginName());
		dept.setModifiedBy(currentUser.getLoginName());
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
		BeanCopierUtils.copyPropertiesIgnoreNull(record, dept);
		dept.setModifiedBy(currentUser.getLoginName());
		dept.setModifiedDate(new Date());
		
		deptRepo.save(dept);
	}

	@Override
	public DeptDto findById(String deptId) {		
		DeptDto deptDto = new DeptDto();
		if(Validate.isString(deptId)){
			Dept dept = deptRepo.findById(deptId);
			BeanCopierUtils.copyProperties(dept, deptDto);
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
}