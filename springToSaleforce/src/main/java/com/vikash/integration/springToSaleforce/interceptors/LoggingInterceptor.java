package com.vikash.integration.springToSaleforce.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    
    private final String loggedStartTimeKey = "_loggedStartingTime"; 
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(loggedStartTimeKey, startTime);
        log.info("Request Started: method={} path={}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        long loggedStartTime = (long) request.getAttribute(loggedStartTimeKey);
        long endTime = System.currentTimeMillis();
        long timeTakenMs = endTime - loggedStartTime;
        log.info("Request Completed: method={} path={} timeTaken={} (milliseconds)", request.getMethod(), request.getRequestURI(), timeTakenMs);
    }
}
