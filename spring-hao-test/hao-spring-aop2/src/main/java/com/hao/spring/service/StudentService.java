package com.hao.spring.service;

import org.springframework.stereotype.Service;

/**
 * StudentService class
 *
 * @author haozhifeng
 * @date 2024/01/03
 */
@Service
public class StudentService {
    public void printStudentServiceInfo() {
        System.out.println("StudentService##printUser");
        System.out.println("studentService =" + this);
    }
}
