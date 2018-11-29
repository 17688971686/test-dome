package cs.common;

import cs.service.sys.RoleServiceImpl;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/*@ControllerAdvice*/
@Deprecated
public class GlobalDefaultExceptionHandler {
	private static Logger logger = Logger.getLogger(GlobalDefaultExceptionHandler.class);
	
	public static final String DEFAULT_ERROR_VIEW = "error";

	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody
	public ResultMsg illegalErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setReMsg(e.getMessage());
		resultMsg.setReCode("555");
		logger.warn( e.getMessage());
		return resultMsg;
	}

	@ExceptionHandler(value ={ UnauthenticatedException.class,AuthorizationException.class})
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public String unAuthErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		logger.warn("登录信息失效或您没有权限!");
		return "error/401";
	}

	@ExceptionHandler(value = Exception.class)
	public void  errorHandler(HttpServletRequest req, Exception e) throws Exception {
		
		logger.error(e.getMessage());
		throw e;		
		
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.
		// if (AnnotationUtils.findAnnotation(e.getClass(),
		// ResponseStatus.class) != null)
		// throw e;

		// // Otherwise setup and send the user to a default error-view.
		// ModelAndView mav = new ModelAndView();
		// mav.addObject("exception", e);
		// mav.addObject("url", req.getRequestURL());
		// mav.setViewName(DEFAULT_ERROR_VIEW);
		// return mav;
	}
}
