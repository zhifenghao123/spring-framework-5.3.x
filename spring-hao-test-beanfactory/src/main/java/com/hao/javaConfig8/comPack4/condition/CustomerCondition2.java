package com.hao.javaConfig8.comPack4.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * CustomerCondition2 class
 *
 * @author haozhifeng
 * @date 2023/03/11
 */
public class CustomerCondition2 implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return true;
    }
}
