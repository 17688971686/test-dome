package cs.service.sys;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.DictDto;
import cs.repository.odata.ODataObj;

import java.util.List;
import java.util.Map;

/**
 * @author lqs
 *         <p>
 *         dictionary service
 */
public interface DictService {


    /**
     * 字典分组查询
     * 描述：查询全部的字典分组数据，首先从缓存查询，
     * 如果存在，直接查询，
     * 如果不存在，从数据库查询，
     * 然后放到缓存，缓存的key为dictGroups，value为字典分组数据数组
     * @param odataObj
     * @return
     */
    PageModelDto<DictDto> get(ODataObj odataObj);

    /**
     * 字典分组新增
     * 描述:增加数据字典分组，必须填写分组名称、字典类型编码，名称和编码都是唯一的、排序默认
     * @param dictDto
     * @return
     */
    ResultMsg createDict(DictDto dictDto);

    /**
     * 字典分组更新
     * 描述：只能更新分组名称、排序，分组类型编码不可编辑
     * @param dictDto
     * @return
     */
    ResultMsg updateDict(DictDto dictDto);

    /**
     * 删除
     * @param dictId
     * @return
     */
    ResultMsg deleteDict(String dictId);

    /**
     * 字典分组启用禁用
     * 描述：设置字典分组启用还是禁用，创建分组字典后，默认是启用的
     * @param dictId
     * @param state
     * @return
     */
    void setDictState(String dictId, String state);


    /**
     * 通过类型编码查询字典数据
     * 描述：通过类型编码查询该类型的所有字典数据，
     * 首先从缓存查询，如果缓存没有，则从数据库查询，放入缓存，key为类型编码，value为字典数据数组
     * @param dictCode
     * @return
     */
    List<DictDto> findDictByCode(String dictCode);

    /**
     * 通过类型编码和字典值查询字典数据
     * 描述：通过类型编码和数据字典值查询单个字典数据，
     * 主要用户显示业务数据对应的字典数据项的时候用到，
     * 首先从缓存查询，如果缓存没有，则从数据库查询，放入缓存， 缓存的key为类型编码-字典值，value为字典数据
     * @param dictCode
     * @return
     */
    DictDto findDictByCodeAndKey(String dictCode, String dictValue);


    Object getFromCache(String key);

    /**
     * 取得指定编码类型的字典数据，不包含字典类型数据
     *
     * @param dictCode 字典编码
     */
    List<DictDto> getDictItemByCode(String dictCode);

    /**
     * 通过编码类型取得字典名称，用于翻译出字典值，只返回字典数据
     *
     * @param dictCode 字典编码
     * @return Map类型，key的格式是dictCode-dictKey,value是dictName
     */
    Map<String, String> getDictNameByCode(String dictCode);

    List<DictDto> getAllDictByCode(String dictCode);


    ResultMsg findById(String id);
}