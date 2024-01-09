package com.fuyu.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *  自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AuToFillAspect {
    /**
     * 切入点
     */
    //  *.* 所有的类和所有的方法  .. 匹配任意参数     拦截添加了特定注解AutoFill的方法：  @annotation(com.sky.annotation.AutoFill)
    //同时满足两个条件 &&
    @Pointcut("execution(* com.fuyu.mapper.*.*(..)) && @annotation(com.fuyu.annotation.AutoFill))")
    public void autoFillPointCut(){
    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(){
        log.info("开始进行公共字段自动填充.....");
    }


}
