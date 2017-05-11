package cs.service.external;

import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.repository.odata.ODataObj;

public interface DeptService {

	PageModelDto<DeptDto> get(ODataObj odataObj);

	void save(DeptDto record);

	void update(DeptDto record);

	DeptDto findById(String deptId);

	void delete(String id);
}
