package com.example.carrental.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging all application logic.
 *
 * @author Yury Raichonak
 */
@Aspect
@Component
@Slf4j
public class LoggerAspect {

  /**
   * Pointcut for logging CRUD methods with parameters.
   */
  @Pointcut(value = "execution(* com.example.carrental.*.*.create*(..)) || "
      + "execution(* com.example.carrental.*.*.update*(..)) || "
      + "execution(* com.example.carrental.*.*.searchCars*(..)) || "
      + "execution(* com.example.carrental.*.*.find*By*(..)) ||"
      + "execution(* com.example.carrental.*.*.login(..)) ||"
      + "execution(* com.example.carrental.*.*.delete(..)) ||"
      + "execution(* com.example.carrental.*.*.approve*(..)) ||"
      + "execution(* com.example.carrental.*.*.reject*(..)) ||"
      + "execution(* com.example.carrental.*.*.forgotPassword(..)) ||"
      + "execution(* com.example.carrental.*.*.usetUserInfo(..)) ||"
      + "execution(* com.example.carrental.*.*.send*(..)) ||"
      + "execution(* com.example.carrental.*.*.calculateTotalCost(..)) ||"
      + "execution(* com.example.carrental.*.*.completeOrder*(..)) ||"
      + "execution(* com.example.carrental.*.*.startRentalPeriod(..)) ||"
      + "execution(* com.example.carrental.*.*.cancelOrder*(..)) ||"
      + "execution(* com.example.carrental.*.*.payBill(..)) ||"
      + "execution(* com.example.carrental.*.*.get*Details*(..)) ||"
      + "execution(* com.example.carrental.*.*.getContactInformation(..)) ||"
      + "execution(* com.example.carrental.*.*.findRequest*Data(..)) ||"
      + "execution(* com.example.carrental.*.*.findNew*Amount*(..)) ||"
      + "execution(* com.example.carrental.*.*.setTranslation(..)) ||"
      + "execution(* com.example.carrental.service.impl.UserServiceImpl.checkExistedEmail(String)) ||"
      + "execution(* com.example.carrental.service.impl.UserServiceImpl.confirmEmail(String)) ||"
      + "execution(* com.example.carrental.*.*.upload*Image(..))")
  public void addOrGetOrUpdateEntityPointcut() {
  }

  /**
   * Pointcut for logging get methods.
   */
  @Pointcut(value = "execution(* com.example.carrental.*.*.findAll*(..))")
  public void getAllEntitiesPointcut() {
  }

  /**
   * Pointcut for logging methods with user personal data, without it.
   */
  @Pointcut(value = "execution(* com.example.carrental.*.*.getUser*(..)) ||"
      + "execution(* com.example.carrental.*.*.changePassword(..)) ||"
      + "execution(* com.example.carrental.*.*.sendEmailConfirmationMessage(..)) ||"
      + "execution(* com.example.carrental.*.*.loadUserByUsername(..)) ||"
      + "execution(* com.example.carrental.*.*.upload*File(..)) ||"
      + "execution(* com.example.carrental.*.*.downloadFiles(..)) ||"
      + "execution(* com.example.carrental.*.*.exportOrderToPDF(..)) ||"
      + "execution(* com.example.carrental.*.*.downloadImage(..)) ||"
      + "execution(* com.example.carrental.*.*.confirmEmail(..))")
  public void getInfoPointcut() {
  }

  /**
   * @param proceedingJoinPoint CRUD methods join point.
   */
  @Around("addOrGetOrUpdateEntityPointcut()")
  public Object addOrUpdateEntityLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    String methodName = proceedingJoinPoint.getSignature().getName();
    String className = proceedingJoinPoint.getTarget().getClass().toString();
    Object[] argsArray = proceedingJoinPoint.getArgs();
    log.debug("{} method {}() called, arguments: {}", className, methodName, argsArray);
    Object object = proceedingJoinPoint.proceed();
    log.debug("{} method {}() finished, response: {}", className, methodName, object);
    return object;
  }

  /**
   * @param proceedingJoinPoint get methods join point.
   */
  @Around("getAllEntitiesPointcut()")
  public Object getAllEntitiesLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    String methodName = proceedingJoinPoint.getSignature().getName();
    String className = proceedingJoinPoint.getTarget().getClass().toString();
    log.debug("{} method {}() called.", className, methodName);
    Object object = proceedingJoinPoint.proceed();
    log.debug("{} method {}() finished, response: {}", className, methodName, object);
    return object;
  }

  /**
   * @param proceedingJoinPoint methods with user personal data join point.
   */
  @Around("getInfoPointcut()")
  public Object getInfoLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    String methodName = proceedingJoinPoint.getSignature().getName();
    String className = proceedingJoinPoint.getTarget().getClass().toString();
    log.debug("{} method {}() called.", className, methodName);
    Object object = proceedingJoinPoint.proceed();
    log.debug("{} method {}() finished", className, methodName);
    return object;
  }
}
