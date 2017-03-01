package fox.alex.robots.util.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fox on 01.03.17.
 */

@Aspect
public class LogAspect {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @AfterThrowing(pointcut = "execution(* fox.alex.robots..*.*(..)) || execution(* java..*.*(..)) || execution(* org.springframework..*.*(..))", throwing = "error")
    public void writeLogException(JoinPoint joinPoint, Throwable error){
        LOG.warn(joinPoint.getSourceLocation().getWithinType().getSimpleName() + ": method " + joinPoint.getSignature().getName() + ": message " + error.getMessage(), error);
    }

}
