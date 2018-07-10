package com.sn.framework.core.shiro.tag.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Description: 判断用户是否已认证（已登录）
 * <p>Equivalent to {@link org.apache.shiro.web.tags.AuthenticatedTag}</p>
 * @author: tzg
 * @date: 2017/10/21 17:52
 */
public class AuthenticatedTag extends SecureTag {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedTag.class);

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        if (getSubject() != null && getSubject().isAuthenticated()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Subject exists and is authenticated.  Tag body will be evaluated.");
            }

            renderBody(env, body);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Subject does not exist or is not authenticated.  Tag body will not be evaluated.");
            }
        }
    }
}