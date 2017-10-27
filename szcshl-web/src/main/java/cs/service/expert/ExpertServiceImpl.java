package cs.service.expert;

import cs.common.Constant;
import cs.common.Constant.EnumExpertState;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.expert.*;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
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
            listExpertDto.add(expertDto);
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(listExpertDto);
        return pageModelDto;
    }

    /**
     * 保存专家信息
     * @param expertDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg saveExpert(ExpertDto expertDto) {
        //重复专家判断
        boolean isFill = expertRepo.checkIsHaveIdCard(expertDto.getIdCard(),expertDto.getExpertID());
        if (isFill == false) {
            Expert expert = null;
            if(Validate.isString(expertDto.getExpertID())){
                expert = expertRepo.findById(Expert_.expertID.getName(),expertDto.getExpertID());
                BeanCopierUtils.copyPropertiesIgnoreNull(expertDto, expert);
            }else{
                expert = new Expert();
                BeanCopierUtils.copyProperties(expertDto, expert);
                //设置默认属性
                expert.setState(EnumExpertState.AUDITTING.getValue());
                expert.setExpertID(UUID.randomUUID().toString());
                //专家编码，系统自动生成
                expert.setExpertNo(String.format("%06d", Integer.valueOf(findMaxNumber())+1));
                expert.setCreatedDate(new Date());
                expert.setCreatedBy(SessionUtil.getDisplayName());
            }
            expert.setModifiedDate(new Date());
            expert.setModifiedBy(SessionUtil.getDisplayName());
            expertRepo.save(expert);
            //设置返回值
            expertDto.setExpertID(expert.getExpertID());
            expertDto.setExpertNo(expert.getExpertNo());

            return  new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！",expertDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),String.format("身份证号为%s 的专家已存在,请重新输入", expertDto.getIdCard()));
        }
    }

    /**
     * 删除操作，实为更新专家状态为删除状态
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteExpert(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + Expert.class.getSimpleName() + " set " + Expert_.state.getName() + "=:state where " + Expert_.expertID.getName() + "=:id");
        hqlBuilder.setParam("state", EnumExpertState.REMOVE.getValue()).setParam("id", id);
        expertRepo.executeHql(hqlBuilder);
    }

    @Override
    @Transactional
    public void deleteExpert(String[] ids) {
        for (String id : ids) {
            this.deleteExpert(id);
        }
        logger.info("删除专家");
    }

    /**
     * 专家审核
     * @param ids
     * @param state
     */
    @Override
    @Transactional
    public void updateAudit(String ids, String state) {
        if (Validate.isString(ids)) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" update " + Expert.class.getSimpleName() + " set " + Expert_.state.getName() + " = :state ");
            hqlBuilder.setParam("state", state);
            hqlBuilder.bulidPropotyString("where",Expert_.expertID.getName(),ids);

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
        hqlBuilder.append(" FROM CS_WORK_PROGRAM wp WHERE WP.ID = :wpid ) lwp ").setParam("wpid",minBusinessId);
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

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select ep.* FROM CS_EXPERT ep ");
        //1、关联本周未满2次，本季度不超过12次的专家
        hqlBuilder.append(" LEFT JOIN (  SELECT er.EXPERTID, COUNT (er.EXPERTID)  FROM ( ");
        hqlBuilder.append(" SELECT EP_SEL.EXPERTID, EP_REV.ID, EP_REV.REVIEWDATE FROM CS_EXPERT_SELECTED ep_sel , CS_EXPERT_REVIEW ep_rev ");
        hqlBuilder.append(" WHERE EP_SEL.EXPERTREVIEWID = EP_REV.ID AND TO_CHAR (ep_rev.REVIEWDATE, 'q') = TO_CHAR (SYSDATE, 'q') ");
        hqlBuilder.append(" AND EP_SEL.ISCONFRIM =:isConfrim AND EP_SEL.ISJOIN =:isJoin ) er GROUP BY er.EXPERTID ");
        hqlBuilder.setParam("isConfrim",Constant.EnumState.YES.getValue()).setParam("isJoin", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" HAVING COUNT (TO_CHAR (er.REVIEWDATE, 'q')) > 12 OR COUNT (TO_CHAR (er.REVIEWDATE, 'yyyy-iw')) > 2) fep ");
        hqlBuilder.append(" ON fep.EXPERTID = EP.EXPERTID ");
        //2、排除跟工作方案单位的专家(保留，不影响)
        hqlBuilder.append(" LEFT JOIN (SELECT WP.ID ID, WP.BUILDCOMPANY bcp, WP.DESIGNCOMPANY dcp ");
        hqlBuilder.append(" FROM CS_WORK_PROGRAM wp WHERE WP.ID = :wpid ) lwp ").setParam("wpid",minBusinessId);
        hqlBuilder.append(" ON (lwp.bcp = ep.COMPANY OR lwp.dcp = ep.COMPANY) ");
        //3、排除本次已经选择的专家
        hqlBuilder.append(" LEFT JOIN CS_EXPERT_SELECTED cursel ON CURSEL.EXPERTID = EP.EXPERTID AND CURSEL.EXPERTREVIEWID =:reviewId ");
        hqlBuilder.setParam("reviewId", reviewId);
        //4、抽取专家只能是正式专家
        hqlBuilder.append(" WHERE ep.STATE = :state ");
        hqlBuilder.setParam("state", EnumExpertState.OFFICIAL.getValue());
        hqlBuilder.append(" AND fep.EXPERTID IS NULL ");
        hqlBuilder.append(" AND lwp.ID IS NULL ");
        hqlBuilder.append(" AND CURSEL.ID IS NULL ");


        //加上选择的条件
        if (Validate.isString(epSelCondition.getMaJorBig()) || Validate.isString(epSelCondition.getMaJorSmall()) || Validate.isString(epSelCondition.getExpeRttype()) || epSelCondition.getCompositeScore() != null ) {
            hqlBuilder.append(" AND (select count(ept.ID) from CS_EXPERT_TYPE ept where ept.expertid = ep.expertid ");
            buildCondition(hqlBuilder, "ept", epSelCondition);
            hqlBuilder.append(" ) > 0");
            if(epSelCondition.getCompositeScore() != null &&  epSelCondition.getCompositeScore() > 0){
                hqlBuilder.append(" and  ep.compositeScore>=:compositeScore");
                hqlBuilder.setParam("compositeScore", epSelCondition.getCompositeScore());
            }else {
                hqlBuilder.append(" and  ep.compositeScore is null or ep.compositeScore >=:compositeScore");
                hqlBuilder.setParam("compositeScore" , epSelCondition.getCompositeScore() == null ? 0 : epSelCondition.getCompositeScore());
            }
        }

        List<Expert> listExpert = expertRepo.findBySql(hqlBuilder);
        List<ExpertDto> listExpertDto = new ArrayList<>();
        if (Validate.isList(listExpert)) {
            listExpert.forEach(el ->{
                //把图片设置为空
                el.setPhoto(null);
                ExpertDto epDto = new ExpertDto();
                BeanCopierUtils.copyProperties(el, epDto);
                listExpertDto.add(epDto);
            });
        }
        return listExpertDto;
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
     * @param minBusinessId  专家抽取的业务ID
     * @param reviewId
     * @param paramArrary
     * @return
     */
    @Override
    public ResultMsg autoExpertReview(String minBusinessId, String reviewId, ExpertSelConditionDto[] paramArrary) {
        String conditionIds = "";       //条件ID，用于更新抽取次数
        boolean notFirstTime = false;
        boolean isLetterRw = false;     //是否专家函评，默认是否
        int selectedEPCount = -1;       //符合条件的专家
        List<ExpertDto> officialEPList = new ArrayList<>(),alternativeEPList = new ArrayList<>(),allEPList = new ArrayList<>();
        ExpertReview expertReview = expertReviewRepo.findById(ExpertReview_.id.getName(),reviewId);
        //如果是项目，则判断是否是专家函评
        if(Constant.BusinessType.SIGN.getValue().equals(expertReview.getBusinessType())){
            WorkProgram wp = workProgramRepo.findById(WorkProgram_.id.getName(),minBusinessId);
            if("专家函评".equals(wp.getReviewType())){
                isLetterRw = true;
            }
        }

        //如果是再次抽取(再次抽取是单个条件抽取)，要判断选定的专家是否已经满足条件，如已经满足，则不允许再次抽取
        if(paramArrary.length == 1 && Validate.isString(paramArrary[0].getId())){
            ExpertSelCondition  expertSelCondition = expertSelConditionRepo.findById(ExpertSelCondition_.id.getName(),paramArrary[0].getId());
            if(expertSelCondition != null && expertSelCondition.getSelectIndex() > 0){
                if(expertSelCondition.getSelectIndex() == 3){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"您已经进行3次专家抽取，不能再进行抽取操作！");
                }
                notFirstTime = true;
            }
        }
        List<ExpertSelected> saveList = new ArrayList<>();

        //2、遍历所有抽取条件，每个条件单独抽取
        ResultMsg resultMsg = null;
        for(int k=0,l=paramArrary.length;k<l;k++){
            ExpertSelConditionDto epConditon = paramArrary[k];
            if(!Validate.isString(epConditon.getId())){
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"请先保存专家抽取条件再进行专家抽取！");
                break;
            }
            conditionIds += Validate.isString(conditionIds)?","+epConditon.getId():epConditon.getId();
            if(resultMsg != null && resultMsg.isFlag() == false){
                return resultMsg;
            }
            int chooseCount = epConditon.getOfficialNum();
            //如果是再次抽取，则要计算已经确认的专家数
            if(notFirstTime){
                selectedEPCount = expertSelectedRepo.findConfirmSeletedEP(reviewId,epConditon.getMaJorBig(),epConditon.getMaJorSmall(),epConditon.getExpeRttype() , epConditon.getCompositeScore());
                chooseCount = (selectedEPCount > -1)?(chooseCount-selectedEPCount):chooseCount;
                if(chooseCount < 1){
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),
                            "专业大类【"+epConditon.getMaJorBig()+"】,专业小类【"+epConditon.getMaJorSmall()+"】，专家类型【"+epConditon.getExpeRttype() + "】,综合评分【" +epConditon.getCompositeScore()+"】抽取并确认的专家数已经满足，不用再次抽取！");
                    return resultMsg;
                }
            }

            //2、获取所有符合条件的专家
            List<ExpertDto> matchEPList = countExpert(minBusinessId,reviewId, epConditon);
            if(!Validate.isList(matchEPList)){
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"条件号【"+(k+1)+"】抽取的专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！");
                break;
            }
            allEPList.addAll(matchEPList);
            int totalCount = matchEPList.size();

            if(epConditon.getOfficialNum() > totalCount){
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"条件号【"+(k+1)+"】抽取的正选专家人数不够，抽取无效！请重新设置抽取条件！");
                break;
            }
            if(epConditon.getAlternativeNum() > (totalCount*2)){
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"条件号【"+(k+1)+"】抽取的备选专家人数不够，抽取无效！请重新设置抽取条件！");
            }

            //3、开始抽取
            for(int i = 0;i<chooseCount;i++){
                if(!addAutoExpert(officialEPList,matchEPList,saveList,epConditon,expertReview,minBusinessId,isLetterRw)){
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"条件号【"+(k+1)+"】抽取的正选专家人数不够，抽取无效！请重新设置抽取条件！");
                    break;
                }
                if(!addAutoExpert(alternativeEPList,matchEPList,saveList,epConditon,expertReview,minBusinessId,isLetterRw)){
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"条件号【"+(k+1)+"】抽取的备选专家人数不够，抽取无效！请重新设置抽取条件！");
                    break;
                }
            }
        }
        if(resultMsg == null){
            resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
        }
        //4、抽取成功，添加相应的抽取记录
        if(resultMsg.isFlag()){
            Map<String,Object> resultMap = new HashMap<>();
            expertSelectedRepo.bathUpdate(saveList);
            List<ExpertSelectedDto> resultList = new ArrayList<>();
            saveList.forEach(ep ->{
                ExpertSelectedDto expertSelectedDto = new ExpertSelectedDto();
                BeanCopierUtils.copyProperties(ep,expertSelectedDto);
                Expert reEP = ep.getExpert();
                reEP.setPhoto(null);
                ExpertDto reEPDto = new ExpertDto();
                BeanCopierUtils.copyProperties(reEP,reEPDto);
                expertSelectedDto.setExpertDto(reEPDto);
                resultList.add(expertSelectedDto);
            });
            resultMap.put("autoEPList",resultList);
            resultMap.put("allEPList",allEPList);
            resultMap.put("officialEPList",officialEPList);
            resultMap.put("alternativeEPList",alternativeEPList);
            //更新专家抽取条件的抽取次数
            expertSelConditionRepo.updateSelectIndexById(conditionIds);
            resultMsg.setReObj(resultMap);
        }
        return resultMsg;
    }

    /**
     * 获取抽取的专家，排除同名
     * @param saveEPList
     * @param randomEPList
     * @return
     */
    private boolean addAutoExpert(List<ExpertDto> saveEPList,List<ExpertDto> randomEPList,List<ExpertSelected> saveList,
                    ExpertSelConditionDto epConditon,ExpertReview expertReview,String minBusinessId,boolean isLetterRw){
        if(randomEPList.size() == 0){
            return false;
        }
        Random random = new Random();
        boolean success = true;
        int randomNum = random.nextInt(randomEPList.size());
        ExpertDto randomEP = randomEPList.get(randomNum);
        randomEPList.remove(randomNum);
        //排除同名
        for(ExpertDto ep:saveEPList){
            if(ep.getExpertID().equals(randomEP.getExpertID())){
                success = false;
                break;
            }
        }
        //如果不满足，则继续抽
        if(success == false){
            return addAutoExpert(saveEPList,randomEPList,saveList,epConditon,expertReview,minBusinessId,isLetterRw);
        }else{
            saveEPList.add(randomEP);
            //保存抽取记录
            ExpertSelected aExpertSelected = new ExpertSelected();
            aExpertSelected.setIsJoin(Constant.EnumState.YES.getValue());
            aExpertSelected.setIsConfrim(Constant.EnumState.NO.getValue());
            aExpertSelected.setSelectType(Constant.EnumExpertSelectType.AUTO.getValue());
            aExpertSelected.setMaJorBig(epConditon.getMaJorBig());
            aExpertSelected.setMaJorSmall(epConditon.getMaJorSmall());
            aExpertSelected.setSelectIndex(epConditon.getSelectIndex()==null?1:(epConditon.getSelectIndex()+1));
            aExpertSelected.setExpeRttype(epConditon.getExpeRttype());
            //默认专家费用，每个专家1000元
            aExpertSelected.setReviewCost(new BigDecimal(1000));
            Expert aEP = new Expert();
            BeanCopierUtils.copyProperties(saveEPList.get(saveEPList.size()-1),aEP);
            aExpertSelected.setExpert(aEP);                 //保存专家映射
            aExpertSelected.setExpertReview(expertReview);  //保存抽取条件映射
            aExpertSelected.setBusinessId(minBusinessId);   //专家抽取业务ID
            if(isLetterRw){                                 //是否专家函评
                aExpertSelected.setIsLetterRw(Constant.EnumState.YES.getValue());
            }else{
                aExpertSelected.setIsLetterRw(Constant.EnumState.NO.getValue());
            }
            saveList.add(aExpertSelected);
            return true;
        }
    }

    /**
     * 专家抽取统计
     * @param expertSelectHis
     * @return
     */
    @Override
    public List<ExpertSelectHis> expertSelectHis(ExpertSelectHis expertSelectHis,boolean isScore) {
        List<Object[]> epList = expertSelectedRepo.getSelectHis(expertSelectHis,isScore);
        List<ExpertSelectHis> resultList = null;
        String expertID="";     //初始专家ID
        ExpertSelectHis expertSelectHisObj = null;
        List<ExpertSelectHis> childList = null;
        if(Validate.isList(epList)){
            resultList = new ArrayList<>();

            for(int i=0,l=epList.size();i<l;i++){
                Object[] expMap = epList.get(i);
                String expId = expMap[0].toString();
                String expName = expMap[1].toString();
                String expCompany = expMap[2]==null?"":expMap[2].toString();
                String expField = expMap[3]==null?"":expMap[3].toString();
                String projectName = expMap[4]==null?"":expMap[4].toString();
                String majorBig = expMap[5]==null?"":expMap[5].toString();
                String marjorSmall = expMap[6]==null?"":expMap[6].toString();
                String expertType = expMap[7]==null?"":expMap[7].toString();
                String selectType = expMap[8]==null?"":expMap[8].toString();
                String isConfirm = expMap[9]==null?"":expMap[9].toString();
                String reviewType = expMap[10]==null?"":expMap[10].toString();
                Date reviewDate = DateUtils.converToDate(expMap[11]==null?"":expMap[11].toString(),null);
                String mainChargeUserName = expMap[12]==null?"":expMap[12].toString();
                Double score = expMap[13]==null?null:Double.valueOf(expMap[13].toString());
                String describes = expMap[14]==null?"":expMap[14].toString();
                String reviewStage = expMap[15]==null?"":expMap[15].toString();

                if(!Validate.isString(expertID) || !expertID.equals(expId)){
                    if(expertSelectHisObj != null){
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
                childObj.setIsConfirm(Constant.EnumState.YES.getValue().equals(isConfirm)?"已选定":"否");
                childObj.setReviewType(reviewType);
                childObj.setExpertType(expertType);
                childObj.setReviewDate(reviewDate);
                childObj.setMainChargeUserName(mainChargeUserName);
                childObj.setScore(score);
                childObj.setDescribes(describes);
                childObj.setReviewStage(reviewStage);
                childList.add(childObj);
                //最后一个
                if(i == (l-1)){
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
        for(Expert expert : expertList){
            ExpertDto expertDto = new ExpertDto();
            BeanCopierUtils.copyProperties(expert , expertDto);
            expertDto.setUnable(Constant.EnumState.NO.getValue().equals(expertDto.getUnable()) ? "否" : "是");
            expertDtoList.add(expertDto);
        }
        return expertDtoList;
    }
}
