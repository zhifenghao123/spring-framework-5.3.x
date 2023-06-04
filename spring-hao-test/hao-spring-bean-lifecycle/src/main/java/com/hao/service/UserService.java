package com.hao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserService class
 *
 * Spring中的一个bean，需要实例化得到一个对象，而实例化就需要用到构造方法, 并且一个对象中可能存在多个构造方法, 无参的,一个参数的, 多个参数的。
 * 那么在实例化对象时,Spring是如何从这些构造函数中选择出一个构造函数来创建实例的?
 * 这就涉及到Bean生命周期中的实例化阶段一个核心知识点- 推断构造方法
 * 1. 只有一个构造函数
 * 一般情况下,一个类只需要有一个构造函数就可以了,要么是无参的构造函数,要么是有参数的构造函数。
 * 如果只有一个无参的构造函数,那么在实例化阶段就只能使用这个无参构造方法了,但是如果只有一个有参的构造函数,那么实例化时能直接使用这个构造方法吗？
 * 要分情况讨论：
 * （1）使用AnnotationConfigApplicationContext,会使用这个构造方法进行实例化,那么Spring会根据构造方法的参数信息去寻找bean，
 * 然后传给构造方法, 前提是这个构造函数的构造参数都能在容器中找到,否则会报错
 * （2）使用ClassPathXmlApplicationContext，表示使用XML的方式来使用bean,要么在XML中指定构造方法的参数值(手动指定)，要么配置autowire=constructor让Spring自动去寻找bean做为构造方法参数值。
 *
 * 2. 多个构造函数
 * 如果有多个构造函数时, 也需要分两种情况来分析, 主要看多个构造函数中是否存在无参的构造函数
 * 一个类存在多个构造方法，那么Spring进行实例化之前，该如何去确定到底用哪个构造方法呢？
 * （1）如果开发者指定了想要使用的构造方法，那么就用这个构造方法
 *     开发者可以通过什么方式来指定使用哪个构造方法呢?
 *     1）使用ClassPathXmlApplicationContext,
 *     在XML中使用<constructor-arg>标签，这个标签表示构造方法参数，所以可以根据这个确定想要使用的构造方法的参数个数，从而确定想要使用的构造方法
 *     2）使用AnnotationConfigApplicationContext,
 *     通过@Autowired注解，@Autowired注解可以写在构造方法上，所以哪个构造方法上写了@Autowired注解，表示开发者想使用哪个构造方法。当然，它和第一个方式的不同点是，通过XML
 *     的方式，我们直接指定了构造方法的参数值，而通过@Autowired注解的方式，需要Spring通过byType+byName的方式去找到符合条件的bean作为构造方法的参数
 *     3）通过getBean()方法来指定构造参数, 然后通过构造参数来匹配使用哪一个构造函数
 *       见源码: AbstractApplicationContext.getBean()
 *          ## name: 是需要获取的Bean的名称
 *          ## args: 是构造参数,可以有多个,Spring可以通过args来匹配构造函数
 *          @Override
 *          public Object getBean(String name, Object... args) throws BeansException {
 * 	            assertBeanFactoryActive();
 * 	            return getBeanFactory().getBean(name, args);
 *          }
 *      4）手动通过BeanDefinitionBuilder来创建一个BeanDefinition,然后手动添加构造参数
 *      5）使用@Autowired来指定使用A与B两个构造函数, 表示开发者同时指定了两个构造函数, 程序在实例对象时,依然不知道选择哪一个,所以程序会报错;
 *      修改下,将B构造函数设置为@Autowired(required = false), A构造函数依旧使用@Autowired, 程序运行时依旧报错,无法实例对象
 *      再修改下,将A和B两个构造函数上都使用@Autowired(required = false), 发现程序运行成功,可以实例对象,并且根据A与B两个构造函数的先后顺序,会使用不同的构造函数来实例对象
 * （2）如果开发者没有指定想要使用的构造方法，则看开发者有没有让Spring自动去选择构造方法
 *      对于这一点，只能用在ClassPathXmlApplicationContext，因为通过AnnotationConfigApplicationContext没有办法去直接指定某个bean
 *      可以自动去选择构造方法，而通过ClassPathXmlApplicationContext可以在XML中指定某个bean的autowire元素为constructor
 *      ，虽然这个属性表示通过构造方法自动注入，所以需要自动的去选择一个构造方法进行自动注入，因为是构造方法，所以顺便是进行实例化。
 *      AnnotationConfigApplicationContext虽然无法直接,但是可以间接的使用手动的方式, 指定使用AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR模式
 * （3）如果开发者也没有让Spring自动去选择构造方法，则Spring利用无参构造方法，如果没有无参构造方法，则报错
 *      对于这一点,分两种情况论述:
 *      ① 使用ClassPathXmlApplicationContext, 在XML中不使用<constructor-arg>标签去指定使用哪个构造函数,
 *      也不在<bean>标签中使用autowire=constructor的形式去指定通过构造方法自动注入, 那么就需要看对象中是否有无参的构造函数,如果有直接使用无参构造函数来实例对象,没有,则直接报错
 *      ② 使用AnnotationConfigApplicationContext,无法去指定某个bean可以自动去选择构造方法,如果此时对象中也未指定使用哪个构造函数, 也要看是否有无参的构造函数,有就直接使用,没有就直接报错
 *
 *    总结下构造函数的使用与Spring如何来选择构造函数去实例对象的, 大致总结如下四点:
 *  默认情况下,用无参的构造方法, 如果只有一个构造方法,就使用那一个
 *  开发者构造方法的入参值,通过getBean()或者beanDefinition.getConstructorArgumentValues()方式指定构造参数,那就使用匹配到的构造方法
 *  开发者想让Spring自动选择构造方法以及构造方法的入参值,可以使用autowire="constructor", 或者AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR
 *  ( 补充: autowire表示使用注入模型,使用constructor,表示把选者构造函数得权力交给了Spring,并且如果选择了有参得构造函数,构造参数也是通过Spring去寻找, 寻找得过程是先byType后byName)
 *  开发者通过@Autowired注解指定了某个构造方法,但是希望Spring自动去找该构造方法的入参值
 * 原文链接：https://blog.csdn.net/weixin_44167408/article/details/122968126
 *
 *
 *  @Bean的情况，spring会把@Bean修饰的方法解析成beanDefinition
 *  （1）如果方法是静态的，那么解析出来的beanDefinition中
 *      factoryBeanName位appConfig对应的beanName,如appConfig
 *      factoryMethodName为对应的方法名，
 *      factoryClass为appconfig.class
 * （2）如果方法不是静态的，那么解析出来的beandefiniton中
 *      factoryBeanName为null
 *      facotryMethodName为对应的方法名
 *      facotryClass为appconfig.class
 *
 * @author haozhifeng
 * @date 2023/06/03
 */
@Component
public class UserService {

    private OrderService orderService;

    /*public UserService(){
        System.out.println("instantiate userService with Contructor: public UserService()");
    }
*/
    // 先byType，再byName
    /*public UserService(OrderService orderService){
        System.out.println("instantiate userService with Contructor: UserService(OrderService orderService)");
        this.orderService = orderService;
        System.out.printf("111");

    }*/

    /*@Autowired
    public UserService(OrderService orderService){
        System.out.println("instantiate userService with Contructor: UserService(OrderService orderService)");
        this.orderService = orderService;
        System.out.printf("111");

    }*/

    public UserService(OrderService xxx){
        System.out.println("instantiate userService with Contructor: UserService(OrderService xxx)");
        this.orderService = xxx;
    }

    /*@Autowired
    public UserService(OrderService xxx){
        System.out.println("instantiate userService with Contructor: UserService(OrderService xxx)");
        this.orderService = xxx;
    }*/

    /*public UserService(OrderService orderService, OrderService orderService1){
        System.out.println("instantiate userService with Contructor: UserService(OrderService orderService, OrderService orderService1))");
        this.orderService = orderService;
    }
*/

    public void test() {
        System.out.println(orderService);
    }
}
