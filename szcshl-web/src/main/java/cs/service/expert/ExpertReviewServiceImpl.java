package cs.service.expert;

import java.util.*;

import cs.common.utils.SessionUtil;
import cs.domain.expert.*;
import cs.domain.project.*;
import org.apache.log4j.Logger;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
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

/**
 * Description: 专家评审 业务操作实现类 author: ldm Date: 2017-5-17 14:02:25
 */
@Service
public class ExpertReviewServiceImpl implements ExpertReviewService {
    private static Logger log = Logger.getLogger(ExpertReviewServiceImpl.class);
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private SignRepo signRepo;

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
        domain.setModifiedBy(SessionUtil.getLoginName());
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
     *
     * @param workProgramId
     * @return
     */
    @Override
    public ExpertReviewDto initByWorkProgramId(String workProgramId) {
        ExpertReviewDto expertReviewDto = new ExpertReviewDto();
        WorkProgram workProgram = workProgramRepo.findById(workProgramId);
        //1、获取抽取方案
        ExpertReview expertReview = workProgram.getExpertReview();
        if (expertReview == null || !Validate.isString(expertReview.getId())) {
            //初始化
            expertReview = new ExpertReview();
            Date now = new Date();
            expertReview.setCreatedBy(SessionUtil.getLoginName());
            expertReview.setCreatedDate(now);
            expertReview.setModifiedBy(SessionUtil.getLoginName());
            expertReview.setModifiedDate(now);
            expertReview.setReviewDate(workProgram.getStageTime());  //评审会时间
            expertReviewRepo.save(expertReview);

            //关联工作方案
            workProgram.setExpertReview(expertReview);
            workProgramRepo.save(workProgram);
        }
        BeanCopierUtils.copyProperties(expertReview, expertReviewDto);
        //2、专家抽取条件信息
        if (expertReview.getExpertSelConditionList() != null && expertReview.getExpertSelConditionList().size() > 0) {
            List<ExpertSelConditionDto> conditionDtoList = new ArrayList<>();
            for (ExpertSelCondition condition : expertReview.getExpertSelConditionList()) {
                ExpertSelConditionDto conditionDto = new ExpertSelConditionDto();
                BeanCopierUtils.copyProperties(condition, conditionDto);
                conditionDtoList.add(conditionDto);//设置抽取条件Dto
            }
            expertReviewDto.setExpertSelConditionDtoList(conditionDtoList); //设置抽取条件列表Dto
        }
        //3、抽取专家
        if (expertReview.getExpertSelectedList() != null && expertReview.getExpertSelectedList().size() > 0) {
            List<ExpertSelectedDto> selDtoList = new ArrayList<>();
            for (ExpertSelected epSelted : expertReview.getExpertSelectedList()) {
                ExpertSelectedDto selDto = new ExpertSelectedDto();
                BeanCopierUtils.copyProperties(epSelted, selDto);
                //4、专家信息
                if (epSelted.getExpert() != null && Validate.isString(epSelted.getExpert().getExpertID())) {
                    ExpertDto expertDto = new ExpertDto();
                    BeanCopierUtils.copyProperties(epSelted.getExpert(), expertDto);
                    selDto.setExpertDto(expertDto);//设置专家信息Dto
                }
                selDtoList.add(selDto);//设置抽取专家Dto
            }
            expertReviewDto.setExpertSelectedDtoList(selDtoList);//设置抽取专家列表Dto
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
    public void expertMark(ExpertReviewDto expertReviewDto) {
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
    public void savePayment(ExpertReviewDto expertReviewDto) throws Exception {
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
     * 确认抽取结果信息
     *
     * @param reviewId
     * @param state
     */
    @Override
    public void affirmAutoExpert(String reviewId, String state) {
        ExpertReview expertReview = expertReviewRepo.findById(reviewId);
        expertReview.setIsComfireResult(state);
        expertReviewRepo.save(expertReview);
    }

    /**
     * 保存自选或者境外专家
     *
     * @param reviewId
     * @param expertIds
     * @param selectType
     */
    @Override
    @Transactional
    public void save(String reviewId, String expertIds, String selectType, boolean isDraw) {
        ExpertReview expertReview = expertReviewRepo.findById(reviewId);
        //保存抽取专家
        List<String> expertIdArr = StringUtil.getSplit(expertIds, ",");
        for (int i = 0, l = expertIdArr.size(); i < l; i++) {
            //如果是专家自选，则要删除之前选择的专家信息
            if (Constant.EnumExpertSelectType.SELF.getValue().equals(selectType)) {
                for (ExpertSelected epSelected : expertReview.getExpertSelectedList()) {
                    if (Constant.EnumExpertSelectType.SELF.getValue().equals(epSelected.getSelectType())) {
                        expertSelectedRepo.deleteById(ExpertSelected_.id.getName(), epSelected.getId());
                    }
                }
            }
            ExpertSelected expertSelected = new ExpertSelected();
            expertSelected.setIsJoin(Constant.EnumState.YES.getValue());
            expertSelected.setSelectType(selectType);
            //保存专家映射
            expertSelected.setExpert(expertRepo.findById(expertIdArr.get(i)));
            //保存抽取条件映射
            expertSelected.setExpertReview(expertReview);
            expertSelectedRepo.save(expertSelected);
        }

        //如果已经抽签，则更改评审方案状态
        if (isDraw) {
            expertReview.setIsSelete(Constant.EnumState.YES.getValue());
            expertReview.setSelCount(expertReview.getSelCount() == null ? 1 : expertReview.getSelCount() + 1);
            expertReviewRepo.save(expertReview);
        }
    }


    /**
     * 更改抽取专家状态(直接用sql更新)
     *
     * @param reviewId  抽取方案ID
     * @param expertIds 专家ID
     * @param state     状态值
     * @param isConfirm 是否已经确认
     */
    @Override
    @Transactional
    public void updateExpertState(String reviewId, String expertIds, String state, boolean isConfirm) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_expert_selected set " + ExpertSelected_.isJoin.getName() + " =:state ");
        sqlBuilder.setParam("state", state);

        String[] idArr = expertIds.split(",");
        if (idArr.length > 1) {
            sqlBuilder.append(" where expertid in ( ");
            int totalL = idArr.length;
            for (int i = 0; i < totalL; i++) {
                if (i == totalL - 1) {
                    sqlBuilder.append(" :id" + i).setParam("id" + i, idArr[i]);
                } else {
                    sqlBuilder.append(" :id" + i + ",").setParam("id" + i, idArr[i]);
                }
            }
            sqlBuilder.append(" ) ");
        } else {
            sqlBuilder.append(" where expertid = :expertId ");
            sqlBuilder.setParam("expertId", expertIds);
        }
        expertReviewRepo.executeSql(sqlBuilder);

        //如果已经确认，则修改状态
        if (isConfirm) {
            ExpertReview expertReview = expertReviewRepo.findById(reviewId);
            expertReview.setIsComfireResult(Constant.EnumState.YES.getValue());
            expertReviewRepo.save(expertReview);
        }
    }

    /**
     * 根据工作方案获取评审专家
     *
     * @param signId
     * @return
     */
    @Override
    public PageModelDto<ExpertSelectedDto> getSelectExpert(String signId) {
        PageModelDto<ExpertSelectedDto> pageModelDto = new PageModelDto<ExpertSelectedDto>();
        Sign sign = signRepo.getById(signId);
        if (sign.getWorkProgramList() != null && sign.getWorkProgramList().size() > 0) {

            List<WorkProgram> workPrograms = sign.getWorkProgramList();
            List<ExpertSelectedDto> expertSelectedDtos = new ArrayList<ExpertSelectedDto>();


            workPrograms.forEach(workProgram -> {

                ExpertReview expertReview = workProgram.getExpertReview();
                if (expertReview != null && expertReview.getExpertSelectedList() != null
                        && expertReview.getExpertSelectedList().size() > 0) {
                    List<ExpertSelected> expertSelecteds = expertReview.getExpertSelectedList();
                    expertSelecteds.forEach(expertSelected -> {
                        ExpertSelectedDto expertSelectedDto = new ExpertSelectedDto();
                        BeanUtils.copyProperties(expertSelected, expertSelectedDto, new String[]{ExpertSelected_.expertReview.getName()});

                        //评审专家设置
                        expertSelectedDto.setExpertDto(new ExpertDto());
                        BeanUtils.copyProperties(expertSelected.getExpert(), expertSelectedDto.getExpertDto(),
                                new String[]{Expert_.photo.getName(), Expert_.work.getName(),
                                        Expert_.project.getName(), Expert_.expertSelectedList.getName()});

                        //评审方案设置
                        expertSelectedDto.setExpertReviewDto(new ExpertReviewDto());
                        BeanUtils.copyProperties(expertSelected.getExpertReview(), expertSelectedDto.getExpertReviewDto(),
                                ExpertReview_.workProgramList.getName(), ExpertReview_.expertSelConditionList.getName(),
                                ExpertReview_.expertSelectedList.getName());

                        expertSelectedDtos.add(expertSelectedDto);

                    });
                }

            });

            pageModelDto.setValue(expertSelectedDtos);
            pageModelDto.setCount(expertSelectedDtos.size());

            return pageModelDto;
        }


        return null;
    }

    @Override
    public PageModelDto<ExpertReviewDto> getBySignId(String signId) {
        PageModelDto<ExpertReviewDto> expertReviewDtoPageModelDto = new PageModelDto<ExpertReviewDto>();
        List<ExpertReviewDto> expertReviewDtos = new ArrayList<ExpertReviewDto>();
        Sign sign = signRepo.getById(signId);

        List<WorkProgram> workPrograms = sign.getWorkProgramList();
        if (workPrograms != null && workPrograms.size() > 0) {
            Map<String, ExpertReview> expertReviewMap = new HashMap<String, ExpertReview>();
            workPrograms.forEach(workProgram -> {
                ExpertReview expertReview = workProgram.getExpertReview();
                if (expertReview != null) {
                    expertReviewMap.put(expertReview.getId(), expertReview);
                }
            });

            for (Map.Entry<String, ExpertReview> entry : expertReviewMap.entrySet()) {
                ExpertReview expertReview = entry.getValue();
                ExpertReviewDto expertReviewDto = new ExpertReviewDto();
                BeanUtils.copyProperties(expertReview, expertReviewDto,
                        new String[]{ExpertReview_.expertSelConditionList.getName(), ExpertReview_.workProgramList.getName()});

                //设置评审费发放默认标题 《项目名称+项目阶段》+评审费发放表
                if (!Validate.isString(expertReview.getReviewTitle())) {
                    expertReviewDto.setReviewTitle("《" + sign.getProjectname() + sign.getReviewstage() + "》评审费发放表");
                }

                //设置评审方案的专家
                List<ExpertSelected> expertSelecteds = expertReview.getExpertSelectedList();
                List<ExpertSelectedDto> expertSelectedDtos = new ArrayList<ExpertSelectedDto>();

                if (expertSelecteds != null && expertSelecteds.size() > 0) {
                    expertSelecteds.forEach(expertSelected -> {
                        ExpertSelectedDto expertSelectedDto = new ExpertSelectedDto();
                        BeanUtils.copyProperties(expertSelected, expertSelectedDto,
                                new String[]{ExpertSelected_.expertReview.getName()});

                        //======================设置专家======================================================
                        expertSelectedDto.setExpertDto(new ExpertDto());
                        BeanUtils.copyProperties(expertSelected.getExpert(), expertSelectedDto.getExpertDto(),
                                new String[]{Expert_.photo.getName(), Expert_.work.getName(),
                                        Expert_.project.getName(), Expert_.expertSelectedList.getName()});
                        //===================================================================================

                        expertSelectedDtos.add(expertSelectedDto);
                    });

                }
                expertReviewDto.setExpertSelectedDtoList(expertSelectedDtos);
                expertReviewDtos.add(expertReviewDto);
            }

        }

        expertReviewDtoPageModelDto.setValue(expertReviewDtos);
        expertReviewDtoPageModelDto.setCount(expertReviewDtos.size());

        return expertReviewDtoPageModelDto;
    }

    /**
     * 获取指定专家，指定月份的评审费用
     */
    @Override
    public List<Map<String, Object>> getExpertReviewCost(String expertIds, String month) {
        List<Map<String, Object>> experReviewCosts = null;
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select * from V_EXPERT_PAY_HIS t where t.expertid in (" + expertIds + ") and t.paydate='" + month + "'");
        //hqlBuilder.setParam("ids",expertIds);
        //hqlBuilder.setParam("m",month);
        NativeQuery nativeQuery = expertReviewRepo.getSession().createNativeQuery(hqlBuilder.getHqlString());
        experReviewCosts = nativeQuery.list();
        return experReviewCosts;
    }

    @Override
    @Transactional
    public void saveExpertReviewCost(ExpertReviewDto[] expertReviews) {

        if (expertReviews != null && expertReviews.length > 0) {
            for (int i = 0; i < expertReviews.length; i++) {

                ExpertReviewDto expertReviewDto = expertReviews[i];
                List<ExpertSelectedDto> expertSelectedDtos = expertReviewDto.getExpertSelectedDtoList();
                //保存评审费用
                String experReviewId = expertReviewDto.getId();
                if (Validate.isString(experReviewId)) {
                    ExpertReview expertReview = expertReviewRepo.findById(experReviewId);
                    if (expertReview != null) {
                        //设置评审方案的评审费用、税费和合计
                        expertReview.setReviewCost(expertReviewDto.getReviewCost());
                        expertReview.setReviewTaxes(expertReviewDto.getReviewTaxes());
                        expertReview.setTotalCost(expertReviewDto.getTotalCost());
                        //设置标题
                        expertReview.setReviewTitle(expertReviewDto.getReviewTitle());
                        //设置评审费发放日期
                        expertReview.setPayDate(expertReviewDto.getPayDate());
                        //设置该评审方案所有专家的评审费用、税费和合计
                        List<ExpertSelected> expertSelecteds = expertReview.getExpertSelectedList();
                        if (expertSelecteds != null && expertSelecteds.size() > 0
                                && expertSelectedDtos != null && expertSelectedDtos.size() > 0) {
                            expertSelecteds.forEach(expertSelected -> {

                                expertSelectedDtos.forEach(expertSelectedDto -> {
                                    if (expertSelectedDto.getId().equals(expertSelected.getId())) {
                                        expertSelected.setReviewCost(expertSelectedDto.getReviewCost());
                                        expertSelected.setReviewTaxes(expertSelectedDto.getReviewTaxes());
                                        expertSelected.setTotalCost(expertSelectedDto.getTotalCost());
                                        return;
                                    }

                                });

                            });
                        }
                        //设置专家评审费用结束s

                        expertReviewRepo.save(expertReview);
                    }

                }
                //保存评审费用

            }
        }
    }
}