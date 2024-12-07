package com.prgrms.ijuju.domain.chat.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.MDC;

@Aspect
@Component
@Slf4j
public class ChatLoggingAspect {

    @Around("execution(* com.prgrms.ijuju.domain.chat.service.*.*(..))")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        MDC.put("className", className);
        MDC.put("methodName", methodName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("메서드 실행 시작: {}.{}", className, methodName);
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.info("메서드 실행 완료: {}.{}, 실행시간: {}ms", 
                    className, methodName, executionTime);
            
            return result;
        } catch (Exception e) {
            log.error("메서드 실행 실패: {}.{}, 에러: {}", 
                    className, methodName, e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
} 