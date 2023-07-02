package com.hao.springboot.webserver.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.hao.springboot.webserver.WebServer;

/**
 * JettyWebServer class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
public class JettyWebServer implements WebServer {

    @Override
    public void start(WebApplicationContext applicationContext) {
        System.out.println("启动Jetty");
        startJetty(applicationContext);
        System.out.println("启动Jetty成功");
    }

    private static void startJetty(WebApplicationContext applicationContext) {
        Server server = new Server(DEFAULT_PORT);

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(CONTEXT_PATH);

        String dispatcherServletName = "dispatcherServlet";
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        handler.addServlet(new ServletHolder(dispatcherServlet), MAPPING_URL);
        handler.addEventListener(new ContextLoaderListener(applicationContext));


        server.setHandler(handler);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
