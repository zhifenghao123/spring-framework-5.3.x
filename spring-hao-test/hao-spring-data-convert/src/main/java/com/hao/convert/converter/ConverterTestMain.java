package com.hao.convert.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.serializer.support.SerializingConverter;

import com.hao.model.User;

/**
 * ConverterTestMain class
 *
 * 总结
 * 1.Spring使用ConversionService来convert各种类型.默认提供的是DefaultConversionService.同时它实现了ConverterRegistry接口,
 * 所以也可以添加你自定义的converter.
 * 2.Spring提供了3种converter接口,分别是Converter,ConverterFactory和GenericConverter.一般用于1:1, 1:N, N:N的source->target类型转化.
 * 3.在DefaultConversionService内部3种converter都会转化成GenericConverter放到静态内部类Converters中.
 * 4.接口GenericConverter的内部类ConvertiblePair是source的class与target的Class的封装。GenericConversionService
 * 的静态内部类ConvertersForPair是多个converter对应的LinkedList的封装。。。GenericConversionService的静态内部类Converters中含有1个Map
 * <ConvertiblePair, ConvertersForPair>用来储存所有converter.
 *
 * 1个GenericConverter可以对应N个ConvertiblePair,1个ConvertiblePair对应的ConvertersForPair中也可以有N个GenericConverter.
 * Convertible：可转换的
 *
 * Spring为何要使用ConversionService替代PropertyEditor
 * 此处总结三个原因，供给大家参考：
 *
 * ConversionService功能更强大，支持的类型转换范围更广。
 * 1. 相比PropertyEditor只提供String<->Object的转换，ConversionService能够提供任意Object<->Object的转换。
 * ConverterFactory支持一整个class hierarchy的转换（也就是多态），PropertyEditor则不行
 * Java Bean这个规范最初是和Java GUI（Swing）一起诞生的，PropertyEditor接口里有大量和GUI相关的方法，显然已经过时了。
 * 1. Java Bean和POJO不是一个概念，Java Bean不仅有getter、setter，还有一系列和Java GUI配套的东西。
 *
 *
 * @author haozhifeng
 * @date 2023/05/30
 */
public class ConverterTestMain {
    public static void main(String[] args) {
        testSerializingConverter();

        testCustomizeConverter();

        testConversionService();

        testPropertyEditor();
    }

    public static void testSerializingConverter() {

        User user = new User();
        user.setName("haozhifeng");
        user.setAge(29);

        SerializingConverter serializingConverter = new SerializingConverter();
        byte[] convert = serializingConverter.convert(user);
        System.out.println(convert);

    }

    public static void testCustomizeConverter() {

        String userStr = "haozhifng:29";
        UserConverter userConverter = new UserConverter();
        User user = userConverter.convert(userStr);
        System.out.println(user);

    }

    public static void testConversionService() {
        String ageStr = "29";
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        Integer ageInt = defaultConversionService.convert(ageStr, Integer.TYPE);
        System.out.println(ageInt);

    }

    public static void testPropertyEditor() {
        String dateStr = "2023-05-11";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor customDateEditor = new CustomDateEditor(sdf, false);
        customDateEditor.setAsText(dateStr);
        System.out.println(customDateEditor.getJavaInitializationString() + " = " + customDateEditor.getValue());


    }
}
