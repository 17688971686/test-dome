package cs.service.sys;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cs.common.utils.Validate;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    /**
     * 按照部门职责类型区分部门组别信息
     * @return
     */
    @Override
    public Map<String, List<OrgDept>> groubByOrgType() {
        Map<String, List<OrgDept>> resultMap = Maps.newHashMap();
        List<OrgDept> allOrgDept = orgDeptRepo.findAllByCache();
        for(OrgDept orgDept : allOrgDept){
            if(Validate.isString(orgDept.getOrgType())){
                countTypeNum(resultMap,orgDept.getOrgType(),orgDept);
            }
        }
        return resultMap;
    }

    /**
     * 根据部门职责类型分组项目名称
     * @param linkString 链接字符串
     * @return
     */
    @Override
    public Map<String, String> groubOrgNameByType(String linkString) {
        Map<String, String> resultMap = Maps.newHashMap();
        List<OrgDept> allOrgDept = orgDeptRepo.findAllByCache();
        for(OrgDept orgDept : allOrgDept){
            if(Validate.isString(orgDept.getOrgType())){
                countOrgName(resultMap,orgDept.getOrgType(),orgDept,linkString);
            }
        }
        return resultMap;
    }

    private void countOrgName(Map<String, String> resultMap, String orgType, OrgDept orgDept,String linkString) {
        String orgName = resultMap.get(orgType);
        if(null == orgName){
            orgName = orgDept.getName();
        }else{
            orgName = orgName + linkString + orgDept.getName();
        }
        resultMap.put(orgType,orgName);
    }

    private void countTypeNum(Map<String, List<OrgDept>> resultMap, String orgType,OrgDept orgDept) {
        List<OrgDept> orgTypeList = resultMap.get(orgType);
        if(null == orgTypeList){
            orgTypeList = Lists.newArrayList();
        }
        orgTypeList.add(orgDept);
        resultMap.put(orgType,orgTypeList);
    }
}
