package com.hao;

import org.springframework.context.annotation.Import;

import com.hao.springboot.HaoSpringApplication;
import com.hao.springboot.HaoSpringBootApplication;
import com.hao.springboot.webserver.WebServerAutoConfiguration;

/**
 * BootAppMain class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
@HaoSpringBootApplication
@Import(WebServerAutoConfiguration.class)
public class BootAppMain {
    public static void main(String[] args) {
        HaoSpringApplication.run(BootAppMain.class);
    }


}
