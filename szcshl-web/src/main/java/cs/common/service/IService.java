package cs.common.service;

import cs.model.MyTestDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 公共业务处理借口
 * User: Administrator
 * Date: 2017/5/4 17:21
 * @param <T>   数据库映射实体类
 * @param <D>   页面数据映射实体
 */
public interface IService<T, D> {

    /**
     * 保持记录
     * @param record
     */
    void save(D record);

    /**
     * 删除记录
     * @param id
     */
    void delete(String id);

    /**
     * 删除记录（多条）
     * @param ids
     */
    void delete(String[] ids);

    /**
     * 根据主键查找记录
     * @param id
     * @return
     */
    T findById(String id);

    /**
     * 获取数据
     * @param odataObj
     * @return
     */
    PageModelDto<T> get(ODataObj odataObj);

//    List<T> selectList(D query);

}
