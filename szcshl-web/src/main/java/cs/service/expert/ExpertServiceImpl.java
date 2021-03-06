package cs.service.expert;

import cs.common.HqlBuilder;
import cs.common.RandomGUID;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.Constant.EnumExpertState;
import cs.common.constants.ProjectConstant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.*;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.service.sys.UserServiceImpl;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static cs.common.constants.Constant.EXPERT_REVIEW_COST;
import static cs.common.constants.SysConstants.SEPARATE_COMMA;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

@Service
public class ExpertServiceImpl implements ExpertService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private ExpertSelConditionRepo expertSelConditionRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;

    @Override
    public PageModelDto<ExpertDto> get(ODataObj odataObj) {
        PageModelDto<ExpertDto> pageModelDto = new PageModelDto<>();
        List<ExpertDto> listExpertDto = new ArrayList<>();
        List<Expert> expertList = expertRepo.get(odataObj);
        for (Expert item : expertList) {
            //把图片设置为空
            item.setPhoto(null);
            ExpertDto expertDto = new ExpertDto();
            BeanCopierUtils.copyProperties(item, expertDto);
            //添加专家类别
            if (Validate.isList(item.getExpertType())) {
                List<ExpertTypeDto> expertTypeDtoList = new ArrayList<>();
                item.getExpertType().forEach(x -> {
                    ExpertTypeDto expertTypeDto = new ExpertTypeDto();
                    BeanCopierUtils.copyProperties(x, expertTypeDto);
                    expertTypeDtoList.add(expertTypeDto);
                });
                expertDto.setExpertTypeDtoList(expertTypeDtoList);
            }
            listExpertDto.add(expertDto);
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(listExpertDto);
        return pageModelDto;
    }

    /**
     * 新专家、市外、境外专家
     *
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<ExpertDto> getExpertField(ODataObj odataObj) {
        PageModelDto<ExpertDto> pageModelDto = new PageModelDto<ExpertDto>();
        String state1 = "", state2 = "", expertField1 = "", expertField2 = "", maJorBigParam = "", maJorSamllParam = "", expertTypeParam = "";
        //Criteria 查询
        Criteria criteria = expertRepo.getExecutableCriteria();
        if (Validate.isList(odataObj.getFilter())) {
            Object value;
            for (ODataFilterItem item : odataObj.getFilter()) {
                value = item.getValue();
                if (null == value) {
                    continue;
                }
                if ("state1".equals(item.getField())) {
                    state1 = item.getValue().toString();
                    continue;
                }
                if ("state2".equals(item.getField())) {
                    state2 = item.getValue().toString();
                    continue;
                }
                if ("expertField1".equals(item.getField())) {
                    expertField1 = item.getValue().toString();
                    continue;
                }
                if ("expertField2".equals(item.getField())) {
                    expertField2 = item.getValue().toString();
                    continue;
                }
                if ("maJorBigParam".equals(item.getField())) {
                    maJorBigParam = item.getValue().toString();
                    continue;
                }
                if ("maJorSamllParam".equals(item.getField())) {
                    maJorSamllParam = item.getValue().toString();
                    continue;
                }
                if ("expertTypeParam".equals(item.getField())) {
                    expertTypeParam = item.getValue().toString();
                    continue;
                }
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(), value));
            }

            //关联专家大类、小类和专业类型查询
            if (Validate.isString(maJorBigParam) || Validate.isString(maJorSamllParam) || Validate.isString(expertTypeParam)) {
                StringBuffer sqlSB = new StringBuffer();
                sqlSB.append(" (select count(ept.ID) from CS_EXPERT_TYPE ept where EPT.EXPERTID = " + criteria.getAlias() + "_.EXPERTID ");
                //突出专业，大类
                if (Validate.isString(maJorBigParam)) {
                    sqlSB.append(" and ept.maJorBig = '" + maJorBigParam + "' ");
                }
                //突出专业，小类
                if (Validate.isString(maJorSamllParam)) {
                    sqlSB.append(" and ept.maJorSmall = '" + maJorSamllParam + "' ");
                }
                //专家类型
                if (Validate.isString(expertTypeParam)) {
                    sqlSB.append(" and ept.expertType = '" + expertTypeParam + "' ");
                }
                sqlSB.append(" ) > 0 ");
                criteria.add(Restrictions.sqlRestriction(sqlSB.toString()));
            }

        }
        if (Validate.isString(state1) && Validate.isString(state2) && Validate.isString(expertField1)
                && Validate.isString(expertField2)) {
            StringBuffer sqlSB = new StringBuffer();
            sqlSB.append(" ( state = " + state1 + "  or ( ( expertField = " + expertField1 + " or EXPERTFIELD = " + expertField2 + " ) and state <> " + state2 + " ) )");
            criteria.add(Restrictions.sqlRestriction(sqlSB.toString()));
        }
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<Expert> resultList = criteria.list();
        List<ExpertDto> resultDtoList = new ArrayList<ExpertDto>(resultList.size());

        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                ExpertDto modelDto = new ExpertDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                if (Validate.isList(x.getExpertType())) {
                    List<ExpertTypeDto> expertDtoList = new ArrayList<ExpertTypeDto>(x.getExpertType().size());
                    x.getExpertType().forEach(y -> {
                        ExpertTypeDto expertTypeDto = new ExpertTypeDto();
                        BeanCopierUtils.copyProperties(y, expertTypeDto);
                        expertDtoList.add(expertTypeDto);
                    });
                    modelDto.setExpertTypeDtoList(expertDtoList);
                }
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     * 保存专家信息
     *
     * @param expertDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg saveExpert(ExpertDto expertDto) {
        if(!Validate.isString(expertDto.getIdCard())){
            return ResultMsg.error("身份证号还没填写！");
        }
        //重复专家判断
        boolean isHave = expertRepo.checkIsHaveIdCard(expertDto.getIdCard(), expertDto.getExpertID());
        if(isHave){
            return ResultMsg.error(String.format("身份证号为%s 的专家已存在,请重新输入", expertDto.getIdCard()));
        }
        String updateUserNo = Validate.isString(SessionUtil.getUserInfo().getUserNo())?SessionUtil.getUserInfo().getUserNo():SUPER_ACCOUNT;
        Expert expert = null;
        Date now = new Date();
        boolean isCreate = true;
        if (Validate.isString(expertDto.getExpertID())) {
            expert = expertRepo.findById(Expert_.expertID.getName(), expertDto.getExpertID());
            if(Validate.isObject(expert) && Validate.isString(expert.getExpertID())){
                isCreate = false;
            }
        } else {
            expert = new Expert();
        }
        BeanCopierUtils.copyProperties(expertDto, expert);
        //如果是新增，设置默认属性
        if(isCreate){
            expert.setExpertID((new RandomGUID()).valueAfterMD5);
            //专家编码，系统自动生成
            expert.setExpertNo(String.format("%06d", Integer.valueOf(findMaxNumber()) + 1));
            expert.setInputPerson(SessionUtil.getDisplayName());
            expert.setApplyDate(now);
            expert.setCreatedDate(now);
            //统一用用户编号，跟旧系统一致
            expert.setCreatedBy(updateUserNo);
        }
        expert.setModifiedDate(now);
        expert.setModifiedBy(updateUserNo);

        if(!Validate.isString(expert.getState())){
            expert.setState(EnumExpertState.AUDITTING.getValue());
        }
        if(!Validate.isString(expert.getUnable())){
            //是否作废（1为作废，0 为正常）,默认不作废
            expert.setUnable(Constant.EnumState.NO.getValue());
        }
        expertRepo.save(expert);
        //设置返回值
        BeanCopierUtils.copyProperties(expert, expertDto);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", expertDto);
    }

    /**
     * 删除操作，实为更新专家状态为删除状态
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg deleteExpert(String id) {
        try {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append("update " + Expert.class.getSimpleName() + " set " + Expert_.state.getName() + "=:state ");
            hqlBuilder.setParam("state", EnumExpertState.REMOVE.getValue());
            hqlBuilder.bulidPropotyString("where", Expert_.expertID.getName(), id);
            expertRepo.executeHql(hqlBuilder);

            return ResultMsg.ok( "操作成功！");
        } catch (Exception e) {
            logger.info("逻辑删除专家异常：" + e.getMessage());
            return ResultMsg.error( "删除失败！");
        }
    }

    /**
     * 物理删除专家
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg deleteExpertData(String id) {
        try {
            expertRepo.deleteById(Expert_.expertID.getName(), id);
            return ResultMsg.ok( "操作成功！");
        } catch (Exception e) {
            logger.info("物理删除专家异常：" + e.getMessage());
            return ResultMsg.error( "删除失败！");
        }
    }

    /**
     * 专家审核
     *
     * @param ids
     * @param state
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAudit(String ids, String state) {
        if (Validate.isString(ids)) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" update " + Expert.class.getSimpleName() + " set " + Expert_.state.getName() + " = :state ");
            hqlBuilder.setParam("state", state);
            hqlBuilder.bulidPropotyString("where", Expert_.expertID.getName(), ids);

            expertRepo.executeHql(hqlBuilder);
        }
    }

    @Override
    public ExpertDto findById(String id) {
        Expert expert = expertRepo.findById(id);
        ExpertDto expertDto = new ExpertDto();
        if (expert != null) {
            //把图片设为null,不用拷贝图片
            expert.setPhoto(null);
            BeanCopierUtils.copyProperties(expert, expertDto);
            //工作经验
            if (expert.getWork() != null && expert.getWork().size() > 0) {
                List<WorkExpeDto> workDtoList = new ArrayList<>(expert.getWork().size());
                expert.getWork().forEach(ew -> {
                    WorkExpeDto workDto = new WorkExpeDto();
                    BeanCopierUtils.copyProperties(ew, workDto);
                    workDtoList.add(workDto);
                });
                expertDto.setWorkDtoList(workDtoList);
            }
            //项目经验
            if (expert.getProject() != null && expert.getProject().size() > 0) {
                List<ProjectExpeDto> projectDtoList = new ArrayList<>(expert.getProject().size());
                (expert.getProject()).forEach(ep -> {
                    ProjectExpeDto projectDto = new ProjectExpeDto();
                    BeanCopierUtils.copyProperties(ep, projectDto);
                    projectDtoList.add(projectDto);
                });
                expertDto.setProjectDtoList(projectDtoList);
            }

            //专家类型
            if (expert.getExpertType() != null && expert.getExpertType().size() > 0) {
                List<ExpertTypeDto> expertTypeDtoList = new ArrayList<>();
                for (ExpertType expertType : expert.getExpertType()) {
                    ExpertTypeDto expertTyprDto = new ExpertTypeDto();
                    BeanCopierUtils.copyProperties(expertType, expertTyprDto);
                    expertTypeDtoList.add(expertTyprDto);
                }
                expertDto.setExpertTypeDtoList(expertTypeDtoList);
            }

            //专家聘书
            if (expert.getExpertOfferList() != null && expert.getExpertOfferList().size() > 0) {
                List<ExpertOfferDto> expertOfferList = new ArrayList<>(expert.getExpertOfferList().size());
                (expert.getExpertOfferList()).forEach(epo -> {
                    ExpertOfferDto expertOfferDto = new ExpertOfferDto();
                    BeanCopierUtils.copyProperties(epo, expertOfferDto);
                    expertOfferList.add(expertOfferDto);
                });
                expertDto.setExpertOfferDtoList(expertOfferList);
            }
        }
        return expertDto;
    }

    /**
     * 查询重名专家
     */
    @Override
    public List<ExpertDto> findAllRepeat() {
        List<Expert> list = expertRepo.findAllRepeat();
        List<ExpertDto> dtoList = new ArrayList<ExpertDto>();
        if (list != null && list.size() > 0) {
            list.forEach(el -> {
                //把图片设置为空
                el.setPhoto(null);
                ExpertDto expertDto = new ExpertDto();
                BeanCopierUtils.copyProperties(el, expertDto);
                dtoList.add(expertDto);
            });
        }
        return dtoList;
    }

    /**
     * 专家抽取(与统计sql语句基本相同，目前先不整合)
     *
     * @param epSelConditions
     * @return
     */
    @Override
    public List<ExpertDto> findExpert(String minBusinessId, String reviewId, ExpertSelConditionDto[] epSelConditions) {
        if (!Validate.isString(minBusinessId) || epSelConditions == null || epSelConditions.length < 1) {
            return null;
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select ep.* from CS_EXPERT ep ");
        //1、关联本周未满2次，本月未满4次的专家
        hqlBuilder.append(" LEFT JOIN (  SELECT er.EXPERTID, COUNT (er.EXPERTID)  FROM ( ");
        hqlBuilder.append(" SELECT EP_SEL.EXPERTID, EP_REV.ID, EP_REV.REVIEWDATE FROM CS_EXPERT_SELECTED ep_sel LEFT JOIN CS_EXPERT_REVIEW ep_rev ");
        hqlBuilder.append(" ON EP_SEL.EXPERTREVIEWID = EP_REV.ID WHERE EP_SEL.ISJOIN = '9') er GROUP BY er.EXPERTID ");
        hqlBuilder.append(" HAVING COUNT (TO_CHAR (er.REVIEWDATE, 'yyyy-mm')) > 4 OR COUNT (TO_CHAR (er.REVIEWDATE, 'yyyy-iw')) > 2) fep ");
        hqlBuilder.append(" ON fep.EXPERTID = EP.EXPERTID ");
        //2、排除跟工作方案单位的专家
        hqlBuilder.append(" LEFT JOIN (SELECT WP.ID ID, WP.BUILDCOMPANY bcp, WP.DESIGNCOMPANY dcp ");
        hqlBuilder.append(" FROM CS_WORK_PROGRAM wp WHERE WP.ID = :wpid ) lwp ").setParam("wpid", minBusinessId);
        hqlBuilder.append(" ON (lwp.bcp = ep.COMPANY OR lwp.dcp = ep.COMPANY) ");
        //3、排除本次已经选择的专家
        hqlBuilder.append(" LEFT JOIN CS_EXPERT_SELECTED cursel ON CURSEL.EXPERTID = EP.EXPERTID AND CURSEL.EXPERTREVIEWID =:reviewId ");
        hqlBuilder.setParam("reviewId", reviewId);

        hqlBuilder.append(" WHERE (ep.STATE = :state1 or ep.STATE = :state2 ) ");
        hqlBuilder.setParam("state1", EnumExpertState.OFFICIAL.getValue()).setParam("state2", EnumExpertState.ALTERNATIVE.getValue());
        hqlBuilder.append(" AND fep.EXPERTID IS NULL ");
        hqlBuilder.append(" AND lwp.ID IS NULL ");
        hqlBuilder.append(" AND CURSEL.ID IS NULL ");

        //拼接专家抽取条件
        int totalLength = epSelConditions.length;
        //1、只有一个抽取条件的时候
        if (totalLength == 1) {
            hqlBuilder.append(" and (select count(ept.ID) from CS_EXPERT_TYPE ept where ept.expertid = ep.expertid ");
            buildCondition(hqlBuilder, "ept", epSelConditions[0]);
            hqlBuilder.append(" ) > 0");

            //2、多个专家的抽取条件
        } else {
            hqlBuilder.append(" and ( ");
            for (int i = 0; i < totalLength; i++) {
                if (i > 0) {
                    hqlBuilder.append(" or");
                }
                hqlBuilder.append("(select count(ept" + i + ".ID) from CS_EXPERT_TYPE ept" + i + " where ept" + i + ".expertid = ep.expertid ");
                buildCondition(hqlBuilder, "ept" + i, epSelConditions[i]);
                hqlBuilder.append(" ) > 0");
            }
            hqlBuilder.append(" ) ");
        }

        List<Expert> listExpert = expertRepo.findBySql(hqlBuilder);
        List<ExpertDto> listExpertDto = new ArrayList<>();
        if (listExpert != null && listExpert.size() > 0) {
            for (Expert item : listExpert) {
                //把图片设置为空
                item.setPhoto(null);
                ExpertDto epDto = new ExpertDto();
                BeanCopierUtils.copyProperties(item, epDto);
                listExpertDto.add(epDto);
            }
        }
        return listExpertDto;
    }

    /**
     * 根据抽取条件，统计符合条件的专家
     *
     * @param epSelCondition
     * @return
     */
    @Override
    public List<ExpertDto> countExpert(String minBusinessId, String reviewId, ExpertSelConditionDto epSelCondition) {
        return expertRepo.fingDrafExpert(minBusinessId, reviewId, epSelCondition);
    }

    /**
     * 查询条件封装
     *
     * @param hqlBuilder
     * @param alias
     * @param epSelCondition
     */
    private void buildCondition(HqlBuilder hqlBuilder, String alias, ExpertSelConditionDto epSelCondition) {
        //突出专业，大类
        if (Validate.isString(epSelCondition.getMaJorBig())) {
            hqlBuilder.append(" and " + alias + ".maJorBig = :maJorBig ").setParam("maJorBig", epSelCondition.getMaJorBig());
        }
        //突出专业，小类
        if (Validate.isString(epSelCondition.getMaJorSmall())) {
            hqlBuilder.append(" and " + alias + ".maJorSmall = :maJorSmall ").setParam("maJorSmall", epSelCondition.getMaJorSmall());
        }
        //专家类型
        if (Validate.isString(epSelCondition.getExpeRttype())) {
            hqlBuilder.append(" and " + alias + ".expertType = :rttype ").setParam("rttype", epSelCondition.getExpeRttype());
        }

    }

    @Override
    @Transactional
    public void savePhone(byte[] bytes, String expertId) {
        Expert expert = expertRepo.findById(expertId);
        expert.setPhoto(bytes);
        expertRepo.save(expert);
    }

    @Override
    public byte[] findExpertPhoto(String expertId) {
        Expert expert = expertRepo.findByIds(Expert_.expertID.getName(), expertId, null).get(0);
        return expert.getPhoto();
    }

    /**
     * 获取最大的专家序号
     *
     * @return
     */
    @Override
    public int findMaxNumber() {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(to_number(expertNo)) from cs_expert");
        return expertRepo.returnIntBySql(sqlBuilder);
    }

    /**
     * 专家方案抽取
     *
     * @param minBusinessId 专家抽取的业务ID
     * @param reviewId
     * @param paramArrary
     * @return
     */
    @Override
    public ResultMsg autoExpertReview(boolean isAllExtract,String minBusinessId, String reviewId, ExpertSelConditionDto[] paramArrary) {
        String conditionIds = "";       //条件ID，用于更新抽取次数
        boolean isLetterRw = false;     //是否专家函评，默认是否
        int selectedEPCount = -1;       //符合条件的专家
        List<ExpertDto> officialEPList = new ArrayList<>(), alternativeEPList = new ArrayList<>(), allEPList = new ArrayList<>();
        ExpertReview expertReview = expertReviewRepo.findById(reviewId);
        if( null != expertReview.getPayDate() && null != expertReview.getReviewDate() && (new Date()).after(expertReview.getReviewDate())){
            return ResultMsg.error("已发送专家评审费，不能对方案再修改！");
        }

        if(isAllExtract){
            //如果已经有抽取类型的专家，则表示该方案已经进行了全局抽取
            List<ExpertSelected> selectedList = expertReview.getExpertSelectedList();
            for(ExpertSelected expertSelected : selectedList){
                if(Constant.EnumExpertSelectType.AUTO.getValue().equals(expertSelected.getSelectType())
                        && minBusinessId.equals(expertSelected.getBusinessId())){
                    return ResultMsg.error("该项目已进行整体专家方案抽取，不能再次执行此操作！");
                }
            }
        }

        //如果是项目，则判断是否是专家函评
        if ((ProjectConstant.BUSINESS_TYPE.SIGN.toString()).equals(expertReview.getBusinessType())) {
            isLetterRw = workProgramRepo.checkReviewType(Constant.MergeType.REVIEW_LEETER.getValue(), minBusinessId);
        }

        //如果是再次抽取(再次抽取是单个条件抽取)，要判断选定的专家是否已经满足条件，如已经满足，则不允许再次抽取
        if (!isAllExtract) {
            ExpertSelCondition expertSelCondition = expertSelConditionRepo.findById(ExpertSelCondition_.id.getName(), paramArrary[0].getId());
            if (expertSelCondition != null && expertSelCondition.getSelectIndex() > 0) {
                if (!SUPER_ACCOUNT.equals(SessionUtil.getLoginName()) && (expertSelCondition.getSelectIndex() > 2)) {
                    return ResultMsg.error("该条件已经进行3次专家抽取，不能再进行专家抽取！");
                }
            }
        }

        List<ExpertSelected> saveList = new ArrayList<>();

        //2、遍历所有抽取条件，每个条件单独抽取
        ResultMsg resultMsg = null;
        for (int k = 0, l = paramArrary.length; k < l; k++) {
            ExpertSelConditionDto epConditon = paramArrary[k];
            if (!Validate.isString(epConditon.getId())) {
                resultMsg = ResultMsg.error( "请先保存专家抽取条件再进行专家抽取！");
                break;
            }
            if(Validate.isString(conditionIds)){
                conditionIds += SEPARATE_COMMA;
            }
            conditionIds += epConditon.getId();
            if (resultMsg != null && resultMsg.isFlag() == false) {
                return resultMsg;
            }
            int chooseCount = epConditon.getOfficialNum();
            //如果是再次抽取，则要计算已经确认的专家数
            if (!isAllExtract) {
                selectedEPCount = expertSelectedRepo.findConfirmSeletedEP(reviewId, epConditon.getMaJorBig(), epConditon.getMaJorSmall(), epConditon.getExpeRttype(), epConditon.getCompositeScore(), epConditon.getCompositeScoreEnd());
                chooseCount = (selectedEPCount > 1) ? (chooseCount - selectedEPCount) : chooseCount;
                if (chooseCount < 1) {
                    resultMsg = ResultMsg.error("该条件抽取的专家已经达到设定人数，不能再次抽取！");
                    return resultMsg;
                }
                expertReview.setExtractInfo(epConditon.getId());
                expertReview.setSelectIndex(Validate.isObject(epConditon.getSelectIndex())?(epConditon.getSelectIndex()+ 1):1);
            }

            //2、获取所有符合条件的专家
            List<ExpertDto> matchEPList = countExpert(minBusinessId, reviewId, epConditon);
            if (!Validate.isList(matchEPList)) {
                resultMsg = ResultMsg.error( "条件号【" + (k + 1) + "】抽取的专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！");
                break;
            }
            allEPList.addAll(matchEPList);
            int totalCount = matchEPList.size();

            if (epConditon.getOfficialNum() > totalCount) {
                resultMsg = ResultMsg.error( "条件号【" + (k + 1) + "】抽取的正选专家人数不够，抽取无效！请重新设置抽取条件！");
                break;
            }
            if (epConditon.getAlternativeNum() > (totalCount * 2)) {
                resultMsg = ResultMsg.error("条件号【" + (k + 1) + "】抽取的备选专家人数不够，抽取无效！请重新设置抽取条件！");
            }

            //3、开始抽取
            for (int i = 0; i < chooseCount; i++) {
                if (!addAutoExpert(officialEPList, matchEPList, saveList, epConditon, expertReview, minBusinessId, isLetterRw,true)) {
                    resultMsg = ResultMsg.error("条件号【" + (k + 1) + "】抽取的正选专家人数不够，抽取无效！请重新设置抽取条件！");
                    break;
                }
                if (!addAutoExpert(alternativeEPList, matchEPList, saveList, epConditon, expertReview, minBusinessId, isLetterRw,false)) {
                    resultMsg = ResultMsg.error("条件号【" + (k + 1) + "】抽取的备选专家人数不够，抽取无效！请重新设置抽取条件！");
                    break;
                }
            }
        }
        if (resultMsg == null) {
            resultMsg = ResultMsg.ok( "操作成功！");
        }
        //4、抽取成功，添加相应的抽取记录
        if (resultMsg.isFlag()) {
            Map<String, Object> resultMap = new HashMap<>();
            expertSelectedRepo.bathUpdate(saveList);
            List<ExpertSelectedDto> resultList = new ArrayList<>();
            saveList.forEach(ep -> {
                ExpertSelectedDto expertSelectedDto = new ExpertSelectedDto();
                BeanCopierUtils.copyProperties(ep, expertSelectedDto);
                Expert reEP = ep.getExpert();
                reEP.setPhoto(null);
                ExpertDto reEPDto = new ExpertDto();
                BeanCopierUtils.copyProperties(reEP, reEPDto);
                expertSelectedDto.setExpertDto(reEPDto);
                resultList.add(expertSelectedDto);
            });
            resultMap.put("autoEPList", resultList);
            resultMap.put("allEPList", allEPList);
            resultMap.put("officialEPList", officialEPList);
            resultMap.put("alternativeEPList", alternativeEPList);
            //更新专家抽取条件的抽取次数
            expertSelConditionRepo.updateSelectIndexById(conditionIds);
            if(isAllExtract){
                expertReview.setExtractInfo("ALL");
                expertReview.setFinishExtract(1);
            }
            expertReviewRepo.save(expertReview);
            resultMsg.setReObj(resultMap);
        }
        return resultMsg;
    }

    /**
     * 获取抽取的专家，排除同名
     *
     * @param saveEPList
     * @param randomEPList
     * @return
     */
    private boolean addAutoExpert(List<ExpertDto> saveEPList, List<ExpertDto> randomEPList, List<ExpertSelected> saveList,
                                  ExpertSelConditionDto epConditon, ExpertReview expertReview, String minBusinessId, boolean isLetterRw,boolean isOfficial) {
        if (randomEPList.size() == 0) {
            return false;
        }
        Random random = new Random();
        boolean success = true;
        int randomNum = random.nextInt(randomEPList.size());
        ExpertDto randomEP = randomEPList.get(randomNum);
        randomEPList.remove(randomNum);
        //排除同名
        for (ExpertDto ep : saveEPList) {
            if (ep.getExpertID().equals(randomEP.getExpertID())) {
                success = false;
                break;
            }
        }
        //如果不满足，则继续抽
        if (success == false) {
            return addAutoExpert(saveEPList, randomEPList, saveList, epConditon, expertReview, minBusinessId, isLetterRw,isOfficial);
        } else {
            saveEPList.add(randomEP);
            //保存抽取记录
            ExpertSelected aExpertSelected = new ExpertSelected();
            //抽取条件ID
            aExpertSelected.setConditionId(epConditon.getId());
            aExpertSelected.setIsJoin(Constant.EnumState.YES.getValue());
            aExpertSelected.setIsConfrim(Constant.EnumState.NO.getValue());
            aExpertSelected.setSelectType(Constant.EnumExpertSelectType.AUTO.getValue());
            aExpertSelected.setMaJorBig(epConditon.getMaJorBig());
            aExpertSelected.setMaJorSmall(epConditon.getMaJorSmall());
            aExpertSelected.setSelectIndex(epConditon.getSelectIndex() == null ? 1 : (epConditon.getSelectIndex() + 1));
            aExpertSelected.setExpeRttype(epConditon.getExpeRttype());
            //抽取专家备注信息
            if(isOfficial){
                aExpertSelected.setRemark(Constant.ExpertSelectType.正选.name());
            }else{
                aExpertSelected.setRemark(Constant.ExpertSelectType.备选.name());
            }
            //默认专家费用，每个专家1000元
            aExpertSelected.setReviewCost(new BigDecimal(EXPERT_REVIEW_COST));
            Expert aEP = new Expert();
            BeanCopierUtils.copyProperties(saveEPList.get(saveEPList.size() - 1), aEP);
            aExpertSelected.setExpert(aEP);                 //保存专家映射
            aExpertSelected.setExpertReview(expertReview);  //保存抽取条件映射
            aExpertSelected.setBusinessId(minBusinessId);   //专家抽取业务ID
            //是否专家函评
            if (isLetterRw) {
                aExpertSelected.setIsLetterRw(Constant.EnumState.YES.getValue());
            } else {
                aExpertSelected.setIsLetterRw(Constant.EnumState.NO.getValue());
            }
            saveList.add(aExpertSelected);
            return true;
        }
    }

    /**
     * 专家抽取统计
     *
     * @param expertSelectHis
     * @return
     */
    @Override
    public List<ExpertSelectHis> expertSelectHis(ExpertSelectHis expertSelectHis, boolean isScore) {
        List<Object[]> epList = expertSelectedRepo.getSelectHis(expertSelectHis, isScore);
        List<ExpertSelectHis> resultList = null;
        String expertID = "";     //初始专家ID
        ExpertSelectHis expertSelectHisObj = null;
        List<ExpertSelectHis> childList = null;
        if (Validate.isList(epList)) {
            resultList = new ArrayList<>();
            for (int i = 0, l = epList.size(); i < l; i++) {
                Object[] expMap = epList.get(i);
                String expId = expMap[0].toString();
                String expName = expMap[1].toString();
                String expCompany = expMap[2] == null ? "" : expMap[2].toString();
                String expField = expMap[3] == null ? "" : expMap[3].toString();
                String projectName = expMap[4] == null ? "" : expMap[4].toString();
                String majorBig = expMap[5] == null ? "" : expMap[5].toString();
                String marjorSmall = expMap[6] == null ? "" : expMap[6].toString();
                String expertType = expMap[7] == null ? "" : expMap[7].toString();
                String selectType = expMap[8] == null ? "" : expMap[8].toString();
                String isConfirm = expMap[9] == null ? "" : expMap[9].toString();
                String reviewType = expMap[10] == null ? "" : expMap[10].toString();
                Date reviewDate = DateUtils.converToDate(expMap[11] == null ? "" : expMap[11].toString(), null);
                String mainChargeUserName = expMap[12] == null ? "" : expMap[12].toString();
                Double score = expMap[13] == null ? null : Double.valueOf(expMap[13].toString());
                String describes = expMap[14] == null ? "" : expMap[14].toString();
                String reviewStage = expMap[15] == null ? "" : expMap[15].toString();

                if (!Validate.isString(expertID) || !expertID.equals(expId)) {
                    if (expertSelectHisObj != null) {
                        expertSelectHisObj.setChildList(childList);
                        resultList.add(expertSelectHisObj);
                    }
                    expertID = expId;
                    expertSelectHisObj = new ExpertSelectHis();
                    childList = new ArrayList<>();
                    expertSelectHisObj.setEpId(expId);
                    expertSelectHisObj.setEpName(expName);
                    expertSelectHisObj.setEpCompany(expCompany);
                }
                ExpertSelectHis childObj = new ExpertSelectHis();
                childObj.setEpId(expId);
                childObj.setEpName(expName);
                childObj.setEpCompany(expCompany);
                childObj.setEpField(expField);
                childObj.setProjectName(projectName);
                childObj.setMajorBig(majorBig);
                childObj.setMarjorSmall(marjorSmall);
                childObj.setSelectType(Constant.EnumExpertSelectType.getName(selectType));
                childObj.setIsConfirm(Constant.EnumState.YES.getValue().equals(isConfirm) ? "已选定" : "否");
                childObj.setReviewType(reviewType);
                childObj.setExpertType(expertType);
                childObj.setReviewDate(reviewDate);
                childObj.setMainChargeUserName(mainChargeUserName);
                childObj.setScore(score);
                childObj.setDescribes(describes);
                childObj.setReviewStage(reviewStage);
                childList.add(childObj);
                //最后一个
                if (i == (l - 1)) {
                    //如果只有一个专家的情况
                    if (!Validate.isList(expertSelectHisObj.getChildList())) {
                        expertSelectHisObj.setChildList(childList);
                    }
                    resultList.add(expertSelectHisObj);
                }
            }
        }
        return resultList;
    }

    @Override
    public List<ExpertDto> exportData(String filters) {
        List<Expert> expertList = expertRepo.exportData(filters);
        List<ExpertDto> expertDtoList = new ArrayList<>();
        for (Expert expert : expertList) {
            ExpertDto expertDto = new ExpertDto();
            BeanCopierUtils.copyProperties(expert, expertDto);
            expertDto.setUnable(Constant.EnumState.NO.getValue().equals(expertDto.getUnable()) ? "否" : "是");
            expertDtoList.add(expertDto);
        }
        return expertDtoList;
    }
}
