package cs.xss;

import cs.common.constants.SysConstants;
import cs.common.utils.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class XSSFilter implements Filter {
    private static Logger log = Logger.getLogger(XSSFilter.class);

    private FilterConfig filterConfig;
    private String excludedUris;
    private String[] excludedUriArr;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        excludedUris = filterConfig.getInitParameter("excludedUris");
        if (StringUtils.isNotEmpty(excludedUris)) {
            excludedUriArr = excludedUris.split(SysConstants.SEPARATE_COLON);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        boolean excludedUri = false;
        String uri = servletRequest.getRequestURI();
        String contextPath = servletRequest.getContextPath();
        String mappingURL = uri.replaceAll(contextPath, "");
        if (!Validate.isEmpty(excludedUriArr)) {
            for (int i = 0, l = excludedUriArr.length; i < l; i++) {
                if (excludedUriArr[i].equals(mappingURL)) {
                    excludedUri = true;
                    break;
                }
            }
        }

        if (excludedUri) {
            chain.doFilter(request, response);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("过滤请求：" + mappingURL);
            }
            chain.doFilter(new XssHttpServletRequestWrapper(servletRequest), response);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}