package com.hao.javaConfig6;

import java.util.Map;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.hao.javaConfig6.component.Order;
import com.hao.javaConfig6.component.Product;
import com.hao.javaConfig6.component.Comment;

/**
 * MyImportSelector class
 *
 * @author haozhifeng
 * @date 2023/02/27
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> annotationAttributes =
                importingClassMetadata.getAnnotationAttributes(EnableMyImport.class.getName());
        //通过 不同type注入不同的实例到容器中
        if (annotationAttributes.get("type").equals(0)) {
            return new String[]{Order.class.getName(), Product.class.getName()};
        } else {
            return new String[]{Comment.class.getName(), Product.class.getName()};
        }
    }
}
