package com.hao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.springmybatis.HaoSpringMybatisImportBeanDefinitionRegistrator;
import com.springmybatis.HaoSpringMybatisScan;

/**
 * AppConfig class
 *
 * @author haozhifeng
 * @date 2023/06/03
 */
@ComponentScan("com.hao")
//@Import(HaoSpringMybatisImportBeanDefinitionRegistrator.class)
@HaoSpringMybatisScan
public class AppConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-cfg.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

}
