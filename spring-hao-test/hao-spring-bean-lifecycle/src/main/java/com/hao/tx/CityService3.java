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
public class CityService3 {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CityService3 cityService3;

    @Transactional
    public void test() {
        jdbcTemplate.execute("insert into city(city_id,city_name,country_id) values(9999,'hao',8888)");
        cityService3.test2();
    }

    @Transactional(propagation = Propagation.NEVER)
    public void test2() {
        jdbcTemplate.execute("insert into city(city_id,city_name,country_id) values(9988,'hao',8877)");
    }

}
