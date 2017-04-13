package cs.repository.repositoryImpl;

import java.util.List;

import cs.domain.Dict;
import cs.repository.IRepository;

public interface DictRepo extends IRepository<Dict, String>{

	public Dict findByCodeOrName(String code, String name);

	public Dict findByName(String dictGroupName);
	public Dict findByCode(String dictGroupCode);

	public Dict findByCodeKeyAndName(String dictCode, String dictKey, String dictName);

	public List<Dict> findDictItemByCode(String dictCode);

}
