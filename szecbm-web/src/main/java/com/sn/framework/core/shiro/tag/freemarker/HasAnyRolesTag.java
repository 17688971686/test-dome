package com.sn.framework.core.shiro.tag.freemarker;

import org.apache.shiro.subject.Subject;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;

/**
 * Description: 判断是否存在角色（多个角色）
 * <p>Equivalent to {@link org.apache.shiro.web.tags.HasAnyRolesTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:52
 */
public class HasAnyRolesTag extends RoleTag {

    protected boolean showTagBody(String roleNames) {
        boolean hasAnyRole = false;
        Subject subject = getSubject();

        if (subject != null) {
            // Iterate through roles and check to see if the user has one of the roles
            for (String role : roleNames.split(SEPARATE_COMMA)) {
                if (subject.hasRole(role.trim())) {
                    hasAnyRole = true;
                    break;
                }
            }
        }

        return hasAnyRole;
    }
}