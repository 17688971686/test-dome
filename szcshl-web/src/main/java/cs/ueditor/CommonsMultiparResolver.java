package cs.ueditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 自定义文件上传过滤器，否则ueditor文件上传不了
 * @author liangdengming
 *
 */
public class CommonsMultiparResolver extends CommonsMultipartResolver {
	
	private static final Logger log = LoggerFactory.getLogger(CommonsMultiparResolver.class);
    @Override  
    public boolean isMultipart(javax.servlet.http.HttpServletRequest request) {  
       String uri = request.getRequestURI();        
       //过滤使用百度UEditor的URI  
       if (uri.indexOf("ueditor/dispatch") > 0) {     //此处拦截路径即为上面编写的controller路径
    	   log.debug("commonsMultipartResolver 放行");;
    	   return false;  
       }  
       return super.isMultipart(request);  
    }  
}
