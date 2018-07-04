package cs.service.sys;

import cs.common.ResultMsg;
import cs.common.cache.CacheFactory;
import cs.common.cache.DefaultCacheFactory;
import cs.common.cache.ICache;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
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
import java.util.Date;
import java.util.List;

/**
 * Description: 系统参数 业务操作实现类
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigRepo sysConfigRepo;

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
    public ResultMsg save(SysConfigDto record) {
        SysConfig domain = new SysConfig();

        Date now = new Date();
        Boolean excitKey = false ;
        if(!Validate.isString(record.getId())){
            excitKey = sysConfigRepo.existByKey(record.getConfigKey());
        }

        //判断新增的key值是否已经存在
        if(!excitKey){
            if (!Validate.isString(record.getId())) {
                record.setCreatedBy(SessionUtil.getLoginName());
                record.setCreatedDate(now);
            }

            if(!Validate.isString(record.getIsShow())){
                record.setIsShow(Constant.EnumState.YES.getValue());
            }
            record.setModifiedBy(SessionUtil.getLoginName());
            record.setModifiedDate(now);

            BeanCopierUtils.copyProperties(record, domain);
            sysConfigRepo.save(domain);

            //清除缓存
            CacheFactory cacheFactory = new DefaultCacheFactory();
            ICache cache = cacheFactory.getCache();
            cache.clear(Constant.EnumConfigKey.CONFIG_LIST.getValue());

            return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "添加数据成功！" , null);
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "配置key："+record.getConfigKey() + "已经存在，请重新输入！" , null);
        }

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
        if (resultList.size() == 0) {
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
        List<SysConfigDto> resultList = queryAll();
        if (resultList != null && resultList.size() >= 0) {
            for (SysConfigDto sc : resultList) {
                if (sc.getConfigKey() != null && sc.getConfigKey().equals(key)) {
                    modelDto = sc;
                    break;
                }
            }
        }
        return modelDto;
    }

    /**
     * 根据key值从数据库查询
     * @param key
     * @return
     */
    @Override
    public SysConfigDto findByDataKey(String key) {
        SysConfigDto sysConfigDto = new SysConfigDto();
        SysConfig sysConfig = sysConfigRepo.findByDataKey(key);
        if(sysConfig != null && Validate.isString(sysConfig.getConfigValue())){
            BeanCopierUtils.copyProperties(sysConfig,sysConfigDto);
        }
        return sysConfigDto;
    }

}