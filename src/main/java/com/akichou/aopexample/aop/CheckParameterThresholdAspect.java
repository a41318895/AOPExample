package com.akichou.aopexample.aop;

import com.akichou.aopexample.annotation.CheckParameterThreshold;
import com.akichou.aopexample.exception.ThresholdExceededException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
@Aspect
public class CheckParameterThresholdAspect {

    @Pointcut("execution(* com.akichou.aopexample.controller.Controller.*(..))")
    public void checkParameterThresholdPointcut() {}

    @Around("checkParameterThresholdPointcut()")
    public Object checkParameterThreshold(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        StringBuilder exceededParams = new StringBuilder();
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof CheckParameterThreshold checkParameterThreshold) {
                    long threshold = checkParameterThreshold.value();
                    if (args[i] instanceof Number && ((Number) args[i]).longValue() > threshold) {
                        exceededParams.append("Parameter[").append(i).append("] with value ")
                                .append(args[i]).append(" exceeds threshold: ").append(threshold).append("; ");
                    }
                }
            }
        }

        if (!exceededParams.isEmpty()) {
            throw new ThresholdExceededException("Method [" + methodName + "] - " + exceededParams);
        }

        return joinPoint.proceed();
    }
}
