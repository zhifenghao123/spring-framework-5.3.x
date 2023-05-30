package com.hao.model;

import java.io.Serializable;

/**
 * User class
 *
 * @author haozhifeng
 * @date 2023/05/30
 */
public class User implements Serializable {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
