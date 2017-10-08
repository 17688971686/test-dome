package cs.ahelper;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/9/30 0030.
 */
@Target({ElementType.TYPE})       //字段注解
@Retention(RetentionPolicy.RUNTIME) //在运行期保留注解信息
@Documented     //在生成javac时显示该注解的信息
@Inherited      //标明MyAnnotation1注解可以被使用它的子类继承
public @interface IgnoreAnnotation {
    String value() default "";
}
