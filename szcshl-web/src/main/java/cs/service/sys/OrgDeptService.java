package cs.service.sys;

import cs.domain.sys.OrgDept;
import cs.domain.sys.User;

import java.util.List;
import java.util.Map;

public interface OrgDeptService {

    List<OrgDept> queryAll();

    OrgDept queryById(String id);

    OrgDept queryBySignBranchId(String signId,String branchId);

    List<User> queryOrgDeptUser(String id);

    List<User> queryOrgDeptUser(String signId,String branchId);

    void fleshOrgDeptCache();

    OrgDept findOrgDeptById(String id);

    /**
     * 按照部门职责类型区分部门组别信息
     * @return
     */
    Map<String,List<OrgDept>> groubByOrgType();

    /**
     * 根据部门职责类型分组项目名称
     * @param linkString 链接字符串
     * @return
     */
    Map<String,String> groubOrgNameByType(String linkString);
}