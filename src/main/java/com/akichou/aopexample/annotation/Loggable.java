package com.akichou.aopexample.annotation;

import com.akichou.aopexample.annotation.selectionEnum.LogLevelEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {

    LogLevelEnum logLevel() default LogLevelEnum.INFO ;

    boolean isLogParameters() default false ;

    boolean isLogReturnValue() default false ;
}
