package cs.service.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertCostCountDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertCostCountRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 抽取专家 业务操作实现类
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Service
public class ExpertSelectedServiceImpl  implements ExpertSelectedService {

	@Autowired
	private ExpertSelectedRepo expertSelectedRepo;
	@Autowired
	private ExpertReviewRepo expertReviewRepo;
	@Autowired
	private ExpertCostCountRepo expertCostCountRepo;

	@Override
	@Transactional
	public void save(ExpertSelectedDto record) {
		ExpertSelected domain = new ExpertSelected(); 
		BeanCopierUtils.copyProperties(record, domain); 

		expertSelectedRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(ExpertSelectedDto record) {
		ExpertSelected domain = expertSelectedRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);

		expertSelectedRepo.save(domain);
	}

	@Override
	public ExpertSelectedDto findById(String id) {		
		ExpertSelectedDto modelDto = new ExpertSelectedDto();
		if(Validate.isString(id)){
			ExpertSelected domain = expertSelectedRepo.findById(id);
			modelDto.setExpertDto(new ExpertDto());
			BeanCopierUtils.copyProperties(domain, modelDto);
			BeanCopierUtils.copyProperties(domain.getExpert(),modelDto.getExpertDto());
		}		
		return modelDto;
	}

    /**
     * 删除已经抽取的专家（主要针对自选和境外专家）
     * @param reviewId
     * @param ids
     */
	@Override
	@Transactional
	public ResultMsg delete(String reviewId, String ids, boolean deleteAll) {
		ExpertReview expertReview = expertReviewRepo.findById(reviewId);
		if(deleteAll){
			expertReviewRepo.delete(expertReview);
        }else{
            expertSelectedRepo.deleteById(ExpertSelected_.id.getName(),ids);
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"删除成功！");
	}

	@Override
	public PageModelDto<ExpertSelectedDto> get(ODataObj odataObj) {
		PageModelDto<ExpertSelectedDto> pageModelDto = new PageModelDto<ExpertSelectedDto>();
		List<ExpertSelected> resultList = expertSelectedRepo.findByOdata(odataObj);
		List<ExpertSelectedDto> resultDtoList = new ArrayList<ExpertSelectedDto>(resultList.size());
		if (resultList != null && resultList.size() > 0) {
			resultList.forEach(x -> {
				ExpertSelectedDto modelDto = new ExpertSelectedDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				modelDto.setExpertDto(new ExpertDto());
				modelDto.setExpertReviewDto(new ExpertReviewDto());
				BeanCopierUtils.copyProperties(x.getExpert(),modelDto.getExpertDto());
				BeanCopierUtils.copyProperties(x.getExpertReview(),modelDto.getExpertReviewDto());
				resultDtoList.add(modelDto);
			});
		}
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	/**
	 * 专家评审费用统计
	 * @param odataObj
	 * @return
	 */
	@Override
	public ResultMsg expertCostTotal(ODataObj odataObj) {
		//odataObj.getFilter().get(0);
		Map<String,Object> resultMap = new HashMap<>();
		PageModelDto<ExpertCostCountDto> pageModelDto = new PageModelDto<ExpertCostCountDto>();
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select t.name,t.idcard,t.userphone,sum(t.reviewcost)reviewcost,sum(t.reviewtaxes)reviewtaxes,sum(t.yreviewcost)yreviewcost,sum(t.yreviewtaxes)yreviewtaxes from( ");
		sqlBuilder.append("select e.name,e.idcard,e.userphone,sum(s.reviewcost) reviewcost,sum(s.reviewtaxes)reviewtaxes,null yreviewcost,null yreviewtaxes from cs_expert_selected s ");
		sqlBuilder.append("left join cs_expert e  on s.expertid = e.expertid ");
		sqlBuilder.append("left join cs_expert_review r on s.expertreviewid = r.id ");
		sqlBuilder.append("where 1=1 ");
		sqlBuilder.append("and r.reviewdate >= to_date('2017-06-01 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		sqlBuilder.append("and r.reviewdate <= to_date('2017-06-30 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
		sqlBuilder.append("group by e.expertid,e.name,e.idcard,e.userphone ");
		sqlBuilder.append("having sum(s.reviewcost)>0" );
		sqlBuilder.append("union  ");
		sqlBuilder.append("select e.name,e.idcard,e.userphone,null reviewcost,null reviewtaxes,sum(s.reviewcost) yreviewcost,sum(s.reviewtaxes)yreviewtaxes from cs_expert_selected s  ");
		sqlBuilder.append("left join cs_expert e  on s.expertid = e.expertid  ");
		sqlBuilder.append("left join cs_expert_review r on s.expertreviewid = r.id ");
		sqlBuilder.append("where r.reviewdate >= to_date('2017-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		sqlBuilder.append("and  r.reviewdate <= to_date('2017-12-31 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
		sqlBuilder.append("group by e.expertid,e.name,e.idcard,e.userphone ");
		sqlBuilder.append("having sum(s.reviewcost)>0 ) t ");
		sqlBuilder.append("group by t.name,t.idcard,t.userphone ");
		//List<ExpertCostCountDto> expertCostCountDtoList = expertCostCountRepo.findBySql(sqlBuilder);
		List<Map> expertCostCountDtoList = expertCostCountRepo.findMapListBySql(sqlBuilder);
		List<ExpertCostCountDto> expertCostCountDtoList1 = new ArrayList<ExpertCostCountDto>();
		if(expertCostCountDtoList.size()>0){
			for(int i=0 ; i<expertCostCountDtoList.size();i++){
				Object obj=expertCostCountDtoList.get(i);
				Object[] expertCostCounts = (Object[]) obj;
				ExpertCostCountDto expertCostCountDto = new ExpertCostCountDto();
				if(null != expertCostCounts[0]){
					expertCostCountDto.setName((String)expertCostCounts[0]);
				}
				if(null != expertCostCounts[1]){
					expertCostCountDto.setIdCard((String)expertCostCounts[1]);
				}
				if(null != expertCostCounts[2]){
					expertCostCountDto.setUserPhone((String)expertCostCounts[2]);
				}
				if(null != expertCostCounts[3]){
					expertCostCountDto.setReviewcost((BigDecimal) expertCostCounts[3]);
				}
				if(null != expertCostCounts[4]){
					expertCostCountDto.setReviewtaxes((BigDecimal) expertCostCounts[4]);
				}
				if(null != expertCostCounts[5]){
					expertCostCountDto.setYreviewcost((BigDecimal) expertCostCounts[5]);
				}
				if(null != expertCostCounts[6]){
					expertCostCountDto.setYreviewtaxes((BigDecimal) expertCostCounts[6]);
				}

				expertCostCountDtoList1.add(expertCostCountDto);
			}
		}
		resultMap.put("expertCostTotalInfo",expertCostCountDtoList1);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
	}

}