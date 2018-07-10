package com.sn.framework.core.shiro.tag.freemarker;

/**
 * Description: 判断是否缺少某个权限
 * <p>Equivalent to {@link org.apache.shiro.web.tags.LacksPermissionTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:53
 */
public class LacksPermissionTag extends PermissionTag {
    protected boolean showTagBody(String p) {
        return !isPermitted(p);
    }
}