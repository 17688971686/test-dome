package cs.spring;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author ldm
 * @date 2018/12/3
 * cookie 只读过滤器
 */
public class SecureFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        String sessionid = req.getSession().getId();
        resp.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Secure; HttpOnly");
        //X-Frame-Options
        resp.setHeader("x-frame-options","SAMEORIGIN");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}

