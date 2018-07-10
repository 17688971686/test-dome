package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.common.StringUtil;
import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.core.repo.impl.BeanRowMapper;
import com.sn.framework.module.sys.domain.Role;
import com.sn.framework.module.sys.domain.Role_;
import com.sn.framework.module.sys.model.RoleDto;
import com.sn.framework.module.sys.repo.IRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: 角色信息  数据操作实现类
 *
 * @Author: tzg
 * @Date: 2017/8/24 15:30
 */
@Repository
public class RoleRepoImpl extends AbstractRepository<Role, String> implements IRoleRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Role findByRoleName(String roleName) {
        CriteriaQuery<Role> query = createCriteriaQuery();
        Root<Role> root = query.from(Role.class);
        query.where(getBuilder().equal(root.get(Role_.roleName.getName()), roleName));
        try {
            return entityManager.createQuery(query.select(root)).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<RoleDto> findUserRoles(String userId) {
        String sql = "SELECT T.ROLE_ID,T.ROLE_NAME,T.DISPLAY_NAME,R.USER_ID FROM CS_SYS_ROLE T " +
                "LEFT JOIN CS_SYS_USER_ROLE R ON T.ROLE_ID=R.ROLE_ID AND R.USER_ID=? " +
                "WHERE T.ROLE_STATE='1' AND T.ROLE_NAME<>'ADMIN' " +
                "ORDER BY T.ITEM_ORDER ASC,T.CREATED_BY ASC";
        return jdbcTemplate.query(sql, new String[]{userId}, new BeanRowMapper<RoleDto>() {
            @Override
            protected RoleDto mapRow(ResultSet rs, int rowNum, RoleDto record) throws SQLException {
                String userId = rs.getString("USER_ID");
                record.setChecked(StringUtil.isNotBlank(userId));
                return record;
            }
        });
    }

}