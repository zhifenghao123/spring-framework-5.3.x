package com.hao.javaConfig8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.hao.javaConfig8.comPack.ComponentVO1;
import com.hao.javaConfig8.comPack.ComponentVO2;
import com.hao.javaConfig8.comPack.ComponentVO3;
import com.hao.javaConfig8.comPack2.Component2;
import com.hao.javaConfig8.comPack4.comSubPack1.Componenet41;

/**
 * AppConfig class
 *
 * @author haozhifeng
 * @date 2023/03/09
 */
@Configuration
@ComponentScan("com.hao.javaConfig8.comPack1")
public class AppConfig {

    //@Configuration
    @Import(Component2.class)
    public class AppConfigInner1{

    }

    @Bean
    public ComponentVO1 componentVO1() {
        return new ComponentVO1();
    }

    @Bean
    public ComponentVO3 componentVO3() {
        ComponentVO3 componentVO3 = new ComponentVO3();
        componentVO3.setComponentVO1(componentVO1());
        return componentVO3;
    }

    @Component
    @ComponentScan("com.hao.javaConfig8.comPack4")
    public class AppConfigInner2{
        @Bean
        public ComponentVO2 componentVO2() {
            return new ComponentVO2();
        }
    }



}
