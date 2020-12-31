package com.mljr.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mljr.annotation.LogMonitor;
import com.mljr.ding.auth.DingAuth;
import com.mljr.ding.client.DingRobotService;
import com.mljr.ding.dto.builder.MarkdownDingRobotReqBuilder;
import com.mljr.ding.dto.req.MarkdownDingRobotReq;
import com.mljr.util.StringTools;
import com.mljr.util.TimeTools;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

/**
 * @description: LogMonitorAdvice
 * 业务逻辑：对方法的出参、入参进行日志输出
 * @Date : 2018/3/21 下午3:54
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 3)
public class LogMonitorAdvice {

    private static final String TITLE = "接口超时告警";

    private static final long MILLIS = 1000L;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DingRobotService dingRobotService;

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Pointcut("@annotation(com.mljr.annotation.LogMonitor)")
    private void annotationPoint() {
    }

    @Around("annotationPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceedResult;
        String target = null;
        long start = System.currentTimeMillis();
        Object[] jointPointArgs;
        String module = "";
        String action = "";
        int timeoutSecond = 5;
        long duration = 0;
        String durationMessage = null;
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Class<?> targetClass = joinPoint.getTarget().getClass();
            String className = targetClass.getName();
            target = className + "." + method.getName();
            jointPointArgs = joinPoint.getArgs();
            LogMonitor annotation = method.getAnnotation(LogMonitor.class);
            if (null != annotation) {
                module = annotation.value();
                action = annotation.action().value();
                timeoutSecond = annotation.timeoutSecond();
                if (StringTools.isNotEmpty(action)) {
                    action = MessageFormat.format(",action={0}", action);
                }
            }
            LOGGER.info("[Request]{}{},dto={}", module, action, JSON.toJSON(jointPointArgs));
            proceedResult = joinPoint.proceed();
            LOGGER.info("[Response]{}{},result={}", module, action, JSON.toJSON(proceedResult));
            duration = System.currentTimeMillis() - start;
            String params = JSONObject.toJSONString(jointPointArgs);
            durationMessage = TimeTools.spend(duration);
            try {
                if (duration != 0 && (duration > timeoutSecond * MILLIS)) {
                    LOGGER.warn("[Method Timeout] target={}, module={}{}, duration={}, threshold={}秒,args={}",target, module, action, durationMessage, timeoutSecond, params);
                    MarkdownDingRobotReq robotReq = new MarkdownDingRobotReqBuilder().title(TITLE)
                            .applicationName(applicationName)
                            .content(MessageFormat.format("[Method Timeout] target={0}, module={1}{2}, duration={3}, threshold={4}秒,args={5}", target, module, action, TimeTools.spend(duration), timeoutSecond,params)).build();
                    CompletableFuture.runAsync(() -> dingRobotService.sendMarkdown(DingAuth.TOKEN, robotReq));
                }
            } catch (Throwable e) {
                LOGGER.warn("[LogMonitor dingTalk warn error],module={}",module,e);
            }
        } finally {
            LOGGER.info("[Duration]{}{},target={},duration={}", module, action,target, durationMessage);
        }
        return proceedResult;
    }
}
