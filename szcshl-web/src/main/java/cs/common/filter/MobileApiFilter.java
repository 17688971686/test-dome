package cs.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 手机端API调用验证
 * Created by lqs on 2017/7/12.
 */
public class MobileApiFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        String callKey = request.getParameter("callKey");
        if(callKey == null||callKey.isEmpty()||!callKey.equals("5DF50E30-B215-45BC-B361-776AE1D09994")){
            //验证未通过
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.flushBuffer();
            return ;
        }

        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
