package com.engtalkmo.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class QueryLoggingAspect {

    @Around("execution(* com.engtalkmo.domain..*(..))")
    public Object logQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Method {} executed in {} ms", joinPoint.getSignature(), elapsedTime);
        return result;
    }
}
