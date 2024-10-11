package com.akichou.aopexample.aop;

import com.akichou.aopexample.annotation.Performanceable;
import com.akichou.aopexample.annotation.selectionEnum.PerformanceTimeUnitEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class) ;

    // 切入點為 : 帶有@Performancable自定義註解的方法
    @Pointcut("@annotation(com.akichou.aopexample.annotation.Performanceable)")
    public void performancePointcut() {}

    // 方法前後都執行此邏輯
    @Around("performancePointcut()")
    public Object performanceAround(ProceedingJoinPoint joinPoint) throws Throwable {

        // 獲取切入方法的簽章
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature() ;

        // 獲取切入方法簽章的切面方法, 再獲取觸發該切面方法的註解
        Performanceable performanceable = methodSignature.getMethod().getAnnotation(Performanceable.class) ;
        PerformanceTimeUnitEnum performanceTimeUnitEnum = performanceable.calculateTimeUnit() ;
        long threshold = performanceable.logPerformanceLowestThreshold() ;

        return changePerformanceTimeUnit(performanceTimeUnitEnum, threshold, joinPoint) ;
    }

    private Object changePerformanceTimeUnit(
            PerformanceTimeUnitEnum performanceTimeUnitEnum,
            long threshold,
            ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime ;
        Object result ;
        long spendTime ;

        switch (performanceTimeUnitEnum) {

            case NANO -> {

                startTime = System.nanoTime() ;
                result = joinPoint.proceed() ;
                spendTime = System.nanoTime() - startTime ;

                if(spendTime > threshold) logger.info("{} executed in {} nanoseconds.", joinPoint.getSignature(), spendTime) ;
            }

            case MILLI -> {

                startTime = System.currentTimeMillis() ;
                result = joinPoint.proceed() ;
                spendTime = System.currentTimeMillis() - startTime ;

                if(spendTime > threshold) logger.info("{} executed in {} milliseconds.", joinPoint.getSignature(), spendTime) ;
            }

            default -> throw new IllegalArgumentException("Unsupported Performance Time Unit : " + performanceTimeUnitEnum) ;
        }

        return result ;
    }
}
