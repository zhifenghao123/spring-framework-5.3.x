package com.hao.javaConfig5;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.hao.javaConfig5.component.Order;
import com.hao.javaConfig5.component.Product;

/**
 * MyImportSelector class
 *
 * @author haozhifeng
 * @date 2023/02/27
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{Order.class.getName(), Product.class.getName()};
    }
}
