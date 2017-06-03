package cs.service.sys;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.domain.sys.Dict;
import cs.domain.sys.Dict_;
import cs.model.PageModelDto;
import cs.model.sys.DictDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.DictRepo;


@Service
public class DictServiceImpl implements DictService {
	private static Logger logger = Logger.getLogger(DictServiceImpl.class);

	@Autowired
	private DictRepo dictRepo;

	@Autowired
	private ICurrentUser currentUser;
	
	private ICache cache = CacheManager.getCache();
	
	@Override
	public PageModelDto<DictDto> get(ODataObj odataObj) {
		
		PageModelDto<DictDto> pageModelDto = new PageModelDto<DictDto>();
		List<Dict> dicts = dictRepo.findByOdata(odataObj);
		List<DictDto> dictDtos = new ArrayList<DictDto>();
		
		if(dicts != null){
			for(Dict dict:dicts){
				DictDto dictDto = new DictDto();
				dictDto.setDictId(dict.getDictId());
				//dictDto.set
				BeanUtils.copyProperties(dict, dictDto);				
				dictDtos.add(dictDto);
				
			}
						
		}
		
		pageModelDto.setCount(dictDtos.size());
		pageModelDto.setValue(dictDtos);
		
		return pageModelDto;
	} 
	@Override
	@Transactional
	public void createDict(DictDto dictDto) {
		
		checkDictExists(dictDto,null);
		
		/*
		if(dictDto.getDictType().equals("0")){
			//如果是增加字典编码，检查编码是否有重复
			Dict dictExists = dictRepo.findByCode(dictDto.getDictCode());
			if(dictExists != null){
				throw new IllegalArgumentException(String.format("增加字典类型，字典编码：%s 已经存在,请重新输入！", dictDto.getDictCode()));
			}
		}else if(dictDto.getDictType().equals("1")){
			//如果是增加字典数据项，检查类型编码、字典值、字典名称是否重复
			Dict dictExists = dictRepo.findByCodeKeyAndName(dictDto.getDictCode(),dictDto.getDictKey(),dictDto.getDictName());
			if(dictExists != null){
				throw new IllegalArgumentException(String.format("增加字典数据项[%s,%s,%s] 已经存在,请重新输入！", dictDto.getDictCode(),dictDto.getDictKey(),dictDto.getDictName()));
			}
		}*/
		Dict dict = new Dict();
		
		BeanUtils.copyProperties(dictDto, dict);
		
		dict.setDictId(UUID.randomUUID().toString());
		dict.setCreatedBy(currentUser.getLoginName());
		dict.setModifiedBy(currentUser.getLoginName());
		dict.setIsUsed("0");

		Date now = new Date();
		dict.setCreatedDate(now);
		dict.setModifiedDate(now);
		dictRepo.save(dict);
		clearCache(dict.getDictCode());
		
	}
	
	
	//检查数据字典是否已存在
	private void checkDictExists(DictDto dictDto, String checkDictId) {
		//检查DICT CODE是否重复
		Dict dictExists = dictRepo.findByCode(dictDto.getDictCode(),checkDictId);
		if(dictExists != null){
			throw new IllegalArgumentException(String.format("增加字典，字典编码：%s 已经存在,请重新输入！", dictDto.getDictCode()));
		}
		//dictRepo.findByCodeKeyAndName(dictDto.getDictCode(),dictDto.getDictKey(),dictDto.getDictName());
		//如果DICT CODE不重复，检查和同级字典值和字典名称是否有重复，有重复不创建
		Dict hasDictAtSomeLevel = dictRepo.findByKeyAndNameAtSomeLevel(dictDto.getDictKey(),dictDto.getDictName(), dictDto.getParentId(),checkDictId);
		if(hasDictAtSomeLevel != null){
			throw new IllegalArgumentException(String.format("增加字典[%s-%s]在同级已经存在,请重新输入！", dictDto.getDictName(),dictDto.getDictKey()));
		}
	}
	@Override
	@Transactional
	public void updateDict(DictDto dictDto) {
		
		checkDictExists(dictDto,dictDto.getDictId());
		
		/*Dict dictExists = dictRepo.findByCode(dictDto.getDictCode(),dictDto.getDictId());
		if(dictExists != null){
			throw new IllegalArgumentException(String.format("更新字典，字典编码：%s 已经存在,请重新输入！", dictDto.getDictCode()));
		}
		dictRepo.findByCodeKeyAndName(dictDto.getDictCode(),dictDto.getDictKey(),dictDto.getDictName());
		//如果DICT CODE不重复，检查和同级字典值和字典名称是否有重复，有重复不创建
		Dict hasDictAtSomeLevel = dictRepo.findByKeyAndNameAtSomeLevel(dictDto.getDictKey(),dictDto.getDictName(), dictDto.getParentId(),dictDto.getDictId());
		if(hasDictAtSomeLevel != null){
			throw new IllegalArgumentException(String.format("更新字典[%s-%s]在同级已经存在,请重新输入！", dictDto.getDictName(),dictDto.getDictKey()));
		}*/
		/*if(dictDto.getDictType().equals("1")){
			//如果是增加字典数据项，检查类型编码、字典值、字典名称是否重复
			Dict dictExists = dictRepo.findByCodeKeyAndName(dictDto.getDictCode(),dictDto.getDictKey(),dictDto.getDictName());
			if(dictExists != null&&!dictExists.getDictId().equals(dictDto.getDictId())){
				throw new IllegalArgumentException(String.format("更新字典数据项[%s,%s,%s] 已经存在,请重新输入！", dictDto.getDictCode(),dictDto.getDictKey(),dictDto.getDictName()));
			}
		}*/
		Dict dict = dictRepo.findById(dictDto.getDictId());
		
		BeanUtils.copyProperties(dictDto, dict);
		dict.setModifiedBy(currentUser.getLoginName());
		dict.setModifiedDate(new Date());
		dictRepo.save(dict);

		clearCache(dict.getDictCode());
	}
	private void clearCache(String dictCode) {
		//update cache
		cache.clear("DICT_ITEMS".concat(dictCode));		
		cache.clear("DICT_ALL_ITEMS");
		cache.clear("DICT_NAME_DATA".concat(dictCode));
		
	}
	@Override
	public void setDictState(String dictCode, String state) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<DictDto> findDictByCode(String dictCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DictDto findDictByCodeAndKey(String dictCode,String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object getFromCache(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@Transactional
	public void deleteDict(String dictId) {
		Dict dict = dictRepo.findById(dictId);
		if(dict != null){
			dictRepo.delete(dict);
			clearCache(dict.getDictCode());
		}
		
	}
	@Override
	@Transactional
	public void deleteDicts(String[] dictCodes) {
		if(dictCodes.length > 0){
			for(String dictCode:dictCodes){
				deleteDict(dictCode);
			}
		}
		
	}
	
	
	/**
	 * 取得指定编码类型的字典数据，不包含编码类型
	 * @param dictCode 字典编码
	 * */
	@Override
	public List<DictDto> getDictItemByCode(String dictCode) {
		//先从缓存取，如果取不到就查询数据库，把查到的数据放到缓存
		//缓存键格式DICT_ITEMS+dictCode
		//如果dictCode是null，则返回全部数据
		
		List<DictDto> dictDtos = new ArrayList<>();
		
		if(dictCode != null&&!dictCode.isEmpty()){
//			dictDtos = (List<DictDto>)cache.get("DICT_ITEMS".concat(dictCode));
			
			if(dictDtos.size() == 0){
				Dict pDict = dictRepo.findByCode(dictCode, null);
				if(pDict != null){
					//List<Dict> dicts = dictRepo.findDictItemByCode(dictCode);
					List<Dict> dicts = dictRepo.findByPdictId(pDict.getParentId());
					dictDtos = new ArrayList<DictDto>();
					if(dicts != null){
						for(Dict dict:dicts){
							DictDto dictDto = new DictDto();
							
							BeanUtils.copyProperties(dict, dictDto);
							
							dictDtos.add(dictDto);
						}
						
						if(dictDtos.size()>0){
							//放到缓存
							cache.put("DICT_ITEMS".concat(dictCode), dictDtos);
						}
						
					}
				}
				
				
			}
		}else{
//			dictDtos = (List<DictDto>)cache.get("DICT_ALL_ITEMS");
			if(dictDtos.size() == 0){
				Criteria criteria = dictRepo.getExecutableCriteria();
				criteria.addOrder(Order.asc(Dict_.dictSort.getName()));
				List<Dict> dicts = criteria.list();
				if(dicts != null){
					dictDtos = new ArrayList<DictDto>();
					//begin dict foreach
					for(Dict dict:dicts){
						DictDto dictDto = new DictDto();
						BeanUtils.copyProperties(dict, dictDto);
						dictDtos.add(dictDto);
					}
					//end dict foreach
				}
				
				if(dictDtos != null){
					cache.put("DICT_ALL_ITEMS", dictDtos);
				}
			}
		}
		
		return dictDtos;
	}
	
	
	/**
	 * 通过编码类型取得字典名称，用于翻译出字典值，只返回字典数据
	 * @param dictCode 字典编码
	 * @param dictKey 字典值
	 * @return Map类型，key的格式是dictCode-dictKey,value是dictName
	 * */
	@Override
	public Map<String, String> getDictNameByCode(String dictCode) {
		//先从缓存取，如果取不到就查询数据库，把查到的数据放到缓存
		//缓存键格式DICT_NAME_DATA+dictCode
		Map<String, String> dictNameDataMap = (Map<String, String>)cache.get("DICT_NAME_DATA".concat(dictCode));
		
		if(dictNameDataMap == null){
			//TODO:查询数据库
			List<DictDto> dictItems = getDictItemByCode(dictCode);
			
			if(dictItems != null){
				
				//dictNameDataMap = new ArrayList<Map<String, String>>();
				dictNameDataMap = new HashMap<String, String>();
				for(DictDto dictDto : dictItems){
					
					String key = dictDto.getDictCode().concat("_").concat(dictDto.getDictKey());
					dictNameDataMap.put(key, dictDto.getDictName());
				}
				
				//放到缓存
				cache.put("DICT_NAME_DATA".concat(dictCode), dictNameDataMap);
			}
			
			
		}
		
		return dictNameDataMap;
	}
	
	

}
