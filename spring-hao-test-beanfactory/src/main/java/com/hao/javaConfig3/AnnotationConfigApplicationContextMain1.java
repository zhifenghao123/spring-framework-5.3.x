package com.hao.javaConfig3;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hao.javaConfig3.component.Comment;
import com.hao.javaConfig3.component.Order;
import com.hao.javaConfig3.component.Product;

/**
 * AnnotationConfigApplicationContextMain1 class
 * 在Spring XML中使用@ImportResource注释
 * @Configuration类可以在Spring XML文件中声明为常规的Spring <bean>定义。还可以使用@ImportResource注释将Spring
 * XML配置文件导入@Configuration类。
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class AnnotationConfigApplicationContextMain1 {
    public static void main(String[] args) {
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
