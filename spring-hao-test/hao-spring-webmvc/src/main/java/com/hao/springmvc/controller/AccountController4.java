package com.hao.springmvc.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * AccountController4 class
 *
 * @author haozhifeng
 * @date 2023/05/21
 */
@Component("/controllerInterfaceTest")
public class AccountController4  implements Controller {
    @Override
    public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest request,
                                      javax.servlet.http.HttpServletResponse response) throws Exception {
        System.out.println("Controller接口 实现  处理 AccountController3");
        return null;
    }
}
