package com.sn.framework.core.web;

import com.sn.framework.common.IdWorker;
import com.sn.framework.common.SystemClock;
import com.sn.framework.core.common.JacksonUtils;
import com.sn.framework.core.common.SNKit;
import com.sn.framework.module.sys.model.OperatorLogDto;
import com.sn.framework.module.sys.service.IOperatorLogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Description: 系统操作日志（针对controller）
 * Author: tzg
 * Date: 2017/9/11 13:59
 */
@Aspect
@Component
public class ControllerLogAspect {

    private static Logger logger = LoggerFactory.getLogger(ControllerLogAspect.class);

    /** 开始时间 */
    private long startTime = 0L;
    /** 结束时间 */
    private long endTime = 0L;

    @Autowired
    private IOperatorLogService operatorLogService;

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = getHttpServletRequest();
        if (!RequestMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            logger.debug("operatorLog doBefore");
            startTime = SystemClock.now();
        }
    }

    @After("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void doAfter(JoinPoint joinPoint) {
        HttpServletRequest request = getHttpServletRequest();
        if (!RequestMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            logger.debug("operatorLog doAfter");
        }
    }

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 从注解中获取操作名称、获取响应结果
        try {
            Object result = pjp.proceed();
            recordLog(pjp, result, true);
            return result;
        } catch (Exception e) {
            recordLog(pjp, e.getMessage(), false);
            throw e;
        }
    }

    /**
     * 记录日志
     * @param pjp
     * @param result
     * @param operateType
     */
    private void recordLog(ProceedingJoinPoint pjp, Object result, boolean operateType) {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (null != subject && subject.isAuthenticated()) {
                // 获取request
                HttpServletRequest request = getHttpServletRequest();
                if (!RequestMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
                    // 拦截的方法参数类型
                    Signature sig = pjp.getSignature();
                    Class<?> cls = sig.getDeclaringType();
                    if (cls.isAnnotationPresent(RequestMapping.class) && sig instanceof MethodSignature) {
                        // 获得被拦截的方法
                        Method method = ((MethodSignature) sig).getMethod();
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);

                            OperatorLogDto myLog = new OperatorLogDto();
                            myLog.setId(IdWorker.get32UUID());
                            myLog.setOperateName(rm.name());
                            myLog.setOperateMethod(request.getMethod());

                            myLog.setOperateUri(request.getRequestURI());
                            myLog.setIpAddress(SNKit.getRealIp(request));

                            String userAgentStr = request.getHeader("User-Agent");
                            UserAgent userAgent = UserAgentUtil.getUserAgent(userAgentStr);
                            logger.debug("请求来自：{}", userAgent);
                            myLog.setClientIdentify(userAgentStr);
                            myLog.setUserAgent(userAgent.toString());
                            myLog.setClientType(userAgent.getClientType().getTypeName());

                            if (null != result) {
                                String msg;
                                if (result instanceof CharSequence) {
                                    msg = result.toString();
                                } else {
                                    msg = JacksonUtils.objectToString(result);
                                }
                                if (msg.length() > 1900) {
                                    msg = msg.substring(0, 1900) + "...";
                                }
                                myLog.setMessage(msg);
                            }
                            myLog.setOperateType(operateType ? "1" : "0");

                            endTime = SystemClock.now();
                            long operateTime = endTime - startTime;
                            myLog.setOperateTime(operateTime);
                            logger.debug("doAfterReturning ===>> result={}, 耗时：{}", result, operateTime);

                            operatorLogService.create(myLog);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("***操作请求日志记录失败***", e);
        }
    }

    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

}