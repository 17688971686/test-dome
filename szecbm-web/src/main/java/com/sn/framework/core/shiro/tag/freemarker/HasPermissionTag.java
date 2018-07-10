package com.sn.framework.core.shiro.tag.freemarker;

/**
 * Description: 判断是否有某个权限
 * <p>Equivalent to {@link org.apache.shiro.web.tags.HasPermissionTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:52
 */
public class HasPermissionTag extends PermissionTag {

    protected boolean showTagBody(String p) {
        return isPermitted(p);
    }

}