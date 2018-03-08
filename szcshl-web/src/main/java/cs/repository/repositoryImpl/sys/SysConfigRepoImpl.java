package cs.repository.repositoryImpl.sys;

import cs.domain.sys.SysConfig;
import cs.domain.sys.SysConfig_;
import cs.model.sys.SysConfigDto;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 系统参数 数据操作实现类
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
@Repository
public class SysConfigRepoImpl extends AbstractRepository<SysConfig, String> implements SysConfigRepo {

    /**
     * 通过配置的key值判断是否已经存在 ，确认key值唯一性
     * @param configKey
     * @return
     */
    @Override
    public Boolean existByKey(String configKey) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SysConfig_.configKey.getName() , configKey));
        List<SysConfig> sysConfigList = criteria.list();

        return !sysConfigList.isEmpty();
    }

    /**
     * 通过参数名获取对象
     * @param configName
     * @return
     */
    @Override
    public SysConfig findByConfigName(String configName) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SysConfig_.configName.getName() , configName));
        List<SysConfig> sysConfigList = criteria.list();
        if(sysConfigList!=null && sysConfigList.size()>0){
            return sysConfigList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 根据key值从数据库查询数据
     * @param key
     * @return
     */
    @Override
    public SysConfig findByDataKey(String key) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SysConfig_.configKey.getName() , key));
        SysConfig sysConfig = (SysConfig) criteria.uniqueResult();
        return sysConfig;
    }
}