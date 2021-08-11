package com.example.carrental.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

  @Pointcut(value = "execution(* com.example.carrental.*.*.create*(..)) || "
      + "execution(* com.example.carrental.*.*.update*(..)) || "
      + "execution(* com.example.carrental.*.*.searchCars(..)) || "
      + "execution(* com.example.carrental.*.*.find*By*(..)) ||"
      + "execution(* com.example.carrental.*.*.login(..)) ||"
      + "execution(* com.example.carrental.*.*.approve*(..)) ||"
      + "execution(* com.example.carrental.*.*.reject*(..)) ||"
      + "execution(* com.example.carrental.*.*.forgotPassword(..)) ||"
      + "execution(* com.example.carrental.*.*.usetUserInfo(..)) ||"
      + "execution(* com.example.carrental.*.*.sendEmail(..)) ||"
      + "execution(* com.example.carrental.*.*.findNewMessagesAmount(..)) ||"
      + "execution(* com.example.carrental.service.impl.UserServiceImpl.loadUserByUsername(String)) ||"
      + "execution(* com.example.carrental.service.impl.UserServiceImpl.enableUser(Long)) ||"
      + "execution(* com.example.carrental.service.impl.UserServiceImpl.checkExistedEmail(String)) ||"
      + "execution(* com.example.carrental.service.impl.UserServiceImpl.confirmEmail(String)) ||"
      + "execution(* com.example.carrental.*.*.upload*Image(..))")
  public void addOrGetOrUpdateEntityPointcut(){
  }

  @Pointcut(value = "execution(* com.example.carrental.*.*.downloadImage(..))")
  public void getImagePointcut(){
  }

  @Pointcut(value = "execution(* com.example.carrental.*.*.findAll*(..))")
  public void getAllEntitiesPointcut(){
  }

  @Around("addOrGetOrUpdateEntityPointcut()")
  public Object addOrUpdateEntityLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    String methodName = proceedingJoinPoint.getSignature().getName();
    String className = proceedingJoinPoint.getTarget().getClass().toString();
    Object[] argsArray = proceedingJoinPoint.getArgs();
    log.info("{} method {}() called, arguments: {}", className, methodName, argsArray);
    Object object = proceedingJoinPoint.proceed();
    log.info("{} method {}() finished, response: {}", className, methodName, object);
    return object;
  }

  @Around("getAllEntitiesPointcut()")
  public Object getAllEntitiesLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    String methodName = proceedingJoinPoint.getSignature().getName();
    String className = proceedingJoinPoint.getTarget().getClass().toString();
    log.info("{} method {}() called.", className, methodName);
    Object object = proceedingJoinPoint.proceed();
    log.info("{} method {}() finished, response: {}", className, methodName, object);
    return object;
  }

  @Around("getImagePointcut()")
  public Object getImageLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    String methodName = proceedingJoinPoint.getSignature().getName();
    String className = proceedingJoinPoint.getTarget().getClass().toString();
    log.info("{} method {}() called.", className, methodName);
    Object object = proceedingJoinPoint.proceed();
    log.info("{} method {}() finished", className, methodName);
    return object;
  }
}
