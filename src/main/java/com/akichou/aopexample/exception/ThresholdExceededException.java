package com.akichou.aopexample.exception;

public class ThresholdExceededException extends RuntimeException {

    public ThresholdExceededException(String message) {
        super(message) ;
    }
}
