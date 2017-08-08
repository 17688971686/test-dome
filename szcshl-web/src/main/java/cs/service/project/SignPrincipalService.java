package cs.service.project;

import cs.domain.project.SignPrincipal;
import cs.domain.sys.User;

import java.util.List;

/**
 * Description: 项目负责人 业务操作接口
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
public interface SignPrincipalService {

    boolean isMainPri(String userId,String signId);

    boolean isFlowPri(String userId,String signId);

    boolean isMainFlowPri(String userId,String signId);

    List<User> getSignPriUser(String signId,String branchId);

    User getMainPriUser(String signId);

    List<User> getAllSecondPriUser(String signId);

    SignPrincipal getPrincipalInfo(String userId, String signId);

}
