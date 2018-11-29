package cs.expert;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sn.framework.common.SnRuntimeException;
import cs.common.ResultMsg;
import cs.common.utils.ResponseUtils;
import cs.common.utils.Validate;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;


/**
 * 全局错误处理控制器
 *
 * @author tzg
 * @date 2017/9/15.
 */
@ControllerAdvice
public class GlobalErrorHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    private final static String ERROR_URL = "admin/error";

    @Autowired
    private HttpServletRequest request;

    /**
     * 4缺少请求参数的处理
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView requestParameterException(MissingServletRequestParameterException e, HttpServletResponse response) {
        logger.error("缺少请求参数", e);
        return getErrorView(response, HttpStatus.BAD_REQUEST, "缺少请求参数");
    }

    /**
     * 参数转换错误的处理
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView writableException(HttpMessageNotWritableException e, HttpServletResponse response) {
        logger.error("数据转换失败", e);
        return getErrorView(response, HttpStatus.BAD_REQUEST, "数据转换失败");
    }

    /**
     * 参数解析错误的处理
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView readableException(HttpMessageNotReadableException e, HttpServletResponse response) {
        logger.error("无效的数据格式", e);
        String error = "数据解析失败";
        if (e.getCause() instanceof InvalidFormatException) {
            error = "无效的数据格式";
            InvalidFormatException invalidFormatException = (InvalidFormatException) e.getCause();
            Class invalidType = invalidFormatException.getTargetType();
            // 更多数据格式的判断
            if (invalidType == Date.class) {
                error = "无效的日期格式";
            }
        }
        return getErrorView(response, HttpStatus.BAD_REQUEST, error);
    }

    /**
     * 返回错误视图
     *
     * @param response
     * @param status
     * @param error
     * @return
     */
    public ModelAndView getErrorView(HttpServletResponse response, HttpStatus status, String error) {
        if (Validate.isJsonContent(request)) {
            ResponseUtils.printJsonMsg(response, status, ResultMsg.error(error));
            return null;
        } else {
            return new ModelAndView(ERROR_URL).addObject("error", error);
        }
    }

    /**
     * 上传的文件超过限制
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView maxUploadSizeExceeded(Exception e, HttpServletResponse response) {
        logger.error("上传的文件超过限制", e);
        return getErrorView(response, HttpStatus.BAD_REQUEST, "上传的文件超过限制");
    }

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        logger.error("参数验证失败", e);
        return getErrorView(response, HttpStatus.BAD_REQUEST, fieldErrorsResult(e.getBindingResult()));
    }

    /**
     * 返回字段校验的错误信息
     *
     * @param result
     * @return
     */
    protected String fieldErrorsResult(BindingResult result) {
        List<FieldError> error = result.getFieldErrors();
        StringBuilder errorMsg = new StringBuilder();
        error.forEach(x -> {
            String field = x.getField();
            String msg = x.getDefaultMessage();
            logger.error(String.format("%s:%s", field, msg));
            if (errorMsg.length() > 0) {
                errorMsg.append(SEPARATE_COMMA);
            }
            errorMsg.append(msg);
        });
        return errorMsg.toString();
    }

    /**
     * 4参数绑定失败的处理
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException e, HttpServletResponse response) {
        logger.error("参数绑定失败", e);
        return getErrorView(response, HttpStatus.BAD_REQUEST, fieldErrorsResult(e.getBindingResult()));
    }
    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView requestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        logger.error("不支持当前请求方法", e);
        return getErrorView(response, HttpStatus.METHOD_NOT_ALLOWED, "不支持当前请求方法");
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ModelAndView handleHttpMediaTypeNotSupportedException(Exception e, HttpServletResponse response) {
        logger.error("不支持当前媒体类型", e);
        return getErrorView(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "不支持当前媒体类型");
    }

    /**
     * 缺少权限，无法操作
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView unauthorizedErrorHandler(UnauthorizedException e, HttpServletResponse response) {
        logger.error("缺少权限，无法操作", e);
        return getErrorView(response, HttpStatus.UNAUTHORIZED, "缺少权限，无法操作");
    }

    /**
     * 业务逻辑异常
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(SnRuntimeException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ModelAndView snErrorHandler(SnRuntimeException e, HttpServletResponse response) {
        logger.error("业务逻辑异常", e);
        return getErrorView(response, HttpStatus.PRECONDITION_FAILED, e.getMessage());
    }

    /**
     * 业务逻辑异常
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ModelAndView illegalErrorHandler(Exception e, HttpServletResponse response) {
        logger.error("业务逻辑异常", e);
        return getErrorView(response, HttpStatus.PRECONDITION_FAILED, e.getMessage());
    }

    /**
     * 操作数据库出现异常:名称重复，外键关联，数据异常
     */
    @ExceptionHandler({DataIntegrityViolationException.class, PersistenceException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception e, HttpServletResponse response) {
        logger.error("操作数据库出现异常:", e);
        String msg = "数据异常:";
        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            msg += "违反数据约束";
        }else{
            msg += e.getMessage();
        }
        return getErrorView(response, HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }

    /**
     * 操作数据库出现异常:名称重复，外键关联
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(InvalidDataAccessApiUsageException e, HttpServletResponse response) {
        logger.error("参数有误:", e);
        return getErrorView(response, HttpStatus.INTERNAL_SERVER_ERROR, "参数有误");
    }

    /**
     * 通用异常处理
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView errorHandler(Exception e, HttpServletResponse response) {
        logger.error("通用异常 URI[" + request.getRequestURI() + "] - [{}]", e);
        return getErrorView(response, HttpStatus.PRECONDITION_FAILED, e.getMessage());
    }

}
