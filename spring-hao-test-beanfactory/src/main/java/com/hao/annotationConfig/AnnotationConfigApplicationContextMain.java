package com.hao.annotationConfig;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hao.xml1.component.Order;
import com.hao.xml1.component.Product;

/**
 * AnnotationConfigApplicationContextMain class
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class AnnotationConfigApplicationContextMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);
        Product product = annotationConfigApplicationContext.getBean(Product.class);
        Order order = annotationConfigApplicationContext.getBean(Order.class);

        System.out.println("--------------------");
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = annotationConfigApplicationContext.getBeanDefinition(beanDefinitionName);
            System.out.println(beanDefinition.getBeanClassName());
        }
        System.out.println("--------------------");
        System.out.println(product);
        System.out.println(order);
        System.out.println("--------------------");
    }
}
