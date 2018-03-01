package cs.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lqs on 2017/7/11.
 */
public class XFrameOptionFilter implements Filter {
    @Override
    public void destroy() {

    }
  @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request ;//response.
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        //httpServletResponse.setHeader("X-Frame-Options", "ALLOW-FROM http://192.168.1.18:8100/");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","GET, POST, PUT");
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        if(!response.isCommitted())
            filterChain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}
