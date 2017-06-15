package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.cache.CacheFactory;
import cs.common.cache.DefaultCacheFactory;
import cs.common.cache.ICache;
import cs.domain.project.AssistPlan;
import cs.domain.sys.SysConfig;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.service.sys.SysConfigService;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnitUser;
import cs.domain.project.AssistUnit_;
import cs.model.PageModelDto;
import cs.model.project.AssistUnitDto;
import cs.model.project.AssistUnitUserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AssistUnitRepo;
import cs.repository.repositoryImpl.project.AssistUnitUserRepo;

/**
 * Description: 协审单位 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
@Service
public class AssistUnitServiceImpl  implements AssistUnitService {

	private static Logger logger=Logger.getLogger(AssistUnitServiceImpl.class);
	
	
	@Autowired
	private AssistUnitRepo assistUnitRepo;
	
	@Autowired
	private AssistUnitUserRepo assistUnitUserRepo;
	
	@Autowired
	private ICurrentUser currentUser;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private AssistPlanRepo assistPlanRepo;

	@Override
	public PageModelDto<AssistUnitDto> get(ODataObj odataObj) {
		PageModelDto<AssistUnitDto> pageModelDto = new PageModelDto<AssistUnitDto>();
		List<AssistUnit> resultList = assistUnitRepo.findByOdata(odataObj);
		List<AssistUnitDto> resultDtoList = new ArrayList<AssistUnitDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
			resultList.forEach(x->{
				AssistUnitDto modelDto = new AssistUnitDto();
				BeanCopierUtils.copyProperties(x, modelDto);

				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(AssistUnitDto record) {
		boolean isUnitExist=assistUnitRepo.isUnitExist(record.getUnitName());
		if(!isUnitExist){//单位不存在
			AssistUnit domain = new AssistUnit(); 
			BeanCopierUtils.copyProperties(record, domain); 
			domain.setId(UUID.randomUUID().toString());
			int unitSort=assistUnitRepo.getUnitSortMax();
			domain.setUnitSort(++unitSort);
			Date now = new Date();
			domain.setCreatedBy(currentUser.getLoginName());
			domain.setModifiedBy(currentUser.getLoginName());
			domain.setCreatedDate(now);
			domain.setModifiedDate(now);
			assistUnitRepo.save(domain);
		}else{
			throw new IllegalArgumentException(String.format("单位名：%s 已经存在,请重新输入！",	record.getUnitName()));
		}
	}

	@Override
	@Transactional
	public void update(AssistUnitDto record) {
		AssistUnit domain = assistUnitRepo.findById(record.getId());
		
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		assistUnitRepo.save(domain);
	}

	@Override
	public AssistUnitDto findById(String id) {		
		AssistUnitDto modelDto = new AssistUnitDto();
		if(Validate.isString(id)){
			AssistUnit domain = assistUnitRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		assistUnitRepo.deleteById(AssistUnit_.id.getName(),id);
	}

	@Override
	@Transactional
	public PageModelDto<AssistUnitUserDto> getUnitAndUser(String id) {
		
		PageModelDto<AssistUnitUserDto> pageModelDto=new PageModelDto<>();
		List<AssistUnitUserDto> assistUnitUserDtoList=new ArrayList<>();
		AssistUnit assistUnit=assistUnitRepo.findById(id);
		if(assistUnit!=null){
			assistUnit.getAssistUnitUserList().forEach(x -> {
				AssistUnitUserDto assistUnitUserDto=new AssistUnitUserDto();
				BeanCopierUtils.copyProperties(x, assistUnitUserDto);
				assistUnitUserDtoList.add(assistUnitUserDto);
				
			});
			
			pageModelDto.setCount(assistUnitUserDtoList.size());
			pageModelDto.setValue(assistUnitUserDtoList);
		}
		
		return pageModelDto;
	}

	@Override
	@Transactional
	public PageModelDto<AssistUnitUserDto> getUserNotIn(ODataObj odataObj, String id) {
		PageModelDto<AssistUnitUserDto> pageModelDto=new PageModelDto<>();
		List<AssistUnitUserDto> assistUnitUserDtoList=new ArrayList<>();
		AssistUnit assistUnit=assistUnitRepo.findById(id);
		List<String> assistUnitUserIdList=new ArrayList<>();
		if(assistUnit!=null){
			List<AssistUnitUser> assistUnitUserList=assistUnitUserRepo.getUserNotIn(odataObj, assistUnitUserIdList);
			assistUnitUserList.forEach(x ->{
				AssistUnitUserDto assistUnitUserDto=new AssistUnitUserDto();
				BeanCopierUtils.copyProperties(x, assistUnitUserDto);
				assistUnitUserDtoList.add(assistUnitUserDto);
			});
			
			pageModelDto.setCount(assistUnitUserDtoList.size());
			pageModelDto.setValue(assistUnitUserDtoList);
		}
		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void addUser(String unitId, String userId) {
		AssistUnit assistUnit=assistUnitRepo.findById(unitId);
		if(assistUnit!=null){
			AssistUnitUser assistUnitUser=assistUnitUserRepo.findById(userId);
			if(assistUnitUser!=null){
				assistUnitUser.setAssistUnit(assistUnit);
			}
			assistUnitUserRepo.save(assistUnitUser);
		}
	}

	@Override
	@Transactional
	public void removeUser(String unitId, String userId) {
		AssistUnit assistUnit=assistUnitRepo.findById(unitId);
		if(assistUnit!=null){
			AssistUnitUser assistUnitUser=assistUnitUserRepo.findById(userId);
			if(assistUnitUser!=null){
				assistUnitUser.setAssistUnit(null);
			}
			assistUnitUserRepo.save(assistUnitUser);
		}
		
	}

	@Override
	@Transactional
	public void removeUsers(String unitId, String[] userIds) {
		AssistUnit assistUnit=assistUnitRepo.findById(unitId);
		if(assistUnit!=null){
			for(String userId:userIds){
				this.removeUser(unitId, userId);
			}
		}
		
	}

	/**
	 * 查询抽签单位
     * @param planId
	 * @param number
	 * @return
	 */
	@Override
	public List<AssistUnitDto> findDrawUnit(String planId,Integer number) {
	    List<AssistUnitDto> resultList = new ArrayList<>();
        List<AssistUnit> saveList =  new ArrayList<>();
		SysConfigDto sysConfig = null;
        Integer maxSort = 0;
		//1、先查询轮空的单位
        AssistUnitDto assistUnitDto = new AssistUnitDto();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+AssistUnit.class.getSimpleName()+" where "+AssistUnit_.isLastUnSelected.getName()+" =:isLastUnSelected");
        hqlBuilder.setParam("isLastUnSelected", Constant.EnumState.YES.getValue());
        List<AssistUnit> list = assistUnitRepo.findByHql(hqlBuilder);
        if(list == null || list.size() == 0){
        }else{
            BeanCopierUtils.copyProperties(list.get(0),assistUnitDto);
            saveList.add(list.get(0));
            resultList.add(assistUnitDto);
            number = number - 1;
        }

        /**
         * 如果已满足条件，则不用查询
         */
        if(number > 0){
            //2、取出前一次抽签的最大序号
			sysConfig = sysConfigService.findByKey(Constant.EnumConfigKey.LAST_UNIT_MAXSORT.getValue());
			if(sysConfig == null || !Validate.isString(sysConfig.getId())){
                sysConfig = new SysConfigDto();
                sysConfig.setConfigName(Constant.DRAW_ASSIST_UNITNAME);
                sysConfig.setConfigKey(Constant.EnumConfigKey.LAST_UNIT_MAXSORT.getValue());
                sysConfig.setConfigValue(String.valueOf(number));
                sysConfig.setIsShow(Constant.EnumState.NO.getValue());
            }else{
                maxSort = Integer.valueOf(sysConfig.getConfigValue());
            }

            //3、按照序号轮流抽签
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" SELECT nUnit.* FROM ( ");
            sqlBuilder.append(" SELECT cu.*,CASE WHEN  unitsort < :maxSort then (10000+unitsort) else unitsort end newSort FROM CS_AS_UNIT cu");
            sqlBuilder.setParam("maxSort",(maxSort+1));
            if(Validate.isString(assistUnitDto.getId())){
                sqlBuilder.append(" WHERE  WHERE id != :id").setParam("id",assistUnitDto.getId()).append(" ORDER BY newSort) nUnit ");
            }
            sqlBuilder.append(" ) nUnit WHERE ROWNUM < :number ").setParam("number",number+1);
            list = assistUnitRepo.findBySql(sqlBuilder);
            saveList.addAll(list);
            if(list != null && list.size() > 0){
                list.forEach(l ->{
                    AssistUnitDto unitDto = new AssistUnitDto();
                    BeanCopierUtils.copyProperties(l,unitDto);
                    resultList.add(unitDto);
                });
            }
        }

        //4、添加关联和更新最大值缓存
        if(saveList != null && saveList.size() > 0){
            AssistPlan assistPlan = assistPlanRepo.findById(planId);
            if(assistPlan != null){
                assistPlan.setAssistUnitList(saveList);
            }
            assistPlanRepo.save(assistPlan);

            sysConfig.setConfigValue(String.valueOf(saveList.get(saveList.size()-1).getUnitSort()));
            sysConfigService.save(sysConfig);
        }

		return resultList;
	}

	@Override
	@Transactional
	public List<AssistUnitDto> getAssistUnitByPlanId(String planId) {
		List<AssistUnit> assistUnitList=assistUnitRepo.getAssistUnitByPlanId(planId);
		
		List<AssistUnitDto> assistUnitDtoList=new ArrayList<>();
		
		for(AssistUnit assistUnit:assistUnitList){
			
			AssistUnitDto assistUnitDto =new AssistUnitDto();
			BeanCopierUtils.copyProperties(assistUnit, assistUnitDto);
			assistUnitDtoList.add(assistUnitDto);
		}
		
		return assistUnitDtoList;
	}

}