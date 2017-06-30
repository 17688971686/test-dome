package cs.service.sys;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cs.common.Response;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.DictDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

/**
 * @author lqs
 *         <p>
 *         dictionary services
 */
public interface DictService {


    /*字典分组查询
    描述：查询全部的字典分组数据，首先从缓存查询，如果存在，直接查询，如果不存在，从数据库查询，然后放到缓存，缓存的key为dictGroups，value为字典分组数据数组*/
    public PageModelDto<DictDto> get(ODataObj odataObj);

    /*字典分组新增
    描述:增加数据字典分组，必须填写分组名称、字典类型编码，名称和编码都是唯一的、排序默认*/
    public void createDict(DictDto dictDto);

    /*字典分组更新
    描述：只能更新分组名称、排序，分组类型编码不可编辑*/
    public void updateDict(DictDto dictDto);


    /*
     * 删除
     *
    */
    public void deleteDict(String dictId);

    /*
     * 批量删除
    */
    public void deleteDicts(String[] dictIds);

    /*字典分组启用禁用
    描述：设置字典分组启用还是禁用，创建分组字典后，默认是启用的*/
    public void setDictState(String dictId, String state);


    /*通过类型编码查询字典数据
    描述：通过类型编码查询该类型的所有字典数据，首先从缓存查询，如果缓存没有，则从数据库查询，放入缓存，key为类型编码，value为字典数据数组*/
    public List<DictDto> findDictByCode(String dictCode);

    /*通过类型编码和字典值查询字典数据
    描述：通过类型编码和数据字典值查询单个字典数据，主要用户显示业务数据对应的字典数据项的时候用到，首先从缓存查询，如果缓存没有，则从数据库查询，放入缓存， 缓存的key为类型编码-字典值，value为字典数据
    */
    public DictDto findDictByCodeAndKey(String dictCode, String dictValue);


    public Object getFromCache(String key);

    /**
     * 取得指定编码类型的字典数据，不包含字典类型数据
     *
     * @param dictCode 字典编码
     */
    public List<DictDto> getDictItemByCode(String dictCode);

    /**
     * 通过编码类型取得字典名称，用于翻译出字典值，只返回字典数据
     *
     * @param dictCode 字典编码
     * @param dictKey  字典值
     * @return Map类型，key的格式是dictCode-dictKey,value是dictName
     */
    public Map<String, String> getDictNameByCode(String dictCode);

    public List<DictDto> getAllDictByCode(String dictCode);


}