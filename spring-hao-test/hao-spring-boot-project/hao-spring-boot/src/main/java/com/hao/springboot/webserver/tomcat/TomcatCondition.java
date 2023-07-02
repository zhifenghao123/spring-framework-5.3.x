package com.hao.springboot.webserver.tomcat;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * TomcatCondition class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
public class TomcatCondition implements Condition {
    private static String TOMCAT_CLASS = "org.apache.catalina.startup.Tomcat";
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        try {
            context.getClassLoader().loadClass(TOMCAT_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
