package com.akichou.aopexample.annotation;

import com.akichou.aopexample.annotation.selectionEnum.PerformanceTimeUnitEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Performanceable {

    PerformanceTimeUnitEnum calculateTimeUnit() default PerformanceTimeUnitEnum.NANO ;

    long logPerformanceLowestThreshold() default 0 ;
}
