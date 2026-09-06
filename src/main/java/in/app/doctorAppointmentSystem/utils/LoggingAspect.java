package in.app.doctorAppointmentSystem.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // ---------- NAMED POINTCUT (the "relative" reusable one) ----------
    // This defines the pointcut ONCE. Other advices can reference it by method name
    // instead of repeating the same execution() expression everywhere.
    @Pointcut("execution(* in.app.doctorAppointmentSystem.services.*.*(..))")
    public void serviceLayerMethods() {
        // empty body — this method is just a named handle for the pointcut expression
    }

    // ---------- ADVICE 1: uses execution() directly (inline, one-off) ----------
    @Before("execution(* in.app.doctorAppointmentSystem.services.*.signUp(..))")
    public void logSignUpAttempt(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        logger.info("Attempting signUp with args: {}", Arrays.toString(args));
    }

    // ---------- ADVICE 2: uses the NAMED pointcut by reference ----------
    @Before("serviceLayerMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Entering method: {} with args: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // ---------- ADVICE 3: also uses the NAMED pointcut, different advice type ----------
    @AfterReturning(pointcut = "serviceLayerMethods()", returning = "result")
    public void logMethodSuccess(JoinPoint joinPoint, Object result) {
        logger.info("Method {} completed successfully. Return value: {}",
                joinPoint.getSignature().toShortString(), result);
    }

    // ---------- ADVICE 4: uses the NAMED pointcut for failure logging ----------
    @AfterThrowing(pointcut = "serviceLayerMethods()", throwing = "exception")
    public void logMethodFailure(JoinPoint joinPoint, Throwable exception) {
        logger.error("Method {} threw exception: {}",
                joinPoint.getSignature().toShortString(), exception.getMessage());
    }

    // ---------- ADVICE 5: @Around using execution() directly (execution-time measuring) ----------
    @Around("execution(* in.app.doctorAppointmentSystem.services.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // actually runs the real method
        long end = System.currentTimeMillis();
        logger.info("Method {} executed in {} ms",
                joinPoint.getSignature().toShortString(), (end - start));
        return result;
    }
}