package cs.repository.repositoryImpl.project;

import cs.domain.project.SignPrincipal;
import cs.domain.sys.User;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 项目-负责人中间表
 * author: ldm
 * Date: 2017-6-16 14:48:31
 */
public interface SignPrincipalRepo extends IRepository<SignPrincipal, String> {
    /**
     * 获取项目负责人列表
     * @param businessId
     * @param orgId
     * @return
     */
    List<User> getPrinUserList(String businessId, String orgId);
}
