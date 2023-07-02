package com.hao.springboot.webserver.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.hao.springboot.webserver.WebServer;

/**
 * TomcatWebServer class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
public class TomcatWebServer implements WebServer {
    @Override
    public void start(WebApplicationContext applicationContext) {
        System.out.println("启动Tomcat");
        startTomcat(applicationContext);
        System.out.println("启动Tomcat成功");
    }

        private static void startTomcat(WebApplicationContext applicationContext) {
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(DEFAULT_PORT);

        Engine engine = new StandardEngine();
        engine.setDefaultHost("localhost");

        Host host = new StandardHost();
        host.setName("localhost");

        //String contextPath = "";
        Context context = new StandardContext();
        context.setPath(CONTEXT_PATH);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);


        String dispatcherServletName = "dispatcherServlet";
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        tomcat.addServlet(CONTEXT_PATH, dispatcherServletName, dispatcherServlet);
        context.addServletMappingDecoded(MAPPING_URL, dispatcherServletName);

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
