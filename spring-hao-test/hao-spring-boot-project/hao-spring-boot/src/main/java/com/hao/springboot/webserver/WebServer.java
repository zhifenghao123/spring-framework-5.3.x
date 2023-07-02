package com.hao.springboot.webserver;

import org.springframework.web.context.WebApplicationContext;

/**
 * WebServer class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
public interface WebServer {
    static final int DEFAULT_PORT = 8080;
    static final String CONTEXT_PATH = "/";
    static final String MAPPING_URL = "/*";

    public void start(WebApplicationContext applicationContext);
}
