package cs.service.sys;


import cs.common.RandomGUID;
import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Dict;
import cs.domain.sys.Dict_;
import cs.model.PageModelDto;
import cs.model.sys.DictDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.DictRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class DictServiceImpl implements DictService {
    private static Logger logger = Logger.getLogger(DictServiceImpl.class);

    @Autowired
    private DictRepo dictRepo;

    private ICache cache = CacheManager.getCache();

    @Override
    public PageModelDto<DictDto> get(ODataObj odataObj) {
        PageModelDto<DictDto> pageModelDto = new PageModelDto<DictDto>();
        //1、先从缓存取数据
        List<DictDto> dictDtos = (List<DictDto>) cache.get("DICT_ALL_ITEMS");
        //2、如果缓存没有，再去读数据库
        if(!Validate.isList(dictDtos)){
            List<Dict> dicts = dictRepo.findByOdata(odataObj);
            if(Validate.isList(dicts)){
                dictDtos = new ArrayList<>(dicts.size());
                for (Dict dict : dicts) {
                    DictDto dictDto = new DictDto();
                    BeanCopierUtils.copyProperties(dict, dictDto);
                    dictDtos.add(dictDto);
                }
            }
            cache.put("DICT_ALL_ITEMS", dictDtos);
        }
        pageModelDto.setCount(dictDtos.size());
        pageModelDto.setValue(dictDtos);
        return pageModelDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg createDict(DictDto dictDto) {
        if(!Validate.isString(dictDto.getDictCode())){
            return ResultMsg.error("请先字典编码还没填写！");
        }
        //检查DICT CODE是否重复
        Dict dictExists = dictRepo.findByCode(dictDto.getDictCode(), null);
        if (dictExists != null) {
            return ResultMsg.error(String.format("增加字典，字典编码：%s 已经存在,请重新输入！", dictDto.getDictCode()));
        }
        Dict dict = new Dict();
        BeanCopierUtils.copyProperties(dictDto, dict);
        dict.setDictId((new RandomGUID()).valueAfterMD5);
        dict.setCreatedBy(SessionUtil.getDisplayName());
        dict.setModifiedBy(SessionUtil.getDisplayName());
        dict.setIsUsed("0");

        Date now = new Date();
        dict.setCreatedDate(now);
        dict.setModifiedDate(now);
        dictRepo.save(dict);

        //清除缓存
        clearCache(dict.getDictCode());
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"创建成功！");
    }


    /**
     * 检查数据字典是否已存在
     * @param dictDto
     * @param checkDictId
     * @return
     */
    private ResultMsg checkDictExists(DictDto dictDto, String checkDictId) {
        //检查DICT CODE是否重复
        Dict dictExists = dictRepo.findByCode(dictDto.getDictCode(), checkDictId);
        if (dictExists != null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),String.format("增加字典，字典编码：%s 已经存在,请重新输入！", dictDto.getDictCode()));
        }
        //如果DICT CODE不重复，检查和同级字典值和字典名称是否有重复，有重复不创建
        Dict hasDictAtSomeLevel = dictRepo.findByKeyAndNameAtSomeLevel(dictDto.getDictKey(), dictDto.getDictName(), dictDto.getParentId(), checkDictId);
        if (hasDictAtSomeLevel != null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),String.format("增加字典[%s-%s]在同级已经存在,请重新输入！", dictDto.getDictName(), dictDto.getDictKey()));
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"验证正确！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg updateDict(DictDto dictDto) {
        if(!Validate.isString(dictDto.getDictCode())){
            return ResultMsg.error("请先字典编码还没填写！");
        }
        //检查DICT CODE是否重复
        Dict dictExists = dictRepo.findByCode(dictDto.getDictCode(), dictDto.getDictId());
        if (dictExists != null) {
            return ResultMsg.error(String.format("增加字典，字典编码：%s 已经存在,请重新输入！", dictDto.getDictCode()));
        }
        //修改
        Dict dict = dictRepo.findById(Dict_.dictId.getName(),dictDto.getDictId());
        BeanCopierUtils.copyProperties(dictDto, dict);
        dict.setModifiedBy(SessionUtil.getLoginName());
        dict.setModifiedDate(new Date());
        dictRepo.save(dict);
        //清除缓存
        clearCache(dict.getDictCode());
        return ResultMsg.ok("操作成功！");
    }

    private void clearCache(String dictCode) {
        cache.clear("DICT_ALL_ITEMS");
        cache.clear("DICT_ITEMS".concat(dictCode));
        cache.clear("DICT_NAME_DATA".concat(dictCode));

    }

    @Override
    public void setDictState(String dictCode, String state) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<DictDto> findDictByCode(String dictCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DictDto findDictByCodeAndKey(String dictCode, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getFromCache(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 删除字典，包括它的子项，只适用于单个删除
     * @param dictId
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteDict(String dictId) {
        Dict dict = dictRepo.findById(Dict_.dictId.getName(),dictId);
        if(dict != null){
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" delete from "+Dict.class.getSimpleName()+" where "+Dict_.dictId.getName()+" =:dictId ");
            hqlBuilder.setParam("dictId",dictId);
            hqlBuilder.append(" or "+Dict_.parentId.getName()+" =:parentId ");
            hqlBuilder.setParam("parentId",dictId);
            int result = dictRepo.executeHql(hqlBuilder);
            if(result > 0){
                //删除所有缓存
                clearCache(dict.getDictCode());
                return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"删除成功！");
            }else{
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"该字典信息已被删除！");
            }
        }
        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"该字典信息已被删除！");
    }


    /**
     * 取得指定编码类型的字典数据，不包含编码类型
     *
     * @param dictCode 字典编码
     */
    @Override
    public List<DictDto> getDictItemByCode(String dictCode) {
        //先从缓存取，如果取不到就查询数据库，把查到的数据放到缓存
        //缓存键格式DICT_ITEMS+dictCode
        List<DictDto> dictDtos ;
        //如果dictCode是null，则返回全部数据
        if (Validate.isString(dictCode)) {
            dictDtos = (List<DictDto>) cache.get("DICT_ITEMS".concat(dictCode));
            if (!Validate.isList(dictDtos)) {
                List<Dict> dicts = dictRepo.findDictItemByCode(dictCode);
                if (Validate.isList(dicts)) {
                    dictDtos = new ArrayList<DictDto>(dicts.size());
                    for (Dict dict : dicts) {
                        DictDto dictDto = new DictDto();
                        BeanCopierUtils.copyProperties(dict, dictDto);
                        dictDtos.add(dictDto);
                    }
                    //放到缓存
                    cache.put("DICT_ITEMS".concat(dictCode), dictDtos);
                }
            }
        } else {
            dictDtos = (List<DictDto>) cache.get("DICT_ALL_ITEMS");
            if (!Validate.isList(dictDtos)) {
                Criteria criteria = dictRepo.getExecutableCriteria();
                criteria.addOrder(Order.asc(Dict_.dictSort.getName()));
                List<Dict> dicts = criteria.list();
                if (Validate.isList(dicts)) {
                    dictDtos = new ArrayList<DictDto>(dicts.size());
                    for (Dict dict : dicts) {
                        DictDto dictDto = new DictDto();
                        BeanCopierUtils.copyProperties(dict, dictDto);
                        dictDtos.add(dictDto);
                    }
                    cache.put("DICT_ALL_ITEMS", dictDtos);
                }
            }
        }
        return dictDtos;
    }


    /**
     * 通过编码类型取得字典名称，用于翻译出字典值，只返回字典数据
     *
     * @param dictCode 字典编码
     * @return Map类型，key的格式是dictCode-dictKey,value是dictName
     */
    @Override
    public Map<String, String> getDictNameByCode(String dictCode) {
        //先从缓存取，如果取不到就查询数据库，把查到的数据放到缓存
        //缓存键格式DICT_NAME_DATA+dictCode
        Map<String, String> dictNameDataMap = (Map<String, String>) cache.get("DICT_NAME_DATA".concat(dictCode));
        if (dictNameDataMap == null) {
            //TODO:查询数据库
            List<DictDto> dictItems = getDictItemByCode(dictCode);
            if (dictItems != null) {
                dictNameDataMap = new HashMap<>();
                for (DictDto dictDto : dictItems) {
                    String key = dictDto.getDictCode().concat("_").concat(dictDto.getDictKey());
                    dictNameDataMap.put(key, dictDto.getDictName());
                }
                //放到缓存
                cache.put("DICT_NAME_DATA".concat(dictCode), dictNameDataMap);
            }
        }
        return dictNameDataMap;
    }

    @Override
    @Transactional
    public List<DictDto> getAllDictByCode(String dictCode) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select *　from CS_DICT c where c.PARENTID in(");
        hqlBuilder.append("select c1.dictId from CS_DICT c1 where c1.PARENTID in(");
        hqlBuilder.append("select c2.dictId from CS_DICT c2 where c2.DICTCODE=:dictCode))");
        hqlBuilder.setParam("dictCode", dictCode);
        List<Dict> dictList = dictRepo.findBySql(hqlBuilder);
        List<DictDto> dictDtoList = new ArrayList<>();
        for (Dict dict : dictList) {
            DictDto dictDto = new DictDto();
            BeanCopierUtils.copyProperties(dict, dictDto);
            dictDtoList.add(dictDto);
        }
        return dictDtoList;
    }

    @Override
    @Transactional
    public List<DictDto> getDictForAppByCode(String dictCode) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select c1.* from CS_DICT c1 where c1.PARENTID in(");
        hqlBuilder.append("select c2.dictId from CS_DICT c2 where c2.DICTCODE=:dictCode)");
        hqlBuilder.setParam("dictCode", dictCode);
        List<Dict> dictList = dictRepo.findBySql(hqlBuilder);
        List<DictDto> dictDtoList = new ArrayList<>();
        for (Dict dict : dictList) {
            DictDto dictDto = new DictDto();
            BeanCopierUtils.copyProperties(dict, dictDto);
            dictDtoList.add(dictDto);
        }
        return dictDtoList;
    }

    /**
     * 根据ID查询字典信息
     * @param id
     * @return
     */
    @Override
    public ResultMsg findById(String id) {
        Dict dict = dictRepo.findById(Dict_.dictId.getName(),id);
        if(dict == null){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"获取数据失败，字典信息已被删除！");
        }else{
            DictDto dictDto = new DictDto();
            BeanCopierUtils.copyProperties(dict,dictDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"查询成功！",dictDto);
        }
    }


}
