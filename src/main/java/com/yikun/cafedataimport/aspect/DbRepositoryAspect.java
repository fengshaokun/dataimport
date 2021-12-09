package com.yikun.cafedataimport.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 15:59
 */
@Aspect
@Component
public class DbRepositoryAspect {

    private static  final Logger LOGGER = LoggerFactory.getLogger(DbRepositoryAspect.class);

   /* @Pointcut(value = "execution(public * com.yikun.cafedataimport.config(..))")*/
    public void    dbRepository(){}

/*    @Around(value = "dbRepository()")*/
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{

        String simpleName = joinPoint.getTarget().getClass().getSimpleName();
        try {
            return  joinPoint.proceed();
        } catch (Throwable throwable) {
            LOGGER.error("数据库发生异常 : className is {},exception is {}",simpleName,throwable);
            throw  throwable;
        }
    }
}
