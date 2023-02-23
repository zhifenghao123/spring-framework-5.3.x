package com.hao.xml1;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hao.xml1.component.Order;
import com.hao.xml1.component.Product;

/**
 * ClassPathXmlApplicationContextMain1 class
 * 1、采用完全xml配置方式 配置Bean以及Bean之间依赖关系
 *
 * @author haozhifeng
 * @date 2023/02/23
 */
public class ClassPathXmlApplicationContextMain1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("applicationContext-1.xml");
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
