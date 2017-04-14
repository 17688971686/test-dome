package cs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.ICurrentUser;
import cs.domain.Company;
import cs.model.CompanyDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.CompanyRepo;
@Service
public class CompanyServiceImpl implements CompanyService{

	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private CompanyRepo companyRepo;
	@Override
	public PageModelDto<CompanyDto> get(ODataObj odataObj) {
		List<Company> comList =companyRepo.findByOdata(odataObj);
		List<CompanyDto> comDtoList = new ArrayList<>();
		
		for(Company item : comList){
			
			CompanyDto comDto =new CompanyDto();
			
			comDto.setId(UUID.randomUUID().toString());
			comDto.setCoAddress(item.getCoAddress());
			comDto.setCoDept(item.getCoDept());
			comDto.setCoDeptName(item.getCoDeptName());
			comDto.setCoFax(item.getCoFax());
			comDto.setCoName(item.getCoName());
			comDto.setCoPC(item.getCoPC());
			comDto.setCoPhone(item.getCoPhone());
			comDto.setCoSite(item.getCoSite());
			comDto.setCoSynopsis(item.getCoSynopsis());
	
			comDtoList.add(comDto);
			
		}
		PageModelDto<CompanyDto> pageModelDto =new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(comDtoList);
		return pageModelDto;
	}

	@Override
	public void createMeeting(CompanyDto companyDto) {
		
	}

	@Override
	public void deleteMeeting(String id) {
		
	}

	@Override
	public void deleteMeeting(String[] ids) {
		
	}

	@Override
	public void updateMeeting(CompanyDto companyDto) {
		
	}

}
