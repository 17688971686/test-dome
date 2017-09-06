package cs.repository.repositoryImpl.flow;

import cs.domain.flow.FlowPrincipal;
import cs.domain.sys.User;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 流程负责人 数据操作实现接口
 * author: ldm
 * Date: 2017-9-4 17:47:04
 */
public interface FlowPrincipalRepo extends IRepository<FlowPrincipal, String> {

    List<User> getFlowAllPrinByBusiId(String busiId);

    List<User> getFlowPrinByBusiId(String busiId);

    User getFlowMainPrinByBusiId(String busiId);
}
