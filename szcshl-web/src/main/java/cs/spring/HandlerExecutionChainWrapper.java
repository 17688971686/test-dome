package cs.spring;

import cs.common.utils.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldm on 2018/11/22 0022.
 */
public class HandlerExecutionChainWrapper extends HandlerExecutionChain {

    private BeanFactory beanFactory;
    private HttpServletRequest request;
    private HandlerMethod handlerWrapper;
    private byte[] lock = new byte[0];

    public HandlerExecutionChainWrapper(HandlerExecutionChain chain,
                                        HttpServletRequest request,
                                        BeanFactory beanFactory) {
        super(chain.getHandler(), chain.getInterceptors());
        this.request = request;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getHandler() {
        if (handlerWrapper != null) {
            return handlerWrapper;
        }

        synchronized (lock) {
            if (handlerWrapper != null) {
                return handlerWrapper;
            }
            HandlerMethod superMethodHandler = (HandlerMethod) super.getHandler();
            Object proxyBean = createProxyBean(superMethodHandler);
            handlerWrapper = new HandlerMethod(proxyBean, superMethodHandler.getMethod());
            return handlerWrapper;
        }

    }

    /**
     * 为Controller Bean创建一个代理实例,以便用于 实现调用真实Controller Bean前的切面拦截
     * 用以过滤方法参数中可能的XSS注入
     *
     * @param handler
     * @return
     */
    private Object createProxyBean(HandlerMethod handler) {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(handler.getBeanType());
            Object bean = handler.getBean();
            if (bean instanceof String) {
                bean = beanFactory.getBean((String) bean);
            }
            ControllerXssInterceptor xss = new ControllerXssInterceptor(bean);
            xss.setRequest(this.request);
            enhancer.setCallback(xss);
            return enhancer.create();
        } catch (Exception e) {
            throw new IllegalStateException("为Controller创建代理失败:" + e.getMessage(), e);
        }
    }


    public static class ControllerXssInterceptor implements MethodInterceptor {

        private Object target;
        private HttpServletRequest request;
        private List<String> objectMatchPackages;

        public ControllerXssInterceptor(Object target) {
            this.target = target;
            this.objectMatchPackages = new ArrayList<String>();
            this.objectMatchPackages.add("cs");
        }

        public void setRequest(HttpServletRequest request) {
            this.request = request;
        }


        @Override
        public Object intercept(Object obj, Method method, Object[] args,
                                MethodProxy proxy)
                throws Throwable {

            //对Controller的方法参数进行调用前处理
            //过滤String类型参数中可能存在的XSS注入
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null)
                        continue;

                    if (args[i] instanceof String) {
                        args[i] = StringUtil.cleanXSS((String) args[i]);
                        continue;
                    }

                    for (String pk : objectMatchPackages) {
                        if (args[i].getClass().getName().startsWith(pk)) {
                            objectXssReplace(args[i]);
                            break;
                        }
                    }
                }
            }
            return method.invoke(target, args);
        }

        private void objectXssReplace(final Object argument) {
            if (argument == null)
                return;

            ReflectionUtils.doWithFields(argument.getClass(), new ReflectionUtils.FieldCallback() {

                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    String fv = (String) field.get(argument);
                    if (fv != null) {
                        String nv = StringUtil.cleanXSS(fv);
                        field.set(argument, nv);
                    }
                }

            }, new ReflectionUtils.FieldFilter() {

                @Override
                public boolean matches(Field field) {
                    boolean typeMatch = String.class.equals(field.getType());

                    if (request != null && (RequestMethod.GET.toString()).equals(request.getMethod())) {
                        boolean requMatch = request.getParameterMap().containsKey(field.getName());
                        return typeMatch && requMatch;
                    }

                    return typeMatch;
                }

            });
        }
    }


}

