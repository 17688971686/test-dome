package cs.mobile.service;

import cs.domain.flow.RuProcessTask;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

/**
 * Created by Administrator on 2018/2/28.
 */
public interface WorkDynamicService {

    int findMyDoingSignTask( String id);

    int findMyDoingTask( String id);

    /**
     * 20170706 项目签收流程接口
     **/
    PageModelDto<RuProcessTask> queryRunProcessTasks(String id, boolean isUserDeal);
}
