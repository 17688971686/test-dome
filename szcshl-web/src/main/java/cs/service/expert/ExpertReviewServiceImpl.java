package cs.service.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.*;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private ExpertNewInfoRepo expertNewInfoRepo;

    @Autowired
    private ExpertNewTypeRepo expertNewTypeRepo;


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
            if (Validate.isList(ep.getExpertType())) {
                List<ExpertTypeDto> expertDtoList = new ArrayList<ExpertTypeDto>(ep.getExpertType().size());
                ep.getExpertType().forEach(y -> {
                    ExpertTypeDto expertTypeDto = new ExpertTypeDto();
                    BeanCopierUtils.copyProperties(y, expertTypeDto);
                    expertDtoList.add(expertTypeDto);
                });
                expertDto.setExpertTypeDtoList(expertDtoList);
            }

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
            expertReview.setCreatedBy(SessionUtil.getDisplayName());
            expertReview.setModifiedBy(SessionUtil.getDisplayName());
            expertReview.setCreatedDate(now);
            expertReview.setModifiedDate(now);
            expertReview.setBusinessType(businessType);


            /*审批通过工作方案，再更新评审会日期
            if(expertReview.getReviewDate() == null){
                if (Constant.BusinessType.SIGN.getValue().equals(businessType)) {
                    WorkProgram wp = workProgramRepo.findById(WorkProgram_.id.getName(),minBusinessId);
                    if(Constant.MergeType.REVIEW_LEETER.getValue().equals(wp.getReviewType())){
                        //函评日期
                        expertReview.setReviewDate(wp.getLetterDate());
                    }else{
                        //获取评审会日期
                        expertReview.setReviewDate(roomBookingRepo.getMeetingDateByBusinessId(minBusinessId));
                    }
                }
            }*/
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
            Expert expert = expertRepo.findById(Expert_.expertID.getName(), expertIdArr.get(i));
            if(null != expert && null != expert.getExpertType()){
               if(expert.getExpertType().size() == 1){
                   expertSelected.setMaJorBig(expert.getExpertType().get(0).getMaJorBig());
                   expertSelected.setMaJorSmall(expert.getExpertType().get(0).getMaJorSmall());
                   expertSelected.setExpeRttype(expert.getExpertType().get(0).getExpertType());
               }
            }
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
            if (Validate.isList(ep.getExpertType())) {
                List<ExpertTypeDto> expertDtoList = new ArrayList<ExpertTypeDto>(ep.getExpertType().size());
                ep.getExpertType().forEach(y -> {
                    ExpertTypeDto expertTypeDto = new ExpertTypeDto();
                    BeanCopierUtils.copyProperties(y, expertTypeDto);
                    expertDtoList.add(expertTypeDto);
                });
                expertDto.setExpertTypeDtoList(expertDtoList);
            }
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

   /* @Override
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
    }*/

    /**
     * 保存单个评审方案的专家评审费
     *
     * @param expertReviewDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg saveExpertReviewCost(ExpertReviewDto expertReviewDto) {
        //保存评审费用
        String experReviewId = expertReviewDto.getId();
        if (Validate.isString(experReviewId)) {
            ExpertReview expertReview = expertReviewRepo.findById(experReviewId);
            //管理员可以维护
            if (expertReview.getPayDate() != null && !Constant.SUPER_USER.equals(SessionUtil.getLoginName())) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "已经进行评审费发放，不能再次发放！");
            }
            //日期比较(跟系统的日期比较)，只有评审会前一天或者后一天才能保存(或者超级管理员) 超级管理员发放，不做时间限制
            if (!Constant.SUPER_USER.equals(SessionUtil.getLoginName()) && expertReview.getReviewDate() != null) {
                long diffDays = DateUtils.daysBetween(new Date(), expertReview.getReviewDate());
                if (diffDays != 0) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "只能在评审当天计算专家应纳税额！");
                }
            }
            List<Map<String, Object>> resultObj = new ArrayList<>();
            if (expertReview != null) {
               /* //设置评审方案的评审费用、税费和合计
                expertReview.setReviewCost(expertReviewDto.getReviewCost());
                expertReview.setReviewTaxes(expertReviewDto.getReviewTaxes());
                expertReview.setTotalCost(expertReviewDto.getTotalCost());*/

                //设置该评审方案所有专家的评审费用、税费和合计
                List<ExpertSelected> expertSelecteds = expertReview.getExpertSelectedList();
                List<ExpertSelectedDto> expertSelectedDtos = expertReviewDto.getExpertSelectedDtoList();
                if (Validate.isList(expertSelecteds) && Validate.isList(expertSelectedDtos)) {
                    expertSelecteds.forEach(expertSelected -> {
                        expertSelectedDtos.forEach(expertSelectedDto -> {
                            if (expertSelectedDto.getId().equals(expertSelected.getId())) {
                                expertSelected.setReviewCost(expertSelectedDto.getReviewCost());
                               /*
                                这两项是计算出来的
                                expertSelected.setReviewTaxes(expertSelectedDto.getReviewTaxes());
                                expertSelected.setTotalCost(expertSelectedDto.getTotalCost());
                                */
                                expertSelected.setIsLetterRw(expertSelectedDto.getIsLetterRw());
                                return;
                            }
                        });
                    });
                    //计算评审费和税
                    resultObj = countReviewExpense(expertReview);
                    //设置标题
                    expertReview.setReviewTitle(expertReviewDto.getReviewTitle());
                    //设置评审费发放日期
                    expertReview.setPayDate(expertReviewDto.getPayDate());
                    //设置状态(已报评审费)
                    expertReview.setState(Constant.EnumState.YES.getValue());
                }
                //设置专家评审费用结束s
                expertReviewRepo.save(expertReview);
                return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", resultObj);
            }
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该评审方案已被删除");
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该评审方案已被删除");
        }
    }

    @Override
    public List<Map<String, Object>> countReviewExpense(ExpertReview expertReview) {
        List<Map<String, Object>> resultObj = new ArrayList<>();
        //筛选出已经确认的专家
        List<ExpertSelected> selectList = new ArrayList<>();
        String selectExpIds = "";
        List<ExpertSelected> expertSelectedList = expertReview.getExpertSelectedList();
        for (ExpertSelected expertSelected : expertSelectedList) {
            if (Constant.EnumState.YES.getValue().equals(expertSelected.getIsJoin()) && Constant.EnumState.YES.getValue().equals(expertSelected.getIsConfrim())) {
                if (Validate.isString(selectExpIds)) {
                    selectExpIds += ",";
                }
                selectExpIds += expertSelected.getExpert().getExpertID();
                selectList.add(expertSelected);
            }
        }
        String reviewDateString = DateUtils.date2String(expertReview.getReviewDate(), "yyyy-MM-dd");
        List<Object[]> countMapList = getExpertReviewCost(expertReview.getId(), selectExpIds, reviewDateString);
        //如果之间有缴税，则本次的缴税还要减去之前的缴税；否则按本次计算的缴税结果计算
        BigDecimal totalCount = BigDecimal.ZERO, totalTaxes = BigDecimal.ZERO;
        for (ExpertSelected slEP : selectList) {
            String epId = slEP.getExpert().getExpertID();
            Map<String, Object> epMap = new HashMap<>();
            boolean isHave = false;
            BigDecimal monthCount = null, montTaxes = null;
            for (Object[] countMap : countMapList) {
                if (countMap[0] != null && epId.equals(countMap[0].toString())) {
                    isHave = true;
                    //本月除了本次之外的收入
                    monthCount = countMap[1] == null ? BigDecimal.ZERO : new BigDecimal(countMap[1].toString());
                    //本月所有收入
                    monthCount = Arith.safeAdd(monthCount, slEP.getReviewCost());
                    //计算出本月应缴税额
                    montTaxes = Arith.countCost(monthCount);
                    //计算本次应缴税额
                    montTaxes = Arith.safeSubtract(montTaxes, countMap[2] == null ? BigDecimal.ZERO : new BigDecimal(countMap[2].toString()));
                }
            }
            if (!isHave) {
                monthCount = slEP.getReviewCost();
                montTaxes = Arith.countCost(monthCount);
            }
            slEP.setReviewTaxes(montTaxes);
            //此专家本次的花费，本次的评审费+本次的税费
            slEP.setTotalCost(Arith.safeAdd(slEP.getReviewCost(), montTaxes));

            totalCount = Arith.safeAdd(slEP.getReviewCost(), totalCount);
            totalTaxes = Arith.safeAdd(montTaxes, totalTaxes);
            //设置返回的数据
            epMap.put("EXPERTID", epId);
            epMap.put("MONTCOST", slEP.getReviewCost());
            epMap.put("MONTAXES", montTaxes);
            resultObj.add(epMap);
        }
        expertReview.setReviewCost(totalCount);
        expertReview.setReviewTaxes(totalTaxes);
        expertReview.setTotalCost(Arith.safeAdd(totalCount, totalTaxes));
        return resultObj;
    }

    /**
     * 获取指定专家，指定月份的评审费用
     * 排除本次评审费的费用，统计出本月所得金额
     *
     * @param expertIds  专家ID
     * @param reviewDate 评审会日期
     * @return
     */
    @Override
    public List<Object[]> getExpertReviewCost(String reviewId, String expertIds, String reviewDate) {
        String month = reviewDate.substring(0, reviewDate.lastIndexOf("-"));
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT expertid, SUM (REVIEWCOST) excost, SUM (REVIEWTAXES) extaxes ");
        sqlBuilder.append(" FROM (SELECT es.id esId, es.expertid,es.reviewcost,es.reviewTaxes FROM CS_EXPERT_SELECTED es, CS_EXPERT_REVIEW er ");
        sqlBuilder.append(" WHERE es.isjoin = :isjoin AND es.ISCONFRIM = :isconfrim AND es.Expertreviewid = er.id AND ER.ID !=:reviewId ");
        sqlBuilder.setParam("isjoin", Constant.EnumState.YES.getValue());
        sqlBuilder.setParam("isconfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.setParam("reviewId", reviewId);
        sqlBuilder.append(" AND TO_CHAR (er.REVIEWDATE, 'yyyy-mm') =:reviewmonth ").setParam("reviewmonth", month);
        sqlBuilder.bulidPropotyString("AND", "ES.EXPERTID", expertIds);
        sqlBuilder.append(" )GROUP BY expertid ");
        List<Object[]> resultList = expertReviewRepo.getObjectArray(sqlBuilder);
        return resultList;
    }

    /*@Override
    public List<Object[]> countExpertReviewCost(String expertReviewId, String month) {
        return expertReviewRepo.countExpertReviewCost(expertReviewId, month);
    }*/

    /**
     * 查询专家评审费超期发放的信息
     *
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
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(), value));
            }
        }
        //没有业务ID和评审日期的，过滤掉
        criteria.add(Restrictions.isNotNull(ExpertReview_.businessId.getName()));
        criteria.add(Restrictions.isNotNull(ExpertReview_.reviewDate.getName()));
        //未完成评审费发放
        criteria.add(Restrictions.or(Restrictions.isNull(ExpertReview_.state.getName()), Restrictions.eq(ExpertReview_.state.getName(), Constant.EnumState.NO.getValue()), Restrictions.eq(ExpertReview_.state.getName(), "")));
//        String newDate = DateUtils.converToString(new Date(),"yyyy-MM-dd");
        //超期
//        criteria.add(Restrictions.sqlRestriction( " ( (to_date('"+newDate+"','yyyy-mm-dd') - "+criteria.getAlias()+"_."+ExpertReview_.reviewDate.getName()+") > 0 and '"+newDate+"' != TO_CHAR("+criteria.getAlias()+"_."+ExpertReview_.reviewDate.getName()+", 'yyyy-mm-dd') )"));

        criteria.addOrder(Order.desc(ExpertReview_.reviewDate.getName()));
        if (odataObj.isCount()) {
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
        if (Validate.isList(expertReviewList)) {
            for (ExpertReview expertReview : expertReviewList) {
                ExpertReviewDto expertReviewDto = new ExpertReviewDto();
                BeanCopierUtils.copyProperties(expertReview, expertReviewDto);
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
     *
     * @param businessId
     * @return
     */
    @Override
    public List<ExpertDto> refleshBusinessEP(String businessId) {
        List<ExpertDto> expertDtoList = new ArrayList<>();
        List<Expert> expertList = expertRepo.findByBusinessId(businessId);
        if (Validate.isList(expertList)) {
            expertList.forEach(el -> {
                ExpertDto expertDto = new ExpertDto();
                el.setPhoto(null);
                BeanCopierUtils.copyProperties(el, expertDto);
                expertDtoList.add(expertDto);
            });
        }
        return expertDtoList;
    }

    /**
     * 后去新专家信息
     *
     * @param businessId
     * @return
     */
    @Override
    public List<ExpertNewInfoDto> getExpertInfo(String businessId) {
        List<ExpertNewInfoDto> expertDtoList = new ArrayList<>();
        List<ExpertNewInfo> expertList = expertNewInfoRepo.findByBusinessId(businessId);
        if (Validate.isList(expertList)) {
            expertList.forEach(el -> {
                ExpertNewInfoDto expertDto = new ExpertNewInfoDto();
                BeanCopierUtils.copyProperties(el, expertDto);
                expertDtoList.add(expertDto);
            });
        }
        return expertDtoList;
    }


    @Override
    @Transactional
    public List<ExpertReview> queryUndealReview() {
        return expertReviewRepo.queryUndealReview();
    }

    @Override
    @Transactional
    public void save(ExpertReview expertReview) {
        expertReviewRepo.save(expertReview);
    }

    @Override
    public void saveSplit(ExpertSelectedDto expertSelectedDto) {
        expertReviewRepo.saveSplit(expertSelectedDto);
    }

    @Override
    public ResultMsg saveExpertNewInfo(ExpertReviewNewInfoDto[] expertReviewNewInfoDtos) {
        try {
            if (expertReviewNewInfoDtos.length > 0) {
                expertNewTypeRepo.deleteById("businessId", expertReviewNewInfoDtos[0].getBusinessId());
                expertNewInfoRepo.deleteById("businessId", expertReviewNewInfoDtos[0].getBusinessId());
            }
            for (int i = 0; i < expertReviewNewInfoDtos.length; i++) {
                ExpertNewInfoDto expertNewInfoDto = expertReviewNewInfoDtos[i].getExpertDto();
                expertNewInfoDto.setBusinessId(expertReviewNewInfoDtos[i].getBusinessId());
                //保存最新的专家信息
                ExpertNewInfo expertNewInfo = new ExpertNewInfo();
                BeanCopierUtils.copyPropertiesIgnoreNull(expertNewInfoDto, expertNewInfo);
                expertNewInfo.setExpertNewInfoId(UUID.randomUUID().toString());
                expertNewInfo.setCreatedDate(new Date());
                expertNewInfo.setCreatedBy(SessionUtil.getDisplayName());
                expertNewInfo.setModifiedDate(new Date());
                expertNewInfo.setModifiedBy(SessionUtil.getDisplayName());
                expertNewInfo.setMaJorBig(expertReviewNewInfoDtos[i].getMaJorBig());
                expertNewInfo.setMaJorSmall(expertReviewNewInfoDtos[i].getMaJorSmall());
                expertNewInfo.setExpeRttype(expertReviewNewInfoDtos[i].getExpeRttype());
                expertNewInfo.setIsJoin(expertReviewNewInfoDtos[i].getIsJoin());
                expertNewInfo.setIsLetterRw(expertReviewNewInfoDtos[i].getIsLetterRw());
                expertNewInfoRepo.save(expertNewInfo);//保存新的专家信息

                ExpertNewType expertNewType = new ExpertNewType();
                expertNewType.setExpertNewInfo(expertNewInfo);//专家信息的关联
                expertNewType.setId(UUID.randomUUID().toString());
                expertNewType.setMaJorBig(expertReviewNewInfoDtos[i].getMaJorBig());
                expertNewType.setMaJorSmall(expertReviewNewInfoDtos[i].getMaJorSmall());
                expertNewType.setExpertType(expertReviewNewInfoDtos[i].getExpeRttype());
                expertNewType.setBusinessId(expertNewInfoDto.getBusinessId());
                expertNewType.setModifiedBy(SessionUtil.getDisplayName());
                expertNewType.setModifiedDate(new Date());
                expertNewType.setCreatedBy(SessionUtil.getDisplayName());
                expertNewType.setCreatedDate(new Date());
                expertNewTypeRepo.save(expertNewType);//保存专家类型

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存失败！");
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");

    }

    /**
     * 删除新专家信息
     * @param minBusinessId
     */
    @Override
    public void deleteExpertNewInfo(String minBusinessId) {
        if(Validate.isString(minBusinessId)){
            expertNewTypeRepo.deleteById("businessId",minBusinessId);
            expertNewInfoRepo.deleteById("businessId",minBusinessId);
        }
    }

}