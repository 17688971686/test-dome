package cs.service.sys;

import cs.common.Constant;
import cs.common.ICurrentUser;
import cs.common.cache.CacheFactory;
import cs.common.cache.DefaultCacheFactory;
import cs.common.cache.ICache;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sys.SysConfig;
import cs.domain.sys.SysConfig_;
import cs.model.PageModelDto;
import cs.model.sys.SysConfigDto;
import cs.repository.odata.ODataObj;

import cs.repository.repositoryImpl.sys.SysConfigRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 系统参数 业务操作实现类
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigRepo sysConfigRepo;
    @Autowired
    private ICurrentUser currentUser;

    @Override
    public PageModelDto<SysConfigDto> get(ODataObj odataObj) {
        PageModelDto<SysConfigDto> pageModelDto = new PageModelDto<SysConfigDto>();
        List<SysConfig> resultList = sysConfigRepo.findByOdata(odataObj);
        List<SysConfigDto> resultDtoList = new ArrayList<SysConfigDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
            resultList.forEach(x -> {
                SysConfigDto modelDto = new SysConfigDto();
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
    public void save(SysConfigDto record) {
        SysConfig domain = new SysConfig();

        Date now = new Date();
        if (!Validate.isString(record.getId())) {
            record.setCreatedBy(currentUser.getLoginName());
            record.setCreatedDate(now);
        }

        if(!Validate.isString(record.getIsShow())){
            record.setIsShow(Constant.EnumState.YES.getValue());
        }
        record.setModifiedBy(currentUser.getLoginName());
        record.setModifiedDate(now);

        BeanCopierUtils.copyProperties(record, domain);
        sysConfigRepo.save(domain);

        //清除缓存
        CacheFactory cacheFactory = new DefaultCacheFactory();
        ICache cache = cacheFactory.getCache();
        cache.clear(Constant.EnumConfigKey.CONFIG_LIST.getValue());
    }

    @Override
    public SysConfigDto findById(String id) {
        SysConfigDto modelDto = new SysConfigDto();
        CacheFactory cacheFactory = new DefaultCacheFactory();
        ICache cache = cacheFactory.getCache();
        List<SysConfigDto> resultList = (List<SysConfigDto>) cache.get(Constant.EnumConfigKey.CONFIG_LIST.getValue());
        if (resultList == null || resultList.size() == 0) {
            resultList = queryAll();
        }
        for (SysConfigDto sc : resultList) {
            if (sc.getId().equals(id)) {
                modelDto = sc;
                break;
            }
        }
        return modelDto;
    }

    @Override
    @Transactional
    public void delete(String id) {
        sysConfigRepo.deleteById(SysConfig_.id.getName(), id);
        //清除缓存
        CacheFactory cacheFactory = new DefaultCacheFactory();
        ICache cache = cacheFactory.getCache();
        cache.clear(Constant.EnumConfigKey.CONFIG_LIST.getValue());
    }

    /**
     * 查询所有显示的系统参数
     *
     * @return
     */
    @Override
    public List<SysConfigDto> queryAll() {
        CacheFactory cacheFactory = new DefaultCacheFactory();
        ICache cache = cacheFactory.getCache();
        List<SysConfigDto> resultList = (List<SysConfigDto>) cache.get(Constant.EnumConfigKey.CONFIG_LIST.getValue())
                == null ? new ArrayList<>() : (List<SysConfigDto>) cache.get(Constant.EnumConfigKey.CONFIG_LIST.getValue());
        if (resultList == null || resultList.size() == 0) {
            Criteria criteria = sysConfigRepo.getExecutableCriteria();
            criteria.add(Restrictions.eq(SysConfig_.isShow.getName(), Constant.EnumState.YES.getValue()));
            List<SysConfig> configList = criteria.list();
            if (configList != null && configList.size() > 0) {
                configList.forEach(cl -> {
                    SysConfigDto sysConfigDto = new SysConfigDto();
                    BeanCopierUtils.copyProperties(cl, sysConfigDto);
                    resultList.add(sysConfigDto);
                });
            }
            cache.put(Constant.EnumConfigKey.CONFIG_LIST.getValue(), resultList);
        }
        return resultList;
    }

    @Override
    public SysConfigDto findByKey(String key) {
        SysConfigDto modelDto = new SysConfigDto();
        CacheFactory cacheFactory = new DefaultCacheFactory();
        ICache cache = cacheFactory.getCache();
        List<SysConfigDto> resultList = (List<SysConfigDto>) cache.get(Constant.EnumConfigKey.CONFIG_LIST.getValue());
        if (resultList == null || resultList.size() == 0) {
            resultList = queryAll();
        }
        for (SysConfigDto sc : resultList) {
            if (sc.getConfigKey().equals(key)) {
                modelDto = sc;
                break;
            }
        }
        return modelDto;
    }

}