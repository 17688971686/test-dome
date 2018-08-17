package com.sn.framework.core.syslog;

import com.sn.framework.common.IdWorker;
import com.sn.framework.common.SystemClock;
import com.sn.framework.core.common.JacksonUtils;
import com.sn.framework.core.common.ResultMsg;
import com.sn.framework.core.common.SNKit;
import com.sn.framework.core.common.Validate;
import com.sn.framework.core.util.ReflectionUtils;
import com.sn.framework.core.web.UserAgent;
import com.sn.framework.core.web.UserAgentUtil;
import com.sn.framework.module.sys.model.OperatorLogDto;
import com.sn.framework.module.sys.service.IOperatorLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Description: 系统操作日志（针对controller）
 * Author: tzg
 * Date: 2017/9/11 13:59
 */
@Aspect
@Component
public class LogAspect {

    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);
    /**
     * 日志对象
     */
    private OperatorLogDto operateLog = new OperatorLogDto();
    /**
     * 旧对象
     */
    private Object oldObject;
    /**
     * 新对象
     */
    private Object newObject;

    /**
     * 返回结果
     */
    private Object resultObj;
    /**
     * 开始时间
     */
    private long startTime = 0L;
    /**
     * 结束时间
     */
    private long endTime = 0L;

    @Autowired
    private IOperatorLogService operatorLogService;

    @Before("@annotation(sysLog)")
    public void doBefore(JoinPoint joinPoint, SysLog sysLog) {
        logger.debug("operatorLog doBefore");
        startTime = SystemClock.now();
        //操作类型
        operateLog.setOperateType(sysLog.operatorType());
        operateLog.setBusinessType(sysLog.businessType());
        //方法参数
        Object[] args = joinPoint.getArgs();
        try {
            if (Validate.isObject(args) && args.length > 0) {
                newObject = args[0];
                operateLog.setNewInfo(JacksonUtils.objectToString(newObject));
                if (OperatorType.UPDATE.equals(sysLog.operatorType())) {
                    Object idValue = ReflectionUtils.getFieldValue(newObject, sysLog.idName());
                    ContentParser contentParser = (ContentParser) sysLog.parseclass().newInstance();
                    oldObject = contentParser.getResult(idValue, sysLog);
                    if (Validate.isObject(oldObject)) {
                        operateLog.setOldInfo(JacksonUtils.objectToString(oldObject));
                        //对比具体的修改项
                        List<Map<String, Object>> changelist = ReflectionUtils.compareTwoClass(oldObject, newObject,sysLog.ingoreProps());
                        StringBuilder str = new StringBuilder();
                        for (Map<String, Object> map : changelist) {
                            str.append("【" + map.get("name") + "】从【" + map.get("old") + "】改为了【" + map.get("new") + "】;\n");
                        }
                        operateLog.setUpdateInfo(str.toString());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("service加载失败:", e);
        }
    }

    @After("@annotation(sysLog)")
    public void doAfter(JoinPoint joinPoint, SysLog sysLog) {
        recordLog(joinPoint, null, true);    // 等执行完SQL语句之后再记录，避免获取不到Session
    }

    @Around("@annotation(sysLog)")
    public Object doAround(ProceedingJoinPoint pjp, SysLog sysLog) throws Throwable {
        // 从注解中获取操作名称、获取响应结果
        try {
            resultObj = pjp.proceed();
            return resultObj;
        } catch (Exception e) {
            recordLog(pjp, e.getMessage(), false);
            throw e;
        }
    }

    /**
     * 记录日志
     *
     * @param joinPoint
     * @param result
     */
    private void recordLog(JoinPoint joinPoint, Object result, boolean isSucess) {
        try {
            // 获取request
            HttpServletRequest request = getHttpServletRequest();
            //基础信息
            operateLog.setId(IdWorker.get32UUID());
            operateLog.setOperateMethod(request.getMethod());
            operateLog.setOperateUri(request.getRequestURI());
            operateLog.setIpAddress(SNKit.getRealIp(request));
            //浏览器信息
            String userAgentStr = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgentUtil.getUserAgent(userAgentStr);

            operateLog.setClientIdentify(userAgentStr);
            operateLog.setUserAgent(userAgent.toString());
            operateLog.setClientType(userAgent.getClientType().getTypeName());

            if (Validate.isObject(resultObj)) {
                if (null != result) {
                    String msg;
                    if (result instanceof CharSequence) {
                        msg = result.toString();
                    } else if (resultObj instanceof ResultMsg) {
                        msg = JacksonUtils.objectToString(result);
                        isSucess = ((ResultMsg) resultObj).isFlag();
                    } else {
                        msg = JacksonUtils.objectToString(result);
                    }
                    if (msg.length() > 1900) {
                        msg = msg.substring(0, 1900) + "...";
                    }
                    operateLog.setMessage(msg);
                }
            }
            operateLog.setSucessFlag(isSucess ? "1" : "0");
            endTime = SystemClock.now();
            long operateTime = endTime - startTime;
            operateLog.setOperateTime(operateTime);
            operatorLogService.create(operateLog);
        } catch (Exception e) {
            logger.error("***操作请求日志记录失败***", e);
        }
    }

    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

}