package com.hao.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * UserMapper class
 *
 * @author haozhifeng
 * @date 2023/06/04
 */
public interface UserMapper {
    @Select("select 'user'")
    public String getUser();
}
