package cs.service.project;

import cs.domain.project.SignBranch;

public interface SignBranchService {

    /**
     * 根据部门ID和项目ID，查询所在分支信息
     * @param signId
     * @param orgId
     * @return
     */
    SignBranch findByOrgDirector(String signId, String orgId);

}
