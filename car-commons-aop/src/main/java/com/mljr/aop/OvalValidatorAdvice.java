package com.mljr.aop;

import com.alibaba.fastjson.JSON;
import com.lyqc.base.common.Result;
import com.lyqc.base.enums.RemoteEnum;
import com.mljr.annotation.OvalValidator;
import com.mljr.util.CollectionsTools;
import com.mljr.util.StringTools;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;

/**
 * @description: 基于oval的注解的DTO约束校验
 * <pre>
 *     如果基于oval，需要支持javascript表达式，需要引入
 *     <!-- https://mvnrepository.com/artifact/org.mozilla/rhino -->
 *     <dependency>
 *         <groupId>org.mozilla</groupId>
 *         <artifactId>rhino</artifactId>
 *         <version>1.7R4</version>
 *     </dependency>
 * </pre>
 * @Date : 2018/6/2 下午2:32
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Aspect
@Component
public class OvalValidatorAdvice {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Validator validator;

    @Pointcut("@annotation(com.mljr.annotation.OvalValidator)")
    public void validator() {
    }

    @Around("validator()")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OvalValidator annotation = method.getAnnotation(OvalValidator.class);
        if (annotation == null) {
            return joinPoint.proceed();
        }
        String module = annotation.value();
        String action = "";
        action = annotation.action().value();
        if(StringTools.isNotEmpty(action)){
            action = MessageFormat.format(",action={0}",action);
        }
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String className = targetClass.getName();
        String target = className + "." + method.getName();
        Object args[] = joinPoint.getArgs();
        log.info("[OvalValidator Request]{}{},target={},dto={}",module,action,target,JSON.toJSON(args));
        if (args == null || args.length == 0) {
            log.warn("该方法缺少校验参数 ");
            return joinPoint.proceed();
        }
        List<ConstraintViolation> violations = validator.validate(args[0]);
        if (CollectionsTools.isNotEmpty(violations)) {
            return Result.fail(RemoteEnum.ERROR_WITH_EMPTY_PARAM.getIndex(), violations.get(0).getMessage());
        }
        return joinPoint.proceed();
    }
}
