package com.sn.framework.module.sys.service;

import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.model.DictDto;

import java.util.List;
import java.util.Map;

/**
 * Description: 数据字典  业务操作接口
 * @Author: qbl
 * @Date: 2017/9/12 16:36
 */
public interface IDictService extends ISService<DictDto> {

    /**
     *
     * @param dictId
     * @return
     */
    List<DictDto> findChildrenById(String dictId);

    /**
     * 通过父级字典 ID 获取子字典集
     * @param dictId
     * @return
     */
    Map<String, DictDto> findChildrenMapById(String dictId);

    /**
     *
     * @param parentId
     * @return
     */
//    List<DictDto> getParentDictItemById(String parentId);

    /**
     *
     * @param dictId
     * @return
     */
    Map<String, String> getDictNameById(String dictId);

    /**
     * 调序：置顶
     * @param dictId
     */
    void sortTop(String dictId);

    /**
     * 调序：向上
     * @param dictId
     */
    void sortUp(String dictId);

    /**
     * 调序：向下
     * @param dictId
     */
    void sortDown(String dictId);

    /**
     * 调序：置底
     * @param dictId
     */
    void sortBottom(String dictId);

    /**
     * 初始化数据字典的数据
     * @throws Exception
     */
    void initDictData();

//    /**
//     * 根据存储ID查询字典KEY
//     * @param organId
//     */
//    String getDictKeyByDatabaseKey(String organId);
}
