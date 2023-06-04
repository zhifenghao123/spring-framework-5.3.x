package com.springmybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * HaoSpringMybatisFactoryBean class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
public class HaoSpringMybatisFactoryBean implements FactoryBean {

    private SqlSession sqlSession;

    private Class mapperClass;

    public HaoSpringMybatisFactoryBean(Class mapperClass) {
        this.mapperClass = mapperClass;
    }

    @Autowired
    public void setSqlSession(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().addMapper(mapperClass);
        this.sqlSession = sqlSessionFactory.openSession();
    }

    @Override
    public Object getObject() throws Exception {
        return sqlSession.getMapper(mapperClass);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
