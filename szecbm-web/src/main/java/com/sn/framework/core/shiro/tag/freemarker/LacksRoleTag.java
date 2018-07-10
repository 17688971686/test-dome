package com.sn.framework.core.shiro.tag.freemarker;

/**
 * Description: 判断是否缺少某个角色
 * <p>Equivalent to {@link org.apache.shiro.web.tags.LacksRoleTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:53
 */
public class LacksRoleTag extends RoleTag {
    protected boolean showTagBody(String roleName) {
        boolean hasRole = getSubject() != null && getSubject().hasRole(roleName);
        return !hasRole;
    }
}