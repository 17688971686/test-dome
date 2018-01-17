package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.activiti.ProcessDiagramGenerator;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.*;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.model.PageModelDto;
import cs.model.project.AssistPlanDto;
import cs.model.project.AssistPlanSignDto;
import cs.model.project.AssistUnitDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import cs.repository.repositoryImpl.project.AssistUnitRepo;
import cs.repository.repositoryImpl.project.SignRepo;

/**
 * Description: 协审方案 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Service
public class AssistPlanServiceImpl implements AssistPlanService {
    private static final Logger log = Logger.getLogger(AssistPlanServiceImpl.class);
    @Autowired
    private AssistPlanRepo assistPlanRepo;
    @Autowired
    private AssistPlanSignRepo assistPlanSignRepo;
    @Autowired
    private SignService signService;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private AssistUnitRepo assistUnitRepo;

    @Override
    public PageModelDto<AssistPlanDto> get(ODataObj odataObj) {
        PageModelDto<AssistPlanDto> pageModelDto = new PageModelDto<AssistPlanDto>();
        List<AssistPlan> resultList = assistPlanRepo.findByOdata(odataObj);
        List<AssistPlanDto> resultDtoList = new ArrayList<AssistPlanDto>(resultList==null?0:resultList.size());
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                AssistPlanDto modelDto = new AssistPlanDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     *
     * @param record
     */
    @Override
    @Transactional
    public ResultMsg save(AssistPlanDto record) {
        try{
            AssistPlan assistPlan = new AssistPlan();
            Date now = new Date();
            if (!Validate.isString(record.getId())) {
                BeanCopierUtils.copyProperties(record, assistPlan);
                assistPlan.setId(UUID.randomUUID().toString());
                int maxPlanName = findMaxPlanName(now);
                String panName = DateUtils.converToString(now, "yyyyMMdd") + String.format("%02d", (maxPlanName+1));
                assistPlan.setPlanName(panName);
                assistPlan.setCreatedBy(SessionUtil.getDisplayName());
                assistPlan.setCreatedDate(now);
                //默认全部抽中
                assistPlan.setDrawType(Constant.EnumState.PROCESS.getValue());
                assistPlan.setPlanState(Constant.EnumState.PROCESS.getValue());
            } else {
                assistPlan = assistPlanRepo.findById(record.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(record, assistPlan);
                List<AssistUnit> assistUnitList = new ArrayList<>();
                if(record.getAssistUnitDtoList()!= null && record.getAssistUnitDtoList().size()>0){
                    for(AssistUnitDto assistUnitDto : record.getAssistUnitDtoList()){
                        AssistUnit assistUnit = new AssistUnit();
                        BeanCopierUtils.copyPropertiesIgnoreNull(assistUnitDto , assistUnit);
                        assistUnitList.add(assistUnit);
                    }
                }
                assistPlan.setAssistUnitList(assistUnitList);
            }

            assistPlan.setModifiedBy(SessionUtil.getDisplayName());
            assistPlan.setModifiedDate(now);
            assistPlanRepo.save(assistPlan);

            //赋值返回页面
            BeanCopierUtils.copyProperties(assistPlan,record);

            /************************  协审类型判断 **************************/
            if (Constant.MergeType.ASSIST_SIGNLE.getValue().equals(record.getAssistType())) {
                List<AssistPlanSign> saveList = new ArrayList<>(record.getSplitNum());
                int i = 1;
                while (i <= record.getSplitNum()) {
                    AssistPlanSign assistPlanSign = new AssistPlanSign();
                    assistPlanSign.setSignId(record.getSignId());
                    assistPlanSign.setSplitNum(i);
                    assistPlanSign.setAssistType(record.getAssistType());
                    assistPlanSign.setAssistPlan(assistPlan);
                    assistPlanSign.setIsMain(i == 1 ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue()); //主要为了方便显示
                    saveList.add(assistPlanSign);
                    i++;
                }
                assistPlanSignRepo.bathUpdate(saveList);
            } else {
                //合并协审，保存的只有主项目
                AssistPlanSign assistPlanSign = new AssistPlanSign();
                assistPlanSign.setSignId(record.getSignId());
                assistPlanSign.setAssistType(record.getAssistType());
                assistPlanSign.setAssistPlan(assistPlan);
                assistPlanSign.setIsMain(Constant.EnumState.YES.getValue());
                assistPlanSignRepo.save(assistPlanSign);
            }

            //更新项目协审状态
            signService.updateAssistState(record.getSignId(), Constant.EnumState.YES.getValue());

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！", record);
        }catch (Exception e){
            log.error("项目协审保存异常："+e.getMessage());
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，错误信息已记录，请联系管理员查看！");
        }

    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @Override
    public AssistPlanDto findById(String id) {
        AssistPlanDto planDto = new AssistPlanDto();
        if (Validate.isString(id)) {
            AssistPlan p = assistPlanRepo.findById(id);
            BeanCopierUtils.copyProperties(p, planDto);
            //获取评审单位
            if (p.getAssistUnitList() != null && p.getAssistUnitList().size() > 0) {
                List<AssistUnitDto> unitDtoList = new ArrayList<>(p.getAssistUnitList().size());
                for (AssistUnit assistUnit : p.getAssistUnitList()) {
                    AssistUnitDto unitDto = new AssistUnitDto();
                    BeanCopierUtils.copyProperties(assistUnit, unitDto);
                    unitDtoList.add(unitDto);
                }
                planDto.setAssistUnitDtoList(unitDtoList);
            }
            //获取项目信息
            if (p.getAssistPlanSignList() != null && p.getAssistPlanSignList().size() > 0) {
                List<AssistPlanSignDto> planSignDtoList = new ArrayList<>(p.getAssistPlanSignList().size());
                for (AssistPlanSign assistPlanSign : p.getAssistPlanSignList()) {
                    AssistPlanSignDto planSignDto = new AssistPlanSignDto();
                    BeanCopierUtils.copyProperties(assistPlanSign, planSignDto);
                    planSignDtoList.add(planSignDto);
                }
                planDto.setAssistPlanSignDtoList(planSignDtoList);
            }
        }
        return planDto;
    }

    @Override
    @Transactional
    public void delete(String id) {
        //1、更新项目协审状态
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE cs_sign set isassistproc = '0' where signid in ( ");
        sqlBuilder.append(" SELECT distinct signid FROM cs_as_plansign WHERE planid = :planid)");
        sqlBuilder.setParam("planid", id);
        signRepo.executeSql(sqlBuilder);

        //2、先删除协审项目信息
        AssistPlan assistPlan = assistPlanRepo.findById(id);
        if(assistPlan != null){
            assistPlanRepo.delete(assistPlan);      //删除协审计划，同时级联删除协审项目
        }
    }

    /**
     * 初始化协审页面参数
     *
     * @return
     */
    @Override
    public Map<String, Object> initPlanManager(String isOnlySign) {
        Map<String, Object> resultMap = new HashMap<String, Object>(2);
        //1、待选择的协审项目
        resultMap.put("signList", signService.findAssistSign());

        //如果还要查询计划包信息
        if(Constant.EnumState.NO.getValue().equals(isOnlySign)){
            //2、正在处理的协审计划包
            Criteria criteria = assistPlanRepo.getExecutableCriteria();
            criteria.add(Restrictions.eq(AssistPlan_.planState.getName(), Constant.EnumState.PROCESS.getValue()));
            List<AssistPlan> planList = criteria.list();
            List<AssistPlanDto> dtoList = new ArrayList<>(planList == null ? 0 : planList.size());
            planList.forEach(p -> {
                AssistPlanDto planDto = new AssistPlanDto();
                BeanCopierUtils.copyProperties(p, planDto);
                //这里不用查询这么细的数据（2017-11-28）
            /*//获取评审单位
            if (Validate.isList(p.getAssistUnitList())) {
                List<AssistUnitDto> unitDtoList = new ArrayList<>(p.getAssistUnitList().size());
                for (AssistUnit assistUnit : p.getAssistUnitList()) {
                    AssistUnitDto unitDto = new AssistUnitDto();
                    BeanCopierUtils.copyProperties(assistUnit, unitDto);
                    unitDtoList.add(unitDto);
                }
                planDto.setAssistUnitDtoList(unitDtoList);
            }
            //获取项目信息
            if (Validate.isList(p.getAssistPlanSignList())) {
                List<AssistPlanSignDto> planSignDtoList = new ArrayList<>(p.getAssistPlanSignList().size());
                for (AssistPlanSign assistPlanSign : p.getAssistPlanSignList()) {
                    AssistPlanSignDto planSignDto = new AssistPlanSignDto();
                    BeanCopierUtils.copyProperties(assistPlanSign, planSignDto);
                    planSignDtoList.add(planSignDto);
                }
                planDto.setAssistPlanSignDtoList(planSignDtoList);
            }*/

                dtoList.add(planDto);
            });
            resultMap.put("planList", dtoList);
        }
        return resultMap;
    }

    /**
     * 取消挑选
     *
     * @param planId
     * @param signIds
     */
    @Override
    @Transactional
    public void cancelPlanSign(String planId, String signIds, boolean isMain) {
        StringBuffer updateSignId = new StringBuffer();
        StringBuffer removeId = new StringBuffer();
        List<AssistPlanSign> planSignList = assistPlanSignRepo.findByIds("planId",planId,null);
        if(isMain){
            //删除所有
            planSignList.forEach(sl ->{
                updateSignId.append(sl.getSignId()+",");
                removeId.append(sl.getId()+",");
            });
        }else{
            //删除部分（删除次项目用）
            List<String> removeSignIdList = StringUtil.getSplit(signIds,",");
            planSignList.forEach(sl ->{
                for(String signId:removeSignIdList){
                    if(signId.equals(sl.getSignId())){
                        updateSignId.append(sl.getSignId()+",");
                        removeId.append(sl.getId()+",");
                    }
                }
            });
        }
        assistPlanSignRepo.deleteById(AssistPlanSign_.id.getName(),removeId.toString());
        //更新项目协审状态
        signService.updateAssistState(updateSignId.toString(), Constant.EnumState.NO.getValue());
    }

    /**
     * 保存次项目信息
     *
     * @param assistPlanDto
     */
    @Override
    public void saveLowPlanSign(AssistPlanDto assistPlanDto) {
        AssistPlan assistPlan = new AssistPlan();
        StringBuffer signIds = new StringBuffer();
        BeanCopierUtils.copyProperties(assistPlanDto, assistPlan);
        if (Validate.isString(assistPlan.getId())) {
            BeanCopierUtils.copyProperties(assistPlanDto, assistPlan);
            List<AssistPlanSign> saveList = new ArrayList<>(assistPlanDto.getAssistPlanSignDtoList() == null ? 0 : assistPlanDto.getAssistPlanSignDtoList().size());
            if (assistPlanDto.getAssistPlanSignDtoList() != null && assistPlanDto.getAssistPlanSignDtoList().size() > 0) {
                for (int i = 0, l = assistPlanDto.getAssistPlanSignDtoList().size(); i < l; i++) {
                    AssistPlanSignDto apDto = assistPlanDto.getAssistPlanSignDtoList().get(i);
                    AssistPlanSign assistPlanSign = new AssistPlanSign();
                    BeanCopierUtils.copyProperties(apDto, assistPlanSign);
                    assistPlanSign.setAssistPlan(assistPlan);
                    saveList.add(assistPlanSign);
                    //拼接项目ID
                    signIds.append(assistPlanSign.getSignId() + ",");
                }
            }
            if (saveList.size() > 0) {
                assistPlanSignRepo.bathUpdate(saveList);
            }
        }

        //更新项目协审状态
        signService.updateAssistState(signIds.toString(), Constant.EnumState.YES.getValue());
    }

    /**
     * 根据项目ID查找协审计划信息
     *
     * @param signId
     * @return
     */
    @Override
    public AssistPlanDto getAssistPlanBySignId(String signId) {
        AssistPlanDto asPlanDto = null;
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("SELECT * FROM cs_as_plan WHERE id = ( ");
        sqlBuilder.append(" SELECT distinct planid FROM cs_as_plansign WHERE signid = :signid ) ");
        sqlBuilder.setParam("signid", signId);
        List<AssistPlan> assistPlanList = assistPlanRepo.findBySql(sqlBuilder);
        if (assistPlanList != null) {
            AssistPlan assistPlan = assistPlanList.get(0);
            asPlanDto = new AssistPlanDto();
            BeanCopierUtils.copyProperties(assistPlan, asPlanDto);
        }
        return asPlanDto;
    }

    /**
     * 保存抽签结果
     *
     * @param planId
     * @param drawAssitUnitIds
     * @param unSelectedIds
     */
    @Override
    @Transactional
    public ResultMsg saveDrawAssistUnit(String planId, String drawAssitUnitIds, String unSelectedIds) {
        AssistPlan assistPlan = assistPlanRepo.findById(planId);
        if (assistPlan == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该协审计划不存在");
        }
        String isDraw = assistPlan.getIsDrawed();
        if (isDraw != null && Constant.EnumState.YES.getValue().equals(assistPlan.getIsDrawed())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该协审计划已经进行了抽签，不能重复抽签！");
        }

        //1、获取协审项目ID和对应的协审单位ID
        List<String> planSignAndUnitIds = StringUtil.getSplit(drawAssitUnitIds, ",");
        if (Validate.isList(planSignAndUnitIds)) {
            for (int i = 0, l = planSignAndUnitIds.size(); i < l; i++) {
                String[] planSignAndUnitId = planSignAndUnitIds.get(i).split("\\|");
                if (planSignAndUnitId.length != 2) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "协审项目抽签数据格式不正确,请联系管理员进行处理!");
                }
                String planSignId = planSignAndUnitId[0];
                String assistUnitId = planSignAndUnitId[1];

                AssistPlanSign assistPlanSign = assistPlanSignRepo.findById(planSignId);
                assistPlanSign.getAssistPlan().getId();
                AssistUnit assistUnit = assistUnitRepo.findById(assistUnitId);
                Integer drawCount = assistUnit.getDrawCount() == null ? 1 : assistUnit.getDrawCount() + 1;
                assistUnit.setDrawCount(drawCount);
                assistUnit.setIsLastUnSelected(Constant.EnumState.NO.getValue());
                assistPlanSign.setAssistUnit(assistUnit);
                assistPlanSignRepo.save(assistPlanSign);
            }

            //轮空的单位
            if (Validate.isString(unSelectedIds)) {
                String[] unSelectedAssistUnitIds = unSelectedIds.split(",");
                for (int i = 0; i < unSelectedAssistUnitIds.length; i++) {
                    AssistUnit assistUnit = assistUnitRepo.findById(unSelectedAssistUnitIds[i]);
                    if (assistUnit != null) {
                        assistUnit.setIsLastUnSelected(Constant.EnumState.YES.getValue());
                        assistUnitRepo.save(assistUnit);
                    }
                }
            }

            assistPlan.setIsDrawed(Constant.EnumState.YES.getValue());
            assistPlanRepo.save(assistPlan);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！");
    }

    @Override
    @Transactional
    public void updateDrawType(String id, String drawType) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + AssistPlan.class.getSimpleName() + " set " + AssistPlan_.drawType.getName() + "=:drawType where " + AssistPlan_.id.getName() + "=:id");
        hqlBuilder.setParam("drawType", drawType);
        hqlBuilder.setParam("id", id);
        assistPlanRepo.executeHql(hqlBuilder);

    }

    /**
     * 保存手动添加协审单位信息
     *
     * @param planId
     * @param unitId
     */
    @Override
    @Transactional
    public ResultMsg addAssistUnit(String planId, String unitId) {
        try{
            AssistUnitDto assistUnitDto = new AssistUnitDto();
            AssistPlan assistPlan = assistPlanRepo.findById(planId);
            AssistUnit assistUnit = assistUnitRepo.findById(unitId);
            List<AssistUnit> assistUnitList = assistPlan.getAssistUnitList();
            assistUnitList.add(assistUnit);
            assistPlan.setAssistUnitList(assistUnitList);
            assistPlanRepo.save(assistPlan);
            BeanCopierUtils.copyProperties(assistUnit,assistUnitDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"添加成功！",assistUnitDto);
        }catch (Exception e){
            log.error("手动添加协审抽签单位异常："+e.getMessage());
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，错误信息已记录，请联系管理员查看！");
        }
    }

    @Override
    @Transactional
    public List<AssistUnitDto> getAssistUnit(String planId) {
        List<AssistUnit> assitUnitList = assistPlanRepo.findById(planId).getAssistUnitList();
        List<AssistUnitDto> assitUnitDtoList = new ArrayList<>();
        for (AssistUnit assistUnit : assitUnitList) {
            AssistUnitDto assistUnitDto = new AssistUnitDto();
            BeanCopierUtils.copyProperties(assistUnit, assistUnitDto);
            assitUnitDtoList.add(assistUnitDto);

        }
        return assitUnitDtoList;
    }

    /**
     * 保存计划信息和协审信息
     *
     * @param record
     * @return
     */
    @Override
    @Transactional
    public ResultMsg savePlanAndSign(AssistPlanDto record) {
        AssistPlan assistPlan = assistPlanRepo.findById(AssistPlan_.id.getName() , record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, assistPlan);
        List<AssistUnit> assistUnitList = new ArrayList<>();
        if(record.getAssistUnitDtoList()!=null && record.getAssistUnitDtoList().size() > 0){
            for(AssistUnitDto assistUnitDto : record.getAssistUnitDtoList()){
                AssistUnit assistUnit = new AssistUnit();
                BeanCopierUtils.copyPropertiesIgnoreNull(assistUnitDto , assistUnit);
                assistUnitList.add(assistUnit);
            }
            assistPlan.setAssistUnitList(assistUnitList);

        }
        assistPlanRepo.save(assistPlan);
        if (Validate.isList(record.getAssistPlanSignDtoList())) {
            List<AssistPlanSign> planSignList = new ArrayList<>();
            for (AssistPlanSign assistPlanSign :assistPlan.getAssistPlanSignList() ) {
                for (AssistPlanSignDto assistPlanSignDto : record.getAssistPlanSignDtoList()) {
                    if (assistPlanSign.getId().equals(assistPlanSignDto.getId())) {
                        BeanCopierUtils.copyPropertiesIgnoreNull(assistPlanSignDto, assistPlanSign);
                        assistPlanSign.setAssistPlan(assistPlan);
                        planSignList.add(assistPlanSign);
                    }
                }
            }
            //assistPlan.setAssistPlanSignList(planSignList);
            assistPlanSignRepo.bathUpdate(planSignList);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 获取最大的项目名称号
     *
     * @param date
     * @return
     */
    private int findMaxPlanName(Date date) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select max(to_number(substr(" + AssistPlan_.planName.getName() + ",9))) from cs_as_plan ");
        sqlBuilder.append(" where " + AssistPlan_.planName.getName() + " like :cdate " );
        sqlBuilder.setParam("cdate", "%" + DateUtils.converToString(date, "yyyyMMdd") + "%");
        return assistPlanRepo.returnIntBySql(sqlBuilder);
    }
}