package com.akichou.aopexample.service;

import com.akichou.aopexample.annotation.Loggable;
import com.akichou.aopexample.annotation.Performanceable;
import com.akichou.aopexample.annotation.selectionEnum.LogLevelEnum;
import com.akichou.aopexample.annotation.selectionEnum.PerformanceTimeUnitEnum;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Override
    @Loggable(logLevel = LogLevelEnum.DEBUG, isLogParameters = true, isLogReturnValue = true)
    @Performanceable(calculateTimeUnit = PerformanceTimeUnitEnum.NANO, logPerformanceLowestThreshold = 100000)
    public String getUserById(Long userId, String userName) {

        return "User: " + userId + " ( " + userName + " )" ;
    }
}
