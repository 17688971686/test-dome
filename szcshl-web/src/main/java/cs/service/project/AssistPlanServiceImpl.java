package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.*;
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
        List<AssistPlanDto> resultDtoList = new ArrayList<AssistPlanDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
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
     * TODO(递增序号还要实现)
     *
     * @param record
     */
    @Override
    @Transactional
    public ResultMsg save(AssistPlanDto record) {
        AssistPlan assistPlan = new AssistPlan();
        Date now = new Date();
        if (!Validate.isString(record.getId())) {
            BeanCopierUtils.copyProperties(record, assistPlan);
            assistPlan.setId(UUID.randomUUID().toString());
            int maxPlanName = findMaxPlanName(now);
            String panName = "";
            if (maxPlanName == 0) {
                panName = DateUtils.converToString(now, "yyyyMMdd") + String.format("%02d", maxPlanName + 1);
            } else {
                panName = String.valueOf(maxPlanName++);
            }
            assistPlan.setPlanName(panName);
            assistPlan.setCreatedBy(SessionUtil.getDisplayName());
            assistPlan.setCreatedDate(now);
            assistPlan.setPlanState(Constant.EnumState.PROCESS.getValue());
        } else {
            assistPlan = assistPlanRepo.findById(record.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(record, assistPlan);
        }
        assistPlan.setModifiedBy(SessionUtil.getDisplayName());
        assistPlan.setModifiedDate(now);
        assistPlanRepo.save(assistPlan);
        record.setId(assistPlan.getId());   //copy ID
        /************************  协审类型判断 **************************/
        if (record.isSingle()) {//单个协审
            if (record.getSplitNum() == null) {
                record.setSplitNum(1);  //默认不拆分
            }
            List<AssistPlanSign> saveList = new ArrayList<>(record.getSplitNum());
            int i = 1;
            while (i <= record.getSplitNum()) {
                AssistPlanSign assistPlanSign = new AssistPlanSign();
                assistPlanSign.setSignId(record.getSignId());
                assistPlanSign.setMainSignId(record.getSignId());
                assistPlanSign.setProjectName(record.getProjectName());
                assistPlanSign.setAssistType(record.getAssistType());
                assistPlanSign.setSplitNum(i);
                assistPlanSign.setAssistPlan(assistPlan);
                assistPlanSign.setIsMain(i == 1 ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue()); //主要为了方便显示
                saveList.add(assistPlanSign);
                i++;
            }
            assistPlanSignRepo.bathUpdate(saveList);

        } else {  //合并协审，保存的只有主项目
            AssistPlanSign assistPlanSign = new AssistPlanSign();
            assistPlanSign.setSignId(record.getSignId());
            assistPlanSign.setMainSignId(record.getSignId());
            assistPlanSign.setProjectName(record.getProjectName());
            assistPlanSign.setAssistType(record.getAssistType());
            assistPlanSign.setAssistPlan(assistPlan);
            assistPlanSign.setIsMain(Constant.EnumState.YES.getValue());
            assistPlanSignRepo.save(assistPlanSign);
        }

        //更新项目协审状态
        signService.updateAssistState(record.getSignId(), Constant.EnumState.YES.getValue());

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！", record);
    }

    @Override
    @Transactional
    public void update(AssistPlanDto record) {
        AssistPlan domain = assistPlanRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getLoginName());
        domain.setModifiedDate(new Date());

        assistPlanRepo.save(domain);
    }

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
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from " + AssistPlanSign.class.getSimpleName());
        String[] idArr = id.split(",");
        if (idArr.length > 1) {
            hqlBuilder.append(" where " + AssistPlanSign_.assistPlan.getName() + "." + AssistPlan_.id.getName() + " in ( ");
            int totalL = idArr.length;
            for (int i = 0; i < totalL; i++) {
                if (i == totalL - 1) {
                    hqlBuilder.append(" :id" + i).setParam("id" + i, idArr[i]);
                } else {
                    hqlBuilder.append(" :id" + i + ",").setParam("id" + i, idArr[i]);
                }
            }
            hqlBuilder.append(" )");
        } else {
            hqlBuilder.append(" where " + AssistPlanSign_.assistPlan.getName() + "." + AssistPlan_.id.getName() + " = :id ");
            hqlBuilder.setParam("id", id);
        }
        assistPlanSignRepo.executeHql(hqlBuilder);
        //3、再删协审计划信息
        assistPlanRepo.deleteById(AssistPlan_.id.getName(), id);
    }

    /**
     * 初始化协审页面参数
     *
     * @return
     */
    @Override
    public Map<String, Object> initPlanManager() {
        Map<String, Object> resultMap = new HashMap<String, Object>(2);
        //1、待选择的协审项目
        resultMap.put("signList", signService.findAssistSign());

        //2、正在处理的协审计划包
        Criteria criteria = assistPlanRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(AssistPlan_.planState.getName(), Constant.EnumState.PROCESS.getValue()));
        List<AssistPlan> planList = criteria.list();
        List<AssistPlanDto> dtoList = new ArrayList<>(planList == null ? 0 : planList.size());
        planList.forEach(p -> {
            AssistPlanDto planDto = new AssistPlanDto();
            BeanCopierUtils.copyProperties(p, planDto);
            //获取评审单位
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
            }

            dtoList.add(planDto);
        });
        resultMap.put("planList", dtoList);
        return resultMap;
    }

    /**
     * 取消挑选
     *
     * @param planId
     * @param signIds
     */
    @Override
    public void cancelPlanSign(String planId, String signIds, boolean isMain) {
        boolean isHavePlanId = Validate.isString(planId) ? true : false;
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from cs_as_plansign  where ");
        if (isHavePlanId) {
            sqlBuilder.append(" planid =:planid  ").setParam("planid", planId).append(" and ");
        }
        List<String> signIdList = StringUtil.getSplit(signIds, ",");
        if (signIdList.size() == 0) {
            sqlBuilder.append("( signid = :signid ").setParam("signid", signIdList.get(0));
            if (isMain) {//删除次项目
                sqlBuilder.append(" or mainsignid = :mainsignid  ").setParam("signid", signIdList.get(0));
            }
            sqlBuilder.append(" ) ");
        } else {
            sqlBuilder.append(" ( signid in ( ");
            for (int i = 0, l = signIdList.size(); i < l; i++) {
                if (i == l - 1) {
                    sqlBuilder.append(" :id" + i).setParam("id" + i, signIdList.get(i));
                } else {
                    sqlBuilder.append(" :id" + i + ",").setParam("id" + i, signIdList.get(i));
                }
            }
            sqlBuilder.append(" ) ");
            if (isMain) {//删除次项目
                sqlBuilder.append(" or mainsignid in ( ");
                for (int i = 0, l = signIdList.size(); i < l; i++) {
                    if (i == l - 1) {
                        sqlBuilder.append(" :mainsignid" + i).setParam("mainsignid" + i, signIdList.get(i));
                    } else {
                        sqlBuilder.append(" :mainsignid" + i + ",").setParam("mainsignid" + i, signIdList.get(i));
                    }
                }
                sqlBuilder.append(" ) ");
            }
            sqlBuilder.append(" ) ");
        }
        assistPlanRepo.executeSql(sqlBuilder);

        //更新项目协审状态
        signService.updateAssistState(signIds, Constant.EnumState.NO.getValue());
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
    public void addAssistUnit(String planId, String unitId) {
        AssistPlan assistPlan = assistPlanRepo.findById(planId);
        AssistUnit assistUnit = assistUnitRepo.findById(unitId);
        List<AssistUnit> assistUnitList = assistPlan.getAssistUnitList();
        assistUnitList.add(assistUnit);
        assistPlan.setAssistUnitList(assistUnitList);
        assistPlanRepo.save(assistPlan);
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
        AssistPlan assistPlan = assistPlanRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, assistPlan);
        assistPlanRepo.save(assistPlan);
        if (Validate.isList(record.getAssistPlanSignDtoList())) {
            for (AssistPlanSign assistPlanSign : assistPlan.getAssistPlanSignList()) {
                for (AssistPlanSignDto assistPlanSignDto : record.getAssistPlanSignDtoList()) {
                    if (assistPlanSign.getId().equals(assistPlanSignDto.getId())) {
                        BeanCopierUtils.copyPropertiesIgnoreNull(assistPlanSignDto, assistPlanSign);
                        assistPlanSignRepo.save(assistPlanSign);
                    }
                }
            }
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
        sqlBuilder.append("select max(to_number(" + AssistPlan_.planName.getName() + ")) from cs_as_plan where " + AssistPlan_.planName.getName() + " like :cdate ");
        sqlBuilder.setParam("cdate", "%" + DateUtils.converToString(date, "yyyyMMdd") + "%");
        return assistPlanRepo.returnIntBySql(sqlBuilder);
    }
}