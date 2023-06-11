package com.hao.javaConfig5;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.hao.javaConfig5.component.User;

/**
 * MyDeferredImportSelector class
 *
 * @author haozhifeng
 * @date 2023/06/11
 */
public class MyDeferredImportSelector implements DeferredImportSelector {
    // 是通过Group（可能是自定义的MyDeferredImportSelectorGroup，
    // 也可能是Spring的ConfigurationClassParser#DefaultDeferredImportSelectorGroup）的方法调用的
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        System.out.println("MyDeferredImportSelector##selectImports");
        return new String[]{User.class.getName()};
    }

    // 实际上调用到的是这个方法，拿到实现了Group接口的类
    @Override
    public Class<? extends Group> getImportGroup() {
        System.out.println("MyDeferredImportSelector##getImportGroup");

        return MyDeferredImportSelectorGroup.class;
    }

    private static class MyDeferredImportSelectorGroup implements Group {
        List<Entry> entries = new ArrayList<>();


        @Override
        public void process(AnnotationMetadata metadata, DeferredImportSelector selector) {
            System.out.println("MyDeferredImportSelectorGroup##process");
            // selector是外部类的对象
            String[] selectImports = selector.selectImports(metadata);
            for (String selectImport : selectImports) {
                entries.add(new Entry(metadata, selectImport));
            }

        }

        @Override
        public Iterable<Entry> selectImports() {
            System.out.println("MyDeferredImportSelectorGroup##selectImports");

            return entries;
        }
    }
}
