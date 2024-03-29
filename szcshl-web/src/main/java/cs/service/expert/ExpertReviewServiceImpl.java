package cs.service.expert;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.*;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import static cs.common.constants.Constant.EXPERT_REVIEW_COST;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * @author ldm
 * Date: 2017-5-17 14:02:25
 * Description: 专家评审 业务操作实现类
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
    private ExpertNewInfoRepo expertNewInfoRepo;
    @Autowired
    private ExpertNewTypeRepo expertNewTypeRepo;
    @Autowired
    private SignRepo signRepo;


    @Override
    public PageModelDto<ExpertReviewDto> get(ODataObj odataObj) {
        PageModelDto<ExpertReviewDto> pageModelDto = new PageModelDto<ExpertReviewDto>();
        List<ExpertReviewDto> resultDtoList = new ArrayList<>();
        List<ExpertReview> resultList = expertReviewRepo.findByOdata(odataObj);
        if (Validate.isList(resultList)) {
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
            if (Validate.isList(expertReview.getExpertSelConditionList())) {
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
            }else{
                expertReviewDto.setState(null);
                expertReviewDto.setSelectIndex(null);
                expertReviewDto.setExtractInfo(null);
                expertReviewDto.setFinishExtract(null);
            }
            List<ExpertSelected> expertSelectedList = expertReview.getExpertSelectedList();
            //3、抽取专家
            if (Validate.isList(expertSelectedList)) {
                List<ExpertSelectedDto> selDtoList = new ArrayList<>();
                Collections.sort(expertSelectedList, new Comparator<ExpertSelected>() {
                    @Override
                    public int compare(ExpertSelected e1, ExpertSelected e2) {
                        if(e2.getExpertSeq() == null){
                            return -1;
                        }else if(e1.getExpertSeq() == null){
                            return 1;
                        }else if(e2.getExpertSeq()>e1.getExpertSeq()){
                            return 1;
                        }
                        return 0;
                    }
                });
                for (ExpertSelected epSelted : expertSelectedList) {
                    if (isFilter) {
                        if (minBusinessId.equals(epSelted.getBusinessId())) {
                            //设置抽取专家Dto
                            selDtoList.add(initSelExpert(epSelted));
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

    /**
     * 验证是否有专家评审费发放
     *
     * @param signid
     */
    @Override
    public boolean checkReviewCost(String signid) {
        //如果有专家评审费，则要先办理专家评审费
        if (expertReviewRepo.isHaveEPReviewCost(signid)) {
            ExpertReview expertReview = expertReviewRepo.findById(ExpertReview_.businessId.getName(), signid);
            if (expertReview.getPayDate() == null || expertReview.getTotalCost() == null) {
                return false;
            }
        }
        return true;
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
        boolean isSuper = SUPER_ACCOUNT.equals(SessionUtil.getLoginName()),isSelf = Constant.EnumExpertSelectType.SELF.getValue().equals(selectType);
        if (!Validate.isString(reviewId)) {
            Date now = new Date();
            expertReview = new ExpertReview();
            expertReview.setBusinessId(businessId);
            expertReview.setCreatedBy(SessionUtil.getDisplayName());
            expertReview.setModifiedBy(SessionUtil.getDisplayName());
            expertReview.setCreatedDate(now);
            expertReview.setModifiedDate(now);
            expertReview.setBusinessType(businessType);
            //设置未完成整体工作方案抽取
            expertReview.setFinishExtract(0);
            //评审费发放标题
            expertReviewRepo.initReviewTitle(expertReview, businessId, businessType);
            expertReviewRepo.save(expertReview);
        } else {
            expertReview = expertReviewRepo.findById(ExpertReview_.id.getName(), reviewId);
            if(!isSuper && null != expertReview.getPayDate() && null != expertReview.getReviewDate() && (new Date()).after(expertReview.getReviewDate())){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"已发送专家评审费，不能对方案再修改！");
            }
        }
        Sign sign = signRepo.findById(Sign_.signid.getName() , businessId);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        boolean isMoreExpert = Constant.EnumState.YES.getValue().equals(sign.getIsMoreExpert());
        List<ExpertSelectedDto> resultList = new ArrayList<>();
        //保存抽取专家
        List<String> expertIdArr = StringUtil.getSplit(expertIds, ",");
        int totalLength = expertIdArr.size();

        String resultString = Constant.EnumState.NO.getValue();
        if(isSuper || isMoreExpert){
            resultString = Constant.EnumState.YES.getValue();
        }
        resultMap.put("moreExpert",resultString);
        //如果是专家自选，系统管理员可以添加多个自选专家，其他人员则要删除之前选择的专家信息 ,补充：如果不是可以自选多个专家
        if(!isSuper && isSelf && !isMoreExpert){
            if(totalLength > 1){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), expertReview.getId(), "操作失败，只能选一个自选专家！");
            }
        }
        for (int i = 0; i < totalLength; i++) {
            if(!isSuper && isSelf && !Constant.EnumState.YES.getValue().equals(sign.getIsMoreExpert())){
                if (Validate.isList(expertReview.getExpertSelectedList())) {
                    //不是系统管理员，则要删除之前添加的自选专家
                    for (ExpertSelected epSelected : expertReview.getExpertSelectedList()) {
                        boolean isSelfType = Constant.EnumExpertSelectType.SELF.getValue().equals(epSelected.getSelectType());
                        boolean isSuperCreate = SUPER_ACCOUNT.equals(epSelected.getCreateBy());
                        if (isSelfType && !isSuperCreate && !isMoreExpert) {
                            expertSelectedRepo.deleteById(ExpertSelected_.id.getName(), epSelected.getId());
                        }
                    }
                }
            }
            ExpertSelected expertSelected = new ExpertSelected();
            //每个专家默认评审费1000元
            expertSelected.setReviewCost(new BigDecimal(EXPERT_REVIEW_COST));
            expertSelected.setBusinessId(minBusinessId);
            expertSelected.setIsJoin(Constant.EnumState.YES.getValue());
            expertSelected.setIsConfrim(Constant.EnumState.YES.getValue());
            expertSelected.setSelectType(selectType);
            expertSelected.setCreateBy(SessionUtil.getLoginName());
            //获取专家信息
            Expert expert = expertRepo.findById(Expert_.expertID.getName(), expertIdArr.get(i));
            //如果是自选或者境外专家，默认已经确认
            if(Constant.EnumExpertSelectType.SELF.getValue().equals(expertSelected.getSelectType())){
                expertSelected.setRemark(Constant.ExpertSelectType.自选.name());
                expertSelected.setIsConfrim(Constant.EnumState.YES.getValue());
            }else if( Constant.EnumExpertSelectType.OUTSIDE.getValue().equals(expertSelected.getSelectType())){
                expertSelected.setIsConfrim(Constant.EnumState.YES.getValue());
                //设定备注信息，优先 （境外专家<-市外专家<-新专家）
                if("2".equals(expert.getExpertField())){
                    expertSelected.setRemark(Constant.ExpertSelectType.境外专家.name());
                }else if("1".equals(expert.getExpertField())){
                    expertSelected.setRemark(Constant.ExpertSelectType.市外专家.name());
                }else{
                    expertSelected.setRemark(Constant.ExpertSelectType.新专家.name());
                }
            }
            //保存专家映射
            expertSelected.setExpert(expert);

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
        resultMap.put("selectedDtoList",resultList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), expertReview.getId(), "操作成功！", resultMap);
    }


    /**
     * 更改抽取专家状态(直接用sql更新)
     * @param reviewId 专家抽取方案ID
     * @param minBusinessId 业务ID
     * @param expertSelId   专家ID
     * @param businessType  业务类型
     * @param state         状态值
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg updateJoinState(String reviewId,String minBusinessId, String businessType, String expertSelId, String state) {
        if(!Validate.isString(reviewId)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败！请联系管理员处理！");
        }
        if(!Validate.isString(expertSelId)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"请选择要更改的专家！");
        }
        //是否更新工作方案中的专家费用
        ExpertReview expertReview = expertReviewRepo.findById(reviewId);
        if(!Validate.isObject(expertReview)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败！该评审方案已被删除！");
        }
        boolean isSuperUser = SUPER_ACCOUNT.equals(SessionUtil.getLoginName());
        boolean isPay = false;
        if(expertReview.getPayDate() != null){
            //如果当前日期大于评审会日期，则不能进行修改
            if((new Date()).after(expertReview.getReviewDate())){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"已开评审会，不能对专家信息调整！");
            }else if(!isSuperUser){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"评审费已发放，不能对专家信息调整！请联系系统管理员进行调整！");
            }
            isPay = true;
        }
        //更改抽取专家状态
        List<ExpertSelected> selectedList = expertReview.getExpertSelectedList();
        List<String> expIds = StringUtil.getSplit(expertSelId,",");
        for(ExpertSelected es : selectedList){
            for(String epId : expIds){
                if(epId.equals(es.getId())){
                    es.setIsJoin(state);
                }
            }
        }

        //如果已经发送评审费，判断是否要重新计算
        if(isPay){
            //系统管理员修改，要重新计算评审费，
            //如果是从确认到未确认，则重新计算该方案总数
            //如果是从未确认到确认，则重置总数，让系统管理员重新录入
            if( Constant.EnumState.STOP.getValue().equals(state)){
               List<Map<String,Object>> resultObj = countTotalExpense(expertReview);
                resultObj = null;
            }else{
                expertReview.setReviewCost(null);
                expertReview.setReviewTaxes(null);
                expertReview.setTotalCost(null);
            }
            //设置专家评审费用结束s
            expertReviewRepo.save(expertReview);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
    }
    /**
     * 更改专家是否确认状态
     * @param minBusinessId
     * @param businessType
     * @param expertSelId
     * @param state
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfirmState(String reviewId,String minBusinessId, String businessType, String expertSelId, String state) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_expert_selected ");
        sqlBuilder.append("set " + ExpertSelected_.isConfrim.getName() + " =:state ");
        sqlBuilder.setParam("state", state);
        sqlBuilder.bulidPropotyString("where", ExpertSelected_.id.getName(), expertSelId);
        expertReviewRepo.executeSql(sqlBuilder);

        //修改评审方案的抽取专家信息
        HqlBuilder sqlBuilder2 = HqlBuilder.create();
        sqlBuilder2.append(" update CS_EXPERT_REVIEW ");
        sqlBuilder2.append("set " + ExpertReview_.extractInfo.getName() + " = null ,  "+ ExpertReview_.selectIndex.getName()+" = null ");
        sqlBuilder2.append(" where "+ ExpertReview_.id.getName()+" = :reviewId ").setParam("reviewId",reviewId);
        expertReviewRepo.executeSql(sqlBuilder2);
    }

    /**
     * 更新新专家状态
     * @param minBusinessId
     * @param expertSelId
     * @param state
     * @param isConfirm
     */
    public void updateNewExpertState(String minBusinessId, String expertSelId, String state, boolean isConfirm) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_expert_new_info ");
        if (isConfirm) {
            sqlBuilder.append("set " + ExpertSelected_.isConfrim.getName() + " =:state ");
        } else {
            sqlBuilder.append("set " + ExpertSelected_.isJoin.getName() + " =:state ");
        }
        sqlBuilder.setParam("state", state);
        sqlBuilder.bulidPropotyString("where", ExpertNewInfo_.businessId.getName(), minBusinessId);
        sqlBuilder.bulidPropotyString("and", ExpertNewInfo_.expertSelectedId.getName(), expertSelId);
        expertReviewRepo.executeSql(sqlBuilder);

    }

    /**
     * 保存单个评审方案的专家评审费
     * @param expertReviewDto
     * @param isCountTaxes 是否计税
     * @return
     */
    @Override
    @Transactional
    public ResultMsg saveExpertReviewCost(ExpertReviewDto expertReviewDto,boolean isCountTaxes) {
        //保存评审费用
        String experReviewId = expertReviewDto.getId();
        boolean isSuperUser = SUPER_ACCOUNT.equals(SessionUtil.getLoginName());
        if (Validate.isString(experReviewId)) {
            ExpertReview expertReview = expertReviewRepo.findById(experReviewId);
            if(!Validate.isObject(expertReview)){
                return ResultMsg.error("操作失败，该评审方案已被删除");
            }
            //管理员可以维护
            if (expertReview.getPayDate() != null && !isSuperUser) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "已经进行评审费发放，不能再次发放！");
            }
            //日期比较(跟系统的日期比较)，只有评审会前一天或者后一天才能保存(或者超级管理员) 超级管理员发放，不做时间限制
            if (isCountTaxes && !isSuperUser && expertReview.getReviewDate() != null) {
                long diffDays = DateUtils.daysBetween(new Date(), expertReview.getReviewDate());
                if (diffDays != 0) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "只能在评审当天计算专家应纳税额！");
                }
            }
            //如果是超级管理员，还可以修改评审日期
            if(isSuperUser){
                expertReview.setReviewDate(expertReviewDto.getReviewDate());
            }
            List<Map<String, Object>> resultObj = new ArrayList<>();
            if (expertReview != null) {
                //设置该评审方案所有专家的评审费用、税费和合计
                List<ExpertSelected> expertSelecteds = expertReview.getExpertSelectedList();
                List<ExpertSelectedDto> expertSelectedDtos = expertReviewDto.getExpertSelectedDtoList();
                if (Validate.isList(expertSelecteds) && Validate.isList(expertSelectedDtos)) {
                    expertSelecteds.forEach(expertSelected -> {
                        expertSelectedDtos.forEach(expertSelectedDto -> {
                            if (expertSelectedDto.getId().equals(expertSelected.getId())) {
                                expertSelected.setReviewCost(expertSelectedDto.getReviewCost());
                                expertSelected.setIsLetterRw(expertSelectedDto.getIsLetterRw());
                                if(!isCountTaxes){
                                    expertSelected.setReviewTaxes(expertSelectedDto.getReviewTaxes());
                                }
                                return;
                            }
                        });
                    });
                    if(isCountTaxes){
                        //计算评审费和税
                        resultObj = countReviewExpense(expertReview);
                        //设置评审费发放日期
                        expertReview.setPayDate(expertReviewDto.getPayDate());
                        //设置状态(已报评审费)
                        expertReview.setState(Constant.EnumState.YES.getValue());
                    }else{
                        resultObj = countTotalExpense(expertReview);
                    }
                    //设置标题
                    expertReview.setReviewTitle(expertReviewDto.getReviewTitle());
                }
                //设置专家评审费用结束s
                expertReviewRepo.save(expertReview);
                return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", resultObj);
            }
            return ResultMsg.error("操作失败，该评审方案已被删除");
        } else {
            return ResultMsg.error("操作失败，该评审方案已被删除");
        }
    }

    /**
     * 统计总数此次评审费总数，不包含计税
     * @param expertReview
     * @return
     */
    @Override
    public List<Map<String,Object>> countTotalExpense(ExpertReview expertReview) {
        List<Map<String, Object>> resultObj = new ArrayList<>();
        List<ExpertSelected> expertSelectedList = expertReview.getExpertSelectedList();
        BigDecimal totalCount = BigDecimal.ZERO, totalTaxes = BigDecimal.ZERO;
        for (ExpertSelected slEP : expertSelectedList) {
            //过滤掉参加的专家
            if(Constant.EnumState.YES.getValue().equals(slEP.getIsJoin()) && Constant.EnumState.YES.getValue().equals(slEP.getIsConfrim())){
                Map<String, Object> epMap = new HashMap<>();
                BigDecimal reviewCost = slEP.getReviewCost()== null?BigDecimal.ZERO:slEP.getReviewCost();
                BigDecimal reviewTaxes = slEP.getReviewTaxes()== null?BigDecimal.ZERO:slEP.getReviewTaxes();
                slEP.setTotalCost(Arith.safeAdd(reviewCost, reviewTaxes));
                //设置返回的数据
                epMap.put("EXPERTID", slEP.getExpert().getExpertID());
                epMap.put("MONTCOST", reviewCost);
                epMap.put("MONTAXES", reviewTaxes);

                totalCount = Arith.safeAdd(reviewCost, totalCount);
                totalTaxes = Arith.safeAdd(reviewTaxes, totalTaxes);
                resultObj.add(epMap);
            }
        }
        expertReview.setReviewCost(totalCount);
        expertReview.setReviewTaxes(totalTaxes);
        expertReview.setTotalCost(Arith.safeAdd(totalCount, totalTaxes));
        return resultObj;
    }

    /**
     * 计算此次专家评审费用
     * @param expertReview
     * @return
     */
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
        String reviewDateString = DateUtils.date2String(expertReview.getReviewDate(), DateUtils.DATE_PATTERN);
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
        sqlBuilder.append(" AND ER.PAYDATE is not null ");
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
     * 获取新专家信息
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
    public ResultMsg saveExpertNewInfo(ExpertSelectedDto[] expertSelectedDtos) {
        try {
            List<ExpertSelected> expertSelectedList = new ArrayList<>();
            for (int i = 0; i < expertSelectedDtos.length; i++) {
                ExpertSelectedDto expertSelectedDto = (ExpertSelectedDto)expertSelectedDtos[i];
                ExpertSelected expertSelected = expertSelectedRepo.findById(expertSelectedDto.getId());
                //BeanCopierUtils.copyProperties(expertSelectedDto, expertSelected);
                expertSelected.setMaJorBig(expertSelectedDto.getMaJorBig());
                expertSelected.setMaJorSmall(expertSelectedDto.getMaJorSmall());
                expertSelected.setExpeRttype(expertSelectedDto.getExpeRttype());
                expertSelected.setIsLetterRw(expertSelectedDto.getIsLetterRw());
                expertSelected.setExpertSeq(i+1);
                expertSelectedList.add(expertSelected);
            }
            expertSelectedRepo.bathUpdate(expertSelectedList);
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