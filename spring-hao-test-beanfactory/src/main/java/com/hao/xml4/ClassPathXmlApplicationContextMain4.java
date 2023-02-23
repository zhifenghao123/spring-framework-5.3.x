package com.hao.xml4;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hao.xml4.component.Order;
import com.hao.xml4.component.Product;

/**
 * ClassPathXmlApplicationContextMain4 class
 * 4、半配置半注解方式(对象属性用注解@Resource或@Autowired配置，普通属性xml配置，但使用 <context:annotation-config/> 简化配置)
 * （1）Spring 2.1 添加了一个新的 context 的 Schema 命名空间，该命名空间对注解驱动、属性文件引入、加载期织入等功能提供了便捷的配置。
 *    我们知道注解本身是不会做任何事情的，它仅提供元数据信息。要使元数 据信息真正起作用，必须让负责处理这些元数据的处理器工作起来。
 * （2）而我们前面所介绍的 AutowiredAnnotationBeanPostProcessor 和 CommonAnnotationBeanPostProcessor 就是处理这些注释元数据的处理器。
 *    但是直接在 Spring 配置文件中定义这些 Bean 显得比较笨拙。Spring 为我们提供了一种方便的注册这些BeanPostProcessor 的方式，
 *    这就是 <context:annotation-config/>
 * （3）<context:annotationconfig/> 将隐式地向 Spring 容器注册AutowiredAnnotationBeanPostProcessor、
 *    CommonAnnotationBeanPostProcessor、PersistenceAnnotationBeanPostProcessor 以及equiredAnnotationBeanPostProcessor
 *    这 4 个 BeanPostProcessor。
 * （4）在配置文件中使用 context 命名空间之前，必须在 <beans> 元素中声明 context 命名空间。
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class ClassPathXmlApplicationContextMain4 {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("applicationContext-4.xml");
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

        xmlApplicationContext.destroy();// 关闭 Spring 容器，以触发 Bean 销毁方法的执行

    }
}
