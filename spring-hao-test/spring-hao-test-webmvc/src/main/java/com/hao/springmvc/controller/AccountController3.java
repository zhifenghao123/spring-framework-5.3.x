package com.hao.springmvc.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * AccountController3 class
 *
 * 这个类能做的事就相当于@Controller类中的一个@RequestMapping方法可以做的事
 *
 * @author haozhifeng
 * @date 2023/05/20
 */
@Component("/beanNameUrl")
public class AccountController3 {

}
/*public class AccountController3 implements Controller {
    @Override
    public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest request,
                                      javax.servlet.http.HttpServletResponse response) throws Exception {
        System.out.println("/beanNameUrl  处理 AccountController3");
        return null;
    }
}*/
