package cs.service.sys;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Quartz;
import cs.domain.sys.Quartz_;
import cs.model.PageModelDto;
import cs.model.sys.QuartzDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.QuartzRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 定时器配置 业务操作实现类
 * author: ldm
 * Date: 2017-6-20 10:47:42
 */
@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private QuartzRepo quartzRepo;

    @Override
    public PageModelDto<QuartzDto> get(ODataObj odataObj) {
        PageModelDto<QuartzDto> pageModelDto = new PageModelDto<QuartzDto>();
        List<Quartz> resultList = quartzRepo.findByOdata(odataObj);
        List<QuartzDto> resultDtoList = new ArrayList<QuartzDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
            resultList.forEach(x -> {
                QuartzDto modelDto = new QuartzDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                //cannot copy
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
    public void save(QuartzDto record) {
        Quartz domain = new Quartz();
        BeanCopierUtils.copyProperties(record, domain);
        Date now = new Date();
        domain.setCreatedBy(SessionUtil.getLoginName());
        domain.setModifiedBy(SessionUtil.getLoginName());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        //默认自动执行
        if(!Validate.isString(domain.getRunWay())){
            domain.setRunWay(Constant.EnumState.YES.getValue());
        }
        domain.setCurState("0"); //默认未执行
        domain.setIsEnable("9"); //默认在用
        quartzRepo.save(domain);
    }

    @Override
    @Transactional
    public void update(QuartzDto record) {
        Quartz domain = quartzRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getLoginName());
        domain.setModifiedDate(new Date());

        quartzRepo.save(domain);
    }

    @Override
    public QuartzDto findById(String id) {
        QuartzDto modelDto = new QuartzDto();
        if (Validate.isString(id)) {
            Quartz domain = quartzRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
        }
        return modelDto;
    }

    @Override
    @Transactional
    public void delete(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + Quartz.class.getName() + " set " + Quartz_.isEnable.getName() + "='0' where " + Quartz_.id.getName() + "=:id");
        hqlBuilder.setParam("id", id);
        quartzRepo.executeHql(hqlBuilder);

    }

    @Override
    @Transactional
    public void changeCurState(String id, String state) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + Quartz.class.getName() + " set " + Quartz_.curState.getName() + "=:state where " + Quartz_.id.getName() + "=:id");
        hqlBuilder.setParam("id", id);
        hqlBuilder.setParam("state", state);
        quartzRepo.executeHql(hqlBuilder);

    }

    @Override
    public List<Quartz> findDefaultQuartz() {

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select q from " + Quartz.class.getSimpleName() + " q where q." + Quartz_.runWay.getName() + "=:runWay");
        hqlBuilder.setParam("runWay", Constant.EnumState.YES.getValue());
        List<Quartz> quartzList = quartzRepo.findByHql(hqlBuilder);

        return quartzList;
    }

}