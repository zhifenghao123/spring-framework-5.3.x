package com.hao.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
@RestController
public class UserController {
    @RequestMapping("/user")
    String getUser() {
        return "haozhifefng";
    }
}
