package cs.service.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.NumIncreaseUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.*;
import cs.model.PageModelDto;
import cs.model.project.AssistPlanDto;
import cs.model.project.AssistPlanSignDto;
import cs.model.project.AssistUnitDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Handler;

/**
 * Description: 协审方案 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Service
public class AssistPlanServiceImpl  implements AssistPlanService {

	@Autowired
	private AssistPlanRepo assistPlanRepo;
    @Autowired
    private AssistPlanSignRepo assistPlanSignRepo;
	@Autowired
	private ICurrentUser currentUser;
    @Autowired
    private SignService signService;

	@Override
	public PageModelDto<AssistPlanDto> get(ODataObj odataObj) {
		PageModelDto<AssistPlanDto> pageModelDto = new PageModelDto<AssistPlanDto>();
		List<AssistPlan> resultList = assistPlanRepo.findByOdata(odataObj);
		List<AssistPlanDto> resultDtoList = new ArrayList<AssistPlanDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
			resultList.forEach(x->{
				AssistPlanDto modelDto = new AssistPlanDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(AssistPlanDto record) {
        AssistPlan assistPlan = new AssistPlan();
        Date now = new Date();
        if(!Validate.isString(record.getId())){
            BeanCopierUtils.copyProperties(record,assistPlan);
            assistPlan.setId(UUID.randomUUID().toString());
            assistPlan.setPlanName(NumIncreaseUtils.getAssistPlanName());
            assistPlan.setCreatedBy(currentUser.getLoginUser().getLoginName());
            assistPlan.setCreatedDate(now);
            assistPlan.setPlanState(Constant.EnumState.PROCESS.getValue());
        }else{
            assistPlan = assistPlanRepo.findById(record.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(record,assistPlan);
        }
        assistPlan.setModifiedBy(currentUser.getLoginUser().getLoginName());
        assistPlan.setModifiedDate(now);
        assistPlanRepo.save(assistPlan);
        record.setId(assistPlan.getId());   //copy ID
        /************************  协审类型判断 **************************/
        if(record.isSingle()){//单个协审
            if(record.getSplitNum() == null){
                record.setSplitNum(1);  //默认不拆分
            }
            List<AssistPlanSign> saveList = new ArrayList<>(record.getSplitNum());
            int i=1;
            while(i <= record.getSplitNum()){
                AssistPlanSign assistPlanSign = new AssistPlanSign();
                assistPlanSign.setSignId(record.getSignId());
                assistPlanSign.setMainSignId(record.getSignId());
                assistPlanSign.setProjectName(record.getProjectName());
                assistPlanSign.setAssistType(record.getAssistType());
                assistPlanSign.setSplitNum(i);
                assistPlanSign.setAssistPlan(assistPlan);
                assistPlanSign.setIsMain(i==1? Constant.EnumState.YES.getValue():Constant.EnumState.NO.getValue()); //主要为了方便显示
                saveList.add(assistPlanSign);
                i++;
            }
            assistPlanSignRepo.bathUpdate(saveList);

        }else{  //合并协审，保存的只有主项目
            AssistPlanSign assistPlanSign = new AssistPlanSign();
            assistPlanSign.setSignId(record.getSignId());
            assistPlanSign.setMainSignId(record.getSignId());
            assistPlanSign.setProjectName(record.getProjectName());
            assistPlanSign.setAssistType(record.getAssistType());
            assistPlanSign.setAssistPlan(assistPlan);
            assistPlanSign.setIsMain(Constant.EnumState.YES.getValue());
            assistPlanSignRepo.save(assistPlanSign);
        }

	}

	@Override
	@Transactional
	public void update(AssistPlanDto record) {
		AssistPlan domain = assistPlanRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		assistPlanRepo.save(domain);
	}

	@Override
	public AssistPlanDto findById(String id) {		
		AssistPlanDto planDto = new AssistPlanDto();
		if(Validate.isString(id)){
			AssistPlan p = assistPlanRepo.findById(id);
			BeanCopierUtils.copyProperties(p, planDto);
            //获取评审单位
            if(p.getAssistUnitList() != null && p.getAssistUnitList().size() > 0){
                List<AssistUnitDto> unitDtoList = new ArrayList<>(p.getAssistUnitList().size());
                for(AssistUnit assistUnit : p.getAssistUnitList()){
                    AssistUnitDto unitDto = new AssistUnitDto();
                    BeanCopierUtils.copyProperties(assistUnit,unitDto);
                    unitDtoList.add(unitDto);
                }
                planDto.setAssistUnitDtoList(unitDtoList);
            }
            //获取项目信息
            if(p.getAssistPlanSignList() != null && p.getAssistPlanSignList().size() > 0){
                List<AssistPlanSignDto> planSignDtoList = new ArrayList<>(p.getAssistPlanSignList().size());
                for(AssistPlanSign assistPlanSign : p.getAssistPlanSignList()){
                    AssistPlanSignDto planSignDto = new AssistPlanSignDto();
                    BeanCopierUtils.copyProperties(assistPlanSign,planSignDto);
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
	    //1、先删除协审项目信息
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from " +AssistPlanSign.class.getSimpleName());
        String[] idArr = id.split(",");
        if (idArr.length > 1) {
            hqlBuilder.append( " where "+ AssistPlanSign_.assistPlan.getName()+"."+AssistPlan_.id.getName()+" in ( ");
            int totalL = idArr.length;
            for(int i=0;i<totalL;i++){
                if(i==totalL-1){
                    hqlBuilder.append(" :id"+i).setParam("id"+i, idArr[i]);
                }else{
                    hqlBuilder.append(" :id"+i+",").setParam("id"+i, idArr[i]);
                }
            }
            hqlBuilder.append(" )");
        } else {
            hqlBuilder.append( " where "+ AssistPlanSign_.assistPlan.getName()+"."+AssistPlan_.id.getName()+" = :id ");
            hqlBuilder.setParam("id", id);
        }
        assistPlanSignRepo.executeHql(hqlBuilder);
        //2、再删协审计划信息
		assistPlanRepo.deleteById(AssistPlan_.id.getName(),id);
	}

    /**
     * 初始化协审页面参数
     * @return
     */
    @Override
    public Map<String, Object> initPlanManager() {
        Map<String, Object> resultMap = new HashMap<String,Object>(2);
        //1、待选择的协审项目
        resultMap.put("signList",signService.findAssistSign());

        //2、正在处理的协审计划包
        Criteria criteria = assistPlanRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(AssistPlan_.planState.getName(), Constant.EnumState.PROCESS.getValue()));
        List<AssistPlan> planList = criteria.list();
        List<AssistPlanDto> dtoList = new ArrayList<>(planList==null?0:planList.size());
        planList.forEach(p->{
            AssistPlanDto planDto = new AssistPlanDto();
            BeanCopierUtils.copyProperties(p,planDto);
            //获取评审单位
            if(p.getAssistUnitList() != null && p.getAssistUnitList().size() > 0){
                List<AssistUnitDto> unitDtoList = new ArrayList<>(p.getAssistUnitList().size());
                for(AssistUnit assistUnit : p.getAssistUnitList()){
                    AssistUnitDto unitDto = new AssistUnitDto();
                    BeanCopierUtils.copyProperties(assistUnit,unitDto);
                    unitDtoList.add(unitDto);
                }
                planDto.setAssistUnitDtoList(unitDtoList);
            }
            //获取项目信息
            if(p.getAssistPlanSignList() != null && p.getAssistPlanSignList().size() > 0){
                List<AssistPlanSignDto> planSignDtoList = new ArrayList<>(p.getAssistPlanSignList().size());
                for(AssistPlanSign assistPlanSign : p.getAssistPlanSignList()){
                    AssistPlanSignDto planSignDto = new AssistPlanSignDto();
                    BeanCopierUtils.copyProperties(assistPlanSign,planSignDto);
                    planSignDtoList.add(planSignDto);
                }
                planDto.setAssistPlanSignDtoList(planSignDtoList);
            }

            dtoList.add(planDto);
        });
        resultMap.put("planList",dtoList);
        return resultMap;
    }

    /**
     * 取消挑选
     * @param planId
     * @param signIds
     */
    @Override
    public void cancelPlanSign(String planId, String signIds,boolean isMain) {
        boolean isHavePlanId = Validate.isString(planId)?true:false;
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from cs_as_plansign  where ");
        if(isHavePlanId){
            sqlBuilder.append(" planid =:planid  ").setParam("planid",planId).append(" and ");
        }
        List<String> signIdList = StringUtil.getSplit(signIds,",");
        if(signIdList.size() == 0){
            sqlBuilder.append("( signid = :signid ").setParam("signid",signIdList.get(0));
            if(isMain){//删除次项目
                sqlBuilder.append(" or mainsignid = :mainsignid  ").setParam("signid",signIdList.get(0));
            }
            sqlBuilder.append(" ) ");
        }else{
            sqlBuilder.append(" ( signid in ( ");
            for(int i=0,l=signIdList.size();i<l;i++){
                if(i==l-1){
                    sqlBuilder.append(" :id"+i).setParam("id"+i, signIdList.get(i));
                }else{
                    sqlBuilder.append(" :id"+i+",").setParam("id"+i, signIdList.get(i));
                }
            }
            sqlBuilder.append(" ) ");
            if(isMain){//删除次项目
                sqlBuilder.append(" or mainsignid in ( ");
                for(int i=0,l=signIdList.size();i<l;i++){
                    if(i==l-1){
                        sqlBuilder.append(" :mainsignid"+i).setParam("mainsignid"+i, signIdList.get(i));
                    }else{
                        sqlBuilder.append(" :mainsignid"+i+",").setParam("mainsignid"+i, signIdList.get(i));
                    }
                }
                sqlBuilder.append(" ) ");
            }
            sqlBuilder.append(" ) ");
        }
        assistPlanRepo.executeSql(sqlBuilder);
    }

    /**
     * 保存次项目信息
     * @param assistPlanDto
     */
    @Override
    public void saveLowPlanSign(AssistPlanDto assistPlanDto) {
        AssistPlan assistPlan = new AssistPlan();
        BeanCopierUtils.copyProperties(assistPlanDto,assistPlan);
        if(Validate.isString(assistPlan.getId())){
            BeanCopierUtils.copyProperties(assistPlanDto,assistPlan);
            List<AssistPlanSign> saveList = new ArrayList<>(assistPlanDto.getAssistPlanSignDtoList()==null?0:assistPlanDto.getAssistPlanSignDtoList().size());
            if(assistPlanDto.getAssistPlanSignDtoList() != null && assistPlanDto.getAssistPlanSignDtoList().size() > 0){
                assistPlanDto.getAssistPlanSignDtoList().forEach(apDto ->{
                    AssistPlanSign assistPlanSign = new AssistPlanSign();
                    BeanCopierUtils.copyProperties(apDto,assistPlanSign);
                    assistPlanSign.setAssistPlan(assistPlan);
                    saveList.add(assistPlanSign);
                });
            }
            if(saveList.size() > 0){
                assistPlanSignRepo.bathUpdate(saveList);
            }
        }

    }

}