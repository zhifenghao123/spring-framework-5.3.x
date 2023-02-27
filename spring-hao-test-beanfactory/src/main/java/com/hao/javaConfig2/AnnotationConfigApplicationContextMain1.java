package com.hao.javaConfig2;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hao.javaConfig2.component.Comment;
import com.hao.javaConfig2.component.Order;
import com.hao.javaConfig2.component.Product;

/**
 * AnnotationConfigApplicationContextMain2 class
 * Bootstrapping @Configuration classes ：Via Spring <beans> XML
 * 通过Spring <beans> XML引导@Configuration配置类
 * 注意：对应的xml配置中，<context:annotation-config/>是必需的，以便启用ConfigurationClassPostProcessor和其他与注释相关的后期处理器，以便处理@Configuration类。
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class AnnotationConfigApplicationContextMain1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("applicationContext-javaConfig2.xml");
        Product product = xmlApplicationContext.getBean(Product.class);
        Order order = xmlApplicationContext.getBean(Order.class);
        Comment comment = xmlApplicationContext.getBean(Comment.class);

        System.out.println("--------------------");
        String[] beanDefinitionNames = xmlApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = xmlApplicationContext.getBeanFactory().getBeanDefinition(beanDefinitionName);
            if ( null != beanDefinition.getBeanClassName()) {
                System.out.println(beanDefinition.getBeanClassName());
            } else {
                System.out.println(beanDefinition.getFactoryBeanName() + "##" + beanDefinition.getFactoryMethodName());
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
