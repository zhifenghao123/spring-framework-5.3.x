package com.hao.springboot.webserver.jetty;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * JettyCondition class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
public class JettyCondition implements Condition {

    private static String JETTY_CLASS = "org.eclipse.jetty.server.Server";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        try {
            context.getClassLoader().loadClass(JETTY_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
