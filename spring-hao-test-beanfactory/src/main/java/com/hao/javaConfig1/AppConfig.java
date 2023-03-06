package com.hao.javaConfig1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hao.javaConfig1.component.Comment;
import com.hao.javaConfig1.component.Order;
import com.hao.javaConfig1.component.Product;

/**
 * AppConfig class
 * Spring3.0开始，@Configuration用于定义配置类，定义的配置类可以替换xml文件，一般和@Bean注解联合使用。
 * @Configuration注解主要标注在某个类上，相当于xml配置文件中的<beans>
 * @Bean注解主要标注在某个方法上，相当于xml配置文件中的<bean>
 *
 * @Configuration注解的配置类有如下要求：
 * （1）@Configuration不可以是final类型；
 * （2）@Configuration不可以是匿名类；
 * （3）嵌套的configuration必须是静态类。
 *
 * SpringBoot社区推荐使用基于JavaConfig的配置形式，所以，启动类标注了@Configuration之后，本身其实也是一个IoC容器的配置类。
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
@Configuration
//@Configuration注解的proxyBeanMethods属性值默认为true，使用proxyBeanMethods=true可以保障调用此方法得到的对象是从容器中获取的而不是重新创建的
@ComponentScan(basePackages = "com.hao.javaConfig1.componentScan")
//@EnableAspectJAutoProxy//(proxyTargetClass = true)
public class AppConfig {
    @Bean
    Product product() {
        Product product = new Product();
        product.setProductId("javaConfig1_procuct_111");
        product.setProductName("orange");
        product.setPrice(3.5);
        return product;
    }

    @Bean
    Order order() {
        Order order = new Order();
        order.setUserAccountId("javaConfig1_userAccount_111");
        order.setOrderId("javaConfig1_orderId_111");
        order.setProduct(product());
        return order;
    }

    @Bean
    Comment comment() {
        Comment comment = new Comment();
        comment.setCommentId("javaConfig1_comment_111");
        comment.setCommentText("很棒");
        comment.setProduct(product());
        return comment;
    }
}
