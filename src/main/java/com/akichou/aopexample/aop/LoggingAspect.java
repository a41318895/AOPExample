package com.akichou.aopexample.aop;

import com.akichou.aopexample.annotation.Loggable;
import com.akichou.aopexample.annotation.selectionEnum.LogLevelEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class) ;

    // 切入點為 : 帶有@Loggable自定義註解的方法
    @Pointcut("@annotation(com.akichou.aopexample.annotation.Loggable)")
    public void loggablePointcut() {}

    // 方法前後都執行此邏輯
    @Around("loggablePointcut()")
    public Object loggableAround(ProceedingJoinPoint joinPoint) throws Throwable {

        // 獲取切入方法的簽章
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature() ;

        // 獲取切入方法簽章的切面方法, 再獲取觸發該切面方法的註解
        Loggable loggable = methodSignature.getMethod().getAnnotation(Loggable.class);
        LogLevelEnum logLevel = loggable.logLevel() ;
        boolean isLogParameters = loggable.isLogParameters() ;
        boolean isLogReturnValue = loggable.isLogReturnValue() ;

        // 是否需要印出方法之參數(型態為陣列)
        if(isLogParameters) {
            logMessage(logLevel, "Method Parameters: " + Arrays.toString(joinPoint.getArgs()));
        }

        // 是否需要印出方法之回傳值
        Object result = joinPoint.proceed() ;
        if(isLogReturnValue) {
            logMessage(logLevel, "Method Return Value: \"" + result + "\"") ;
        }

        return result ;
    }

    // 處理打印級別切換
    private void logMessage(LogLevelEnum logLevel, String logMessage) {

        switch (logLevel) {

            case INFO -> logger.info(logMessage) ;
            case DEBUG -> logger.debug(logMessage) ;
            case WARN -> logger.warn(logMessage) ;
            case ERROR -> logger.error(logMessage) ;
        }
    }

}
