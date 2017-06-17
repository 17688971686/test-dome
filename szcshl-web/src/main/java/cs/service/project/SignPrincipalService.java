package cs.service.project;

import cs.domain.sys.User;

import java.util.List;

/**
 * Description: 项目负责人 业务操作接口
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
public interface SignPrincipalService {

    boolean isMainPri(String userId,String signId,String isMainFolw);

    boolean isFlowPri(String userId,String signId,String isMainFolw);

    List<User> getSignPriUser(String signId, String isMainFolw);

    User getMainPriUser(String signId, String isMainFolw);

    User getSecondPriUser(String signId, String isMainFolw);

    List<User> getAllSecondPriUser(String signId, String isMainFolw);

    User getPriUserByType(String signId, String isMainFolw,String userType);
}
