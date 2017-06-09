package cs.service.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.NumIncreaseUtils;
import cs.common.utils.Validate;
import cs.domain.project.AssistPlan;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.AssistPlan_;
import cs.model.PageModelDto;
import cs.model.project.AssistPlanDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;

/**
 * Description: 协审方案 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Service
public class AssistPlanServiceImpl  implements AssistPlanService {

	@Autowired
	private AssistPlanRepo assistPlanRepo;
    @Autowired
    private AssistPlanSignRepo assistPlanSignRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	public PageModelDto<AssistPlanDto> get(ODataObj odataObj) {
		PageModelDto<AssistPlanDto> pageModelDto = new PageModelDto<AssistPlanDto>();
		List<AssistPlan> resultList = assistPlanRepo.findByOdata(odataObj);
		List<AssistPlanDto> resultDtoList = new ArrayList<AssistPlanDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
			resultList.forEach(x->{
				AssistPlanDto modelDto = new AssistPlanDto();
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
	public void save(AssistPlanDto record) {
        AssistPlan assistPlan = new AssistPlan();
        BeanCopierUtils.copyProperties(record,assistPlan);
        if(!Validate.isString(record.getId())){
            assistPlan.setId(UUID.randomUUID().toString());
            assistPlan.setPlanName(NumIncreaseUtils.getAssistPlanName());
        }
        assistPlanRepo.save(assistPlan);
        /************************  协审类型判断 **************************/
        if(record.isSignle()){//单个协审
            if(record.getSplitNum() > 0){
                List<AssistPlanSign> saveList = new ArrayList<>(record.getSplitNum());
                int i=1;
                while(i <= record.getSplitNum()){
                    AssistPlanSign assistPlanSign = new AssistPlanSign();
                    assistPlanSign.setSignId(record.getSignId());
                    assistPlanSign.setProjectName(record.getProjectName());
                    assistPlanSign.setAssistType(record.getAssistType());
                    assistPlanSign.setSplitNum(i);
                    assistPlanSign.setAssistPlan(assistPlan);
                    assistPlanSign.setIsMain(i==1? Constant.EnumState.YES.getValue():Constant.EnumState.NO.getValue()); //主要为了方便显示
                    saveList.add(assistPlanSign);
                    i++;
                }
                assistPlanSignRepo.bathUpdate(saveList);
            }
        }else{  //合并协审，保存的只有主项目
            AssistPlanSign assistPlanSign = new AssistPlanSign();
            assistPlanSign.setSignId(record.getSignId());
            assistPlanSign.setMainSignId(record.getSignId());
            assistPlanSign.setProjectName(record.getProjectName());
            assistPlanSign.setAssistType(record.getAssistType());
            assistPlanSign.setAssistPlan(assistPlan);
            assistPlanSign.setIsMain(Constant.EnumState.YES.getValue());
            assistPlanSignRepo.save(assistPlanSign);
        }

	}

	@Override
	@Transactional
	public void update(AssistPlanDto record) {
		AssistPlan domain = assistPlanRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		assistPlanRepo.save(domain);
	}

	@Override
	public AssistPlanDto findById(String id) {		
		AssistPlanDto modelDto = new AssistPlanDto();
		if(Validate.isString(id)){
			AssistPlan domain = assistPlanRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
	    //1、先删除协审项目信息
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from " +AssistPlanSign.class.getSimpleName());
        String[] idArr = id.split(",");
        if (idArr.length > 1) {
            hqlBuilder.append( " where "+ AssistPlanSign_.assistPlan.getName()+"."+AssistPlan_.id.getName()+" in ( ");
            int totalL = idArr.length;
            for(int i=0;i<totalL;i++){
                if(i==totalL-1){
                    hqlBuilder.append(" :id"+i).setParam("id"+i, idArr[i]);
                }else{
                    hqlBuilder.append(" :id"+i+",").setParam("id"+i, idArr[i]);
                }
            }
            hqlBuilder.append(" )");
        } else {
            hqlBuilder.append( " where "+ AssistPlanSign_.assistPlan.getName()+"."+AssistPlan_.id.getName()+" = :id ");
            hqlBuilder.setParam("id", id);
        }
        assistPlanSignRepo.executeHql(hqlBuilder);
        //2、再删协审计划信息
		assistPlanRepo.deleteById(AssistPlan_.id.getName(),id);
	}
	
}