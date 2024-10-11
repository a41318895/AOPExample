package com.akichou.aopexample.controller;

import com.akichou.aopexample.annotation.CheckParameterThreshold;
import com.akichou.aopexample.annotation.CheckReturnValueThreshold;
import com.akichou.aopexample.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class Controller {

    private final Service service;

    @GetMapping(value =  "/user/{userId}")
    public String getUserById(@PathVariable("userId") Long userId, @RequestParam("userName") String userName) {

        return service.getUserById(userId, userName) ;
    }

    @GetMapping(value = "/test/param")
    public Long getValuesForTestingCheckParameterThresholdAOP(
            @RequestParam("testValue") @CheckParameterThreshold Long testValue,
            @RequestParam("testValue2") Long testValue2,
            @RequestParam("testValue3") @CheckParameterThreshold Long testValue3) {

        return testValue + testValue2 + testValue3 ;
    }

    @GetMapping(value = "/test/returnValue")
    @CheckReturnValueThreshold
    public Long getValuesForTestingCheckReturnValueAOP(
            @RequestParam("testValue") Long testValue,
            @RequestParam("testValue2") Long testValue2) {

        return testValue + testValue2 ;
    }

}
