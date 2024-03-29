package cs.repository.repositoryImpl.sys;

import java.util.List;

import cs.domain.sys.Dict;
import cs.model.sys.DictDto;
import cs.repository.IRepository;

public interface DictRepo extends IRepository<Dict, String>{

	public Dict findByCodeOrName(String code, String name);

	public Dict findByName(String dictName);

	public Dict findByCode(String dictCode,String excludeId);

	public Dict findByCodeKeyAndName(String dictCode, String dictKey, String dictName);
	
	public Dict findByKeyAndNameAtSomeLevel(String dictKey, String dictName,String parentId,String excludeId);

	public List<Dict> findDictItemByCode(String dictCode);

	public List<Dict> findByPdictId(String parentId);

}
