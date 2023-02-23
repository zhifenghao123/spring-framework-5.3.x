package com.hao.xml5;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hao.xml5.component.Order;
import com.hao.xml5.component.Product;

/**
 * ClassPathXmlApplicationContextMain5 class
 * 4、使用@Component注解完成Bean的定义
 * （1）在前面几种方式中，可以通过 @Autowired 或 @Resource 在 Bean 类中使用自动注入功能，但是 Bean 还是在 XML 文件中通过 <bean> 进
 * 行定义。也就是说，在 XML 配置文件中定义Bean，通过@Autowired 或 @Resource 为 Bean 的成员变量、方法入参或构造函数入参提供自动注入的功能。
 * 能否也通过注解定义 Bean，从 XML 配置文件中完全移除 Bean 定义的配置呢？
 * 答案是肯定的，我们通过 Spring 2.5 提供的 @Component 注解就可以达到这个目标了。
 * （2）@Component 有一个可选的入参，用于指定 Bean 的名称，在 Boss 中，我们就将 Bean 名称定义为“boss”。
 * 一般情况下，Bean 都是 singleton 的，需要注入 Bean 的地方仅需要通过 byType 策略就可以自动注入了，所以大可不必指定 Bean 的名称。
 * （3）在使用 @Component 注解后，Spring 容器必须启用类扫描机制以启用注解驱动 Bean 定义和注解驱动 Bean 自动注入的策略。
 * Spring 2.5 对 context 命名空间进行了扩展（<context:component-scan base-package="com.hao"/>），提供了这一功能
 * （4）<context:component-scan/> 还允许定义过滤器将基包下的某些类纳入或排除。Spring 支持以下 4 种类型的过滤方式：注解、类名指定、
 * 正则表达式、AspectJ 表达式，例如
 * <context:component-scan base-package="com.hao">
 *     <context:include-filter type="regex"
 *         expression="com.hao.util..*"/>
 * </context:component-scan>
 * （5）值得注意的是 <context:component-scan/> 配置项不但启用了对类包进行扫描以实施注解驱动 Bean 定义的功能，同时还启用了注解驱动
 * 自动注入的功能（即还隐式地在内部注册了 AutowiredAnnotationBeanPostProcessor 和CommonAnnotationBeanPostProcessor），因此当
 * 使用 <context:component-scan/> 后，就可以将 <context:annotation-config/> 移除了。
 *
 * <context:annotation-config> 和 <context:component-scan>的区别
 * <context:annotation-config> 是用于激活那些已经在spring容器里注册过的bean（无论是通过xml的方式还是通过package sanning的方式）上面的注解，是一个注解处理工具。
 * <context:component-scan>除了具有<context:annotation-config>的功能之外，<context:component-scan>还可以在指定的package下扫描以及注册javabean 。

 *
 * （6）默认情况下通过 @Component 定义的 Bean 都是 singleton 的，如果需要使用其它作用范围的 Bean，可以通过 @Scope 注解来达到目标
 * （7）Spring 2.5 中除了提供 @Component 注解外，还定义了几个拥有特殊语义的注解，它们分别是：@Repository、@Service 和@Controller。
 * 在目前的 Spring 版本中，这 3 个注解和 @Component 是等效的，但是从注释类的命名上，很容易看出这 3 个注释分别和持久层、业务层和
 * 控制层（Web 层）相对应。虽然目前这 3 个注解和 @Component 相比没有什么新意，但 Spring 将在以后的版本中为它们添加特殊的功能。
 * 所以，如果 Web 应用程序采用了经典的三层分层结构的话，最好在持久层、业务层和控制层分别采用@Repository、@Service 和 @Controller
 * 对分层中的类进行注解，而用 @Component 对那些比较中立的类进行注解。
 *
 *
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class ClassPathXmlApplicationContextMain5 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("applicationContext-5.xml");
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
