package com.hao.javaConfig1;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hao.javaConfig1.component.Comment;
import com.hao.javaConfig1.component.Order;
import com.hao.javaConfig1.component.Product;

/**
 * AnnotationConfigApplicationContextMain1 class
 * Bootstrapping @Configuration classes ：Via AnnotationConfigApplicationContext
 * 通过AnnotationConfigApplicationContext引导@Configuration配置类
 *
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class AnnotationConfigApplicationContextMain1 {
    public static void main(String[] args) {
        // AnnotationConfigApplicationContext annotationConfigApplicationContext = new
        // AnnotationConfigApplicationContext();
        // ctx.register(AppConfig.class);
        // ctx.refresh();

        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        Product product = annotationConfigApplicationContext.getBean(Product.class);
        Order order = annotationConfigApplicationContext.getBean(Order.class);
        Comment comment = annotationConfigApplicationContext.getBean(Comment.class);

        System.out.println("--------------------");
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = annotationConfigApplicationContext.getBeanDefinition(beanDefinitionName);
            if ( null != beanDefinition.getBeanClassName()) {
                System.out.println(beanDefinition.getBeanClassName());
            } else {
                System.out.println("工厂Bean（factoryBean##facotryMechod）：" + beanDefinition.getFactoryBeanName() + "##" + beanDefinition.getFactoryMethodName());
            }
        }
        System.out.println("--------------------");
        System.out.println(product);
        System.out.println(order);
        System.out.println(comment);
        System.out.println("--------------------");
        System.out.println("order.getProduct == comment.getProduct 为 ：" + (order.getProduct() == comment.getProduct()));

    }
}
