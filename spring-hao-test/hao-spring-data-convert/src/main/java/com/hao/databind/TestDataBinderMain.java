package com.hao.databind;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;

import com.hao.model.User;

/**
 * TestDataBinderMain class
 *
 * @author haozhifeng
 * @date 2023/05/30
 */
public class TestDataBinderMain {
    public static void main(String[] args) {
        testDataBinder();

        testWebDataBinder();

        testServletRequestDataBinder();
    }

    public static void testDataBinder() {
        User user = new User();

        Map<String, Object> input = new HashMap<>();
        input.put("name", "haozhifeng");
        //input.put("!name", "默认的名字hiahiahia"); // 无法像WebDataBinder起到设置默认值
        input.put("age", "29");
        input.put("hobbies", Arrays.asList("aaa", "bbb"));
        PropertyValues propertyValues = new MutablePropertyValues(input);

        DataBinder dataBinder = new DataBinder(user, "user");
        dataBinder.bind(propertyValues);

        System.out.println(user);

    }

    public static void testWebDataBinder() {
       User user = new User();
        WebDataBinder binder = new WebDataBinder(user, "userDto");

        // 设置属性（此处演示一下默认值）
        MutablePropertyValues pvs = new MutablePropertyValues();

        // 使用!来模拟各个字段手动指定默认值
        //pvs.add("name", "haozhifeng");
        pvs.add("!name", "默认的名字hiahiahia");
        pvs.add("age", 18);
        pvs.add("!age", 10); // 上面有确切的值了，默认值不会再生效

        binder.bind(pvs);
        System.out.println(user);
    }

    public static void testServletRequestDataBinder() {
        User user = new User();
        ServletRequestDataBinder binder = new ServletRequestDataBinder(user, "person");

        // 构造参数，此处就不用MutablePropertyValues，以HttpServletRequest的实现类MockHttpServletRequest为例吧
        MockHttpServletRequest request = new MockHttpServletRequest();
        // 模拟请求参数
        request.addParameter("name", "haozhifefeng");
        request.addParameter("age", "18");

        // flag不仅仅可以用true/false  用0和1也是可以的？
        request.addParameter("flag", "1");

        // 设置多值的
        request.addParameter("hobbies", "4", "2", "3", "1");
        // 给map赋值(Json串)
        // request.addParameter("map", "{'key1':'value1','key2':'value2'}"); // 这样可不行
        request.addParameter("map['key1']", "value1");
        request.addParameter("map['key2']", "value2");

        binder.bind(request);
        System.out.println(user);
    }
}
