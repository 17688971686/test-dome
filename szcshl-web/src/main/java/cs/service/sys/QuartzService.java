package cs.service.sys;

import java.util.List;

import cs.domain.sys.Quartz;
import cs.model.PageModelDto;
import cs.model.sys.QuartzDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 定时器配置 业务操作接口
 * author: ldm
 * Date: 2017-6-20 10:47:42
 */
public interface QuartzService {

    PageModelDto<QuartzDto> get(ODataObj odataObj);

	void save(QuartzDto record);

	void update(QuartzDto record);

	QuartzDto findById(String deptId);

	void delete(String id);
	
	void changeCurState(String id,String state);
	
	List<Quartz> findDefaultQuartz();

}
