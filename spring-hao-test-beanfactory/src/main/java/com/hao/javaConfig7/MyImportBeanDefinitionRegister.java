package com.hao.javaConfig7;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.hao.javaConfig7.component.Product;

/**
 * MyImportBeanDefinitionRegister class
 *
 * @author haozhifeng
 * @date 2023/02/27
 */
public class MyImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //构建一个 BeanDefinition , Bean的类型为 UserConfig,这个Bean的属性username的值为后端元宇宙
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Product.class)
                .addPropertyValue("productId", "-11111")
                .addPropertyValue("productName", "苹果")
                .addPropertyValue("price", "120")
                .getBeanDefinition();
        //把 UserConfig 这个Bean的定义注册到容器中
        registry.registerBeanDefinition("product", beanDefinition);
    }
}
