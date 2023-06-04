package com.hao.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * CityService class
 *
 * @author haozhifeng
 * @date 2023/06/03
 */
@Component
public class CityService2 {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CityServiceBase cityServiceBase;//这里注入的是CityServiceBase的事务aop后的代理对象

    @Transactional
    public void test() {
        jdbcTemplate.execute("insert into city(city_id,city_name,country_id) values(9999,'hao',8888)");
        cityServiceBase.test2(); // 执行的是代理对象的test2()方法
    }


}
