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
public class CityServiceBase {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional(propagation = Propagation.NEVER)
    public void test2() {
        jdbcTemplate.execute("insert into city(city_id,city_name,country_id) values(9988,'hao',8877)");
    }

}
