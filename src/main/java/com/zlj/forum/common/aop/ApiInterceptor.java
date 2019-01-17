package com.zlj.forum.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author tori
 * @description
 * @date 2018/9/3 下午1:33
 */

@Slf4j
@Aspect
@Component
public class ApiInterceptor {

    @Pointcut("execution(public * com.zlj.forum.web.controller..*.*(..))")
    public void api() {};


    @Around("api()")
    public Object invoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String method = proceedingJoinPoint.getSignature().getName();
        Object result;
        String success = "success";

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            success = "fail";
            throw e;
        } finally {
            long time = System.currentTimeMillis() - start;
            log.info("({}.{}, {}, {})", className, method, success, time);
        }
        return result;
    }
}
