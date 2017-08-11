package cs.service.project;

import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.AddRegisterFile_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.model.project.AddSuppLetterDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AddRegisterFileRepo;
import cs.repository.repositoryImpl.project.SignRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 登记补充资料 业务操作实现类
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
@Service
public class AddRegisterFileServiceImpl  implements AddRegisterFileService {

	@Autowired
	private AddRegisterFileRepo addRegisterFileRepo;
	@Autowired
	private SignRepo signRepo;
	@Override
	public PageModelDto<AddRegisterFileDto> get(ODataObj odataObj) {
		PageModelDto<AddRegisterFileDto> pageModelDto = new PageModelDto<AddRegisterFileDto>();
		List<AddRegisterFile> resultList = addRegisterFileRepo.findByOdata(odataObj);
		List<AddRegisterFileDto> listDto=new ArrayList<>();
		 if(resultList.size() > 0){
			for (AddRegisterFile addRegisterFile : resultList) {
				AddRegisterFileDto addRegisterFileDto = new AddRegisterFileDto();
				BeanCopierUtils.copyProperties(addRegisterFile, addRegisterFileDto);
				listDto.add(addRegisterFileDto);
			}
		 }
		List<AddRegisterFileDto> resultDtoList = new ArrayList<AddRegisterFileDto>(resultList.size());
		pageModelDto.setValue(listDto);
		pageModelDto.setCount(odataObj.getCount());
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(String signid,AddRegisterFileDto[] addRegisterFileDtos) {
		if(addRegisterFileDtos !=null && addRegisterFileDtos.length>0){
			for (AddRegisterFileDto addRegisterFileDto : addRegisterFileDtos) {
				AddRegisterFile addRegisterFile = new AddRegisterFile(); 
				BeanCopierUtils.copyProperties(addRegisterFileDto, addRegisterFile); 
				Date now = new Date();
				addRegisterFile.setId(UUID.randomUUID().toString());
				addRegisterFile.setCreatedDate(now);
				addRegisterFile.setModifiedDate(now);
				addRegisterFile.setCreatedBy(SessionUtil.getLoginName());
				addRegisterFile.setModifiedBy(SessionUtil.getLoginName());
				addRegisterFile.setSignid(signid);
				addRegisterFileRepo.save(addRegisterFile);
			}
		}
	}

	@Override
	@Transactional
	public void update(AddRegisterFileDto[] addRegisterFileDtos) {
		if(addRegisterFileDtos !=null && addRegisterFileDtos.length > 0){
			for (AddRegisterFileDto addRegisterFileDto : addRegisterFileDtos) {
				AddRegisterFile addRegisterFile = addRegisterFileRepo.findById(addRegisterFileDto.getId());
				if(addRegisterFile !=null){
					BeanCopierUtils.copyProperties(addRegisterFileDto, addRegisterFile);
					Date now = new Date();
					addRegisterFile.setCreatedDate(now);
					addRegisterFile.setModifiedDate(now);
					addRegisterFile.setCreatedBy(SessionUtil.getLoginName());
					addRegisterFile.setModifiedBy(SessionUtil.getLoginName());
					addRegisterFileRepo.save(addRegisterFile);
				}
			}
		}
		
	}

	@Override
	@Transactional
	public AddRegisterFileDto findById(String id) {		
		AddRegisterFileDto modelDto = new AddRegisterFileDto();
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(AddRegisterFileDto[] addRegisterFileDtos) {
		if(addRegisterFileDtos !=null && addRegisterFileDtos.length > 0){
			for (AddRegisterFileDto addRegisterFileDto : addRegisterFileDtos) {
					AddRegisterFile addRegisterFile = addRegisterFileRepo.findById(addRegisterFileDto.getId());
					if(addRegisterFile !=null){
						addRegisterFileRepo.delete(addRegisterFile);
					}
			}
		}
	}
	
	@Override
	public List<AddRegisterFileDto> findbySuppdate(String suppDate) {
		List<AddRegisterFileDto> dtoList = new ArrayList<>();
        HqlBuilder hqlBuilder1 = HqlBuilder.create();
        hqlBuilder1.append("from " + AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.suppleDate.getName() + " =:suppleDate");
        hqlBuilder1.setParam("suppleDate", suppDate);
        List<AddRegisterFile> addRegisterFilelist = addRegisterFileRepo.findByHql(hqlBuilder1);
        if(!addRegisterFilelist.isEmpty()){
        	for (AddRegisterFile addRegisterFile : addRegisterFilelist) {
        		AddRegisterFileDto addRegisterFileDto = new AddRegisterFileDto();
        		BeanCopierUtils.copyProperties(addRegisterFile, addRegisterFileDto);
        		dtoList.add(addRegisterFileDto);
			}
        }
		return dtoList;
	}
	
	@Override
	public Map<String,Object> initprint( String signid){
		Date now =  new Date();
		Map<String,Object>  map=new HashMap<>();
		//根据signid获取所有数据登记补充资料
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.signid.getName() + " =:signid");
        hqlBuilder.setParam("signid", signid);
        List<AddRegisterFile> addRegisterFileList=addRegisterFileRepo.findByHql(hqlBuilder);
        List<AddRegisterFileDto> AddRegisterFileDtoList = new ArrayList<>();
        List<Date> suppDate = new ArrayList<>();
        for (AddRegisterFile addRegisterFile : addRegisterFileList) {
        	AddRegisterFileDto addRegisterFileDto = new AddRegisterFileDto();
        	BeanCopierUtils.copyProperties(addRegisterFile, addRegisterFileDto);
        	AddRegisterFileDtoList.add(addRegisterFileDto);
        	suppDate.add(addRegisterFileDto.getSuppleDate());
		}
        map.put("AddRegisterFileDtoList", AddRegisterFileDtoList);
        map.put("printDate", now);
        
        //根据signid获取收文相关内容
        Sign sign = signRepo.findById(signid);
        	SignDto signdto=new SignDto();
        	signdto.setProjectname(sign.getProjectname());
        	signdto.setFilecode(sign.getFilecode());
        	signdto.setDesigncompanyName(sign.getDesigncompanyName());
        	signdto.setProjectcode(sign.getProjectcode());
        	signdto.setSendusersign(sign.getSendusersign());
        	map.put("signdto", signdto);
		return map;
		
	}
	
}