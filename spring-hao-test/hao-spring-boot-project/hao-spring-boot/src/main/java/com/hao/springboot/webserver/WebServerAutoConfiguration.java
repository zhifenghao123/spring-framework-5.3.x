package com.hao.springboot.webserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.hao.springboot.webserver.jetty.JettyCondition;
import com.hao.springboot.webserver.jetty.JettyWebServer;
import com.hao.springboot.webserver.tomcat.TomcatCondition;
import com.hao.springboot.webserver.tomcat.TomcatWebServer;

/**
 * WebServerAutoConfiguration class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
@Configuration
public class WebServerAutoConfiguration {
    @Bean
    @Conditional(TomcatCondition.class)
    public TomcatWebServer tomcatWebServer(){
        return new TomcatWebServer();
    }

    @Bean
    @Conditional(JettyCondition.class)
    public JettyWebServer jettyWebServer() {
        return new JettyWebServer();
    }
}
