package com.sn.framework.core.shiro.tag.freemarker;

import freemarker.template.SimpleHash;

/**
 * Description: shiro的freemarker权限标签
 * <p>Usage: cfg.setSharedVeriable("shiro", new ShiroTags());</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:43
 */
public class ShiroTags extends SimpleHash {
    public ShiroTags() {
        put("authenticated", new AuthenticatedTag());
        put("guest", new GuestTag());
        put("hasAnyRoles", new HasAnyRolesTag());
        put("hasPermission", new HasPermissionTag());
        put("hasRole", new HasRoleTag());
        put("lacksPermission", new LacksPermissionTag());
        put("lacksRole", new LacksRoleTag());
        put("notAuthenticated", new NotAuthenticatedTag());
        put("principal", new PrincipalTag());
        put("user", new UserTag());
        put("hasOrgan", new HasOrganTag());
        put("lacksOrgan", new LacksOrganTag());
    }
}