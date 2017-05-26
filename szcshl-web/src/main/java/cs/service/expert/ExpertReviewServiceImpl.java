package cs.service.expert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.domain.expert.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.Constant.EnumExpertSelectType;
import cs.common.Constant.EnumState;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;

/**
 * Description: 专家评审 业务操作实现类
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
@Service
public class ExpertReviewServiceImpl  implements ExpertReviewService {
	private static Logger log = Logger.getLogger(ExpertReviewServiceImpl.class);
	@Autowired
	private ExpertReviewRepo expertReviewRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private ExpertRepo expertRepo;
	@Autowired
	private WorkProgramRepo workProgramRepo;
	
	@Override
	public PageModelDto<ExpertReviewDto> get(ODataObj odataObj) {
		PageModelDto<ExpertReviewDto> pageModelDto = new PageModelDto<ExpertReviewDto>();
		List<ExpertReview> resultList = expertReviewRepo.findByOdata(odataObj);
		List<ExpertReviewDto> resultDtoList = new ArrayList<ExpertReviewDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
			resultList.forEach(x->{
				ExpertReviewDto modelDto = new ExpertReviewDto();
				BeanCopierUtils.copyProperties(x, modelDto);
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
	public void save(ExpertReviewDto record) throws Exception {
		ExpertReview domain = new ExpertReview(); 
		BeanCopierUtils.copyProperties(record, domain); 
		if(Validate.isString(record.getExpertId()) && Validate.isString(record.getWorkProgramId())){
			domain.setExpert(expertRepo.findById(record.getExpertId()));
			domain.setWorkProgram(workProgramRepo.findById(record.getWorkProgramId()));
		}else{
			log.info("评审专家保存失败：无法获取专家ID和工作方案ID");
			throw new Exception(Constant.ERROR_MSG);
		}
		
		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		expertReviewRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(ExpertReviewDto record) {
		ExpertReview domain = expertReviewRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		expertReviewRepo.save(domain);
	}

	@Override
	public ExpertReviewDto findById(String id) {		
		ExpertReviewDto modelDto = new ExpertReviewDto();
		if(Validate.isString(id)){
			ExpertReview domain = expertReviewRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		expertReviewRepo.deleteById(ExpertReview_.id.getName(), id);
	}

	@Override
	public List<ExpertReviewDto> initByWorkProgramId(String workProgramId) {		
		HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+ExpertReview.class.getSimpleName()+" where "+ExpertReview_.workProgram.getName()+"."+WorkProgram_.id.getName()+" = :workProgramId ");        
        hqlBuilder.setParam("workProgramId", workProgramId);
        List<ExpertReview> list = expertReviewRepo.findByHql(hqlBuilder);
        
        if(list != null && list.size() > 0){
        	List<ExpertReviewDto> resultList = new ArrayList<ExpertReviewDto>(list.size());
        	list.forEach(l -> {
        		ExpertReviewDto epDto = new ExpertReviewDto();
        		BeanCopierUtils.copyProperties(l, epDto);
        		if(l.getExpert() != null){
        			ExpertDto expertDto = new ExpertDto();
        			BeanCopierUtils.copyProperties(l.getExpert(), expertDto);
        			epDto.setExpertDto(expertDto);
        		}
        		resultList.add(epDto);
        	});
        	
        	return resultList;
        }
		return null;
	}

	@Override
	@Transactional
	public void save(String workProgramId, String expertIds, String selectType) {		
		WorkProgram workProgram = workProgramRepo.findById(workProgramId);
		Date now = new Date();
		List<String> expertIdArr = StringUtil.getSplit(expertIds,",");			
		for(int i=0,l=expertIdArr.size();i<l;i++){
			ExpertReview domain = new ExpertReview(); 
			domain.setId(UUID.randomUUID().toString());
			
			//由于自选只能选一个，所以要先删除之前选的专家
			if(EnumExpertSelectType.SELF.getValue().equals(selectType)){	
				deleteExpert(workProgramId,null,EnumExpertSelectType.SELF.getValue(),null);
			}
			//评审会时间
			domain.setReviewDate(workProgram.getStageTime());
			domain.setSelectType(selectType);
			domain.setExpert(expertRepo.findById(expertIdArr.get(i)));
			domain.setWorkProgram(workProgram);
			domain.setState(EnumState.NO.getValue());
			
			domain.setCreatedBy(currentUser.getLoginName());
			domain.setModifiedBy(currentUser.getLoginName());
			domain.setCreatedDate(now);
			domain.setModifiedDate(now);
			expertReviewRepo.save(domain);
		}												
	}

	@Override
	public List<ExpertDto> refleshExpert(String workProgramId, String selectType) {
		List<ExpertDto> resultList = new ArrayList<ExpertDto>();
		
		HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+ExpertReview.class.getSimpleName()+" where "+ExpertReview_.workProgram.getName()+"."+WorkProgram_.id.getName()+" = :workProgramId ");
        hqlBuilder.setParam("workProgramId", workProgramId);
        hqlBuilder.append(" and "+ExpertReview_.selectType.getName()+" = :selectType ");
        hqlBuilder.setParam("selectType", selectType);
        
        List<ExpertReview> list = expertReviewRepo.findByHql(hqlBuilder);
        if(list != null && list.size() > 0){
        	list.forEach( l -> {
        		ExpertDto expertDto = new ExpertDto();
        		Expert expert = l.getExpert();
        		if(expert != null){
        			BeanCopierUtils.copyProperties(expert, expertDto);
        			resultList.add(expertDto);
        		}
        	});
        }
		return resultList;
	}

	@Override
	@Transactional
	public void updateExpertState(String workProgramId, String expertIds, String state) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+ExpertReview.class.getSimpleName()+" set "+ExpertReview_.state.getName()+" = :state ");
        hqlBuilder.setParam("state", state);
        hqlBuilder.append(" where "+ExpertReview_.workProgram.getName()+"."+WorkProgram_.id.getName()+" = :workProgramId ");
        hqlBuilder.setParam("workProgramId", workProgramId);
        
        String[] idArr = expertIds.split(",");		
        if (idArr.length > 1) {
        	hqlBuilder.append( " and "+ExpertReview_.expert.getName()+"."+Expert_.expertID.getName()+" in ( ");	  
        	int totalL = idArr.length;
        	for(int i=0;i<totalL;i++){
        		if(i==totalL-1){
        			hqlBuilder.append(" :id"+i).setParam("id"+i, idArr[i]);
        		}else{
        			hqlBuilder.append(" :id"+i+",").setParam("id"+i, idArr[i]);
        		}	        		
        	}
        	hqlBuilder.append(" ) ");
        } else {
        	hqlBuilder.append( " and "+ExpertReview_.expert.getName()+"."+Expert_.expertID.getName()+" = :expertId ");
        	hqlBuilder.setParam("expertId", expertIds);
        }
        expertReviewRepo.executeHql(hqlBuilder);
	}

	@Override
	@Transactional
	public void deleteExpert(String workProgramId, String expertIds,String seleType,String expertSelConditionId) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from "+ExpertReview.class.getSimpleName());
        hqlBuilder.append(" where "+ExpertReview_.workProgram.getName()+"."+WorkProgram_.id.getName()+" = :workProgramId ");
        hqlBuilder.setParam("workProgramId", workProgramId);

        if(Validate.isString(expertIds)){
			String[] idArr = expertIds.split(",");
			if (idArr.length > 1) {
				hqlBuilder.append( " and "+ExpertReview_.expert.getName()+"."+Expert_.expertID.getName()+" in ( ");
				int totalL = idArr.length;
				for(int i=0;i<totalL;i++){
					if(i==totalL-1){
						hqlBuilder.append(" :id"+i).setParam("id"+i, idArr[i]);
					}else{
						hqlBuilder.append(" :id"+i+",").setParam("id"+i, idArr[i]);
					}
				}
				hqlBuilder.append(" ) ");
			} else {
				hqlBuilder.append( " and "+ExpertReview_.expert.getName()+"."+Expert_.expertID.getName()+" = :expertId ");
				hqlBuilder.setParam("expertId", expertIds);
			}
		}

		if(Validate.isString(seleType)){
            hqlBuilder.append(" and "+ExpertReview_.selectType.getName() +" =:seleType").setParam("seleType",seleType);
        }

        if(Validate.isString(expertSelConditionId)){
			hqlBuilder.append(" and "+ ExpertReview_.epSelCondition.getName()+"."+ExpertSelCondition_.id.getName() +" =:expertSelConditionId").setParam("expertSelConditionId",expertSelConditionId);
		}
        expertReviewRepo.executeHql(hqlBuilder);
	}
	
}