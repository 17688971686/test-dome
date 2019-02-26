package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.Org;
import cs.model.PageModelDto;
import cs.model.sys.CompanyDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

import java.util.List;
import java.util.Map;

public interface OrgService {
	/**
	 * 分页查询
	 * @param odataObj
	 * @return
	 */
	PageModelDto<OrgDto> get(ODataObj odataObj);

	/**
	 * 创建部门
	 * @param orgDto
	 * @return
	 */
	ResultMsg createOrg(OrgDto orgDto);

	/**
	 * 更新部门
	 * @param orgDto
	 * @return
	 */
	ResultMsg updateOrg(OrgDto orgDto);

	/**
	 * 删除部门
	 * @param id
	 */
	void deleteOrg(String id) ;

	/**
	 * 获取部门用户信息
	 * @param id
	 * @param odataObj
	 * @return
	 */
	PageModelDto<UserDto> getOrgUsers(String id,ODataObj odataObj) ;

	/**
	 * 用户不在部门的用户（没有分配部门的用户）
	 * @param id
	 * @param oDataObj
	 * @return
	 */
	PageModelDto<UserDto> getUsersNotInOrg(String id,ODataObj oDataObj);

	/**
	 * 添加部门用户
	 * @param userId
	 * @param orgId
	 */
	void addUserToOrg(String userId,String orgId);

	/**
	 * 删除部门用户
	 * @param userId
	 * @param orgId
	 */
	void removeOrgUser(String userId, String orgId);

	/**
	 * 批量删除部门用户
	 * @param userIds
	 * @param orgId
	 */
	void removeOrgUsers(String[] userIds, String orgId);
	List<CompanyDto> getCompany(ODataObj odataObj);

	/**
	 * 根据ID获取部门信息
	 * @param id
	 * @return
	 */
	OrgDto findById(String id);

	/**
	 * 获取所有部门信息
	 * @return
	 */
	List<OrgDto> listAll();

	/**
	 * 验证是否是部门用户
	 * @param orgId
	 * @param userId
	 * @return
	 */
    boolean checkIsOrgUer(String orgId, String userId);
}