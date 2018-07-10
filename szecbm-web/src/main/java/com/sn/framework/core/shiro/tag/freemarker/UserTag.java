package com.sn.framework.core.shiro.tag.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Description: 判断是否某个用户
 * <p>The logically opposite tag of this one is the {@link org.apache.shiro.web.tags.GuestTag}.
 * <p>Equivalent to {@link org.apache.shiro.web.tags.UserTag}</p>
 *
 * @author: tzg
 * @date: 2017/10/21 17:44
 */
public class UserTag extends SecureTag {
    private static final Logger logger = LoggerFactory.getLogger(UserTag.class);

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        if (getSubject() != null && getSubject().getPrincipal() != null) {
            logger.debug("Subject has known identity (aka 'principal'). Tag body will be evaluated.");
            renderBody(env, body);
        } else {
            logger.debug("Subject does not exist or have a known identity (aka 'principal'). Tag body will not be evaluated.");
        }
    }

}