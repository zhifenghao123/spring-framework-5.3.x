package com.hao.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * CityService class
 *
 * CityService 经过Spring事务增强（底层AOP）之后其实会成生一个代理类
 * class CityService$$EnhancerBySpringCGLIB extends CityService {
 *
 *      CityService target;
 *
 *      public void test() {
 *         // 执行事务切面逻辑
 *         // 1. 开启事务
 *         // 2. 事务管理器PlatformTransactionManager（有dataSource） 新建一个数据库连接conn
 *         ，并以dataSource为键，以conn为值，存入到ThreadLocal<Map<dataSource,conn>>
 *         // 3. conn.autocommit = false
 *         4. target.test()   //这里才实际是调用CityService普通对象.test()，底层执行jdbcTemplate方法时，
 *         // 会以jdbcTemplate的dataSource为键名从ThreadLocal<Map<dataSource,conn>>取数据库连接conn，
 *         // 如果没有获取到，则自己创建数据库连接(autocommit为true)并执行SQL
 *         // 为了避免CityService中的test000()方法在执行时遇到RuntimeException()
 *         // 正常回滚，就需要让事务管理器PlatformTransactionManager和jdbcTemplate的数据源相同,可以通过 在配置类上加@Configuration 借助代理类保持相同
 *
 *          5. conn.commit(); / conn.rollback();
 *
 *      }
 * }
 *
 * @author haozhifeng
 * @date 2023/06/03
 */
@Component
public class CityService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void test000() {
        jdbcTemplate.execute("insert into city(city_id,city_name,country_id) values(9999,'hao',8888)");
       throw new RuntimeException();
    }

    @Transactional
    public void test() {
        jdbcTemplate.execute("insert into city(city_id,city_name,country_id) values(9999,'hao',8888)");
        test2(); // 在调用CityService Bean的test()方法时，实际上是通过CityService代理对象的调用了Spirng CityService普通bean对象的test()方法，
        // 因此执行到此处时，实际上也是在执行 CityService普通bean对象的test()2方法，因此执行test2()方法时不会抛异常,也就是事务失效了
    }

    @Transactional(propagation = Propagation.NEVER)
    public void test2() {
        jdbcTemplate.execute("insert into city(city_id,city_name,country_id) values(9988,'hao',8877)");
    }

}
