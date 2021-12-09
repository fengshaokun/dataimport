package com.yikun.cafedataimport.aspect;

import com.yikun.cafedataimport.factory.IFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/12/3 15:40
 */
@Aspect
@Component
public class DataMappingFactoryAspect {
    private static  final Logger LOGGER = LoggerFactory.getLogger(DataMappingFactoryAspect.class);

    /*@Pointcut(value = "execution(public * com.yikun.cafedataimport.config(..))")*/
    public void    dataMapping(){}

    /*@Around(value = "dataMapping()")*/
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        Object target = joinPoint.getTarget();
        if (!(target instanceof IFactory)){return joinPoint.proceed();}
        String className = target.getClass().getSimpleName();
        try {
            Object result = joinPoint.proceed();
            Object[] args = joinPoint.getArgs();
            if (null == args || 2!=args.length){
                LOGGER.error("参数格式不对: className is {} ,methodName is createMediaFactory" ,className);
                return result;
            }
            List<?>originList = (List<?>) args[0];
            String dataSource = (String) args[1];
            List<?>mappedList = (List<?>) result;

            if (originList.size()!=mappedList.size()){
                LOGGER.error("数据长度异常: dataSource if {}",dataSource);
            }
            return result;
        }catch (Throwable e){
            LOGGER.error("数据转换发送异常: className is {},exception is {}",className,e);
            throw e;
        }
    }
}
