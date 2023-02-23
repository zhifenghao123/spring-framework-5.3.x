package com.hao.xml2;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hao.xml2.component.Order;
import com.hao.xml2.component.Product;

/**
 * ClassPathXmlApplicationContextMain2 class
 * 1、半配置半注解方式(对象属性用注解@Autowired配置，普通属性xml配置)
 * （1）注入一般的属性包括对象属性可以在bean的property属性中编写，但注入对象属性也可以不用在property中编写，可以通过注解的方式
 * （2）Spring 2.5 引入了 @Autowired 注释，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。
 *     构造函数包括无参构造函数、只含有要注入bean类型的构造函数，以及多参数构造函数上，(如果有多余的参数，则配置文件必须含对应参数的bean对象）
 * （3）@Autowired是根据类型来注入bean对象的，当相同类型的bean对象有多个时，可以和@Qualifier(“名字”)配合使用，
 *     @Qualifier 的标注对象是成员变量、方法入参、构造函数入参;
 * （4）Spring 是通过一个 BeanPostProcessor 对 @Autowired 进行解析，所以要让 @Autowired 起作用必须事先在 Spring
 *     容器中声明AutowiredAnnotationBeanPostProcessor Bean。
 *     其原理是：当 Spring 容器启动时，AutowiredAnnotationBeanPostProcessor 将扫描Spring 容器中所有 Bean，当发现 Bean 中拥
 *     有@Autowired 注释时就找到和其匹配（默认按类型匹配）的 Bean，并注入到对应的地方中去。
 *     本例中，在applicationContext-2.xml中，配置了AutowiredAnnotationBeanPostProcessor Bean
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class ClassPathXmlApplicationContextMain2 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("applicationContext-2.xml");
        Product product = xmlApplicationContext.getBean(Product.class);
        Order order = xmlApplicationContext.getBean(Order.class);
        System.out.println("--------------------");
        String[] beanDefinitionNames = xmlApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition =
                    xmlApplicationContext.getBeanFactory().getBeanDefinition(beanDefinitionName);
            System.out.println(beanDefinition.getBeanClassName());
        }
        System.out.println("--------------------");
        System.out.println(product);
        System.out.println(order);
        System.out.println("--------------------");
    }
}
