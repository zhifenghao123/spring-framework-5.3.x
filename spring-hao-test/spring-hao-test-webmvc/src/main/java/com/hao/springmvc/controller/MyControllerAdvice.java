package com.hao.springmvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * MyControllerAdvice class
 *
 * @author haozhifeng
 * @date 2023/05/21
 */
@ControllerAdvice
//@SessionAttributes("name")
public class MyControllerAdvice {

    // @InitBinder 方法 可以定义在全局的@ControllerAdvice bean中，
    // 也可以定义在某一个Controller类中，如果定义在某一个Controller类中，只会对那个Controller类中的方法起作用
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, false));
    }

    /*@ModelAttribute("user")
    public String addString(@RequestParam("xxx") String value) {
        return value;
    }*/
}
