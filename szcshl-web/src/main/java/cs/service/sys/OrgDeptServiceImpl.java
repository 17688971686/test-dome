package cs.service.sys;

import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgDeptServiceImpl implements OrgDeptService {
	private static Logger logger = Logger.getLogger(OrgDeptServiceImpl.class);
	@Autowired
	private OrgDeptRepo orgDeptRepo;


    /**
     * 查询所有
     * @return
     */
	@Override
	public List<OrgDept> queryAll() {
		return orgDeptRepo.findAllByCache();
	}

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public OrgDept queryById(String id) {
        return orgDeptRepo.findById(id);
    }

    /**
     * 根据项目签收流程分支查询
     * @param branchId
     * @return
     */
    @Override
    public OrgDept queryBySignBranchId(String signId,String branchId) {
        return orgDeptRepo.queryBySignBranchId(signId,branchId);
    }

    /**
     * 根据ID 查询部门下的用户
     * @param id
     * @return
     */
    @Override
    public List<User> queryOrgDeptUser(String id) {
        return orgDeptRepo.queryOrgDeptUser(id);
    }

    @Override
    public List<User> queryOrgDeptUser(String signId, String branchId) {
        return orgDeptRepo.queryOrgDeptUser(signId,branchId);
    }

    /**
     * 刷新缓存
     */
    @Override
    public void fleshOrgDeptCache() {
        orgDeptRepo.fleshOrgDeptCache();
    }

    /**
     * 根据ID查询（先从缓存找，再从数据库找）
     * @param id
     * @return
     */
    @Override
    public OrgDept findOrgDeptById(String id) {
        return orgDeptRepo.findOrgDeptById(id);
    }
}
