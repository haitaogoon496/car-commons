package com.mljr.aop;

import lombok.extern.slf4j.Slf4j;
import net.sf.oval.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 基于Oval注解约束配置
 * <pre>
 *     如果基于oval，需要支持javascript表达式，需要引入
 *     <!-- https://mvnrepository.com/artifact/org.mozilla/rhino -->
 *     <dependency>
 *         <groupId>org.mozilla</groupId>
 *         <artifactId>rhino</artifactId>
 *         <version>1.7R4</version>
 *     </dependency>
 * </pre>
 * @Date : 2018/10/8 下午7:30
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Configuration
@Slf4j
public class OvalValidatorConfig {

    @Bean
    public Validator ovalValidatorInit(){
        Validator validator = new Validator();
        log.info("OvalValidatorConfig initializing done.");
        return validator;
    }
}