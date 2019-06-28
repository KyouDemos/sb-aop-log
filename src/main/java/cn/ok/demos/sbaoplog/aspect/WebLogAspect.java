package cn.ok.demos.sbaoplog.aspect;

import cn.ok.demos.sbaoplog.annotation.ControllerTraceLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.sun.org.glassfish.external.statistics.impl.StatisticImpl.START_TIME;

/**
 * @author kyou on 2019-06-24 07:15
 */
@Aspect
@Component
@Order(100)
@Slf4j
public class WebLogAspect {
    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    @Pointcut("execution(* cn.ok.demos.sbaoplog.controller..*.*(..))")
    public void webLog() {
    }


    @Before(value = "webLog() &&  @annotation(controllerTraceLog)")
    public void doBefore(JoinPoint joinPoint, ControllerTraceLog controllerTraceLog) {
        // 开始时间。
        long startTime = System.currentTimeMillis();
        Map<String, Object> threadInfo = new HashMap<>();
        threadInfo.put(START_TIME, startTime);
        threadLocal.set(threadInfo);

        // 请求参数。
        StringBuilder requestStr = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            requestStr.append(arg.toString());
        }

        log.info("{} Start, args={}", controllerTraceLog.name(), requestStr.toString());
    }

    @AfterReturning(value = "webLog() && @annotation(controllerTraceLog)", returning = "res")
    public void doAfterReturning(ControllerTraceLog controllerTraceLog, Object res) {
        Map<String, Object> threadInfo = threadLocal.get();
        long takeTime = System.currentTimeMillis() - (long) threadInfo.getOrDefault(START_TIME, System.currentTimeMillis());

        threadLocal.remove();
        log.info("{} End, Used Time={} ms, result={}", controllerTraceLog.name(), takeTime, res);
    }

    @AfterThrowing(value = "webLog() &&  @annotation(controllerTraceLog)", throwing = "throwable")
    public void doAfterThrowing(ControllerTraceLog controllerTraceLog, Throwable throwable) {
        threadLocal.remove();
        log.error("{} Exception: ", controllerTraceLog.name(), throwable);
    }
}
