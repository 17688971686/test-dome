package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.constants.SysConstants;
import cs.common.utils.SessionUtil;
import cs.domain.project.AssistPlan;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.service.sys.SysConfigService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnit_;
import cs.model.PageModelDto;
import cs.model.project.AssistUnitDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AssistUnitRepo;

/**
 * Description: 协审单位 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
@Service
public class AssistUnitServiceImpl implements AssistUnitService {

    private static Logger logger = Logger.getLogger(AssistUnitServiceImpl.class);


    @Autowired
    private AssistUnitRepo assistUnitRepo;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private AssistPlanRepo assistPlanRepo;

    @Override
    public PageModelDto<AssistUnitDto> get(ODataObj odataObj) {
        PageModelDto<AssistUnitDto> pageModelDto = new PageModelDto<AssistUnitDto>();
        List<AssistUnit> resultList = assistUnitRepo.findByOdata(odataObj);
        List<AssistUnitDto> resultDtoList = new ArrayList<>();
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                AssistUnitDto modelDto = new AssistUnitDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AssistUnitDto record) {
        boolean isUnitExist = assistUnitRepo.isUnitExist(record.getUnitName());
        //单位不存在
        if (!isUnitExist) {
            AssistUnit domain = new AssistUnit();
            BeanCopierUtils.copyProperties(record, domain);
            domain.setId(UUID.randomUUID().toString());
            domain.setIsUse("1");
            int unitSort = assistUnitRepo.getUnitSortMax();
            domain.setUnitSort(++unitSort);
            Date now = new Date();
            domain.setCreatedBy(SessionUtil.getLoginName());
            domain.setModifiedBy(SessionUtil.getLoginName());
            domain.setCreatedDate(now);
            domain.setModifiedDate(now);
            assistUnitRepo.save(domain);
        } else {
            throw new IllegalArgumentException(String.format("单位名：%s 已经存在,请重新输入！", record.getUnitName()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AssistUnitDto record) {
        AssistUnit domain = assistUnitRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setModifiedDate(new Date());
        domain.getAssistPlanList().clear();
        assistUnitRepo.save(domain);
    }

    @Override
    public AssistUnitDto findById(String id) {
        AssistUnitDto modelDto = new AssistUnitDto();
        if (Validate.isString(id)) {
            AssistUnit domain = assistUnitRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
        }
        return modelDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + AssistUnit.class.getSimpleName() + " set " + AssistUnit_.isUse.getName() + "='0' ");
        hqlBuilder.bulidPropotyString("where",AssistUnit_.id.getName(),ids);
        assistUnitRepo.executeHql(hqlBuilder);
    }

    /**
     * 查询抽签单位
     *
     * @param planId
     * @param number
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AssistUnitDto> findDrawUnit(String planId, Integer number,String drawType) {
        List<AssistUnitDto> resultList = new ArrayList<>();
        List<AssistUnit> saveList = new ArrayList<>();
        SysConfigDto sysConfig = new SysConfigDto();
        Integer maxSort = 0;
        //1、先查询轮空的单位
        AssistUnitDto assistUnitDto = new AssistUnitDto();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + AssistUnit.class.getSimpleName() + " where " + AssistUnit_.isLastUnSelected.getName() + " =:isLastUnSelected");
        hqlBuilder.append(" and  " + AssistUnit_.isUse.getName() + "='1'");
        hqlBuilder.setParam("isLastUnSelected", Constant.EnumState.YES.getValue());
        List<AssistUnit> list = assistUnitRepo.findByHql(hqlBuilder);
        if (list == null || list.size() == 0) {
        } else {
            BeanCopierUtils.copyProperties(list.get(0), assistUnitDto);
            saveList.add(list.get(0));
            resultList.add(assistUnitDto);
            number = number - 1;
        }

        /**
         * 如果已满足条件，则不用查询
         */
        if (number > 0) {
            //2、取出前一次抽签的最大序号
            String configKey = SysConstants.SYS_CONFIG_ENUM.LAST_UNIT_MAXSORT.toString();
            sysConfig = sysConfigService.findByDataKey(configKey);
            if (sysConfig == null || !Validate.isString(sysConfig.getId())) {
                sysConfig = new SysConfigDto();
                sysConfig.setConfigName(Constant.DRAW_ASSIST_UNITNAME);
                sysConfig.setConfigKey(configKey);
                sysConfig.setConfigValue(String.valueOf(number));
                sysConfig.setIsShow(Constant.EnumState.NO.getValue());
            } else {
                maxSort = Integer.valueOf(sysConfig.getConfigValue());
            }

            //3、按照序号轮流抽签
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" SELECT nUnit.* FROM ( ");
            sqlBuilder.append(" SELECT cu.*,CASE WHEN  unitsort < :maxSort then (10000+unitsort) else unitsort end newSort ");
            sqlBuilder.setParam("maxSort", (maxSort + 1));
            sqlBuilder.append(" FROM CS_AS_UNIT cu ");
            if (Validate.isString(assistUnitDto.getId())) {
                sqlBuilder.append(" WHERE id != :id ORDER BY newSort ");
                sqlBuilder.setParam("id", assistUnitDto.getId());
            }
            sqlBuilder.append(" ) nUnit WHERE ROWNUM < :number ");
            sqlBuilder.setParam("number", number + 1);
            hqlBuilder.append(" " + AssistUnit_.isUse.getName() + "= '1' ");
            list = assistUnitRepo.findBySql(sqlBuilder);
            saveList.addAll(list);
            if (Validate.isList(list)) {
                list.forEach(l -> {
                    AssistUnitDto unitDto = new AssistUnitDto();
                    BeanCopierUtils.copyProperties(l, unitDto);
                    resultList.add(unitDto);
                });
            }
        }

        //4、添加关联和更新最大值缓存
        if (Validate.isList(saveList)) {
            AssistPlan assistPlan = assistPlanRepo.findById(planId);
            if (assistPlan != null) {
                assistPlan.setAssistUnitList(saveList);
            }
            //设定为抽签方式
            assistPlan.setDrawType(drawType);
            assistPlanRepo.save(assistPlan);

            sysConfig.setConfigValue(String.valueOf(saveList.get(saveList.size() - 1).getUnitSort()));
            sysConfigService.save(sysConfig);
            //刷新缓存
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AssistUnitDto> getAssistUnitByPlanId(String planId) {
        List<AssistUnit> assistUnitList = assistUnitRepo.getAssistUnitByPlanId(planId);
        List<AssistUnitDto> assistUnitDtoList = new ArrayList<>();
        for (AssistUnit assistUnit : assistUnitList) {
            AssistUnitDto assistUnitDto = new AssistUnitDto();
            BeanCopierUtils.copyProperties(assistUnit, assistUnitDto);
            assistUnitDtoList.add(assistUnitDto);
        }
        return assistUnitDtoList;
    }

}