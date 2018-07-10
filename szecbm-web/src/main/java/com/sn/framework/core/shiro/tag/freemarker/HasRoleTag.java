package com.sn.framework.core.shiro.tag.freemarker;

/**
 * Description: 判断是否存在某个角色
 * <p>Equivalent to {@link org.apache.shiro.web.tags.HasRoleTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:53
 */
public class HasRoleTag extends RoleTag {

    protected boolean showTagBody(String roleName) {
        return getSubject() != null && getSubject().hasRole(roleName);
    }

}