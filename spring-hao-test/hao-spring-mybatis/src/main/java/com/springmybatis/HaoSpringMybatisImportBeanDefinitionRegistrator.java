package com.springmybatis;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

/**
 * HaoSpringMybatisImportBeanDefinitionRegistrator class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
public class HaoSpringMybatisImportBeanDefinitionRegistrator implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {

        Map<String, Object> annotationAttributes =
                importingClassMetadata.getAnnotationAttributes(HaoSpringMybatisScan.class.getName());
        String path = (String) annotationAttributes.get("value");

        HaoSpringMybatisScanner haoSpringMybatisScanner = new HaoSpringMybatisScanner(registry);
        haoSpringMybatisScanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                    throws IOException {
                return true;
            }
        });
        haoSpringMybatisScanner.scan(path);

    }
}
