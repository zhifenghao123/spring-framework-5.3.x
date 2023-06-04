package com.hao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * AppConfig2 class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
@ComponentScan
@EnableTransactionManagement
@Configuration // 可以让AppConfig2为代理类
public class AppConfig2 {
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/hao_01");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
     }
}
