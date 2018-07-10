package com.sn.framework.core.shiro.tag.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

/**
 * Description: 判断机构的标签基础类
 *
 * @author: tzg
 * @date: 2017/10/21 18:46
 */
public abstract class OrganTag extends SecureTag {

    String getOrganCode(Map params) {
        return getParam(params, "name");
    }

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        if (showTagBody(getOrganCode(params))) {
            renderBody(env, body);
        }
    }

    protected abstract boolean showTagBody(String organCode);

}