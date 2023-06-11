package com.hao.javaConfig8;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hao.javaConfig8.comPack.ComponentVO3;

/**
 * AnnotationConfigApplicationContextMain8 class
 *
 * @author haozhifeng
 * @date 2023/03/09
 */
public class AnnotationConfigApplicationContextMain8 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("--------------------");
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = annotationConfigApplicationContext.getBeanDefinition(beanDefinitionName);
            if ( null != beanDefinition.getBeanClassName()) {
                System.out.println(beanDefinition.getBeanClassName());
            } else {
                System.out.println(beanDefinition.getFactoryBeanName() + "##" + beanDefinition.getFactoryMethodName());
            }
        }
        System.out.println("--------------------");

        Object componentVO1 = annotationConfigApplicationContext.getBean("componentVO1");
        ComponentVO3 componentVO3 = annotationConfigApplicationContext.getBean("componentVO3", ComponentVO3.class);
        System.out.println("componentVO1.equals(componentVO3.getComponentVO1())" + componentVO1.equals(componentVO3.getComponentVO1()));

    }
}
