package cs.ahelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cs.common.RandomGUID;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.RequestUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.UserAgentUtils;
import cs.common.utils.Validate;
import cs.domain.sys.Log;
import cs.service.sys.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by ldm on 2018/6/6 0006.
 */
@Aspect
@Component
public class LogAspect {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LogAspect.class);

    private Object resultObj = null;
    private String resultState = Constant.EnumState.YES.getValue();

    @Autowired
    private LogService logService;

    /**
     * 定义Pointcut，Pointcut的名称，此方法不能有返回值，该方法只是一个标示
     */
    @Pointcut("@annotation(cs.ahelper.LogMsg)")
    public void controllerAspect() {
        //System.out.println("我是一个切入点");
    }

    /**
     * 前置通知（Before advice） ：在某连接点（JoinPoint）之前执行的通知，但这个通知不能阻止连接点前的执行。
     *
     * @param joinPoint
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        //System.out.println("=====LogAspect前置通知开始=====");
        // handleLog(joinPoint, null);
    }

    /**
     * 后通知（After advice） ：当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。
     *
     * @param joinPoint
     */
    @AfterReturning(pointcut = "controllerAspect()")
    public void doAfter(JoinPoint joinPoint) {
        //System.out.println("=====LogAspect后置通知开始=====");
        handleLog(joinPoint, null);    // 等执行完SQL语句之后再记录，避免获取不到Session
    }

    /**
     * 抛出异常后通知（After throwing advice） ： 在方法抛出异常退出时执行的通知。
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "controllerAspect()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) {
        //System.out.println("=====LogAspect异常通知开始=====");
        resultState = Constant.EnumState.NO.getValue();
        // handleLog(joinPoint, e);
    }

    /**
     * 环绕通知（Around advice）
     * ：包围一个连接点的通知，类似Web中Servlet规范中的Filter的doFilter方法。可以在方法的调用前后完成自定义的行为
     * ，也可以选择不执行。
     *
     * @param joinPoint
     */
    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //System.out.println("=====LogAspect 环绕通知开始=====");
        //handleLog(joinPoint, null);
        resultObj = joinPoint.proceed();
        //System.out.println("=====LogAspect 环绕通知结束=====");
        return resultObj;
    }

    /**
     * 日志处理
     *
     * @param joinPoint
     * @param e
     */
    private void handleLog(JoinPoint joinPoint, Exception e) {
        try {
            LogMsg logger = giveController(joinPoint);
            if (logger == null) {
                return;
            }
            Log log = new Log();
            HttpServletRequest request = RequestUtils.getHttpServletRequest();
            //IP地址
            log.setIpAdd(RequestUtils.getIpAddr(request));
            //操作用户
            String userName = Validate.isString(SessionUtil.getDisplayName()) ? SessionUtil.getDisplayName() : "匿名用户";
            log.setUserName(userName);
            //操作日期
            log.setCreatedDate(new Date());
            //所属模块
            log.setModule(logger.module());
            //日志级别
            log.setLogLevel(logger.logLevel());

            String signature = joinPoint.getSignature().toString(); // 获取目标方法签名
            String methodName = signature.substring(signature.lastIndexOf(".") + 1, signature.indexOf("("));

            String classType = joinPoint.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(classType);
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(LogMsg.class) && method.getName().equals(methodName)) {
                    String clazzName = clazz.getName();
                    //类名
                    log.setLogger(clazzName);
                    //方法名
                    log.setLogMethod(methodName);
                }
            }
            //浏览器信息
            log.setBrowserInfo(UserAgentUtils.getBrowserInfo(request));
            //参数信息
            String params = "";
            //1、处理request中入参，但获取不到注解的bean
            Enumeration em = request.getParameterNames();
            while (em.hasMoreElements()) {
                String name = (String) em.nextElement();
                String value = request.getParameter(name);
                params += name + "=" + value + "，";
            }
            params += ";";


            log.setParamsInfo("参数:" + params);
            if (Validate.isObject(resultObj)) {
                if(resultObj instanceof ResultMsg){
                    ResultMsg resultMsg = (ResultMsg) resultObj;
                    if(resultMsg.isFlag()){
                        resultState = Constant.EnumState.YES.getValue();
                    }else {
                        resultState = Constant.EnumState.NO.getValue();
                    }
                    log.setLogCode(resultMsg.getReCode());
                }
                log.setMessage(JSON.toJSONString(resultObj));
            }
            log.setResult(resultState);
            logService.save(log);    //保存日志对象
        } catch (Exception exp) {
            resultState = Constant.EnumState.NO.getValue();
            logger.error("异常信息:{}", exp);
            exp.printStackTrace();
        }
    }

    /**
     * 获得注解
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private static LogMsg giveController(JoinPoint joinPoint)
            throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(LogMsg.class);
        }
        return null;
    }
}
