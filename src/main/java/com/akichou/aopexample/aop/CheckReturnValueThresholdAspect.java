package com.akichou.aopexample.aop;

import com.akichou.aopexample.annotation.CheckReturnValueThreshold;
import com.akichou.aopexample.exception.ThresholdExceededException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CheckReturnValueThresholdAspect {

    @Pointcut("@annotation(com.akichou.aopexample.annotation.CheckReturnValueThreshold)")
    public void checkReturnValueThresholdPointcut() {}

    @Around("checkReturnValueThresholdPointcut()")
    public Object checkReturnValueThreshold(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature() ;

        CheckReturnValueThreshold checkReturnValueThreshold =
                signature.getMethod().getAnnotation(CheckReturnValueThreshold.class);
        long threshold = checkReturnValueThreshold.value() ;

        Object result = joinPoint.proceed() ;
        long returnValue = Long.parseLong(result.toString()) ;
        if(returnValue > threshold) {
            throw new ThresholdExceededException("The return value: " + returnValue +" exceeded the threshold: 1000") ;
        }

        return result ;
    }
}
