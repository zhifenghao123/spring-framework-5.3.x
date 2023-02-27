package com.hao.javaConfig4;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hao.javaConfig4.component.Comment;
import com.hao.javaConfig4.component.Order;
import com.hao.javaConfig4.component.Product;

/**
 * AnnotationConfigApplicationContextMain4 class
 * @Import通过快速导入的方式实现把实例加入Spring的容器中。
 * 将实例注入Spring容器的方式有很多中，@Import注解功能相对比较强大，这个注解可以用于以更加灵活便捷的方式将Bean注入到Spring容器中。
 * (@Import注解可以用于导入第三方包 ，当然@Bean注解也可以，但是@Import注解快速导入的方式更加便捷)
 * @Import注解只能作用在类上
 * @Import有三种用法: (1)直接填class数组方式; (2)ImportSelector实现类方式【重点】;(3)ImportBeanDefinitionRegistrar实现类
 *
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
