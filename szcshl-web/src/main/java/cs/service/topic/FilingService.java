package cs.service.topic;

import cs.domain.topic.Filing;
import cs.model.PageModelDto;
import cs.model.topic.FilingDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 课题归档 业务操作接口
 * author: ldm
 * Date: 2017-9-4 15:40:48
 */
public interface FilingService {
    
    PageModelDto<FilingDto> get(ODataObj odataObj);

	void save(FilingDto record);

	void update(FilingDto record);

	FilingDto findById(String deptId);

	void delete(String id);

}
