package cs.service.sys;

import java.util.List;

import cs.common.ResultMsg;
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

	/**
	 * 保存定时器
	 * @param record
	 * @return
	 */
	ResultMsg save(QuartzDto record);

    /**
     * 根据ID查询
     * @param deptId
     * @return
     */
	QuartzDto findById(String deptId);

    /**
     * 停用定时器
     * @param id
     * @return
     */
    ResultMsg delete(String id);

	
	List<Quartz> findDefaultQuartz();

    /**
     * 执行定时器
     * @param quartzId
     * @return
     */
    ResultMsg quartzExecute(String quartzId);

    /**
     * 暂停定时器
     * @param quartzId
     * @return
     */
    ResultMsg quartzStop(String quartzId);

}
