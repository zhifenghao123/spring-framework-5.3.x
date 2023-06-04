package com.hao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hao.mapper.UserMapper;

/**
 * UserService class
 *
 * @author haozhifeng
 * @date 2023/06/03
 */
@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String getUser() {
        return userMapper.getUser();
    }

}
