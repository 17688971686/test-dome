package cs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.domain.Company;
import cs.model.CompanyDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.CompanyRepo;
@Service
public class CompanyServiceImpl implements CompanyService{

	private static Logger logger = Logger.getLogger(CompanyServiceImpl.class);
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
			
			comDto.setId(item.getId());
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
	@Transactional
	public void createCompany(CompanyDto companyDto) {
		//判断单位名称是否添加
		Criteria criteria =	companyRepo.getSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("coName", companyDto.getCoName()));
		List<Company> com = criteria.list();
		if(com.isEmpty()){
			
			Company c =new Company();
			c.setId(UUID.randomUUID().toString());
			c.setCoAddress(companyDto.getCoAddress());
			c.setCoDept(companyDto.getCoDept());
			c.setCoDeptName(companyDto.getCoDeptName());
			c.setCoFax(companyDto.getCoFax());
			c.setCoName(companyDto.getCoName());
			c.setCoPC(companyDto.getCoPC());
			c.setCoPhone(companyDto.getCoPhone());
			c.setCoSite(companyDto.getCoSite());
			c.setCoSynopsis(companyDto.getCoSynopsis());
			c.setCreatedBy(currentUser.getLoginName());
			c.setModifiedBy(currentUser.getLoginName());
			
			companyRepo.save(c);
			logger.info(String.format("创建单位，单位名:%s", companyDto.getCoName()));
		}else{
			
			throw new IllegalArgumentException(String.format("该单位已存在：%s 已经存在，请重新输入", companyDto.getCoName()));

		}
		
	}
	@Override
	@Transactional
	public void deleteCompany(String id) {
		
		Company com  = companyRepo.findById(id);
		if(com !=null){
			
			companyRepo.delete(com);
		//	this.deleteCompany(id);
			logger.info(String.format("删除单位，单位名coName:%s", com.getCoName()));
		}
		
	}
	@Override
	@Transactional
	public void deleteCompanys(String[] ids) {

		for( String id : ids){
			
			this.deleteCompany(id);
			//companyRepo.delete(entity);
		}
		logger.info("批量删除单位");
		
	}
	@Override
	@Transactional
	public void updateCompany(CompanyDto companyDto) {
		
		Company c =companyRepo.findById(companyDto.getId());
	
		c.setCoAddress(companyDto.getCoAddress());
		c.setCoDept(companyDto.getCoDept());
		c.setCoDeptName(companyDto.getCoDeptName());
		c.setCoFax(companyDto.getCoFax());
		c.setCoName(companyDto.getCoName());
		c.setCoPC(companyDto.getCoPC());
		c.setCoPhone(companyDto.getCoPhone());
		c.setCoSite(companyDto.getCoSite());
		c.setCoSynopsis(companyDto.getCoSynopsis());
		c.setCreatedBy(currentUser.getLoginName());
		c.setModifiedBy(currentUser.getLoginName());
		
		companyRepo.save(c);
		logger.info(String.format("编辑单位，单位名:%s", companyDto.getCoName()));
	}

}
