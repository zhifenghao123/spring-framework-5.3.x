package com.hao.springmvc.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.springmvc.model.Account;

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
    public String testDate(Date date) {
        return date.toString();
    }

    // 访问 http://localhost:8080/spring_hao_test_webmvc/testModelAttribute
    @RequestMapping({"testModelAttribute"})
    @ResponseBody
    public String testModelAttribute(HttpServletRequest request, Model model) {
        //return date.toString();
        return (String) model.getAttribute("user");
    }

    // 访问 http://localhost:8080/spring_hao_test_webmvc/addSession
    @RequestMapping({"addSession"})
    @ResponseBody
    public String addSession(HttpServletRequest request) {
        request.getSession().setAttribute("name", "haozhifeng");
        return "succ";
    }

    // 访问 http://localhost:8080/spring_hao_test_webmvc/testPathVariable/1
    @RequestMapping({"testPathVariable"})
    @ResponseBody
    public String testDate(@PathVariable("id") Integer id) {
        return id + "-haotest";
    }

    // 访问 http://localhost:8080/spring_hao_test_webmvc/testRequestParam?xname=hi
    // 访问 http://localhost:8080/spring_hao_test_webmvc/testRequestParam
    @RequestMapping({"testRequestParam"})
    @ResponseBody
    public String testRequestParam(@RequestParam(value = "xname", defaultValue = "zhifeng") String name) {
        return name + "-haotest";
    }

    // 访问 http://localhost:8080/spring_hao_test_webmvc/testRequestParamMap?date=2022-09-01&xxx=111
    @RequestMapping({"testRequestParamMap"})
    @ResponseBody
    public String testDate(@RequestParam Map map) {
        return map + "-haotest";
    }

    // 访问 http://localhost:8080/spring_hao_test_webmvc/queryAccountNew
    @RequestMapping({"queryAccountNew"})
    @ResponseBody
    public Account queryAccount() {
        Account account = new Account();
        account.setAccountId("111111111111d");
        account.setAccountName("haozhifeng");
        account.setAccountNo("hao000000");
        return account;
    }

}
