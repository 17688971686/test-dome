package cs.common;

import java.io.IOException;

import cs.common.sysprop.BusinessProperties;
import cs.spring.SpringContextUtil;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateException;


public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {

    private BusinessProperties businessProperties = null;

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        businessProperties = SpringContextUtil.getBean(cs.common.constants.SysConstants.SYS_BUSI_PROP_BEAN);
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
        this.getConfiguration().setSharedVariable("projectTitle", businessProperties.getProjectTitle());
        this.getConfiguration().setSharedVariable("accreditUnit", businessProperties.getAccreditUnit());
        this.getConfiguration().setSharedVariable("logoName", businessProperties.getLogoName());
        this.getConfiguration().setSharedVariable("logoTitle", businessProperties.getLogoTitle());
        this.getConfiguration().setSharedVariable("homeLogo", businessProperties.getHomeLogo());
    }
}