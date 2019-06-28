package cn.ok.demos.sbaoplog.annotation;

import java.lang.annotation.*;

/**
 * @author kyou on 2019-06-20 11:45
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControllerTraceLog {
    String name();
}
