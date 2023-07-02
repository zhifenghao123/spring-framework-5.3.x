package com.hao.springboot;

import java.util.Map;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.hao.springboot.webserver.WebServer;

/**
 * HaoSpringApplication class
 *
 * @author haozhifeng
 * @date 2023/07/01
 */
public class HaoSpringApplication {
    public static void run(Class clazz) {
        // 创建Spring容器
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(clazz);
        applicationContext.refresh();

        // 启动Tomcat
        //startTomcat(applicationContext);

        WebServer webServer = getWebServer(applicationContext);
        webServer.start(applicationContext);
    }

    private static WebServer getWebServer(WebApplicationContext applicationContext) {
        Map<String, WebServer> webServerBeans = applicationContext.getBeansOfType(WebServer.class);
        if (webServerBeans.isEmpty()) {
            throw new NullPointerException();
        }
        if (webServerBeans.size() > 1) {
            throw new IllegalStateException();
        }
        return webServerBeans.values().stream().findFirst().get();
    }
   /* private static void startTomcat(WebApplicationContext applicationContext) {
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(8080);

        Engine engine = new StandardEngine();
        engine.setDefaultHost("localhost");

        Host host = new StandardHost();
        host.setName("localhost");

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);


        String dispatcherServletName = "dispatcherServlet";
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        tomcat.addServlet(contextPath, dispatcherServletName, dispatcherServlet);
        context.addServletMappingDecoded("/*", dispatcherServletName);

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }*/
}
