package com.sion.concertbooking.intefaces.presentation.accesslog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.ZonedDateTime;

@Order(0)
@Slf4j
@Component
@Aspect
public class AccessLogAspect {

    private final LogManager accessLogManager;

    public AccessLogAspect(LogManager accessLogManager) {
        this.accessLogManager = accessLogManager;
    }

    @Around("@annotation(LogMapping)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogMapping logMapping = signature.getMethod().getAnnotation(LogMapping.class);
        LogGroup logGroup = logMapping.logGroup();

        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();

            AccessLog accessLog = createAccessLog(request, startTime, endTime);

            accessLogManager.write(accessLog, logGroup);
        }

    }

    private AccessLog createAccessLog(
            HttpServletRequest request,
            long startTime, long endTime
    ) {
        String clientIp = request.getRemoteAddr();
        String serverIp = request.getLocalAddr();
        ZonedDateTime time = ZonedDateTime.now();
        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        String requestProtocol = request.getProtocol();
        String referer = request.getHeader("Referer");
        String userAgent = request.getHeader("User-Agent");
        int requestBodySize = request.getContentLength();
        long elapsedTime = endTime - startTime;

        return new AccessLog(
                clientIp, serverIp, time, requestMethod, requestUri, requestProtocol,
                referer, userAgent, requestBodySize, startTime, endTime, elapsedTime
        );
    }
}
