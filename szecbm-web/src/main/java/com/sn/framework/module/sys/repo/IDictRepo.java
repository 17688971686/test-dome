package com.sn.framework.module.sys.repo;

import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.Dict;

import java.util.List;

/**
 * Description: 数据字典  数据操作接口
 * Author: qbl
 * Date: 2017/9/12 16:42
 */
public interface IDictRepo extends IRepository<Dict,String> {

    /**
     * 通过ID查找子字典列表
     * @param dictId
     * @return
     */
    List<Dict> findChildrenById(String dictId);

    /**
     * 查找同等级下排序号之前的数据
     * @param parentId
     * @param itemOrder
     * @return
     */
    List<Dict> findBeforeDicts(String parentId, Integer itemOrder);

    /**
     * 查找同等级下排序号之后的数据
     * @param parentId
     * @param itemOrder
     * @return
     */
    List<Dict> findAfterDicts(String parentId, Integer itemOrder);

    /**
     * 根据字典值查询字典信息
     * @param parentId
     * @param dictKey
     * @return
     */
    Dict findByParentIdAndDictKey(String parentId, String dictKey);
}
