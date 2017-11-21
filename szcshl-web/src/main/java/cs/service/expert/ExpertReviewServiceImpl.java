package cs.service.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;

    @Autowired
    private WorkProgramRepo workProgramRepo;


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
     * 根据业务ID初始化专家抽取方案
     *
     * @param businessId
     * @return
     */
    @Override
    public ExpertReviewDto initBybusinessId(String businessId, String minBusinessId) {
        ExpertReviewDto expertReviewDto = new ExpertReviewDto();
        boolean isFilter = Validate.isString(minBusinessId) ? true : false;
        //1、根据工作方案ID 获取评审方案
        ExpertReview expertReview = expertReviewRepo.findById(ExpertReview_.businessId.getName(), businessId);
        if (expertReview != null && Validate.isString(expertReview.getId())) {
            BeanCopierUtils.copyProperties(expertReview, expertReviewDto);
            //2、专家抽取条件信息
            if (expertReview.getExpertSelConditionList() != null && expertReview.getExpertSelConditionList().size() > 0) {
                List<ExpertSelConditionDto> conditionDtoList = new ArrayList<>();
                for (ExpertSelCondition condition : expertReview.getExpertSelConditionList()) {
                    if (isFilter) {
                        if (minBusinessId.equals(condition.getBusinessId())) {
                            ExpertSelConditionDto conditionDto = new ExpertSelConditionDto();
                            BeanCopierUtils.copyProperties(condition, conditionDto);
                            conditionDtoList.add(conditionDto);//设置抽取条件Dto
                        }
                    } else {
                        ExpertSelConditionDto conditionDto = new ExpertSelConditionDto();
                        BeanCopierUtils.copyProperties(condition, conditionDto);
                        conditionDtoList.add(conditionDto);//设置抽取条件Dto
                    }
                }
                expertReviewDto.setExpertSelConditionDtoList(conditionDtoList); //设置抽取条件列表Dto
            }
            //3、抽取专家
            if (expertReview.getExpertSelectedList() != null && expertReview.getExpertSelectedList().size() > 0) {
                List<ExpertSelectedDto> selDtoList = new ArrayList<>();
                for (ExpertSelected epSelted : expertReview.getExpertSelectedList()) {
                    if (isFilter) {
                        if (minBusinessId.equals(epSelted.getBusinessId())) {
                            selDtoList.add(initSelExpert(epSelted));//设置抽取专家Dto
                            //如果有抽取类型的专家，则说明已经已经整体专家的抽取
                            if (Constant.EnumExpertSelectType.AUTO.getValue().equals(epSelted.getSelectType())) {
                                expertReviewDto.setState(Constant.EnumState.YES.getValue());  //表示专家已经进行整体抽取方案的抽取
                            }
                        }
                    } else {
                        selDtoList.add(initSelExpert(epSelted));//设置抽取专家Dto
                    }
                }
                expertReviewDto.setExpertSelectedDtoList(selDtoList);//设置抽取专家列表Dto
            }

        }
        return expertReviewDto;
    }

    private ExpertSelectedDto initSelExpert(ExpertSelected epSelted) {
        ExpertSelectedDto selDto = new ExpertSelectedDto();
        BeanCopierUtils.copyProperties(epSelted, selDto);
        //4、专家信息
        if (epSelted.getExpert() != null && Validate.isString(epSelted.getExpert().getExpertID())) {
            Expert ep = epSelted.getExpert();
            ep.setPhoto(null);
            ExpertDto expertDto = new ExpertDto();
            BeanCopierUtils.copyProperties(ep, expertDto);
            selDto.setExpertDto(expertDto);//设置专家信息Dto
        }
        return selDto;
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
    public ResultMsg save(String businessId, String minBusinessId, String businessType, String reviewId, String expertIds, String selectType) {
        ExpertReview expertReview = null;
        if (!Validate.isString(reviewId)) {
            Date now = new Date();
            expertReview = new ExpertReview();
            expertReview.setBusinessId(businessId);
            expertReview.setCreatedBy(SessionUtil.getLoginName());
            expertReview.setModifiedBy(SessionUtil.getLoginName());
            expertReview.setCreatedDate(now);
            expertReview.setModifiedDate(now);
            expertReview.setBusinessType(businessType);
            //获取评审会日期
            if (Constant.BusinessType.SIGN.getValue().equals(businessType)) {
                WorkProgram wp = workProgramRepo.findById(WorkProgram_.id.getName(),minBusinessId);
                if("专家函评".equals(wp.getReviewType())){
                    expertReview.setReviewDate(wp.getLetterDate());
                }
            }
            if(expertReview.getReviewDate() == null){
                expertReview.setReviewDate(roomBookingRepo.getMeetingDateByBusinessId(minBusinessId));
            }
            //评审费发放标题
            expertReviewRepo.initReviewTitle(expertReview, businessId, businessType);
            expertReviewRepo.save(expertReview);
        } else {
            expertReview = expertReviewRepo.findById(ExpertReview_.id.getName(), reviewId);
        }

        List<ExpertSelectedDto> resultList = new ArrayList<>();
        //保存抽取专家
        List<String> expertIdArr = StringUtil.getSplit(expertIds, ",");
        for (int i = 0, l = expertIdArr.size(); i < l; i++) {
            //如果是专家自选，则要删除之前选择的专家信息
            if (Constant.EnumExpertSelectType.SELF.getValue().equals(selectType) && Validate.isList(expertReview.getExpertSelectedList())) {
                for (ExpertSelected epSelected : expertReview.getExpertSelectedList()) {
                    if (Constant.EnumExpertSelectType.SELF.getValue().equals(epSelected.getSelectType())) {
                        expertSelectedRepo.deleteById(ExpertSelected_.id.getName(), epSelected.getId());
                    }
                }
            }
            ExpertSelected expertSelected = new ExpertSelected();
            expertSelected.setReviewCost(new BigDecimal(1000)); //每个专家默认评审费1000元
            expertSelected.setBusinessId(minBusinessId);
            expertSelected.setIsJoin(Constant.EnumState.YES.getValue());
            expertSelected.setIsConfrim(Constant.EnumState.YES.getValue());
            expertSelected.setSelectType(selectType);
            //如果是自选或者境外专家，默认已经确认
            if (Constant.EnumExpertSelectType.SELF.getValue().equals(expertSelected.getSelectType())
                    || Constant.EnumExpertSelectType.OUTSIDE.getValue().equals(expertSelected.getSelectType())) {
                expertSelected.setIsConfrim(Constant.EnumState.YES.getValue());
            }
            //保存专家映射
            expertSelected.setExpert(expertRepo.findById(Expert_.expertID.getName(), expertIdArr.get(i)));
            //保存抽取条件映射
            expertSelected.setExpertReview(expertReview);
            expertSelectedRepo.save(expertSelected);

            //设置前端显示信息
            ExpertSelectedDto dto = new ExpertSelectedDto();
            BeanCopierUtils.copyProperties(expertSelected, dto);
            Expert ep = expertSelected.getExpert();
            ep.setPhoto(null);
            ExpertDto expertDto = new ExpertDto();
            BeanCopierUtils.copyProperties(ep, expertDto);
            dto.setExpertDto(expertDto);//设置专家信息Dto
            resultList.add(dto);
        }
        //更改专家评审费
        if (Constant.BusinessType.SIGN.getValue().equals(businessType)) {
            workProgramRepo.initExpertCost(minBusinessId);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), expertReview.getId(), "操作成功！", resultList);
    }


    /**
     * 更改抽取专家状态(直接用sql更新)
     *
     * @param minBusinessId 业务ID
     * @param expertSelId   专家ID
     * @param businessType  业务类型
     * @param state         状态值
     * @param isConfirm     是否是确认值状态（true:是，否:更改的是是否参加会议状态）
     */
    @Override
    @Transactional
    public void updateExpertState(String minBusinessId, String businessType, String expertSelId, String state, boolean isConfirm) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_expert_selected ");
        if (isConfirm) {
            sqlBuilder.append("set " + ExpertSelected_.isConfrim.getName() + " =:state ");
        } else {
            sqlBuilder.append("set " + ExpertSelected_.isJoin.getName() + " =:state ");
        }
        sqlBuilder.setParam("state", state);
        sqlBuilder.bulidPropotyString("where", ExpertSelected_.id.getName(), expertSelId);
        expertReviewRepo.executeSql(sqlBuilder);
        //更改专家评审费
        if (Constant.BusinessType.SIGN.getValue().equals(businessType)) {
            workProgramRepo.initExpertCost(minBusinessId);
        }
    }


    /**
     * 获取指定专家，指定月份的评审费用
     */
    @Override
    public List<Map<String, Object>> getExpertReviewCost(String expertIds, String month) {
        List<Map<String, Object>> experReviewCosts = null;
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select * from V_EXPERT_PAY_HIS t where t.expertid in (" + expertIds + ") and t.paydate = '" + month + "'");
        NativeQuery nativeQuery = expertReviewRepo.getSession().createNativeQuery(hqlBuilder.getHqlString());
        experReviewCosts = nativeQuery.list();
        return experReviewCosts;
    }

    @Override
    @Transactional
    @Deprecated
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
                                        expertSelected.setIsLetterRw(expertSelectedDto.getIsLetterRw());
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

    /**
     * 保存单个评审方案的专家评审费
     *
     * @param expertReviewDto
     * @return
     */
    @Override
    public ResultMsg saveExpertReviewCost(ExpertReviewDto expertReviewDto) {
        //保存评审费用
        String experReviewId = expertReviewDto.getId();
        if (Validate.isString(experReviewId)) {
            ExpertReview expertReview = expertReviewRepo.findById(experReviewId);
            if(expertReview.getPayDate() != null){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "已经进行评审费发送，不能再次保存！");
            }
            //日期比较(跟系统的日期比较)，只有评审会前一天或者后一天才能保存(或者超级管理员)
            if (expertReview.getReviewDate() != null && !Constant.SUPER_USER.equals(SessionUtil.getLoginName())) {
                //String sysDateString = expertReviewRepo.getDataBaseTime("yyyy-mm-dd");
                //long diffDays = DateUtils.daysBetween(DateUtils.converToDate(sysDateString, "yyyy-MM-dd"), expertReview.getReviewDate());
                long diffDays = DateUtils.daysBetween(new Date(), expertReview.getReviewDate());
                if (diffDays > 1 || diffDays < -1) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "评分费只能在评审会时间的前一天，当天或者后一天才能计算！");
                }
            }

            if (expertReview != null) {
                //设置评审方案的评审费用、税费和合计
                expertReview.setReviewCost(expertReviewDto.getReviewCost());
                expertReview.setReviewTaxes(expertReviewDto.getReviewTaxes());
                expertReview.setTotalCost(expertReviewDto.getTotalCost());
                //设置标题
                expertReview.setReviewTitle(expertReviewDto.getReviewTitle());
                //设置评审费发放日期
                expertReview.setPayDate(expertReviewDto.getPayDate());
                //设置状态(已报评审费)
                expertReview.setState(Constant.EnumState.YES.getValue());
                //设置该评审方案所有专家的评审费用、税费和合计
                List<ExpertSelected> expertSelecteds = expertReview.getExpertSelectedList();
                List<ExpertSelectedDto> expertSelectedDtos = expertReviewDto.getExpertSelectedDtoList();
                if (Validate.isList(expertSelecteds) && Validate.isList(expertSelectedDtos)) {
                    expertSelecteds.forEach(expertSelected -> {
                        expertSelectedDtos.forEach(expertSelectedDto -> {
                            if (expertSelectedDto.getId().equals(expertSelected.getId())) {
                                expertSelected.setReviewCost(expertSelectedDto.getReviewCost());
                                expertSelected.setReviewTaxes(expertSelectedDto.getReviewTaxes());
                                expertSelected.setTotalCost(expertSelectedDto.getTotalCost());
                                expertSelected.setIsLetterRw(expertSelectedDto.getIsLetterRw());
                                return;
                            }
                        });
                    });
                }
                //设置专家评审费用结束s
                expertReviewRepo.save(expertReview);
                return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
            }
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该评审方案已被删除");
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该评审方案已被删除");
        }
    }

    /**
     * 查询专家评审费超期发放的信息
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<ExpertReviewDto> findOverTimeReview(ODataObj odataObj) {
        PageModelDto<ExpertReviewDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = expertReviewRepo.getExecutableCriteria();
        if (Validate.isList(odataObj.getFilter())) {
            Object value;
            for (ODataFilterItem item : odataObj.getFilter()) {
                value = item.getValue();
                if (null == value) {
                    continue;
                }
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(),value));
            }
        }
        //未完成评审费发放
        criteria.add(Restrictions.or(Restrictions.isNull(ExpertReview_.state.getName()),Restrictions.eq(ExpertReview_.state.getName(), Constant.EnumState.NO.getValue()),Restrictions.eq(ExpertReview_.state.getName(), "")));
        String newDate = DateUtils.converToString(new Date(),"yyyy-MM-dd");
        //超期
        criteria.add(Restrictions.sqlRestriction( " ( (to_date('"+newDate+"','yyyy-mm-dd') - "+criteria.getAlias()+"_."+ExpertReview_.reviewDate.getName()+") > 0 and '"+newDate+"' != TO_CHAR("+criteria.getAlias()+"_."+ExpertReview_.reviewDate.getName()+", 'yyyy-mm-dd') )"));
        if(odataObj.isCount()){
            Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
            criteria.setProjection(null);
            odataObj.setCount(totalResult);
        }

        // 处理分页
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<ExpertReview> expertReviewList = criteria.list();
        List<ExpertReviewDto> expertReviewDtoList = new ArrayList<>();
        if(Validate.isList(expertReviewList)){
            for(ExpertReview expertReview : expertReviewList){
                ExpertReviewDto expertReviewDto = new ExpertReviewDto();
                BeanCopierUtils.copyProperties(expertReview , expertReviewDto);
                expertReviewDtoList.add(expertReviewDto);
            }
        }
        /*List<ExpertReview> expertReviewList = expertReviewRepo.findReviewOverTime(businessType);
       List<ExpertReviewDto> expertReviewDtoList = new ArrayList<>();
        */
        pageModelDto.setValue(expertReviewDtoList);
        pageModelDto.setCount(odataObj.getCount());
        return pageModelDto;
    }

    /**
     * 查询业务的专家信息
     * @param businessId
     * @return
     */
    @Override
    public List<ExpertDto> refleshBusinessEP(String businessId) {
        List<ExpertDto> expertDtoList = new ArrayList<>();
        List<Expert> expertList = expertRepo.findByBusinessId(businessId);
        if(Validate.isList(expertList)){
            expertList.forEach( el ->{
                ExpertDto expertDto = new ExpertDto();
                el.setPhoto(null);
                BeanCopierUtils.copyProperties(el,expertDto);
                expertDtoList.add(expertDto);
            });
        }
        return expertDtoList;
    }

}