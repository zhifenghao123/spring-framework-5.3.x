package com.hao.springmvc.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AccountControllerTest class
 *
 * @author haozhifeng
 * @date 2023/05/21
 */
@Controller
public class AccountControllerTest {

    // 访问 http://localhost:8080/spring_hao_test_webmvc/testDate?date=2022-09-01&xxx=111
    @RequestMapping({"testDate"})
    @ResponseBody
    public String testDate(Date date, HttpServletRequest request, Model model) {
        //return date.toString();
        return (String) model.getAttribute("user");
    }

    // 访问 http://localhost:8080/spring_hao_test_webmvc/testDate?date=2022-09-01&xxx=111
    @RequestMapping({"addSession"})
    @ResponseBody
    public String addSession(HttpServletRequest request) {
        request.getSession().setAttribute("name", "haozhifeng");
        return "succ";
    }

}
