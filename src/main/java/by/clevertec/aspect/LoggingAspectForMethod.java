package by.clevertec.aspect;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Aspect
@Component
@ConditionalOnProperty(prefix = "performance.monitor", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class LoggingAspectForMethod {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspectForMethod.class);

    private final DefaultPropertiesLoggingAspect properties;

    @Around("@annotation(by.clevertec.annotation.MonitorPerformance)")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        if (executionTime >= properties.getMinExecutionTime()) {
            String message = "Method " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " took " + executionTime + " ms";
            logger.info(message);
        }

        return result;
    }
}

