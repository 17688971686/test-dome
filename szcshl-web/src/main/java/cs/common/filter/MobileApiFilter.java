package cs.common.filter;

import com.alibaba.fastjson.JSON;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.mobile.service.UserSvc;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static cs.mobile.constants.AppConstants.START_URL;
import static cs.mobile.util.TokenUtil.PASS_URL;
import static cs.mobile.util.TokenUtil.SYS_TOKEN_KEY;

/**
 * 手机端API调用验证
 * Created by lqs on 2017/7/12.
 */
public class MobileApiFilter implements Filter {
    @Autowired
    private UserSvc userSvc;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        String urlString = httpServletRequest.getRequestURI();
        if(!Validate.isString(urlString) || PASS_URL.contains(urlString)){

        }else{
            //手机端的请求
            if(Validate.isString(urlString) && urlString.startsWith(START_URL)){
                String token = request.getParameter(SYS_TOKEN_KEY);
                if(Validate.isString(token) && Validate.isObject(userSvc.findByToken(token))){

                }else{
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    ResultMsg resultMsg = ResultMsg.error("没有权限");
                    resultMsg.setReCode("NOT_AUTH");
                    out.print(JSON.toJSONString(resultMsg));
                    out.close();
                    return ;
                }
            }
        }

        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
