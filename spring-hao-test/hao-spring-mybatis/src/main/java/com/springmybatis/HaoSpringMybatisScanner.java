package com.springmybatis;

import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * HaoSpringMybatisScanner class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
public class HaoSpringMybatisScanner extends ClassPathBeanDefinitionScanner {
    public HaoSpringMybatisScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        for (BeanDefinitionHolder bdh : beanDefinitionHolders) {
            BeanDefinition beanDefinition = bdh.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClassName(HaoSpringMybatisFactoryBean.class.getName());
        }
        return beanDefinitionHolders;
    }
}
