package com.hao.xml3;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hao.xml3.component.Order;
import com.hao.xml3.component.Product;

/**
 * ClassPathXmlApplicationContextMain5 class
 * 1、半配置半注解方式(对象属性用注解@Resource配置，普通属性xml配置)
 * （1）Spring 不但支持自己定义的 @Autowired 的注释，还支持几个由 JSR-250 规范定义的注释，它们分别是 @Resource、@PostConstruct以及 @PreDestroy。
 * （2）@Resource 的作用相当于 @Autowired，只不过 @Autowired 按 byType 自动注入，而 @Resource 默认按 byName 自动注入罢了。
 *  @Resource有两个属性是比较重要的，分别是 name 和 type，Spring 将 @Resource 注释的 name 属性解析为 Bean 的名字，
 *  而 type 属性则解析为 Bean 的类型。所以如果使用 name 属性，则使用byName 的自动注入策略，而使用 type 属性时则使用 byType 自动注入策略。
 *  如果既不指定 name 也不指定 type 属性，这时将通过反射机制使用 byName 自动注入策略。
 * （3）Resource 注释类位于 Spring 发布包的 lib/j2ee/common-annotations.jar 类包中，因此在使用之前必须将其加入到项目的类库中。
 * （4）要让 JSR-250 的注释生效，除了在 Bean 类中标注这些注释外，还需要在 Spring 容器中注册一个负责处理这些注释的BeanPostProcessor：
 *   <bean class="org.springframework.context.annotation.CommonAnnotationBeanPostProcessor"/>
 *   CommonAnnotationBeanPostProcessor 实现了 BeanPostProcessor 接口，它负责扫描使用了 JSR-250 注释的 Bean，并对它们进行相应的操作。
 *
 * （5）@PostConstruct 和 @PreDestroy
 * Spring 容器中的 Bean 是有生命周期的，Spring 允许在 Bean 在初始化完成后以及 Bean 销毁前执行特定的操作，您既可以通过
 * 实现 InitializingBean/DisposableBean接口来定制初始化之后 / 销毁之前的操作方法，也可以通过 <bean> 元素
 * 的 init-method/destroy-method 属性指定初始化之后 / 销毁之前调用的操作方法
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class ClassPathXmlApplicationContextMain3 {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("applicationContext-3.xml");
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
