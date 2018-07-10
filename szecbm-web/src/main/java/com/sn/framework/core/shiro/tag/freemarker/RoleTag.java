package com.sn.framework.core.shiro.tag.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

/**
 * Description: 判断是否包含某个角色
 * <p>Equivalent to {@link org.apache.shiro.web.tags.RoleTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 18:03
 */
public abstract class RoleTag extends SecureTag {

    String getName(Map params) {
        return getParam(params, "name");
    }

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        boolean show = showTagBody(getName(params));
        if (show) {
            renderBody(env, body);
        }
    }

    protected abstract boolean showTagBody(String roleName);
}