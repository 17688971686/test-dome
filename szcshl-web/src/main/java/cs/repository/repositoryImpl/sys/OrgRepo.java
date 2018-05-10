package cs.repository.repositoryImpl.sys;

import java.util.List;

import cs.domain.sys.Org;
import cs.repository.IRepository;

public interface OrgRepo extends IRepository<Org, String> {

	List<Org> findUserChargeOrg();

    /**
     * 根据账号ID统计是否还有分管部门
     * @param oldOrgSLeader
     * @return
     */
    int checkMngOrg(String oldOrgSLeader);

    /**
     * 验证是否是部门用户
     * @param orgId
     * @param userId
     * @return
     */
    boolean checkIsOrgUer(String orgId, String userId);
}
