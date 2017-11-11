package cs.service.project;

import cs.domain.project.SignBranch;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SignBranchServiceImpl implements SignBranchService {

    @Autowired
    private SignBranchRepo signBranchRepo;
    /**
     * 根据部门ID和项目ID，查询所在分支信息
     * @param signId
     * @param orgId
     * @return
     */
    @Override
    public SignBranch findByOrgDirector(String signId, String orgId){
        return signBranchRepo.findByOrgDirector(signId,orgId);
    }
}