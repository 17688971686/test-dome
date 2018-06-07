package cs.ahelper;

import java.lang.annotation.*;

/**
 * 日记注解
 * Created by ldm on 2018/6/6 0006.
 */
@Retention(RetentionPolicy.RUNTIME)	//注解会在class中存在，运行时可通过反射获取
@Target(ElementType.METHOD)			//目标是方法
@Documented
public @interface LogMsg {
    /**
     * 模块
     * @return
     */
    String module() default "";

    /**
     * 日记级别
     * @return
     */
    String logLevel() default "3";

}
