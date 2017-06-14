package cs.service.expert;

import cs.common.Constant;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.expert.*;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.service.project.SignService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cs.domain.meeting.RoomBooking_.workProgram;

/**
 * Description: 专家评审 业务操作实现类 author: ldm Date: 2017-5-17 14:02:25
 */
@Service
public class ExpertReviewServiceImpl implements ExpertReviewService {
	private static Logger log = Logger.getLogger(ExpertReviewServiceImpl.class);
	@Autowired
	private ExpertReviewRepo expertReviewRepo;
	@Autowired
	private ICurrentUser currentUser;
    @Autowired
    private ExpertSelConditionRepo expertSelConditionRepo;
	@Autowired
	private ExpertRepo expertRepo;
	@Autowired
	private WorkProgramRepo workProgramRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
	@Autowired
	private SignRepo signRepo;
    @Autowired
    private SignService signService;

	@Override
	public PageModelDto<ExpertReviewDto> get(ODataObj odataObj) {
		PageModelDto<ExpertReviewDto> pageModelDto = new PageModelDto<ExpertReviewDto>();
		List<ExpertReview> resultList = expertReviewRepo.findByOdata(odataObj);
		List<ExpertReviewDto> resultDtoList = new ArrayList<ExpertReviewDto>(resultList.size());

		if (resultList != null && resultList.size() > 0) {
			resultList.forEach(x -> {
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
		/*ExpertReview domain = new ExpertReview();
		BeanCopierUtils.copyProperties(record, domain);
		if (Validate.isString(record.getExpertId()) && Validate.isString(record.getWorkProgramId())) {
			domain.setExpert(expertRepo.findById(record.getExpertId()));
			domain.setWorkProgram(workProgramRepo.findById(record.getWorkProgramId()));
		} else {
			log.info("评审专家保存失败：无法获取专家ID和工作方案ID");
			throw new Exception(Constant.ERROR_MSG);
		}

		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		expertReviewRepo.save(domain);*/
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
		if (Validate.isString(id)) {
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

    /**
     * 根据工作方案ID初始化专家抽取
     * @param workProgramId
     * @return
     */
	@Override
	public ExpertReviewDto initByWorkProgramId(String workProgramId) {
        ExpertReviewDto expertReviewDto = new ExpertReviewDto();
        WorkProgram workProgram = workProgramRepo.findById(workProgramId);
        //1、获取抽取方案
        ExpertReview expertReview = workProgram.getExpertReview();
        if(expertReview == null|| !Validate.isString(expertReview.getId())) {
            //初始化
            expertReview = new ExpertReview();
            Date now = new Date();
            expertReview.setCreatedBy(currentUser.getLoginName());
            expertReview.setCreatedDate(now);
            expertReview.setModifiedBy(currentUser.getLoginName());
            expertReview.setModifiedDate(now);
            expertReview.setReviewDate(workProgram.getStageTime());  //评审会时间
            expertReviewRepo.save(expertReview);

            //关联工作方案
            workProgram.setExpertReview(expertReview);
            workProgramRepo.save(workProgram);
        }
        BeanCopierUtils.copyProperties(expertReview,expertReviewDto);
        //2、专家抽取条件信息
        if(expertReview.getExpertSelConditionList() != null && expertReview.getExpertSelConditionList().size() > 0){
            List<ExpertSelConditionDto> conditionDtoList = new ArrayList<>();
            for(ExpertSelCondition condition :expertReview.getExpertSelConditionList()){
                ExpertSelConditionDto conditionDto = new ExpertSelConditionDto();
                BeanCopierUtils.copyProperties(condition,conditionDto);
                //3、抽取专家
                if(condition.getExpertSelectedList() != null && condition.getExpertSelectedList().size() > 0){
                    List<ExpertSelectedDto> selDtoList = new ArrayList<>();
                    for(ExpertSelected epSelted : condition.getExpertSelectedList()){
                        ExpertSelectedDto selDto = new ExpertSelectedDto();
                        BeanCopierUtils.copyProperties(epSelted,selDto);
                        //4、专家信息
                        if(epSelted.getExpert() != null && Validate.isString(epSelted.getExpert().getExpertID())){
                            ExpertDto expertDto = new ExpertDto();
                            BeanCopierUtils.copyProperties(epSelted.getExpert(),expertDto);
                            selDto.setExpertDto(expertDto);//设置专家信息Dto
                        }
                        selDtoList.add(selDto);//设置抽取专家Dto
                    }
                    conditionDto.setExpertSelectedDtoList(selDtoList);//设置抽取专家列表Dto
                }
                conditionDtoList.add(conditionDto);//设置抽取条件Dto
            }
            expertReviewDto.setExpertSelConditionDtoList(conditionDtoList); //设置抽取条件列表Dto
        }

        return expertReviewDto;
	}

	@Override
	public Map<String, Object> getReviewList(String orgName, String year, String quarter) {

		return null;
	}
	//更新数据,保存分数
	@Override
	@Transactional
	public void expertMark(ExpertReviewDto expertReviewDto ) {
		/*Date now=new Date();
		Expert expert=expertRepo.getById(expertReviewDto.getExpertId());
		ExpertReview expertReview=expert.getExpertReview();
		if(expertReview != null){
			expertReview.setScore(expertReviewDto.getScore()==null?0:expertReviewDto.getScore());
			expertReview.setDescribes(expertReviewDto.getDescribes());
			expertReview.setReviewDate(now);
			expert.setExpertReview(expertReview);
			expertRepo.save(expert);
		}*/
	}

	//更新数据,保存费用
	@Override
	@Transactional
	public void savePayment(ExpertReviewDto expertReviewDto)throws Exception {
		/*if(Validate.isString(expertReviewDto.getExpertId())){
			Date now=new Date();
			Expert expert=expertRepo.getById(expertReviewDto.getExpertId());
			if(expert!=null && !Validate.isBlank(expert.getExpertID())){
				ExpertReview expertReview=expert.getExpertReview();
				if(expertReview != null){
					BeanCopierUtils.copyPropertiesIgnoreNull(expertReviewDto,expertReview);
					expertReview.setModifiedBy(currentUser.getLoginName());
					expertReview.setModifiedDate(now);
					expert.setExpertReview(expertReview);
				}
				expertRepo.save(expert);
			}
		} else {
			log.info("提交收文信息异常：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}*/
	}

    /**
     * 保存自选或者境外专家
     * @param reviewId
     * @param expertIds
     * @param selectType
     */
	@Override
	@Transactional
	public void save(String reviewId, String expertIds, String selectType) {
	    //评审方案
        ExpertReview expertReview = expertReviewRepo.findById(reviewId);
        //保存抽取条件
        ExpertSelCondition newCondition = new ExpertSelCondition();
        newCondition.setSelectType(selectType);
        newCondition.setExpertReview(expertReview);
        expertSelConditionRepo.save(newCondition);
        //保存抽取专家
		List<String> expertIdArr = StringUtil.getSplit(expertIds, ",");
		for (int i = 0, l = expertIdArr.size(); i < l; i++) {
            ExpertSelected expertSelected = new ExpertSelected();
            expertSelected.setIsJoin(Constant.EnumState.YES.getValue());
            //保存专家映射
            expertSelected.setExpert(expertRepo.findById(expertIdArr.get(i)));
            //保存抽取条件映射
            expertSelected.setExpertSelCondition(newCondition);
            expertSelectedRepo.save(expertSelected);
		}
	}

	@Override
	public List<ExpertDto> refleshExpert(String workProgramId, String selectType) {
		/*List<ExpertDto> resultList = new ArrayList<ExpertDto>();

		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" from " + ExpertReview.class.getSimpleName() + " where "
				+ ExpertReview_.workProgram.getName() + "." + WorkProgram_.id.getName() + " = :workProgramId ");
		hqlBuilder.setParam("workProgramId", workProgramId);
		hqlBuilder.append(" and " + ExpertReview_.selectType.getName() + " = :selectType ");
		hqlBuilder.setParam("selectType", selectType);

		List<ExpertReview> list = expertReviewRepo.findByHql(hqlBuilder);
		if (list != null && list.size() > 0) {
			list.forEach(l -> {
				ExpertDto expertDto = new ExpertDto();
				Expert expert = l.getExpert();
				if (expert != null) {
					BeanCopierUtils.copyProperties(expert, expertDto);
					resultList.add(expertDto);
				}
			});
		}
		return resultList;*/
		return null;
	}

	@Override
	@Transactional
	public void updateExpertState(String workProgramId, String expertIds, String state) {
		/*HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" update " + ExpertReview.class.getSimpleName() + " set " + ExpertReview_.state.getName()
				+ " = :state ");
		hqlBuilder.setParam("state", state);
		hqlBuilder.append(" where " + ExpertReview_.workProgram.getName() + "." + WorkProgram_.id.getName()
				+ " = :workProgramId ");
		hqlBuilder.setParam("workProgramId", workProgramId);

		String[] idArr = expertIds.split(",");
		if (idArr.length > 1) {
			hqlBuilder.append(" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " in ( ");
			int totalL = idArr.length;
			for (int i = 0; i < totalL; i++) {
				if (i == totalL - 1) {
					hqlBuilder.append(" :id" + i).setParam("id" + i, idArr[i]);
				} else {
					hqlBuilder.append(" :id" + i + ",").setParam("id" + i, idArr[i]);
				}
			}
			hqlBuilder.append(" ) ");
		} else {
			hqlBuilder.append(
					" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " = :expertId ");
			hqlBuilder.setParam("expertId", expertIds);
		}
		expertReviewRepo.executeHql(hqlBuilder);*/
	}

	@Override
	@Transactional
	public void deleteExpert(String workProgramId, String expertIds, String seleType, String expertSelConditionId) {
		/*HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" delete from " + ExpertReview.class.getSimpleName());
		hqlBuilder.append(" where " + ExpertReview_.workProgram.getName() + "." + WorkProgram_.id.getName()
				+ " = :workProgramId ");
		hqlBuilder.setParam("workProgramId", workProgramId);

		if (Validate.isString(expertIds)) {
			String[] idArr = expertIds.split(",");
			if (idArr.length > 1) {
				hqlBuilder
						.append(" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " in ( ");
				int totalL = idArr.length;
				for (int i = 0; i < totalL; i++) {
					if (i == totalL - 1) {
						hqlBuilder.append(" :id" + i).setParam("id" + i, idArr[i]);
					} else {
						hqlBuilder.append(" :id" + i + ",").setParam("id" + i, idArr[i]);
					}
				}
				hqlBuilder.append(" ) ");
			} else {
				hqlBuilder.append(
						" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " = :expertId ");
				hqlBuilder.setParam("expertId", expertIds);
			}
		}

		if (Validate.isString(seleType)) {
			hqlBuilder.append(" and " + ExpertReview_.selectType.getName() + " =:seleType").setParam("seleType",
					seleType);
		}

		if (Validate.isString(expertSelConditionId)) {
			hqlBuilder.append(" and " + ExpertReview_.epSelCondition.getName() + "." + ExpertSelCondition_.id.getName()
					+ " =:expertSelConditionId").setParam("expertSelConditionId", expertSelConditionId);
		}
		expertReviewRepo.executeHql(hqlBuilder);*/
	}

}