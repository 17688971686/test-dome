package cs.service.expert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelCondition_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.project.WorkProgramDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;

/**
 * Description: 专家抽取条件 业务操作实现类
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
@Service
public class ExpertSelConditionServiceImpl  implements ExpertSelConditionService {
    private static Logger log = Logger.getLogger(ExpertSelConditionServiceImpl.class);

    @Autowired
	private ExpertSelConditionRepo expertSelConditionRepo;
    @Autowired
    private ExpertReviewService expertReviewService;
    @Autowired
    private WorkProgramRepo workProgramRepo;
	
	@Override
	public PageModelDto<ExpertSelConditionDto> get(ODataObj odataObj) {
		PageModelDto<ExpertSelConditionDto> pageModelDto = new PageModelDto<ExpertSelConditionDto>();
		List<ExpertSelCondition> resultList = expertSelConditionRepo.findByOdata(odataObj);
		List<ExpertSelConditionDto> resultDtoList = new ArrayList<ExpertSelConditionDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				ExpertSelConditionDto modelDto = new ExpertSelConditionDto();
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
	public void save(ExpertSelConditionDto record) {
		ExpertSelCondition domain = new ExpertSelCondition(); 
		BeanCopierUtils.copyProperties(record, domain);
		expertSelConditionRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(ExpertSelConditionDto record) {
		ExpertSelCondition domain = expertSelConditionRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		expertSelConditionRepo.save(domain);
	}

	@Override
	public ExpertSelConditionDto findById(String id) {		
		ExpertSelConditionDto modelDto = new ExpertSelConditionDto();
		if(Validate.isString(id)){
			ExpertSelCondition domain = expertSelConditionRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id, String workProId, boolean deleteEP) {
        expertSelConditionRepo.deleteById(ExpertSelCondition_.id.getName(),id);
        //判断删除掉根据此条件抽取的专家
        List<String> idList = StringUtil.getSplit(id, ",");
        if(deleteEP && (idList != null && idList.size() > 0)){
            idList.forEach(idStr -> {
                expertReviewService.deleteExpert(workProId,null,null,idStr);
            });
        }
	}

	@Override
	@Transactional
	public List<ExpertSelConditionDto> saveConditionList(ExpertSelConditionDto[] recordList) throws Exception{
		List<ExpertSelConditionDto> resultList = new ArrayList<>();
	    if(recordList != null && recordList.length > 0){
            WorkProgram wp = workProgramRepo.findById(recordList[0].getWorkProgramId());
	        if(wp == null || !Validate.isString(wp.getId())){
                log.info("保存专家收取条件失败：无法获取评审方案信息" );
	            throw  new Exception(Constant.ERROR_MSG);
            }
            for(int i=0,l=recordList.length;i<l;i++){
				ExpertSelCondition domain = new ExpertSelCondition();
				ExpertSelConditionDto rl = recordList[i];
				if(!Validate.isString(rl.getId())){
					rl.setId(UUID.randomUUID().toString());
					domain.setId(rl.getId());
				}
				BeanCopierUtils.copyProperties(rl, domain);

                domain.setWorkProgram(wp);
				expertSelConditionRepo.save(domain);
				resultList.add(rl);
			}
        }
        return resultList;
	}

    @Override
    public Map<String,Object> findByWorkProId(String workProId) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<ExpertSelConditionDto> resultList = new ArrayList<>();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+ExpertSelCondition.class.getSimpleName()+" where "+ExpertSelCondition_.workProgram.getName()+"."+ WorkProgram_.id.getName()  +" =:workProId");
        hqlBuilder.setParam("workProId",workProId);
        hqlBuilder.append(" order by " + ExpertSelCondition_.sort.getName());
        List<ExpertSelCondition> queryList = expertSelConditionRepo.findByHql(hqlBuilder);

        if(queryList != null && queryList.size() > 0){
            queryList.forEach(ql -> {
                ExpertSelConditionDto eDto = new ExpertSelConditionDto();
                BeanCopierUtils.copyProperties(ql, eDto);
                resultList.add(eDto);
            });
        }
        resultMap.put("conditionList",resultList);
        //添加工作方案信息
        WorkProgram workProgram = workProgramRepo.findById(workProId);
        WorkProgramDto wpDto = new WorkProgramDto();
        BeanCopierUtils.copyProperties(workProgram, wpDto);
        resultMap.put("workProgram",wpDto);

        return resultMap;
    }

}