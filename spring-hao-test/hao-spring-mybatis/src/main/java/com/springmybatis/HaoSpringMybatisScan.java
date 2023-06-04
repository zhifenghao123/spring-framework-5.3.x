package com.springmybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * HaoSpringMybatisScan class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HaoSpringMybatisImportBeanDefinitionRegistrator.class) // 在执行HaoSpringMybatisImportBeanDefinitionRegistrator
// 时，可以将应用上使用的HaoSpringMybatisScan注解值信息获取到
public @interface HaoSpringMybatisScan {

    String value() default "";
}
