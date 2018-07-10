package com.sn.framework.core.shiro.tag.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Description:
 * <p>Equivalent to {@link org.apache.shiro.web.tags.NotAuthenticatedTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:53
 */
public class NotAuthenticatedTag extends SecureTag {
    private static final Logger logger = LoggerFactory.getLogger(UserTag.class);

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        if (getSubject() == null || !getSubject().isAuthenticated()) {
            logger.debug("Subject does not exist or is not authenticated.  Tag body will be evaluated.");
            renderBody(env, body);
        } else {
            logger.debug("Subject exists and is authenticated.  Tag body will not be evaluated.");
        }
    }
}