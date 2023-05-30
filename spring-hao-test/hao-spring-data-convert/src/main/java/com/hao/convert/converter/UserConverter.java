package com.hao.convert.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.hao.model.User;

/**
 * UserConverter class
 *
 * Converter的实现类举例：该接口Spring内部的实现也非常多，大多数都是以内部类的形式实现（因为它是一个@FunctionalInterface）
 * 比如：ObjectToStringConverter、StringToPropertiesConverter、StringToBooleanConverter等等
 * Converter接口非常的简单，所以除了SerializingConverter一个是外部类，我们可以拿来使用外，其余的都是Spring内部自己使用的。
 * 从此可以看出：此接口一般也用于我们自己去实现，即：自定义数据转换器。
 *
 * 备注：在Spring内部消息转换器的注册、使用一般都结合ConversionService这个接口
 *
 * @author haozhifeng
 * @date 2023/05/30
 */
public class UserConverter implements Converter<String, User> {
    @Override
    public User convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        String[] strings = StringUtils.delimitedListToStringArray(source, ":");
        User user = new User();
        user.setName(strings[0]);
        user.setAge(Integer.valueOf(strings[1]));
        return user;
    }

    @Override
    public <U> Converter<String, U> andThen(Converter<? super User, ? extends U> after) {
        return null;
    }
}
