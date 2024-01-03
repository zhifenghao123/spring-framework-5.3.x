package com.hao.spring.main;

import com.hao.spring.aspect2.IPay;
import com.hao.spring.aspect2.IPayPlugin;
import com.hao.spring.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * ApplicationContextMain class
 *
 * @author haozhifeng
 * @date 2023/12/26
 */
public class ApplicationContextMain2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        IPay onlinePay = applicationContext.getBean("onlinePay", IPay.class);
        onlinePay.pay();

        IPayPlugin alipayPlugin = (IPayPlugin) onlinePay;
        alipayPlugin.payPlugin();
    }
}
