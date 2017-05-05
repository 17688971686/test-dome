package cs.common.service;

import cs.model.BaseDto2;
import cs.model.PageModelDto;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * Description: 公共业务实现类
 * User: tzg
 * Date: 2017/5/4 17:40
 *
 * @param <T> 数据库映射实体类
 * @param <D> 页面数据映射实体
 * @param <R> 数据库操作接口
 */
public abstract class AbstractServiceImpl<T, D extends BaseDto2<T>, R extends IRepository<T, String>> implements IService<T, D> {

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    protected R baseRepo;

    @Override
    @Transactional
    public void save(D record) {
        baseRepo.save(record.getDomain());
    }

    @Override
    @Transactional
    public void delete(String id) {
        T record = baseRepo.findById(id);
        if (record != null) {
            baseRepo.delete(record);
        }
    }

    @Override
    @Transactional
    public void delete(String[] ids) {
        for (String id : ids) {
            delete(id);
        }
    }

    @Override
    public T findById(String id) {
        return baseRepo.findById(id);
    }

    @Override
    public PageModelDto<T> get(ODataObj odataObj) {
        PageModelDto<T> page = new PageModelDto<T>();
        page.setValue(baseRepo.findByOdata(odataObj));
        page.setCount(odataObj.getCount());

        logger.info("查询用户数据");
        return page;
    }



}