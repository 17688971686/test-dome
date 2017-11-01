package cs.repository.repositoryImpl.sys;

import cs.domain.sys.SysConfig;
import cs.repository.IRepository;

/**
 * Description: 系统参数 数据操作实现接口
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
public interface SysConfigRepo extends IRepository<SysConfig, String> {

    /**
     * 通过配置的key值判断是否已经存在，确认key值唯一性
     * @param configKey
     * @return
     */
    Boolean existByKey(String configKey);

    /**
     * 通过参数名获取对象
     * @param configName
     * @return
     */
    SysConfig findByConfigName(String configName);

}
